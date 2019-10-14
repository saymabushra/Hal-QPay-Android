package com.haltechbd.app.android.qpay.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

public class PrinterSettings {

	// font size and style
	public static final byte FONT_24PX = (byte) 0x01;
	public static final byte FONT_32PX = (byte) 0x00;
	public static final byte FONT_24PX_UNDERLINE = (byte) 0x81;
	public static final byte FONT_32PX_UNDERLINE = (byte) 0x80;
	public static final byte FONT_48PX_HEIGHT = (byte) 0x12;
	public static final byte FONT_48PX_HEIGHT_UNDERLINE = (byte) 0x92;
	public static final byte FONT_48PX_WIDTH = (byte) 0x21;
	public static final byte FONT_48PX_WIDTH_UNDERLINE = (byte) 0xA1;
	public static final byte FONT_64PX_HEIGHT = (byte) 0x10;
	public static final byte FONT_64PX_HEIGHT_UNDERLINE = (byte) 0x90;
	public static final byte FONT_64PX_WIDTH = (byte) 0x20;
	public static final byte FONT_64PX_WIDTH_UNDERLINE = (byte) 0xA0;
	public static final byte FONT_64PX = (byte) 0x30;
	public static final byte FONT_64PX_UNDERLINE = (byte) 0xB0;
	public static final byte FONT_48PX = (byte) 0x31;
	public static final byte FONT_48PX_UNDERLINE = (byte) 0xB1;

	// font alignment
	public static final byte Align_LEFT = (byte) 0x30;
	public static final byte Align_CENTER = (byte) 0x31;
	public static final byte Align_RIGHT = (byte) 0x32;

	// the command type
	public final static byte FONT_DEFAULT = 0;
	public static byte fontSize = FONT_DEFAULT;
	public final static byte LANGUAGE_ENGLISH = 0;

	// add decode and encode
	public final static byte B_SOF = (byte) 0xc0;
	public final static byte B_EOF = (byte) 0xc1;
	public final static byte B_TRANSFER = (byte) 0x7D;
	public final static byte B_XOR = (byte) 0x20;
	public final static byte B_ESC = (byte) 0x1B;

	private BluetoothSocket mSocket;
	private OutputStream mOutputStream;
	private ConnectTask mConnectTask;
	private P25ConnectionListener mListener;

	private boolean mIsConnecting = false;
	private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

	
	
	public static byte[] convertPrintData(String str, int offset, int length,
			byte languageSet, byte fontSet, byte align, byte linespace) {

		byte[] buffer = null;
		buffer = new byte[length];
		System.arraycopy(str.getBytes(), offset, buffer, 0, length);

		byte[] lang = null;
		if (languageSet != LANGUAGE_ENGLISH) {
			lang = new byte[] { B_ESC, (byte) 0x4B, (byte) 0x31, B_ESC,
					(byte) 0x52, languageSet };
		}

		byte[] font = null;
		byte[] fontalign = null;
		byte[] fontlinespace = null;

		if (fontSet != FONT_DEFAULT) {

			font = new byte[] { B_ESC, (byte) 0x21, fontSet };
			fontalign = new byte[] { B_ESC, (byte) 0x61, align };
			fontlinespace = new byte[] { B_ESC, (byte) 0x33, linespace };

		}

		byte[] formate = concatByteArray(lang, font, fontalign, fontlinespace);
		return concatByteArray(formate, buffer);
	}

	
	
	private static byte[] concatByteArray(byte[] a, byte[] b, byte c[], byte[] d) {

		if (a == null && b == null && c == null && d == null)
			return null;

		int aL = (a == null ? 0 : a.length);
		int bL = (b == null ? 0 : b.length);
		int cL = (c == null ? 0 : c.length);
		int dL = (d == null ? 0 : d.length);

		Log.i("a length", aL + "");
		Log.i("b length", bL + "");
		Log.i("c length", cL + "");
		Log.i("d length", dL + "");

		int len = aL + bL + cL + dL;
		byte[] result = new byte[len];

		Log.i("len", len + "");

		if (a != null)
			System.arraycopy(a, 0, result, 0, aL);
		if (b != null)
			System.arraycopy(b, 0, result, aL, bL);
		if (c != null)
			System.arraycopy(c, 0, result, aL + bL, cL);
		if (d != null)
			System.arraycopy(d, 0, result, aL + bL + cL, dL);
		return result;
	}

	
	
	private static byte[] concatByteArray(byte[] a, byte[] b) {
		if (a == null && b == null)
			return null;

		int aL = (a == null ? 0 : a.length);
		int bL = (b == null ? 0 : b.length);

		if (bL == 0)
			return a;
		int len = aL + bL;
		byte[] result = new byte[len];

		if (a != null)
			System.arraycopy(a, 0, result, 0, aL);
		if (b != null)
			System.arraycopy(b, 0, result, aL, bL);

		return result;
	}

	
	public PrinterSettings(P25ConnectionListener listener) {
		mListener = listener;
	}

	public boolean isConnecting() {
		return mIsConnecting;
	}

	public boolean isConnected() {
		return (mSocket == null) ? false : true;
	}

	public void connect(BluetoothDevice device) throws Exception {
		if (mIsConnecting && mConnectTask != null) {
			throw new Exception("Connection in progress");
		}

		if (mSocket != null) {
			throw new Exception("Socket already connected");
		}

		(mConnectTask = new ConnectTask(device)).execute();
	}

	public void disconnect() throws Exception {
		if (mSocket == null) {
			throw new Exception("Socket is not connected");
		}

		try {
			mSocket.close();

			mSocket = null;

			mListener.onDisconnected();
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}

	public void cancel() throws Exception {
		if (mIsConnecting && mConnectTask != null) {
			mConnectTask.cancel(true);
		} else {
			throw new Exception("No connection is in progress");
		}
	}

	public void sendData(byte[] msg) throws Exception {
		if (mSocket == null) {
			throw new Exception(
					"Socket is not connected, try to call connect() first");
		}

		try {
			mOutputStream.write(msg);
			mOutputStream.flush();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public interface P25ConnectionListener {
		public abstract void onStartConnecting();

		public abstract void onConnectionCancelled();

		public abstract void onConnectionSuccess();

		public abstract void onConnectionFailed(String error);

		public abstract void onDisconnected();
	}

	public class ConnectTask extends AsyncTask<URL, Integer, Long> {
		BluetoothDevice device;
		String error = "";

		public ConnectTask(BluetoothDevice device) {
			this.device = device;
		}

		protected void onCancelled() {
			mIsConnecting = false;

			mListener.onConnectionCancelled();
		}

		protected void onPreExecute() {
			mListener.onStartConnecting();

			mIsConnecting = true;
		}

		protected Long doInBackground(URL... urls) {
			long result = 0;

			try {
				mSocket = device.createRfcommSocketToServiceRecord(UUID
						.fromString(SPP_UUID));

				mSocket.connect();

				mOutputStream = mSocket.getOutputStream();

				result = 1;
			} catch (IOException e) {
				e.printStackTrace();

				error = e.getMessage();
			}

			return result;
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Long result) {
			mIsConnecting = false;

			if (mSocket != null && result == 1) {
				mListener.onConnectionSuccess();
			} else {
				mListener.onConnectionFailed("Connection failed " + error);
			}
		}
	}
}

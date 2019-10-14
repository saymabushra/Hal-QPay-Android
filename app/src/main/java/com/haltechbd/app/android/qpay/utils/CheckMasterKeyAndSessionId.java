package com.haltechbd.app.android.qpay.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CheckMasterKeyAndSessionId {



	public static String strUrl = GlobalData.getStrUrl().replaceAll(" ","%20");
	public static String strNamespace = GlobalData.getStrNamespace().replaceAll(" ","%20");

	// Check Master Key
	// Check Master Key
	// Check Master Key
	public static String checkMasterKey(String strEncryptMasterKey, String strEncryptUserId) {
		String METHOD_NAME = "QPAY_CheckMasterKey";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_CheckMasterKey";
		SoapObject request = new SoapObject(strNamespace, METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("MasterKey");
		masterKey.setValue(strEncryptMasterKey);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNO");
		encryptAccountNumber.setValue(strEncryptUserId);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		Log.v("myApp:", request.toString());
		envelope.implicitTypes = true;
		Object objCheckMasterKey = null;
		String strCheckMasterKey = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(strUrl, 3000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objCheckMasterKey = envelope.getResponse();
			strCheckMasterKey = objCheckMasterKey.toString();
			Log.v("Check Master Key: ", strCheckMasterKey);
		} catch (Exception exception) {
			Log.e("Check Master Key: ", "Error!!!");
		}

		return strCheckMasterKey;

	}


	// Method retrieving Session ID
	// Method retrieving Session ID
	// Method retrieving Session ID
	public static String getSessionId(String strEncryptAccountNumber, String strUser,String strDeviceID) {
		String METHOD_NAME = "QPAY_GetSessionID";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetSessionID";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNO");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(strUser);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(strDeviceID);
		masterKey.setType(String.class);
		request.addProperty(masterKey);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		Log.v("myApp:", request.toString());
		envelope.implicitTypes = true;
		Object objSessionId = null;
		String strSessionId = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 3000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objSessionId = envelope.getResponse();
			strSessionId = objSessionId.toString();

			Log.v("Session ID: ", strSessionId);
		} catch (Exception exception) {
			Log.e("Session ID: ", "Error!!!");
		}

		return strSessionId;

	}

	// Check Session ID
	// Check Session ID
	// Check Session ID
	public static String checkSessionId(String strSessionId,String UserID,String DeviceID) {
		String METHOD_NAME = "QPAY_CheckSessionID";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_CheckSessionID";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo sessionId = new PropertyInfo();
		sessionId.setName("E_Session");
		sessionId.setValue(strSessionId);
		sessionId.setType(String.class);
		request.addProperty(sessionId);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(UserID);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(DeviceID);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objCheckSessionId = null;
		String strCheckSessionId = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 2000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objCheckSessionId = envelope.getResponse();
			strCheckSessionId = objCheckSessionId.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strCheckSessionId;

	}


//	public static Boolean checkMasterKeyAndSessionId() {
//		boolean isSessionIdAndMasterKeyOk = false;
//		String strCheckMasterKey = CheckMasterKeyAndSessionId.checkMasterKey(GlobalData.getStrMasterKey(), GlobalData.getStrEncryptUserId());
//		String strCheckSessionId = CheckMasterKeyAndSessionId.checkSessionId(GlobalData.getStrSessionId());
//		if (strCheckMasterKey.equalsIgnoreCase("Right key")
//				&& GlobalData.getStrSessionId().equalsIgnoreCase(strCheckSessionId)) {
//			isSessionIdAndMasterKeyOk = true;
//		} else {
//			isSessionIdAndMasterKeyOk = false;
//		}
//
//		return isSessionIdAndMasterKeyOk;
//
//	}
}

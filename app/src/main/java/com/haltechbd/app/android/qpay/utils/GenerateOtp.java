package com.haltechbd.app.android.qpay.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GenerateOtp {

	public static String getGenerateOtp(String strEncryptAccountNumber, String strEncryptPin, String strMasterKey) {
		String METHOD_NAME = "GenerateOTP";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/GenerateOTP";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo enctryptAccountNumber = new PropertyInfo();
		enctryptAccountNumber.setName("AgentNo");
		enctryptAccountNumber.setValue(strEncryptAccountNumber);
		enctryptAccountNumber.setType(String.class);
		request.addProperty(enctryptAccountNumber);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("PIN");
		encryptPin.setValue(strEncryptPin);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("strMasterKey");
		encryptMasterKey.setValue(strMasterKey);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		Log.v("myApp:", request.toString());
		envelope.implicitTypes = true;
		Object objGenerateOtp = null;
		String strEncryptOtp = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objGenerateOtp = envelope.getResponse();
			strEncryptOtp = objGenerateOtp.toString();
			Log.v("Agent Location: ", "Success...");
		} catch (Exception exception) {
			Log.e("Agent Location(Error): ", "Fail...");
		}
		return strEncryptOtp;
	}

}

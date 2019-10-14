package com.haltechbd.app.android.qpay.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Registration {

	public static String checkToken(String strEncryptAccountNumber, String strEncryptToken) {
		String METHOD_NAME = "QPAY_CheckToken";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_CheckToken";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNO");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(GlobalData.getStrDeviceId());
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptToken = new PropertyInfo();
		encryptToken.setName("E_Token");
		encryptToken.setValue(strEncryptToken);
		encryptToken.setType(String.class);
		request.addProperty(encryptToken);

		PropertyInfo securityKey = new PropertyInfo();
		securityKey.setName("Name");
		securityKey.setValue(GlobalData.getStrAccountHolderName());
		securityKey.setType(String.class);
		request.addProperty(securityKey);

		PropertyInfo Mobileno = new PropertyInfo();
		Mobileno.setName("MobileNo");
		Mobileno.setValue(GlobalData.getStrMobileNo());
		Mobileno.setType(String.class);
		request.addProperty(Mobileno);

		PropertyInfo Email = new PropertyInfo();
		Email.setName("EmailAddress");
		Email.setValue(GlobalData.getStrEmailAddress());
		Email.setType(String.class);
		request.addProperty(Email);


		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objCheckToken = null;
		String strCheckToken = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objCheckToken = envelope.getResponse();
			strCheckToken = objCheckToken.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strCheckToken;

	}

	public static String setUserId(String strEncryptUserId, String strEncryptAccountNumber, String strDeviceID) {
		String METHOD_NAME = "QPAY_SetUserID";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_SetUserID";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(strEncryptUserId);
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_Account_No");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(strDeviceID);
		masterKey.setType(String.class);
		request.addProperty(masterKey);


		PropertyInfo securityKey = new PropertyInfo();
		securityKey.setName("Name");
		securityKey.setValue(GlobalData.getStrAccountHolderName());
		securityKey.setType(String.class);
		request.addProperty(securityKey);

		PropertyInfo Mobileno = new PropertyInfo();
		Mobileno.setName("MobileNo");
		Mobileno.setValue(GlobalData.getStrMobileNo());
		Mobileno.setType(String.class);
		request.addProperty(Mobileno);

		PropertyInfo Email = new PropertyInfo();
		Email.setName("EmailAddress");
		Email.setValue(GlobalData.getStrEmailAddress());
		Email.setType(String.class);
		request.addProperty(Email);


		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objSetUserIdResponse = null;
		String strSetUserId = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objSetUserIdResponse = envelope.getResponse();
			strSetUserId = objSetUserIdResponse.toString();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strSetUserId;

	}

	public static String setPin(String strEncryptPin, String strEncryptAccountNumber, String struserID) {
		String METHOD_NAME = "QPAY_SetPIN";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_SetPIN";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("E_PIN");
		encryptPin.setValue(strEncryptPin);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_Account_No");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("UserID");
		masterKey.setValue(struserID);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo deviceid = new PropertyInfo();
		deviceid.setName("DeviceID");
		deviceid.setValue(GlobalData.getStrDeviceId());
		deviceid.setType(String.class);
		request.addProperty(deviceid);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objSetPinResponse = null;
		String strSetPin = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objSetPinResponse = envelope.getResponse();
			strSetPin = objSetPinResponse.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strSetPin;

	}

	public static String setTransactionPin(String strEncryptPin, String strEncryptAccountNumber, String struserID) {
		String METHOD_NAME = "QPAY_Set_Transaction_PIN";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Set_Transaction_PIN";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("E_PIN");
		encryptPin.setValue(strEncryptPin);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_Account_No");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("UserID");
		masterKey.setValue(struserID);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo deviceid = new PropertyInfo();
		deviceid.setName("DeviceID");
		deviceid.setValue(GlobalData.getStrDeviceId());
		deviceid.setType(String.class);
		request.addProperty(deviceid);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objSetPinResponse = null;
		String strSetPin = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objSetPinResponse = envelope.getResponse();
			strSetPin = objSetPinResponse.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strSetPin;

	}
}

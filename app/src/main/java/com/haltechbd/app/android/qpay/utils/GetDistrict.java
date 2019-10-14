package com.haltechbd.app.android.qpay.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetDistrict {
	public static String getDistrict(String strEncryptAccountNumber,String strEncryptPin) {
		String METHOD_NAME = "QPAY_KYC_Get_DISTRICT";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Get_DISTRICT";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_Get_DISTRICT
//		AccountNo
//		PIN
//		strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("E_PIN");
		encryptPin.setValue(strEncryptPin);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("UserID");
		masterKey.setValue(GlobalData.getStrUserId());
		masterKey.setType(String.class);
		request.addProperty(masterKey);


		PropertyInfo device = new PropertyInfo();
		device.setName("DeviceID");
		device.setValue(GlobalData.getStrDeviceId());
		device.setType(String.class);
		request.addProperty(device);


		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objServerResponse = null;
		String strServerResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objServerResponse = envelope.getResponse();
			strServerResponse = objServerResponse.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strServerResponse;
	}

	public static String getBank(String strEncryptAccountNumber) {
		String METHOD_NAME = "QPAY_GetBank_List";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetBank_List";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_Get_DISTRICT
//		AccountNo
//		PIN
//		strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNO");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);



		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("UserID");
		masterKey.setValue(GlobalData.getStrUserId());
		masterKey.setType(String.class);
		request.addProperty(masterKey);


		PropertyInfo device = new PropertyInfo();
		device.setName("DeviceID");
		device.setValue(GlobalData.getStrDeviceId());
		device.setType(String.class);
		request.addProperty(device);


		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objServerResponse = null;
		String strServerResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objServerResponse = envelope.getResponse();
			strServerResponse = objServerResponse.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strServerResponse;
	}

}

package com.haltechbd.app.android.qpay.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetAllMerchant {
	public static String getAllMerchant(String strEncryptAccountNumber,String strSearch) {
		String METHOD_NAME = "QPAY_Get_MerchantList";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Get_MerchantList";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_Get_MerchantList
//		AccountNo:
//		strMasterKey:

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("E_Search");
		encryptPin.setValue(strSearch);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);


		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(GlobalData.getStrUserId());
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(GlobalData.getStrDeviceId());
		masterKey.setType(String.class);
		request.addProperty(masterKey);
		envelope.dotNet = true;

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objGetAllMerchant = null;
		String strGetAllMerchantResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objGetAllMerchant = envelope.getResponse();
			strGetAllMerchantResponse = objGetAllMerchant.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strGetAllMerchantResponse;

	}

	public static String SelMerchantlocation(String strEncryptAccountNumber,String strE_lat,String strE_Long) {
		String METHOD_NAME = "QPAY_SetMerchantLocation";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_SetMerchantLocation";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_Get_MerchantList
//		AccountNo:
//		strMasterKey:

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_strMerchantAccountNo");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("E_Lat");
		encryptPin.setValue(strE_lat);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);

		PropertyInfo locationlong = new PropertyInfo();
		locationlong.setName("E_Long");
		locationlong.setValue(strE_Long);
		locationlong.setType(String.class);
		request.addProperty(locationlong);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(GlobalData.getStrUserId());
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(GlobalData.getStrDeviceId());
		masterKey.setType(String.class);
		request.addProperty(masterKey);
		envelope.dotNet = true;

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objGetAllMerchant = null;
		String strGetAllMerchantResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objGetAllMerchant = envelope.getResponse();
			strGetAllMerchantResponse = objGetAllMerchant.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strGetAllMerchantResponse;

	}

}

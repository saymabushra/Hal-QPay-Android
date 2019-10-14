package com.haltechbd.app.android.qpay.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetAllFavoriteList {
	public static String getAllFavoriteList(String strEncryptAccountNumber) {
		String METHOD_NAME = "QPAY_Get_ALL_Fav_List";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Get_ALL_Fav_List";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_Get_ALL_Fav_List
//		AccountNo:
//		strMasterKey:

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

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

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objGetAllFavoriteList = null;
		String strGetAllFavoriteListResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objGetAllFavoriteList = envelope.getResponse();
			strGetAllFavoriteListResponse = objGetAllFavoriteList.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return strGetAllFavoriteListResponse;

	}

	public static String deleteFavoriteList(String strEncryptAccountNumber, String strEncryptCaption) {
		String METHOD_NAME = "QPAY_Delete_Fav_List";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Delete_Fav_List";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//                                QPAY_Delete_Fav_List
//                                AccountNo
//                                Caption
//                                strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptCaption = new PropertyInfo();
		encryptCaption.setName("E_Caption");
		encryptCaption.setValue(strEncryptCaption);
		encryptCaption.setType(String.class);
		request.addProperty(encryptCaption);

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

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objDeleteFavoriteList = null;
		String strDeleteFavoriteListResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objDeleteFavoriteList = envelope.getResponse();
			strDeleteFavoriteListResponse = objDeleteFavoriteList.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strDeleteFavoriteListResponse;
	}


	public static String GetNotification_BroadCast(String AccountID,String PIN,String strUser, String strDeviceiD) {
		String METHOD_NAME = "QPAY_Get_BroadCast_Notification";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Get_BroadCast_Notification";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//                                QPAY_Delete_Fav_List
//                                AccountNo
//                                Caption
//                                strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(AccountID);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptCaption = new PropertyInfo();
		encryptCaption.setName("E_PIN");
		encryptCaption.setValue(PIN);
		encryptCaption.setType(String.class);
		request.addProperty(encryptCaption);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(strUser);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(strDeviceiD);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objDeleteFavoriteList = null;
		String strDeleteFavoriteListResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 2000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objDeleteFavoriteList = envelope.getResponse();
			strDeleteFavoriteListResponse = objDeleteFavoriteList.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strDeleteFavoriteListResponse;
	}

	public static String GetNotification(String AccountID,String PIN,String strUser, String strDeviceiD) {
		String METHOD_NAME = "QPAY_Get_Notification";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Get_Notification";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//                                QPAY_Delete_Fav_List
//                                AccountNo
//                                Caption
//                                strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(AccountID);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptCaption = new PropertyInfo();
		encryptCaption.setName("E_PIN");
		encryptCaption.setValue(PIN);
		encryptCaption.setType(String.class);
		request.addProperty(encryptCaption);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(strUser);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(strDeviceiD);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objDeleteFavoriteList = null;
		String strDeleteFavoriteListResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objDeleteFavoriteList = envelope.getResponse();
			strDeleteFavoriteListResponse = objDeleteFavoriteList.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strDeleteFavoriteListResponse;
	}

	public static String UpdateNotification(String AccountID,String PIN,String strENotifyid,String strUser, String strDeviceiD) {
		String METHOD_NAME = "QPAY_Update_Notification";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Update_Notification";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//                                QPAY_Delete_Fav_List
//                                AccountNo
//                                Caption
//                                strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(AccountID);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptCaption = new PropertyInfo();
		encryptCaption.setName("E_PIN");
		encryptCaption.setValue(PIN);
		encryptCaption.setType(String.class);
		request.addProperty(encryptCaption);

		PropertyInfo notifyid = new PropertyInfo();
		notifyid.setName("E_NotificationID");
		notifyid.setValue(strENotifyid);
		notifyid.setType(String.class);
		request.addProperty(notifyid);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(strUser);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(strDeviceiD);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objDeleteFavoriteList = null;
		String strDeleteFavoriteListResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objDeleteFavoriteList = envelope.getResponse();
			strDeleteFavoriteListResponse = objDeleteFavoriteList.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strDeleteFavoriteListResponse;
	}

	public static String DeleteNotification(String AccountID,String PIN,String strENotifyid,String strUser, String strDeviceiD) {
		String METHOD_NAME = "QPAY_Delete_Notification";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Delete_Notification";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//                                QPAY_Delete_Fav_List
//                                AccountNo
//                                Caption
//                                strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AccountNo");
		encryptAccountNumber.setValue(AccountID);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptCaption = new PropertyInfo();
		encryptCaption.setName("E_PIN");
		encryptCaption.setValue(PIN);
		encryptCaption.setType(String.class);
		request.addProperty(encryptCaption);

		PropertyInfo notifyid = new PropertyInfo();
		notifyid.setName("E_NotificationID");
		notifyid.setValue(strENotifyid);
		notifyid.setType(String.class);
		request.addProperty(notifyid);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(strUser);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(strDeviceiD);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objDeleteFavoriteList = null;
		String strDeleteFavoriteListResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objDeleteFavoriteList = envelope.getResponse();
			strDeleteFavoriteListResponse = objDeleteFavoriteList.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strDeleteFavoriteListResponse;
	}

}

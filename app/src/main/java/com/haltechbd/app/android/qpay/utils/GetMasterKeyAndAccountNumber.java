package com.haltechbd.app.android.qpay.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetMasterKeyAndAccountNumber {
    private static String strUrl = GlobalData.getStrUrl().replaceAll(" ", "%20");
    private static String strNamespace = GlobalData.getStrNamespace().replaceAll(" ", "%20");

    // Get Master Key
    // Get Master Key
    // Get Master Key
    public static String getMasterKeyAndAccountNumberByCompositeKey(String strDeviceId, String Name,String MobileNo,String EmailAddress) {
        String METHOD_NAME = "QPAY_GetMasterKeyAndAcctIDByCompositeKey";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetMasterKeyAndAcctIDByCompositeKey";
        SoapObject request = new SoapObject(strNamespace, METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName("Device_ID");
        deviceId.setValue(strDeviceId);
        deviceId.setType(String.class);
        request.addProperty(deviceId);

        PropertyInfo securityKey = new PropertyInfo();
        securityKey.setName("Name");
        securityKey.setValue(Name);
        securityKey.setType(String.class);
        request.addProperty(securityKey);

        PropertyInfo Mobileno = new PropertyInfo();
        Mobileno.setName("MobileNo");
        Mobileno.setValue(MobileNo);
        Mobileno.setType(String.class);
        request.addProperty(Mobileno);

        PropertyInfo Email = new PropertyInfo();
        Email.setName("EmailAddress");
        Email.setValue(EmailAddress);
        Email.setType(String.class);
        request.addProperty(Email);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objGetMasterKeyByDeviceId = null;
        String strMasterKey = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(strUrl, 1000000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objGetMasterKeyByDeviceId = envelope.getResponse();
            strMasterKey = objGetMasterKeyByDeviceId.toString();
            Log.v("Master Key: ", strMasterKey);
        } catch (Exception exception) {
            Log.e("Master Key: ", "Error!!!");
        }

        return strMasterKey;
    }

    // Get Master Key
    // Get Master Key
    // Get Master Key
    public static String getMasterKeyByUserId(String strUserId, String strKey) {
        String METHOD_NAME = "QPAY_GetMasterKey_ByUserID";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetMasterKey_ByUserID";
        SoapObject request = new SoapObject(strNamespace, METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo userId = new PropertyInfo();
        userId.setName("UserID");
        userId.setValue(strUserId);
        userId.setType(String.class);
        request.addProperty(userId);

        PropertyInfo key = new PropertyInfo();
        key.setName("key");
        key.setValue(strKey);
        key.setType(String.class);
        request.addProperty(key);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objGetMasterKeyByUserId = null;
        String strMasterKey = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(strUrl, 2000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objGetMasterKeyByUserId = envelope.getResponse();
            strMasterKey = objGetMasterKeyByUserId.toString();
            Log.v("Master Key: ", strMasterKey);
        } catch (Exception exception) {
            Log.e("Master Key: ", "Error!!!");
        }

        return strMasterKey;
    }

    // Get Master Key
    // Get Master Key
    // Get Master Key
    public static String getMasterKeyAndAccountNumberByDeviceId(String strDeviceId, String strSecurityKey) {
        String METHOD_NAME = "QPAY_GetMasterKeyAndAcctIDByDeviceID";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetMasterKeyAndAcctIDByDeviceID";
        SoapObject request = new SoapObject(strNamespace, METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName("Device_ID");
        deviceId.setValue(strDeviceId);
        deviceId.setType(String.class);
        request.addProperty(deviceId);

        PropertyInfo securityKey = new PropertyInfo();
        securityKey.setName("Securitykey");
        securityKey.setValue(strSecurityKey);
        securityKey.setType(String.class);
        request.addProperty(securityKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objGetMasterKeyByDeviceId = null;
        String strMasterKey = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(strUrl, 1000000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objGetMasterKeyByDeviceId = envelope.getResponse();
            strMasterKey = objGetMasterKeyByDeviceId.toString();
            Log.v("Master Key: ", strMasterKey);
        } catch (Exception exception) {
            Log.e("Master Key: ", "Error!!!");
        }

        return strMasterKey;
    }


    // get encrypted Account no And MAster key
    public static String getEncryptAccountNumberAndMasterKeyByQrCode(String strEncryptQrCodeContent) {
        String METHOD_NAME = "QPAY_Get_Account_BY_QR_CARD";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Get_Account_BY_QR_CARD";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo encryptQrCodeContent = new PropertyInfo();
        encryptQrCodeContent.setName("strRequest");
        encryptQrCodeContent.setValue(strEncryptQrCodeContent);
        encryptQrCodeContent.setType(String.class);
        request.addProperty(encryptQrCodeContent);


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
        Object objAccountNumberByQrCode = null;
        String strAccountNumberByQrCodeResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 3000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objAccountNumberByQrCode = envelope.getResponse();
            strAccountNumberByQrCodeResponse = objAccountNumberByQrCode.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return strAccountNumberByQrCodeResponse;

    }


}

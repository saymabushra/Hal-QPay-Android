package com.haltechbd.app.android.qpay.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetResponse {
    // Method retrieving MYCash Server Response
    // Method retrieving MYCash Server Response
    // Method retrieving MYCash Server Response
    public static String getResponse(String strEncryptMitUserName, String strEncryptMitPin, String strEncryptReferenceId, String strMasterKey) {
        String METHOD_NAME = "QPAY_GetResponse";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetResponse";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

//        AccountNo:
//        PIN:
//        RefID:
//        strMasterKey:

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo encryptMitUserName = new PropertyInfo();
        encryptMitUserName.setName("AccountNo");
        encryptMitUserName.setValue(strEncryptMitUserName);
        encryptMitUserName.setType(String.class);
        request.addProperty(encryptMitUserName);

        PropertyInfo encryptMitPin = new PropertyInfo();
        encryptMitPin.setName("PIN");
        encryptMitPin.setValue(strEncryptMitPin);
        encryptMitPin.setType(String.class);
        request.addProperty(encryptMitPin);

        PropertyInfo encryptReferenceId = new PropertyInfo();
        encryptReferenceId.setName("RefID");
        encryptReferenceId.setValue(strEncryptReferenceId);
        encryptReferenceId.setType(String.class);
        request.addProperty(encryptReferenceId);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("strMasterKey");
        masterKey.setValue(strMasterKey);
        masterKey.setType(String.class);
        request.addProperty(masterKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objMyCashResponse = null;
        String strMyCashResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objMyCashResponse = envelope.getResponse();
            strMyCashResponse = objMyCashResponse.toString();
            Log.v("MyCashResponse: ", strMyCashResponse);

            if (strMyCashResponse.equalsIgnoreCase("0001")) {
                strMyCashResponse = "Request Submit successfully";
            } else if (strMyCashResponse.equalsIgnoreCase("0002")) {
                strMyCashResponse = "Transaction failed";
            } else if (strMyCashResponse.equalsIgnoreCase("0003")) {
                strMyCashResponse = "Wallet Id is not correct format";
            } else if (strMyCashResponse.equalsIgnoreCase("0004")) {
                strMyCashResponse = "Not a Customer Wallet";
            } else if (strMyCashResponse.equalsIgnoreCase("0005")) {
                strMyCashResponse = "If user name and password is incorrect";
            } else if (strMyCashResponse.equalsIgnoreCase("0006")) {
                strMyCashResponse = "Customer Wallet/PIN No. Not Correct";
            } else if (strMyCashResponse.equalsIgnoreCase("0007")) {
                strMyCashResponse = "OTP is expired";
            } else if (strMyCashResponse.equalsIgnoreCase("0008")) {
                strMyCashResponse = "Merchant is not correct";
            } else if (strMyCashResponse.equalsIgnoreCase("0009")) {
                strMyCashResponse = "Not enough balance";
            } else if (strMyCashResponse.equalsIgnoreCase("0010")) {
                strMyCashResponse = "Unauthorized Domain";
            } else if (strMyCashResponse.equalsIgnoreCase("0011")) {
                strMyCashResponse = "Technical Error";
            } else if (strMyCashResponse.equalsIgnoreCase("0012")) {
                strMyCashResponse = "Request ID is not correct";
            } else if (strMyCashResponse.equalsIgnoreCase("0013")) {
                strMyCashResponse = "Unauthorized terminal";
            } else if (strMyCashResponse.equalsIgnoreCase("0024")) {
                strMyCashResponse = "Excceds Funds Available";
            } else if (strMyCashResponse.equalsIgnoreCase("0050")) {
                strMyCashResponse = "Request Time out";
            } else {
                strMyCashResponse = objMyCashResponse.toString();
            }
        } catch (Exception exception) {
            Log.e("MyCashResponse: ", "Error!!!");
            strMyCashResponse = "Error!!!";
        }

        return strMyCashResponse;

    }
}

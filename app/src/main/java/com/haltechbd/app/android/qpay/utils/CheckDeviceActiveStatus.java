package com.haltechbd.app.android.qpay.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CheckDeviceActiveStatus {
    public static String checkDeviceActiveStatus(String strDeviceId) {
        String METHOD_NAME = "QPAY_DeviceActiveStatusCheck";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_DeviceActiveStatusCheck";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName("strTernimalSerial");
        deviceId.setValue(strDeviceId);
        deviceId.setType(String.class);
        request.addProperty(deviceId);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objDeviceActiveStatusResponse = null;
        String strDeviceActiveStatus = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 5000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objDeviceActiveStatusResponse = envelope.getResponse();
            strDeviceActiveStatus = objDeviceActiveStatusResponse.toString();
        } catch (Exception exception) {
            Log.e("Session ID: ", "Error!!!");
        }

        return strDeviceActiveStatus;

    }

    public static String checkDeviceIdByAccountNumber(String strUserId, String strDeviceId, String strMasterKey) {
        String METHOD_NAME = "QPAY_CheckDeviceIDWithAccountNo";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_CheckDeviceIDWithAccountNo";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo userId = new PropertyInfo();
        userId.setName("UserID");
        userId.setValue(strUserId);
        userId.setType(String.class);
        request.addProperty(userId);

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName("DeviceID");
        deviceId.setValue(strDeviceId);
        deviceId.setType(String.class);
        request.addProperty(deviceId);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("E_AccountNO");
        masterKey.setValue(strMasterKey);
        masterKey.setType(String.class);
        request.addProperty(masterKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objDeviceActiveStatusResponse = null;
        String strDeviceActiveStatus = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 3000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objDeviceActiveStatusResponse = envelope.getResponse();
            strDeviceActiveStatus = objDeviceActiveStatusResponse.toString();
        } catch (Exception exception) {
            Log.e("Session ID: ", "Error!!!");
        }

        return strDeviceActiveStatus;

    }

    public static String checkAccountActiveStatus(String strEncryptAccountNumber, String struserid,String strDeviceId) {
        String METHOD_NAME = "QPAY_Account_Status_Check";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Account_Status_Check";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_Account_Status_Check
//		Account_No:
//		MasterKey:

        PropertyInfo encryptAccountNumber = new PropertyInfo();
        encryptAccountNumber.setName("E_Account_No");
        encryptAccountNumber.setValue(strEncryptAccountNumber);
        encryptAccountNumber.setType(String.class);
        request.addProperty(encryptAccountNumber);

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName("Device_ID");
        deviceId.setValue(strDeviceId);
        deviceId.setType(String.class);
        request.addProperty(deviceId);

        PropertyInfo userId = new PropertyInfo();
        userId.setName("UserID");
        userId.setValue(struserid);
        userId.setType(String.class);
        request.addProperty(userId);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.implicitTypes = true;
        Object objCheckAccountActiveStatus = null;
        String strCheckAccountActiveStatusResponse = "";


        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 2000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objCheckAccountActiveStatus = envelope.getResponse();
            strCheckAccountActiveStatusResponse = objCheckAccountActiveStatus.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return strCheckAccountActiveStatusResponse;

    }

    public static  String GetAccountList(String strEncryptAccountNo,String StrUserId,String strDeviceID)
    {
        String mStrServerResponse="";
        try
        {
            String METHOD_NAME = "QPAY_Get_Account_List";
            String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Get_Account_List";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo encryptPin = new PropertyInfo();
            encryptPin.setName("E_AccountNO");
            encryptPin.setValue(strEncryptAccountNo);
            encryptPin.setType(String.class);
            request.addProperty(encryptPin);

            PropertyInfo encryptUserId = new PropertyInfo();
            encryptUserId.setName("UserID");
            encryptUserId.setValue(StrUserId);
            encryptUserId.setType(String.class);
            request.addProperty(encryptUserId);

            PropertyInfo masterKey = new PropertyInfo();
            masterKey.setName("DeviceID");
            masterKey.setValue(strDeviceID);
            masterKey.setType(String.class);
            request.addProperty(masterKey);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            envelope.implicitTypes = true;
            Object objLoginResponse = null;


            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 2000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objLoginResponse = envelope.getResponse();
            String strLoginResponse = objLoginResponse.toString();
            if(strLoginResponse.equalsIgnoreCase("No data found"))
            {
                mStrServerResponse ="No Account Found found";
            }
            else
            {
                if (strLoginResponse.contains("*")) {
                    mStrServerResponse=strLoginResponse;

                }
                else
                {
                    mStrServerResponse ="No Account Found found";
                }

            }
        }
        catch (Exception ex)
        {

        }
        return  mStrServerResponse;

    }

    public static  String AddLinkAccount(String strAccountNo,String strBankID,String strAccountType,String strOther_Bank_Acc_No,
                                      String strAccountName,String strDistrictID,String strLinkAccBankBranch)
    {
        String mStrServerResponse="";
        try
        {
            EncryptionDecryption encryptDecrypt = new EncryptionDecryption();

            String EncryptAccountNo = encryptDecrypt.Encrypt(strAccountNo,  GlobalData.getStrSessionId());
            String EncryptBankID = encryptDecrypt.Encrypt(strBankID, GlobalData.getStrSessionId());
            String EncryptstrAccountType = encryptDecrypt.Encrypt(strAccountType,  GlobalData.getStrSessionId());

            String EncryptstrOther_Bank_Acc_No = encryptDecrypt.Encrypt(strOther_Bank_Acc_No,  GlobalData.getStrSessionId());
            String EncptystrAccountName = encryptDecrypt.Encrypt(strAccountName,  GlobalData.getStrSessionId());
            String EncptystrDistrictID = encryptDecrypt.Encrypt(strDistrictID,  GlobalData.getStrSessionId());
            String EncptystrLinkAccBankBranch = encryptDecrypt.Encrypt(strLinkAccBankBranch,  GlobalData.getStrSessionId());

            String METHOD_NAME = "QPAY_AddLinkAccount";
            String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_AddLinkAccount";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);




            PropertyInfo encryptPin = new PropertyInfo();
            encryptPin.setName("E_AccountNO");
            encryptPin.setValue(EncryptAccountNo);
            encryptPin.setType(String.class);
            request.addProperty(encryptPin);

            PropertyInfo encryptUserId = new PropertyInfo();
            encryptUserId.setName("UserID");
            encryptUserId.setValue(GlobalData.getStrUserId());
            encryptUserId.setType(String.class);
            request.addProperty(encryptUserId);

            PropertyInfo masterKey = new PropertyInfo();
            masterKey.setName("DeviceID");
            masterKey.setValue(GlobalData.getStrDeviceId());
            masterKey.setType(String.class);
            request.addProperty(masterKey);

            PropertyInfo bankid = new PropertyInfo();
            bankid.setName("E_BankID");
            bankid.setValue(EncryptBankID);
            bankid.setType(String.class);
            request.addProperty(bankid);

            PropertyInfo Acctype = new PropertyInfo();
            Acctype.setName("E_ACCOUNT_TYPE");
            Acctype.setValue(EncryptstrAccountType);
            Acctype.setType(String.class);
            request.addProperty(Acctype);

            PropertyInfo otherAccNo = new PropertyInfo();
            otherAccNo.setName("E_Other_Bank_Acc_No");
            otherAccNo.setValue(EncryptstrOther_Bank_Acc_No);
            otherAccNo.setType(String.class);
            request.addProperty(otherAccNo);

            PropertyInfo AccName = new PropertyInfo();
            AccName.setName("E_LinkAccountName");
            AccName.setValue(EncptystrAccountName);
            AccName.setType(String.class);
            request.addProperty(AccName);

            PropertyInfo District = new PropertyInfo();
            District.setName("E_DistrictID");
            District.setValue(EncptystrDistrictID);
            District.setType(String.class);
            request.addProperty(District);

            PropertyInfo Branch = new PropertyInfo();
            Branch.setName("E_LinkAccBankBranch");
            Branch.setValue(EncptystrLinkAccBankBranch);
            Branch.setType(String.class);
            request.addProperty(Branch);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            envelope.implicitTypes = true;
            Object objLoginResponse = null;


            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 10000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objLoginResponse = envelope.getResponse();
            String strLoginResponse = objLoginResponse.toString();
            mStrServerResponse=strLoginResponse;
        }
        catch (Exception ex)
        {
           String str=ex.getMessage().toString();
        }
        return  mStrServerResponse;

    }


    public static  String DeleteLinkAccount(String strAccountNo,String strBankID,String strAccountType,String strOther_Bank_Acc_No,
                                         String strAccountName,String strDistrictID,String strLinkAccBankBranch)
    {
        String mStrServerResponse="";
        try
        {
            EncryptionDecryption encryptDecrypt = new EncryptionDecryption();

            String EncryptAccountNo = encryptDecrypt.Encrypt(strAccountNo,  GlobalData.getStrSessionId());
            String EncryptBankID = encryptDecrypt.Encrypt(strBankID, GlobalData.getStrSessionId());
            String EncryptstrAccountType = encryptDecrypt.Encrypt(strAccountType,  GlobalData.getStrSessionId());

            String EncryptstrOther_Bank_Acc_No = encryptDecrypt.Encrypt(strOther_Bank_Acc_No,  GlobalData.getStrSessionId());
            String EncptystrAccountName = encryptDecrypt.Encrypt(strAccountName,  GlobalData.getStrSessionId());
            String EncptystrDistrictID = encryptDecrypt.Encrypt(strDistrictID,  GlobalData.getStrSessionId());
            String EncptystrLinkAccBankBranch = encryptDecrypt.Encrypt(strLinkAccBankBranch,  GlobalData.getStrSessionId());

            String METHOD_NAME = "QPAY_DeleteLinkAccount";
            String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_DeleteLinkAccount";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);




            PropertyInfo encryptPin = new PropertyInfo();
            encryptPin.setName("E_AccountNO");
            encryptPin.setValue(EncryptAccountNo);
            encryptPin.setType(String.class);
            request.addProperty(encryptPin);

            PropertyInfo encryptUserId = new PropertyInfo();
            encryptUserId.setName("UserID");
            encryptUserId.setValue(GlobalData.getStrUserId());
            encryptUserId.setType(String.class);
            request.addProperty(encryptUserId);

            PropertyInfo masterKey = new PropertyInfo();
            masterKey.setName("DeviceID");
            masterKey.setValue(GlobalData.getStrDeviceId());
            masterKey.setType(String.class);
            request.addProperty(masterKey);

            PropertyInfo bankid = new PropertyInfo();
            bankid.setName("E_BankID");
            bankid.setValue(EncryptBankID);
            bankid.setType(String.class);
            request.addProperty(bankid);

            PropertyInfo Acctype = new PropertyInfo();
            Acctype.setName("E_ACCOUNT_TYPE");
            Acctype.setValue(EncryptstrAccountType);
            Acctype.setType(String.class);
            request.addProperty(Acctype);

            PropertyInfo otherAccNo = new PropertyInfo();
            otherAccNo.setName("E_Other_Bank_Acc_No");
            otherAccNo.setValue(EncryptstrOther_Bank_Acc_No);
            otherAccNo.setType(String.class);
            request.addProperty(otherAccNo);

            PropertyInfo AccName = new PropertyInfo();
            AccName.setName("E_LinkAccountName");
            AccName.setValue(EncptystrAccountName);
            AccName.setType(String.class);
            request.addProperty(AccName);

            PropertyInfo District = new PropertyInfo();
            District.setName("E_DistrictID");
            District.setValue(EncptystrDistrictID);
            District.setType(String.class);
            request.addProperty(District);

            PropertyInfo Branch = new PropertyInfo();
            Branch.setName("E_LinkAccBankBranch");
            Branch.setValue(EncptystrLinkAccBankBranch);
            Branch.setType(String.class);
            request.addProperty(Branch);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            envelope.implicitTypes = true;
            Object objLoginResponse = null;


            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 10000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objLoginResponse = envelope.getResponse();
            String strLoginResponse = objLoginResponse.toString();
            mStrServerResponse=strLoginResponse;
        }
        catch (Exception ex)
        {
            String str=ex.getMessage().toString();
        }
        return  mStrServerResponse;

    }

    public static String doChangePin(String strEncryptAccountNumber,
                                     String strEncryptCurrentPin,
                                     String strEncryptNewPin) {
        String METHOD_NAME = "QPAY_AccountPINChange ";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_AccountPINChange ";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo encryptAccountNumber = new PropertyInfo();
        encryptAccountNumber.setName("E_AccountNo");
        encryptAccountNumber.setValue(strEncryptAccountNumber);
        encryptAccountNumber.setType(String.class);
        request.addProperty(encryptAccountNumber);

        PropertyInfo encryptCurrentPin = new PropertyInfo();
        encryptCurrentPin.setName("E_AccountOldPIN");
        encryptCurrentPin.setValue(strEncryptCurrentPin);
        encryptCurrentPin.setType(String.class);
        request.addProperty(encryptCurrentPin);

        PropertyInfo encryptNewPin = new PropertyInfo();
        encryptNewPin.setName("E_NewPIN");
        encryptNewPin.setValue(strEncryptNewPin);
        encryptNewPin.setType(String.class);
        request.addProperty(encryptNewPin);

        PropertyInfo encryptUserId = new PropertyInfo();
        encryptUserId.setName("UserID");
        encryptUserId.setValue(GlobalData.getStrUserId());
        encryptUserId.setType(String.class);
        request.addProperty(encryptUserId);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(GlobalData.getStrDeviceId());
        masterKey.setType(String.class);
        request.addProperty(masterKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objResponse = null;
        String strServerResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 5000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objResponse = envelope.getResponse();
            strServerResponse = objResponse.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return strServerResponse;
    }


    public static String GetHelpLinePhnoeNo(String mStrUserId,String mStrDeviceId) {

        String METHOD_NAME = "QPAY_GetHelp_Line_No";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetHelp_Line_No";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);



        PropertyInfo encryptMasterKey = new PropertyInfo();
        encryptMasterKey.setName("UserID");
        encryptMasterKey.setValue(mStrUserId);
        encryptMasterKey.setType(String.class);
        request.addProperty(encryptMasterKey);


        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(mStrDeviceId);
        masterKey.setType(String.class);
        request.addProperty(masterKey);


        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objSendCustomerOtp = null;
        String strNumber = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 2000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objSendCustomerOtp = envelope.getResponse();
            strNumber = objSendCustomerOtp.toString();
//            mStrServerResponse = strSendCustomerWalletResponse;
        } catch (Exception exception) {
//            mStrServerResponse = strSendCustomerWalletResponse;
        }
        return  strNumber;
    }


}

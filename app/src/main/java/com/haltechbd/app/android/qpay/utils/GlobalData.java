package com.haltechbd.app.android.qpay.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class GlobalData {
    //    private static String strUrl = "http://27.147.137.179:90/qpay/";//Live Ip Fiber Optic Cable
//    private static String strUrl = "http://203.76.116.170:90/qpay/";//Live Ip Radio
    //    private static String strUrl = "http://203.83.175.154/qpayws/M2BWS_agent_QR.asmx";//Live Huawei Old
    //  private static String strUrl = "http://203.83.175.202//qpayws/M2BWS_agent_QR.asmx";//Live Huawei New
    // http://hqpay.bdmitech.com/  // huawei

    //  private static String strUrl = "http://27.147.137.181:84/qpaywshal/Service.asmx?";//Qpay Demo 5 MAB db

    private static String strUrl = "http://testquickpayws.bdmitech.com/";//Qpay Demo 5 MAB db
   // private static String strUrl = "http://www.ws.quickpay.global/"; // quick pay live 31 db 33 server
    private static String strNamespace = "http://www.bdmitech.com/m2b";
    private static String strChannelType = "MPOS";
    private static String strSecurityKey = "^%$#@!";
    private static String strRegistrationSecurityKey = "Mit%$#21_^&*";
    private static String strRegistrationSecurityKeyEncryptionKey = "MiT^%$#@!";

    private static String strDeviceId;
    private static String strDeviceName;

    private static String strToken;
    private static String strAccountHolderName;
    private static String strMobileNo;
    private static String strEmailAddress;

    private static String strAccountNumber;
    private static String strAccountTypename;
    private static String strAccountStatus;
    private static String strWallet;
    private static String strPin;
    private static String strMasterKey;
    private static String strUserId;
    private static String strEncryptPin; // bad dite hbe


    private static String strQrCodeContent;
    private static String strScanQRDate;
    private static String strPageRecord;
    private static String strPackage;

    private static String strSessionId;

    private static String strLinkAccountName;
    private static String strLinkAccountDefult;
    private static String strLinkAccountNo;
    private static String strLinkAccountStatus;
    private static String strLinkAccountVerifiedStatus;

    private static String strSourceWallet;
    private static String strDestinationWallet;
    private static String strDestinationWalletName;

    private static String strFavSourceWallet;
    private static String strFavSourcePin;
    private static String strFavAmount;
    private static String strFavDestinationWallet;
    private static String strFavDestinationWalletAccountHolderName;
    private static String strFavReference;
    private static String strFavFunctionType;
    private static String strFavCaption;
    private static String strFavSourceMasterKey;
    private static String strMenuFrom;

    private static String strKYCPersonalInfoOccupation;
    private static String strKYCPersonalInfoGender;
    private static String strKYCPersonalInfoOrg;
    private static String strKYCPersonalInfoDOB;

    private static String strKYCPersonalAndSpouseName;
    private static String strKYCPersonalAndSpouseTitle;
    private static String strKYCPersonalAndSpouseMotherName;
    private static String strKYCPersonalAndSpourFathername;

    public static String getStrAddressifnoThandaID;
    public static String strKYCAddinfoPresent;
    public static String strKYCAddinfoParmanent;
    public static String strKYCAddinfoOffice;
    public static String strKYCAddInfoThanaNAme;
    public static String strKYCAddInfoThanaId;
    public static String strKYCAddInfoDistrictName;
    public static String strKYCAddInfoDistrictID;

    public static String strNomineeRemarks;
    public static String strNomineePercentage;
    public static String strNomineeRelation;
    public static String strNomineePhNo;
    public static String strNomineeName;

    public static String strIntroducerRemarks;
    public static String strIntroducerOccupation;
    public static String strIntroducerAddress;
    public static String strIntroducerPhNo;
    public static String strIntroducerName;

    public static String strKycBankRemarks = "";
    public static String strKycBankAccount = "";
    public static String strkycBankBrnach = "";
    public static String strKycBankName = "";

    private static String strMpAccountNoTextForMPCustomer = "";
    private static String strMPAccountNofromDropdwnMP = "";
    private static String strCustomerWalletForMPTestView = "";

    private static String strIdentificationID = "";
    private static String strIdentificationname = "";

    private static String strCCTFORTextAgentNO = "";
    private static String strCustomerWalletForCCTTrrouhgAgent = "";

    private static String strIdentificationRemarks = "";
    private static String strIdentificationNumber = "";
    private static String strIdentificationPictureID = "";
    private static String strIdentificationPicIDBack = "";
    private static Bitmap strProPicBitmap;
    private static String strGetAccountList;
    private static String strCallCenterPhoneNumber;
    private static String strlastTransaction;

    private static String strFMAgentNO;
    private static String strCashInCustomerNO;
    private static String strIdentificationInfo;
    private static String strIndetificationInfoPicture;
    private static String accountListString;
    private static String strLastTransaction;
    //----------------------------------

    private static String strAccountRank;
    private static String strCustomerAccountL14;
    private static String strEncryptUserId;


    private static String strEncryptPackage;
    private static String strEncryptAccountRank;

    private static String strOtp;
    private static String strEncryptOtp;
    private static String strPrimaryWalletBalance;
    private static String strSalaryWalletBalance;
    private static String strCreditWalletBalance;

    private static boolean isLogout;

    private static String strCheckMasterKey;
    private static String strResponseMessage;

    private static String strAccountTypeCode = "12";//Saving Account, 11 for Current Account
    private static String strMitEncryptionKey = "MitMycash";
    private static String strMitUserName = "mit";
    private static String strMitPin = "1234";


    public static String getGetStrAddressifnoThandaID() {
        return getStrAddressifnoThandaID;
    }

    public static void setGetStrAddressifnoThandaID(String getStrAddressifnoThandaID) {
        GlobalData.getStrAddressifnoThandaID = getStrAddressifnoThandaID;
    }
// private static String strEncryptAccountNumber;

    public static String getStrUrl() {
        return strUrl;
    }

    public static void setStrUrl(String strUrl) {
        GlobalData.strUrl = strUrl;
    }

    public static String getStrNamespace() {
        return strNamespace;
    }

    public static void setStrNamespace(String strNamespace) {
        GlobalData.strNamespace = strNamespace;
    }

    public static String getStrChannelType() {
        return strChannelType;
    }

    public static void setStrChannelType(String strChannelType) {
        GlobalData.strChannelType = strChannelType;
    }

    public static String getStrSecurityKey() {
        return strSecurityKey;
    }

    public static void setStrSecurityKey(String strSecurityKey) {
        GlobalData.strSecurityKey = strSecurityKey;
    }

    public static String getStrRegistrationSecurityKey() {
        return strRegistrationSecurityKey;
    }

    public static void setStrRegistrationSecurityKey(String strRegistrationSecurityKey) {
        GlobalData.strRegistrationSecurityKey = strRegistrationSecurityKey;
    }

    public static String getStrRegistrationSecurityKeyEncryptionKey() {
        return strRegistrationSecurityKeyEncryptionKey;
    }

    public static void setStrRegistrationSecurityKeyEncryptionKey(String strRegistrationSecurityKeyEncryptionKey) {
        GlobalData.strRegistrationSecurityKeyEncryptionKey = strRegistrationSecurityKeyEncryptionKey;
    }

    public static String getStrDeviceId() {
        return strDeviceId;
    }

    public static void setStrDeviceId(String strDeviceId) {
        GlobalData.strDeviceId = strDeviceId;
    }

    public static String getStrDeviceName() {
        return strDeviceName;
    }

    public static void setStrDeviceName(String strDeviceName) {
        GlobalData.strDeviceName = strDeviceName;
    }

    public static String getStrToken() {
        return strToken;
    }

    public static void setStrToken(String strToken) {
        GlobalData.strToken = strToken;
    }

    public static String getStrMasterKey() {
        return strMasterKey;
    }

    public static void setStrMasterKey(String strMasterKey) {
        GlobalData.strMasterKey = strMasterKey;
    }

    public static String getStrSessionId() {
        return strSessionId;
    }

    public static void setStrSessionId(String strSessionId) {
        GlobalData.strSessionId = strSessionId;
    }

    public static String getStrUserId() {
        return strUserId;
    }

    public static void setStrUserId(String strUserId) {
        GlobalData.strUserId = strUserId;
    }

    public static String getStrEncryptUserId() {
        return strEncryptUserId;
    }

    public static void setStrEncryptUserId(String strEncryptUserId) {
        GlobalData.strEncryptUserId = strEncryptUserId;
    }

    public static String getStrAccountHolderName() {
        return strAccountHolderName;
    }

    public static void setStrAccountHolderName(String strAccountHolderName) {
        GlobalData.strAccountHolderName = strAccountHolderName;
    }

    public static String getStrAccountNumber() {
        return strAccountNumber;
    }

    public static void setStrAccountNumber(String strAccountNumber) {
        GlobalData.strAccountNumber = strAccountNumber;
    }

//    public static String getStrEncryptAccountNumber() {
//        return strEncryptAccountNumber;
//    }
//
//    public static void setStrEncryptAccountNumber(String strEncryptAccountNumber) {
//        GlobalData.strEncryptAccountNumber = strEncryptAccountNumber;
//    }

    public static String getStrWallet() {
        return strWallet;
    }

    public static void setStrWallet(String strWallet) {
        GlobalData.strWallet = strWallet;
    }

    public static String getStrPin() {
        return strPin;
    }

    public static void setStrPin(String strPin) {
        GlobalData.strPin = strPin;
    }

    public static String getStrEncryptPin() {
        return strEncryptPin;
    }

    public static void setStrEncryptPin(String strEncryptPin) {
        GlobalData.strEncryptPin = strEncryptPin;
    }

    public static String getStrPackage() {
        return strPackage;
    }

    public static void setStrPackage(String strPackage) {
        GlobalData.strPackage = strPackage;
    }

    public static String getStrEncryptPackage() {
        return strEncryptPackage;
    }

    public static void setStrEncryptPackage(String strEncryptPackage) {
        GlobalData.strEncryptPackage = strEncryptPackage;
    }

    public static String getStrEncryptAccountRank() {
        return strEncryptAccountRank;
    }

    public static void setStrEncryptAccountRank(String strEncryptAccountRank) {
        GlobalData.strEncryptAccountRank = strEncryptAccountRank;
    }

    public static String getStrAccountRank() {
        return strAccountRank;
    }

    public static void setStrAccountRank(String strAccountRank) {
        GlobalData.strAccountRank = strAccountRank;
    }

    public static String getStrOtp() {
        return strOtp;
    }

    public static void setStrOtp(String strOtp) {
        GlobalData.strOtp = strOtp;
    }

    public static String getStrEncryptOtp() {
        return strEncryptOtp;
    }

    public static void setStrEncryptOtp(String strEncryptOtp) {
        GlobalData.strEncryptOtp = strEncryptOtp;
    }

    public static String getStrPrimaryWalletBalance() {
        return strPrimaryWalletBalance;
    }

    public static void setStrPrimaryWalletBalance(String strPrimaryWalletBalance) {
        GlobalData.strPrimaryWalletBalance = strPrimaryWalletBalance;
    }

    public static String getStrSalaryWalletBalance() {
        return strSalaryWalletBalance;
    }

    public static void setStrSalaryWalletBalance(String strSalaryWalletBalance) {
        GlobalData.strSalaryWalletBalance = strSalaryWalletBalance;
    }

    public static String getStrCreditWalletBalance() {
        return strCreditWalletBalance;
    }

    public static void setStrCreditWalletBalance(String strCreditWalletBalance) {
        GlobalData.strCreditWalletBalance = strCreditWalletBalance;
    }

    public static String getStrQrCodeContent() {
        return strQrCodeContent;
    }

    public static void setStrQrCodeContent(String strQrCodeContent) {
        GlobalData.strQrCodeContent = strQrCodeContent;
    }

    public static boolean isIsLogout() {
        return isLogout;
    }

    public static void setIsLogout(boolean isLogout) {
        GlobalData.isLogout = isLogout;
    }

    public static String getStrCheckMasterKey() {
        return strCheckMasterKey;
    }

    public static void setStrCheckMasterKey(String strCheckMasterKey) {
        GlobalData.strCheckMasterKey = strCheckMasterKey;
    }

    public static String getStrResponseMessage() {
        return strResponseMessage;
    }

    public static void setStrResponseMessage(String strResponseMessage) {
        GlobalData.strResponseMessage = strResponseMessage;
    }

    public static String getStrFavSourceWallet() {
        return strFavSourceWallet;
    }

    public static void setStrFavSourceWallet(String strFavSourceWallet) {
        GlobalData.strFavSourceWallet = strFavSourceWallet;
    }

    public static String getStrFavSourcePin() {
        return strFavSourcePin;
    }

    public static void setStrFavSourcePin(String strFavSourcePin) {
        GlobalData.strFavSourcePin = strFavSourcePin;
    }

    public static String getStrFavAmount() {
        return strFavAmount;
    }

    public static void setStrFavAmount(String strFavAmount) {
        GlobalData.strFavAmount = strFavAmount;
    }

    public static String getStrFavDestinationWallet() {
        return strFavDestinationWallet;
    }

    public static void setStrFavDestinationWallet(String strFavDestinationWallet) {
        GlobalData.strFavDestinationWallet = strFavDestinationWallet;
    }

    public static String getStrFavDestinationWalletAccountHolderName() {
        return strFavDestinationWalletAccountHolderName;
    }

    public static void setStrFavDestinationWalletAccountHolderName(String strFavDestinationWalletAccountHolderName) {
        GlobalData.strFavDestinationWalletAccountHolderName = strFavDestinationWalletAccountHolderName;
    }

    public static String getStrFavCaption() {
        return strFavCaption;
    }

    public static void setStrFavCaption(String strFavCaption) {
        GlobalData.strFavCaption = strFavCaption;
    }

    public static String getStrFavReference() {
        return strFavReference;
    }

    public static void setStrFavReference(String strFavReference) {
        GlobalData.strFavReference = strFavReference;
    }

    public static String getStrFavFunctionType() {
        return strFavFunctionType;
    }

    public static void setStrFavFunctionType(String strFavFunctionType) {
        GlobalData.strFavFunctionType = strFavFunctionType;
    }

    public static String getStrFavSourceMasterKey() {
        return strFavSourceMasterKey;
    }

    public static void setStrFavSourceMasterKey(String strFavSourceMasterKey) {
        GlobalData.strFavSourceMasterKey = strFavSourceMasterKey;
    }

    public static String getStrMenuFrom() {
        return strMenuFrom;
    }

    public static void setStrMenuFrom(String strMenuFrom) {
        GlobalData.strMenuFrom = strMenuFrom;
    }

    public static String getStrSourceWallet() {
        return strSourceWallet;
    }

    public static void setStrSourceWallet(String strSourceWallet) {
        GlobalData.strSourceWallet = strSourceWallet;
    }

    public static String getStrDestinationWallet() {
        return strDestinationWallet;
    }

    public static void setStrDestinationWallet(String strDestinationWallet) {
        GlobalData.strDestinationWallet = strDestinationWallet;
    }

    public static String getStrDestinationWalletName() {
        return strDestinationWalletName;
    }

    public static void setStrDestinationWalletName(String strDestinationWalletName) {
        GlobalData.strDestinationWalletName = strDestinationWalletName;
    }

    public static String getStrCustomerAccountL14() {
        return strCustomerAccountL14;
    }

    public static void setStrCustomerAccountL14(String strCustomerAccountL14) {
        GlobalData.strCustomerAccountL14 = strCustomerAccountL14;
    }

    public static String getStrAccountTypeCode() {
        return strAccountTypeCode;
    }

    public static void setStrAccountTypeCode(String strAccountTypeCode) {
        GlobalData.strAccountTypeCode = strAccountTypeCode;
    }

    public static String getStrMitEncryptionKey() {
        return strMitEncryptionKey;
    }

    public static void setStrMitEncryptionKey(String strMitEncryptionKey) {
        GlobalData.strMitEncryptionKey = strMitEncryptionKey;
    }

    public static String getStrMitUserName() {
        return strMitUserName;
    }

    public static void setStrMitUserName(String strMitUserName) {
        GlobalData.strMitUserName = strMitUserName;
    }

    public static String getStrMitPin() {
        return strMitPin;
    }

    public static void setStrMitPin(String strMitPin) {
        GlobalData.strMitPin = strMitPin;
    }

    public static void setStrMobileNo(String strMobileNo) {
        GlobalData.strMobileNo = strMobileNo;
    }

    public static String getStrMobileNo() {
        return strMobileNo;
    }

    public static void setStrEmailAddress(String strEmailAddress) {
        GlobalData.strEmailAddress = strEmailAddress;
    }

    public static String getStrEmailAddress() {
        return strEmailAddress;
    }


    public static void setStrAccountTypename(String strAccountTypename) {
        GlobalData.strAccountTypename = strAccountTypename;
    }

    public static String getStrAccountTypename() {
        return strAccountTypename;
    }

    public static void setStrLinkAccountName(String strLinkAccountName) {
        GlobalData.strLinkAccountName = strLinkAccountName;
    }

    public static void setStrLinkAccountDefult(String strLinkAccountDefult) {
        GlobalData.strLinkAccountDefult = strLinkAccountDefult;
    }

    public static String getStrLinkAccountName() {
        return strLinkAccountName;
    }

    public static String getStrLinkAccountDefult() {
        return strLinkAccountDefult;
    }

    public static void setStrLinkAccountNo(String strLinkAccountNo) {
        GlobalData.strLinkAccountNo = strLinkAccountNo;
    }

    public static String getStrLinkAccountNo() {
        return strLinkAccountNo;
    }

    public static void setStrLinkAccountStatus(String strLinkAccountStatus) {
        GlobalData.strLinkAccountStatus = strLinkAccountStatus;
    }

    public static String getStrLinkAccountStatus() {
        return strLinkAccountStatus;
    }

    public static void setStrLinkAccountVerifiedStatus(String strLinkAccountVerifiedStatus) {
        GlobalData.strLinkAccountVerifiedStatus = strLinkAccountVerifiedStatus;
    }

    public static String getStrLinkAccountVerifiedStatus() {
        return strLinkAccountVerifiedStatus;
    }


    public static void setStrKYCPersonalInfoOccupation(String strKYCPersonalInfoOccupation) {
        GlobalData.strKYCPersonalInfoOccupation = strKYCPersonalInfoOccupation;
    }

    public static String getStrKYCPersonalInfoOccupation() {
        return strKYCPersonalInfoOccupation;
    }

    public static void setStrKYCPersonalInfoGender(String strKYCPersonalInfoGender) {
        GlobalData.strKYCPersonalInfoGender = strKYCPersonalInfoGender;
    }

    public static void setStrKYCPersonalInfoOrg(String strKYCPersonalInfoOrg) {
        GlobalData.strKYCPersonalInfoOrg = strKYCPersonalInfoOrg;
    }

    public static void setStrKYCPersonalInfoDOB(String strKYCPersonalInfoDOB) {
        GlobalData.strKYCPersonalInfoDOB = strKYCPersonalInfoDOB;
    }

    public static String getStrKYCPersonalInfoGender() {
        return strKYCPersonalInfoGender;
    }

    public static String getStrKYCPersonalInfoOrg() {
        return strKYCPersonalInfoOrg;
    }

    public static String getStrKYCPersonalInfoDOB() {
        return strKYCPersonalInfoDOB;
    }

    public static void setStrKYCPersonalAndSpouseName(String strKYCPersonalAndSpouseName) {
        GlobalData.strKYCPersonalAndSpouseName = strKYCPersonalAndSpouseName;
    }

    public static void setStrKYCPersonalAndSpouseTitle(String strKYCPersonalAndSpouseTitle) {
        GlobalData.strKYCPersonalAndSpouseTitle = strKYCPersonalAndSpouseTitle;
    }

    public static void setStrKYCPersonalAndSpouseMotherName(String strKYCPersonalAndSpouseMotherName) {
        GlobalData.strKYCPersonalAndSpouseMotherName = strKYCPersonalAndSpouseMotherName;
    }

    public static void setStrKYCPersonalAndSpourFathername(String strKYCPersonalAndSpourFathername) {
        GlobalData.strKYCPersonalAndSpourFathername = strKYCPersonalAndSpourFathername;
    }

    public static String getStrKYCPersonalAndSpouseName() {
        return strKYCPersonalAndSpouseName;
    }

    public static String getStrKYCPersonalAndSpouseTitle() {
        return strKYCPersonalAndSpouseTitle;
    }

    public static String getStrKYCPersonalAndSpouseMotherName() {
        return strKYCPersonalAndSpouseMotherName;
    }

    public static String getStrKYCPersonalAndSpourFathername() {
        return strKYCPersonalAndSpourFathername;
    }


    public static void setStrKYCAddinfoPresent(String strKYCAddinfoPresent) {
        GlobalData.strKYCAddinfoPresent = strKYCAddinfoPresent;
    }

    public static void setStrKYCAddinfoParmanent(String strKYCAddinfoParmanent) {
        GlobalData.strKYCAddinfoParmanent = strKYCAddinfoParmanent;
    }

    public static void setStrKYCAddinfoOffice(String strKYCAddinfoOffice) {
        GlobalData.strKYCAddinfoOffice = strKYCAddinfoOffice;
    }

    public static void setStrKYCAddInfoThanaNAme(String strKYCAddInfoThanaNAme) {
        GlobalData.strKYCAddInfoThanaNAme = strKYCAddInfoThanaNAme;
    }

    public static void setStrKYCAddInfoThanaId(String strKYCAddInfoThanaId) {
        GlobalData.strKYCAddInfoThanaId = strKYCAddInfoThanaId;
    }

    public static void setStrKYCAddInfoDistrictName(String strKYCAddInfoDistrictName) {
        GlobalData.strKYCAddInfoDistrictName = strKYCAddInfoDistrictName;
    }

    public static void setStrKYCAddInfoDistrictID(String strKYCAddInfoDistrictID) {
        GlobalData.strKYCAddInfoDistrictID = strKYCAddInfoDistrictID;
    }

    public static String getStrKYCAddinfoPresent() {
        return strKYCAddinfoPresent;
    }

    public static String getStrKYCAddinfoParmanent() {
        return strKYCAddinfoParmanent;
    }

    public static String getStrKYCAddinfoOffice() {
        return strKYCAddinfoOffice;
    }

    public static String getStrKYCAddInfoThanaNAme() {
        return strKYCAddInfoThanaNAme;
    }

    public static String getStrKYCAddInfoThanaId() {
        return strKYCAddInfoThanaId;
    }

    public static String getStrKYCAddInfoDistrictName() {
        return strKYCAddInfoDistrictName;
    }

    public static String getStrKYCAddInfoDistrictID() {
        return strKYCAddInfoDistrictID;
    }

    public static void setStrNomineeRemarks(String strNomineeRemarks) {
        GlobalData.strNomineeRemarks = strNomineeRemarks;
    }

    public static void setStrNomineePercentage(String strNomineePercentage) {
        GlobalData.strNomineePercentage = strNomineePercentage;
    }

    public static void setStrNomineeRelation(String strNomineeRelation) {
        GlobalData.strNomineeRelation = strNomineeRelation;
    }

    public static void setStrNomineePhNo(String strNomineePhNo) {
        GlobalData.strNomineePhNo = strNomineePhNo;
    }

    public static void setStrNomineeName(String strNomineeName) {
        GlobalData.strNomineeName = strNomineeName;
    }

    public static String getStrNomineeRemarks() {
        return strNomineeRemarks;
    }

    public static String getStrNomineePercentage() {
        return strNomineePercentage;
    }

    public static String getStrNomineeRelation() {
        return strNomineeRelation;
    }

    public static String getStrNomineePhNo() {
        return strNomineePhNo;
    }

    public static String getStrNomineeName() {
        return strNomineeName;
    }

    public static void setStrIntroducerRemarks(String strIntroducerRemarks) {
        GlobalData.strIntroducerRemarks = strIntroducerRemarks;
    }

    public static void setStrIntroducerOccupation(String strIntroducerOccupation) {
        GlobalData.strIntroducerOccupation = strIntroducerOccupation;
    }

    public static void setStrIntroducerAddress(String strIntroducerAddress) {
        GlobalData.strIntroducerAddress = strIntroducerAddress;
    }

    public static void setStrIntroducerPhNo(String strIntroducerPhNo) {
        GlobalData.strIntroducerPhNo = strIntroducerPhNo;
    }

    public static void setStrIntroducerName(String strIntroducerName) {
        GlobalData.strIntroducerName = strIntroducerName;
    }

    public static String getStrIntroducerRemarks() {
        return strIntroducerRemarks;
    }

    public static String getStrIntroducerOccupation() {
        return strIntroducerOccupation;
    }

    public static String getStrIntroducerAddress() {
        return strIntroducerAddress;
    }

    public static String getStrIntroducerPhNo() {
        return strIntroducerPhNo;
    }

    public static String getStrIntroducerName() {
        return strIntroducerName;
    }

    public static void setStrKycBankRemarks(String strKycBankRemarks) {
        GlobalData.strKycBankRemarks = strKycBankRemarks;
    }

    public static void setStrKycBankAccount(String strKycBankAccount) {
        GlobalData.strKycBankAccount = strKycBankAccount;
    }

    public static void setStrkycBankBrnach(String strkycBankBrnach) {
        GlobalData.strkycBankBrnach = strkycBankBrnach;
    }

    public static void setStrKycBankName(String strKycBankName) {
        GlobalData.strKycBankName = strKycBankName;
    }

    public static String getStrKycBankRemarks() {
        return strKycBankRemarks;
    }

    public static String getStrKycBankAccount() {
        return strKycBankAccount;
    }

    public static String getStrkycBankBrnach() {
        return strkycBankBrnach;
    }

    public static String getStrKycBankName() {
        return strKycBankName;
    }

    public static void setStrScanQRDate(String strScanQRDate) {
        GlobalData.strScanQRDate = strScanQRDate;
    }

    public static String getStrScanQRDate() {
        return strScanQRDate;
    }

    public static void setStrPageRecord(String strPageRecord) {
        GlobalData.strPageRecord = strPageRecord;
    }

    public static String getStrPageRecord() {
        return strPageRecord;
    }

    public static void setStrMpAccountNoTextForMPCustomer(String strMpAccountNoTextForMPCustomer) {
        GlobalData.strMpAccountNoTextForMPCustomer = strMpAccountNoTextForMPCustomer;
    }

    public static String getStrMpAccountNoTextForMPCustomer() {
        return strMpAccountNoTextForMPCustomer;
    }

    public static void setStrMPAccountNofromDropdwnMP(String strMPAccountNofromDropdwnMP) {
        GlobalData.strMPAccountNofromDropdwnMP = strMPAccountNofromDropdwnMP;
    }

    public static String getStrMPAccountNofromDropdwnMP() {
        return strMPAccountNofromDropdwnMP;
    }


    public static void setStrCustomerWalletForMPTestView(String strCustomerWalletForMPTestView) {
        GlobalData.strCustomerWalletForMPTestView = strCustomerWalletForMPTestView;
    }

    public static String getStrCustomerWalletForMPTestView() {
        return strCustomerWalletForMPTestView;
    }

    public static void setStrIdentificationID(String strIdentificationID) {
        GlobalData.strIdentificationID = strIdentificationID;
    }

    public static void setStrIdentificationname(String strIdentificationname) {
        GlobalData.strIdentificationname = strIdentificationname;
    }

    public static String getStrIdentificationID() {
        return strIdentificationID;
    }

    public static String getStrIdentificationname() {
        return strIdentificationname;
    }

    public static void setStrCCTFORTextAgentNO(String strCCTFORTextAgentNO) {
        GlobalData.strCCTFORTextAgentNO = strCCTFORTextAgentNO;
    }

    public static String getStrCCTFORTextAgentNO() {
        return strCCTFORTextAgentNO;
    }

    public static void setStrCustomerWalletForCCTTrrouhgAgent(String strCustomerWalletForCCTTrrouhgAgent) {
        GlobalData.strCustomerWalletForCCTTrrouhgAgent = strCustomerWalletForCCTTrrouhgAgent;
    }

    public static String getStrCustomerWalletForCCTTrrouhgAgent() {
        return strCustomerWalletForCCTTrrouhgAgent;
    }


    public static void setStrIdentificationNumber(String strIdentificationNumber) {
        GlobalData.strIdentificationNumber = strIdentificationNumber;
    }

    public static void setStrIdentificationPictureID(String strIdentificationPictureID) {
        GlobalData.strIdentificationPictureID = strIdentificationPictureID;
    }

    public static void setStrIdentificationRemarks(String strIdentificationRemarks) {
        GlobalData.strIdentificationRemarks = strIdentificationRemarks;
    }

    public static String getStrIdentificationRemarks() {
        return strIdentificationRemarks;
    }

    public static String getStrIdentificationNumber() {
        return strIdentificationNumber;
    }

    public static String getStrIdentificationPictureID() {
        return strIdentificationPictureID;
    }

    public static void setStrIdentificationPicIDBack(String strIdentificationPicIDBack) {
        GlobalData.strIdentificationPicIDBack = strIdentificationPicIDBack;
    }

    public static String getStrIdentificationPicIDBack() {
        return strIdentificationPicIDBack;
    }

    public static void setStrProPicBitmap(Bitmap strProPicBitmap) {
        GlobalData.strProPicBitmap = strProPicBitmap;
    }

    public static Bitmap getStrProPicBitmap() {
        return strProPicBitmap;
    }

    public static void setStrGetAccountList(String strGetAccountList) {
        GlobalData.strGetAccountList = strGetAccountList;
    }

    public static String getStrGetAccountList() {
        return strGetAccountList;
    }

    public static void setStrAccountStatus(String strAccountStatus) {
        GlobalData.strAccountStatus = strAccountStatus;
    }

    public static String getStrAccountStatus() {
        return strAccountStatus;
    }

    public static void setStrCallCenterPhoneNumber(String strCallCenterPhoneNumber) {
        GlobalData.strCallCenterPhoneNumber = strCallCenterPhoneNumber;
    }

    public static String getStrCallCenterPhoneNumber() {
        return strCallCenterPhoneNumber;
    }

    public static void setStrlastTransaction(String strlastTransaction) {
        GlobalData.strlastTransaction = strlastTransaction;
    }

    public static String getStrlastTransaction() {
        return strlastTransaction;
    }

    public static void setStrFMAgentNO(String strFMAgentNO) {
        GlobalData.strFMAgentNO = strFMAgentNO;
    }

    public static String getStrFMAgentNO() {
        return strFMAgentNO;
    }

    public static void setStrCashInCustomerNO(String strCashInCustomerNO) {
        GlobalData.strCashInCustomerNO = strCashInCustomerNO;
    }

    public static String getStrCashInCustomerNO() {
        return strCashInCustomerNO;
    }

    public static void setStrIdentificationInfo(String strIdentificationInfo) {
        GlobalData.strIdentificationInfo = strIdentificationInfo;
    }

    public static String getStrIdentificationInfo() {
        return strIdentificationInfo;
    }


    public static void setStrIndetificationInfoPicture(String strIndetificationInfoPicture) {
        GlobalData.strIndetificationInfoPicture = strIndetificationInfoPicture;
    }

    public static String getStrIndetificationInfoPicture() {
        return strIndetificationInfoPicture;
    }

    public static void setAccountListString(String accountListString) {
        GlobalData.accountListString = accountListString;
    }

    public static String getAccountListString() {
        return accountListString;
    }


}


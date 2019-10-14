package com.haltechbd.app.android.qpay.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class InsertKyc {
	public static String insertAddressInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptThanaId,
			String strEncryptPresentAddress,
			String strEncryptPermanentAddress,
			String strEncryptOfficeAddress,
			String strParameter) {
		String METHOD_NAME = "QPAY_KYC_Address_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Address_Info";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_Address_Info
//		AccountNo
//		PIN
//		ThanaId
//		PresentAddress
//		ParmanetAddress
//		OfficeAddress
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

		PropertyInfo encryptThanaId = new PropertyInfo();
		encryptThanaId.setName("E_ThanaId");
		encryptThanaId.setValue(strEncryptThanaId);
		encryptThanaId.setType(String.class);
		request.addProperty(encryptThanaId);

		PropertyInfo encryptPresenetAddress = new PropertyInfo();
		encryptPresenetAddress.setName("E_PresentAddress");
		encryptPresenetAddress.setValue(strEncryptPresentAddress);
		encryptPresenetAddress.setType(String.class);
		request.addProperty(encryptPresenetAddress);

		PropertyInfo encryptPermanentAddress = new PropertyInfo();
		encryptPermanentAddress.setName("E_ParmanetAddress");
		encryptPermanentAddress.setValue(strEncryptPermanentAddress);
		encryptPermanentAddress.setType(String.class);
		request.addProperty(encryptPermanentAddress);

		PropertyInfo encryptOfficeAddress = new PropertyInfo();
		encryptOfficeAddress.setName("E_OfficeAddress");
		encryptOfficeAddress.setValue(strEncryptOfficeAddress);
		encryptOfficeAddress.setType(String.class);
		request.addProperty(encryptOfficeAddress);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("E_Parameter");
		masterKey.setValue(strParameter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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


	public static String insertPersonalInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptDateOfBirth,
			String strEncryptOccupation,
			String strEncryptOrganizationName,
			String strEncryptGender,
			String strParamter) {


	String	METHOD_NAME = "QPAY_KYC_Personal_Info";
	String	SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Personal_Info ";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

//        QPAY_KYC_Personal_Info
//        AccountNo
//        PIN
//        DOB
//        Occupation
//        Org_Name
//        Gender
//        strMasterKey

		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

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

		PropertyInfo encryptDateOfBirth = new PropertyInfo();
		encryptDateOfBirth.setName("E_DOB");
		encryptDateOfBirth.setValue(strEncryptDateOfBirth);
		encryptDateOfBirth.setType(String.class);
		request.addProperty(encryptDateOfBirth);

		PropertyInfo encryptOccupation = new PropertyInfo();
		encryptOccupation.setName("E_Occupation");
		encryptOccupation.setValue(strEncryptOccupation);
		encryptOccupation.setType(String.class);
		request.addProperty(encryptOccupation);

		PropertyInfo encryptOrganizationName = new PropertyInfo();
		encryptOrganizationName.setName("E_Org_Name");
		encryptOrganizationName.setValue(strEncryptOrganizationName);
		encryptOrganizationName.setType(String.class);
		request.addProperty(encryptOrganizationName);

		PropertyInfo encryptGender = new PropertyInfo();
		encryptGender.setName("E_Gender");
		encryptGender.setValue(strEncryptGender);
		encryptGender.setType(String.class);
		request.addProperty(encryptGender);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("E_Parameter");
		masterKey.setValue(strParamter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		Log.v("myApp:", request.toString());
		envelope.implicitTypes = true;
		Object objKycPersonalInfo = null;
		String strKycPersonalInfoReponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objKycPersonalInfo = envelope.getResponse();
			strKycPersonalInfoReponse = objKycPersonalInfo.toString();
//			mStrServerResponse = strKycPersonalInfoReponse;
//			if (mStrServerResponse.equalsIgnoreCase("No Data Found")) {
//
//			} else {
//				// strDecryptDOB + "*" + strDecryptOccupation + "*" + strDecryptOrg_Name + "*" + strDecryptGender
//
//				String[] parts = mStrServerResponse.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
//				String strDOB = parts[0];//Login Successfully
//				String Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
//				String ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
//				String Gender = parts[3];
//
//				mTextViewDateOfBirth.setText(strDOB);
//				mTextViewOrganization.setText(ORG_name);
//				mTextViewGender.setText(Gender);
//				mTextViewOccupation.setText(Occupation);
//				GlobalData.setStrKYCPersonalInfoDOB(strDOB);
//				GlobalData.setStrKYCPersonalInfoOrg(ORG_name);
//				GlobalData.setStrKYCPersonalInfoGender(Gender);
//				GlobalData.setStrKYCPersonalInfoOccupation(Occupation);
//
//
//			}
		} catch (Exception exception) {
			strKycPersonalInfoReponse = strKycPersonalInfoReponse;
		}
		return  strKycPersonalInfoReponse;
	}


	public static String insertParentsAndSpouseInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptFatherName,
			String strEncryptMotherName,
			String strEncryptSpouseTitle,
			String strEncryptSpouseName,
			String strparameter) {
		String METHOD_NAME = "QPAY_KYC_Parent_Spouse_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Parent_Spouse_Info ";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

//        QPAY_KYC_Parent_Spouse_Info
//        AccountNo
//        PIN
//        Fathername
//        Mothername
//        SpouseTitle
//        SpouseName
//        strMasterKey

		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

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

		PropertyInfo encryptFatherName = new PropertyInfo();
		encryptFatherName.setName("E_Fathername");
		encryptFatherName.setValue(strEncryptFatherName);
		encryptFatherName.setType(String.class);
		request.addProperty(encryptFatherName);

		PropertyInfo encryptMotherName = new PropertyInfo();
		encryptMotherName.setName("E_Mothername");
		encryptMotherName.setValue(strEncryptMotherName);
		encryptMotherName.setType(String.class);
		request.addProperty(encryptMotherName);

		PropertyInfo encryptSpouseTitle = new PropertyInfo();
		encryptSpouseTitle.setName("E_SpouseTitle");
		encryptSpouseTitle.setValue(strEncryptSpouseTitle);
		encryptSpouseTitle.setType(String.class);
		request.addProperty(encryptSpouseTitle);

		PropertyInfo encryptSpouseName = new PropertyInfo();
		encryptSpouseName.setName("E_SpouseName");
		encryptSpouseName.setValue(strEncryptSpouseName);
		encryptSpouseName.setType(String.class);
		request.addProperty(encryptSpouseName);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("E_Parameter");
		masterKey.setValue(strparameter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		Log.v("myApp:", request.toString());
		envelope.implicitTypes = true;
		Object objKycParentsAndSpouseInfo = null;
		String strKycParentsAndSpouseInfoReponse = "";
		String mStrServerResponse="";
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objKycParentsAndSpouseInfo = envelope.getResponse();
			strKycParentsAndSpouseInfoReponse = objKycParentsAndSpouseInfo.toString();
			mStrServerResponse = strKycParentsAndSpouseInfoReponse;
//
		} catch (Exception exception) {
			mStrServerResponse = strKycParentsAndSpouseInfoReponse;
		}

		return mStrServerResponse;
	}


	public static String updateContactInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,String parameter) {
		String METHOD_NAME = "QPAY_KYC_GET_Contact_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_GET_Contact_Info";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_GET_Contact_Info
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
		masterKey.setName("E_Parameter");
		masterKey.setValue(parameter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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


	public static String getIdentificationInfo(
			String strEncryptAccountNumber,
			String strEncryptPin) {
		String METHOD_NAME = "QPAY_KYC_Get_Identification";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Get_Identification";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_Identification_Info
//		AccountNo
//		PIN
//		IDNTIFCTION_ID
//		IDNTIFCTION_IDNameID
//		Remarks
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

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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

	public static String insertIndentificationInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,String strIdentificationID,String strIdentificationName,String strremarks,
			String strparameter,String strPictureID,String strPicIdBack) {
		String METHOD_NAME = "QPAY_KYC_Identification_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Identification_Info";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_Identification_Info
//		AccountNo
//		PIN
//		IDNTIFCTION_ID
//		IDNTIFCTION_IDNameID
//		Remarks
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

		PropertyInfo encryptFatherName = new PropertyInfo();
		encryptFatherName.setName("E_IDNTIFCTION_ID");
		encryptFatherName.setValue(strIdentificationID);
		encryptFatherName.setType(String.class);
		request.addProperty(encryptFatherName);

		PropertyInfo encryptMotherName = new PropertyInfo();
		encryptMotherName.setName("E_IDNTIFCTION_IDName");
		encryptMotherName.setValue(strIdentificationName);
		encryptMotherName.setType(String.class);
		request.addProperty(encryptMotherName);

		PropertyInfo encryptSpouseTitle = new PropertyInfo();
		encryptSpouseTitle.setName("E_Remarks");
		encryptSpouseTitle.setValue(strremarks);
		encryptSpouseTitle.setType(String.class);
		request.addProperty(encryptSpouseTitle);

		PropertyInfo encryptSpouseName = new PropertyInfo();
		encryptSpouseName.setName("E_Parameter");
		encryptSpouseName.setValue(strparameter);
		encryptSpouseName.setType(String.class);
		request.addProperty(encryptSpouseName);

		PropertyInfo encryptPicture = new PropertyInfo();
		encryptPicture.setName("E_Identification_Picture_ID");
		encryptPicture.setValue(strPictureID);
		encryptPicture.setType(String.class);
		request.addProperty(encryptPicture);

		PropertyInfo encryptPictureback = new PropertyInfo();
		encryptPictureback.setName("E_Identification_Picture_IDback");
		encryptPictureback.setValue(strPicIdBack);
		encryptPictureback.setType(String.class);
		request.addProperty(encryptPictureback);


		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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

	public static String insertNomineeInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptNomineeName,
			String strEncryptNomineeMobileNumber,
			String strEncryptRelation,
			String strEncryptPercent,
			String strEncryptRemark,
			String strParameter) {
		String METHOD_NAME = "QPAY_KYC_Nominee_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Nominee_Info";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_Nominee_Info
//		AccountNo
//		PIN
//		NomineeName
//		NomineeMobile
//		Relation
//		Percent
//		Remarks
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

		PropertyInfo encryptNomineeName = new PropertyInfo();
		encryptNomineeName.setName("E_NomineeName");
		encryptNomineeName.setValue(strEncryptNomineeName);
		encryptNomineeName.setType(String.class);
		request.addProperty(encryptNomineeName);

		PropertyInfo encryptNomineeMobileNumber = new PropertyInfo();
		encryptNomineeMobileNumber.setName("E_NomineeMobile");
		encryptNomineeMobileNumber.setValue(strEncryptNomineeMobileNumber);
		encryptNomineeMobileNumber.setType(String.class);
		request.addProperty(encryptNomineeMobileNumber);

		PropertyInfo encryptRelation = new PropertyInfo();
		encryptRelation.setName("E_Relation");
		encryptRelation.setValue(strEncryptRelation);
		encryptRelation.setType(String.class);
		request.addProperty(encryptRelation);

		PropertyInfo encryptPercent = new PropertyInfo();
		encryptPercent.setName("E_Percent");
		encryptPercent.setValue(strEncryptPercent);
		encryptPercent.setType(String.class);
		request.addProperty(encryptPercent);

		PropertyInfo encryptRemark = new PropertyInfo();
		encryptRemark.setName("E_Remarks");
		encryptRemark.setValue(strEncryptRemark);
		encryptRemark.setType(String.class);
		request.addProperty(encryptRemark);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("E_Parameter");
		masterKey.setValue(strParameter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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

	public static String insertIntroducerInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptIntroducerName,
			String strEncryptIntroducerMobileNumber,
			String strEncryptIntroducerAddress,
			String strEncryptIntroducerOccupation,
			String strEncryptRemark,
			String strParameter) {
		String METHOD_NAME = "QPAY_KYC_IntroducerInfo_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_IntroducerInfo_Info";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_IntroducerInfo_Info
//		AccountNo
//		PIN
//		IntroducerName
//		IntroducerMobile
//		IntroducerAddress
//		IntroducerOccupation
//		Remarks
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

		PropertyInfo encryptIntroducerName = new PropertyInfo();
		encryptIntroducerName.setName("E_IntroducerName");
		encryptIntroducerName.setValue(strEncryptIntroducerName);
		encryptIntroducerName.setType(String.class);
		request.addProperty(encryptIntroducerName);

		PropertyInfo encryptIntroducerMobileNumber = new PropertyInfo();
		encryptIntroducerMobileNumber.setName("E_IntroducerMobile");
		encryptIntroducerMobileNumber.setValue(strEncryptIntroducerMobileNumber);
		encryptIntroducerMobileNumber.setType(String.class);
		request.addProperty(encryptIntroducerMobileNumber);

		PropertyInfo encryptIntroducerAddress = new PropertyInfo();
		encryptIntroducerAddress.setName("E_IntroducerAddress");
		encryptIntroducerAddress.setValue(strEncryptIntroducerAddress);
		encryptIntroducerAddress.setType(String.class);
		request.addProperty(encryptIntroducerAddress);

		PropertyInfo encryptIntroducerOccupation = new PropertyInfo();
		encryptIntroducerOccupation.setName("E_IntroducerOccupation");
		encryptIntroducerOccupation.setValue(strEncryptIntroducerOccupation);
		encryptIntroducerOccupation.setType(String.class);
		request.addProperty(encryptIntroducerOccupation);

		PropertyInfo encryptRemark = new PropertyInfo();
		encryptRemark.setName("E_Remarks");
		encryptRemark.setValue(strEncryptRemark);
		encryptRemark.setType(String.class);
		request.addProperty(encryptRemark);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("E_Parameter");
		masterKey.setValue(strParameter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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

	public static String insertBankInfo(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptBankName,
			String strEncryptBankBranch,
			String strEncryptBankAccountNumber,
			String strEncryptRemark,
			String strParameter) {
		String METHOD_NAME = "QPAY_KYC_BankInfo_Info";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_BankInfo_Info";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_bankInfo_Info
//		AccountNo
//		PIN
//		BankName
//		Bankbranch
//		BankAccountNumber
//		Remarks
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

		PropertyInfo encryptBankName = new PropertyInfo();
		encryptBankName.setName("E_BankName");
		encryptBankName.setValue(strEncryptBankName);
		encryptBankName.setType(String.class);
		request.addProperty(encryptBankName);

		PropertyInfo encryptBankBranch = new PropertyInfo();
		encryptBankBranch.setName("E_Bankbranch");
		encryptBankBranch.setValue(strEncryptBankBranch);
		encryptBankBranch.setType(String.class);
		request.addProperty(encryptBankBranch);

		PropertyInfo encryptBankAccountNumber = new PropertyInfo();
		encryptBankAccountNumber.setName("E_BankAccountNumber");
		encryptBankAccountNumber.setValue(strEncryptBankAccountNumber);
		encryptBankAccountNumber.setType(String.class);
		request.addProperty(encryptBankAccountNumber);

		PropertyInfo encryptRemark = new PropertyInfo();
		encryptRemark.setName("E_Remarks");
		encryptRemark.setValue(strEncryptRemark);
		encryptRemark.setType(String.class);
		request.addProperty(encryptRemark);

		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("E_Parameter");
		masterKey.setValue(strParameter);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		PropertyInfo encryptUserId = new PropertyInfo();
		encryptUserId.setName("UserID");
		encryptUserId.setValue(GlobalData.getStrUserId());
		encryptUserId.setType(String.class);
		request.addProperty(encryptUserId);

		PropertyInfo Device = new PropertyInfo();
		Device.setName("DeviceID");
		Device.setValue(GlobalData.getStrDeviceId());
		Device.setType(String.class);
		request.addProperty(Device);

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


	public static String KycGetDocument(String strFileID) {

		String strImageByte="";
		String METHOD_NAME = "QPAY_KYC_Get_Document";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Get_Document";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_bankInfo_Info
//		AccountNo
//		PIN
//		BankName
//		Bankbranch
//		BankAccountNumber
//		Remarks
//		strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("fileName");
		encryptAccountNumber.setValue(strFileID);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);



		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objServerResponse = null;
		byte[] imageBytes = null;

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objServerResponse = envelope.getResponse();
			 strImageByte=objServerResponse.toString();
			//imageBytes =  ;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strImageByte;
	}

	public static String GetKycImage(String Imagefilename, String imageByte)
	{
		String strImageByte="";
		String METHOD_NAME = "QPAY_KYC_Upload_Document";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_KYC_Upload_Document";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_KYC_bankInfo_Info
//		AccountNo
//		PIN
//		BankName
//		Bankbranch
//		BankAccountNumber
//		Remarks
//		strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("fileName");
		encryptAccountNumber.setValue(Imagefilename);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("imagedata");
		encryptPin.setValue(imageByte);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);


		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objServerResponse = null;

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 1000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objServerResponse = envelope.getResponse();
			strImageByte=objServerResponse.toString();
			//imageBytes =  ;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strImageByte;

	}
}

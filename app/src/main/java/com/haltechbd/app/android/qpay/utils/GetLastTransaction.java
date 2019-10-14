package com.haltechbd.app.android.qpay.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetLastTransaction {
	public static String getLastTransaction(
			String strEncryptAccountNumber,
			String strEncryptPin,
			String strEncryptFromDate,
			String strEncryptToDate,
			String strEncryptAccessCode,
			String strEncryptParameter,
			String strUSer,String DeviceID) {
		String METHOD_NAME = "QPAY_GetAllResponse_lastOne";
		String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetAllResponse_lastOne";
		SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ","%20"), METHOD_NAME);

		// Declare the version of the SOAP request
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//		QPAY_GetAllResponse_lastOne
//		AgentNo
//		AgentPIN
//		FromDate
//		Todate
//		AccesCode
//		Parameter
//		strMasterKey

		PropertyInfo encryptAccountNumber = new PropertyInfo();
		encryptAccountNumber.setName("E_AgentNo");
		encryptAccountNumber.setValue(strEncryptAccountNumber);
		encryptAccountNumber.setType(String.class);
		request.addProperty(encryptAccountNumber);

		PropertyInfo encryptPin = new PropertyInfo();
		encryptPin.setName("E_AgentPIN");
		encryptPin.setValue(strEncryptPin);
		encryptPin.setType(String.class);
		request.addProperty(encryptPin);

		PropertyInfo encryptFromDate = new PropertyInfo();
		encryptFromDate.setName("E_FromDate");
		encryptFromDate.setValue(strEncryptFromDate);
		encryptFromDate.setType(String.class);
		request.addProperty(encryptFromDate);

		PropertyInfo encryptToDate = new PropertyInfo();
		encryptToDate.setName("E_Todate");
		encryptToDate.setValue(strEncryptToDate);
		encryptToDate.setType(String.class);
		request.addProperty(encryptToDate);

		PropertyInfo encryptAccessCode = new PropertyInfo();
		encryptAccessCode.setName("E_AccesCode");
		encryptAccessCode.setValue(strEncryptAccessCode);
		encryptAccessCode.setType(String.class);
		request.addProperty(encryptAccessCode);

		PropertyInfo encryptParameter = new PropertyInfo();
		encryptParameter.setName("E_Parameter");
		encryptParameter.setValue(strEncryptParameter);
		encryptParameter.setType(String.class);
		request.addProperty(encryptParameter);

		PropertyInfo encryptMasterKey = new PropertyInfo();
		encryptMasterKey.setName("UserID");
		encryptMasterKey.setValue(strUSer);
		encryptMasterKey.setType(String.class);
		request.addProperty(encryptMasterKey);


		PropertyInfo masterKey = new PropertyInfo();
		masterKey.setName("DeviceID");
		masterKey.setValue(DeviceID);
		masterKey.setType(String.class);
		request.addProperty(masterKey);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		Object objServerResponse = null;
		String strServerResponse = "";

		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ","%20"), 3000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			objServerResponse = envelope.getResponse();
			strServerResponse = objServerResponse.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return strServerResponse;
	}
}

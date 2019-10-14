package com.haltechbd.app.android.qpay.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryption {
    String strResult;

    public String Encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        Log.v("GET Result from  final:", results.toString());
        strResult = Base64.encodeToString(results, 1);

        Log.v("Encrypt01:", strResult.toString());
        Log.v("Encrypt02:", results.toString());
        return strResult;
    }


    public String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //this parameters should not be changed
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = new byte[text.length()];
        //BASE64Decoder decoder = new BASE64Decoder();
        try {
            results = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
        } catch (Exception e) {
            Log.i("Error in Decryption", e.toString());
        }
        Log.i("Data", new String(results, "UTF-8"));
        //return new String(results, "UTF-8"); // it returns the result as a String
        return new String(results, "UTF-8");
    }


}

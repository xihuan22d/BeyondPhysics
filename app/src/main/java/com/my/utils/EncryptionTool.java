package com.my.utils;

import android.util.Base64;

import java.util.Map;


public class EncryptionTool {

    /**
     * 加密
     */
    public static String stringToEncode(String string) {
        if (string == null) {
            return null;
        }
        if (HttpConnectTool.openEncryption) {
            return new String(Base64.encode(string.getBytes(), Base64.DEFAULT));
        } else {
            return string;
        }
    }


    /**
     * 解密
     */
    public static String stringToDecode(String string) {
        if (string == null) {
            return null;
        }
        if (HttpConnectTool.openEncryption) {
            return new String(Base64.decode(string.getBytes(), Base64.DEFAULT));
        } else {
            return string;
        }
    }

    public static Map<String, String> addParamWithEncryption(Map<String, String> bodyParams, String key, String value) {
        if (bodyParams == null || key == null || value == null) {
            return bodyParams;
        }
        bodyParams.put(key, stringToEncode(value));
        return bodyParams;
    }

}

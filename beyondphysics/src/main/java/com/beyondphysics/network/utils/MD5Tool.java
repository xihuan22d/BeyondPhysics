package com.beyondphysics.network.utils;

import com.beyondphysics.network.RequestManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xihuan22 on 2017/8/18.
 */
public class MD5Tool {


    /**
     * 根据urlString,requestType获得缓存路径,返回小写格式
     */
    public static String getCachePath(String urlString, int requestType, String fileCacheFolder, String fileType) {
        String cachePath = null;
        if (urlString == null || fileCacheFolder == null) {
            return cachePath;
        }
        if (fileType == null) {
            fileType = "txt";
        }
        FileTool.mkdirs(fileCacheFolder);
        String nameMD5 = MD5Tool.stringToMD5(RequestManager.getRequestKey(urlString, requestType));
        if (nameMD5 != null) {
            cachePath = fileCacheFolder + File.separator + nameMD5 + "." + fileType;
            cachePath = cachePath.toLowerCase();
        }
        return cachePath;
    }


    /**
     * 获得md5值
     */
    public static String stringToMD5(String string) {
        if (string == null) {
            return null;
        }
        byte[] byteHashs = null;
        try {
            byteHashs = MessageDigest.getInstance("MD5").digest(string.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder stringBuilderHex = null;
        if (byteHashs != null) {
            stringBuilderHex = new StringBuilder(byteHashs.length * 2);
            for (int i = 0; i < byteHashs.length; i++) {
                byte theByte = byteHashs[i];
                if ((theByte & 0xff) < 0x10) {
                    stringBuilderHex.append("0");
                }
                stringBuilderHex.append(Integer.toHexString(theByte & 0xff));
            }
        }
        if (stringBuilderHex == null) {
            return null;
        } else {
            return stringBuilderHex.toString();
        }
    }

}

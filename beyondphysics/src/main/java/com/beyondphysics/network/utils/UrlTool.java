package com.beyondphysics.network.utils;

/**
 * Created by xihuan22 on 2017/9/8.
 */

public class UrlTool {
    public static final int URLTYPE_HTTP = 1;
    public static final int URLTYPE_HTTPS = 2;
    public static final int URLTYPE_FILE = 3;
    public static final int URLTYPE_ASSETS = 4;
    public static final int URLTYPE_RESOURCE = 5;


    /**
     * 获得url的类型,如果返回-1,表示未知类型,1表示是http请求,2表示是https请求,3表示文件请求
     */
    public static int getUrlType(String url) {
        int type = -1;
        if (url == null) {
            return type;
        }
        url = url.toLowerCase();
        if (url.startsWith("http://")) {
            type = URLTYPE_HTTP;
        } else if (url.startsWith("https://")) {
            type = URLTYPE_HTTPS;
        } else if (url.startsWith("file://")) {
            type = URLTYPE_FILE;
        } else if (url.startsWith("assets://")) {
            type = URLTYPE_ASSETS;
        } else if (url.startsWith("resource://")) {
            type = URLTYPE_RESOURCE;
        }
        return type;
    }

    /**
     * 从url里面获取文件路径
     */
    public static String getFileNameFromUrl(String url) {
        String fileName = null;
        if (url == null) {
            return fileName;
        }
        url = url.toLowerCase();
        if (url.startsWith("file://")) {
            fileName = url.replaceFirst("file://", "");
        }
        return fileName;
    }

    /**
     * 从url里面获取assets路径
     */
    public static String getAssetsNameFromUrl(String url) {
        String assetsName = null;
        if (url == null) {
            return assetsName;
        }
        url = url.toLowerCase();
        if (url.startsWith("assets://")) {
            assetsName = url.replaceFirst("assets://", "");
        }
        return assetsName;
    }

    /**
     * 从url里面获取resource的id
     */
    public static int getResourceIdFromUrl(String url) {
        int resourceId = -1;
        if (url == null) {
            return resourceId;
        }
        url = url.toLowerCase();
        if (url.startsWith("resource://")) {
            url = url.replaceFirst("resource://", "");
            url = url.split("\\.")[0];
        }
        try {
            resourceId = Integer.parseInt(url);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return resourceId;
    }
}

package com.beyondphysics.network.cache;


import com.beyondphysics.network.utils.SuperKey;

import java.io.Serializable;

/**
 * Created by xihuan22 on 2017/8/5.
 */

public class CacheItem implements Serializable {

    private final String urlString;
    /**
     * NORMAL_REQUEST表示缓存的内容是文本信息,BITMAP_REQUEST表示缓存的内容是图片
     */
    private final int modelType;
    /**
     * 请求类型,GET,POST等
     */
    private final int requestType;
    /**
     * 缓存数据所在本地路径
     */
    private final String cachePath;

    private final long cacheSize;

    private final SuperKey superKey;


    private final String contentType;

    private final String contentEncoding;

    private final int contentLength;


    public CacheItem(String urlString, int modelType, int requestType, String cachePath, long cacheSize, SuperKey superKey, String contentType, String contentEncoding, int contentLength) {
        this.urlString = urlString;
        this.modelType = modelType;
        this.requestType = requestType;
        this.cachePath = cachePath;
        this.cacheSize = cacheSize;
        this.superKey = superKey;
        this.contentType = contentType;
        this.contentEncoding = contentEncoding;
        this.contentLength = contentLength;
    }

    public String getUrlString() {
        return urlString;
    }

    public int getModelType() {
        return modelType;
    }

    public int getRequestType() {
        return requestType;
    }

    public String getCachePath() {
        return cachePath;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public SuperKey getSuperKey() {
        return superKey;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public int getContentLength() {
        return contentLength;
    }

    @Override
    public String toString() {
        return "CacheItem{" +
                "urlString='" + urlString + '\'' +
                ", modelType=" + modelType +
                ", requestType=" + requestType +
                ", cachePath='" + cachePath + '\'' +
                ", cacheSize=" + cacheSize +
                ", superKey=" + superKey +
                ", contentType='" + contentType + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLength=" + contentLength +
                '}';
    }
}

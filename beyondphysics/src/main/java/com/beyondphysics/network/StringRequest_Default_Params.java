package com.beyondphysics.network;

/**
 * Created by xihuan22 on 2017/12/19.
 */
public class StringRequest_Default_Params {
    private String urlString;
    private int requestType = Request.GET;
    private String tag = RequestManager.NORMALREQUEST_TAG;
    private Request.OnResponseListener<String> onResponseListener;
    private int priority = 10;
    private String encoding = Request.DEFAULT_ENCODING;
    private int connectTimeoutMs = Request.DEFAULT_CONNECTTIMEOUTMS;
    private int readTimeoutMs = Request.DEFAULT_READTIMEOUTMS;
    private boolean cacheInDisk = false;

    public StringRequest_Default_Params() {
    }

    public StringRequest_Default_Params(String urlString, int requestType, String tag, Request.OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        this.urlString = urlString;
        this.requestType = requestType;
        this.tag = tag;
        this.onResponseListener = onResponseListener;
        this.priority = priority;
        this.encoding = encoding;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.cacheInDisk = cacheInDisk;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Request.OnResponseListener<String> getOnResponseListener() {
        return onResponseListener;
    }

    public void setOnResponseListener(Request.OnResponseListener<String> onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public boolean isCacheInDisk() {
        return cacheInDisk;
    }

    public void setCacheInDisk(boolean cacheInDisk) {
        this.cacheInDisk = cacheInDisk;
    }

    @Override
    public String toString() {
        return "StringRequest_Default_Params{" +
                "urlString='" + urlString + '\'' +
                ", requestType=" + requestType +
                ", tag='" + tag + '\'' +
                ", onResponseListener=" + onResponseListener +
                ", priority=" + priority +
                ", encoding='" + encoding + '\'' +
                ", connectTimeoutMs=" + connectTimeoutMs +
                ", readTimeoutMs=" + readTimeoutMs +
                ", cacheInDisk=" + cacheInDisk +
                '}';
    }
}

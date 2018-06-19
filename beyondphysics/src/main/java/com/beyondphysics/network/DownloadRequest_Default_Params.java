package com.beyondphysics.network;

/**
 * Created by xihuan22 on 2017/12/19.
 */
public class DownloadRequest_Default_Params {
    private String urlString;
    private String savePath;
    private String tag = RequestManager.DOWNLOADREQUEST_TAG;
    private Request.OnResponseListener<String> onResponseListener;
    private int priority = 10;
    private String encoding = Request.DEFAULT_ENCODING;
    private int connectTimeoutMs = Request.DEFAULT_CONNECTTIMEOUTMS;
    private int readTimeoutMs = Request.DEFAULT_READTIMEOUTMS;
    private DownloadRequest.OnDownloadProgressListener onDownloadProgressListener;

    public DownloadRequest_Default_Params() {
    }

    public DownloadRequest_Default_Params(String urlString, String savePath, String tag, Request.OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, DownloadRequest.OnDownloadProgressListener onDownloadProgressListener) {
        this.urlString = urlString;
        this.savePath = savePath;
        this.tag = tag;
        this.onResponseListener = onResponseListener;
        this.priority = priority;
        this.encoding = encoding;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.onDownloadProgressListener = onDownloadProgressListener;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
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

    public DownloadRequest.OnDownloadProgressListener getOnDownloadProgressListener() {
        return onDownloadProgressListener;
    }

    public void setOnDownloadProgressListener(DownloadRequest.OnDownloadProgressListener onDownloadProgressListener) {
        this.onDownloadProgressListener = onDownloadProgressListener;
    }

    @Override
    public String toString() {
        return "DownloadRequest_Default_Params{" +
                "urlString='" + urlString + '\'' +
                ", savePath='" + savePath + '\'' +
                ", tag='" + tag + '\'' +
                ", onResponseListener=" + onResponseListener +
                ", priority=" + priority +
                ", encoding='" + encoding + '\'' +
                ", connectTimeoutMs=" + connectTimeoutMs +
                ", readTimeoutMs=" + readTimeoutMs +
                ", onDownloadProgressListener=" + onDownloadProgressListener +
                '}';
    }
}

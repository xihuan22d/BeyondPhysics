package com.beyondphysics.network;

/**
 * Created by xihuan22 on 2017/12/19.
 */
public class BreakpointDownloadRequest_Default_Params {
    private String urlString;
    private String savePath;
    private int beginAddress;
    private String tag = RequestManager.BREAKPOINTDOWNLOADREQUEST_TAG;
    private Request.OnResponseListener<String> onResponseListener;
    private int priority = 10;
    private String encoding = Request.DEFAULT_ENCODING;
    private int connectTimeoutMs = Request.DEFAULT_CONNECTTIMEOUTMS;
    private int readTimeoutMs = Request.DEFAULT_READTIMEOUTMS;
    private BreakpointDownloadRequest.OnDownloadProgressListener onDownloadProgressListener;

    public BreakpointDownloadRequest_Default_Params() {
    }

    public BreakpointDownloadRequest_Default_Params(String urlString, String savePath, int beginAddress, String tag, Request.OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, BreakpointDownloadRequest.OnDownloadProgressListener onDownloadProgressListener) {
        this.urlString = urlString;
        this.savePath = savePath;
        this.beginAddress = beginAddress;
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

    public int getBeginAddress() {
        return beginAddress;
    }

    public void setBeginAddress(int beginAddress) {
        this.beginAddress = beginAddress;
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

    public BreakpointDownloadRequest.OnDownloadProgressListener getOnDownloadProgressListener() {
        return onDownloadProgressListener;
    }

    public void setOnDownloadProgressListener(BreakpointDownloadRequest.OnDownloadProgressListener onDownloadProgressListener) {
        this.onDownloadProgressListener = onDownloadProgressListener;
    }

    @Override
    public String toString() {
        return "BreakpointDownloadRequest_Default_Params{" +
                "urlString='" + urlString + '\'' +
                ", savePath='" + savePath + '\'' +
                ", beginAddress=" + beginAddress +
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

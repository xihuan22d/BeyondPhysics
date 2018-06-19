package com.beyondphysics.network;

import java.util.Arrays;

/**
 * Created by xihuan22 on 2017/12/19.
 */
public class UploadRequest_Default_Params {
    private String urlString;
    private String[] names;
    private String[] values;
    private String[] fileNames;
    private String[] filePaths;
    private String tag = RequestManager.UPLOADREQUEST_TAG;
    private Request.OnResponseListener<String> onResponseListener;
    private int priority = 10;
    private String encoding = Request.DEFAULT_ENCODING;
    private int connectTimeoutMs = Request.DEFAULT_CONNECTTIMEOUTMS;
    private int readTimeoutMs = Request.DEFAULT_READTIMEOUTMS;
    private UploadRequest.OnUploadProgressListener onUploadProgressListener;

    public UploadRequest_Default_Params() {
    }

    public UploadRequest_Default_Params(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, Request.OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, UploadRequest.OnUploadProgressListener onUploadProgressListener) {
        this.urlString = urlString;
        this.names = names;
        this.values = values;
        this.fileNames = fileNames;
        this.filePaths = filePaths;
        this.tag = tag;
        this.onResponseListener = onResponseListener;
        this.priority = priority;
        this.encoding = encoding;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.onUploadProgressListener = onUploadProgressListener;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String[] getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
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

    public UploadRequest.OnUploadProgressListener getOnUploadProgressListener() {
        return onUploadProgressListener;
    }

    public void setOnUploadProgressListener(UploadRequest.OnUploadProgressListener onUploadProgressListener) {
        this.onUploadProgressListener = onUploadProgressListener;
    }

    @Override
    public String toString() {
        return "UploadRequest_Default_Params{" +
                "urlString='" + urlString + '\'' +
                ", names=" + Arrays.toString(names) +
                ", values=" + Arrays.toString(values) +
                ", fileNames=" + Arrays.toString(fileNames) +
                ", filePaths=" + Arrays.toString(filePaths) +
                ", tag='" + tag + '\'' +
                ", onResponseListener=" + onResponseListener +
                ", priority=" + priority +
                ", encoding='" + encoding + '\'' +
                ", connectTimeoutMs=" + connectTimeoutMs +
                ", readTimeoutMs=" + readTimeoutMs +
                ", onUploadProgressListener=" + onUploadProgressListener +
                '}';
    }
}

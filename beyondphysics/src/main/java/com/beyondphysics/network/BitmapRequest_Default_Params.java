package com.beyondphysics.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView.ScaleType;

/**
 * Created by xihuan22 on 2017/12/19.
 */
public class BitmapRequest_Default_Params {
    private String urlString;
    private String tag = RequestManager.BITMAPREQUEST_TAG;
    private Request.OnResponseListener<BitmapResponse> onResponseListener;
    private int priority = 10;
    private int connectTimeoutMs = Request.DEFAULT_CONNECTTIMEOUTMS;
    private int readTimeoutMs = Request.DEFAULT_READTIMEOUTMS;
    private boolean cacheInDisk = true;
    private int width;
    private int height;
    private ScaleType scaleType = ScaleType.CENTER;
    private BitmapConfig bitmapConfig = new BitmapConfig(Bitmap.Config.RGB_565);
    private boolean cacheInMemory = true;
    private BitmapRequest.OnDownloadProgressListener onDownloadProgressListener;
    private Context context;
    private boolean decodeGif;

    public BitmapRequest_Default_Params() {
    }

    public BitmapRequest_Default_Params(String urlString, String tag, Request.OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, BitmapRequest.OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        this.urlString = urlString;
        this.tag = tag;
        this.onResponseListener = onResponseListener;
        this.priority = priority;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.cacheInDisk = cacheInDisk;
        this.width = width;
        this.height = height;
        this.scaleType = scaleType;
        this.bitmapConfig = bitmapConfig;
        this.cacheInMemory = cacheInMemory;
        this.onDownloadProgressListener = onDownloadProgressListener;
        this.context = context;
        this.decodeGif = decodeGif;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Request.OnResponseListener<BitmapResponse> getOnResponseListener() {
        return onResponseListener;
    }

    public void setOnResponseListener(Request.OnResponseListener<BitmapResponse> onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ScaleType getScaleType() {
        return scaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public BitmapConfig getBitmapConfig() {
        return bitmapConfig;
    }

    public void setBitmapConfig(BitmapConfig bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
    }

    public boolean isCacheInMemory() {
        return cacheInMemory;
    }

    public void setCacheInMemory(boolean cacheInMemory) {
        this.cacheInMemory = cacheInMemory;
    }

    public BitmapRequest.OnDownloadProgressListener getOnDownloadProgressListener() {
        return onDownloadProgressListener;
    }

    public void setOnDownloadProgressListener(BitmapRequest.OnDownloadProgressListener onDownloadProgressListener) {
        this.onDownloadProgressListener = onDownloadProgressListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isDecodeGif() {
        return decodeGif;
    }

    public void setDecodeGif(boolean decodeGif) {
        this.decodeGif = decodeGif;
    }

    @Override
    public String toString() {
        return "BitmapRequest_Default_Params{" +
                "urlString='" + urlString + '\'' +
                ", tag='" + tag + '\'' +
                ", onResponseListener=" + onResponseListener +
                ", priority=" + priority +
                ", connectTimeoutMs=" + connectTimeoutMs +
                ", readTimeoutMs=" + readTimeoutMs +
                ", cacheInDisk=" + cacheInDisk +
                ", width=" + width +
                ", height=" + height +
                ", scaleType=" + scaleType +
                ", bitmapConfig=" + bitmapConfig +
                ", cacheInMemory=" + cacheInMemory +
                ", onDownloadProgressListener=" + onDownloadProgressListener +
                ", context=" + context +
                ", decodeGif=" + decodeGif +
                '}';
    }
}

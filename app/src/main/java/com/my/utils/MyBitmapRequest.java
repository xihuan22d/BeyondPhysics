package com.my.utils;

import android.content.Context;
import android.widget.ImageView.ScaleType;

import com.beyondphysics.network.BitmapConfig;
import com.beyondphysics.network.BitmapRequest_Default;
import com.beyondphysics.network.BitmapRequest_Default_Params;
import com.beyondphysics.network.BitmapResponse;
import com.beyondphysics.network.utils.SuperKey;

import java.util.HashMap;
import java.util.Map;

public class MyBitmapRequest extends BitmapRequest_Default {

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, width, height, scaleType);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, priority, width, height, scaleType);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, width, height, scaleType);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener, context);
    }

    public MyBitmapRequest(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener, context, decodeGif);
    }

    public MyBitmapRequest(String urlString, SuperKey superKey, OnResponseListener<BitmapResponse> onResponseListener, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        super(urlString, superKey, onResponseListener, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener, context, decodeGif);
    }

    public MyBitmapRequest(BitmapRequest_Default_Params bitmapRequest_Default_Params) {
        super(bitmapRequest_Default_Params);
    }

    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        HttpConnectTool.addReferer(headerParams);
        return headerParams;
    }
}

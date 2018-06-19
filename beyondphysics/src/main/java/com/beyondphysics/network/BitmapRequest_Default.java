package com.beyondphysics.network;

import android.content.Context;
import android.widget.ImageView.ScaleType;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;

/**
 * Created by xihuan22 on 2017/8/19.
 */

public class BitmapRequest_Default extends BitmapRequest<BitmapResponse> {

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, width, height, scaleType);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, priority, width, height, scaleType);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, width, height, scaleType);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener, context);
    }

    public BitmapRequest_Default(String urlString, String tag, OnResponseListener<BitmapResponse> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        super(urlString, tag, onResponseListener, priority, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener, context, decodeGif);
    }

    public BitmapRequest_Default(String urlString, SuperKey superKey, OnResponseListener<BitmapResponse> onResponseListener, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        super(urlString, superKey, onResponseListener, connectTimeoutMs, readTimeoutMs, cacheInDisk, width, height, scaleType, bitmapConfig, cacheInMemory, onDownloadProgressListener, context, decodeGif);
    }

    public BitmapRequest_Default(BitmapRequest_Default_Params bitmapRequest_Default_Params) {
        super(bitmapRequest_Default_Params.getUrlString(), bitmapRequest_Default_Params.getTag(), bitmapRequest_Default_Params.getOnResponseListener(), bitmapRequest_Default_Params.getPriority(), bitmapRequest_Default_Params.getConnectTimeoutMs(), bitmapRequest_Default_Params.getReadTimeoutMs(), bitmapRequest_Default_Params.isCacheInDisk(), bitmapRequest_Default_Params.getWidth(), bitmapRequest_Default_Params.getHeight(), bitmapRequest_Default_Params.getScaleType(), bitmapRequest_Default_Params.getBitmapConfig(), bitmapRequest_Default_Params.isCacheInMemory(), bitmapRequest_Default_Params.getOnDownloadProgressListener(), bitmapRequest_Default_Params.getContext(), bitmapRequest_Default_Params.isDecodeGif());
    }

    @Override
    public BitmapResponse convertResponseType(Request<BitmapResponse> request, HttpResponse httpResponse) {
        if (request == null || httpResponse == null) {
            return null;
        }
        return new BitmapResponse(request.getUrlString(), request.getSuperKey().getKey(), request.getSuperKey().getTag(), httpResponse.getBitmapGetFrom(), httpResponse.getSuperBitmap());
    }
}

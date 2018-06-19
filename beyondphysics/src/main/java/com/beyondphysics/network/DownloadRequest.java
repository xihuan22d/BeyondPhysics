package com.beyondphysics.network;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/8/19.
 */

public abstract class DownloadRequest<T> extends Request<T> {

    public static final String DOWNLOADSUCCESS = "downloadSuccess";

    private final String savePath;

    private final boolean receiveProgress;

    private OnDownloadProgressListener onDownloadProgressListener;

    public DownloadRequest(String urlString, String savePath, String tag, OnResponseListener<T> onResponseListener) {
        super(urlString, Request.GET, tag, onResponseListener, 10, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, false, DOWNLOAD_REQUEST);
        this.savePath = savePath;
        receiveProgress = false;
        onDownloadProgressListener = null;
    }

    public DownloadRequest(String urlString, String savePath, String tag, OnResponseListener<T> onResponseListener, int priority) {
        super(urlString, Request.GET, tag, onResponseListener, priority, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, false, DOWNLOAD_REQUEST);
        this.savePath = savePath;
        receiveProgress = false;
        onDownloadProgressListener = null;
    }

    public DownloadRequest(String urlString, String savePath, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding) {
        super(urlString, Request.GET, tag, onResponseListener, priority, encoding, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, false, DOWNLOAD_REQUEST);
        this.savePath = savePath;
        receiveProgress = false;
        onDownloadProgressListener = null;
    }

    public DownloadRequest(String urlString, String savePath, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, Request.GET, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, false, DOWNLOAD_REQUEST);
        this.savePath = savePath;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
    }

    public DownloadRequest(String urlString, String savePath, SuperKey superKey, OnResponseListener<T> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, Request.GET, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, false, DOWNLOAD_REQUEST);
        this.savePath = savePath;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
    }


    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        return headerParams;
    }

    @Override
    abstract public T convertResponseType(Request<T> request, HttpResponse httpResponse);

    public String getSavePath() {
        return savePath;
    }

    public boolean isReceiveProgress() {
        return receiveProgress;
    }

    public OnDownloadProgressListener getOnDownloadProgressListener() {
        return onDownloadProgressListener;
    }

    public void removeDownloadProgressListener() {
        ThreadTool.throwIfNotOnMainThread();
        onDownloadProgressListener = null;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "savePath='" + savePath + '\'' +
                ", receiveProgress=" + receiveProgress +
                ", onDownloadProgressListener=" + onDownloadProgressListener +
                '}';
    }

    public interface OnDownloadProgressListener {
        /**
         * 文件需要下载的最大尺寸,文件下载前触发
         */
        void maxProgress(DownloadRequest<?> downloadRequest, int totalSize);

        /**
         * 文件当前的下载进度,文件下载过程中触发
         */
        void updateProgress(DownloadRequest<?> downloadRequest, int currentSize, int totalSize);
    }
}

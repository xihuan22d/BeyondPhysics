package com.beyondphysics.network;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;

import java.util.HashMap;
import java.util.Map;

/**
 * 断点下载,需要文件服务器支持Range操作,如果beginAddress>0,会从beginAddress开始读取写入到savePath,否则会先读取savePath的当前文件长度,再从该位置开始下载
 * Created by xihuan22 on 2017/8/19.
 */

public abstract class BreakpointDownloadRequest<T> extends Request<T> {


    public static final String BREAKPOINTDOWNLOADSUCCESS = "breakpointDownloadSuccess";

    public static final String BREAKPOINTDOWNLOADSUCCESS_EXISTED = "breakpointDownloadSuccess_existed";


    private final String savePath;

    private final int beginAddress;

    private final boolean receiveProgress;

    private OnDownloadProgressListener onDownloadProgressListener;

    public BreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<T> onResponseListener) {
        super(urlString, Request.GET, tag, onResponseListener, 10, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, true, BREAKPOINT_DOWNLOAD_REQUEST);
        this.savePath = savePath;
        this.beginAddress = beginAddress;
        receiveProgress = false;
        onDownloadProgressListener = null;
    }

    public BreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<T> onResponseListener, int priority) {
        super(urlString, Request.GET, tag, onResponseListener, priority, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, true, BREAKPOINT_DOWNLOAD_REQUEST);
        this.savePath = savePath;
        this.beginAddress = beginAddress;
        receiveProgress = false;
        onDownloadProgressListener = null;
    }

    public BreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding) {
        super(urlString, Request.GET, tag, onResponseListener, priority, encoding, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, true, BREAKPOINT_DOWNLOAD_REQUEST);
        this.savePath = savePath;
        this.beginAddress = beginAddress;
        receiveProgress = false;
        onDownloadProgressListener = null;
    }

    public BreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, Request.GET, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, true, BREAKPOINT_DOWNLOAD_REQUEST);
        this.savePath = savePath;
        this.beginAddress = beginAddress;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
    }

    public BreakpointDownloadRequest(String urlString, String savePath, int beginAddress, SuperKey superKey, OnResponseListener<T> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, Request.GET, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, true, BREAKPOINT_DOWNLOAD_REQUEST);
        this.savePath = savePath;
        this.beginAddress = beginAddress;
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

    public int getBeginAddress() {
        return beginAddress;
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
        return "BreakpointDownloadRequest{" +
                "savePath='" + savePath + '\'' +
                ", beginAddress=" + beginAddress +
                ", receiveProgress=" + receiveProgress +
                ", onDownloadProgressListener=" + onDownloadProgressListener +
                '}';
    }

    public interface OnDownloadProgressListener {
        /**
         * 文件需要下载的最大尺寸,文件下载前触发
         */
        void maxProgress(BreakpointDownloadRequest<?> breakpointDownloadRequest, int totalSize);

        /**
         * 文件当前的下载进度,文件下载过程中触发
         */
        void updateProgress(BreakpointDownloadRequest<?> breakpointDownloadRequest, int currentSize, int totalSize);
    }
}

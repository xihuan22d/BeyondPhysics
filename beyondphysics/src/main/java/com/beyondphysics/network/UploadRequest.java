package com.beyondphysics.network;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/8/19.
 */

public abstract class UploadRequest<T> extends Request<T> {

    private final String[] names;

    private final String[] values;

    private final String[] fileNames;

    private final String[] filePaths;

    private final boolean receiveProgress;

    private OnUploadProgressListener onUploadProgressListener;

    public UploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<T> onResponseListener) {
        super(urlString, Request.POST, tag, onResponseListener, 10, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, false, UPLOAD_REQUEST);
        this.names = names;
        this.values = values;
        this.fileNames = fileNames;
        this.filePaths = filePaths;
        receiveProgress = false;
        onUploadProgressListener = null;
    }

    public UploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<T> onResponseListener, int priority) {
        super(urlString, Request.POST, tag, onResponseListener, priority, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, false, UPLOAD_REQUEST);
        this.names = names;
        this.values = values;
        this.fileNames = fileNames;
        this.filePaths = filePaths;
        receiveProgress = false;
        onUploadProgressListener = null;
    }

    public UploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding) {
        super(urlString, Request.POST, tag, onResponseListener, priority, encoding, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, false, UPLOAD_REQUEST);
        this.names = names;
        this.values = values;
        this.fileNames = fileNames;
        this.filePaths = filePaths;
        receiveProgress = false;
        onUploadProgressListener = null;
    }

    public UploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnUploadProgressListener onUploadProgressListener) {
        super(urlString, Request.POST, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, false, UPLOAD_REQUEST);
        this.names = names;
        this.values = values;
        this.fileNames = fileNames;
        this.filePaths = filePaths;
        if (onUploadProgressListener == null) {
            receiveProgress = false;
            this.onUploadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onUploadProgressListener = onUploadProgressListener;
        }
    }

    public UploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, SuperKey superKey, OnResponseListener<T> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnUploadProgressListener onUploadProgressListener) {
        super(urlString, Request.POST, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, false, UPLOAD_REQUEST);
        this.names = names;
        this.values = values;
        this.fileNames = fileNames;
        this.filePaths = filePaths;
        if (onUploadProgressListener == null) {
            receiveProgress = false;
            this.onUploadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onUploadProgressListener = onUploadProgressListener;
        }
    }

    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "multipart/form-data;charset=" + getEncoding() + ";boundary=*****");
        return headerParams;
    }

    @Override
    abstract public T convertResponseType(Request<T> request, HttpResponse httpResponse);

    public String[] getNames() {
        return names;
    }

    public String[] getValues() {
        return values;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public String[] getFilePaths() {
        return filePaths;
    }

    public boolean isReceiveProgress() {
        return receiveProgress;
    }

    public OnUploadProgressListener getOnUploadProgressListener() {
        return onUploadProgressListener;
    }

    public void removeUploadProgressListener() {
        ThreadTool.throwIfNotOnMainThread();
        onUploadProgressListener = null;
    }

    @Override
    public String toString() {
        return "UploadRequest{" +
                "names=" + Arrays.toString(names) +
                ", values=" + Arrays.toString(values) +
                ", fileNames=" + Arrays.toString(fileNames) +
                ", filePaths=" + Arrays.toString(filePaths) +
                ", receiveProgress=" + receiveProgress +
                ", onUploadProgressListener=" + onUploadProgressListener +
                '}';
    }

    public interface OnUploadProgressListener {
        /**
         * 所有文件上传总大小
         */
        void maxProgress(UploadRequest<?> uploadRequest, long totalSize);

        /**
         * 文件当前的上传进度,上传过程中触发
         */
        void updateProgress(UploadRequest<?> uploadRequest, long currentSize, long totalSize);
    }
}

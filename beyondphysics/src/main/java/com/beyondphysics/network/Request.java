package com.beyondphysics.network;

import android.os.Looper;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;
import com.beyondphysics.network.utils.TimeTool;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by xihuan22 on 2017/8/5.
 */

public abstract class Request<T> implements Serializable {

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;
    public static final int HEAD = 4;
    public static final int OPTIONS = 5;
    public static final int TRACE = 6;
    public static final int PATCH = 7;

    public static final String DEFAULT_ENCODING = "utf-8";

    public static final int DEFAULT_CONNECTTIMEOUTMS = 8000;
    public static final int DEFAULT_READTIMEOUTMS = 22000;


    /**
     * 初始化状态,当初始化但未加入请求队列时激活
     */
    public static final int INIT = 0;
    /**
     * 等待被处理状态,当加入请求队列但未运行时激活
     */
    public static final int WAITING = 1;
    /**
     * 正在被处理,当网络请求正在运行时激活
     */
    public static final int RUNNING = 2;
    /**
     * 已处理完毕,当网络请求被取消或已回调完毕时激活
     */
    public static final int FINISH = 3;
    /**
     * 普通请求
     */
    public static final int NORMAL_REQUEST = 1;
    /**
     * 图片请求
     */
    public static final int BITMAP_REQUEST = 2;
    /**
     * 普通文件下载请求,下载文件到指定目录
     */
    public static final int DOWNLOAD_REQUEST = 3;
    /**
     * 断点下载请求
     */
    public static final int BREAKPOINT_DOWNLOAD_REQUEST = 4;
    /**
     * 上传请求
     */
    public static final int UPLOAD_REQUEST = 5;

    /**
     * 用于框架内控制,外界只可读取不该修改
     */
    private int runningStatus = INIT;
    /**
     * 如果为true,将中断网络请求
     */
    private boolean cancelRequest = false;
    /**
     * 如果为true,表示取消的请求也接受取消提示的回调
     */
    private boolean receiveCancel = false;
    /**
     * NotNull
     */
    private final String urlString;

    private final int requestType;
    /**
     * NotNull
     */
    private final SuperKey superKey;
    /**
     * 可在主线程中取消请求时候调用removeResponseListener移除请求,避免短时间的activity内存泄露
     */
    private OnResponseListener<T> onResponseListener;
    /**
     * NotNull
     */
    private final String encoding;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final boolean cacheInDisk;
    /**
     * 1表示直接继承于Request的请求,2表示继承于BitmapRequest的图片请求
     */
    private final int modelType;

    private boolean needSort;

    private OnHttpStatusListener onHttpStatusListener;

    public Request(String urlString, int requestType, String tag, OnResponseListener<T> onResponseListener) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        this.requestType = requestType;
        superKey = new SuperKey(RequestManager.getRequestKey(this.urlString, this.requestType), tag, 10, 1, TimeTool.getOnlyTimeWithoutSleep());
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        encoding = DEFAULT_ENCODING;
        connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        readTimeoutMs = DEFAULT_READTIMEOUTMS;
        cacheInDisk = false;
        modelType = NORMAL_REQUEST;
    }

    public Request(String urlString, int requestType, String tag, OnResponseListener<T> onResponseListener, int priority) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        this.requestType = requestType;
        superKey = new SuperKey(RequestManager.getRequestKey(this.urlString, this.requestType), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep());
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        encoding = DEFAULT_ENCODING;
        connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        readTimeoutMs = DEFAULT_READTIMEOUTMS;
        cacheInDisk = false;
        modelType = NORMAL_REQUEST;
    }

    public Request(String urlString, int requestType, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        this.requestType = requestType;
        superKey = new SuperKey(RequestManager.getRequestKey(this.urlString, this.requestType), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep());
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        this.encoding = encoding;

        connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        readTimeoutMs = DEFAULT_READTIMEOUTMS;
        cacheInDisk = false;
        modelType = NORMAL_REQUEST;
    }

    public Request(String urlString, int requestType, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        this.requestType = requestType;
        superKey = new SuperKey(RequestManager.getRequestKey(this.urlString, this.requestType), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep());
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        this.encoding = encoding;

        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        }
        this.connectTimeoutMs = connectTimeoutMs;

        if (readTimeoutMs <= 0) {
            readTimeoutMs = DEFAULT_READTIMEOUTMS;
        }
        this.readTimeoutMs = readTimeoutMs;
        cacheInDisk = false;
        modelType = NORMAL_REQUEST;
    }

    public Request(String urlString, int requestType, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        this.requestType = requestType;
        superKey = new SuperKey(RequestManager.getRequestKey(this.urlString, this.requestType), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep());
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        this.encoding = encoding;

        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        }
        this.connectTimeoutMs = connectTimeoutMs;

        if (readTimeoutMs <= 0) {
            readTimeoutMs = DEFAULT_READTIMEOUTMS;
        }
        this.readTimeoutMs = readTimeoutMs;
        this.cacheInDisk = cacheInDisk;
        modelType = NORMAL_REQUEST;
    }

    public Request(String urlString, int requestType, String tag, OnResponseListener<T> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int modelType) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        this.requestType = requestType;
        superKey = new SuperKey(RequestManager.getRequestKey(this.urlString, this.requestType), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep());
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        this.encoding = encoding;

        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        }
        this.connectTimeoutMs = connectTimeoutMs;

        if (readTimeoutMs <= 0) {
            readTimeoutMs = DEFAULT_READTIMEOUTMS;
        }
        this.readTimeoutMs = readTimeoutMs;
        this.cacheInDisk = cacheInDisk;
        this.modelType = modelType;
    }

    public Request(String urlString, int requestType, SuperKey superKey, OnResponseListener<T> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int modelType) {
        if (urlString == null) {
            throw new NullPointerException("urlString为null");
        } else {
            this.urlString = urlString;
        }
        if (superKey == null) {
            throw new NullPointerException("superKey为null");
        } else {
            this.superKey = superKey;
        }
        this.requestType = requestType;
        if (onResponseListener == null) {
            throw new NullPointerException("onResponseListener为null");
        } else {
            this.onResponseListener = onResponseListener;
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        this.encoding = encoding;

        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = DEFAULT_CONNECTTIMEOUTMS;
        }
        this.connectTimeoutMs = connectTimeoutMs;

        if (readTimeoutMs <= 0) {
            readTimeoutMs = DEFAULT_READTIMEOUTMS;
        }
        this.readTimeoutMs = readTimeoutMs;
        this.cacheInDisk = cacheInDisk;
        this.modelType = modelType;
    }

    /**
     * 获得请求运行状态
     */
    public int getRunningStatus() {
        return runningStatus;
    }

    /**
     * 用于框架内控制,外界只可读取不该修改
     */
    protected void setRunningStatus(int runningStatus) {
        if (this.runningStatus == FINISH) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "Request_setRunningStatus:runningStatus为FINISH后不允许修改", null, 1);
            throw new NullPointerException("runningStatus为FINISH后不允许修改");
        }
        this.runningStatus = runningStatus;
    }

    public boolean isCancelRequest() {
        return cancelRequest;
    }

    /**
     * 只可取消
     */
    public void cancelRequest() {
        cancelRequest = true;
    }

    public boolean isReceiveCancel() {
        return receiveCancel;
    }

    public void setReceiveCancel(boolean receiveCancel) {
        this.receiveCancel = receiveCancel;
    }

    public String getUrlString() {
        return urlString;
    }

    public OnResponseListener<T> getOnResponseListener() {
        return onResponseListener;
    }

    public void removeResponseListener() {
        ThreadTool.throwIfNotOnMainThread();
        onResponseListener = null;
    }

    public int getRequestType() {
        return requestType;
    }

    public SuperKey getSuperKey() {
        return superKey;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public boolean isCacheInDisk() {
        return cacheInDisk;
    }

    public int getModelType() {
        return modelType;
    }

    public boolean isNeedSort() {
        return needSort;
    }

    public void setNeedSort(boolean needSort) {
        this.needSort = needSort;
    }

    public OnHttpStatusListener getOnHttpStatusListener() {
        return onHttpStatusListener;
    }

    public void setOnHttpStatusListener(OnHttpStatusListener onHttpStatusListener) {
        this.onHttpStatusListener = onHttpStatusListener;
    }

    public Map<String, String> getHeaderParams() throws Exception {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        return headerParams;
    }

    public Map<String, String> getBodyParams() throws Exception {
        Map<String, String> bodyParams = new HashMap<String, String>();
        return bodyParams;
    }

    public byte[] getBodyParamsWithBytes() {
        try {
            Map<String, String> bodyParams = getBodyParams();
            if (bodyParams != null && bodyParams.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                int size = bodyParams.size();
                int i = 0;
                for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                    i = i + 1;
                    stringBuilder.append(URLEncoder.encode(entry.getKey(), encoding));
                    stringBuilder.append('=');
                    stringBuilder.append(URLEncoder.encode(entry.getValue(), encoding));
                    if (i != size) {
                        stringBuilder.append('&');
                    }
                }
                return stringBuilder.toString().getBytes(encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    abstract public T convertResponseType(Request<T> request, HttpResponse httpResponse);

    @Override
    public String toString() {
        return "Request{" +
                "runningStatus=" + runningStatus +
                ", cancelRequest=" + cancelRequest +
                ", receiveCancel=" + receiveCancel +
                ", urlString='" + urlString + '\'' +
                ", requestType=" + requestType +
                ", superKey=" + superKey +
                ", onResponseListener=" + onResponseListener +
                ", encoding='" + encoding + '\'' +
                ", connectTimeoutMs=" + connectTimeoutMs +
                ", readTimeoutMs=" + readTimeoutMs +
                ", cacheInDisk=" + cacheInDisk +
                ", modelType=" + modelType +
                ", needSort=" + needSort +
                ", onHttpStatusListener=" + onHttpStatusListener +
                '}';
    }

    public static void removeListener(Request<?> request, boolean removeListener) {
        if (request == null || removeListener == false) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            request.removeResponseListener();
            if (request instanceof BitmapRequest<?>) {
                BitmapRequest<?> bitmapRequest = (BitmapRequest<?>) request;
                bitmapRequest.removeDownloadProgressListener();
            } else if (request instanceof DownloadRequest<?>) {
                DownloadRequest<?> downloadRequest = (DownloadRequest<?>) request;
                downloadRequest.removeDownloadProgressListener();
            } else if (request instanceof BreakpointDownloadRequest<?>) {
                BreakpointDownloadRequest<?> breakpointDownloadRequest = (BreakpointDownloadRequest<?>) request;
                breakpointDownloadRequest.removeDownloadProgressListener();
            } else if (request instanceof UploadRequest<?>) {
                UploadRequest<?> uploadRequest = (UploadRequest<?>) request;
                uploadRequest.removeUploadProgressListener();
            }
        }
    }

    public static void removeBitmapRequestListener(BitmapRequest<?> bitmapRequest, boolean removeListener) {
        if (bitmapRequest == null || removeListener == false) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            bitmapRequest.removeResponseListener();
            bitmapRequest.removeDownloadProgressListener();
        }
    }

    public interface OnResponseListener<T> {

        void onSuccessResponse(T response);

        void onErrorResponse(String error);
    }

    /**
     * 所有方法的回调是在线程中执行的
     */
    public interface OnHttpStatusListener {
        /**
         * 如果使用的请求方式是httpURLConnection,第一个参数返回httpURLConnection,第二个返回null,不然第一个参数返回null,后面的参数返回网络请求的方式
         */
        void onHttpInit(HttpURLConnection httpURLConnection, Object object);

        /**
         * 如果使用的请求方式是HttpsURLConnection,第一个参数返回HttpsURLConnection,第二个返回null,不然第一个参数返回null,后面的参数返回网络请求的方式
         * 在该方法内实现自定义的SSLSocketFactory
         */
        void onHttpsInit(HttpsURLConnection httpsURLConnection, Object object);

        /**
         * 如果使用的请求方式是httpURLConnection,第一个参数返回httpURLConnection,第二个返回null,不然第一个参数返回null,后面的参数返回网络请求的方式
         */
        void onHttpCompleted(HttpURLConnection httpURLConnection, Object object);

        /**
         * 如果使用的请求方式是HttpsURLConnection,第一个参数返回HttpsURLConnection,第二个返回null,不然第一个参数返回null,后面的参数返回网络请求的方式
         */
        void onHttpsCompleted(HttpsURLConnection httpsURLConnection, Object object);
    }
}

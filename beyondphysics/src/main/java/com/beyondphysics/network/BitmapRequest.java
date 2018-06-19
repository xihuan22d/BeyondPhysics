package com.beyondphysics.network;

import android.content.Context;
import android.widget.ImageView.ScaleType;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;
import com.beyondphysics.network.utils.TimeTool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/8/19.
 */

public abstract class BitmapRequest<T> extends Request<T> {


    public static final String BITMAPDOWNLOADSUCCESS = "bitmapDownloadSuccess";

    public static final String BITMAPREADDISKCACHESUCCESS = "bitmapReadDiskCacheSuccess";

    public static final String BITMAPREADMEMORYCACHESUCCESS = "bitmapReadMemoryCacheSuccess";

    /**
     * 如果找到了磁盘缓存,就设置该状态为FINISH,主要用于以阻塞方式关闭请求,只有在lostCache为false时候才能把它变成FINISH,外界只可读取不该修改
     */
    private int findCacheRunningStatus = INIT;

    /**
     * 该变量需要在执行networkThreadSafelyLinkedHasMapBitmapRequest的put方法之前改变,并且之后不允许改变,lostCache为true时候在networkThreadSafelyLinkedHasMapBitmapRequest.removeRunningRequestAndRefreshSameKeyRemoveSameUrl方法内不会返回,避免再次用到runningStatus
     * 缓存丢失时候diskCacheThreadSafelyLinkedHasMapBitmapRequest内请求重新加入到networkThreadSafelyLinkedHasMapBitmapRequest时执行,只可在加入之前改变
     * 一但开启对于请求是否完成的判断将使用lostCacheRunningStatus替代runningStatus,runningStatus和lostCacheRunningStatus一旦FINISH了就不允许再操作了,否则以阻塞方式关闭的请求将可能死锁
     */
    private boolean lostCache = false;
    /**
     * 如果lostCache为true,判断请求是否处理完毕将使用该变量,丢失缓存后的运行状态,用于框架内控制,外界只可读取不该修改
     */
    private int lostCacheRunningStatus = INIT;


    private final int width;

    private final int height;
    /**
     * NotNull
     */
    private final ScaleType scaleType;
    /**
     * NotNull
     */
    private final BitmapConfig bitmapConfig;

    private final boolean cacheInMemory;

    private final boolean receiveProgress;

    private OnDownloadProgressListener onDownloadProgressListener;

    private final Context context;

    private final boolean decodeGif;

    private String cachePath;


    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int width, int height, ScaleType scaleType) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, RequestManager.getDefaultBitmapConfig()), tag, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, true, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        bitmapConfig = RequestManager.getDefaultBitmapConfig();
        cacheInMemory = true;
        receiveProgress = false;
        onDownloadProgressListener = null;
        context = null;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int width, int height, ScaleType scaleType) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, RequestManager.getDefaultBitmapConfig()), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, Request.DEFAULT_CONNECTTIMEOUTMS, Request.DEFAULT_READTIMEOUTMS, true, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        bitmapConfig = RequestManager.getDefaultBitmapConfig();
        cacheInMemory = true;
        receiveProgress = false;
        onDownloadProgressListener = null;
        context = null;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, int width, int height, ScaleType scaleType) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, RequestManager.getDefaultBitmapConfig()), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, true, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        bitmapConfig = RequestManager.getDefaultBitmapConfig();
        cacheInMemory = true;
        receiveProgress = false;
        onDownloadProgressListener = null;
        context = null;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, RequestManager.getDefaultBitmapConfig()), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        bitmapConfig = RequestManager.getDefaultBitmapConfig();
        cacheInMemory = true;
        receiveProgress = false;
        onDownloadProgressListener = null;
        context = null;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, bitmapConfig), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        if (bitmapConfig == null) {
            bitmapConfig = RequestManager.getDefaultBitmapConfig();
        }
        this.bitmapConfig = bitmapConfig;
        cacheInMemory = true;
        receiveProgress = false;
        onDownloadProgressListener = null;
        context = null;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, bitmapConfig), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        if (bitmapConfig == null) {
            bitmapConfig = RequestManager.getDefaultBitmapConfig();
        }
        this.bitmapConfig = bitmapConfig;
        this.cacheInMemory = cacheInMemory;
        receiveProgress = false;
        onDownloadProgressListener = null;
        context = null;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, bitmapConfig), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        if (bitmapConfig == null) {
            bitmapConfig = RequestManager.getDefaultBitmapConfig();
        }
        this.bitmapConfig = bitmapConfig;
        this.cacheInMemory = cacheInMemory;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
        context = null;
        decodeGif = false;
    }

    /**
     * 如果图片请求是获取本地的assets,drawable,mipmap,需要传入context,否则获取图片将失败
     */
    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, bitmapConfig), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        if (bitmapConfig == null) {
            bitmapConfig = RequestManager.getDefaultBitmapConfig();
        }
        this.bitmapConfig = bitmapConfig;
        this.cacheInMemory = cacheInMemory;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
        if (context != null) {
            context = context.getApplicationContext();
        }
        this.context = context;
        decodeGif = false;
    }

    public BitmapRequest(String urlString, String tag, OnResponseListener<T> onResponseListener, int priority, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        super(urlString, Request.GET, new SuperKey(RequestManager.getBitmapRequestKey(urlString, width, height, scaleType, bitmapConfig), tag, priority, 1, TimeTool.getOnlyTimeWithoutSleep()), onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        if (bitmapConfig == null) {
            bitmapConfig = RequestManager.getDefaultBitmapConfig();
        }
        this.bitmapConfig = bitmapConfig;
        this.cacheInMemory = cacheInMemory;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
        if (context != null) {
            context = context.getApplicationContext();
        }
        this.context = context;
        this.decodeGif = decodeGif;
    }

    public BitmapRequest(String urlString, SuperKey superKey, OnResponseListener<T> onResponseListener, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk, int width, int height, ScaleType scaleType, BitmapConfig bitmapConfig, boolean cacheInMemory, OnDownloadProgressListener onDownloadProgressListener, Context context, boolean decodeGif) {
        super(urlString, Request.GET, superKey, onResponseListener, Request.DEFAULT_ENCODING, connectTimeoutMs, readTimeoutMs, cacheInDisk, BITMAP_REQUEST);
        this.width = width;
        this.height = height;
        if (scaleType == null) {
            scaleType = ScaleType.CENTER;
        }
        this.scaleType = scaleType;
        if (bitmapConfig == null) {
            bitmapConfig = RequestManager.getDefaultBitmapConfig();
        }
        this.bitmapConfig = bitmapConfig;
        this.cacheInMemory = cacheInMemory;
        if (onDownloadProgressListener == null) {
            receiveProgress = false;
            this.onDownloadProgressListener = null;
        } else {
            receiveProgress = true;
            this.onDownloadProgressListener = onDownloadProgressListener;
        }
        if (context != null) {
            context = context.getApplicationContext();
        }
        this.context = context;
        this.decodeGif = decodeGif;
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

    public int getFindCacheRunningStatus() {
        return findCacheRunningStatus;
    }

    public void setFindCacheRunningStatus(int findCacheRunningStatus) {
        if (this.findCacheRunningStatus == FINISH) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapRequest_setFindCacheRunningStatus:findCacheRunningStatus为FINISH后不允许修改", null, 1);
            throw new NullPointerException("findCacheRunningStatus为FINISH后不允许修改");
        }
        this.findCacheRunningStatus = findCacheRunningStatus;
    }

    protected boolean isLostCache() {
        return lostCache;
    }

    protected void lostCache() {
        lostCache = true;
    }

    protected int getLostCacheRunningStatus() {
        return lostCacheRunningStatus;
    }

    protected void setLostCacheRunningStatus(int lostCacheRunningStatus) {
        if (this.lostCacheRunningStatus == FINISH) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapRequest_setLostCacheRunningStatus:lostCacheRunningStatus为FINISH后不允许修改", null, 1);
            throw new NullPointerException("lostCacheRunningStatus为FINISH后不允许修改");
        }
        this.lostCacheRunningStatus = lostCacheRunningStatus;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ScaleType getScaleType() {
        return scaleType;
    }

    public BitmapConfig getBitmapConfig() {
        return bitmapConfig;
    }

    public boolean isCacheInMemory() {
        return cacheInMemory;
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

    public Context getContext() {
        return context;
    }

    public boolean isDecodeGif() {
        return decodeGif;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    @Override
    public String toString() {
        return "BitmapRequest{" +
                "findCacheRunningStatus=" + findCacheRunningStatus +
                ", lostCache=" + lostCache +
                ", lostCacheRunningStatus=" + lostCacheRunningStatus +
                ", width=" + width +
                ", height=" + height +
                ", scaleType=" + scaleType +
                ", bitmapConfig=" + bitmapConfig +
                ", cacheInMemory=" + cacheInMemory +
                ", receiveProgress=" + receiveProgress +
                ", onDownloadProgressListener=" + onDownloadProgressListener +
                ", context=" + context +
                ", decodeGif=" + decodeGif +
                ", cachePath='" + cachePath + '\'' +
                '}';
    }

    public interface OnDownloadProgressListener {
        /**
         * 文件需要下载的最大尺寸,文件下载前触发
         */
        void maxProgress(BitmapRequest<?> bitmapRequest, int totalSize);

        /**
         * 文件当前的下载进度,文件下载过程中触发
         */
        void updateProgress(BitmapRequest<?> bitmapRequest, int currentSize, int totalSize);
    }

}

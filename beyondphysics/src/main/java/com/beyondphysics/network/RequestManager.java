package com.beyondphysics.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.widget.ImageView;

import com.beyondphysics.network.cache.BitmapDiskCacheAnalyze;
import com.beyondphysics.network.cache.BitmapDiskCacheAnalyze_Default;
import com.beyondphysics.network.cache.BitmapMemoryCache;
import com.beyondphysics.network.cache.BitmapMemoryCache_Default;
import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;
import com.beyondphysics.network.http.HttpAgreement;
import com.beyondphysics.network.http.HttpAgreement_Default;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.JsonTool;
import com.beyondphysics.network.utils.SuperBitmap;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;
import com.beyondphysics.network.utils.UrlTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 涉及到RequestManager内变量赋值的操作得保证在同一个线程,所以对变量赋值的操作都需要加上是否在主线程的检测
 * Created by xihuan22 on 2017/8/1.
 */
public class RequestManager {
    public static final String NORMAL_SEPARATOR = "##";

    public static final String CACHECONTENT_DATA = "data";

    public static final String NORMALREQUEST_TAG = "normalRequest_tag";

    public static final String BITMAPREQUEST_TAG = "bitmapRequest_tag";

    public static final String DOWNLOADREQUEST_TAG = "downloadRequest_tag";

    public static final String BREAKPOINTDOWNLOADREQUEST_TAG = "breakpointDownloadRequest_tag";

    public static final String UPLOADREQUEST_TAG = "uploadRequest_tag";

    public enum CacheItemTAG {
        UNKNOWN, NORMALREQUEST, BITMAPREQUEST, BREAKPOINTDOWNLOADREQUEST
    }

    public static boolean openDebug = true;

    public static String logFileName;
    /**
     * NotNull
     */
    protected final ResponseHandler mainResponseHandler;
    /**
     * NotNull
     */
    protected final ThreadSafelyArrayListRequestStatusItem threadSafelyArrayListRequestStatusItem;
    /**
     * NotNull
     */
    protected final ThreadSafelyLinkedHasMapRequest threadSafelyLinkedHasMapRequest_Network;
    /**
     * NotNull
     */
    protected final ThreadSafelyLinkedHasMapBitmapRequest_Network threadSafelyLinkedHasMapBitmapRequest_Network;
    /**
     * NotNull
     */
    protected final ThreadSafelyLinkedHasMapBitmapRequest threadSafelyLinkedHasMapBitmapRequest_DiskCache;
    /**
     * NotNull
     */
    protected final ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem;
    /**
     * NotNull
     */
    protected final BaseNetworkContainer baseNetworkContainer;
    /**
     * NotNull
     */
    protected final BaseBitmapNetworkContainer baseBitmapNetworkContainer;
    /**
     * NotNull
     */
    protected final BaseDiskCacheContainer baseDiskCacheContainer;
    /**
     * NotNull
     */
    protected final BaseCacheItemIOContainer baseCacheItemIOContainer;
    /**
     * 对该open变量的操作都在主线程中
     */
    protected boolean open = false;


    public RequestManager(Context context, int networkThreadCount, int bitmapNetworkThreadCount, HttpAgreement httpAgreement, BitmapMemoryCache bitmapMemoryCache, BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze, int diskCacheThreadCount, String fileCache, int maxDiskCacheSize, int responseHandlerType) {
        ThreadTool.throwIfNotOnMainThread();
        if (httpAgreement == null) {
            httpAgreement = new HttpAgreement_Default();
        }
        if (bitmapMemoryCache == null) {
            bitmapMemoryCache = new BitmapMemoryCache_Default();
        }
        if (bitmapDiskCacheAnalyze == null) {
            bitmapDiskCacheAnalyze = new BitmapDiskCacheAnalyze_Default();
        }
        if (fileCache == null) {
            if (context != null) {
                fileCache = context.getFilesDir().getAbsolutePath() + File.separator + FileTool.DEFAULT_ROOT_PATH;
            } else {
                fileCache = FileTool.getDefaultRootPath();
            }
        }
        if (responseHandlerType == ResponseHandler.RESPONSEHANDLER) {
            mainResponseHandler = new ResponseHandler(Looper.getMainLooper());
        } else {
            mainResponseHandler = new ResponseHandlerWithMessageRecord(Looper.getMainLooper());
        }
        threadSafelyArrayListRequestStatusItem = new ThreadSafelyArrayListRequestStatusItem();
        threadSafelyLinkedHasMapRequest_Network = new ThreadSafelyLinkedHasMapRequest(mainResponseHandler, threadSafelyArrayListRequestStatusItem);
        threadSafelyLinkedHasMapBitmapRequest_Network = new ThreadSafelyLinkedHasMapBitmapRequest_Network(mainResponseHandler, threadSafelyArrayListRequestStatusItem);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache = new ThreadSafelyLinkedHasMapBitmapRequest(mainResponseHandler, threadSafelyArrayListRequestStatusItem);
        threadSafelyLinkedHasMapCacheItem = new ThreadSafelyLinkedHasMapCacheItem(bitmapMemoryCache, fileCache, maxDiskCacheSize);
        baseNetworkContainer = new BaseNetworkContainer(this, networkThreadCount, httpAgreement);
        baseBitmapNetworkContainer = new BaseBitmapNetworkContainer(this, bitmapNetworkThreadCount, httpAgreement);
        baseDiskCacheContainer = new BaseDiskCacheContainer(this, diskCacheThreadCount, bitmapDiskCacheAnalyze);
        baseCacheItemIOContainer = new BaseCacheItemIOContainer(this);
    }

    public ResponseHandler getMainResponseHandler() {
        return mainResponseHandler;
    }

    public ThreadSafelyLinkedHasMapRequest getThreadSafelyLinkedHasMapRequest_Network() {
        return threadSafelyLinkedHasMapRequest_Network;
    }

    public ThreadSafelyLinkedHasMapBitmapRequest_Network getThreadSafelyLinkedHasMapBitmapRequest_Network() {
        return threadSafelyLinkedHasMapBitmapRequest_Network;
    }

    public ThreadSafelyLinkedHasMapBitmapRequest getThreadSafelyLinkedHasMapBitmapRequest_DiskCache() {
        return threadSafelyLinkedHasMapBitmapRequest_DiskCache;
    }

    public ThreadSafelyLinkedHasMapCacheItem getThreadSafelyLinkedHasMapCacheItem() {
        return threadSafelyLinkedHasMapCacheItem;
    }

    public BaseNetworkContainer getBaseNetworkContainer() {
        return baseNetworkContainer;
    }

    public BaseBitmapNetworkContainer getBaseBitmapNetworkContainer() {
        return baseBitmapNetworkContainer;
    }

    public BaseDiskCacheContainer getBaseDiskCacheContainer() {
        return baseDiskCacheContainer;
    }

    public BaseCacheItemIOContainer getBaseCacheItemIOContainer() {
        return baseCacheItemIOContainer;
    }

    public boolean isOpen() {
        return open;
    }

    /**
     * 在调用open方法初始化之前,可以安全的配置一些运行参数
     */
    public void open() {
        ThreadTool.throwIfNotOnMainThread(); //多个地方涉及到RequestManager内变量open赋值操作,所以需要加上是否在主线程的检测
        if (open) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "RequestManager_open:重复调用open方法", null, 1);
        } else {
            if (baseNetworkContainer != null) {
                baseNetworkContainer.openAllThread();
            }
            if (baseBitmapNetworkContainer != null) {
                baseBitmapNetworkContainer.openAllThread();
            }
            if (baseDiskCacheContainer != null) {
                baseDiskCacheContainer.openAllThread();
            }
            if (baseCacheItemIOContainer != null) {
                baseCacheItemIOContainer.openAllThread();
            }
            open = true;
        }
    }

    public void closeByWait() {
        ThreadTool.throwIfNotOnMainThread();
        if (!open) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "RequestManager_closeByWait:未调用open方法", null, 1);
        } else {
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItemWithAll(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequestByWait(true);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequestByWait(true);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            threadSafelyLinkedHasMapRequest_Network.cancelAllRequestByWait(true);
            mainResponseHandler.removeAllResponseMessage(true);
            if (baseNetworkContainer != null) {
                baseNetworkContainer.closeAllThreadByWait();
            }
            if (baseBitmapNetworkContainer != null) {
                baseBitmapNetworkContainer.closeAllThreadByWait();
            }
            if (baseDiskCacheContainer != null) {
                baseDiskCacheContainer.closeAllThreadByWait();
            }
            if (baseCacheItemIOContainer != null) {
                baseCacheItemIOContainer.closeAllThreadByWait();
            }
            open = false;
        }
    }

    public void close() {
        ThreadTool.throwIfNotOnMainThread();
        if (!open) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "RequestManager_close:未调用open方法", null, 1);
        } else {
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequest(true);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequest(true);
            threadSafelyLinkedHasMapRequest_Network.cancelAllRequest(true);
            mainResponseHandler.removeAllResponseMessage(true);
            if (baseNetworkContainer != null) {
                baseNetworkContainer.closeAllThread();
            }
            if (baseBitmapNetworkContainer != null) {
                baseBitmapNetworkContainer.closeAllThread();
            }
            if (baseDiskCacheContainer != null) {
                baseDiskCacheContainer.closeAllThread();
            }
            if (baseCacheItemIOContainer != null) {
                baseCacheItemIOContainer.closeAllThread();
            }
            open = false;
        }
    }

    public void addRequest(Request<?> request) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            request.setRunningStatus(Request.WAITING);
            threadSafelyLinkedHasMapRequest_Network.putRequest(request.getSuperKey(), request);
        }
    }


    public void addRequestWithSort(Request<?> request) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            request.setRunningStatus(Request.WAITING);
            request.setNeedSort(true);
            threadSafelyLinkedHasMapRequest_Network.putAndSortRequest(request.getSuperKey(), request, 1);
        }
    }

    public <T> void addBitmapRequest(BitmapRequest<T> bitmapRequest) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.WAITING);
            SuperBitmap superBitmap = threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().getSuperBitmap(bitmapRequest.getSuperKey().getKey());
            if (superBitmap == null) {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                    threadSafelyLinkedHasMapBitmapRequest_Network.putRequest(bitmapRequest.getSuperKey(), bitmapRequest);
                } else if (type == UrlTool.URLTYPE_FILE || type == UrlTool.URLTYPE_ASSETS || type == UrlTool.URLTYPE_RESOURCE) {
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putRequest(bitmapRequest.getSuperKey(), bitmapRequest);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                    Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(HttpResponse.ERROR_URLTYPE);
                    }
                }
            } else {
                bitmapRequest.setRunningStatus(Request.FINISH);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setResult(BitmapRequest.BITMAPREADMEMORYCACHESUCCESS);
                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_MEMORY);
                httpResponse.setSuperBitmap(superBitmap);
                Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                if (onResponseListener != null) {
                    onResponseListener.onSuccessResponse(bitmapRequest.convertResponseType(bitmapRequest, httpResponse));
                }
            }
        }
    }


    public <T> void addBitmapRequestWithSort(BitmapRequest<T> bitmapRequest) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.WAITING);
            bitmapRequest.setNeedSort(true);
            SuperBitmap superBitmap = threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().getSuperBitmap(bitmapRequest.getSuperKey().getKey());
            if (superBitmap == null) {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                    threadSafelyLinkedHasMapBitmapRequest_Network.putAndSortRequest(bitmapRequest.getSuperKey(), bitmapRequest, 1);
                } else if (type == UrlTool.URLTYPE_FILE || type == UrlTool.URLTYPE_ASSETS || type == UrlTool.URLTYPE_RESOURCE) {
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putAndSortRequest(bitmapRequest.getSuperKey(), bitmapRequest, 1);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                    Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(HttpResponse.ERROR_URLTYPE);
                    }
                }
            } else {
                bitmapRequest.setRunningStatus(Request.FINISH);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setResult(BitmapRequest.BITMAPREADMEMORYCACHESUCCESS);
                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_MEMORY);
                httpResponse.setSuperBitmap(superBitmap);
                Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                if (onResponseListener != null) {
                    onResponseListener.onSuccessResponse(bitmapRequest.convertResponseType(bitmapRequest, httpResponse));
                }
            }

        }
    }


    public void cancelRequestWithSuperKeyByWait(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            //阻止BaseBitmapNetworkThread把下载完成的相同url但不同大小的图片请求传递给BaseDiskCacheThread,这样先取消diskCacheThreadSafelyLinkedHasMapBitmapRequest内请求,再取消networkThreadSafelyLinkedHasMapBitmapRequest就能完全关闭
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST, superKey);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }


    public void cancelRequestWithSuperKey(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKey(superKey, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }


    public void cancelRequestWithRequestByWait(Request<?> request, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST, request.getSuperKey());
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(request.getSuperKey(), removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKeyByWait(request.getSuperKey(), removeListener);
            request.cancelRequest();
            Request.removeListener(request, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(request.getSuperKey(), removeListener);
        }
    }


    public void cancelRequestWithRequest(Request<?> request, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKey(request.getSuperKey(), removeListener);
            request.cancelRequest();
            Request.removeListener(request, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(request.getSuperKey(), removeListener);
        }
    }

    public void cancelBitmapRequestWithSuperKeyByWait(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_BITMAP_REQUEST, superKey);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }

    public void cancelBitmapRequestWithSuperKey(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(superKey, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }

    public void cancelBitmapRequestWithRequestByWait(BitmapRequest<?> bitmapRequest, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_BITMAP_REQUEST, bitmapRequest.getSuperKey());
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            bitmapRequest.cancelRequest();
            Request.removeBitmapRequestListener(bitmapRequest, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(bitmapRequest.getSuperKey(), removeListener);
        }
    }

    public void cancelBitmapRequestWithRequest(BitmapRequest<?> bitmapRequest, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            bitmapRequest.cancelRequest();
            Request.removeBitmapRequestListener(bitmapRequest, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(bitmapRequest.getSuperKey(), removeListener);
        }
    }


    public void cancelRequestWithTagByWait(String tag, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST, tag);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithTagByWait(tag, removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithTagByWait(tag, removeListener);
        threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
        threadSafelyLinkedHasMapRequest_Network.cancelRequestWithTagByWait(tag, removeListener);
        mainResponseHandler.removeResponseMessageByTag(tag, removeListener);
    }


    public void cancelRequestWithTag(String tag, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithTag(tag, removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithTag(tag, removeListener);
        threadSafelyLinkedHasMapRequest_Network.cancelRequestWithTag(tag, removeListener);
        mainResponseHandler.removeResponseMessageByTag(tag, removeListener);
    }

    public void cancelAllRequestByWait(boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItemWithAll(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequestByWait(removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequestByWait(removeListener);
        threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
        threadSafelyLinkedHasMapRequest_Network.cancelAllRequestByWait(removeListener);
        mainResponseHandler.removeAllResponseMessage(removeListener);
    }

    public void cancelAllRequest(boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequest(removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequest(removeListener);
        threadSafelyLinkedHasMapRequest_Network.cancelAllRequest(removeListener);
        mainResponseHandler.removeAllResponseMessage(removeListener);
    }

    public RequestStatusItem addRequestStatusItem(int status, int kind, SuperKey superKey) {
        return threadSafelyArrayListRequestStatusItem.addRequestStatusItem(status, kind, superKey);
    }

    public RequestStatusItem addRequestStatusItem(int status, int kind, String tag) {
        return threadSafelyArrayListRequestStatusItem.addRequestStatusItem(status, kind, tag);
    }

    public RequestStatusItem addRequestStatusItemWithAll(int status, int kind) {
        return threadSafelyArrayListRequestStatusItem.addRequestStatusItemWithAll(status, kind);
    }

    public void removeRequestStatusItem(RequestStatusItem requestStatusItem) {
        threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
    }

    public void clearMemory(String key) {
        if (key != null) {
            threadSafelyLinkedHasMapCacheItem.clearMemory(key);
        }
    }

    public void clearAllMemory() {
        threadSafelyLinkedHasMapCacheItem.clearAllMemory();
    }


    /**
     * 获取图片的CacheItem只需要传入url即可,普通请求和断点下载请求需要调用RequestManager.getRequestKey方法获得key
     * 对于CacheItem的获取和清除可以不在主线程中操作
     */
    public CacheItem getCacheItemByKey(String key, CacheItemTAG cacheItemTAG) {
        CacheItem cacheItem = threadSafelyLinkedHasMapCacheItem.getCacheItemByKey(key, getTagByCacheItemTAG(cacheItemTAG));
        return cacheItem;
    }

    public List<CacheItem> getCacheItemsByKeys(List<String> keys, CacheItemTAG cacheItemTAG) {
        List<CacheItem> cacheItems = threadSafelyLinkedHasMapCacheItem.getCacheItemsByKeys(keys, getTagByCacheItemTAG(cacheItemTAG));
        return cacheItems;
    }

    public void clearCacheItem(String key, CacheItemTAG cacheItemTAG) {
        threadSafelyLinkedHasMapCacheItem.clearCacheItem(key, getTagByCacheItemTAG(cacheItemTAG));
    }

    public void clearCacheItems(List<String> keys, CacheItemTAG cacheItemTAG) {
        threadSafelyLinkedHasMapCacheItem.clearCacheItems(keys, getTagByCacheItemTAG(cacheItemTAG));
    }

    public void clearAllCacheItem() {
        threadSafelyLinkedHasMapCacheItem.clearAllCacheItem();
    }

    public static String getTagByCacheItemTAG(CacheItemTAG cacheItemTAG) {
        String tag = null;
        if (cacheItemTAG == null) {
            return tag;
        }
        switch (cacheItemTAG) {
            case UNKNOWN:
                tag = null;
                break;
            case NORMALREQUEST:
                tag = NORMALREQUEST_TAG;
                break;
            case BITMAPREQUEST:
                tag = BITMAPREQUEST_TAG;
                break;
            case BREAKPOINTDOWNLOADREQUEST:
                tag = BREAKPOINTDOWNLOADREQUEST_TAG;
                break;
        }
        return tag;
    }

    public static String getRequestKey(String urlString, int requestType) {
        if (urlString == null) {
            return null;
        }
        return urlString + RequestManager.NORMAL_SEPARATOR + requestType;
    }

    /**
     * return NotNull
     */
    public static BitmapConfig getDefaultBitmapConfig() {
        return new BitmapConfig(Bitmap.Config.RGB_565);
    }

    public static String getBitmapRequestKey(String urlString, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (urlString == null) {
            return null;
        }
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.CENTER;
        }
        if (bitmapConfig == null) {
            bitmapConfig = getDefaultBitmapConfig();
        }
        return urlString + "#W" + maxWidth + "#H" + maxHeight + "#S" + scaleType.ordinal() + "#C" + bitmapConfig.getConfig().ordinal() + "#RT" + bitmapConfig.getRoundedType() + "#CBW" + bitmapConfig.getCircleBorderWidth() + "#CBC" + bitmapConfig.getCircleBorderColor() + "#CD" + bitmapConfig.getCornerDegree();
    }


    /**
     * 从读取的缓存数据内获取上次网络请求存储的内容
     */
    public static String getDataFromCacheContent(String cacheContent) {
        String data = null;
        if (cacheContent == null) {
            return data;
        }
        try {
            JSONObject jsonObject = new JSONObject(cacheContent);
            data = JsonTool.getStringFromJsonObject(jsonObject, CACHECONTENT_DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}

package com.beyondphysics.network;

import android.os.Process;

import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;
import com.beyondphysics.network.http.DoRequestParams;
import com.beyondphysics.network.http.HttpAgreement;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileNameLockManager;
import com.beyondphysics.network.utils.SuperKey;

import java.io.File;
import java.util.List;

/**
 * Created by xihuan22 on 2017/9/5.
 */

public class BaseBitmapNetworkThread extends BaseThread {
    /**
     * NotNull
     */
    private final RequestManager requestManager;
    /**
     * NotNull
     */
    private final ResponseHandler mainResponseHandler;
    /**
     * NotNull
     */
    private final ThreadSafelyLinkedHasMapBitmapRequest_Network threadSafelyLinkedHasMapBitmapRequest_Network;
    /**
     * NotNull
     */
    private final ThreadSafelyLinkedHasMapBitmapRequest threadSafelyLinkedHasMapBitmapRequest_DiskCache;
    /**
     * NotNull
     */
    private final ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem;
    /**
     * NotNull
     */
    private final HttpAgreement httpAgreement;


    public BaseBitmapNetworkThread(RequestManager requestManager, HttpAgreement httpAgreement) {
        super(Process.THREAD_PRIORITY_BACKGROUND, 10);
        this.setName("BaseBitmapNetworkThread");
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
            mainResponseHandler = this.requestManager.getMainResponseHandler();
            threadSafelyLinkedHasMapBitmapRequest_Network = this.requestManager.getThreadSafelyLinkedHasMapBitmapRequest_Network();
            threadSafelyLinkedHasMapBitmapRequest_DiskCache = this.requestManager.getThreadSafelyLinkedHasMapBitmapRequest_DiskCache();
            threadSafelyLinkedHasMapCacheItem = this.requestManager.getThreadSafelyLinkedHasMapCacheItem();
        }
        if (httpAgreement == null) {
            throw new NullPointerException("httpAgreement为null");
        } else {
            this.httpAgreement = httpAgreement;
        }
    }

    @Override
    public void doWork() {
        BitmapRequest<?> bitmapRequest = threadSafelyLinkedHasMapBitmapRequest_Network.takeTopUnRunningRequestByUrlAndPutToRunning();
        if (bitmapRequest != null) {
            boolean isHaveCacheItem = false;
            if (!bitmapRequest.isLostCache()) {
                CacheItem cacheItem = threadSafelyLinkedHasMapCacheItem.getCacheItemByKey(bitmapRequest.getUrlString(), RequestManager.BITMAPREQUEST_TAG);
                if (cacheItem != null) {
                    String fileName = cacheItem.getCachePath();
                    if (fileName != null) {
                        try {
                            FileNameLockManager.getInstance().getFileNameLock(fileName);
                            File file = new File(fileName);
                            if (file.exists()) {
                                bitmapRequest.setCachePath(fileName);
                                isHaveCacheItem = true;
                            }
                        } finally {
                            FileNameLockManager.getInstance().removeFileNameLock(fileName);
                        }
                    }
                    if (!isHaveCacheItem) {
                        threadSafelyLinkedHasMapCacheItem.removeRequestWithInit(cacheItem.getSuperKey());
                    }
                }
            }
            if (isHaveCacheItem) {
                threadSafelyLinkedHasMapBitmapRequest_Network.removeRunningRequest(bitmapRequest.getSuperKey());
                if (bitmapRequest.isNeedSort()) {
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putAndSortRequest(bitmapRequest.getSuperKey(), bitmapRequest, 1);
                } else {
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putRequest(bitmapRequest.getSuperKey(), bitmapRequest);
                }

                bitmapRequest.setFindCacheRunningStatus(Request.FINISH);
            } else {
                if (bitmapRequest.isLostCache()) {//引入该变量是为了保证runningStatus在FINISH后不会再进行任何操作的特性
                    bitmapRequest.setLostCacheRunningStatus(Request.RUNNING);
                } else {
                    bitmapRequest.setRunningStatus(Request.RUNNING);
                }
                HttpResponse httpResponse = httpAgreement.doRequest(bitmapRequest, new DoRequestParams(mainResponseHandler, threadSafelyLinkedHasMapCacheItem));
                if (httpResponse == null) {
                    httpResponse = new HttpResponse();
                    httpResponse.setStatus(HttpResponse.ERROR);
                    httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
                }
                CacheItem cacheItem = null;
                boolean isSuccess = false;
                if (httpResponse.getStatus() == HttpResponse.SUCCESS) {
                    if (bitmapRequest.isCacheInMemory()) {
                        threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().putSuperBitmap(bitmapRequest.getSuperKey().getKey(), httpResponse.getSuperBitmap());
                    }
                    if (bitmapRequest.isCacheInDisk()) {
                        String cachePath = httpResponse.getCachePath();
                        long cacheSize = httpResponse.getCacheSize();
                        if (cachePath != null && cacheSize > 0) {
                            SuperKey superKeyCacheItem = new SuperKey(bitmapRequest.getUrlString(), RequestManager.BITMAPREQUEST_TAG, 9, 2, bitmapRequest.getSuperKey().getTime());
                            cacheItem = new CacheItem(bitmapRequest.getUrlString(), bitmapRequest.getModelType(), bitmapRequest.getRequestType(), cachePath, cacheSize, superKeyCacheItem, httpResponse.getContentType(), httpResponse.getContentEncoding(), httpResponse.getContentLength());
                            threadSafelyLinkedHasMapCacheItem.putAndSortRequestWithInit(superKeyCacheItem, cacheItem, 2);
                        }
                    }
                    isSuccess = true;
                }
                mainResponseHandler.sendResponseMessage(bitmapRequest, httpResponse);//必须在removeRunningRequest之前发送消息,这样才算真正的完成了
                if (isSuccess && bitmapRequest.isCacheInDisk()) {
                    List<BitmapRequest<?>> bitmapRequests = threadSafelyLinkedHasMapBitmapRequest_Network.removeRunningRequestAndRefreshSameKeyRemoveSameUrl(bitmapRequest.getSuperKey(), httpResponse, bitmapRequest.getUrlString(), cacheItem);
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putRequests(bitmapRequests);
                } else {
                    threadSafelyLinkedHasMapBitmapRequest_Network.removeRunningRequest(bitmapRequest.getSuperKey());
                }
                if (bitmapRequest.isLostCache()) {
                    bitmapRequest.setLostCacheRunningStatus(Request.FINISH);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                }
            }
        }
    }

}

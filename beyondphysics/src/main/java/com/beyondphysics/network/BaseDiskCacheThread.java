package com.beyondphysics.network;


import android.os.Process;

import com.beyondphysics.network.cache.BitmapDiskCacheAnalyze;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;
import com.beyondphysics.network.http.DoRequestParams;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.UrlTool;


/**
 * Created by xihuan22 on 2017/8/5.
 * 如果是网络请求,缓存丢失将把失败的请求推送给BaseBitmapNetworkContainer处理
 * 如果是本地缓存请求,缓存丢失将直接返回请求失败
 */

public class BaseDiskCacheThread extends BaseThread {
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
    private final BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze;


    public BaseDiskCacheThread(RequestManager requestManager, BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze) {
        super(Process.THREAD_PRIORITY_BACKGROUND, 10);
        this.setName("BaseDiskCacheThread");
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
            mainResponseHandler = this.requestManager.getMainResponseHandler();
            threadSafelyLinkedHasMapBitmapRequest_Network = this.requestManager.getThreadSafelyLinkedHasMapBitmapRequest_Network();
            threadSafelyLinkedHasMapBitmapRequest_DiskCache = this.requestManager.getThreadSafelyLinkedHasMapBitmapRequest_DiskCache();
            threadSafelyLinkedHasMapCacheItem = this.requestManager.getThreadSafelyLinkedHasMapCacheItem();
        }
        if (bitmapDiskCacheAnalyze == null) {
            throw new NullPointerException("bitmapDiskCacheAnalyze为null");
        } else {
            this.bitmapDiskCacheAnalyze = bitmapDiskCacheAnalyze;
        }
    }

    @Override
    public void doWork() {
        BitmapRequest<?> bitmapRequest = threadSafelyLinkedHasMapBitmapRequest_DiskCache.takeTopUnRunningRequestByKeyAndPutToRunning();
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.RUNNING);
            HttpResponse httpResponse = bitmapDiskCacheAnalyze.doDiskCacheAnalyze(bitmapRequest, new DoRequestParams(mainResponseHandler, threadSafelyLinkedHasMapCacheItem));
            if (httpResponse == null) {
                httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            boolean isSuccess = false;
            boolean needRetryGet = false;
            if (httpResponse.getStatus() == HttpResponse.SUCCESS) {
                if (bitmapRequest.isCacheInMemory()) {
                    threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().putSuperBitmap(bitmapRequest.getSuperKey().getKey(), httpResponse.getSuperBitmap());
                }
                mainResponseHandler.sendResponseMessage(bitmapRequest, httpResponse);
                isSuccess = true;
            } else {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (httpResponse.getStatus() == HttpResponse.LOST && (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS)) {
                    needRetryGet = true;
                } else {
                    mainResponseHandler.sendResponseMessage(bitmapRequest, httpResponse);
                }
            }
            if (isSuccess) {
                threadSafelyLinkedHasMapBitmapRequest_DiskCache.removeRunningRequestAndRefreshSameKey(bitmapRequest.getSuperKey(), httpResponse);
            } else {
                threadSafelyLinkedHasMapBitmapRequest_DiskCache.removeRunningRequest(bitmapRequest.getSuperKey());
                if (needRetryGet) {
                    //因为runningStatus已经FINISH了,这个状态用以阻塞方式关闭请求,一旦FINISH了不允许改变,所以通知networkThreadSafelyLinkedHasMapBitmapRequest使用新的标志位
                    bitmapRequest.lostCache();
                    if (bitmapRequest.isNeedSort()) {
                        threadSafelyLinkedHasMapBitmapRequest_Network.putAndSortRequest(bitmapRequest.getSuperKey(), bitmapRequest, 1);
                    } else {
                        threadSafelyLinkedHasMapBitmapRequest_Network.putRequest(bitmapRequest.getSuperKey(), bitmapRequest);
                    }
                }
            }
            bitmapRequest.setRunningStatus(Request.FINISH);
        }

    }
}

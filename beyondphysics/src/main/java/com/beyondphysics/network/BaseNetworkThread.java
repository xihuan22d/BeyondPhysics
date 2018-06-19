package com.beyondphysics.network;

import android.os.Process;

import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;
import com.beyondphysics.network.http.DoRequestParams;
import com.beyondphysics.network.http.HttpAgreement;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.MD5Tool;
import com.beyondphysics.network.utils.SuperKey;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xihuan22 on 2017/8/5.
 */

public class BaseNetworkThread extends BaseThread {
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
    private final ThreadSafelyLinkedHasMapRequest threadSafelyLinkedHasMapRequest_Network;
    /**
     * NotNull
     */
    private final ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem;
    /**
     * NotNull
     */
    private final HttpAgreement httpAgreement;


    public BaseNetworkThread(RequestManager requestManager, HttpAgreement httpAgreement) {
        super(Process.THREAD_PRIORITY_BACKGROUND, 10);
        this.setName("BaseNetworkThread");
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
            mainResponseHandler = this.requestManager.getMainResponseHandler();
            threadSafelyLinkedHasMapRequest_Network = this.requestManager.getThreadSafelyLinkedHasMapRequest_Network();
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
        Request<?> request = threadSafelyLinkedHasMapRequest_Network.takeTopRequestAndPutToRunning();
        if (request != null) {
            request.setRunningStatus(Request.RUNNING);
            HttpResponse httpResponse = httpAgreement.doRequest(request, new DoRequestParams(mainResponseHandler, threadSafelyLinkedHasMapCacheItem));
            if (httpResponse == null) {
                httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            if (httpResponse.getStatus() == HttpResponse.SUCCESS) {
                if (request.getModelType() == Request.NORMAL_REQUEST && request.isCacheInDisk()) {
                    String cachePath = MD5Tool.getCachePath(request.getUrlString(), request.getRequestType(), threadSafelyLinkedHasMapCacheItem.getFileCache_textFolder(), null);
                    if (cachePath != null) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(RequestManager.CACHECONTENT_DATA, httpResponse.getResult());
                            long cacheSize = FileTool.writeContentWithLock(cachePath, jsonObject.toString(), false);
                            SuperKey superKeyCacheItem = new SuperKey(request.getSuperKey().getKey(), RequestManager.NORMALREQUEST_TAG, 10, 2, request.getSuperKey().getTime());
                            CacheItem cacheItem = new CacheItem(request.getUrlString(), request.getModelType(), request.getRequestType(), cachePath, cacheSize, superKeyCacheItem, httpResponse.getContentType(), httpResponse.getContentEncoding(), httpResponse.getContentLength());
                            threadSafelyLinkedHasMapCacheItem.putAndSortRequestWithInit(superKeyCacheItem, cacheItem, 2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (request.getModelType() == Request.BREAKPOINT_DOWNLOAD_REQUEST && request.isCacheInDisk()) {//因为断点下载的文件位置是用户指定的不算框架内生成的,不应该移除它,所以cachePath设置为null
                    SuperKey superKeyCacheItem = new SuperKey(request.getSuperKey().getKey(), RequestManager.BREAKPOINTDOWNLOADREQUEST_TAG, 11, 2, request.getSuperKey().getTime());
                    CacheItem cacheItem = new CacheItem(request.getUrlString(), request.getModelType(), request.getRequestType(), null, 0, superKeyCacheItem, httpResponse.getContentType(), httpResponse.getContentEncoding(), httpResponse.getContentLength());
                    threadSafelyLinkedHasMapCacheItem.putAndSortRequestWithInit(superKeyCacheItem, cacheItem, 2);
                }
            } else if (httpResponse.getStatus() == HttpResponse.CANCEL_AFTER_OK && request.getModelType() == Request.BREAKPOINT_DOWNLOAD_REQUEST && request.isCacheInDisk()) {
                SuperKey superKeyCacheItem = new SuperKey(request.getSuperKey().getKey(), RequestManager.BREAKPOINTDOWNLOADREQUEST_TAG, 11, 2, request.getSuperKey().getTime());
                CacheItem cacheItem = new CacheItem(request.getUrlString(), request.getModelType(), request.getRequestType(), null, 0, superKeyCacheItem, httpResponse.getContentType(), httpResponse.getContentEncoding(), httpResponse.getContentLength());
                threadSafelyLinkedHasMapCacheItem.putAndSortRequestWithInit(superKeyCacheItem, cacheItem, 2);
            }
            mainResponseHandler.sendResponseMessage(request, httpResponse);

            threadSafelyLinkedHasMapRequest_Network.removeRunningRequest(request.getSuperKey());
            request.setRunningStatus(Request.FINISH);
        }
    }

}

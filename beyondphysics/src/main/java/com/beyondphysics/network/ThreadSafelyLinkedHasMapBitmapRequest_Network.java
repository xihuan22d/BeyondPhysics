package com.beyondphysics.network;


import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/9/4.
 */

public class ThreadSafelyLinkedHasMapBitmapRequest_Network extends ThreadSafelyLinkedHasMapBitmapRequest {


    public ThreadSafelyLinkedHasMapBitmapRequest_Network(ResponseHandler mainResponseHandler, ThreadSafelyArrayListRequestStatusItem threadSafelyArrayListRequestStatusItem) {
        super(mainResponseHandler, threadSafelyArrayListRequestStatusItem);
    }


    /**
     * 与普通网络请求有所不同,主要用在BaseBitmapNetworkThread
     * 通过匹配url会取得最顶部的不处于运行状态的请求,并弹出,并转移
     */
    public BitmapRequest takeTopUnRunningRequestByUrlAndPutToRunning() {
        synchronized (this) {
            Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, BitmapRequest<?>> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                BitmapRequest<?> bitmapRequest = entry.getValue();
                boolean bitmapRequestIsRunning = false;
                Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> theIterator = runningLinkedHashMapRequests.entrySet().iterator();
                while (theIterator.hasNext()) {
                    Map.Entry<SuperKey, BitmapRequest<?>> theEntry = theIterator.next();
                    BitmapRequest<?> theBitmapRequest = theEntry.getValue();
                    if (bitmapRequest.getUrlString().equals(theBitmapRequest.getUrlString())) {
                        bitmapRequestIsRunning = true;
                        break;
                    }
                }
                if (!bitmapRequestIsRunning) {
                    if (!threadSafelyArrayListRequestStatusItem.isRequestPauseOrCanceling(bitmapRequest, RequestStatusItem.KIND_BITMAP_REQUEST)) {
                        runningLinkedHashMapRequests.put(superKey, bitmapRequest);
                        removeRequest(superKey);
                        return bitmapRequest;
                    }
                }
            }
            return null;
        }
    }


    /**
     * 移除正在运行的请求,并且立即刷新相同key的请求,主要用在BaseBitmapNetworkThread
     * 移除并返回相同url的所有图片请求
     * return NotNull
     */
    public List<BitmapRequest<?>> removeRunningRequestAndRefreshSameKeyRemoveSameUrl(SuperKey superKey, HttpResponse httpResponse, String url, CacheItem cacheItem) {
        List<SuperKey> sameUrlSuperKeys = new ArrayList<SuperKey>();
        List<BitmapRequest<?>> sameUrlBitmapRequests = new ArrayList<BitmapRequest<?>>();
        if (superKey == null) {
            return sameUrlBitmapRequests;
        }
        synchronized (this) {
            if (runningLinkedHashMapRequests.containsKey(superKey)) {
                runningLinkedHashMapRequests.remove(superKey);
            }
            if (httpResponse != null && httpResponse.getStatus() == HttpResponse.SUCCESS) {
                List<SuperKey> superKeys = new ArrayList<SuperKey>();
                List<BitmapRequest<?>> bitmapRequests = new ArrayList<BitmapRequest<?>>();
                Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<SuperKey, BitmapRequest<?>> entry = iterator.next();
                    SuperKey theSuperKey = entry.getKey();
                    BitmapRequest<?> theBitmapRequest = entry.getValue();
                    if (superKey.getKey().equals(theSuperKey.getKey())) {
                        if (!threadSafelyArrayListRequestStatusItem.isRequestPauseOrCanceling(theBitmapRequest, RequestStatusItem.KIND_BITMAP_REQUEST)) {
                            superKeys.add(theSuperKey);
                            bitmapRequests.add(theBitmapRequest);
                        }
                    } else {
                        if (url != null && url.equals(theBitmapRequest.getUrlString()) && !theBitmapRequest.isLostCache()) {//只允许没有在BaseDiskCacheThread执行过的请求移除去加入到BaseDiskCacheThread执行
                            if (!threadSafelyArrayListRequestStatusItem.isRequestPauseOrCanceling(theBitmapRequest, RequestStatusItem.KIND_BITMAP_REQUEST)) {
                                sameUrlSuperKeys.add(theSuperKey);
                                sameUrlBitmapRequests.add(theBitmapRequest);
                            }
                        }
                    }
                }
                for (int i = 0; i < superKeys.size(); i++) {
                    mainResponseHandler.sendResponseMessage(bitmapRequests.get(i), httpResponse);
                    removeRequest(superKeys.get(i));
                }
                if (url != null) {
                    for (int i = 0; i < sameUrlSuperKeys.size(); i++) {
                        BitmapRequest<?> bitmapRequest = sameUrlBitmapRequests.get(i);
                        if (cacheItem != null) {
                            bitmapRequest.setCachePath(cacheItem.getCachePath());
                        }
                        removeRequest(sameUrlSuperKeys.get(i));
                    }
                }
            }
            return sameUrlBitmapRequests;
        }
    }


    @Override
    public void cancelRequestWithSuperKeyByWait(SuperKey superKey, boolean removeListener) {
        if (superKey == null) {
            return;
        }
        BitmapRequest<?> needWaitRunningBitmapRequest = null;
        synchronized (this) {
            if (linkedHashMapRequests.containsKey(superKey)) {
                BitmapRequest<?> bitmapRequest = linkedHashMapRequests.get(superKey);
                bitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(bitmapRequest, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(bitmapRequest, httpResponse);
                removeRequest(superKey);
            }
            if (runningLinkedHashMapRequests.containsKey(superKey)) {
                needWaitRunningBitmapRequest = runningLinkedHashMapRequests.get(superKey);
                needWaitRunningBitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(needWaitRunningBitmapRequest, removeListener);
            }
        }
        if (needWaitRunningBitmapRequest != null) {
            int count = 0;
            if (needWaitRunningBitmapRequest.isLostCache()) {
                while (needWaitRunningBitmapRequest.getLostCacheRunningStatus() != Request.FINISH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count = count + 10;
                    if (count >= 5000) {
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_Network_cancelRequestWithSuperKeyByWait:LostCache关闭请求超时", null, 1);
                        count = 0;
                    }
                }
            } else {
                while (needWaitRunningBitmapRequest.getRunningStatus() != Request.FINISH && needWaitRunningBitmapRequest.getFindCacheRunningStatus() != Request.FINISH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count = count + 10;
                    if (count >= 5000) {
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_Network_cancelRequestWithSuperKeyByWait:关闭请求超时", null, 1);
                        count = 0;
                    }
                }
            }

        }
    }


    @Override
    public void cancelRequestWithTagByWait(String tag, boolean removeListener) {
        if (tag == null) {
            tag = SuperKey.DEFAULT_TAG;
        }
        List<BitmapRequest<?>> needWaitRunningBitmapRequests = null;
        synchronized (this) {
            List<BitmapRequest<?>> bitmapRequests = findRequestWithTag(1, tag);
            for (int i = 0; i < bitmapRequests.size(); i++) {
                BitmapRequest<?> bitmapRequest = bitmapRequests.get(i);
                bitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(bitmapRequest, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(bitmapRequest, httpResponse);
                removeRequest(bitmapRequest.getSuperKey());
            }
            needWaitRunningBitmapRequests = findRequestWithTag(2, tag);
            for (int i = 0; i < needWaitRunningBitmapRequests.size(); i++) {
                BitmapRequest<?> runningBitmapRequest = needWaitRunningBitmapRequests.get(i);
                runningBitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(runningBitmapRequest, removeListener);
            }
        }
        for (int i = 0; i < needWaitRunningBitmapRequests.size(); i++) {
            BitmapRequest<?> runningBitmapRequest = needWaitRunningBitmapRequests.get(i);
            int count = 0;
            if (runningBitmapRequest.isLostCache()) {
                while (runningBitmapRequest.getLostCacheRunningStatus() != Request.FINISH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count = count + 10;
                    if (count >= 5000) {
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_Network_cancelRequestsWithTagByWait:LostCache关闭请求超时", null, 1);
                        count = 0;
                    }
                }
            } else {
                while (runningBitmapRequest.getRunningStatus() != Request.FINISH && runningBitmapRequest.getFindCacheRunningStatus() != Request.FINISH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count = count + 10;
                    if (count >= 5000) {
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_Network_cancelRequestsWithTagByWait:关闭请求超时", null, 1);
                        count = 0;
                    }
                }
            }

        }
    }


    @Override
    public void cancelAllRequestByWait(boolean removeListener) {
        List<BitmapRequest<?>> needWaitRunningBitmapRequests = null;
        synchronized (this) {
            List<BitmapRequest<?>> bitmapRequests = getAllRequest(1);
            for (int i = 0; i < bitmapRequests.size(); i++) {
                BitmapRequest<?> bitmapRequest = bitmapRequests.get(i);
                bitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(bitmapRequest, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(bitmapRequest, httpResponse);
                removeRequest(bitmapRequest.getSuperKey());
            }

            needWaitRunningBitmapRequests = getAllRequest(2);
            for (int i = 0; i < needWaitRunningBitmapRequests.size(); i++) {
                BitmapRequest<?> runningBitmapRequest = needWaitRunningBitmapRequests.get(i);
                runningBitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(runningBitmapRequest, removeListener);
            }
        }
        for (int i = 0; i < needWaitRunningBitmapRequests.size(); i++) {
            BitmapRequest<?> runningBitmapRequest = needWaitRunningBitmapRequests.get(i);
            int count = 0;
            if (runningBitmapRequest.isLostCache()) {
                while (runningBitmapRequest.getLostCacheRunningStatus() != Request.FINISH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count = count + 10;
                    if (count >= 5000) {
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_Network_cancelAllRequestsByWait:LostCache关闭请求超时", null, 1);
                        count = 0;
                    }
                }
            } else {
                while (runningBitmapRequest.getRunningStatus() != Request.FINISH && runningBitmapRequest.getFindCacheRunningStatus() != Request.FINISH) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count = count + 10;
                    if (count >= 5000) {
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_Network_cancelAllRequestsByWait:关闭请求超时", null, 1);
                        count = 0;
                    }
                }
            }
        }
    }

}

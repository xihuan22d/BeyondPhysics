package com.beyondphysics.network;


import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadSafelyLinkedHasMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/9/4.
 */

public class ThreadSafelyLinkedHasMapBitmapRequest extends ThreadSafelyLinkedHasMap<BitmapRequest<?>> {
    /**
     * 正在运行的请求,不要在外界直接操作,否则可能导致线程不安全
     */
    public final LinkedHashMap<SuperKey, BitmapRequest<?>> runningLinkedHashMapRequests = new LinkedHashMap<SuperKey, BitmapRequest<?>>();
    /**
     * NotNull
     */
    protected final ResponseHandler mainResponseHandler;

    /**
     * NotNull
     */
    protected final ThreadSafelyArrayListRequestStatusItem threadSafelyArrayListRequestStatusItem;

    public ThreadSafelyLinkedHasMapBitmapRequest(ResponseHandler mainResponseHandler, ThreadSafelyArrayListRequestStatusItem threadSafelyArrayListRequestStatusItem) {
        if (mainResponseHandler == null) {
            throw new NullPointerException("mainResponseHandler为null");
        } else {
            this.mainResponseHandler = mainResponseHandler;
        }
        if (threadSafelyArrayListRequestStatusItem == null) {
            throw new NullPointerException("threadSafelyArrayListRequestStatusItem为null");
        } else {
            this.threadSafelyArrayListRequestStatusItem = threadSafelyArrayListRequestStatusItem;
        }
    }


    public ResponseHandler getMainResponseHandler() {
        return mainResponseHandler;
    }


    public void putRequests(List<BitmapRequest<?>> bitmapRequests) {
        if (bitmapRequests == null) {
            return;
        }
        synchronized (this) {
            boolean needSort = false;
            for (int i = 0; i < bitmapRequests.size(); i++) {
                BitmapRequest<?> bitmapRequest = bitmapRequests.get(i);
                if (bitmapRequest != null) {
                    SuperKey superKey = bitmapRequest.getSuperKey();
                    if (putEventBefore(superKey, bitmapRequest)) {
                        linkedHashMapRequests.put(superKey, bitmapRequest);
                        putEventAfter(superKey, bitmapRequest);
                    }
                    if (bitmapRequest.isNeedSort()) {
                        needSort = true;
                    }
                }
            }
            if (needSort) {
                sortRequestDown();
            }
        }
    }


    /**
     * 与普通网络请求有所不同,主要用在BaseDiskCacheThread
     * 通过匹配key取得最顶部的不处于运行状态的请求,并弹出,并转移
     */
    public BitmapRequest takeTopUnRunningRequestByKeyAndPutToRunning() {
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
                    SuperKey theSuperKey = theEntry.getKey();
                    if (superKey.getKey().equals(theSuperKey.getKey())) {
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

    public void removeRunningRequest(SuperKey superKey) {
        if (superKey == null) {
            return;
        }
        synchronized (this) {
            if (runningLinkedHashMapRequests.containsKey(superKey)) {
                runningLinkedHashMapRequests.remove(superKey);
            }
        }
    }


    /**
     * 移除正在运行的请求,并且立即刷新相同key的请求,主要用在BaseDiskCacheThread
     * return NotNull
     */
    public void removeRunningRequestAndRefreshSameKey(SuperKey superKey, HttpResponse httpResponse) {
        if (superKey == null) {
            return;
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
                    }
                }
                for (int i = 0; i < superKeys.size(); i++) {
                    mainResponseHandler.sendResponseMessage(bitmapRequests.get(i), httpResponse);
                    removeRequest(superKeys.get(i));
                }
            }
        }
    }


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
            while (needWaitRunningBitmapRequest.getRunningStatus() != Request.FINISH) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = count + 10;
                if (count >= 5000) {
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_cancelRequestWithSuperKeyByWait:关闭请求超时", null, 1);
                    count = 0;
                }
            }
        }
    }

    public void cancelRequestWithSuperKey(SuperKey superKey, boolean removeListener) {
        if (superKey == null) {
            return;
        }
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
                BitmapRequest<?> bitmapRequest = runningLinkedHashMapRequests.get(superKey);
                bitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(bitmapRequest, removeListener);
            }
        }
    }

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
            while (runningBitmapRequest.getRunningStatus() != Request.FINISH) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = count + 10;
                if (count >= 5000) {
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_cancelRequestsWithTagByWait:关闭请求超时", null, 1);
                    count = 0;
                }
            }
        }
    }

    public void cancelRequestWithTag(String tag, boolean removeListener) {
        if (tag == null) {
            tag = SuperKey.DEFAULT_TAG;
        }
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

            List<BitmapRequest<?>> runningBitmapRequests = findRequestWithTag(2, tag);
            for (int i = 0; i < runningBitmapRequests.size(); i++) {
                BitmapRequest<?> runningBitmapRequest = runningBitmapRequests.get(i);
                runningBitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(runningBitmapRequest, removeListener);
            }
        }
    }

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
            while (runningBitmapRequest.getRunningStatus() != Request.FINISH) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = count + 10;
                if (count >= 5000) {
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapBitmapRequest_cancelAllRequestsByWait:关闭请求超时", null, 1);
                    count = 0;
                }
            }
        }
    }

    public void cancelAllRequest(boolean removeListener) {
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

            List<BitmapRequest<?>> runningBitmapRequests = getAllRequest(2);
            for (int i = 0; i < runningBitmapRequests.size(); i++) {
                BitmapRequest<?> runningBitmapRequest = runningBitmapRequests.get(i);
                runningBitmapRequest.cancelRequest();
                Request.removeBitmapRequestListener(runningBitmapRequest, removeListener);
            }
        }
    }

    /**
     * 根据tag查找符合条件的Request
     * type为1表示在linkedHashMapRequests内进行查找,type为2表示在runningLinkedHashMapRequests内进行查找
     * return NotNull
     */
    public List<BitmapRequest<?>> findRequestWithTag(int type, String tag) {
        synchronized (this) {
            List<BitmapRequest<?>> bitmapRequests = new ArrayList<BitmapRequest<?>>();
            if (tag != null) {
                if (type == 1) {
                    Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<SuperKey, BitmapRequest<?>> entry = iterator.next();
                        SuperKey superKey = entry.getKey();
                        BitmapRequest<?> bitmapRequest = entry.getValue();
                        if (tag.equals(superKey.getTag())) {
                            bitmapRequests.add(bitmapRequest);
                        }
                    }
                } else if (type == 2) {
                    Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> iterator = runningLinkedHashMapRequests.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<SuperKey, BitmapRequest<?>> entry = iterator.next();
                        SuperKey superKey = entry.getKey();
                        BitmapRequest<?> bitmapRequest = entry.getValue();
                        if (tag.equals(superKey.getTag())) {
                            bitmapRequests.add(bitmapRequest);
                        }
                    }
                }
            }
            return bitmapRequests;
        }
    }

    /**
     * type为1表示返回所有linkedHashMapRequests内请求,type为2表示返回所有runningLinkedHashMapRequests内请求
     * return NotNull
     */
    public List<BitmapRequest<?>> getAllRequest(int type) {
        synchronized (this) {
            List<BitmapRequest<?>> bitmapRequests = new ArrayList<BitmapRequest<?>>();
            if (type == 1) {
                Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<SuperKey, BitmapRequest<?>> entry = iterator.next();
                    BitmapRequest<?> bitmapRequest = entry.getValue();
                    bitmapRequests.add(bitmapRequest);
                }
            } else if (type == 2) {
                Iterator<Map.Entry<SuperKey, BitmapRequest<?>>> iterator = runningLinkedHashMapRequests.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<SuperKey, BitmapRequest<?>> entry = iterator.next();
                    BitmapRequest<?> bitmapRequest = entry.getValue();
                    bitmapRequests.add(bitmapRequest);
                }
            }
            return bitmapRequests;
        }
    }

}

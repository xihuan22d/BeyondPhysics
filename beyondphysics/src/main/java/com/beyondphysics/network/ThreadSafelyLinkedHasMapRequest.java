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
 * Created by xihuan22 on 2017/8/17.
 */

public class ThreadSafelyLinkedHasMapRequest extends ThreadSafelyLinkedHasMap<Request<?>> {
    /**
     * 正在运行的请求,不要在外界直接操作,否则可能导致线程不安全
     */
    public final LinkedHashMap<SuperKey, Request<?>> runningLinkedHashMapRequests = new LinkedHashMap<SuperKey, Request<?>>();
    /**
     * NotNull
     */
    private final ResponseHandler mainResponseHandler;
    /**
     * NotNull
     */
    private final ThreadSafelyArrayListRequestStatusItem threadSafelyArrayListRequestStatusItem;

    public ThreadSafelyLinkedHasMapRequest(ResponseHandler mainResponseHandler, ThreadSafelyArrayListRequestStatusItem threadSafelyArrayListRequestStatusItem) {
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

    public ThreadSafelyArrayListRequestStatusItem getThreadSafelyArrayListRequestStatusItem() {
        return threadSafelyArrayListRequestStatusItem;
    }

    /**
     * 取得最顶部的请求,并弹出,并转移
     */
    public Request<?> takeTopRequestAndPutToRunning() {
        synchronized (this) {
            Iterator<Map.Entry<SuperKey, Request<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, Request<?>> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                Request<?> request = entry.getValue();
                if (superKey != null) {
                    if (!threadSafelyArrayListRequestStatusItem.isRequestPauseOrCanceling(request, RequestStatusItem.KIND_NORMAL_REQUEST)) {
                        runningLinkedHashMapRequests.put(superKey, request);
                        removeRequest(superKey);
                        return request;
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


    public void cancelRequestWithSuperKeyByWait(SuperKey superKey, boolean removeListener) {
        if (superKey == null) {
            return;
        }
        Request<?> needWaitRunningRequest = null;
        synchronized (this) {
            if (linkedHashMapRequests.containsKey(superKey)) {
                Request<?> request = linkedHashMapRequests.get(superKey);
                request.cancelRequest();
                Request.removeListener(request, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(request, httpResponse);
                removeRequest(superKey);
            }
            if (runningLinkedHashMapRequests.containsKey(superKey)) {
                needWaitRunningRequest = runningLinkedHashMapRequests.get(superKey);
                needWaitRunningRequest.cancelRequest();
                Request.removeListener(needWaitRunningRequest, removeListener);
            }
        }
        if (needWaitRunningRequest != null) {
            int count = 0;
            while (needWaitRunningRequest.getRunningStatus() != Request.FINISH) {//FINISH了表示已经执行removeRunningRequest了,没必要再移除一次,如果执行cancelRequestWithSuperKeyByWait的线程和setRunningStatus的线程是同一个线程将会导致死锁
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = count + 10;
                if (count >= 5000) {
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapRequest_cancelRequestWithSuperKeyByWait:关闭请求超时", null, 1);
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
                Request<?> request = linkedHashMapRequests.get(superKey);
                request.cancelRequest();
                Request.removeListener(request, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(request, httpResponse);
                removeRequest(superKey);
            }
            if (runningLinkedHashMapRequests.containsKey(superKey)) {
                Request<?> request = runningLinkedHashMapRequests.get(superKey);
                request.cancelRequest();//只取消,移除操作让回调方法调用,这样保证了一旦runningLinkedHashMapRequests数据为0就肯定没有正在运行的请求了
                Request.removeListener(request, removeListener);
            }
        }
    }

    public void cancelRequestWithTagByWait(String tag, boolean removeListener) {
        if (tag == null) {
            tag = SuperKey.DEFAULT_TAG;
        }
        List<Request<?>> needWaitRunningRequests = null;
        synchronized (this) {
            List<Request<?>> requests = findRequestWithTag(1, tag);
            for (int i = 0; i < requests.size(); i++) {
                Request<?> request = requests.get(i);
                request.cancelRequest();
                Request.removeListener(request, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(request, httpResponse);
                removeRequest(request.getSuperKey());
            }
            needWaitRunningRequests = findRequestWithTag(2, tag);
            for (int i = 0; i < needWaitRunningRequests.size(); i++) {
                Request<?> runningRequest = needWaitRunningRequests.get(i);
                runningRequest.cancelRequest();
                Request.removeListener(runningRequest, removeListener);
            }
        }
        for (int i = 0; i < needWaitRunningRequests.size(); i++) {
            Request<?> runningRequest = needWaitRunningRequests.get(i);
            int count = 0;
            while (runningRequest.getRunningStatus() != Request.FINISH) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = count + 10;
                if (count >= 5000) {
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapRequest_cancelRequestsWithTagByWait:关闭请求超时" + runningRequest.getUrlString() + ";" + runningRequest.getSuperKey().getTime(), null, 1);
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
            List<Request<?>> requests = findRequestWithTag(1, tag);
            for (int i = 0; i < requests.size(); i++) {
                Request<?> request = requests.get(i);
                request.cancelRequest();
                Request.removeListener(request, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(request, httpResponse);
                removeRequest(request.getSuperKey());
            }

            List<Request<?>> runningRequests = findRequestWithTag(2, tag);
            for (int i = 0; i < runningRequests.size(); i++) {
                Request<?> runningRequest = runningRequests.get(i);
                runningRequest.cancelRequest();
                Request.removeListener(runningRequest, removeListener);
            }
        }
    }

    public void cancelAllRequestByWait(boolean removeListener) {
        List<Request<?>> needWaitRunningRequests = null;
        synchronized (this) {
            List<Request<?>> requests = getAllRequest(1);
            for (int i = 0; i < requests.size(); i++) {
                Request<?> request = requests.get(i);
                request.cancelRequest();
                Request.removeListener(request, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(request, httpResponse);
                removeRequest(request.getSuperKey());
            }
            needWaitRunningRequests = getAllRequest(2);
            for (int i = 0; i < needWaitRunningRequests.size(); i++) {
                Request<?> runningRequest = needWaitRunningRequests.get(i);
                runningRequest.cancelRequest();
                Request.removeListener(runningRequest, removeListener);
            }
        }
        for (int i = 0; i < needWaitRunningRequests.size(); i++) {
            Request<?> runningRequest = needWaitRunningRequests.get(i);
            int count = 0;
            while (runningRequest.getRunningStatus() != Request.FINISH) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = count + 10;
                if (count >= 5000) {
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ThreadSafelyLinkedHasMapRequest_cancelAllRequestsByWait:关闭请求超时", null, 1);
                    count = 0;
                }
            }
        }
    }

    public void cancelAllRequest(boolean removeListener) {
        synchronized (this) {
            List<Request<?>> requests = getAllRequest(1);
            for (int i = 0; i < requests.size(); i++) {
                Request<?> request = requests.get(i);
                request.cancelRequest();
                Request.removeListener(request, removeListener);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                mainResponseHandler.sendResponseMessage(request, httpResponse);
                removeRequest(request.getSuperKey());
            }

            List<Request<?>> runningRequests = getAllRequest(2);
            for (int i = 0; i < runningRequests.size(); i++) {
                Request<?> runningRequest = runningRequests.get(i);
                runningRequest.cancelRequest();
                Request.removeListener(runningRequest, removeListener);
            }
        }
    }

    /**
     * 根据tag查找符合条件的Request
     * type为1表示在linkedHashMapRequests内进行查找,type为2表示在runningLinkedHashMapRequests内进行查找
     * return NotNull
     */
    public List<Request<?>> findRequestWithTag(int type, String tag) {
        synchronized (this) {
            List<Request<?>> requests = new ArrayList<Request<?>>();
            if (tag != null) {
                if (type == 1) {
                    Iterator<Map.Entry<SuperKey, Request<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<SuperKey, Request<?>> entry = iterator.next();
                        SuperKey superKey = entry.getKey();
                        Request<?> request = entry.getValue();
                        if (tag.equals(superKey.getTag())) {
                            requests.add(request);
                        }
                    }
                } else if (type == 2) {
                    Iterator<Map.Entry<SuperKey, Request<?>>> iterator = runningLinkedHashMapRequests.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<SuperKey, Request<?>> entry = iterator.next();
                        SuperKey superKey = entry.getKey();
                        Request<?> request = entry.getValue();
                        if (tag.equals(superKey.getTag())) {
                            requests.add(request);
                        }
                    }
                }
            }
            return requests;
        }
    }

    /**
     * type为1表示返回所有linkedHashMapRequests内请求,type为2表示返回所有runningLinkedHashMapRequests内请求
     * return NotNull
     */
    public List<Request<?>> getAllRequest(int type) {
        synchronized (this) {
            List<Request<?>> requests = new ArrayList<Request<?>>();
            if (type == 1) {
                Iterator<Map.Entry<SuperKey, Request<?>>> iterator = linkedHashMapRequests.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<SuperKey, Request<?>> entry = iterator.next();
                    Request<?> request = entry.getValue();
                    requests.add(request);
                }
            } else if (type == 2) {
                Iterator<Map.Entry<SuperKey, Request<?>>> iterator = runningLinkedHashMapRequests.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<SuperKey, Request<?>> entry = iterator.next();
                    Request<?> request = entry.getValue();
                    requests.add(request);
                }
            }
            return requests;
        }
    }


}

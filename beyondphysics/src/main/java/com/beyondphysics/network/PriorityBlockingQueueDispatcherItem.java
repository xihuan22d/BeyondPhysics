package com.beyondphysics.network;


import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 通过该容器可以使得主线程进行put操作的时候等待锁的时间降到最低,分发线程通过读取priorityBlockingQueue内的数据使得与主线程争夺锁的时间降到最低
 * Created by xihuan22 on 2017/8/17.
 */

public class PriorityBlockingQueueDispatcherItem {

    private final HashMap<SuperKey, DispatcherItem> hashMapDispatcherItems = new HashMap<SuperKey, DispatcherItem>();

    private final PriorityBlockingQueue<DispatcherItem> priorityBlockingQueue = new PriorityBlockingQueue<DispatcherItem>();
    /**
     * NotNull
     */
    private final ResponseHandler mainResponseHandler;


    public PriorityBlockingQueueDispatcherItem(ResponseHandler mainResponseHandler) {
        if (mainResponseHandler == null) {
            throw new NullPointerException("mainResponseHandler为null");
        } else {
            this.mainResponseHandler = mainResponseHandler;
        }
    }

    public ResponseHandler getMainResponseHandler() {
        return mainResponseHandler;
    }


    public boolean putDispatcherItem(SuperKey superKey, DispatcherItem dispatcherItem) {
        boolean canPut = false;
        if (superKey == null || dispatcherItem == null) {
            return canPut;
        }
        synchronized (hashMapDispatcherItems) {
            hashMapDispatcherItems.put(superKey, dispatcherItem);//必须放在priorityBlockingQueue.add(dispatcherItem)之前
        }
        try {
            canPut = priorityBlockingQueue.add(dispatcherItem);
        } catch (Exception e) {
            e.printStackTrace();
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "PriorityBlockingQueueDispatcherItem_putDispatcherItem:无法加入到队列", e, 1);
        }
        if (!canPut) {
            removeDispatcherItem(superKey);
        }
        return canPut;
    }


    public void removeDispatcherItem(SuperKey superKey) {
        if (superKey == null) {
            return;
        }
        synchronized (hashMapDispatcherItems) {
            if (hashMapDispatcherItems.containsKey(superKey)) {
                hashMapDispatcherItems.remove(superKey);
            }
        }
    }


    public DispatcherItem takeTopDispatcherItem() {
        DispatcherItem dispatcherItem = null;
        try {
            dispatcherItem = priorityBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dispatcherItem;
    }


    public void cancelDispatcherItemWithSuperKey(SuperKey superKey, boolean removeListener) {
        if (superKey == null) {
            return;
        }
        synchronized (hashMapDispatcherItems) {
            if (hashMapDispatcherItems.containsKey(superKey)) {
                DispatcherItem dispatcherItem = hashMapDispatcherItems.get(superKey);
                Request<?> request = getRequestFromDispatcherItem(dispatcherItem);
                if (request != null) {
                    request.cancelRequest();
                    Request.removeListener(request, removeListener);
                    HttpResponse httpResponse = new HttpResponse();
                    httpResponse.setStatus(HttpResponse.CANCEL);
                    httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                    mainResponseHandler.sendResponseMessage(request, httpResponse);
                } else {
                    CacheItemRequest cacheItemRequest = getCacheItemRequestFromDispatcherItem(dispatcherItem);
                    if (cacheItemRequest != null) {
                        cacheItemRequest.cancelRequest();
                        CacheItemRequest.removeCacheItemListener(cacheItemRequest, removeListener);
                        cacheItemRequest.setCacheItems(new ArrayList<CacheItem>());
                        mainResponseHandler.sendCacheItemMessage(cacheItemRequest);
                    }
                }
                removeDispatcherItem(superKey);
            }
        }
    }


    public void cancelDispatcherItemWithTag(String tag, boolean removeListener) {
        if (tag == null) {
            tag = SuperKey.DEFAULT_TAG;
        }
        synchronized (hashMapDispatcherItems) {
            List<DispatcherItem> dispatcherItems = findDispatcherItemWithTag(tag);
            for (int i = 0; i < dispatcherItems.size(); i++) {
                DispatcherItem dispatcherItem = dispatcherItems.get(i);
                Request<?> request = getRequestFromDispatcherItem(dispatcherItem);
                if (request != null) {
                    request.cancelRequest();
                    Request.removeListener(request, removeListener);
                    HttpResponse httpResponse = new HttpResponse();
                    httpResponse.setStatus(HttpResponse.CANCEL);
                    httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                    mainResponseHandler.sendResponseMessage(request, httpResponse);
                } else {
                    CacheItemRequest cacheItemRequest = getCacheItemRequestFromDispatcherItem(dispatcherItem);
                    if (cacheItemRequest != null) {
                        cacheItemRequest.cancelRequest();
                        CacheItemRequest.removeCacheItemListener(cacheItemRequest, removeListener);
                        cacheItemRequest.setCacheItems(new ArrayList<CacheItem>());
                        mainResponseHandler.sendCacheItemMessage(cacheItemRequest);
                    }
                }
                removeDispatcherItem(dispatcherItem.getSuperKey());
            }
        }
    }


    public void cancelAllDispatcherItem(boolean removeListener) {
        synchronized (hashMapDispatcherItems) {
            List<DispatcherItem> dispatcherItems = getAllDispatcherItem();
            for (int i = 0; i < dispatcherItems.size(); i++) {
                DispatcherItem dispatcherItem = dispatcherItems.get(i);
                Request<?> request = getRequestFromDispatcherItem(dispatcherItem);
                if (request != null) {
                    request.cancelRequest();
                    Request.removeListener(request, removeListener);
                    HttpResponse httpResponse = new HttpResponse();
                    httpResponse.setStatus(HttpResponse.CANCEL);
                    httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                    mainResponseHandler.sendResponseMessage(request, httpResponse);
                } else {
                    CacheItemRequest cacheItemRequest = getCacheItemRequestFromDispatcherItem(dispatcherItem);
                    if (cacheItemRequest != null) {
                        cacheItemRequest.cancelRequest();
                        CacheItemRequest.removeCacheItemListener(cacheItemRequest, removeListener);
                        cacheItemRequest.setCacheItems(new ArrayList<CacheItem>());
                        mainResponseHandler.sendCacheItemMessage(cacheItemRequest);
                    }
                }
                removeDispatcherItem(dispatcherItem.getSuperKey());
            }
        }
    }


    public List<DispatcherItem> findDispatcherItemWithTag(String tag) {
        synchronized (hashMapDispatcherItems) {
            List<DispatcherItem> dispatcherItems = new ArrayList<DispatcherItem>();
            if (tag != null) {
                Iterator<Map.Entry<SuperKey, DispatcherItem>> iterator = hashMapDispatcherItems.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<SuperKey, DispatcherItem> entry = iterator.next();
                    SuperKey superKey = entry.getKey();
                    DispatcherItem dispatcherItem = entry.getValue();
                    if (tag.equals(superKey.getTag())) {
                        dispatcherItems.add(dispatcherItem);
                    }
                }
            }
            return dispatcherItems;
        }
    }

    public List<DispatcherItem> getAllDispatcherItem() {
        synchronized (hashMapDispatcherItems) {
            List<DispatcherItem> dispatcherItems = new ArrayList<DispatcherItem>();
            Iterator<Map.Entry<SuperKey, DispatcherItem>> iterator = hashMapDispatcherItems.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, DispatcherItem> entry = iterator.next();
                DispatcherItem dispatcherItem = entry.getValue();
                dispatcherItems.add(dispatcherItem);
            }
            return dispatcherItems;
        }
    }

    private Request<?> getRequestFromDispatcherItem(DispatcherItem dispatcherItem) {
        Request<?> request = null;
        if (dispatcherItem == null) {
            return null;
        }
        int type = dispatcherItem.getType();
        if (type == DispatcherItem.REQUEST_ADD || type == DispatcherItem.REQUEST_ADD_WITHSORT || type == DispatcherItem.BITMAPREQUEST_ADD_DISKCACHE || type == DispatcherItem.BITMAPREQUEST_ADD_DISKCACHE_WITHSORT || type == DispatcherItem.BITMAPREQUEST_ADD_NETWORK || type == DispatcherItem.BITMAPREQUEST_ADD_NETWORK_WITHSORT) {
            request = (Request<?>) dispatcherItem.getObject();
        }
        return request;
    }

    private CacheItemRequest getCacheItemRequestFromDispatcherItem(DispatcherItem dispatcherItem) {
        CacheItemRequest cacheItemRequest = null;
        if (dispatcherItem == null) {
            return null;
        }
        int type = dispatcherItem.getType();
        if (type == DispatcherItem.DOCACHEITEMS) {
            cacheItemRequest = (CacheItemRequest) dispatcherItem.getObject();
        }
        return cacheItemRequest;
    }


}

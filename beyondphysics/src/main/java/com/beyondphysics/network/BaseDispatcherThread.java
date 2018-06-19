package com.beyondphysics.network;

import android.os.Process;

import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;
import com.beyondphysics.network.utils.SuperKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/10/30.
 */

public class BaseDispatcherThread extends BaseThread {
    /**
     * NotNull
     */
    private final RequestManagerUseDispatcher requestManagerUseDispatcher;
    /**
     * NotNull
     */
    protected final ResponseHandler mainResponseHandler;
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
    private final PriorityBlockingQueueDispatcherItem priorityBlockingQueueDispatcherItem;


    public BaseDispatcherThread(RequestManagerUseDispatcher requestManagerUseDispatcher) {
        super(Process.THREAD_PRIORITY_DEFAULT, 10);
        this.setName("BaseDispatcherThread");
        if (requestManagerUseDispatcher == null) {
            throw new NullPointerException("requestManagerUseDispatcher为null");
        } else {
            this.requestManagerUseDispatcher = requestManagerUseDispatcher;
            mainResponseHandler = this.requestManagerUseDispatcher.getMainResponseHandler();

            threadSafelyLinkedHasMapRequest_Network = this.requestManagerUseDispatcher.getThreadSafelyLinkedHasMapRequest_Network();
            threadSafelyLinkedHasMapBitmapRequest_Network = this.requestManagerUseDispatcher.getThreadSafelyLinkedHasMapBitmapRequest_Network();
            threadSafelyLinkedHasMapBitmapRequest_DiskCache = this.requestManagerUseDispatcher.getThreadSafelyLinkedHasMapBitmapRequest_DiskCache();
            threadSafelyLinkedHasMapCacheItem = this.requestManagerUseDispatcher.getThreadSafelyLinkedHasMapCacheItem();
            priorityBlockingQueueDispatcherItem = this.requestManagerUseDispatcher.getPriorityBlockingQueueDispatcherItem();
        }
    }


    @Override
    public void doWork() {
        DispatcherItem dispatcherItem = priorityBlockingQueueDispatcherItem.takeTopDispatcherItem();
        if (dispatcherItem != null) {
            dispatcherItem.setRunningStatus(DispatcherItem.RUNNING);
            int type = dispatcherItem.getType();
            switch (type) {
                case DispatcherItem.REQUEST_ADD:
                    Request<?> request = (Request<?>) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapRequest_Network.putRequest(request.getSuperKey(), request);
                    break;
                case DispatcherItem.REQUEST_ADD_WITHSORT:
                    Request<?> requestWithSort = (Request<?>) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapRequest_Network.putAndSortRequest(requestWithSort.getSuperKey(), requestWithSort, 1);
                    break;
                case DispatcherItem.BITMAPREQUEST_ADD_DISKCACHE:
                    BitmapRequest<?> bitmapRequestDiskCache = (BitmapRequest<?>) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putRequest(bitmapRequestDiskCache.getSuperKey(), bitmapRequestDiskCache);
                    break;
                case DispatcherItem.BITMAPREQUEST_ADD_DISKCACHE_WITHSORT:
                    BitmapRequest<?> bitmapRequestDiskCacheWithSort = (BitmapRequest<?>) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putAndSortRequest(bitmapRequestDiskCacheWithSort.getSuperKey(), bitmapRequestDiskCacheWithSort, 1);
                    break;
                case DispatcherItem.BITMAPREQUEST_ADD_NETWORK:
                    BitmapRequest<?> bitmapRequestNetwork = (BitmapRequest<?>) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_Network.putRequest(bitmapRequestNetwork.getSuperKey(), bitmapRequestNetwork);
                    break;
                case DispatcherItem.BITMAPREQUEST_ADD_NETWORK_WITHSORT:
                    BitmapRequest<?> bitmapRequestNetworkWithSort = (BitmapRequest<?>) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_Network.putAndSortRequest(bitmapRequestNetworkWithSort.getSuperKey(), bitmapRequestNetworkWithSort, 1);
                    break;
                case DispatcherItem.REQUEST_CANCEL:
                    SuperKey superKey = (SuperKey) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(superKey, false);
                    threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(superKey, false);
                    threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKey(superKey, false);
                    break;
                case DispatcherItem.BITMAPREQUEST_CANCEL:
                    SuperKey bitmapRequestSuperKey = (SuperKey) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(bitmapRequestSuperKey, false);
                    threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(bitmapRequestSuperKey, false);
                    break;
                case DispatcherItem.REQUEST_CANCELWITHTAG:
                    String tag = (String) dispatcherItem.getObject();
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithTag(tag, false);
                    threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithTag(tag, false);
                    threadSafelyLinkedHasMapRequest_Network.cancelRequestWithTag(tag, false);
                    break;
                case DispatcherItem.REQUEST_CANCELALL:
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequest(false);
                    threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequest(false);
                    threadSafelyLinkedHasMapRequest_Network.cancelAllRequest(false);
                    break;
                case DispatcherItem.DOCACHEITEMS:
                    CacheItemRequest cacheItemRequest = (CacheItemRequest) dispatcherItem.getObject();
                    if (cacheItemRequest != null) {
                        if (cacheItemRequest.getType() == CacheItemRequest.GETCACHEITEMS) {
                            List<CacheItem> cacheItems = threadSafelyLinkedHasMapCacheItem.getCacheItemsByKeys(cacheItemRequest.getKeys(), RequestManager.getTagByCacheItemTAG(cacheItemRequest.getCacheItemTAG()));
                            cacheItemRequest.setCacheItems(cacheItems);
                            mainResponseHandler.sendCacheItemMessage(cacheItemRequest);
                        } else if (cacheItemRequest.getType() == CacheItemRequest.CLEARCACHEITEMS) {
                            threadSafelyLinkedHasMapCacheItem.clearCacheItems(cacheItemRequest.getKeys(), RequestManager.getTagByCacheItemTAG(cacheItemRequest.getCacheItemTAG()));
                            cacheItemRequest.setCacheItems(new ArrayList<CacheItem>());
                            mainResponseHandler.sendCacheItemMessage(cacheItemRequest);
                        } else if (cacheItemRequest.getType() == CacheItemRequest.CLEARALLCACHEITEM) {
                            threadSafelyLinkedHasMapCacheItem.clearAllCacheItem();
                            cacheItemRequest.setCacheItems(new ArrayList<CacheItem>());
                            mainResponseHandler.sendCacheItemMessage(cacheItemRequest);
                        }
                    }
                    break;
                default:
                    break;
            }
            priorityBlockingQueueDispatcherItem.removeDispatcherItem(dispatcherItem.getSuperKey());
            dispatcherItem.setRunningStatus(DispatcherItem.FINISH);
        }

    }

}

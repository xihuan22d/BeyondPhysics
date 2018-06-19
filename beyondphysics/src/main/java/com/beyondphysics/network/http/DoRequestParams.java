package com.beyondphysics.network.http;


import com.beyondphysics.network.ResponseHandler;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;

/**
 * Created by xihuan22 on 2017/9/7.
 */

public class DoRequestParams {

    private final ResponseHandler mainResponseHandler;

    private final ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem;

    public DoRequestParams(ResponseHandler mainResponseHandler, ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem) {
        this.mainResponseHandler = mainResponseHandler;
        this.threadSafelyLinkedHasMapCacheItem = threadSafelyLinkedHasMapCacheItem;
    }

    public ResponseHandler getMainResponseHandler() {
        return mainResponseHandler;
    }

    public ThreadSafelyLinkedHasMapCacheItem getThreadSafelyLinkedHasMapCacheItem() {
        return threadSafelyLinkedHasMapCacheItem;
    }

    @Override
    public String toString() {
        return "DoRequestParams{" +
                "mainResponseHandler=" + mainResponseHandler +
                ", threadSafelyLinkedHasMapCacheItem=" + threadSafelyLinkedHasMapCacheItem +
                '}';
    }
}

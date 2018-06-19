package com.beyondphysics.network;

import android.os.Process;

import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;

/**
 * Created by xihuan22 on 2017/8/5.
 */

public class BaseCacheItemIOThread extends BaseThread {
    /**
     * NotNull
     */
    private final RequestManager requestManager;
    /**
     * NotNull
     */
    private final ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem;

    /**
     * 如果没有触发needWrite,并且次数到达了写入的次数就变为true,并停止计数
     */
    private boolean triggerSave = false;
    /**
     * 没有存储次数的累计
     */
    private int unSaveCount = 0;

    public BaseCacheItemIOThread(RequestManager requestManager) {
        super(Process.THREAD_PRIORITY_BACKGROUND, 100);
        this.setName("BaseCacheItemIOThread");
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
            threadSafelyLinkedHasMapCacheItem = this.requestManager.getThreadSafelyLinkedHasMapCacheItem();
        }
    }


    @Override
    public void doWork() {
        threadSafelyLinkedHasMapCacheItem.init();
        if (threadSafelyLinkedHasMapCacheItem.isNeedWrite()) {
            threadSafelyLinkedHasMapCacheItem.writeCacheInfo();
            triggerSave = false;
        } else {
            if (!triggerSave) {
                unSaveCount = unSaveCount + 1;
                if (unSaveCount >= 50) {
                    threadSafelyLinkedHasMapCacheItem.writeCacheInfo();
                    triggerSave = true;
                }
            }

        }
    }

}

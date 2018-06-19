package com.beyondphysics.network;


import com.beyondphysics.network.utils.FileTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/8/1.
 * CacheItem控制器,主要处理缓存文件的读写以及json解析
 */

public class BaseCacheItemIOContainer {
    /**
     * NotNull
     */
    private final List<BaseCacheItemIOThread> baseCacheItemIOThreads = new ArrayList<BaseCacheItemIOThread>();
    /**
     * NotNull
     */
    private final RequestManager requestManager;

    private final int cacheItemIOThreadCount = 1;


    public BaseCacheItemIOContainer(RequestManager requestManager) {
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
        }
    }

    /**
     * 初始化线程池
     */
    public void openAllThread() {
        synchronized (this) {
            if (baseCacheItemIOThreads.size() > 0) {
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BaseDiskCacheContainer_openAllThread:重复初始化线程池", null, 1);
            } else {
                for (int i = 0; i < cacheItemIOThreadCount; i++) {
                    BaseCacheItemIOThread baseCacheItemIOThread = new BaseCacheItemIOThread(requestManager);
                    baseCacheItemIOThread.threadOperation(BaseThread.OPENTHREAD);
                    baseCacheItemIOThreads.add(baseCacheItemIOThread);
                }
            }
        }
    }

    /**
     * 阻塞方式关闭所有线程
     */
    public void closeAllThreadByWait() {
        synchronized (this) {
            for (int i = 0; i < baseCacheItemIOThreads.size(); i++) {
                BaseCacheItemIOThread baseCacheItemIOThread = baseCacheItemIOThreads.get(i);
                baseCacheItemIOThread.threadOperation(BaseThread.CLOSETHREADBYWAIT);
            }
            baseCacheItemIOThreads.clear();
        }
    }

    /**
     * 非阻塞方式关闭所有线程
     */
    public void closeAllThread() {
        synchronized (this) {
            for (int i = 0; i < baseCacheItemIOThreads.size(); i++) {
                BaseCacheItemIOThread baseCacheItemIOThread = baseCacheItemIOThreads.get(i);
                baseCacheItemIOThread.threadOperation(BaseThread.CLOSETHREAD);
            }
            baseCacheItemIOThreads.clear();
        }
    }


}

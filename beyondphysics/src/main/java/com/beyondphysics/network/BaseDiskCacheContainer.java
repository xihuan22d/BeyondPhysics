package com.beyondphysics.network;


import com.beyondphysics.network.cache.BitmapDiskCacheAnalyze;
import com.beyondphysics.network.utils.FileTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/8/1.
 * 图片缓存控制器
 */

public class BaseDiskCacheContainer {
    /**
     * NotNull
     */
    private final List<BaseDiskCacheThread> baseDiskCacheThreads = new ArrayList<BaseDiskCacheThread>();
    /**
     * NotNull
     */
    private final RequestManager requestManager;

    private final int diskCacheThreadCount;
    /**
     * NotNull
     */
    private final BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze;

    public BaseDiskCacheContainer(RequestManager requestManager, int diskCacheThreadCount, BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze) {
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
        }
        if (diskCacheThreadCount <= 0) {
            diskCacheThreadCount = 2;
        }
        this.diskCacheThreadCount = diskCacheThreadCount;
        if (bitmapDiskCacheAnalyze == null) {
            throw new NullPointerException("bitmapDiskCacheAnalyze为null");
        } else {
            this.bitmapDiskCacheAnalyze = bitmapDiskCacheAnalyze;
        }
    }

    /**
     * 初始化线程池
     */
    public void openAllThread() {
        synchronized (this) {
            if (baseDiskCacheThreads.size() > 0) {
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BaseDiskCacheContainer_openAllThread:重复初始化线程池", null, 1);
            } else {
                for (int i = 0; i < diskCacheThreadCount; i++) {
                    BaseDiskCacheThread baseDiskCacheThread = new BaseDiskCacheThread(requestManager, bitmapDiskCacheAnalyze);
                    baseDiskCacheThread.threadOperation(BaseThread.OPENTHREAD);
                    baseDiskCacheThreads.add(baseDiskCacheThread);
                }
            }
        }
    }

    /**
     * 阻塞方式关闭所有线程
     */
    public void closeAllThreadByWait() {
        synchronized (this) {
            for (int i = 0; i < baseDiskCacheThreads.size(); i++) {
                BaseDiskCacheThread baseDiskCacheThread = baseDiskCacheThreads.get(i);
                baseDiskCacheThread.threadOperation(BaseThread.CLOSETHREADBYWAIT);
            }
            baseDiskCacheThreads.clear();
        }
    }

    /**
     * 非阻塞方式关闭所有线程
     */
    public void closeAllThread() {
        synchronized (this) {
            for (int i = 0; i < baseDiskCacheThreads.size(); i++) {
                BaseDiskCacheThread baseDiskCacheThread = baseDiskCacheThreads.get(i);
                baseDiskCacheThread.threadOperation(BaseThread.CLOSETHREAD);
            }
            baseDiskCacheThreads.clear();
        }
    }


}

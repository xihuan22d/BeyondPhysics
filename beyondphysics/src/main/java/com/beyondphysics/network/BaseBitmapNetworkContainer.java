package com.beyondphysics.network;


import com.beyondphysics.network.http.HttpAgreement;
import com.beyondphysics.network.utils.FileTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/9/5.
 * 图片网络控制器
 */

public class BaseBitmapNetworkContainer {

    /**
     * NotNull
     */
    private final List<BaseBitmapNetworkThread> baseBitmapNetworkThreads = new ArrayList<BaseBitmapNetworkThread>();
    /**
     * NotNull
     */
    private final RequestManager requestManager;

    private final int bitmapNetworkThreadCount;
    /**
     * NotNull
     */
    private final HttpAgreement httpAgreement;

    public BaseBitmapNetworkContainer(RequestManager requestManager, int bitmapNetworkThreadCount, HttpAgreement httpAgreement) {
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
        }

        if (bitmapNetworkThreadCount <= 0) {
            bitmapNetworkThreadCount = 2;
        }
        this.bitmapNetworkThreadCount = bitmapNetworkThreadCount;
        if (httpAgreement == null) {
            throw new NullPointerException("httpAgreement为null");
        } else {
            this.httpAgreement = httpAgreement;
        }
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * 初始化线程池
     */
    public void openAllThread() {
        synchronized (this) {
            if (baseBitmapNetworkThreads.size() > 0) {
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BaseBitmapNetworkContainer_openAllThread:重复初始化线程池", null, 1);
            } else {
                for (int i = 0; i < bitmapNetworkThreadCount; i++) {
                    BaseBitmapNetworkThread baseBitmapNetworkThread = new BaseBitmapNetworkThread(requestManager, httpAgreement);
                    baseBitmapNetworkThread.threadOperation(BaseThread.OPENTHREAD);
                    baseBitmapNetworkThreads.add(baseBitmapNetworkThread);
                }
            }
        }
    }

    /**
     * 阻塞方式关闭所有线程
     */
    public void closeAllThreadByWait() {
        synchronized (this) {
            for (int i = 0; i < baseBitmapNetworkThreads.size(); i++) {
                BaseBitmapNetworkThread baseBitmapNetworkThread = baseBitmapNetworkThreads.get(i);
                baseBitmapNetworkThread.threadOperation(BaseThread.CLOSETHREADBYWAIT);
            }
            baseBitmapNetworkThreads.clear();
        }
    }

    /**
     * 非阻塞方式关闭所有线程
     */
    public void closeAllThread() {
        synchronized (this) {
            for (int i = 0; i < baseBitmapNetworkThreads.size(); i++) {
                BaseBitmapNetworkThread baseBitmapNetworkThread = baseBitmapNetworkThreads.get(i);
                baseBitmapNetworkThread.threadOperation(BaseThread.CLOSETHREAD);
            }
            baseBitmapNetworkThreads.clear();
        }
    }

}

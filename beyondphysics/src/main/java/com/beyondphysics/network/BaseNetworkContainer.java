package com.beyondphysics.network;


import com.beyondphysics.network.http.HttpAgreement;
import com.beyondphysics.network.utils.FileTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/8/1.
 * 网络控制器
 */

public class BaseNetworkContainer {
    /**
     * NotNull
     */
    private final List<BaseNetworkThread> baseNetworkThreads = new ArrayList<BaseNetworkThread>();
    /**
     * NotNull
     */
    private final RequestManager requestManager;

    private final int networkThreadCount;
    /**
     * NotNull
     */
    private final HttpAgreement httpAgreement;

    public BaseNetworkContainer(RequestManager requestManager, int networkThreadCount, HttpAgreement httpAgreement) {
        if (requestManager == null) {
            throw new NullPointerException("requestManager为null");
        } else {
            this.requestManager = requestManager;
        }

        if (networkThreadCount <= 0) {
            networkThreadCount = 4;
        }
        this.networkThreadCount = networkThreadCount;
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
        synchronized (this) {//因为baseNetworkThreads.clear()操作会改变baseNetworkThreads数据,所以需要加锁
            if (baseNetworkThreads.size() > 0) {
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BaseNetworkContainer_openAllThread:重复初始化线程池", null, 1);
            } else {
                for (int i = 0; i < networkThreadCount; i++) {
                    BaseNetworkThread baseNetworkThread = new BaseNetworkThread(requestManager, httpAgreement);
                    baseNetworkThread.threadOperation(BaseThread.OPENTHREAD);
                    baseNetworkThreads.add(baseNetworkThread);
                }
            }
        }
    }

    /**
     * 阻塞方式关闭所有线程
     */
    public void closeAllThreadByWait() {
        synchronized (this) {
            for (int i = 0; i < baseNetworkThreads.size(); i++) {
                BaseNetworkThread baseNetworkThread = baseNetworkThreads.get(i);
                baseNetworkThread.threadOperation(BaseThread.CLOSETHREADBYWAIT);
            }
            baseNetworkThreads.clear();
        }
    }

    /**
     * 非阻塞方式关闭所有线程
     */
    public void closeAllThread() {
        synchronized (this) {
            for (int i = 0; i < baseNetworkThreads.size(); i++) {
                BaseNetworkThread baseNetworkThread = baseNetworkThreads.get(i);
                baseNetworkThread.threadOperation(BaseThread.CLOSETHREAD);
            }
            baseNetworkThreads.clear();
        }
    }

}

package com.beyondphysics.network;


import com.beyondphysics.network.utils.FileTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/10/30.
 * 请求分发器,使用RequestManagerUseDispatcher开启,使用分发器可以保证主线程的绝对流畅性
 */

public class BaseDispatcherContainer {
    /**
     * NotNull
     */
    private final List<BaseDispatcherThread> baseDispatcherThreads = new ArrayList<BaseDispatcherThread>();
    /**
     * NotNull
     */
    private final RequestManagerUseDispatcher requestManagerUseDispatcher;

    private final int dispatcherThreadCount;


    public BaseDispatcherContainer(RequestManagerUseDispatcher requestManagerUseDispatcher, int dispatcherThreadCount) {
        if (requestManagerUseDispatcher == null) {
            throw new NullPointerException("requestManagerUseDispatcher为null");
        } else {
            this.requestManagerUseDispatcher = requestManagerUseDispatcher;
        }

        if (dispatcherThreadCount <= 0) {
            dispatcherThreadCount = 2;
        }
        this.dispatcherThreadCount = dispatcherThreadCount;
    }

    public RequestManagerUseDispatcher getRequestManagerUseDispatcher() {
        return requestManagerUseDispatcher;
    }

    /**
     * 初始化线程池
     */
    public void openAllThread() {
        synchronized (this) {
            if (baseDispatcherThreads.size() > 0) {
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BaseNetworkContainer_openAllThread:重复初始化线程池", null, 1);
            } else {
                for (int i = 0; i < dispatcherThreadCount; i++) {
                    BaseDispatcherThread baseDispatcherThread = new BaseDispatcherThread(requestManagerUseDispatcher);
                    baseDispatcherThread.threadOperation(BaseThread.OPENTHREAD);
                    baseDispatcherThreads.add(baseDispatcherThread);
                }
            }
        }
    }

    /**
     * 阻塞方式关闭所有线程
     */
    public void closeAllThreadByWait() {
        synchronized (this) {
            for (int i = 0; i < baseDispatcherThreads.size(); i++) {
                BaseDispatcherThread baseDispatcherThread = baseDispatcherThreads.get(i);
                baseDispatcherThread.threadOperation(BaseThread.CLOSETHREADBYWAIT);
            }
            baseDispatcherThreads.clear();
        }
    }

    /**
     * 非阻塞方式关闭所有线程
     */
    public void closeAllThread() {
        synchronized (this) {
            for (int i = 0; i < baseDispatcherThreads.size(); i++) {
                BaseDispatcherThread baseDispatcherThread = baseDispatcherThreads.get(i);
                baseDispatcherThread.threadOperation(BaseThread.CLOSETHREAD);
            }
            baseDispatcherThreads.clear();
        }
    }

}

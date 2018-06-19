package com.beyondphysics.network;

import android.os.Process;

import com.beyondphysics.network.utils.FileTool;

/**
 * Created by xihuan22 on 2017/8/5.
 */

public class BaseThread extends Thread {
    public static final int OPENTHREAD = 1;
    public static final int CLOSETHREADBYWAIT = 2;
    public static final int CLOSETHREAD = 3;


    private final int priority;
    private final int threadCycleSleepTime;//线程的睡眠周期

    private boolean isOpenThread = false;
    private boolean canRunThread = true;
    private boolean isRunningThread = false;//线程的运行状态这个值只允许线程改变,不允许外界改变,外界只能读取

    public BaseThread() {
        priority = Process.THREAD_PRIORITY_BACKGROUND;
        threadCycleSleepTime = 10;
    }

    public BaseThread(int threadCycleSleepTime) {
        priority = Process.THREAD_PRIORITY_BACKGROUND;
        if (threadCycleSleepTime <= 0) {
            threadCycleSleepTime = 10;
        }
        this.threadCycleSleepTime = threadCycleSleepTime;
    }

    public BaseThread(int priority, int threadCycleSleepTime) {
        this.priority = priority;
        if (threadCycleSleepTime <= 0) {
            threadCycleSleepTime = 0;
        }
        this.threadCycleSleepTime = threadCycleSleepTime;
    }

    @Override
    public void run() {
        Process.setThreadPriority(priority);
        int i = 0;
        while (canRunThread) {
            if (threadCycleSleepTime == 0) {
                catchUnknownErrorWhenDoWork();
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i = i + 10;
                if (i >= threadCycleSleepTime) {
                    catchUnknownErrorWhenDoWork();
                    i = 0;
                }
            }
        }
        isRunningThread = false;
    }

    /**
     * 所有对于线程的操作都在该方法内进行,保证了对线程各个时期操作的安全性
     *
     * @param type 1时候表示开启线程,2时候表示一直会等待关闭线程,3时候表示不会等待线程关闭
     */
    public synchronized void threadOperation(int type) {
        if (type == OPENTHREAD) {
            openThread();
        } else if (type == CLOSETHREADBYWAIT) {
            closeThreadByWait();
        } else if (type == CLOSETHREAD) {
            closeThread();
        }
    }


    private void openThread() {
        if (!isOpenThread) {
            canRunThread = true;
            start();
            isOpenThread = true;
        }
    }


    private void closeThreadByWait() {
        if (isOpenThread) {
            canRunThread = false;
            while (isRunningThread) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isOpenThread = false;
        }
    }

    private void closeThread() {
        if (isOpenThread) {
            canRunThread = false;
            isOpenThread = false;
        }
    }

    private void catchUnknownErrorWhenDoWork() {
        try {
            doWork();
        } catch (Exception e) {
            e.printStackTrace();
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "线程未知异常__" + this.getName(), e, 1);
        }
    }

    /**
     * 一个检测周期到了进行逻辑处理
     */
    public void doWork() {

    }

}

package com.beyondphysics.ui.utils;

import android.app.Application;

import com.beyondphysics.network.utils.FileTool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by xihuan22 on 2017/8/17.
 */
public class CatchBugManager implements UncaughtExceptionHandler {
    private static CatchBugManager catchBugManager;
    private final Application application;
    private final UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    private CatchBugManager(Application application) {//只允许单列方式创建
        this.application = application;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 已加方法锁####
     * 强制使用application引用,方法锁保证只会被初始化一次
     */
    public static synchronized CatchBugManager getInstance(Application application) {
        if (catchBugManager == null) {
            catchBugManager = new CatchBugManager(application);
        }
        return catchBugManager;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable == null) {
            writeBugThenDoDefault("throwable为null", thread, throwable);
        } else {
            //throwable.printStackTrace();让默认的来打印,这里可不用打印
            String bugContent = null;
            StringWriter stringWriter = null;
            PrintWriter printWriter = null;
            try {
                stringWriter = new StringWriter();
                printWriter = new PrintWriter(stringWriter);
                throwable.printStackTrace(printWriter);
                Throwable throwableCause = throwable.getCause();
                while (throwableCause != null) {
                    throwableCause.printStackTrace(printWriter);
                    throwableCause = throwableCause.getCause();
                }
                bugContent = stringWriter.toString();
            } finally {
                try {
                    if (printWriter != null) {//先结束的先关闭
                        printWriter.close();
                    }
                    if (stringWriter != null) {
                        stringWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writeBugThenDoDefault(bugContent, thread, throwable);
        }
    }

    public Application getApplication() {
        return application;
    }

    public UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler;
    }

    private void writeBugThenDoDefault(String bugContent, Thread thread, Throwable throwable) {
        String bug = "我的手机型号:" + android.os.Build.MODEL + "在Android版本:" + android.os.Build.VERSION.RELEASE + "上发生了一个bug:\n" + bugContent;
        FileTool.needShowAndWriteLogToSdcard(true, FileTool.getDefaultRootPath() + File.separator + "CrashLog.txt", bug, null, 1);
        defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
    }
}

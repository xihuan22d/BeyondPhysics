package com.my.utils;

import android.app.Application;
import android.content.Context;

import com.beyondphysics.network.Request;
import com.beyondphysics.network.utils.FileTool;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.modelhttpfunctions.TheApplicationHttpFunction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class CatchBugManagerWithSend implements Thread.UncaughtExceptionHandler {
    private static CatchBugManagerWithSend catchBugManagerWithSend;
    private final Application application;
    private final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public CatchBugManagerWithSend(Application application) {//只允许单列方式创建
        this.application = application;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 已加方法锁####
     * 强制使用application引用,方法锁保证只会被初始化一次
     */
    public static synchronized CatchBugManagerWithSend getInstance(Application application) {
        if (catchBugManagerWithSend == null) {
            catchBugManagerWithSend = new CatchBugManagerWithSend(application);
        }
        return catchBugManagerWithSend;
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


    public Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler;
    }

    public void writeBugThenDoDefault(String bugContent, Thread thread, Throwable throwable) {
        String bug = "我的手机型号:" + android.os.Build.MODEL + "在Android版本:" + android.os.Build.VERSION.RELEASE + "上发生了一个bug:\n" + bugContent;
        FileTool.needWriteLogToSdcard(true, FileTool.getSdcardRootPath(TheApplication.DEFAULT_ROOT_PATH) + File.separator + "CrashLog.txt", bug, null, 1);
        TheApplicationHttpFunction.theApplication_feedback(application, "TheApplication", bug, new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {

            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
    }

    public static void writeWarning(Context context, Throwable throwable) {
        String bugContent = null;
        if (throwable != null) {
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
        } else {
            bugContent = "throwable为null";
        }
        String bug = "我的手机型号:" + android.os.Build.MODEL + "在Android版本:" + android.os.Build.VERSION.RELEASE + "上提交了一个warning:\n" + bugContent;
        FileTool.needWriteLogToSdcard(true, FileTool.getSdcardRootPath(TheApplication.DEFAULT_ROOT_PATH) + File.separator + "CrashLog.txt", bug, null, 2);
        TheApplicationHttpFunction.theApplication_feedback(context, "TheApplication", bug, new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {

            }
        });
    }

    public static void writeWarning(Context context, String content) {
        String bugContent = null;
        if (content != null) {
            bugContent = content;
        } else {
            bugContent = "content为null";
        }
        String bug = "我的手机型号:" + android.os.Build.MODEL + "在Android版本:" + android.os.Build.VERSION.RELEASE + "上提交了一个warning:\n" + bugContent;
        FileTool.needWriteLogToSdcard(true, FileTool.getSdcardRootPath(TheApplication.DEFAULT_ROOT_PATH) + File.separator + "CrashLog.txt", bug, null, 2);
        TheApplicationHttpFunction.theApplication_feedback(context, "TheApplication", bug, new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {

            }
        });
    }
}

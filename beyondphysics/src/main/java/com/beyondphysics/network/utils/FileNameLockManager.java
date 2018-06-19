package com.beyondphysics.network.utils;


import com.beyondphysics.network.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/8/17.
 */

public class FileNameLockManager {


    private static FileNameLockManager fileNameLockManager;

    public final List<String> listRunningFileNames = new ArrayList<String>();

    private FileNameLockManager() {

    }

    /**
     * 已加方法锁####
     * 方法锁保证只会被初始化一次
     */
    public static synchronized FileNameLockManager getInstance() {
        if (fileNameLockManager == null) {
            fileNameLockManager = new FileNameLockManager();
        }
        return fileNameLockManager;
    }

    /**
     * 安卓系统同一进程内无法锁文件,需要自行控制进程内文件锁,所有需要文件锁的地方先调用该方法以阻塞方式申请fileName对应文件名的锁,需要与removeFileNameLock成对出现
     * 使用时需要注意,同一个线程内不可以对相同的fileName执行多次getFileNameLock,否则会发生死锁,必须获取和移除一一对应,中间不能再夹着获取
     */
    public void getFileNameLock(String fileName) {
        if (fileName == null) {
            return;
        }
        fileName = fileName.toLowerCase();
        boolean lockSuccess = false;
        int count = 0;
        while (!lockSuccess) {
            synchronized (this) {
                boolean running = false;
                for (int i = 0; i < listRunningFileNames.size(); i++) {
                    String theFileName = listRunningFileNames.get(i);
                    if (fileName.equals(theFileName)) {
                        running = true;
                        break;
                    }
                }
                if (!running) {
                    listRunningFileNames.add(fileName);
                    lockSuccess = true;
                }
            }
            if (lockSuccess) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count = count + 10;
            if (count >= 5000) {
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "FileNameLockManager_getFileNameLock:获取锁超时", null, 1);
                count = 0;
            }
        }
    }

    /**
     * 需要与getFileNameLock成对出现,保证文件锁被移除
     */
    public void removeFileNameLock(String fileName) {
        if (fileName == null) {
            return;
        }
        fileName = fileName.toLowerCase();
        synchronized (this) {
            int runningIndex = -1;
            for (int i = 0; i < listRunningFileNames.size(); i++) {
                String theFileName = listRunningFileNames.get(i);
                if (fileName.equals(theFileName)) {
                    runningIndex = i;
                    break;
                }
            }
            if (runningIndex != -1) {
                listRunningFileNames.remove(runningIndex);
            }
        }
    }


    public void deleteFile(String fileName) {
        if (fileName == null) {
            return;
        }
        fileName = fileName.toLowerCase();
        try {
            getFileNameLock(fileName);
            FileTool.deleteFile(fileName);
        } finally {
            removeFileNameLock(fileName);
        }
    }


}

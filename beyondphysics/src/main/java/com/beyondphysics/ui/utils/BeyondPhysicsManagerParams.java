package com.beyondphysics.ui.utils;

import android.content.Context;

import com.beyondphysics.network.ResponseHandler;
import com.beyondphysics.network.cache.BitmapDiskCacheAnalyze;
import com.beyondphysics.network.cache.BitmapMemoryCache;
import com.beyondphysics.network.http.HttpAgreement;

/**
 * Created by xihuan22 on 2017/8/20.
 */

public class BeyondPhysicsManagerParams {
    private Context context;
    private int networkThreadCount = 4;
    private int bitmapNetworkThreadCount = 2;
    private HttpAgreement httpAgreement;
    private BitmapMemoryCache bitmapMemoryCache;
    private BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze;
    private int diskCacheThreadCount = 2;
    private String fileCache;
    private int maxDiskCacheSize = 52428800;
    private int responseHandlerType = ResponseHandler.RESPONSEHANDLERWITHMESSAGERECORD;
    private boolean useDispatcher = false;
    private int dispatcherThreadCount = 2;

    public BeyondPhysicsManagerParams() {

    }

    public BeyondPhysicsManagerParams(Context context, int networkThreadCount, int bitmapNetworkThreadCount, HttpAgreement httpAgreement, BitmapMemoryCache bitmapMemoryCache, BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze, int diskCacheThreadCount, String fileCache, int maxDiskCacheSize, int responseHandlerType, boolean useDispatcher, int dispatcherThreadCount) {
        this.context = context;
        this.networkThreadCount = networkThreadCount;
        this.bitmapNetworkThreadCount = bitmapNetworkThreadCount;
        this.httpAgreement = httpAgreement;
        this.bitmapMemoryCache = bitmapMemoryCache;
        this.bitmapDiskCacheAnalyze = bitmapDiskCacheAnalyze;
        this.diskCacheThreadCount = diskCacheThreadCount;
        this.fileCache = fileCache;
        this.maxDiskCacheSize = maxDiskCacheSize;
        this.responseHandlerType = responseHandlerType;
        this.useDispatcher = useDispatcher;
        this.dispatcherThreadCount = dispatcherThreadCount;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getNetworkThreadCount() {
        return networkThreadCount;
    }

    public void setNetworkThreadCount(int networkThreadCount) {
        this.networkThreadCount = networkThreadCount;
    }

    public int getBitmapNetworkThreadCount() {
        return bitmapNetworkThreadCount;
    }

    public void setBitmapNetworkThreadCount(int bitmapNetworkThreadCount) {
        this.bitmapNetworkThreadCount = bitmapNetworkThreadCount;
    }

    public HttpAgreement getHttpAgreement() {
        return httpAgreement;
    }

    public void setHttpAgreement(HttpAgreement httpAgreement) {
        this.httpAgreement = httpAgreement;
    }

    public BitmapMemoryCache getBitmapMemoryCache() {
        return bitmapMemoryCache;
    }

    public void setBitmapMemoryCache(BitmapMemoryCache bitmapMemoryCache) {
        this.bitmapMemoryCache = bitmapMemoryCache;
    }

    public BitmapDiskCacheAnalyze getBitmapDiskCacheAnalyze() {
        return bitmapDiskCacheAnalyze;
    }

    public void setBitmapDiskCacheAnalyze(BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze) {
        this.bitmapDiskCacheAnalyze = bitmapDiskCacheAnalyze;
    }

    public int getDiskCacheThreadCount() {
        return diskCacheThreadCount;
    }

    public void setDiskCacheThreadCount(int diskCacheThreadCount) {
        this.diskCacheThreadCount = diskCacheThreadCount;
    }

    public String getFileCache() {
        return fileCache;
    }

    public void setFileCache(String fileCache) {
        this.fileCache = fileCache;
    }

    public int getMaxDiskCacheSize() {
        return maxDiskCacheSize;
    }

    public void setMaxDiskCacheSize(int maxDiskCacheSize) {
        this.maxDiskCacheSize = maxDiskCacheSize;
    }

    public int getResponseHandlerType() {
        return responseHandlerType;
    }

    public void setResponseHandlerType(int responseHandlerType) {
        this.responseHandlerType = responseHandlerType;
    }

    public boolean isUseDispatcher() {
        return useDispatcher;
    }

    public void setUseDispatcher(boolean useDispatcher) {
        this.useDispatcher = useDispatcher;
    }

    public int getDispatcherThreadCount() {
        return dispatcherThreadCount;
    }

    public void setDispatcherThreadCount(int dispatcherThreadCount) {
        this.dispatcherThreadCount = dispatcherThreadCount;
    }

    @Override
    public String toString() {
        return "BeyondPhysicsManagerParams{" +
                "context=" + context +
                ", networkThreadCount=" + networkThreadCount +
                ", bitmapNetworkThreadCount=" + bitmapNetworkThreadCount +
                ", httpAgreement=" + httpAgreement +
                ", bitmapMemoryCache=" + bitmapMemoryCache +
                ", bitmapDiskCacheAnalyze=" + bitmapDiskCacheAnalyze +
                ", diskCacheThreadCount=" + diskCacheThreadCount +
                ", fileCache='" + fileCache + '\'' +
                ", maxDiskCacheSize=" + maxDiskCacheSize +
                ", responseHandlerType=" + responseHandlerType +
                ", useDispatcher=" + useDispatcher +
                ", dispatcherThreadCount=" + dispatcherThreadCount +
                '}';
    }
}

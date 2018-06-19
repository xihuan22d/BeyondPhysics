package com.beyondphysics.network;

import android.os.Looper;

import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;
import com.beyondphysics.network.utils.TimeTool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xihuan22 on 2017/9/20.
 */
public class CacheItemRequest implements Serializable {
    /**
     * 在构造方法内需要传入keys和cacheItemTAG,对应ThreadSafelyLinkedHasMapCacheItem内的getCacheItemsByKeys方法
     */
    public static final int GETCACHEITEMS = 1;
    /**
     * 在构造方法内需要传入keys和cacheItemTAG,对应ThreadSafelyLinkedHasMapCacheItem内的clearCacheItems方法
     */
    public static final int CLEARCACHEITEMS = 2;
    /**
     * 在构造方法内可以设置keys和cacheItemTAG为null,这俩个参数不会被用到,对应ThreadSafelyLinkedHasMapCacheItem内的clearAllCacheItem方法
     */
    public static final int CLEARALLCACHEITEM = 3;

    private boolean cancelRequest = false;

    private boolean receiveCancel = false;

    private final int type;
    /**
     * NotNull
     */
    private final SuperKey superKey;
    private final List<String> keys;
    private final RequestManager.CacheItemTAG cacheItemTAG;
    private OnCacheItemListener onCacheItemListener;
    private List<CacheItem> cacheItems;


    public CacheItemRequest(int type, String tag, List<String> keys, RequestManager.CacheItemTAG cacheItemTAG, OnCacheItemListener onCacheItemListener) {
        this.type = type;
        superKey = new SuperKey(SuperKey.DEFAULT_KEY, tag, 10, 1, TimeTool.getOnlyTimeWithoutSleep());
        this.keys = keys;
        this.cacheItemTAG = cacheItemTAG;
        this.onCacheItemListener = onCacheItemListener;
    }

    public boolean isCancelRequest() {
        return cancelRequest;
    }

    public void cancelRequest() {
        cancelRequest = true;
    }

    public boolean isReceiveCancel() {
        return receiveCancel;
    }

    public void setReceiveCancel(boolean receiveCancel) {
        this.receiveCancel = receiveCancel;
    }

    public int getType() {
        return type;
    }

    public SuperKey getSuperKey() {
        return superKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public RequestManager.CacheItemTAG getCacheItemTAG() {
        return cacheItemTAG;
    }

    public OnCacheItemListener getOnCacheItemListener() {
        return onCacheItemListener;
    }

    public void removeCacheItemListener() {
        ThreadTool.throwIfNotOnMainThread();
        onCacheItemListener = null;
    }

    public List<CacheItem> getCacheItems() {
        return cacheItems;
    }

    public void setCacheItems(List<CacheItem> cacheItems) {
        this.cacheItems = cacheItems;
    }

    @Override
    public String toString() {
        return "CacheItemRequest{" +
                "cancelRequest=" + cancelRequest +
                ", receiveCancel=" + receiveCancel +
                ", type=" + type +
                ", superKey=" + superKey +
                ", keys=" + keys +
                ", cacheItemTAG=" + cacheItemTAG +
                ", onCacheItemListener=" + onCacheItemListener +
                ", cacheItems=" + cacheItems +
                '}';
    }

    public static void removeCacheItemListener(CacheItemRequest cacheItemRequest, boolean removeListener) {
        if (cacheItemRequest == null || removeListener == false) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cacheItemRequest.removeCacheItemListener();
        }
    }

    public interface OnCacheItemListener {
        /**
         * GETCACHEITEMS模式下获取完毕会返回cacheItems,返回的数据个数和keys数量对应,若找不到将会在对应位置设置为null
         * CLEARCACHEITEMS和CLEARALLCACHEITEM模式下清理完毕会返回空的cacheItems
         */
        void onSuccessResponse(List<CacheItem> cacheItems);

        void onErrorResponse(String error);
    }

}

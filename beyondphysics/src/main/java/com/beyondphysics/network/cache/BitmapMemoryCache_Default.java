package com.beyondphysics.network.cache;

import android.support.v4.util.LruCache;

import com.beyondphysics.network.utils.SuperBitmap;


/**
 * Created by xihuan22 on 2017/8/27.
 */

public class BitmapMemoryCache_Default implements BitmapMemoryCache {
    /**
     * NotNull
     */
    private final LruCache<String, SuperBitmap> lruCache;


    public BitmapMemoryCache_Default() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int maxSize = maxMemory / 6;
        lruCache = new LruCache<String, SuperBitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, SuperBitmap superBitmap) {
                return superBitmap.getSize();
            }
        };
    }

    @Override
    public void putSuperBitmap(String key, SuperBitmap superBitmap) {
        if (key == null || superBitmap == null) {
            return;
        }
        lruCache.put(key, superBitmap);
    }

    @Override
    public SuperBitmap getSuperBitmap(String key) {
        if (key == null) {
            return null;
        }
        return lruCache.get(key);
    }

    @Override
    public void clearMemory(String key) {
        if (key == null) {
            return;
        }
        lruCache.remove(key);
    }

    @Override
    public void clearAllMemory() {
        lruCache.evictAll();
    }
}

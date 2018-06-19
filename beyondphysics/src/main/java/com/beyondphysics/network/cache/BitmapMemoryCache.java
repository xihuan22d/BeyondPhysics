package com.beyondphysics.network.cache;


import com.beyondphysics.network.utils.SuperBitmap;


/**
 * Created by xihuan22 on 2017/8/27.
 */

public interface BitmapMemoryCache {

    void putSuperBitmap(String key, SuperBitmap superBitmap);

    SuperBitmap getSuperBitmap(String key);

    void clearMemory(String key);

    void clearAllMemory();

}

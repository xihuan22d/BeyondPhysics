package com.beyondphysics.network.cache;


import com.beyondphysics.network.BitmapRequest;
import com.beyondphysics.network.http.DoRequestParams;
import com.beyondphysics.network.http.HttpResponse;

/**
 * Created by xihuan22 on 2017/9/7.
 */

public interface BitmapDiskCacheAnalyze {

    HttpResponse doDiskCacheAnalyze(BitmapRequest<?> bitmapRequest, DoRequestParams doRequestParams);

}

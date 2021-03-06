package com.my.views.network;

import android.content.Context;
import android.util.AttributeSet;

import com.beyondphysics.network.BitmapRequest;
import com.beyondphysics.network.BitmapRequest_Default_Params;
import com.beyondphysics.ui.views.RoundedNetworkGifImageView;
import com.my.utils.MyBitmapRequest;


public class MyRoundedNetworkGifImageView extends RoundedNetworkGifImageView {
    public MyRoundedNetworkGifImageView(Context context) {
        super(context);
    }

    public MyRoundedNetworkGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRoundedNetworkGifImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public BitmapRequest<?> getBitmapRequest(BitmapRequest_Default_Params bitmapRequest_Default_Params) {
        return new MyBitmapRequest(bitmapRequest_Default_Params);
    }
}

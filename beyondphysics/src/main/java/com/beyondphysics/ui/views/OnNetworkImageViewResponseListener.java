package com.beyondphysics.ui.views;

import com.beyondphysics.network.BitmapResponse;

public interface OnNetworkImageViewResponseListener {

    void onSuccessResponse(BitmapResponse response);

    void onErrorResponse(String error);
}
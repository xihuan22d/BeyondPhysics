package com.my.modelhttpfunctions;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.MainActivity_Type_GetWallpaperType_GsonModel;
import com.my.utils.HttpConnectTool;
import com.my.utils.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_Type_HttpFunction {

    public static void mainActivity_type_getWallpaperType(final BaseActivity baseActivity, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<MainActivity_Type_GetWallpaperType_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.mainActivity_type_getWallpaperType;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    mainActivity_type_getWallpaperTypeResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
                }

                @Override
                public void onErrorResponse(String error) {
                    if (TheApplication.SHOWTIMEOUTTIPS) {
                        BaseActivity.showShortToast(baseActivity, TheApplication.NETTIMEOUTERROR);
                    }
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(error);
                    }
                }
            }) {
                @Override
                public Map<String, String> getBodyParams() {
                    Map<String, String> bodyParams = new HashMap<String, String>();
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void mainActivity_type_getWallpaperTypeResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<MainActivity_Type_GetWallpaperType_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            MainActivity_Type_GetWallpaperType_GsonModel.Data data = MainActivity_Type_GetWallpaperType_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }

}
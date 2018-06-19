package com.my.modelhttpfunctions;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.models.Wallpaper;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.MainActivity_Home_GetWallpaper_Default_GsonModel;
import com.my.utils.EncryptionTool;
import com.my.utils.HttpConnectTool;
import com.my.utils.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

public class WaterfallFlowHttpFunction {

    public static void mainActivity_home_getWallpaperByCreateTime(final BaseActivity baseActivity, final String kind, final String createTime, final String screenNumber, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<MainActivity_Home_GetWallpaper_Default_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.nodejsRootUrl + HttpConnectTool.mainActivity_home_getWallpaperByCreateTime;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    mainActivity_home_getWallpaperByCreateTimeResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "kind", kind);
                    EncryptionTool.addParamWithEncryption(bodyParams, "createTime", createTime);
                    EncryptionTool.addParamWithEncryption(bodyParams, "screenNumber", screenNumber);
                    EncryptionTool.addParamWithEncryption(bodyParams, "upOrDown", Wallpaper.UPORDOWN_DOWN);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void mainActivity_home_getWallpaperByCreateTimeResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<MainActivity_Home_GetWallpaper_Default_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            MainActivity_Home_GetWallpaper_Default_GsonModel.Data data = MainActivity_Home_GetWallpaper_Default_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }

    public static void mainActivity_home_getWallpaperBySort(final BaseActivity baseActivity, final String kind, final String sortType, final String sort, final String screenNumber, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<MainActivity_Home_GetWallpaper_Default_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.nodejsRootUrl + HttpConnectTool.mainActivity_home_getWallpaperBySort;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    mainActivity_home_getWallpaperBySortResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "kind", kind);
                    EncryptionTool.addParamWithEncryption(bodyParams, "sortType", sortType);
                    EncryptionTool.addParamWithEncryption(bodyParams, "sort", sort);
                    EncryptionTool.addParamWithEncryption(bodyParams, "screenNumber", screenNumber);
                    EncryptionTool.addParamWithEncryption(bodyParams, "upOrDown", Wallpaper.UPORDOWN_DOWN);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void mainActivity_home_getWallpaperBySortResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<MainActivity_Home_GetWallpaper_Default_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            MainActivity_Home_GetWallpaper_Default_GsonModel.Data data = MainActivity_Home_GetWallpaper_Default_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }
}

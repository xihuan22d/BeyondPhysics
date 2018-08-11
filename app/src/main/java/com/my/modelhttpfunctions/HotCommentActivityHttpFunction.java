package com.my.modelhttpfunctions;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.models.Comment;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.HotCommentActivity_GetWallpaperCommentBySort_GsonModel;
import com.my.utils.EncryptionTool;
import com.my.utils.HttpConnectTool;
import com.my.utils.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

public class HotCommentActivityHttpFunction {


    public static void hotCommentActivity_getWallpaperCommentBySort(final BaseActivity baseActivity, final String targetObject_id, final String sort, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.hotCommentActivity_getWallpaperCommentBySort;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    hotCommentActivity_getWallpaperCommentBySortResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "targetObject_id", targetObject_id);
                    EncryptionTool.addParamWithEncryption(bodyParams, "sortType", Comment.SORTTYPE_CHILDCOMMENTSORT);
                    EncryptionTool.addParamWithEncryption(bodyParams, "sort", sort);
                    EncryptionTool.addParamWithEncryption(bodyParams, "upOrDown", Comment.UPORDOWN_DOWN);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void hotCommentActivity_getWallpaperCommentBySortResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data data = HotCommentActivity_GetWallpaperCommentBySort_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


}

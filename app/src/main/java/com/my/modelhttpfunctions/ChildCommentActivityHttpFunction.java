package com.my.modelhttpfunctions;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.models.Comment;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel;
import com.my.utils.EncryptionTool;
import com.my.utils.HttpConnectTool;
import com.my.utils.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChildCommentActivityHttpFunction {


    public static void childCommentActivity_getWallpaperChildCommentByCreateTime(final BaseActivity baseActivity, final String rootParent_id, final String createTime, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.childCommentActivity_getWallpaperChildCommentByCreateTime;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    childCommentActivity_getWallpaperChildCommentByCreateTimeResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "rootParent_id", rootParent_id);
                    EncryptionTool.addParamWithEncryption(bodyParams, "createTime", createTime);
                    EncryptionTool.addParamWithEncryption(bodyParams, "upOrDown", Comment.UPORDOWN_UP);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void childCommentActivity_getWallpaperChildCommentByCreateTimeResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data data = ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


}

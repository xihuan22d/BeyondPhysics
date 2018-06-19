package com.my.modelhttpfunctions;

import android.content.Context;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;
import com.my.beyondphysicsapplication.NewBaseActivity;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.utils.EncryptionTool;
import com.my.utils.HttpConnectTool;
import com.my.utils.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

public class TheApplicationHttpFunction {

    public static void theApplication_feedback(final Context context, final String activityKey, final String content, final Request.OnResponseListener<String> onResponseListener) {
        String url = HttpConnectTool.nodejsRootUrl + HttpConnectTool.theApplication_feedback;
        if (!NetworkStateTool.isNetworkConnected(context)) {
            BaseActivity.showShortToast(context, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    if (onResponseListener != null) {
                        onResponseListener.onSuccessResponse(response);
                    }
                }

                @Override
                public void onErrorResponse(String error) {
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(error);
                    }
                }
            }) {
                @Override
                public Map<String, String> getBodyParams() {
                    Map<String, String> bodyParams = new HashMap<String, String>();
                    EncryptionTool.addParamWithEncryption(bodyParams, "content", content);
                    EncryptionTool.addParamWithEncryption(bodyParams, "type", "BeyondPhysicsApplication");
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstanceByBeyondPhysicsManagerParams(NewBaseActivity.getBeyondPhysicsManagerParams(context)).addRequestWithSort(myStringRequest);
        }
    }

}

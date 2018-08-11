package com.my.modelhttpfunctions;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.models.Collection;
import com.my.models.Wallpaper;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.WallpaperDetailsActivity_CommentWallpaper_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoCollection_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoFollow_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoPraise_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_GetWallpaperDetails_GsonModel;
import com.my.utils.EncryptionTool;
import com.my.utils.HttpConnectTool;
import com.my.utils.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

public class WallpaperDetailsActivityHttpFunction {

    public static void wallpaperDetailsActivity_getWallpaperDetails(final BaseActivity baseActivity, final String wallpaper_id, final String kind, final String createTime, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.wallpaperDetailsActivity_getWallpaperDetails;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    wallpaperDetailsActivity_getWallpaperDetailsResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "wallpaper_id", wallpaper_id);
                    EncryptionTool.addParamWithEncryption(bodyParams, "kind", kind);
                    EncryptionTool.addParamWithEncryption(bodyParams, "createTime", createTime);
                    EncryptionTool.addParamWithEncryption(bodyParams, "upOrDown", Wallpaper.UPORDOWN_DOWN);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void wallpaperDetailsActivity_getWallpaperDetailsResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data data = WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


    public static void wallpaperDetailsActivity_getWallpaperCommentByCreateTime(final BaseActivity baseActivity, final String targetObject_id, final String createTime, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.wallpaperDetailsActivity_getWallpaperCommentByCreateTime;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    wallpaperDetailsActivity_getWallpaperCommentByCreateTimeResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "createTime", createTime);
                    EncryptionTool.addParamWithEncryption(bodyParams, "upOrDown", Wallpaper.UPORDOWN_DOWN);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void wallpaperDetailsActivity_getWallpaperCommentByCreateTimeResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data data = WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


    public static void wallpaperDetailsActivity_commentWallpaper(final BaseActivity baseActivity, final String parentOrChild, final String content, final String parent_id, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.wallpaperDetailsActivity_commentWallpaper;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    wallpaperDetailsActivity_commentWallpaperResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "parentOrChild", parentOrChild);
                    EncryptionTool.addParamWithEncryption(bodyParams, "content", content);
                    EncryptionTool.addParamWithEncryption(bodyParams, "parent_id", parent_id);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void wallpaperDetailsActivity_commentWallpaperResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data data = WallpaperDetailsActivity_CommentWallpaper_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


    public static void wallpaperDetailsActivity_doPraise(final BaseActivity baseActivity, final String type, final String targetObject_id, final String praiseUpOrDown, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoPraise_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.wallpaperDetailsActivity_doPraise;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    wallpaperDetailsActivity_doPraiseResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "type", type);
                    EncryptionTool.addParamWithEncryption(bodyParams, "targetObject_id", targetObject_id);
                    EncryptionTool.addParamWithEncryption(bodyParams, "praiseUpOrDown", praiseUpOrDown);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void wallpaperDetailsActivity_doPraiseResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoPraise_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            WallpaperDetailsActivity_DoPraise_GsonModel.Data data = WallpaperDetailsActivity_DoPraise_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


    public static void wallpaperDetailsActivity_doCollection(final BaseActivity baseActivity, final String targetObject_id, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoCollection_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.wallpaperDetailsActivity_doCollection;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    wallpaperDetailsActivity_doCollectionResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "type", Collection.type_wallpaper);
                    EncryptionTool.addParamWithEncryption(bodyParams, "targetObject_id", targetObject_id);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void wallpaperDetailsActivity_doCollectionResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoCollection_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            WallpaperDetailsActivity_DoCollection_GsonModel.Data data = WallpaperDetailsActivity_DoCollection_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }


    public static void wallpaperDetailsActivity_doFollow(final BaseActivity baseActivity, final String targetUser_id, final Request.OnResponseListener<String> onResponseListener, final BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoFollow_GsonModel.Data> onBaseGsonModelListener) {
        String url = HttpConnectTool.getServerRootUrl() + HttpConnectTool.wallpaperDetailsActivity_doFollow;
        if (!NetworkStateTool.isNetworkConnected(baseActivity)) {
            BaseActivity.showShortToast(baseActivity, TheApplication.NETOPENERROR);
            if (onResponseListener != null) {
                onResponseListener.onErrorResponse(null);
            }
        } else {
            MyStringRequest myStringRequest = new MyStringRequest(url, Request.POST, baseActivity.activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    wallpaperDetailsActivity_doFollowResponse(response, baseActivity, onResponseListener, onBaseGsonModelListener);
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
                    EncryptionTool.addParamWithEncryption(bodyParams, "targetUser_id", targetUser_id);
                    return bodyParams;
                }
            };
            BeyondPhysicsManager.getInstance(baseActivity).addRequestWithSort(myStringRequest);
        }
    }

    private static void wallpaperDetailsActivity_doFollowResponse(String response, BaseActivity baseActivity, Request.OnResponseListener<String> onResponseListener, BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoFollow_GsonModel.Data> onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = BaseGsonModel.isBaseGsonModelHaveErrorOrTips(response, onBaseGsonModelListener);
        if (!isHaveErrorOrTips) {
            WallpaperDetailsActivity_DoFollow_GsonModel.Data data = WallpaperDetailsActivity_DoFollow_GsonModel.getGsonModel(response);
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.success(data);
            }
        }
        if (onResponseListener != null) {
            onResponseListener.onSuccessResponse(response);
        }
    }

}

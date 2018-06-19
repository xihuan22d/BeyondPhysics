package com.my.models.net;

import com.beyondphysics.ui.BaseActivity;
import com.google.gson.Gson;


public class BaseGsonModel {
    public static final int SUCCESSCODE = 0;
    public static final int ERRORNETCODE = 1;
    public static final int ERROR404CODE = 2;
    public static final int TIPSCODE = 3;
    public static final int ERRORTIPSCODE = 4;
    public static final int ERRORNODECODE = 5;
    public static final int ERRORDBCODE = 6;
    public static final int ERRORFSCODE = 7;
    public static final int ERRORTOKENCODE = 8;

    private boolean result;
    private int code;
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseGsonModel{" +
                "result=" + result +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }


    public static boolean isBaseGsonModelHaveErrorOrTips(String response, OnBaseGsonModelListener onBaseGsonModelListener) {
        boolean isHaveErrorOrTips = false;
        BaseGsonModel baseGsonModel = getBaseGsonModel(response);
        String errorMessage = getErrorMessage(baseGsonModel);
        if (errorMessage != null) {
            isHaveErrorOrTips = true;
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.error(errorMessage);
            }
        }
        if (!isHaveErrorOrTips && baseGsonModel != null && baseGsonModel.isResult() && baseGsonModel.getCode() == TIPSCODE) {
            isHaveErrorOrTips = true;
            if (onBaseGsonModelListener != null) {
                onBaseGsonModelListener.successByTips(baseGsonModel.getMessage());
            }
        }
        return isHaveErrorOrTips;
    }

    public static BaseGsonModel getBaseGsonModel(String response) {
        BaseActivity.showSystemErrorLog("response:" + response);
        BaseGsonModel baseGsonModel = null;
        if (response != null) {
            try {
                baseGsonModel = new Gson().fromJson(response, BaseGsonModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baseGsonModel;
    }

    public static String getErrorMessage(BaseGsonModel baseGsonModel) {
        if (baseGsonModel == null) {
            return "解析的数据为空";
        }
        if (!baseGsonModel.isResult()) {
            if (baseGsonModel.getCode() == ERRORTOKENCODE) {
                return "非法访问,请重新登录!";
            }
            if (baseGsonModel.getMessage() == null) {
                return "错误信息为空";
            } else {
                return baseGsonModel.getMessage();
            }
        }
        return null;
    }


    public interface OnBaseGsonModelListener<T> {

        void error(String error);

        /**
         * result为true,code为TIPSCODE时候触发,属正常提示,该方法和success方法不共存,一旦该方法执行将不再执行success方法
         */
        void successByTips(String tips);

        void success(T data);
    }

}

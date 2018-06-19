package com.my.beyondphysicsapplication.uihelp;

import android.app.ProgressDialog;
import android.view.View;

import com.beyondphysics.network.utils.TimeTool;
import com.beyondphysics.ui.BaseActivity;
import com.my.beyondphysicsapplication.TheApplication;

public class ProgressDialogHelp {
    /**
     * Object[]里面0和1分别代表progressDialog和progressDialogKey
     * 禁止用户点击,俩者可同时生效,baseActivity为null表示不启用progressDialog,view为null表示不禁用view触摸
     */
    public static Object[] unEnabledView(BaseActivity baseActivity, View view) {
        if (view != null) {
            view.setEnabled(false);
        }
        ProgressDialog progressDialog = null;
        if (baseActivity != null) {
            if (TheApplication.CONNECTNETTIPS != null && TheApplication.CONNECTNETTIPS.length > 0) {
                int random = (int) (TheApplication.CONNECTNETTIPS.length * Math.random());
                String tips = TheApplication.CONNECTNETTIPS[random];
                progressDialog = ProgressDialog.show(baseActivity, null, tips, false, false);
            } else {
                progressDialog = ProgressDialog.show(baseActivity, null, "正在尝试买通服务器......", false, false);
            }
        }
        String progressDialogKey = "progressDialogKey" + TimeTool.getOnlyTimeWithoutSleep();
        baseActivity.addProgressDialog(progressDialogKey, progressDialog);
        Object[] objects = new Object[2];
        objects[0] = progressDialog;
        objects[1] = progressDialogKey;
        return objects;
    }

    /**
     * 显示指定的progressDialog
     */
    public static Object[] unEnabledView(BaseActivity baseActivity, View view, ProgressDialog progressDialog) {
        if (view != null) {
            view.setEnabled(false);
        }
        progressDialog.show();
        String progressDialogKey = "progressDialogKey" + TimeTool.getOnlyTimeWithoutSleep();
        baseActivity.addProgressDialog(progressDialogKey, progressDialog);
        Object[] objects = new Object[2];
        objects[0] = progressDialog;
        objects[1] = progressDialogKey;
        return objects;
    }

    /**
     * 销毁progressDialog,重新启用view触摸
     **/
    public static void enabledView(BaseActivity baseActivity, ProgressDialog progressDialog, String progressDialogKey, View view) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        baseActivity.removeProgressDialog(progressDialogKey);
        if (view != null) {
            view.setEnabled(true);
        }
    }

}

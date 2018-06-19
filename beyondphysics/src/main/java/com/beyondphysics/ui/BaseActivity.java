package com.beyondphysics.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.beyondphysics.R;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.ResponseHandler;
import com.beyondphysics.network.http.HttpAgreement_Default;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.TimeTool;
import com.beyondphysics.ui.utils.ActivityManager;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.BeyondPhysicsManagerParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class BaseActivity extends AppCompatActivity {

    /**
     * 以非阻塞方式取消请求,在主线程中取消的时候会把所有回调置为null以避免短时间的Activity内存泄露
     */
    public static final int CANCELREQUESTWITHTAG = 1;
    /**
     * 一般在测试框架和程序稳定性时才使用
     * 以阻塞方式关闭当前Activity的所有请求,因为框架内在读取时候加入了cancelRequest检测,取消速度一般较快,可以保证在onDestroy方法执行后就释放所有请求相关内存,但可能因为readTimeoutMs设置过大在网络极差时候导致阻塞关闭时间过长
     */
    public static final int CANCELREQUESTWITHTAGBYWAIT = 2;


    public String activityKey = "error";
    public Handler handler = null;//让有需要的activity继承下去初始化
    private boolean cancelRequest = true;//默认activity销毁时候取消当前activity所有的网络请求
    private int cancelType = CANCELREQUESTWITHTAG;
    private boolean allowKeyBack = true;
    private HashMap<String, ProgressDialog> hashMapProgressDialogs = new HashMap<String, ProgressDialog>();//防止转屏时候没有取消dialog出现的问题
    private HashMap<String, PopupWindow> hashMapPopupWindows = new HashMap<String, PopupWindow>();

    //hashMap方法写在开头整洁直观
    public void addProgressDialog(String progressDialogKey, ProgressDialog progressDialog) {
        if (progressDialogKey != null && progressDialog != null) {
            hashMapProgressDialogs.put(progressDialogKey, progressDialog);
        }
    }

    public ProgressDialog getProgressDialog(String progressDialogKey) {
        if (progressDialogKey == null) {
            return null;
        }
        if (hashMapProgressDialogs.containsKey(progressDialogKey)) {
            return hashMapProgressDialogs.get(progressDialogKey);
        } else {
            return null;
        }
    }

    public void removeProgressDialog(String progressDialogKey) {
        if (progressDialogKey != null) {
            if (hashMapProgressDialogs.containsKey(progressDialogKey)) {
                hashMapProgressDialogs.remove(progressDialogKey);
            }
        }
    }

    public void dismissAndRemoveAllProgressDialog() {
        Iterator<Map.Entry<String, ProgressDialog>> iterator = hashMapProgressDialogs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ProgressDialog> entry = iterator.next();
            ProgressDialog progressDialog = entry.getValue();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
        hashMapProgressDialogs.clear();
    }


    public void addPopupWindow(String popupWindowKey, PopupWindow popupWindow) {
        if (popupWindowKey != null && popupWindow != null) {
            hashMapPopupWindows.put(popupWindowKey, popupWindow);
        }
    }

    public PopupWindow getPopupWindow(String popupWindowKey) {
        if (popupWindowKey == null) {
            return null;
        }
        if (hashMapPopupWindows.containsKey(popupWindowKey)) {
            return hashMapPopupWindows.get(popupWindowKey);
        } else {
            return null;
        }
    }

    public void removePopupWindow(String popupWindowKey) {
        if (popupWindowKey != null) {
            if (hashMapPopupWindows.containsKey(popupWindowKey)) {
                hashMapPopupWindows.remove(popupWindowKey);
            }
        }
    }

    public void dismissAndRemoveAllPopupWindow() {
        Iterator<Map.Entry<String, PopupWindow>> iterator = hashMapPopupWindows.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PopupWindow> entry = iterator.next();
            PopupWindow popupWindow = entry.getValue();
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }
        hashMapPopupWindows.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityKey = getClass().getName() + ActivityManager.ACTIVITYKEYSEPARATOR + TimeTool.getOnlyTimeWithoutSleep();
        ActivityManager.getInstance().addBaseActivity(activityKey, this);
    }

    /***
     * 包含了onCreate里面需要的所有的初始化操作
     */
    protected void initAll() {
        initHandler();
        initUi();
        initConfigUi();
        initHttp();
        initOther();
    }


    /***
     * 对父类中handler的初始化
     */
    protected void initHandler() {

    }

    /***
     * 规范UI元素的初始化
     */
    protected void initUi() {

    }

    /***
     * 配置UI参数如点击事件等的初始化
     */
    protected void initConfigUi() {

    }


    /***
     * 一些http请求的初始化
     */
    protected void initHttp() {

    }

    /***
     * 初始化其余的内容如开启线程控制activity的跳转等
     */
    protected void initOther() {
    }


    /**
     * 设置顶部状态栏和底部导航栏按键风格
     */
    public void setWindowType(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            if (type == 1) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (type == 2) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获得activity销毁时候取消当前activity所有的网络请求的状态
     */
    public boolean isCancelRequest() {
        return cancelRequest;
    }

    /**
     * 设置activity销毁时候取消当前activity所有的网络请求的状态
     */
    public void setCancelRequest(boolean cancelRequest) {
        this.cancelRequest = cancelRequest;
    }

    public int getCancelType() {
        return cancelType;
    }

    public void setCancelType(int cancelType) {
        this.cancelType = cancelType;
    }

    /**
     * 是否允许使用物理按键执行doBack()方法
     */
    public boolean isAllowKeyBack() {
        return allowKeyBack;
    }

    /**
     * 设置是否允许使用物理按键执行doBack()方法
     */
    public void setAllowKeyBack(boolean allowKeyBack) {
        this.allowKeyBack = allowKeyBack;
    }


    /**
     * 退出当前activity,可让返回按钮和物理按键同时执行该方法
     */
    protected void doBack() {


    }

    /**
     * BeyondPhysics初始化参数
     */
    public BeyondPhysicsManagerParams getBeyondPhysicsManagerParams() {
        return getDefaultBeyondPhysicsManagerParams(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (allowKeyBack) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                doBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (cancelRequest) {
            //会取消所有的包含当前activityKey的普通请求、图片请求、下载请求、断点下载请求、上传请求
            if (cancelType == CANCELREQUESTWITHTAG) {
                BeyondPhysicsManager.getInstance(this).cancelRequestWithTag(activityKey, true);
            } else if (cancelType == CANCELREQUESTWITHTAGBYWAIT) {
                BeyondPhysicsManager.getInstance(this).cancelRequestWithTagByWait(activityKey, true);//销毁前先取消网络请求,最后在ActivityManager移除baseActivity可以让代码更清晰
            }
        }
        dismissAndRemoveAllProgressDialog();
        dismissAndRemoveAllPopupWindow();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        ActivityManager.getInstance().removeBaseActivity(activityKey);
        super.onDestroy();
    }

    public static BeyondPhysicsManagerParams getDefaultBeyondPhysicsManagerParams(Context context) {
        return new BeyondPhysicsManagerParams(context, 4, 2, new HttpAgreement_Default(), null, null, 2, null, 52428800, ResponseHandler.RESPONSEHANDLERWITHMESSAGERECORD, true, 2);
    }

    /**
     * 因为api21之后的cardView才是正常的,之前的会有边界多出和因圆角导致的留白问题,边界多出可以通过该方法解决
     */
    public static int getRecyclerViewSpace_Default(Activity activity) {
        int space = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            space = activity.getResources().getDimensionPixelSize(R.dimen.beyondPhysics_recyclerView_space_default);
        } else {
            space = activity.getResources().getDimensionPixelSize(R.dimen.beyondPhysics_recyclerView_space_beforeLollipop_default);
        }
        return space;
    }

    /**
     * 当前屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 当前屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static void showSystemErrorLog(String error) {
        if (error != null) {
            Log.e("SystemError", error);
        }
    }

    public static void showShortToast(Context context, String tips) {
        if (tips != null) {
            Toast.makeText(context.getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLongToast(Context context, String tips) {
        if (tips != null) {
            Toast.makeText(context.getApplicationContext(), tips, Toast.LENGTH_LONG).show();
        }
    }

    public static Activity getActivityByContext(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivityByContext(((ContextWrapper) context).getBaseContext());
        } else {
            return null;
        }
    }

    /**
     * return NotNull
     */
    public static BeyondPhysicsManager getBeyondPhysicsManager(Context context) {
        BeyondPhysicsManager beyondPhysicsManager = null;
        Activity activity = BaseActivity.getActivityByContext(context);
        if (activity != null && activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            beyondPhysicsManager = BeyondPhysicsManager.getInstance(baseActivity);
        } else {
            beyondPhysicsManager = BeyondPhysicsManager.getInstance();
            if (beyondPhysicsManager == null) {
                beyondPhysicsManager = BeyondPhysicsManager.getInstanceByBeyondPhysicsManagerParams(BaseActivity.getDefaultBeyondPhysicsManagerParams(context));
            }
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BaseActivity_getBeyondPhysicsManager:context异常", null, 1);
        }
        return beyondPhysicsManager;
    }
}

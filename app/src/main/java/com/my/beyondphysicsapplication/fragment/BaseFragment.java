package com.my.beyondphysicsapplication.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.beyondphysics.ui.BaseActivity;


public class BaseFragment extends Fragment {
    public static final String BUNDLEKEY_POSITION_KEY = "position_key";

    private BaseActivity baseActivity;
    public Handler handler = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
    }

    public BaseActivity getBaseActivity() {
        if (baseActivity == null) {
            throw new IllegalArgumentException("the acticity must be extends BaseActivity");
        }
        return baseActivity;
    }

    /***
     * 包含了onCreateView里面需要的所有的初始化操作
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
     * 配置ui参数如点击事件等的初始化
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
     * 有些安卓手机检测到栈顶的activity结构是viewPager,fragment,recyclerView并且recyclerView是visiable的时候会引用这个栈顶导致内存无法释放
     * 退出时候设置一下这个就行了
     */
    public void setRecyclerViewInVisible() {

    }


    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}

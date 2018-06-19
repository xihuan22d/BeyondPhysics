package com.my.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xihuan22 on 2017/7/21.
 */

public class ViewPagerForPhotoView extends ViewPager {

    public ViewPagerForPhotoView(Context context) {
        super(context);
    }

    public ViewPagerForPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();//如果使用ImageView直接使用原本的ViewPager就行,如果使用手势控件如PhotoView进行缩小操作时可能触发异常,这是PhotoView作者推荐的解决办法,虽然不是很好,但如果想使用第三方的手势功能,就应该加上
            return false;
        }
    }
}

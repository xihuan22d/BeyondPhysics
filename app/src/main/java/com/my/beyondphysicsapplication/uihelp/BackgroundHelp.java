package com.my.beyondphysicsapplication.uihelp;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;

import com.beyondphysics.ui.BaseActivity;
import com.my.beyondphysicsapplication.R;


public class BackgroundHelp {

    public static void setGradientDrawableStroke(View view, int color, int strokeWidth) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setStroke(strokeWidth, color);
        }
    }

    public static void setGradientDrawableStroke_default(BaseActivity baseActivity, View view, int color) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            int drawable_normal_stroke_default = baseActivity.getResources().getDimensionPixelSize(
                    R.dimen.drawable_normal_stroke_default);
            gradientDrawable.setStroke(drawable_normal_stroke_default, color);
        }
    }

    public static void setGradientDrawableColor(View view, int color) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(color);
        }
    }

    public static GradientDrawable createGradientDrawable(int color, int strokeWidth, int strokeColor, float radiusWidth) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setCornerRadius(radiusWidth);
        return gradientDrawable;
    }

    //只要存在不是全透明的不管solid还是stroke,都会导致api21以上失去阴影效果
    public static void setBackgroundStateListDrawableWithSolid(View view, int colorDeepen, int colorNormal, float radiusWidth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, BackgroundHelp.createGradientDrawable(colorDeepen, 0, 0xffffffff, radiusWidth));
            stateListDrawable.addState(new int[]{}, BackgroundHelp.createGradientDrawable(colorNormal, 0, 0xffffffff, radiusWidth));
            view.setBackground(stateListDrawable);
        }
    }

    public static void setBackgroundStateListDrawableWithStroke(View view, int colorDeepen, int colorNormal, int strokeWidth, float radiusWidth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, BackgroundHelp.createGradientDrawable(0x00ffffff, strokeWidth, colorDeepen, radiusWidth));
            stateListDrawable.addState(new int[]{}, BackgroundHelp.createGradientDrawable(0x00ffffff, strokeWidth, colorNormal, radiusWidth));
            view.setBackground(stateListDrawable);
        }
    }

}

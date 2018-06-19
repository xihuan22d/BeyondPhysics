package com.my.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.beyondphysics.ui.BaseActivity;

public class KeyboardDetectFrameLayout extends FrameLayout {
    private OnKeyboardDetectFrameLayoutListener onKeyboardDetectFrameLayoutListener;

    private KeyboardOnGlobalChangeListener keyboardOnGlobalChangeListener;
    private boolean keyboardActive = false;
    private int keyboardHeight = 0;

    public KeyboardDetectFrameLayout(@NonNull Context context) {
        super(context);
    }

    public KeyboardDetectFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardDetectFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OnKeyboardDetectFrameLayoutListener getOnKeyboardDetectFrameLayoutListener() {
        return onKeyboardDetectFrameLayoutListener;
    }

    public void setOnKeyboardDetectFrameLayoutListener(OnKeyboardDetectFrameLayoutListener onKeyboardDetectFrameLayoutListener) {
        this.onKeyboardDetectFrameLayoutListener = onKeyboardDetectFrameLayoutListener;
    }

    public boolean isKeyboardActive() {
        return keyboardActive;
    }

    public void setKeyboardActive(boolean keyboardActive) {
        this.keyboardActive = keyboardActive;
    }


    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (keyboardOnGlobalChangeListener == null) {
            keyboardOnGlobalChangeListener = new KeyboardOnGlobalChangeListener();
        }
        getViewTreeObserver().addOnGlobalLayoutListener(keyboardOnGlobalChangeListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (keyboardOnGlobalChangeListener != null) {
            getViewTreeObserver().removeGlobalOnLayoutListener(keyboardOnGlobalChangeListener);
        }
        super.onDetachedFromWindow();
    }

    private class KeyboardOnGlobalChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            Activity activity = BaseActivity.getActivityByContext(getContext());
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = BaseActivity.getScreenHeight(activity);
            int keyboardHeight = screenHeight - rect.bottom;
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 5) {
                isActive = true;
                KeyboardDetectFrameLayout.this.keyboardHeight = keyboardHeight;
            }
            if (isActive != keyboardActive) {
                if (onKeyboardDetectFrameLayoutListener != null) {
                    onKeyboardDetectFrameLayoutListener.onKeyboardStateChanged(isActive, keyboardHeight);
                }
                keyboardActive = isActive;
            }
        }
    }

    public interface OnKeyboardDetectFrameLayoutListener {

        void onKeyboardStateChanged(boolean isActive, int keyboardHeight);
    }

}

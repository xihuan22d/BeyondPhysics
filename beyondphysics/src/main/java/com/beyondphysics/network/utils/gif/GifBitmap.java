package com.beyondphysics.network.utils.gif;


import android.graphics.Bitmap;

/**
 * Created by xihuan22 on 2018/6/2.
 */
public class GifBitmap {
    private int width;
    private int height;
    private int delay;
    private Bitmap bitmap;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}

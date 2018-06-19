package com.beyondphysics.network.utils.gif;


import android.graphics.Bitmap;

/**
 * Created by xihuan22 on 2018/6/2.
 */
public class GifFrame {
    private int ix;
    private int iy;
    private int iw;
    private int ih;
    private boolean interlace;
    private boolean transparency;
    private int dispose;
    private int transIndex;
    private int delay;
    private int bufferFrameStart;
    private int[] lct;
    private Bitmap bitmap;


    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }

    public int getIy() {
        return iy;
    }

    public void setIy(int iy) {
        this.iy = iy;
    }

    public int getIw() {
        return iw;
    }

    public void setIw(int iw) {
        this.iw = iw;
    }

    public int getIh() {
        return ih;
    }

    public void setIh(int ih) {
        this.ih = ih;
    }

    public boolean isInterlace() {
        return interlace;
    }

    public void setInterlace(boolean interlace) {
        this.interlace = interlace;
    }

    public boolean isTransparency() {
        return transparency;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public int getDispose() {
        return dispose;
    }

    public void setDispose(int dispose) {
        this.dispose = dispose;
    }

    public int getTransIndex() {
        return transIndex;
    }

    public void setTransIndex(int transIndex) {
        this.transIndex = transIndex;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getBufferFrameStart() {
        return bufferFrameStart;
    }

    public void setBufferFrameStart(int bufferFrameStart) {
        this.bufferFrameStart = bufferFrameStart;
    }

    public int[] getLct() {
        return lct;
    }

    public void setLct(int[] lct) {
        this.lct = lct;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

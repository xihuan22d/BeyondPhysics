package com.beyondphysics.network.utils;


import android.graphics.Bitmap;

import com.beyondphysics.network.utils.gif.GifBitmap;

import java.util.List;

/**
 * Created by xihuan22 on 2018/6/2.
 */
public class SuperBitmap {

    private boolean decodeGif;

    private int bitmapType;

    private int size;

    private Bitmap bitmap;

    private List<GifBitmap> gifBitmaps;


    public boolean isDecodeGif() {
        return decodeGif;
    }

    public void setDecodeGif(boolean decodeGif) {
        this.decodeGif = decodeGif;
    }

    public int getBitmapType() {
        return bitmapType;
    }

    public void setBitmapType(int bitmapType) {
        this.bitmapType = bitmapType;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        int theSize = 0;
        if (bitmap != null) {
            theSize = bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
        size = theSize;
        this.bitmap = bitmap;
    }

    public List<GifBitmap> getGifBitmaps() {
        return gifBitmaps;
    }

    public void setGifBitmaps(List<GifBitmap> gifBitmaps) {
        int theSize = 0;
        if (gifBitmaps != null) {
            for (int i = 0; i < gifBitmaps.size(); i++) {
                GifBitmap gifBitmap = gifBitmaps.get(i);
                if (gifBitmap != null && gifBitmap.getBitmap() != null) {
                    Bitmap bitmap = gifBitmap.getBitmap();
                    theSize = theSize + bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            }
        }
        size = theSize;
        this.gifBitmaps = gifBitmaps;
    }

    @Override
    public String toString() {
        return "SuperBitmap{" +
                "decodeGif=" + decodeGif +
                ", bitmapType=" + bitmapType +
                ", size=" + size +
                ", bitmap=" + bitmap +
                ", gifBitmaps=" + gifBitmaps +
                '}';
    }
}

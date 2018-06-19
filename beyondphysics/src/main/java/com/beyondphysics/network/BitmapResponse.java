package com.beyondphysics.network;


import com.beyondphysics.network.utils.SuperBitmap;

/**
 * Created by xihuan22 on 2017/9/15.
 */

public class BitmapResponse {

    private String urlString;
    private String key;
    private String tag;
    private int bitmapGetFrom;
    private SuperBitmap superBitmap;


    public BitmapResponse() {

    }

    public BitmapResponse(String urlString, String key, String tag, int bitmapGetFrom, SuperBitmap superBitmap) {
        this.urlString = urlString;
        this.key = key;
        this.tag = tag;
        this.bitmapGetFrom = bitmapGetFrom;
        this.superBitmap = superBitmap;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getBitmapGetFrom() {
        return bitmapGetFrom;
    }

    public void setBitmapGetFrom(int bitmapGetFrom) {
        this.bitmapGetFrom = bitmapGetFrom;
    }

    public SuperBitmap getSuperBitmap() {
        return superBitmap;
    }

    public void setSuperBitmap(SuperBitmap superBitmap) {
        this.superBitmap = superBitmap;
    }

    @Override
    public String toString() {
        return "BitmapResponse{" +
                "urlString='" + urlString + '\'' +
                ", key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", bitmapGetFrom=" + bitmapGetFrom +
                ", superBitmap=" + superBitmap +
                '}';
    }
}

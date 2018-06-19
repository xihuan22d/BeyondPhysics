package com.beyondphysics.ui.imagechooselibrary.models;

import java.io.Serializable;

/**
 * Created by xihuan22 on 2017/7/7.
 */

public class ImageItem implements Serializable {

    private String name;       //图片的名字
    private String path;       //图片的路径
    private long size;         //图片的大小
    private int width;         //图片的宽度
    private int height;        //图片的高度
    private String mime_type;   //图片的类型
    private long date_added;      //图片的创建时间
    private boolean select;      //是否选中

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

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

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public long getDate_added() {
        return date_added;
    }

    public void setDate_added(long date_added) {
        this.date_added = date_added;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", width=" + width +
                ", height=" + height +
                ", mime_type='" + mime_type + '\'' +
                ", date_added=" + date_added +
                ", select=" + select +
                '}';
    }
}

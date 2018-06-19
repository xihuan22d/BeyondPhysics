package com.beyondphysics.ui.imagechooselibrary.models;

import java.util.List;

/**
 * Created by xihuan22 on 2017/7/7.
 */

public class ImageFolder {

    private String name;  //当前文件夹的名字
    private String path;  //当前文件夹的路径
    private ImageItem imageItemThumbnail;   //当前文件夹需要显示的缩略图
    private List<ImageItem> imageItems;  //当前文件夹下所有图片合集

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

    public ImageItem getImageItemThumbnail() {
        return imageItemThumbnail;
    }

    public void setImageItemThumbnail(ImageItem imageItemThumbnail) {
        this.imageItemThumbnail = imageItemThumbnail;
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    @Override
    public String toString() {
        return "ImageFolder{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", imageItemThumbnail=" + imageItemThumbnail +
                ", imageItems=" + imageItems +
                '}';
    }
}

package com.beyondphysics.ui.utils;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyondphysics.network.BitmapRequest_Default_Params;
import com.beyondphysics.ui.views.NetworkImageView;

/**
 * Created by xihuan22 on 2017/9/15.
 */

public class NetworkImageViewHelp {
    public static final int DISKCACHETYPE_FILE = 1;
    public static final int DISKCACHETYPE_ASSETS = 2;
    public static final int DISKCACHETYPE_RESOURCE = 3;

    /**
     * diskCacheType为1表示添加file://,为2表示添加assets://,为3表示添加resource://,resource资源需要在末尾加上如.gif才能支持解析对于格式,否则默认为png
     */
    public static String getDiskCacheUrlString(String urlString, int diskCacheType) {
        String diskCacheUrlString = null;
        if (urlString != null) {
            if (diskCacheType == NetworkImageViewHelp.DISKCACHETYPE_FILE) {
                diskCacheUrlString = "file://" + urlString;
            } else if (diskCacheType == NetworkImageViewHelp.DISKCACHETYPE_ASSETS) {
                diskCacheUrlString = "assets://" + urlString;
            } else if (diskCacheType == NetworkImageViewHelp.DISKCACHETYPE_RESOURCE) {
                diskCacheUrlString = "resource://" + urlString;
            }
        }
        return diskCacheUrlString;
    }


    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromDiskCacheWithPost(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImageWithPost(diskCacheUrlString, tag);
    }

    public static void getImageFromDiskCache(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(diskCacheUrlString, tag);
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromDiskCacheWithPost(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImageWithPost(diskCacheUrlString, tag, scaleType);
    }

    public static void getImageFromDiskCache(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(diskCacheUrlString, tag, scaleType);
    }

    public static void getImageFromDiskCache(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, int width, int height, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(diskCacheUrlString, tag, width, height);
    }

    public static void getImageFromDiskCacheWithNewParams(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, int width, int height, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkImageView.setLayoutParams(layoutParams);
        networkImageView.getImage(diskCacheUrlString, tag, width, height);
    }

    public static void getImageFromDiskCache(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(diskCacheUrlString, tag, width, height, scaleType);
    }

    public static void getImageFromDiskCacheWithNewParams(NetworkImageView networkImageView, String urlString, int diskCacheType, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkImageView.setLayoutParams(layoutParams);
        networkImageView.getImage(diskCacheUrlString, tag, width, height, scaleType);
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromNetworkWithPost(NetworkImageView networkImageView, String urlString, String tag, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImageWithPost(urlString, tag);
    }

    public static void getImageFromNetwork(NetworkImageView networkImageView, String urlString, String tag, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(urlString, tag);
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromNetworkWithPost(NetworkImageView networkImageView, String urlString, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImageWithPost(urlString, tag, scaleType);
    }

    public static void getImageFromNetwork(NetworkImageView networkImageView, String urlString, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(urlString, tag, scaleType);
    }

    public static void getImageFromNetwork(NetworkImageView networkImageView, String urlString, String tag, int width, int height, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(urlString, tag, width, height);
    }

    public static void getImageFromNetworkWithNewParams(NetworkImageView networkImageView, String urlString, String tag, int width, int height, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkImageView.setLayoutParams(layoutParams);
        networkImageView.getImage(urlString, tag, width, height);
    }

    public static void getImageFromNetwork(NetworkImageView networkImageView, String urlString, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(urlString, tag, width, height, scaleType);
    }

    public static void getImageFromNetworkWithNewParams(NetworkImageView networkImageView, String urlString, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkImageView.setLayoutParams(layoutParams);
        networkImageView.getImage(urlString, tag, width, height, scaleType);
    }


    public static void getImageByBitmapRequestParams(NetworkImageView networkImageView, BitmapRequest_Default_Params bitmapRequest_Default_Params, int defaultResId, int errorResId) {
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(bitmapRequest_Default_Params);
    }
}

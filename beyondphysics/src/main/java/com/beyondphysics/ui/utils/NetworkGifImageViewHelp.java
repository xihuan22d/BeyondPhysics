package com.beyondphysics.ui.utils;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyondphysics.network.BitmapRequest_Default_Params;
import com.beyondphysics.ui.views.NetworkGifImageView;

/**
 * Created by xihuan22 on 2018/6/3.
 */
public class NetworkGifImageViewHelp {
    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromDiskCacheWithPost(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImageWithPost(diskCacheUrlString, tag);
    }

    public static void getImageFromDiskCache(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(diskCacheUrlString, tag);
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromDiskCacheWithPost(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImageWithPost(diskCacheUrlString, tag, scaleType);
    }

    public static void getImageFromDiskCache(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(diskCacheUrlString, tag, scaleType);
    }

    public static void getImageFromDiskCache(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, int width, int height, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(diskCacheUrlString, tag, width, height);
    }

    public static void getImageFromDiskCacheWithNewParams(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, int width, int height, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkGifImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkGifImageView.setLayoutParams(layoutParams);
        networkGifImageView.getImage(diskCacheUrlString, tag, width, height);
    }

    public static void getImageFromDiskCache(NetworkGifImageView networkImageView, String urlString, int diskCacheType, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkImageView.setDefaultImageResId(defaultResId);
        networkImageView.setErrorImageResId(errorResId);
        networkImageView.getImage(diskCacheUrlString, tag, width, height, scaleType);
    }

    public static void getImageFromDiskCacheWithNewParams(NetworkGifImageView networkGifImageView, String urlString, int diskCacheType, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        String diskCacheUrlString = NetworkImageViewHelp.getDiskCacheUrlString(urlString, diskCacheType);
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkGifImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkGifImageView.setLayoutParams(layoutParams);
        networkGifImageView.getImage(diskCacheUrlString, tag, width, height, scaleType);
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromNetworkWithPost(NetworkGifImageView networkGifImageView, String urlString, String tag, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImageWithPost(urlString, tag);
    }

    public static void getImageFromNetwork(NetworkGifImageView networkGifImageView, String urlString, String tag, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(urlString, tag);
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public static void getImageFromNetworkWithPost(NetworkGifImageView networkGifImageView, String urlString, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImageWithPost(urlString, tag, scaleType);
    }

    public static void getImageFromNetwork(NetworkGifImageView networkGifImageView, String urlString, String tag, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(urlString, tag, scaleType);
    }

    public static void getImageFromNetwork(NetworkGifImageView networkGifImageView, String urlString, String tag, int width, int height, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(urlString, tag, width, height);
    }

    public static void getImageFromNetworkWithNewParams(NetworkGifImageView networkGifImageView, String urlString, String tag, int width, int height, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkGifImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkGifImageView.setLayoutParams(layoutParams);
        networkGifImageView.getImage(urlString, tag, width, height);
    }

    public static void getImageFromNetwork(NetworkGifImageView networkGifImageView, String urlString, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(urlString, tag, width, height, scaleType);
    }

    public static void getImageFromNetworkWithNewParams(NetworkGifImageView networkGifImageView, String urlString, String tag, int width, int height, ImageView.ScaleType scaleType, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        ViewGroup.LayoutParams layoutParams = networkGifImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        networkGifImageView.setLayoutParams(layoutParams);
        networkGifImageView.getImage(urlString, tag, width, height, scaleType);
    }


    public static void getImageByBitmapRequestParams(NetworkGifImageView networkGifImageView, BitmapRequest_Default_Params bitmapRequest_Default_Params, int defaultResId, int errorResId) {
        networkGifImageView.setDefaultImageResId(defaultResId);
        networkGifImageView.setErrorImageResId(errorResId);
        networkGifImageView.getImage(bitmapRequest_Default_Params);
    }
}

package com.beyondphysics.ui.imagechooselibrary.utils;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.beyondphysics.ui.imagechooselibrary.models.ImageFolder;
import com.beyondphysics.ui.imagechooselibrary.models.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xihuan22 on 2017/7/7.
 */

public class SdcardDataSourceHelper implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String PATHERROR = "error";
    public final int LOADER_ALL = 0;         //加载所有图片
    public final int LOADER_CATEGORY = 1;    //加载指定目录图片
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小 long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度 int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度 int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型 image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间 long型  1450518608

    private AppCompatActivity appCompatActivity;
    private OnSdcardDataSourceLoadedListener onSdcardDataSourceLoadedListener;
    private boolean hasLoaded = false;


    public SdcardDataSourceHelper(AppCompatActivity appCompatActivity, String loadRootPath, OnSdcardDataSourceLoadedListener onSdcardDataSourceLoadedListener) {
        this.appCompatActivity = appCompatActivity;
        this.onSdcardDataSourceLoadedListener = onSdcardDataSourceLoadedListener;
        LoaderManager loaderManager = appCompatActivity.getSupportLoaderManager();
        Bundle bundle = new Bundle();
        if (loadRootPath == null) {
            bundle.putString("path", "/");
            loaderManager.initLoader(LOADER_ALL, bundle, this);
        } else {
            bundle.putString("path", loadRootPath);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        if (id == LOADER_ALL) {//扫描所有图片
            cursorLoader = new CursorLoader(appCompatActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        } else if (id == LOADER_CATEGORY) {//扫描某个图片文件夹
            cursorLoader = new CursorLoader(appCompatActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<ImageFolder> imageFolders = new ArrayList<>();   //所有的图片文件夹
        if (data != null) {
            if (data.moveToFirst()) {
                data.moveToPrevious();
            }
            List<ImageItem> imageItemsAll = new ArrayList<>();   //所有的图片集合
            while (data.moveToNext()) {
                String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                if (path == null) {
                    path = PATHERROR;
                }
                long size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int width = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int height = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String mime_type = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long date_added = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                ImageItem imageItem = new ImageItem();
                imageItem.setName(name);
                imageItem.setPath(path);
                imageItem.setSize(size);
                imageItem.setWidth(width);
                imageItem.setHeight(height);
                imageItem.setMime_type(mime_type);
                imageItem.setDate_added(date_added);
                imageItemsAll.add(imageItem);
                File file = new File(path);
                File fileParent = file.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                if (fileParent != null) {
                    imageFolder.setName(fileParent.getName());
                    imageFolder.setPath(fileParent.getAbsolutePath());
                } else {
                    imageFolder.setName(PATHERROR);
                    imageFolder.setPath(PATHERROR);
                }
                int res = isHaveImageFolder(imageFolders, imageFolder.getPath());
                if (res == -1) {
                    List<ImageItem> imageItems = new ArrayList<>();
                    imageItems.add(imageItem);
                    imageFolder.setImageItemThumbnail(imageItem);
                    imageFolder.setImageItems(imageItems);
                    imageFolders.add(imageFolder);
                } else {
                    if (imageFolders.get(res).getImageItems() != null) {
                        imageFolders.get(res).getImageItems().add(imageItem);
                    }
                }
            }
            ImageItem imageItemThumbnail = null;
            if (imageItemsAll.size() > 0) {
                imageItemThumbnail = imageItemsAll.get(0);
            }
            if (imageItemsAll.size() > 0) {
                ImageFolder imageFolderAll = new ImageFolder();
                imageFolderAll.setName("全部图片");
                imageFolderAll.setPath("/");
                imageFolderAll.setImageItemThumbnail(imageItemThumbnail);
                imageFolderAll.setImageItems(imageItemsAll);
                imageFolders.add(0, imageFolderAll);  //确保第一条是所有图片
            }
        }
        if (!hasLoaded) {
            if (onSdcardDataSourceLoadedListener != null) {
                onSdcardDataSourceLoadedListener.sdcardDataSourceLoaded(imageFolders);
            }
            hasLoaded = true;
        } else {
           // BaseActivity.showShortToast(appCompatActivity, "数据已有更新,您可以刷新获取最新数据");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private int isHaveImageFolder(List<ImageFolder> imageFolders, String path) {
        int res = -1;
        if (imageFolders == null || path == null) {
            return res;
        }
        for (int i = 0; i < imageFolders.size(); i++) {
            ImageFolder imageFolder = imageFolders.get(i);
            if (imageFolder.getPath() != null && imageFolder.getPath().equals(path)) {
                res = i;
                break;
            }
        }
        return res;
    }

    public interface OnSdcardDataSourceLoadedListener {
        void sdcardDataSourceLoaded(List<ImageFolder> imageFolders);
    }
}

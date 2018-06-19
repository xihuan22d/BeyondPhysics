package com.beyondphysics.ui.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

public class GetPathFromUriTool {

    /**
     * Android4.4后从uri获取文件绝对路径
     */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) {
        String result = null;
        if (uri == null) {
            return result;
        }
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (id != null) {
                    String[] split = id.split(":");
                    String type = split[0];
                    if (split.length >= 2) {
                        if (type.equalsIgnoreCase("primary")) {
                            String sDStateString = android.os.Environment.getExternalStorageState();
                            if (sDStateString != null && sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
                                File fileSdcardDir = android.os.Environment.getExternalStorageDirectory();
                                if (fileSdcardDir != null) {
                                    result = fileSdcardDir.getAbsolutePath() + File.separator + split[1];
                                }
                            }
                        }
                    }
                }
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = null;
                try {
                    contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } catch (Exception e) {//string转long的异常捕捉
                    e.printStackTrace();
                }
                result = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (id != null) {
                    String[] split = id.split(":");
                    String type = split[0];
                    Uri contentUri = null;
                    if (type.equals("image")) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (type.equals("video")) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (type.equals("audio")) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = "_id=?";
                    String[] selectionArgs = null;
                    if (split.length >= 2) {
                        selectionArgs = new String[]{split[1]};
                    }
                    result = getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        } else {//4.4以下使用旧方法
            String scheme = uri.getScheme();
            if (scheme != null) {
                if (scheme.equalsIgnoreCase("content")) {
                    if (isGooglePhotosUri(uri)) {
                        result = uri.getLastPathSegment();
                    } else {
                        result = getDataColumn(context, uri, null, null);
                    }
                } else if (scheme.equalsIgnoreCase("file")) {
                    result = uri.getPath();
                }
            }
            if (result == null) {//如果上面的方式还拿不到,再试一次另一种不用scheme的方式
                result = getPathBeforeKitkat(context, uri);
            }
        }
        return result;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String result = null;
        if (uri == null) {
            return result;
        }
        try {
            Cursor cursor = null;
            String column = MediaStore.Images.Media.DATA;
            String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(column);
                    result = cursor.getString(column_index);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        if (uri.getAuthority() != null && uri.getAuthority().equals("com.android.externalstorage.documents")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDownloadsDocument(Uri uri) {
        if (uri.getAuthority() != null && uri.getAuthority().equals("com.android.providers.downloads.documents")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isMediaDocument(Uri uri) {
        if (uri.getAuthority() != null && uri.getAuthority().equals("com.android.providers.media.documents")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        if (uri.getAuthority() != null && uri.getAuthority().equals("com.google.android.apps.photos.content")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从相册获取图片,4.4后系统不行
     */
    public static String getPathBeforeKitkat(Context context, Uri uri) {
        String result = null;
        if (uri == null) {
            return result;
        }
        try {
            String[] split = uri.toString().split(":");//一定不为null
            if (split[0].equals("content")) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        result = cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (split[0].equals("file")) {
                if (split.length >= 2) {
                    result = split[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
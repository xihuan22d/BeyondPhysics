package com.beyondphysics.network.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView.ScaleType;

import com.beyondphysics.network.BitmapConfig;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.utils.gif.GifBitmap;
import com.beyondphysics.network.utils.gif.GifDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xihuan22 on 2017/8/1.
 */
public class FileTool {
    public static final String LOG_TAG = "beyondphysics";
    public static final String DEFAULT_ROOT_PATH = "beyondphysics";

    public static String getSdcardRootPath(String rootPath) {
        String sDStateString = android.os.Environment.getExternalStorageState();
        if (sDStateString == null || !sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File fileSdcardDir = android.os.Environment.getExternalStorageDirectory();
        if (fileSdcardDir == null) {
            return null;
        }
        String sdcardRootPath = fileSdcardDir.getAbsolutePath() + File.separator + rootPath;
        File fileRootDir = new File(sdcardRootPath);
        if (!fileRootDir.exists()) {
            fileRootDir.mkdir();
        }
        return sdcardRootPath;
    }

    public static String getDefaultRootPath() {
        return getSdcardRootPath(DEFAULT_ROOT_PATH);
    }

    public static void mkdirs(String filePath) {
        if (filePath == null) {
            return;
        }
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    /**
     * 日志超过100M就会被删除
     */
    public static void needShowAndWriteLogToSdcard(boolean openDebug, String fileName, String log, Throwable throwable, int type) {
        if (openDebug) {
            showAndWriteLog(true, fileName, log, throwable, type);
        }
    }

    public static void needWriteLogToSdcard(boolean openDebug, String fileName, String log, Throwable throwable, int type) {
        if (openDebug) {
            showAndWriteLog(false, fileName, log, throwable, type);
        }
    }

    /**
     * 当前框架内所有线程记录日志都统一调用该方法
     */
    public static synchronized void showAndWriteLog(boolean showLog, String fileName, String log, Throwable throwable, int type) {
        if (log == null) {
            return;
        }
        String theFileName = null;
        if (fileName == null) {
            theFileName = getDefaultRootPath() + File.separator + "Log.txt";
        } else {
            theFileName = fileName;
        }
        String throwableContent = getThrowableContent(throwable);
        String theLog = null;
        if (throwableContent != null) {
            theLog = log + "__throwableContent:" + throwableContent;
        } else {
            theLog = log;
        }
        String newLog = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(new Date());
        if (type == 1) {
            newLog = "Error_" + time + ":" + theLog + "\n";
            if (showLog) {
                Log.e(LOG_TAG, newLog);
            }
        } else if (type == 2) {
            newLog = "Waring_" + time + ":" + theLog + "\n";
            if (showLog) {
                Log.w(LOG_TAG, newLog);
            }
        } else {
            newLog = "Debug_" + time + ":" + theLog + "\n";
            if (showLog) {
                Log.d(LOG_TAG, newLog);
            }
        }
        long length = getFileLength(fileName);
        if (length > 104857600) {
            deleteFile(fileName);
        }
        writeContent(theFileName, newLog, true);
    }


    public static String getThrowableContent(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        String bugContent = null;
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;
        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            Throwable throwableCause = throwable.getCause();
            while (throwableCause != null) {
                throwableCause.printStackTrace(printWriter);
                throwableCause = throwableCause.getCause();
            }
            bugContent = stringWriter.toString();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (stringWriter != null) {
                    stringWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bugContent;
    }


    public static long writeContent(String fileName, String content, boolean append) {
        long fileLength = -1;
        if (fileName == null || content == null) {
            return fileLength;
        }
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file, append);
            byte[] bytes = content.getBytes("utf-8");
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileLength = getFileLength(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileLength;
    }

    public static String readContent(String fileName) {
        String result = null;
        if (fileName == null) {
            return result;
        }
        FileInputStream fileInputStream = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                int length = fileInputStream.available();
                byte[] bytes = new byte[length];
                fileInputStream.read(bytes);
                result = new String(bytes, "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 该方法外部不可再加fileName对应的锁,否则可能导致死锁
     * 同一个线程内不可以对相同的fileName执行多次getFileNameLock,否则会发生死锁,必须获取和移除一一对应,中间不能再夹着获取
     */
    public static long writeContentWithLock(String fileName, String content, boolean append) {
        long fileLength = -1;
        if (fileName == null || content == null) {
            return fileLength;
        }
        try {
            FileNameLockManager.getInstance().getFileNameLock(fileName);
            FileOutputStream fileOutputStream = null;
            try {
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file, append);
                byte[] bytes = content.getBytes("utf-8");
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                fileLength = getFileLength(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            FileNameLockManager.getInstance().removeFileNameLock(fileName);
        }
        return fileLength;
    }

    public static String readContentWithLock(String fileName) {
        String result = null;
        if (fileName == null) {
            return result;
        }
        try {
            FileNameLockManager.getInstance().getFileNameLock(fileName);
            FileInputStream fileInputStream = null;
            try {
                File file = new File(fileName);
                if (file.exists()) {
                    fileInputStream = new FileInputStream(file);
                    int length = fileInputStream.available();
                    byte[] bytes = new byte[length];
                    fileInputStream.read(bytes);
                    result = new String(bytes, "utf-8");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            FileNameLockManager.getInstance().removeFileNameLock(fileName);
        }
        return result;
    }


    /**
     * 获取图片的宽高,不会返回null,一定会返回一个长度为2的数组
     */
    public static int[] getBitmapWidthAndHeight(String fileName) {
        int[] res = new int[2];
        if (fileName == null) {
            return res;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return res;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        res[0] = options.outWidth;
        res[1] = options.outHeight;
        return res;
    }


    /**
     * 压缩图片
     */
    public static Bitmap getCompressBitmapFromFile(String fileName, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (fileName == null || bitmapConfig == null) {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "getCompressBitmapFromFile:压缩图片异常" + fileName, null, 1);
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = options.outWidth / reqWidth;
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = options.outHeight / reqHeight;
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0) {
            int zoomWidth = options.outWidth / reqWidth;
            int zoomHeight = options.outHeight / reqHeight;
            if (zoomWidth < zoomHeight) {//获得俩者间最小比例作为缩放比
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;//上面3种情况都有可能等于0的
        }
        options.inSampleSize = zoom;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = bitmapConfig.getConfig();
        Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
        return resizeBitmap(bitmap, reqWidth, reqHeight, scaleType, bitmapConfig);
    }

    /**
     * 读取Assets下压缩图片
     */
    public static Bitmap getCompressBitmapFromAssets(Context context, String assetsName, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (context == null || assetsName == null || bitmapConfig == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeAssets(context, assetsName, options);
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = options.outWidth / reqWidth;
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = options.outHeight / reqHeight;
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0) {
            int zoomWidth = options.outWidth / reqWidth;
            int zoomHeight = options.outHeight / reqHeight;
            if (zoomWidth < zoomHeight) {
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;
        }
        options.inSampleSize = zoom;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = bitmapConfig.getConfig();
        Bitmap bitmap = decodeAssets(context, assetsName, options);
        return resizeBitmap(bitmap, reqWidth, reqHeight, scaleType, bitmapConfig);
    }

    public static Bitmap decodeAssets(Context context, String assetsName, BitmapFactory.Options options) {
        if (context == null || assetsName == null) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(assetsName);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 读取Resource下压缩图片,如R.drawable.icon,R.mipmap.icon
     */
    public static Bitmap getCompressBitmapFromResource(Context context, int resourceId, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (context == null || resourceId == -1 || bitmapConfig == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeResource(context, resourceId, options);
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = options.outWidth / reqWidth;
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = options.outHeight / reqHeight;
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0) {
            int zoomWidth = options.outWidth / reqWidth;
            int zoomHeight = options.outHeight / reqHeight;
            if (zoomWidth < zoomHeight) {
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;
        }
        options.inSampleSize = zoom;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = bitmapConfig.getConfig();
        Bitmap bitmap = decodeResource(context, resourceId, options);
        return resizeBitmap(bitmap, reqWidth, reqHeight, scaleType, bitmapConfig);
    }

    public static Bitmap decodeResource(Context context, int resourceId, BitmapFactory.Options options) {
        if (context == null || resourceId == -1) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(resourceId, new TypedValue());
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap getCompressBitmapFromBitmap(Bitmap bitmap, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (bitmap == null || bitmapConfig == null) {
            return null;
        }
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = bitmap.getWidth() / reqWidth;
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = bitmap.getHeight() / reqHeight;
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0) {
            int zoomWidth = bitmap.getWidth() / reqWidth;
            int zoomHeight = bitmap.getHeight() / reqHeight;
            if (zoomWidth < zoomHeight) {
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(zoom, zoom);
        Bitmap theBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBitmap(theBitmap, reqWidth, reqHeight, scaleType, bitmapConfig);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (bitmapConfig != null) {
            bitmap = bitmapConfig.resizeBitmap(bitmap, reqWidth, reqHeight, scaleType);
        }
        return bitmap;
    }

    public static List<GifBitmap> decodeGifFromFile(String fileName, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        List<GifBitmap> gifBitmaps = null;
        if (fileName == null || bitmapConfig == null) {
            return gifBitmaps;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "decodeGif:解码gif异常" + fileName, null, 1);
            return gifBitmaps;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            int length = fileInputStream.available();
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);
            gifBitmaps = getGifBitmaps(bytes, reqWidth, reqHeight, scaleType, bitmapConfig);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gifBitmaps;
    }

    public static List<GifBitmap> decodeGifFromAssets(Context context, String assetsName, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        List<GifBitmap> gifBitmaps = null;
        if (context == null || assetsName == null || bitmapConfig == null) {
            return gifBitmaps;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(assetsName);
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            gifBitmaps = getGifBitmaps(bytes, reqWidth, reqHeight, scaleType, bitmapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return gifBitmaps;
    }

    public static List<GifBitmap> decodeGifFromResource(Context context, int resourceId, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        List<GifBitmap> gifBitmaps = null;
        if (context == null || resourceId == -1 || bitmapConfig == null) {
            return gifBitmaps;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(resourceId, new TypedValue());
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            gifBitmaps = getGifBitmaps(bytes, reqWidth, reqHeight, scaleType, bitmapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return gifBitmaps;
    }


    public static List<GifBitmap> getGifBitmaps(byte[] bytes, int reqWidth, int reqHeight, ScaleType scaleType, BitmapConfig bitmapConfig) {
        if (bytes == null || bitmapConfig == null) {
            return null;
        }
        Bitmap.Config config = null;
        if (bitmapConfig.getRoundedType() != BitmapConfig.ROUNDEDTYPE_NONE) {
            config = bitmapConfig.getConfig();
        }
        GifDecoder gifDecoder = new GifDecoder();
        List<GifBitmap> gifBitmaps = gifDecoder.decode(bytes, reqWidth, reqHeight, config);
        if (gifBitmaps != null) {
            for (int i = 0; i < gifBitmaps.size(); i++) {
                GifBitmap gifBitmap = gifBitmaps.get(i);
                if (gifBitmap != null && gifBitmap.getBitmap() != null) {
                    gifBitmap.setBitmap(resizeBitmap(gifBitmap.getBitmap(), reqWidth, reqHeight, scaleType, bitmapConfig));
                }
            }
        }
        return gifBitmaps;
    }


    public static String saveBitmapWithJPG(String savePath, String lastFileName, Bitmap bitmap, int quality) {
        if (savePath == null || lastFileName == null || bitmap == null) {
            return null;
        }
        FileTool.mkdirs(savePath);
        String fileName = savePath + File.separator + lastFileName;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    public static String saveBitmapWithPNG(String savePath, String lastFileName, Bitmap bitmap) {
        if (savePath == null || lastFileName == null || bitmap == null) {
            return null;
        }
        FileTool.mkdirs(savePath);
        String fileName = savePath + File.separator + lastFileName;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 获得文件字节的长度,-1表示文件不存在
     */
    public static long getFileLength(String fileName) {
        long fileLength = -1;
        if (fileName == null) {
            return fileLength;
        }
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            fileLength = file.length();
        }
        return fileLength;
    }

    /**
     * 删除指定文件或者文件夹
     */
    public static void deleteFile(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件夹总大小
     */
    public static long getFolderSize(String filePath) {
        long size = 0;
        if (filePath == null) {
            return size;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return size;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                File file1 = listFiles[i];
                if (file1 != null) {
                    if (file1.isDirectory()) {
                        size = size + getFolderSize(file1.getAbsolutePath());
                    } else {
                        size = size + file1.length();
                    }
                }
            }
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolder(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    file.delete();
                    return;
                }
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i] != null) {
                        deleteFolder(listFiles[i].getAbsolutePath());
                    }
                }
                file.delete();//全部子文件删除了删除自己
            }
        }

    }
}

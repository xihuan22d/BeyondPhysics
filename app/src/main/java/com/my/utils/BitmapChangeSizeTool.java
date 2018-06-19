package com.my.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

import com.beyondphysics.network.BitmapConfig;
import com.beyondphysics.network.utils.FileTool;

import java.io.File;


public class BitmapChangeSizeTool {

    public static String getResizeBitmapAndSaveWithJPG(String filePath, String lastFileName, String rootPath, int sizeX) {
        if (filePath == null || rootPath == null) {
            return null;
        }
        String theLastFileName = null;
        if (lastFileName != null) {
            theLastFileName = lastFileName;
        } else {
            theLastFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
            theLastFileName = theLastFileName.replace(".png", ".jpg");//如果不加.可能会有俩个png字符串存在的情况
        }
        Bitmap resizeBitmap = getResizeBitmap(filePath, sizeX);
        if (resizeBitmap == null) {
            return null;
        }
        return FileTool.saveBitmapWithJPG(rootPath, theLastFileName, resizeBitmap, 90);
    }

    public static Bitmap getResizeBitmap(String filePath, int sizeX) {
        if (filePath == null) {
            return null;
        }
        boolean isPNG = false;//如果是png加一个白底
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        if (fileType.toLowerCase().equals("png")) {
            isPNG = true;
        }
        Bitmap bitmap = FileTool.getCompressBitmapFromFile(filePath, sizeX, -1, ImageView.ScaleType.CENTER, new BitmapConfig(Bitmap.Config.RGB_565));
        if (bitmap == null) {
            return null;
        }
        float x = (float) sizeX / bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(x, x);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (isPNG) {
            Paint paint = new Paint();
            paint.setColor(0xffffffff);
            Bitmap theBitmap = Bitmap.createBitmap(resizeBitmap.getWidth(), resizeBitmap.getHeight(), resizeBitmap.getConfig());
            Canvas canvas = new Canvas(theBitmap);
            canvas.drawRect(0, 0, resizeBitmap.getWidth(), resizeBitmap.getHeight(), paint);
            canvas.drawBitmap(resizeBitmap, 0, 0, paint);
            resizeBitmap = theBitmap;
        }
        return resizeBitmap;
    }


}

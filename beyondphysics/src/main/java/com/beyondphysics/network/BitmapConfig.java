package com.beyondphysics.network;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.beyondphysics.network.utils.FileTool;

public class BitmapConfig {
    public static final int ROUNDEDTYPE_NONE = 0;
    public static final int ROUNDEDTYPE_CIRCLE = 1;
    public static final int ROUNDEDTYPE_CIRCLEBORDER = 2;
    public static final int ROUNDEDTYPE_CORNER = 3;
    /**
     * NotNull
     */
    private final Bitmap.Config config;
    private int roundedType = ROUNDEDTYPE_NONE;
    private float circleBorderWidth = 3.0f;
    private int circleBorderColor = 0xffffffff;
    private float cornerDegree = 6.0f;

    public BitmapConfig(Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.RGB_565;
        }
        this.config = config;
    }

    public BitmapConfig(Bitmap.Config config, int roundedType, float circleBorderWidth, int circleBorderColor, float cornerDegree) {
        if (config == null) {
            config = Bitmap.Config.RGB_565;
        }
        this.config = config;
        this.roundedType = roundedType;
        this.circleBorderWidth = circleBorderWidth;
        this.circleBorderColor = circleBorderColor;
        this.cornerDegree = cornerDegree;
    }

    /**
     * 继承该方法可以对bitmap进行适当处理再返回最新的图片数据
     */
    public Bitmap resizeBitmap(Bitmap bitmap, int reqWidth, int reqHeight, ImageView.ScaleType scaleType) {
        if (bitmap == null) {
            return null;
        }
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.CENTER;
        }
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            if (reqWidth > 0 && reqHeight > 0 && bitmap.getWidth() > 0 && bitmap.getHeight() > 0 && (reqWidth != bitmap.getWidth() || reqHeight != bitmap.getHeight())) {
                bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
            }
        }
        return convertBitmap(bitmap, reqWidth, reqHeight, scaleType);
    }

    public Bitmap convertBitmap(Bitmap bitmap, int reqWidth, int reqHeight, ImageView.ScaleType scaleType) {
        if (bitmap == null) {
            return null;
        }
        try {
            if (roundedType == ROUNDEDTYPE_CIRCLE) {
                bitmap = getCircleBitmap(bitmap, reqWidth, reqHeight, scaleType);
            } else if (roundedType == ROUNDEDTYPE_CIRCLEBORDER) {
                bitmap = getCircleBitmapWithBorder(bitmap, circleBorderWidth, circleBorderColor, reqWidth, reqHeight, scaleType);
            } else if (roundedType == ROUNDEDTYPE_CORNER) {
                bitmap = getCornerBitmap(bitmap, cornerDegree, reqWidth, reqHeight, scaleType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapConfig_convertBitmap:图片转换异常__", e, 1);
        }

        return bitmap;
    }


    private Bitmap getCircleBitmap(Bitmap bitmap, int reqWidth, int reqHeight, ImageView.ScaleType scaleType) {
        if (bitmap == null) {
            return null;
        }
        float width = 0.0f;
        if (reqWidth <= 0) {
            width = bitmap.getWidth();
        } else {
            width = reqWidth;
        }
        Bitmap newBitmap = bitmap;
        if (scaleType != ImageView.ScaleType.FIT_XY) {
            newBitmap = getCenterCropBitmap(bitmap, width, width);
        }
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = this.config;
        }
        Bitmap theBitmap = Bitmap.createBitmap((int) width, (int) width, config);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(theBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(newBitmap, 0, 0, paint);
        return theBitmap;
    }

    private Bitmap getCircleBitmapWithBorder(Bitmap bitmap, float borderWidth, int borderColor, int reqWidth, int reqHeight, ImageView.ScaleType scaleType) {
        if (bitmap == null) {
            return null;
        }
        float width = 0.0f;
        if (reqWidth <= 0) {
            width = bitmap.getWidth();
        } else {
            width = reqWidth;
        }
        float theWidth = width - borderWidth * 2;
        Bitmap newBitmap = bitmap;
        if (scaleType != ImageView.ScaleType.FIT_XY) {
            newBitmap = getCenterCropBitmap(bitmap, width, width);
        }
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = this.config;
        }
        Bitmap theBitmap = Bitmap.createBitmap((int) width, (int) width, config);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(theBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(width / 2, width / 2, theWidth / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(newBitmap, null, new Rect((int) (borderWidth), (int) (borderWidth), (int) (width - borderWidth), (int) (width - borderWidth)), paint);
        drawCircleBorder(canvas, width, width, width / 2, borderWidth, borderColor);
        return theBitmap;
    }

    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, float width, float height, float radius, float borderWidth, int borderColor) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        float theRadius = radius - borderWidth / 2;
        if (theRadius >= 2) {
            theRadius = theRadius - 1.0f;
        }
        canvas.drawCircle(width / 2, height / 2, theRadius, paint);//-borderWidth/2刚好边框内边界链接圆形图片边界能显示完整的圆形的图片(-borderWidth刚好外边界链接圆形图片边界),因为paint是中心向俩边画宽度的多减少1px是误差因素导致边界有可能少一像素
    }

    /**
     * 获得圆角图片
     */
    private Bitmap getCornerBitmap(Bitmap bitmap, float roundPx, int reqWidth, int reqHeight, ImageView.ScaleType scaleType) {
        if (bitmap == null) {
            return null;
        }
        float width = 0.0f;
        float height = 0.0f;
        if (reqWidth <= 0 || reqHeight <= 0) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        } else {
            width = reqWidth;
            height = reqHeight;
        }
        Bitmap newBitmap = bitmap;
        if (scaleType != ImageView.ScaleType.FIT_XY) {
            newBitmap = getCenterCropBitmap(bitmap, width, height);
        }
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = this.config;
        }
        Bitmap theBitmap = Bitmap.createBitmap((int) width, (int) height, config);
        Canvas canvas = new Canvas(theBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawARGB(0, 0, 0, 0);
        Rect rect = new Rect(0, 0, (int) width, (int) height);
        RectF rectF = new RectF(rect);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(newBitmap, null, rect, paint);
        return theBitmap;
    }

    private Bitmap getCenterCropBitmap(Bitmap bitmap, float newWidth, float newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleX = newWidth / width;
        float scaleY = newHeight / height;
        float scale = Math.max(scaleX, scaleY);
        float scaleWidth = scale * width;
        float scaleHeight = scale * height;
        float left = (newWidth - scaleWidth) / 2;
        float top = (newHeight - scaleHeight) / 2;
        Rect rect = new Rect((int) left, (int) top, (int) (left + scaleWidth), (int) (top + scaleHeight));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = this.config;
        }
        Bitmap theBitmap = Bitmap.createBitmap((int) newWidth, (int) newHeight, config);
        Canvas canvas = new Canvas(theBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return theBitmap;
    }


    public Bitmap.Config getConfig() {
        return config;
    }

    public int getRoundedType() {
        return roundedType;
    }

    public void setRoundedType(int roundedType) {
        this.roundedType = roundedType;
    }

    public float getCircleBorderWidth() {
        return circleBorderWidth;
    }

    public void setCircleBorderWidth(float circleBorderWidth) {
        this.circleBorderWidth = circleBorderWidth;
    }

    public int getCircleBorderColor() {
        return circleBorderColor;
    }

    public void setCircleBorderColor(int circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
    }

    public float getCornerDegree() {
        return cornerDegree;
    }

    public void setCornerDegree(float cornerDegree) {
        this.cornerDegree = cornerDegree;
    }


    @Override
    public String toString() {
        return "BitmapConfig{" +
                "config=" + config +
                ", roundedType=" + roundedType +
                ", circleBorderWidth=" + circleBorderWidth +
                ", circleBorderColor=" + circleBorderColor +
                ", cornerDegree=" + cornerDegree +
                '}';
    }

}

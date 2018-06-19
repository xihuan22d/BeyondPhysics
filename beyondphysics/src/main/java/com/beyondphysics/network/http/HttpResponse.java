package com.beyondphysics.network.http;

import com.beyondphysics.network.utils.SuperBitmap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/8/13.
 */

public class HttpResponse implements Serializable {
    /**
     * 初始状态
     */
    public static final int UNINIT = 0;
    /**
     * 请求成功
     */
    public static final int SUCCESS = 1;
    /**
     * 请求已取消
     */
    public static final int CANCEL = 2;
    /**
     * 异常
     */
    public static final int ERROR = 3;
    /**
     * 缓存丢失
     */
    public static final int LOST = 4;
    /**
     * 缓存路径错误
     */
    public static final int CACHE_PATH_ERROR = 5;

    /**
     * 断点下载,在响应后获得文件长度之后执行的取消
     */
    public static final int CANCEL_AFTER_OK = 6;

    public static final String CANCEL_TIPS = "The request has been canceled";

    public static final String ERROR_UNKNOWN = "Error_unKnown";
    public static final String ERROR_URL = "Error_url";
    public static final String ERROR_OUTOFMEMORY = "Error_outOfMemory";
    public static final String ERROR_URLTYPE = "Error_urlType";
    public static final String ERROR_MODELTYPE = "Error_modelType";
    public static final String ERROR_SOCKET = "Error_socket";
    public static final String ERROR_IO = "Error_IO";
    public static final String LOST_TIPS = "The cache has been lost";
    public static final String CACHE_PATH_ERROR_TIPS = "The cache path is error";

    public static final int BITMAP_TYPE_PNG = 0;

    public static final int BITMAP_TYPE_JPG = 1;

    public static final int BITMAP_TYPE_GIF = 2;

    public static final int BITMAP_GETFROM_UNGET = 0;

    public static final int BITMAP_GETFROM_MEMORY = 1;

    public static final int BITMAP_GETFROM_NETWORK = 2;

    public static final int BITMAP_GETFROM_FILE = 3;

    public static final int BITMAP_GETFROM_ASSETS = 4;

    public static final int BITMAP_GETFROM_RESOURCE = 5;

    private int status = UNINIT;
    private String contentType;
    private String contentEncoding;
    private int contentLength;
    private Map<String, List<String>> headerFields;

    private String result;

    private int bitmapGetFrom = BITMAP_GETFROM_UNGET;

    private SuperBitmap superBitmap;

    private String cachePath;

    private long cacheSize;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", contentType='" + contentType + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLength=" + contentLength +
                ", headerFields=" + headerFields +
                ", result='" + result + '\'' +
                ", bitmapGetFrom=" + bitmapGetFrom +
                ", superBitmap=" + superBitmap +
                ", cachePath='" + cachePath + '\'' +
                ", cacheSize=" + cacheSize +
                '}';
    }
}

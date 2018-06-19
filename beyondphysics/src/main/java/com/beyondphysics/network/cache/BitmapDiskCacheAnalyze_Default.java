package com.beyondphysics.network.cache;


import android.graphics.Bitmap;

import com.beyondphysics.network.BitmapRequest;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.http.DoRequestParams;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileNameLockManager;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperBitmap;
import com.beyondphysics.network.utils.UrlTool;
import com.beyondphysics.network.utils.gif.GifBitmap;

import java.util.List;


/**
 * Created by xihuan22 on 2017/9/7.
 */

public class BitmapDiskCacheAnalyze_Default implements BitmapDiskCacheAnalyze {

    @Override
    public HttpResponse doDiskCacheAnalyze(BitmapRequest<?> bitmapRequest, DoRequestParams doRequestParams) {
        HttpResponse httpResponse = new HttpResponse();
        if (bitmapRequest == null) {
            httpResponse.setStatus(HttpResponse.ERROR);
            httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:bitmapRequest参数为null", null, 1);
        } else {
            if (bitmapRequest.isCancelRequest()) {
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
            } else {
                String urlString = bitmapRequest.getUrlString();
                int type = UrlTool.getUrlType(urlString);
                String contentType = null;
                int lastIndex = urlString.lastIndexOf(".");
                if (lastIndex != -1) {
                    contentType = urlString.substring(lastIndex);
                }
                String fileType = "png";
                int bitmapType = HttpResponse.BITMAP_TYPE_PNG;
                if (contentType != null) {
                    String theContentType = contentType.toLowerCase();
                    if (theContentType.contains("png")) {
                        fileType = "png";
                        bitmapType = HttpResponse.BITMAP_TYPE_PNG;
                    } else if (theContentType.contains("jpg") || theContentType.contains("jpeg")) {
                        fileType = "jpg";
                        bitmapType = HttpResponse.BITMAP_TYPE_JPG;
                    } else if (theContentType.contains("gif")) {
                        fileType = "gif";
                        bitmapType = HttpResponse.BITMAP_TYPE_GIF;
                    }
                }
                boolean needDecodeGif = false;
                if (bitmapRequest.isDecodeGif() && fileType.equals("gif")) {
                    needDecodeGif = true;
                }
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS || type == UrlTool.URLTYPE_FILE) {
                    String fileName = null;
                    if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                        fileName = bitmapRequest.getCachePath();
                    } else if (type == UrlTool.URLTYPE_FILE) {
                        fileName = UrlTool.getFileNameFromUrl(urlString);
                    }
                    if (fileName != null) {
                        try {
                            FileNameLockManager.getInstance().getFileNameLock(fileName);
                            try {
                                if (!needDecodeGif) {
                                    Bitmap bitmap = FileTool.getCompressBitmapFromFile(fileName, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                                    if (bitmap == null) {
                                        httpResponse.setStatus(HttpResponse.LOST);
                                        httpResponse.setResult(HttpResponse.LOST_TIPS);
                                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:File资源解析失败", null, 1);
                                    } else {
                                        SuperBitmap superBitmap = new SuperBitmap();
                                        superBitmap.setDecodeGif(false);
                                        superBitmap.setBitmapType(bitmapType);
                                        superBitmap.setBitmap(bitmap);
                                        httpResponse.setStatus(HttpResponse.SUCCESS);
                                        httpResponse.setResult(BitmapRequest.BITMAPREADDISKCACHESUCCESS);
                                        httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_FILE);
                                        httpResponse.setSuperBitmap(superBitmap);
                                    }
                                } else {
                                    List<GifBitmap> gifBitmaps = FileTool.decodeGifFromFile(fileName, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                                    if (gifBitmaps == null) {
                                        httpResponse.setStatus(HttpResponse.LOST);
                                        httpResponse.setResult(HttpResponse.LOST_TIPS);
                                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:File资源解析失败", null, 1);
                                    } else {
                                        SuperBitmap superBitmap = new SuperBitmap();
                                        superBitmap.setDecodeGif(true);
                                        superBitmap.setBitmapType(bitmapType);
                                        superBitmap.setGifBitmaps(gifBitmaps);
                                        httpResponse.setStatus(HttpResponse.SUCCESS);
                                        httpResponse.setResult(BitmapRequest.BITMAPREADDISKCACHESUCCESS);
                                        httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_FILE);
                                        httpResponse.setSuperBitmap(superBitmap);
                                    }
                                }
                            } catch (OutOfMemoryError e) {
                                e.printStackTrace();
                                httpResponse.setStatus(HttpResponse.ERROR);
                                httpResponse.setResult(HttpResponse.ERROR_OUTOFMEMORY);
                                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:OutOfMemoryError", e, 1);
                            }
                        } finally {
                            FileNameLockManager.getInstance().removeFileNameLock(fileName);
                        }
                    } else {
                        httpResponse.setStatus(HttpResponse.CACHE_PATH_ERROR);
                        httpResponse.setResult(HttpResponse.CACHE_PATH_ERROR_TIPS);
                    }
                } else if (type == UrlTool.URLTYPE_ASSETS) {
                    String assetsName = UrlTool.getAssetsNameFromUrl(urlString);
                    if (assetsName != null) {
                        try {
                            if (!needDecodeGif) {
                                Bitmap bitmap = FileTool.getCompressBitmapFromAssets(bitmapRequest.getContext(), assetsName, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                                if (bitmap == null) {
                                    httpResponse.setStatus(HttpResponse.LOST);
                                    httpResponse.setResult(HttpResponse.LOST_TIPS);
                                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:Assets资源解析失败", null, 1);
                                } else {
                                    SuperBitmap superBitmap = new SuperBitmap();
                                    superBitmap.setDecodeGif(false);
                                    superBitmap.setBitmapType(bitmapType);
                                    superBitmap.setBitmap(bitmap);
                                    httpResponse.setStatus(HttpResponse.SUCCESS);
                                    httpResponse.setResult(BitmapRequest.BITMAPREADDISKCACHESUCCESS);
                                    httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_ASSETS);
                                    httpResponse.setSuperBitmap(superBitmap);
                                }
                            } else {
                                List<GifBitmap> gifBitmaps = FileTool.decodeGifFromAssets(bitmapRequest.getContext(), assetsName, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                                if (gifBitmaps == null) {
                                    httpResponse.setStatus(HttpResponse.LOST);
                                    httpResponse.setResult(HttpResponse.LOST_TIPS);
                                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:Assets资源解析失败", null, 1);
                                } else {
                                    SuperBitmap superBitmap = new SuperBitmap();
                                    superBitmap.setDecodeGif(true);
                                    superBitmap.setBitmapType(bitmapType);
                                    superBitmap.setGifBitmaps(gifBitmaps);
                                    httpResponse.setStatus(HttpResponse.SUCCESS);
                                    httpResponse.setResult(BitmapRequest.BITMAPREADDISKCACHESUCCESS);
                                    httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_ASSETS);
                                    httpResponse.setSuperBitmap(superBitmap);
                                }
                            }
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            httpResponse.setStatus(HttpResponse.ERROR);
                            httpResponse.setResult(HttpResponse.ERROR_OUTOFMEMORY);
                            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:OutOfMemoryError", e, 1);
                        }
                    } else {
                        httpResponse.setStatus(HttpResponse.CACHE_PATH_ERROR);
                        httpResponse.setResult(HttpResponse.CACHE_PATH_ERROR_TIPS);
                    }
                } else if (type == UrlTool.URLTYPE_RESOURCE) {
                    int resourceId = UrlTool.getResourceIdFromUrl(urlString);
                    try {
                        if (!needDecodeGif) {
                            Bitmap bitmap = FileTool.getCompressBitmapFromResource(bitmapRequest.getContext(), resourceId, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                            if (bitmap == null) {
                                httpResponse.setStatus(HttpResponse.LOST);
                                httpResponse.setResult(HttpResponse.LOST_TIPS);
                                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:Resource资源解析失败", null, 1);
                            } else {
                                SuperBitmap superBitmap = new SuperBitmap();
                                superBitmap.setDecodeGif(false);
                                superBitmap.setBitmapType(bitmapType);
                                superBitmap.setBitmap(bitmap);
                                httpResponse.setStatus(HttpResponse.SUCCESS);
                                httpResponse.setResult(BitmapRequest.BITMAPREADDISKCACHESUCCESS);
                                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_RESOURCE);
                                httpResponse.setSuperBitmap(superBitmap);
                            }
                        } else {
                            List<GifBitmap> gifBitmaps = FileTool.decodeGifFromResource(bitmapRequest.getContext(), resourceId, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                            if (gifBitmaps == null) {
                                httpResponse.setStatus(HttpResponse.LOST);
                                httpResponse.setResult(HttpResponse.LOST_TIPS);
                                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "BitmapDiskCacheAnalyze_Defalt_doDiskCacheAnalyze:Resource资源解析失败", null, 1);
                            } else {
                                SuperBitmap superBitmap = new SuperBitmap();
                                superBitmap.setDecodeGif(true);
                                superBitmap.setBitmapType(bitmapType);
                                superBitmap.setGifBitmaps(gifBitmaps);
                                httpResponse.setStatus(HttpResponse.SUCCESS);
                                httpResponse.setResult(BitmapRequest.BITMAPREADDISKCACHESUCCESS);
                                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_RESOURCE);
                                httpResponse.setSuperBitmap(superBitmap);
                            }
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        httpResponse.setStatus(HttpResponse.ERROR);
                        httpResponse.setResult(HttpResponse.ERROR_OUTOFMEMORY);
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:OutOfMemoryError", e, 1);
                    }
                } else {
                    httpResponse.setStatus(HttpResponse.ERROR);
                    httpResponse.setResult(HttpResponse.ERROR_URLTYPE);
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:type不存在", null, 1);
                }
            }
        }
        return httpResponse;
    }

}

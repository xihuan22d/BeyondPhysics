package com.beyondphysics.network.http;

import android.graphics.Bitmap;

import com.beyondphysics.network.BitmapRequest;
import com.beyondphysics.network.BreakpointDownloadRequest;
import com.beyondphysics.network.DownloadRequest;
import com.beyondphysics.network.Request;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.ResponseHandler;
import com.beyondphysics.network.UploadRequest;
import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.cache.ThreadSafelyLinkedHasMapCacheItem;
import com.beyondphysics.network.utils.FileNameLockManager;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.MD5Tool;
import com.beyondphysics.network.utils.SuperBitmap;
import com.beyondphysics.network.utils.gif.GifBitmap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;


/**
 * Created by xihuan22 on 2017/8/1.
 */

public class HttpAgreement_Default implements HttpAgreement {


    public HttpAgreement_Default() {

    }

    @Override
    public HttpResponse doRequest(Request<?> request, DoRequestParams doRequestParams) {
        HttpResponse httpResponse = new HttpResponse();
        if (request == null) {
            httpResponse.setStatus(HttpResponse.ERROR);
            httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:request参数为null", null, 1);
        } else {
            if (request.isCancelRequest()) {
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
            } else {
                String urlString = request.getUrlString();
                if (urlString == null) {
                    httpResponse.setStatus(HttpResponse.ERROR);
                    httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:urlString为null", null, 1);
                } else {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        URL url = new URL(urlString);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(false);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setConnectTimeout(request.getConnectTimeoutMs());
                        httpURLConnection.setReadTimeout(request.getReadTimeoutMs());
                        if (url.getProtocol() != null && url.getProtocol().equals("https")) {
                            SSLSocketFactory sslSocketFactory=request.getSslSocketFactory();
                            if ( sslSocketFactory != null){
                                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslSocketFactory);
                            }
                        }
                        Map<String, String> headerParams = request.getHeaderParams();
                        if (headerParams != null) {
                            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                                if (entry.getKey() != null && entry.getValue() != null) {
                                    httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                        switch (request.getRequestType()) {
                            case Request.GET:
                                httpURLConnection.setRequestMethod("GET");
                                break;
                            case Request.DELETE:
                                httpURLConnection.setRequestMethod("DELETE");
                                break;
                            case Request.POST:
                                httpURLConnection.setRequestMethod("POST");
                                addBody(httpURLConnection, request, headerParams);
                                break;
                            case Request.PUT:
                                httpURLConnection.setRequestMethod("PUT");
                                addBody(httpURLConnection, request, headerParams);
                                break;
                            case Request.HEAD:
                                httpURLConnection.setRequestMethod("HEAD");
                                break;
                            case Request.OPTIONS:
                                httpURLConnection.setRequestMethod("OPTIONS");
                                break;
                            case Request.TRACE:
                                httpURLConnection.setRequestMethod("TRACE");
                                break;
                            case Request.PATCH:
                                httpURLConnection.setRequestMethod("PATCH");
                                addBody(httpURLConnection, request, headerParams);
                                break;
                            default:
                                httpURLConnection.setRequestMethod("GET");
                                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:requestType类型不存在", null, 1);
                                break;
                        }
                        if (request.getModelType() == Request.BREAKPOINT_DOWNLOAD_REQUEST) {
                            breakpointDownloadRequest(request, httpResponse, httpURLConnection, doRequestParams);
                        } else if (request.getModelType() == Request.UPLOAD_REQUEST) {
                            uploadRequest(request, httpResponse, httpURLConnection, doRequestParams);
                        } else {
                            if (httpURLConnection.getResponseCode() == HttpStatus.SC_OK) {
                                String contentType = httpURLConnection.getContentType();
                                String contentEncoding = httpURLConnection.getContentEncoding();
                                int contentLength = httpURLConnection.getContentLength();
                                Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                                InputStream inputStream = null;
                                try {
                                    inputStream = httpURLConnection.getInputStream();
                                    if (request.getModelType() == Request.NORMAL_REQUEST) {
                                        normalRequest(request, httpResponse, inputStream, contentType, contentEncoding, contentLength, headerFields);
                                    } else if (request.getModelType() == Request.BITMAP_REQUEST) {
                                        bitmapRequest(request, httpResponse, inputStream, contentType, contentEncoding, contentLength, headerFields, doRequestParams);
                                    } else if (request.getModelType() == Request.DOWNLOAD_REQUEST) {
                                        downloadRequest(request, httpResponse, inputStream, contentType, contentEncoding, contentLength, headerFields, doRequestParams);
                                    } else {
                                        httpResponse.setStatus(HttpResponse.ERROR);
                                        httpResponse.setResult(HttpResponse.ERROR_MODELTYPE);
                                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:modelType不存在", null, 1);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    httpResponse.setStatus(HttpResponse.ERROR);
                                    httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
                                } finally {
                                    try {
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                httpResponse.setStatus(HttpResponse.ERROR);
                                httpResponse.setResult(HttpResponse.ERROR_SOCKET);
                                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:请求不成功__" + urlString, null, 1);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        httpResponse.setStatus(HttpResponse.ERROR);
                        httpResponse.setResult(HttpResponse.ERROR_URL);
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_doRequest:url格式不正确__" + urlString, e, 1);
                    } finally {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                }
            }
        }

        return httpResponse;
    }

    private void addBody(HttpURLConnection httpURLConnection, Request<?> request, Map<String, String> headerParams) {
        if (httpURLConnection == null || request == null) {
            return;
        }
        httpURLConnection.setDoOutput(true);
        if (headerParams != null) {
            if (!headerParams.containsKey("Connection")) {
                httpURLConnection.setRequestProperty("Connection", "keep-alive");
            }
            if (!headerParams.containsKey("Accept-Charset")) {
                httpURLConnection.setRequestProperty("Accept-Charset", request.getEncoding());
            }
            if (!headerParams.containsKey("Content-Type")) {
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + request.getEncoding());
            }
        } else {
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("Accept-Charset", request.getEncoding());
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + request.getEncoding());
        }

        byte[] bytes = request.getBodyParamsWithBytes();
        if (bytes != null) {
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.write(bytes);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void normalRequest(Request<?> request, HttpResponse httpResponse, InputStream inputStream, String contentType, String contentEncoding, int contentLength, Map<String, List<String>> headerFields) {
        if (request == null || httpResponse == null || inputStream == null) {
            if (httpResponse != null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            return;
        }
        if (contentEncoding == null) {
            contentEncoding = "utf-8";
        }
        boolean cancelRequest = false;
        boolean success = false;
        String result = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, contentEncoding));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (request.isCancelRequest()) {
                    cancelRequest = true;
                    break;
                }
                stringBuffer.append(line);
            }
            if (!cancelRequest) {
                result = stringBuffer.toString();
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cancelRequest) {
            httpResponse.setStatus(HttpResponse.CANCEL);
            httpResponse.setResult(HttpResponse.CANCEL_TIPS);
        } else {
            if (success) {
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setContentType(contentType);
                httpResponse.setContentEncoding(contentEncoding);
                httpResponse.setContentLength(contentLength);
                httpResponse.setHeaderFields(headerFields);
                httpResponse.setResult(result);
            } else {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_IO);
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_normalRequest:IO异常", null, 1);
            }
        }
    }


    private void bitmapRequest(Request<?> request, HttpResponse httpResponse, InputStream inputStream, String contentType, String contentEncoding, int contentLength, Map<String, List<String>> headerFields, DoRequestParams doRequestParams) {
        if (request == null || httpResponse == null || inputStream == null) {
            if (httpResponse != null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            return;
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
        ResponseHandler mainResponseHandler = null;
        String fileCacheFolder = null;
        if (doRequestParams != null) {
            mainResponseHandler = doRequestParams.getMainResponseHandler();
            if (doRequestParams.getThreadSafelyLinkedHasMapCacheItem() != null) {
                fileCacheFolder = doRequestParams.getThreadSafelyLinkedHasMapCacheItem().getFileCache_imageFolder();
            }
        }
        String cachePath = MD5Tool.getCachePath(request.getUrlString(), request.getRequestType(), fileCacheFolder, fileType);
        if (cachePath != null && request instanceof BitmapRequest<?>) {
            BitmapRequest<?> bitmapRequest = (BitmapRequest<?>) request;
            boolean cancelRequest = false;
            boolean success = false;
            try {
                FileNameLockManager.getInstance().getFileNameLock(cachePath);
                if (bitmapRequest.isReceiveProgress() && mainResponseHandler != null) {
                    mainResponseHandler.sendBitmapRequestProgressMessage(bitmapRequest, 1, 0, contentLength);
                }
                FileOutputStream fileOutputStream = null;
                try {
                    File file = new File(cachePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    int downloadSize = 0;
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = inputStream.read(bytes)) != -1) {
                        if (bitmapRequest.isCancelRequest()) {
                            cancelRequest = true;
                            break;
                        }
                        fileOutputStream.write(bytes, 0, length);
                        downloadSize = downloadSize + length;
                        if (bitmapRequest.isReceiveProgress() && mainResponseHandler != null) {
                            mainResponseHandler.sendBitmapRequestProgressMessage(bitmapRequest, 2, downloadSize, contentLength);
                        }
                    }
                    fileOutputStream.flush();
                    if (!cancelRequest) {
                        success = true;
                    }
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
                if (cancelRequest) {
                    FileTool.deleteFile(cachePath);
                    httpResponse.setStatus(HttpResponse.CANCEL);
                    httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                } else {
                    if (success) {
                        try {
                            boolean needDecodeGif = false;
                            if (bitmapRequest.isDecodeGif() && fileType.equals("gif")) {
                                needDecodeGif = true;
                            }
                            if (!needDecodeGif) {
                                Bitmap bitmap = FileTool.getCompressBitmapFromFile(cachePath, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                                if (bitmap == null) {
                                    httpResponse.setStatus(HttpResponse.LOST);
                                    httpResponse.setResult(HttpResponse.LOST_TIPS);
                                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_bitmapRequest:File资源解析失败", null, 1);
                                } else {
                                    SuperBitmap superBitmap = new SuperBitmap();
                                    superBitmap.setDecodeGif(false);
                                    superBitmap.setBitmapType(bitmapType);
                                    superBitmap.setBitmap(bitmap);
                                    httpResponse.setStatus(HttpResponse.SUCCESS);
                                    httpResponse.setContentType(contentType);
                                    httpResponse.setContentEncoding(contentEncoding);
                                    httpResponse.setContentLength(contentLength);
                                    httpResponse.setHeaderFields(headerFields);
                                    httpResponse.setResult(BitmapRequest.BITMAPDOWNLOADSUCCESS);
                                    httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_NETWORK);
                                    httpResponse.setSuperBitmap(superBitmap);
                                    httpResponse.setCachePath(cachePath);
                                    httpResponse.setCacheSize(FileTool.getFileLength(cachePath));
                                }
                            } else {
                                List<GifBitmap> gifBitmaps = FileTool.decodeGifFromFile(cachePath, bitmapRequest.getWidth(), bitmapRequest.getHeight(), bitmapRequest.getScaleType(), bitmapRequest.getBitmapConfig());
                                if (gifBitmaps == null) {
                                    httpResponse.setStatus(HttpResponse.LOST);
                                    httpResponse.setResult(HttpResponse.LOST_TIPS);
                                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_bitmapRequest:File资源解析失败", null, 1);
                                } else {
                                    SuperBitmap superBitmap = new SuperBitmap();
                                    superBitmap.setDecodeGif(true);
                                    superBitmap.setBitmapType(bitmapType);
                                    superBitmap.setGifBitmaps(gifBitmaps);
                                    httpResponse.setStatus(HttpResponse.SUCCESS);
                                    httpResponse.setContentType(contentType);
                                    httpResponse.setContentEncoding(contentEncoding);
                                    httpResponse.setContentLength(contentLength);
                                    httpResponse.setHeaderFields(headerFields);
                                    httpResponse.setResult(BitmapRequest.BITMAPDOWNLOADSUCCESS);
                                    httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_NETWORK);
                                    httpResponse.setSuperBitmap(superBitmap);
                                    httpResponse.setCachePath(cachePath);
                                    httpResponse.setCacheSize(FileTool.getFileLength(cachePath));
                                }
                            }
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            httpResponse.setStatus(HttpResponse.ERROR);
                            httpResponse.setResult(HttpResponse.ERROR_OUTOFMEMORY);
                            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_bitmapRequest:OutOfMemoryError", e, 1);
                        }
                        if (!bitmapRequest.isCacheInDisk()) {
                            FileTool.deleteFile(cachePath);
                        }
                    } else {
                        FileTool.deleteFile(cachePath);
                        httpResponse.setStatus(HttpResponse.ERROR);
                        httpResponse.setResult(HttpResponse.ERROR_IO);
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_bitmapRequest:IO异常", null, 1);
                    }
                }
            } finally {
                FileNameLockManager.getInstance().removeFileNameLock(cachePath);
            }
        } else {
            httpResponse.setStatus(HttpResponse.ERROR);
            httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_bitmapRequest:cachePath为null或类型不存在", null, 1);
        }
    }


    private void downloadRequest(Request<?> request, HttpResponse httpResponse, InputStream inputStream, String contentType, String contentEncoding, int contentLength, Map<String, List<String>> headerFields, DoRequestParams doRequestParams) {
        if (request == null || httpResponse == null || inputStream == null) {
            if (httpResponse != null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            return;
        }
        if (request instanceof DownloadRequest<?>) {
            DownloadRequest<?> downloadRequest = (DownloadRequest<?>) request;
            if (downloadRequest.getSavePath() == null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_downloadRequest:savePath为null", null, 1);
                return;
            }
            String savePath = downloadRequest.getSavePath();
            ResponseHandler mainResponseHandler = null;
            if (doRequestParams != null) {
                mainResponseHandler = doRequestParams.getMainResponseHandler();
            }
            boolean cancelRequest = false;
            boolean success = false;
            try {
                FileNameLockManager.getInstance().getFileNameLock(savePath);
                if (downloadRequest.isReceiveProgress() && mainResponseHandler != null) {
                    mainResponseHandler.sendDownloadRequestProgressMessage(downloadRequest, 1, 0, contentLength);
                }
                FileOutputStream fileOutputStream = null;
                try {
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    int downloadSize = 0;
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = inputStream.read(bytes)) != -1) {
                        if (downloadRequest.isCancelRequest()) {
                            cancelRequest = true;
                            break;
                        }
                        fileOutputStream.write(bytes, 0, length);
                        downloadSize = downloadSize + length;
                        if (downloadRequest.isReceiveProgress() && mainResponseHandler != null) {
                            mainResponseHandler.sendDownloadRequestProgressMessage(downloadRequest, 2, downloadSize, contentLength);
                        }
                    }
                    fileOutputStream.flush();
                    if (!cancelRequest) {
                        success = true;
                    }
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
                if (cancelRequest) {
                    FileTool.deleteFile(savePath);
                    httpResponse.setStatus(HttpResponse.CANCEL);
                    httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                } else {
                    if (success) {
                        httpResponse.setStatus(HttpResponse.SUCCESS);
                        httpResponse.setContentType(contentType);
                        httpResponse.setContentEncoding(contentEncoding);
                        httpResponse.setContentLength(contentLength);
                        httpResponse.setHeaderFields(headerFields);
                        httpResponse.setResult(DownloadRequest.DOWNLOADSUCCESS);
                    } else {
                        FileTool.deleteFile(savePath);
                        httpResponse.setStatus(HttpResponse.ERROR);
                        httpResponse.setResult(HttpResponse.ERROR_IO);
                        FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_downloadRequest:IO异常", null, 1);
                    }
                }
            } finally {
                FileNameLockManager.getInstance().removeFileNameLock(savePath);
            }
        } else {
            httpResponse.setStatus(HttpResponse.ERROR);
            httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_downloadRequest:类型不存在", null, 1);
        }
    }

    private void breakpointDownloadRequest(Request<?> request, HttpResponse httpResponse, HttpURLConnection httpURLConnection, DoRequestParams doRequestParams) {
        if (request == null || httpResponse == null || httpURLConnection == null) {
            if (httpResponse != null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            return;
        }
        if (request instanceof BreakpointDownloadRequest<?>) {
            BreakpointDownloadRequest<?> breakpointDownloadRequest = (BreakpointDownloadRequest<?>) request;
            if (breakpointDownloadRequest.getSavePath() == null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_breakpointDownloadRequest:savePath为null", null, 1);
                return;
            }
            String savePath = breakpointDownloadRequest.getSavePath();
            ResponseHandler mainResponseHandler = null;
            ThreadSafelyLinkedHasMapCacheItem threadSafelyLinkedHasMapCacheItem = null;
            if (doRequestParams != null) {
                mainResponseHandler = doRequestParams.getMainResponseHandler();
                threadSafelyLinkedHasMapCacheItem = doRequestParams.getThreadSafelyLinkedHasMapCacheItem();
            }
            int contentLengthAll = 0;
            if (threadSafelyLinkedHasMapCacheItem != null) {
                CacheItem cacheItem = threadSafelyLinkedHasMapCacheItem.getCacheItemByKey(request.getSuperKey().getKey(), RequestManager.BREAKPOINTDOWNLOADREQUEST_TAG);
                if (cacheItem != null) {
                    contentLengthAll = cacheItem.getContentLength();
                }
            }
            boolean cancelRequest = false;
            boolean success = false;
            try {
                FileNameLockManager.getInstance().getFileNameLock(savePath);
                int beginAddress = 0;
                if (breakpointDownloadRequest.getBeginAddress() >= 0) {
                    beginAddress = breakpointDownloadRequest.getBeginAddress();
                } else {
                    File file = new File(savePath);
                    if (file.exists() && file.isFile()) {
                        beginAddress = (int) file.length();
                    }
                }
                boolean haveDownloadOK = false;
                boolean openRange = false;

                if (contentLengthAll > 0 && beginAddress > 0) {
                    if (beginAddress == contentLengthAll) {
                        haveDownloadOK = true;
                    } else {
                        httpURLConnection.setRequestProperty("Range", "bytes=" + beginAddress + "-" + (contentLengthAll - 1));
                        openRange = true;
                    }
                }
                if (haveDownloadOK) {
                    httpResponse.setStatus(HttpResponse.SUCCESS);
                    httpResponse.setContentLength(contentLengthAll);
                    httpResponse.setResult(BreakpointDownloadRequest.BREAKPOINTDOWNLOADSUCCESS_EXISTED);
                } else {
                    String contentType = null;
                    String contentEncoding = null;
                    int contentLength = 0;
                    Map<String, List<String>> headerFields = null;

                    InputStream inputStream = null;
                    FileOutputStream fileOutputStream = null;
                    RandomAccessFile randomAccessFile = null;
                    try {
                        File file = new File(savePath);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        int responseCode = httpURLConnection.getResponseCode();
                        if (responseCode == HttpStatus.SC_OK || (openRange && responseCode == HttpStatus.SC_PARTIAL_CONTENT)) {
                            contentType = httpURLConnection.getContentType();
                            contentEncoding = httpURLConnection.getContentEncoding();
                            contentLength = httpURLConnection.getContentLength();
                            headerFields = httpURLConnection.getHeaderFields();
                            inputStream = httpURLConnection.getInputStream();
                            if (!openRange) {
                                if (breakpointDownloadRequest.isReceiveProgress() && mainResponseHandler != null) {
                                    mainResponseHandler.sendBreakpointDownloadRequestProgressMessage(breakpointDownloadRequest, 1, 0, contentLength);
                                }
                                fileOutputStream = new FileOutputStream(file);
                                int downloadSize = 0;
                                byte[] bytes = new byte[1024];
                                int length = -1;
                                while ((length = inputStream.read(bytes)) != -1) {
                                    if (breakpointDownloadRequest.isCancelRequest()) {
                                        cancelRequest = true;
                                        break;
                                    }
                                    fileOutputStream.write(bytes, 0, length);
                                    downloadSize = downloadSize + length;
                                    if (breakpointDownloadRequest.isReceiveProgress() && mainResponseHandler != null) {
                                        mainResponseHandler.sendBreakpointDownloadRequestProgressMessage(breakpointDownloadRequest, 2, downloadSize, contentLength);
                                    }
                                }
                                fileOutputStream.flush();
                                if (!cancelRequest) {
                                    success = true;
                                }
                            } else {
                                if (breakpointDownloadRequest.isReceiveProgress() && mainResponseHandler != null) {
                                    mainResponseHandler.sendBreakpointDownloadRequestProgressMessage(breakpointDownloadRequest, 1, beginAddress, contentLengthAll);
                                }
                                randomAccessFile = new RandomAccessFile(file, "rw");
                                int downloadSize = beginAddress;
                                byte[] bytes = new byte[1024];
                                int length = -1;
                                while ((length = inputStream.read(bytes)) != -1) {
                                    if (breakpointDownloadRequest.isCancelRequest()) {
                                        cancelRequest = true;
                                        break;
                                    }
                                    randomAccessFile.seek(downloadSize);
                                    randomAccessFile.write(bytes, 0, length);
                                    downloadSize = downloadSize + length;
                                    if (breakpointDownloadRequest.isReceiveProgress() && mainResponseHandler != null) {
                                        mainResponseHandler.sendBreakpointDownloadRequestProgressMessage(breakpointDownloadRequest, 2, downloadSize, contentLengthAll);
                                    }
                                }
                                if (!cancelRequest) {
                                    success = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (cancelRequest) {
                        httpResponse.setStatus(HttpResponse.CANCEL_AFTER_OK);//断点下载比较特殊,取消了也要返回上次的文件
                        httpResponse.setContentType(contentType);
                        httpResponse.setContentEncoding(contentEncoding);
                        if (contentLengthAll != 0) {
                            httpResponse.setContentLength(contentLengthAll);
                        } else {
                            httpResponse.setContentLength(contentLength);
                        }
                        httpResponse.setHeaderFields(headerFields);
                        httpResponse.setResult(HttpResponse.CANCEL_TIPS);
                    } else {
                        if (success) {
                            httpResponse.setStatus(HttpResponse.SUCCESS);
                            httpResponse.setContentType(contentType);
                            httpResponse.setContentEncoding(contentEncoding);
                            if (contentLengthAll != 0) {
                                httpResponse.setContentLength(contentLengthAll);
                            } else {
                                httpResponse.setContentLength(contentLength);
                            }
                            httpResponse.setHeaderFields(headerFields);
                            httpResponse.setResult(BreakpointDownloadRequest.BREAKPOINTDOWNLOADSUCCESS);
                        } else {
                            httpResponse.setStatus(HttpResponse.ERROR);
                            httpResponse.setResult(HttpResponse.ERROR_IO);
                            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_breakpointDownloadRequest:IO异常", null, 1);
                        }
                    }
                }
            } finally {
                FileNameLockManager.getInstance().removeFileNameLock(savePath);
            }
        } else {
            httpResponse.setStatus(HttpResponse.ERROR);
            httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_breakpointDownloadRequest:类型不存在", null, 1);
        }
    }

    private void uploadRequest(Request<?> request, HttpResponse httpResponse, HttpURLConnection httpURLConnection, DoRequestParams doRequestParams) {
        if (request == null || httpResponse == null || httpURLConnection == null) {
            if (httpResponse != null) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            }
            return;
        }
        if (request instanceof UploadRequest<?>) {
            UploadRequest<?> uploadRequest = (UploadRequest<?>) request;
            String[] names = uploadRequest.getNames();
            String[] values = uploadRequest.getValues();
            String[] fileNames = uploadRequest.getFileNames();
            String[] filePaths = uploadRequest.getFilePaths();
            if ((names == null || values == null || names.length != values.length) && (fileNames == null || filePaths == null || fileNames.length != filePaths.length)) {
                httpResponse.setStatus(HttpResponse.ERROR);
                httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
                FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_uploadRequest:重要参数为null或数量不匹配", null, 1);
                return;
            }
            long totalSize = 0;
            if (fileNames != null && filePaths != null) {
                for (int i = 0; i < filePaths.length; i++) {
                    long length = FileTool.getFileLength(filePaths[i]);
                    if (length != -1) {
                        totalSize = totalSize + length;
                    }
                }
            }
            ResponseHandler mainResponseHandler = null;
            if (doRequestParams != null) {
                mainResponseHandler = doRequestParams.getMainResponseHandler();
            }
            if (uploadRequest.isReceiveProgress() && mainResponseHandler != null) {
                mainResponseHandler.sendUploadRequestProgressMessage(uploadRequest, 1, 0, totalSize);
            }
            boolean cancelRequest = false;
            boolean success = false;
            String result = null;
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            String contentType = null;
            String contentEncoding = null;
            int contentLength = 0;
            Map<String, List<String>> headerFields = null;

            DataOutputStream dataOutputStream = null;
            FileInputStream fileInputStream = null;
            InputStream inputStream = null;
            try {
                httpURLConnection.setChunkedStreamingMode(0);//防止把数据缓存到内存导致大文件上传时内存溢出
                dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                if (names != null && values != null) {
                    for (int i = 0; i < names.length; i++) {
                        String name = names[i];
                        if (name == null) {
                            name = "";
                        }
                        String value = values[i];
                        if (value == null) {
                            value = "";
                        }
                        dataOutputStream.writeBytes(twoHyphens + boundary + end);
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name
                                + "\"" + end);
                        dataOutputStream.writeBytes(end);
                        byte[] bytes = value.getBytes(uploadRequest.getEncoding());
                        dataOutputStream.write(bytes);
                        dataOutputStream.writeBytes(end);
                    }
                }
                long uploadSize = 0;
                if (fileNames != null && filePaths != null) {
                    for (int i = 0; i < fileNames.length; i++) {
                        String fileName = fileNames[i];
                        if (fileName == null) {
                            fileName = "";
                        }
                        String filePath = filePaths[i];
                        if (filePath == null) {
                            filePath = "";
                        }
                        dataOutputStream.writeBytes(twoHyphens + boundary + end);
                        String value = "Content-Disposition: form-data; "
                                + "name=\"" + fileName + "\";filename=\"" + filePath + "\"" + end;
                        byte[] bytes = value.getBytes(uploadRequest.getEncoding());
                        dataOutputStream.write(bytes);
                        dataOutputStream.writeBytes(end);
                        fileInputStream = new FileInputStream(filePath);
                        byte[] bytes1 = new byte[1024];
                        int length = -1;
                        while ((length = fileInputStream.read(bytes1)) != -1) {
                            if (uploadRequest.isCancelRequest()) {
                                cancelRequest = true;
                                break;
                            }
                            dataOutputStream.write(bytes1, 0, length);
                            uploadSize = uploadSize + length;
                            if (uploadRequest.isReceiveProgress() && mainResponseHandler != null) {
                                mainResponseHandler.sendUploadRequestProgressMessage(uploadRequest, 2, uploadSize, totalSize);
                            }
                        }
                        dataOutputStream.writeBytes(end);
                        if (cancelRequest) {
                            break;
                        }
                    }
                }
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + end);
                dataOutputStream.flush();
                if (!cancelRequest && httpURLConnection.getResponseCode() == HttpStatus.SC_OK) {
                    contentType = httpURLConnection.getContentType();
                    contentEncoding = httpURLConnection.getContentEncoding();
                    contentLength = httpURLConnection.getContentLength();
                    headerFields = httpURLConnection.getHeaderFields();
                    inputStream = httpURLConnection.getInputStream();
                    if (contentEncoding == null) {
                        contentEncoding = "utf-8";
                    }
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, contentEncoding));
                        StringBuffer stringBuffer = new StringBuffer();
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            if (request.isCancelRequest()) {
                                cancelRequest = true;
                                break;
                            }
                            stringBuffer.append(line);
                        }
                        if (!cancelRequest) {
                            result = stringBuffer.toString();
                            success = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {//谁先结束先关谁
                        fileInputStream.close();
                    }
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (cancelRequest) {
                httpResponse.setStatus(HttpResponse.CANCEL);
                httpResponse.setResult(HttpResponse.CANCEL_TIPS);
            } else {
                if (success) {
                    httpResponse.setStatus(HttpResponse.SUCCESS);
                    httpResponse.setContentType(contentType);
                    httpResponse.setContentEncoding(contentEncoding);
                    httpResponse.setContentLength(contentLength);
                    httpResponse.setHeaderFields(headerFields);
                    httpResponse.setResult(result);
                } else {
                    httpResponse.setStatus(HttpResponse.ERROR);
                    httpResponse.setResult(HttpResponse.ERROR_IO);
                    FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_uploadRequest:IO异常", null, 1);
                }
            }
        } else {
            httpResponse.setStatus(HttpResponse.ERROR);
            httpResponse.setResult(HttpResponse.ERROR_UNKNOWN);
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "HttpAgreement_Defalt_uploadRequest:类型不存在", null, 1);
        }

    }
}

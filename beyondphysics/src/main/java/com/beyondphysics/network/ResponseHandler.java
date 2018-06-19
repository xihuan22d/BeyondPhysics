package com.beyondphysics.network;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;

import java.util.LinkedHashMap;

/**
 * Created by xihuan22 on 2017/9/5.
 */
public class ResponseHandler extends Handler {
    public static final int RESPONSEHANDLER = 1;
    public static final int RESPONSEHANDLERWITHMESSAGERECORD = 2;

    public final String REQUEST = "request";
    public final String HTTPRESPONSE = "httpResponse";
    public final String CACHEITEMREQUEST = "cacheItemRequest";


    public final String TYPE = "type";
    public final String CURRENTSIZE = "currentSize";
    public final String CURRENTLONGSIZE = "currentLongSize";
    public final String TOTALSIZE = "totalSize";
    public final String TOTALLONGSIZE = "totalLongSize";


    public ResponseHandler() {
        super();
    }

    public ResponseHandler(Callback callback) {
        super(callback);
    }

    public ResponseHandler(Looper looper) {
        super(looper);
    }

    public ResponseHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    @Override
    public void handleMessage(Message message) {
        doHandleMessage(message);
        super.handleMessage(message);
    }

    protected <T> void doHandleMessage(Message message) {
        MessageObject messageObject = (MessageObject) message.obj;
        switch (message.what) {
            case 0:
                if (messageObject != null) {
                    Request<T> request = (Request<T>) messageObject.getObject(REQUEST);
                    HttpResponse httpResponse = (HttpResponse) messageObject.getObject(HTTPRESPONSE);
                    Request.OnResponseListener<T> onResponseListener = request.getOnResponseListener();
                    if (onResponseListener != null) {//主线程中是可以移除回调的
                        if (!request.isCancelRequest()) {//在主线程中执行cancelRequest,responseHandler和取消请求的线程处在同一个线程,保证一但在主线程取消了请求就不会收到成功的回调
                            if (httpResponse.getStatus() == HttpResponse.SUCCESS) {
                                onResponseListener.onSuccessResponse(request.convertResponseType(request, httpResponse));
                            } else {
                                onResponseListener.onErrorResponse(httpResponse.getResult());
                            }
                        } else {
                            if (request.isReceiveCancel()) {
                                onResponseListener.onErrorResponse(HttpResponse.CANCEL_TIPS);
                            }
                        }
                        Request.removeListener(request, true);
                    }
                }
                break;
            case 1:
                if (messageObject != null) {
                    BitmapRequest<?> bitmapRequest = (BitmapRequest<?>) messageObject.getObject(REQUEST);
                    int type = (int) messageObject.getObject(TYPE);
                    int currentSize = (int) messageObject.getObject(CURRENTSIZE);
                    int totalSize = (int) messageObject.getObject(TOTALSIZE);
                    if (totalSize <= 0) {
                        totalSize = 1;
                    }
                    BitmapRequest.OnDownloadProgressListener onDownloadProgressListener = bitmapRequest.getOnDownloadProgressListener();
                    if (onDownloadProgressListener != null) {
                        if (!bitmapRequest.isCancelRequest()) {
                            if (type == 1) {
                                onDownloadProgressListener.maxProgress(bitmapRequest, totalSize);
                            } else {
                                onDownloadProgressListener.updateProgress(bitmapRequest, currentSize, totalSize);
                            }
                        }
                    }
                }
                break;
            case 2:
                if (messageObject != null) {
                    DownloadRequest<?> downloadRequest = (DownloadRequest<?>) messageObject.getObject(REQUEST);
                    int type = (int) messageObject.getObject(TYPE);
                    int currentSize = (int) messageObject.getObject(CURRENTSIZE);
                    int totalSize = (int) messageObject.getObject(TOTALSIZE);
                    if (totalSize <= 0) {
                        totalSize = 1;
                    }
                    DownloadRequest.OnDownloadProgressListener onDownloadProgressListener = downloadRequest.getOnDownloadProgressListener();
                    if (onDownloadProgressListener != null) {
                        if (!downloadRequest.isCancelRequest()) {
                            if (type == 1) {
                                onDownloadProgressListener.maxProgress(downloadRequest, totalSize);
                            } else {
                                onDownloadProgressListener.updateProgress(downloadRequest, currentSize, totalSize);
                            }
                        }
                    }
                }
                break;
            case 3:
                if (messageObject != null) {
                    BreakpointDownloadRequest<?> breakpointDownloadRequest = (BreakpointDownloadRequest<?>) messageObject.getObject(REQUEST);
                    int type = (int) messageObject.getObject(TYPE);
                    int currentSize = (int) messageObject.getObject(CURRENTSIZE);
                    int totalSize = (int) messageObject.getObject(TOTALSIZE);
                    if (totalSize <= 0) {
                        totalSize = 1;
                    }
                    BreakpointDownloadRequest.OnDownloadProgressListener onDownloadProgressListener = breakpointDownloadRequest.getOnDownloadProgressListener();
                    if (onDownloadProgressListener != null) {
                        if (!breakpointDownloadRequest.isCancelRequest()) {
                            if (type == 1) {
                                onDownloadProgressListener.maxProgress(breakpointDownloadRequest, totalSize);
                            } else {
                                onDownloadProgressListener.updateProgress(breakpointDownloadRequest, currentSize, totalSize);
                            }
                        }
                    }
                }
                break;
            case 4:
                if (messageObject != null) {
                    UploadRequest<?> uploadRequest = (UploadRequest<?>) messageObject.getObject(REQUEST);
                    int type = (int) messageObject.getObject(TYPE);
                    long currentSize = (long) messageObject.getObject(CURRENTLONGSIZE);
                    long totalSize = (long) messageObject.getObject(TOTALLONGSIZE);
                    if (totalSize <= 0) {
                        totalSize = 1;
                    }
                    UploadRequest.OnUploadProgressListener onUploadProgressListener = uploadRequest.getOnUploadProgressListener();
                    if (onUploadProgressListener != null) {
                        if (!uploadRequest.isCancelRequest()) {
                            if (type == 1) {
                                onUploadProgressListener.maxProgress(uploadRequest, totalSize);
                            } else {
                                onUploadProgressListener.updateProgress(uploadRequest, currentSize, totalSize);
                            }
                        }
                    }
                }
                break;
            case 5:
                if (messageObject != null) {
                    CacheItemRequest cacheItemRequest = (CacheItemRequest) messageObject.getObject(CACHEITEMREQUEST);
                    CacheItemRequest.OnCacheItemListener onCacheItemListener = cacheItemRequest.getOnCacheItemListener();
                    if (onCacheItemListener != null) {
                        if (!cacheItemRequest.isCancelRequest()) {
                            onCacheItemListener.onSuccessResponse(cacheItemRequest.getCacheItems());
                        } else {
                            if (cacheItemRequest.isReceiveCancel()) {
                                onCacheItemListener.onErrorResponse(HttpResponse.CANCEL_TIPS);
                            }
                        }
                        CacheItemRequest.removeCacheItemListener(cacheItemRequest, true);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 该消息会在主线程执行完毕后移除所有的Listener
     */
    public void sendResponseMessage(Request<?> request, HttpResponse httpResponse) {
        if (request == null || httpResponse == null) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ResponseHandler_sendResponseMessage:request或者httpResponse为null", null, 1);
            return;
        }
        Message message = new Message();
        MessageObject messageObject = new MessageObject(0);
        messageObject.putObject(REQUEST, request);
        messageObject.putObject(HTTPRESPONSE, httpResponse);
        message.what = messageObject.getWhat();
        message.obj = messageObject;
        doSendMessage(message, request.getSuperKey(), messageObject);
    }

    public void sendBitmapRequestProgressMessage(BitmapRequest<?> bitmapRequest, int type, int currentSize, int totalSize) {
        if (bitmapRequest == null) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ResponseHandler_sendBitmapRequestProgressMessage:bitmapRequest为null", null, 1);
            return;
        }
        Message message = new Message();
        MessageObject messageObject = new MessageObject(1);
        messageObject.putObject(REQUEST, bitmapRequest);
        messageObject.putObject(TYPE, type);
        messageObject.putObject(CURRENTSIZE, currentSize);
        messageObject.putObject(TOTALSIZE, totalSize);
        message.what = messageObject.getWhat();
        message.obj = messageObject;
        doSendMessage(message, bitmapRequest.getSuperKey(), messageObject);
    }

    public void sendDownloadRequestProgressMessage(DownloadRequest<?> downloadRequest, int type, int currentSize, int totalSize) {
        if (downloadRequest == null) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ResponseHandler_sendDownloadRequestProgressMessage:downloadRequest为null", null, 1);
            return;
        }
        Message message = new Message();
        MessageObject messageObject = new MessageObject(2);
        messageObject.putObject(REQUEST, downloadRequest);
        messageObject.putObject(TYPE, type);
        messageObject.putObject(CURRENTSIZE, currentSize);
        messageObject.putObject(TOTALSIZE, totalSize);
        message.what = messageObject.getWhat();
        message.obj = messageObject;
        doSendMessage(message, downloadRequest.getSuperKey(), messageObject);
    }

    public void sendBreakpointDownloadRequestProgressMessage(BreakpointDownloadRequest<?> breakpointDownloadRequest, int type, int currentSize, int totalSize) {
        if (breakpointDownloadRequest == null) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ResponseHandler_sendBreakpointDownloadRequestProgressMessage:breakpointDownloadRequest为null", null, 1);
            return;
        }
        Message message = new Message();
        MessageObject messageObject = new MessageObject(3);
        messageObject.putObject(REQUEST, breakpointDownloadRequest);
        messageObject.putObject(TYPE, type);
        messageObject.putObject(CURRENTSIZE, currentSize);
        messageObject.putObject(TOTALSIZE, totalSize);
        message.what = messageObject.getWhat();
        message.obj = messageObject;
        doSendMessage(message, breakpointDownloadRequest.getSuperKey(), messageObject);
    }

    /**
     * 这三个相似的方法其实可以封装成一个方法内,但是那样一堆判断反而不直观
     */
    public void sendUploadRequestProgressMessage(UploadRequest<?> uploadRequest, int type, long currentSize, long totalSize) {
        if (uploadRequest == null) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ResponseHandler_sendUploadRequestProgressMessage:uploadRequest为null", null, 1);
            return;
        }
        Message message = new Message();
        MessageObject messageObject = new MessageObject(4);
        messageObject.putObject(REQUEST, uploadRequest);
        messageObject.putObject(TYPE, type);
        messageObject.putObject(CURRENTLONGSIZE, currentSize);
        messageObject.putObject(TOTALLONGSIZE, totalSize);
        message.what = messageObject.getWhat();
        message.obj = messageObject;
        doSendMessage(message, uploadRequest.getSuperKey(), messageObject);
    }

    public void sendCacheItemMessage(CacheItemRequest cacheItemRequest) {
        if (cacheItemRequest == null) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "ResponseHandler_sendCacheItemMessage:cacheItemRequest为null", null, 1);
            return;
        }
        Message message = new Message();
        MessageObject messageObject = new MessageObject(5);
        messageObject.putObject(CACHEITEMREQUEST, cacheItemRequest);
        message.what = messageObject.getWhat();
        message.obj = messageObject;
        doSendMessage(message, cacheItemRequest.getSuperKey(), messageObject);
    }

    /**
     * 参数message,superKey,messageObject都是NotNull的
     */
    protected void doSendMessage(Message message, SuperKey superKey, MessageObject messageObject) {
        sendMessage(message);
    }

    /**
     * 根据superKey移除未执行的消息队列
     */
    public void removeResponseMessageBySuperKey(SuperKey superKey, boolean removeListener) {

    }

    /**
     * 根据tag移除未执行的消息队列
     */
    public void removeResponseMessageByTag(String tag, boolean removeListener) {

    }

    /**
     * 移除所有未执行的消息队列
     */
    public void removeAllResponseMessage(boolean removeListener) {

    }

    public class MessageObject {

        private final LinkedHashMap<String, Object> linkedHashMapObjects = new LinkedHashMap<String, Object>();

        private final int what;


        public MessageObject(int what) {
            this.what = what;
        }

        public int getWhat() {
            return what;
        }

        public void putObject(String key, Object object) {
            if (key == null || object == null) {
                return;
            }
            linkedHashMapObjects.put(key, object);
        }

        public Object getObject(String key) {
            if (key == null) {
                return null;
            }
            if (linkedHashMapObjects.containsKey(key)) {
                return linkedHashMapObjects.get(key);
            } else {
                return null;
            }
        }

        public void removeObject(String key) {
            if (key == null) {
                return;
            }
            if (linkedHashMapObjects.containsKey(key)) {
                linkedHashMapObjects.remove(key);
            }
        }

        @Override
        public String toString() {
            return "MessageObject{" +
                    "linkedHashMapObjects=" + linkedHashMapObjects +
                    ", what=" + what +
                    '}';
        }
    }
}

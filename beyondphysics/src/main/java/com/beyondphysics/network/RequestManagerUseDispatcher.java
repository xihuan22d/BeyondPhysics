package com.beyondphysics.network;


import android.content.Context;

import com.beyondphysics.network.cache.BitmapDiskCacheAnalyze;
import com.beyondphysics.network.cache.BitmapMemoryCache;
import com.beyondphysics.network.http.HttpAgreement;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperBitmap;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadTool;
import com.beyondphysics.network.utils.TimeTool;
import com.beyondphysics.network.utils.UrlTool;

/**
 * Created by xihuan22 on 2017/10/30.
 */
public class RequestManagerUseDispatcher extends RequestManager {


    /**
     * NotNull
     */
    private final PriorityBlockingQueueDispatcherItem priorityBlockingQueueDispatcherItem;
    /**
     * NotNull
     */
    private final BaseDispatcherContainer baseDispatcherContainer;

    public RequestManagerUseDispatcher(Context context, int networkThreadCount, int bitmapNetworkThreadCount, HttpAgreement httpAgreement, BitmapMemoryCache bitmapMemoryCache, BitmapDiskCacheAnalyze bitmapDiskCacheAnalyze, int diskCacheThreadCount, String fileCache, int maxDiskCacheSize, int responseHandlerType, int dispatcherThreadCount) {
        super(context, networkThreadCount, bitmapNetworkThreadCount, httpAgreement, bitmapMemoryCache, bitmapDiskCacheAnalyze, diskCacheThreadCount, fileCache, maxDiskCacheSize, responseHandlerType);
        priorityBlockingQueueDispatcherItem = new PriorityBlockingQueueDispatcherItem(mainResponseHandler);
        baseDispatcherContainer = new BaseDispatcherContainer(this, dispatcherThreadCount);
    }

    public PriorityBlockingQueueDispatcherItem getPriorityBlockingQueueDispatcherItem() {
        return priorityBlockingQueueDispatcherItem;
    }

    public BaseDispatcherContainer getBaseDispatcherContainer() {
        return baseDispatcherContainer;
    }

    @Override
    public void open() {
        ThreadTool.throwIfNotOnMainThread();
        if (open) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "RequestManagerUseDispatcher_open:重复调用open方法", null, 1);
        } else {
            if (baseNetworkContainer != null) {
                baseNetworkContainer.openAllThread();
            }
            if (baseBitmapNetworkContainer != null) {
                baseBitmapNetworkContainer.openAllThread();
            }
            if (baseDiskCacheContainer != null) {
                baseDiskCacheContainer.openAllThread();
            }
            if (baseCacheItemIOContainer != null) {
                baseCacheItemIOContainer.openAllThread();
            }
            if (baseDispatcherContainer != null) {
                baseDispatcherContainer.openAllThread();
            }
            open = true;
        }
    }

    @Override
    public void closeByWait() {
        ThreadTool.throwIfNotOnMainThread();
        if (!open) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "RequestManagerUseDispatcher_closeByWait:未调用open方法", null, 1);
        } else {
            priorityBlockingQueueDispatcherItem.cancelAllDispatcherItem(true);
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItemWithAll(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequestByWait(true);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequestByWait(true);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            threadSafelyLinkedHasMapRequest_Network.cancelAllRequestByWait(true);
            mainResponseHandler.removeAllResponseMessage(true);
            if (baseNetworkContainer != null) {
                baseNetworkContainer.closeAllThreadByWait();
            }
            if (baseBitmapNetworkContainer != null) {
                baseBitmapNetworkContainer.closeAllThreadByWait();
            }
            if (baseDiskCacheContainer != null) {
                baseDiskCacheContainer.closeAllThreadByWait();
            }
            if (baseCacheItemIOContainer != null) {
                baseCacheItemIOContainer.closeAllThreadByWait();
            }
            if (baseDispatcherContainer != null) {
                baseDispatcherContainer.closeAllThreadByWait();
            }
            open = false;
        }
    }

    @Override
    public void close() {
        ThreadTool.throwIfNotOnMainThread();
        if (!open) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "RequestManagerUseDispatcher_close:未调用open方法", null, 1);
        } else {
            priorityBlockingQueueDispatcherItem.cancelAllDispatcherItem(true);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequest(true);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequest(true);
            threadSafelyLinkedHasMapRequest_Network.cancelAllRequest(true);
            mainResponseHandler.removeAllResponseMessage(true);
            if (baseNetworkContainer != null) {
                baseNetworkContainer.closeAllThread();
            }
            if (baseBitmapNetworkContainer != null) {
                baseBitmapNetworkContainer.closeAllThread();
            }
            if (baseDiskCacheContainer != null) {
                baseDiskCacheContainer.closeAllThread();
            }
            if (baseCacheItemIOContainer != null) {
                baseCacheItemIOContainer.closeAllThread();
            }
            if (baseDispatcherContainer != null) {
                baseDispatcherContainer.closeAllThread();
            }
            open = false;
        }
    }

    @Override
    public void addRequest(Request<?> request) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            request.setRunningStatus(Request.WAITING);
            threadSafelyLinkedHasMapRequest_Network.putRequest(request.getSuperKey(), request);
        }
    }

    /**
     * 在调度线程中添加请求
     */
    public boolean addRequestToDispatcher(Request<?> request) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (request != null) {
            request.setRunningStatus(Request.WAITING);
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.REQUEST_ADD, request.getSuperKey(), request);
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public void addRequestWithSort(Request<?> request) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            request.setRunningStatus(Request.WAITING);
            request.setNeedSort(true);
            threadSafelyLinkedHasMapRequest_Network.putAndSortRequest(request.getSuperKey(), request, 1);
        }
    }

    /**
     * 在调度线程中添加请求并对请求排序
     */
    public boolean addRequestWithSortToDispatcher(Request<?> request) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (request != null) {
            request.setRunningStatus(Request.WAITING);
            request.setNeedSort(true);
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.REQUEST_ADD_WITHSORT, request.getSuperKey(), request);
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public <T> void addBitmapRequest(BitmapRequest<T> bitmapRequest) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.WAITING);
            SuperBitmap superBitmap = threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().getSuperBitmap(bitmapRequest.getSuperKey().getKey());
            if (superBitmap == null) {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                    threadSafelyLinkedHasMapBitmapRequest_Network.putRequest(bitmapRequest.getSuperKey(), bitmapRequest);
                } else if (type == UrlTool.URLTYPE_FILE || type == UrlTool.URLTYPE_ASSETS || type == UrlTool.URLTYPE_RESOURCE) {
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putRequest(bitmapRequest.getSuperKey(), bitmapRequest);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                    Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(HttpResponse.ERROR_URLTYPE);
                    }
                }
            } else {
                bitmapRequest.setRunningStatus(Request.FINISH);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setResult(BitmapRequest.BITMAPREADMEMORYCACHESUCCESS);
                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_MEMORY);
                httpResponse.setSuperBitmap(superBitmap);
                Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                if (onResponseListener != null) {
                    onResponseListener.onSuccessResponse(bitmapRequest.convertResponseType(bitmapRequest, httpResponse));
                }
            }
        }
    }

    /**
     * 在调度线程中添图片请求
     */
    public <T> boolean addBitmapRequestToDispatcher(BitmapRequest<T> bitmapRequest) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.WAITING);
            SuperBitmap superBitmap = threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().getSuperBitmap(bitmapRequest.getSuperKey().getKey());
            if (superBitmap == null) {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                    DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.BITMAPREQUEST_ADD_NETWORK, bitmapRequest.getSuperKey(), bitmapRequest);
                    putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
                } else if (type == UrlTool.URLTYPE_FILE || type == UrlTool.URLTYPE_ASSETS || type == UrlTool.URLTYPE_RESOURCE) {
                    DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.BITMAPREQUEST_ADD_DISKCACHE, bitmapRequest.getSuperKey(), bitmapRequest);
                    putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                    Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(HttpResponse.ERROR_URLTYPE);
                    }
                }
            } else {
                bitmapRequest.setRunningStatus(Request.FINISH);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setResult(BitmapRequest.BITMAPREADMEMORYCACHESUCCESS);
                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_MEMORY);
                httpResponse.setSuperBitmap(superBitmap);
                Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                if (onResponseListener != null) {
                    onResponseListener.onSuccessResponse(bitmapRequest.convertResponseType(bitmapRequest, httpResponse));
                }
            }
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public <T> void addBitmapRequestWithSort(BitmapRequest<T> bitmapRequest) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.WAITING);
            bitmapRequest.setNeedSort(true);
            SuperBitmap superBitmap = threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().getSuperBitmap(bitmapRequest.getSuperKey().getKey());
            if (superBitmap == null) {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                    threadSafelyLinkedHasMapBitmapRequest_Network.putAndSortRequest(bitmapRequest.getSuperKey(), bitmapRequest, 1);
                } else if (type == UrlTool.URLTYPE_FILE || type == UrlTool.URLTYPE_ASSETS || type == UrlTool.URLTYPE_RESOURCE) {
                    threadSafelyLinkedHasMapBitmapRequest_DiskCache.putAndSortRequest(bitmapRequest.getSuperKey(), bitmapRequest, 1);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                    Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(HttpResponse.ERROR_URLTYPE);
                    }
                }
            } else {
                bitmapRequest.setRunningStatus(Request.FINISH);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setResult(BitmapRequest.BITMAPREADMEMORYCACHESUCCESS);
                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_MEMORY);
                httpResponse.setSuperBitmap(superBitmap);
                Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                if (onResponseListener != null) {
                    onResponseListener.onSuccessResponse(bitmapRequest.convertResponseType(bitmapRequest, httpResponse));
                }
            }

        }
    }

    /**
     * 在调度线程中添加图片请求并对请求排序
     */
    public <T> boolean addBitmapRequestWithSortToDispatcher(BitmapRequest<T> bitmapRequest) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (bitmapRequest != null) {
            bitmapRequest.setRunningStatus(Request.WAITING);
            bitmapRequest.setNeedSort(true);
            SuperBitmap superBitmap = threadSafelyLinkedHasMapCacheItem.getBitmapMemoryCache().getSuperBitmap(bitmapRequest.getSuperKey().getKey());
            if (superBitmap == null) {
                int type = UrlTool.getUrlType(bitmapRequest.getUrlString());
                if (type == UrlTool.URLTYPE_HTTP || type == UrlTool.URLTYPE_HTTPS) {
                    DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.BITMAPREQUEST_ADD_NETWORK_WITHSORT, bitmapRequest.getSuperKey(), bitmapRequest);
                    putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
                } else if (type == UrlTool.URLTYPE_FILE || type == UrlTool.URLTYPE_ASSETS || type == UrlTool.URLTYPE_RESOURCE) {
                    DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.BITMAPREQUEST_ADD_DISKCACHE_WITHSORT, bitmapRequest.getSuperKey(), bitmapRequest);
                    putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
                } else {
                    bitmapRequest.setRunningStatus(Request.FINISH);
                    Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                    if (onResponseListener != null) {
                        onResponseListener.onErrorResponse(HttpResponse.ERROR_URLTYPE);
                    }
                }
            } else {
                bitmapRequest.setRunningStatus(Request.FINISH);
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setStatus(HttpResponse.SUCCESS);
                httpResponse.setResult(BitmapRequest.BITMAPREADMEMORYCACHESUCCESS);
                httpResponse.setBitmapGetFrom(HttpResponse.BITMAP_GETFROM_MEMORY);
                httpResponse.setSuperBitmap(superBitmap);
                Request.OnResponseListener<T> onResponseListener = bitmapRequest.getOnResponseListener();
                if (onResponseListener != null) {
                    onResponseListener.onSuccessResponse(bitmapRequest.convertResponseType(bitmapRequest, httpResponse));
                }
            }
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public void cancelRequestWithSuperKeyByWait(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(superKey, removeListener);
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST, superKey);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }

    @Override
    public void cancelRequestWithSuperKey(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKey(superKey, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }

    /**
     * 在调度线程中取消请求
     */
    public boolean cancelRequestWithSuperKeyToDispatcher(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (superKey != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(superKey, removeListener);
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.REQUEST_CANCEL, new SuperKey(SuperKey.DEFAULT_KEY, DispatcherItem.CANCELREQUESTWITHSUPERKEYTODISPATCHER_TAG, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), superKey);
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public void cancelRequestWithRequestByWait(Request<?> request, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(request.getSuperKey(), removeListener);
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST, request.getSuperKey());
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(request.getSuperKey(), removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKeyByWait(request.getSuperKey(), removeListener);
            request.cancelRequest();
            Request.removeListener(request, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(request.getSuperKey(), removeListener);
        }
    }

    @Override
    public void cancelRequestWithRequest(Request<?> request, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (request != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(request.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapRequest_Network.cancelRequestWithSuperKey(request.getSuperKey(), removeListener);
            request.cancelRequest();
            Request.removeListener(request, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(request.getSuperKey(), removeListener);
        }
    }

    /**
     * 在调度线程中取消请求
     */
    public boolean cancelRequestWithRequestToDispatcher(Request<?> request, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (request != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(request.getSuperKey(), removeListener);
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.REQUEST_CANCEL, new SuperKey(SuperKey.DEFAULT_KEY, DispatcherItem.CANCELREQUESTWITHSUPERKEYTODISPATCHER_TAG, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), request.getSuperKey());
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
            request.cancelRequest();
            Request.removeListener(request, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(request.getSuperKey(), removeListener);
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public void cancelBitmapRequestWithSuperKeyByWait(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(superKey, removeListener);
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_BITMAP_REQUEST, superKey);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(superKey, removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }

    @Override
    public void cancelBitmapRequestWithSuperKey(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (superKey != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(superKey, removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(superKey, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
    }

    /**
     * 在调度线程中取消图片请求
     */
    public boolean cancelBitmapRequestWithSuperKeyToDispatcher(SuperKey superKey, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (superKey != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(superKey, removeListener);
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.BITMAPREQUEST_CANCEL, new SuperKey(SuperKey.DEFAULT_KEY, DispatcherItem.CANCELREQUESTWITHSUPERKEYTODISPATCHER_TAG, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), superKey);
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
            mainResponseHandler.removeResponseMessageBySuperKey(superKey, removeListener);
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public void cancelBitmapRequestWithRequestByWait(BitmapRequest<?> bitmapRequest, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_BITMAP_REQUEST, bitmapRequest.getSuperKey());
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKeyByWait(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKeyByWait(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
            bitmapRequest.cancelRequest();
            Request.removeBitmapRequestListener(bitmapRequest, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(bitmapRequest.getSuperKey(), removeListener);
        }
    }

    @Override
    public void cancelBitmapRequestWithRequest(BitmapRequest<?> bitmapRequest, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        if (bitmapRequest != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            bitmapRequest.cancelRequest();
            Request.removeBitmapRequestListener(bitmapRequest, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(bitmapRequest.getSuperKey(), removeListener);
        }
    }

    /**
     * 在调度线程中取消图片请求
     */
    public boolean cancelBitmapRequestWithRequestToDispatcher(BitmapRequest<?> bitmapRequest, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (bitmapRequest != null) {
            priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithSuperKey(bitmapRequest.getSuperKey(), removeListener);
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.BITMAPREQUEST_CANCEL, new SuperKey(SuperKey.DEFAULT_KEY, DispatcherItem.CANCELREQUESTWITHSUPERKEYTODISPATCHER_TAG, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), bitmapRequest.getSuperKey());
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
            bitmapRequest.cancelRequest();
            Request.removeBitmapRequestListener(bitmapRequest, removeListener);
            mainResponseHandler.removeResponseMessageBySuperKey(bitmapRequest.getSuperKey(), removeListener);
        }
        return putDispatcherItemSuccess;
    }

    @Override
    public void cancelRequestWithTagByWait(String tag, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithTag(tag, removeListener);
        RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItem(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST, tag);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithTagByWait(tag, removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithTagByWait(tag, removeListener);
        threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
        threadSafelyLinkedHasMapRequest_Network.cancelRequestWithTagByWait(tag, removeListener);
        mainResponseHandler.removeResponseMessageByTag(tag, removeListener);
    }

    @Override
    public void cancelRequestWithTag(String tag, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithTag(tag, removeListener);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelRequestWithTag(tag, removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelRequestWithTag(tag, removeListener);
        threadSafelyLinkedHasMapRequest_Network.cancelRequestWithTag(tag, removeListener);
        mainResponseHandler.removeResponseMessageByTag(tag, removeListener);
    }

    /**
     * 在调度线程中取消请求
     */
    public boolean cancelRequestWithTagToDispatcher(String tag, boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        priorityBlockingQueueDispatcherItem.cancelDispatcherItemWithTag(tag, removeListener);
        DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.REQUEST_CANCELWITHTAG, new SuperKey(SuperKey.DEFAULT_KEY, DispatcherItem.CANCELREQUESTWITHTAGTODISPATCHER_TAG, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), tag);
        putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
        mainResponseHandler.removeResponseMessageByTag(tag, removeListener);
        return putDispatcherItemSuccess;
    }

    @Override
    public void cancelAllRequestByWait(boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        priorityBlockingQueueDispatcherItem.cancelAllDispatcherItem(removeListener);
        RequestStatusItem requestStatusItem = threadSafelyArrayListRequestStatusItem.addRequestStatusItemWithAll(RequestStatusItem.STATUS_CANCELING, RequestStatusItem.KIND_ALL_REQUEST);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequestByWait(removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequestByWait(removeListener);
        threadSafelyArrayListRequestStatusItem.removeRequestStatusItem(requestStatusItem);
        threadSafelyLinkedHasMapRequest_Network.cancelAllRequestByWait(removeListener);
        mainResponseHandler.removeAllResponseMessage(removeListener);
    }

    @Override
    public void cancelAllRequest(boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        priorityBlockingQueueDispatcherItem.cancelAllDispatcherItem(removeListener);
        threadSafelyLinkedHasMapBitmapRequest_DiskCache.cancelAllRequest(removeListener);
        threadSafelyLinkedHasMapBitmapRequest_Network.cancelAllRequest(removeListener);
        threadSafelyLinkedHasMapRequest_Network.cancelAllRequest(removeListener);
        mainResponseHandler.removeAllResponseMessage(removeListener);
    }

    /**
     * 在调度线程中执行取消
     */
    public boolean cancelAllRequestToDispatcher(boolean removeListener) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        priorityBlockingQueueDispatcherItem.cancelAllDispatcherItem(removeListener);
        DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.REQUEST_CANCELALL, new SuperKey(SuperKey.DEFAULT_KEY, DispatcherItem.CANCELALLREQUESTTODISPATCHER_TAG, 10, 1, TimeTool.getOnlyTimeWithoutSleep()), null);
        putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
        mainResponseHandler.removeAllResponseMessage(removeListener);
        return putDispatcherItemSuccess;
    }

    /**
     * 在调度线程中获取对应key的缓存
     */
    public boolean doCacheItemsWithCallback(CacheItemRequest cacheItemRequest) {
        ThreadTool.throwIfNotOnMainThread();
        boolean putDispatcherItemSuccess = false;
        if (cacheItemRequest != null) {
            DispatcherItem dispatcherItem = new DispatcherItem(DispatcherItem.DOCACHEITEMS, cacheItemRequest.getSuperKey(), cacheItemRequest);
            putDispatcherItemSuccess = priorityBlockingQueueDispatcherItem.putDispatcherItem(dispatcherItem.getSuperKey(), dispatcherItem);
        }
        return putDispatcherItemSuccess;
    }


}

package com.beyondphysics.network;


import com.beyondphysics.network.utils.SuperKey;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xihuan22 on 2017/8/17.
 */

public class ThreadSafelyArrayListRequestStatusItem {

    public final List<RequestStatusItem> listRequestStatusItems = new ArrayList<RequestStatusItem>();


    public ThreadSafelyArrayListRequestStatusItem() {
    }


    public RequestStatusItem addRequestStatusItem(int status, int kind, SuperKey superKey) {
        if (superKey == null) {
            return null;
        }
        RequestStatusItem requestStatusItem = new RequestStatusItem(status, kind, RequestStatusItem.TYPE_SUPERKEY, superKey, null);
        synchronized (this) {
            listRequestStatusItems.add(requestStatusItem);
            return requestStatusItem;
        }
    }

    public RequestStatusItem addRequestStatusItem(int status, int kind, String tag) {
        if (tag == null) {
            return null;
        }
        RequestStatusItem requestStatusItem = new RequestStatusItem(status, kind, RequestStatusItem.TYPE_TAG, null, tag);
        synchronized (this) {
            listRequestStatusItems.add(requestStatusItem);
            return requestStatusItem;
        }
    }

    public RequestStatusItem addRequestStatusItemWithAll(int status, int kind) {
        RequestStatusItem requestStatusItem = new RequestStatusItem(status, kind, RequestStatusItem.TYPE_ALL, null, null);
        synchronized (this) {
            listRequestStatusItems.add(requestStatusItem);
            return requestStatusItem;
        }
    }


    public void removeRequestStatusItem(RequestStatusItem requestStatusItem) {
        if (requestStatusItem == null) {
            return;
        }
        synchronized (this) {
            listRequestStatusItems.remove(requestStatusItem);
        }
    }


    public boolean isRequestPause(Request<?> request, int kind) {
        boolean result = false;
        if (request == null) {
            return result;
        }
        synchronized (this) {
            for (int i = 0; i < listRequestStatusItems.size(); i++) {
                RequestStatusItem requestStatusItem = listRequestStatusItems.get(i);
                if (requestStatusItem.getStatus() == RequestStatusItem.STATUS_PAUSE && (requestStatusItem.getKind() == RequestStatusItem.KIND_ALL_REQUEST || requestStatusItem.getKind() == kind)) {
                    if (requestStatusItem.getType() == RequestStatusItem.TYPE_SUPERKEY) {
                        if (requestStatusItem.getSuperKey() != null && request.getSuperKey().equals(requestStatusItem.getSuperKey())) {
                            result = true;
                            break;
                        }
                    } else if (requestStatusItem.getType() == RequestStatusItem.TYPE_TAG) {
                        if (requestStatusItem.getTag() != null && request.getSuperKey().getTag().equals(requestStatusItem.getTag())) {
                            result = true;
                            break;
                        }
                    } else if (requestStatusItem.getType() == RequestStatusItem.TYPE_ALL) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }

    public boolean isRequestCanceling(Request<?> request, int kind) {
        boolean result = false;
        if (request == null) {
            return result;
        }
        synchronized (this) {
            for (int i = 0; i < listRequestStatusItems.size(); i++) {
                RequestStatusItem requestStatusItem = listRequestStatusItems.get(i);
                if (requestStatusItem.getStatus() == RequestStatusItem.STATUS_CANCELING && (requestStatusItem.getKind() == RequestStatusItem.KIND_ALL_REQUEST || requestStatusItem.getKind() == kind)) {
                    if (requestStatusItem.getType() == RequestStatusItem.TYPE_SUPERKEY) {
                        if (requestStatusItem.getSuperKey() != null && request.getSuperKey().equals(requestStatusItem.getSuperKey())) {
                            result = true;
                            break;
                        }
                    } else if (requestStatusItem.getType() == RequestStatusItem.TYPE_TAG) {
                        if (requestStatusItem.getTag() != null && request.getSuperKey().getTag().equals(requestStatusItem.getTag())) {
                            result = true;
                            break;
                        }
                    } else if (requestStatusItem.getType() == RequestStatusItem.TYPE_ALL) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }


    public boolean isRequestPauseOrCanceling(Request<?> request, int kind) {
        boolean result = false;
        if (request == null) {
            return result;
        }
        synchronized (this) {
            for (int i = 0; i < listRequestStatusItems.size(); i++) {
                RequestStatusItem requestStatusItem = listRequestStatusItems.get(i);
                if ((requestStatusItem.getStatus() == RequestStatusItem.STATUS_PAUSE || requestStatusItem.getStatus() == RequestStatusItem.STATUS_CANCELING) && (requestStatusItem.getKind() == RequestStatusItem.KIND_ALL_REQUEST || requestStatusItem.getKind() == kind)) {
                    if (requestStatusItem.getType() == RequestStatusItem.TYPE_SUPERKEY) {
                        if (requestStatusItem.getSuperKey() != null && request.getSuperKey().equals(requestStatusItem.getSuperKey())) {
                            result = true;
                            break;
                        }
                    } else if (requestStatusItem.getType() == RequestStatusItem.TYPE_TAG) {
                        if (requestStatusItem.getTag() != null && request.getSuperKey().getTag().equals(requestStatusItem.getTag())) {
                            result = true;
                            break;
                        }
                    } else if (requestStatusItem.getType() == RequestStatusItem.TYPE_ALL) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }


}

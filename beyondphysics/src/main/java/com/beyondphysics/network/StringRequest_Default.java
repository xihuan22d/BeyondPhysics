package com.beyondphysics.network;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/8/19.
 */

public class StringRequest_Default extends Request<String> {
    public StringRequest_Default(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, requestType, tag, onResponseListener);
    }

    public StringRequest_Default(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, requestType, tag, onResponseListener, priority);
    }

    public StringRequest_Default(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding);
    }

    public StringRequest_Default(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs);
    }

    public StringRequest_Default(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, cacheInDisk);
    }

    public StringRequest_Default(String urlString, int requestType, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        super(urlString, requestType, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, cacheInDisk, NORMAL_REQUEST);
    }

    public StringRequest_Default(StringRequest_Default_Params stringRequest_Default_Params) {
        super(stringRequest_Default_Params.getUrlString(), stringRequest_Default_Params.getRequestType(), stringRequest_Default_Params.getTag(), stringRequest_Default_Params.getOnResponseListener(), stringRequest_Default_Params.getPriority(), stringRequest_Default_Params.getEncoding(), stringRequest_Default_Params.getConnectTimeoutMs(), stringRequest_Default_Params.getReadTimeoutMs(), stringRequest_Default_Params.isCacheInDisk());
    }

    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        return headerParams;
    }

    @Override
    public String convertResponseType(Request<String> request, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        return httpResponse.getResult();
    }

}

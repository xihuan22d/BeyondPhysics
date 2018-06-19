package com.my.utils;

import com.beyondphysics.network.Request;
import com.beyondphysics.network.StringRequest_Default;
import com.beyondphysics.network.StringRequest_Default_Params;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;

import java.util.HashMap;
import java.util.Map;


public class MyStringRequest extends StringRequest_Default {
    //uid和token在主线程中获取,getHeaderParams方法是线程中执行的
    private String uid;
    private String token;

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, requestType, tag, onResponseListener);
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, requestType, tag, onResponseListener, priority);
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding);
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs);
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, cacheInDisk);
    }

    public MyStringRequest(String urlString, int requestType, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        super(urlString, requestType, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, cacheInDisk);
    }

    public MyStringRequest(StringRequest_Default_Params stringRequest_Default_Params) {
        super(stringRequest_Default_Params);
    }


    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        headerParams.put("uid", "5ae16a9ea9494908a41df04e");
        headerParams.put("token", "c3be8edecce68a77_1524722334500_20n9ossp");
        return headerParams;
    }

    @Override
    public String convertResponseType(Request<String> request, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        return EncryptionTool.stringToDecode(httpResponse.getResult());
    }
}

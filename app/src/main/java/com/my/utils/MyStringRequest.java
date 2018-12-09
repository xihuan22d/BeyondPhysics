package com.my.utils;

import com.beyondphysics.network.Request;
import com.beyondphysics.network.StringRequest_Default;
import com.beyondphysics.network.StringRequest_Default_Params;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;
import com.my.beyondphysicsapplication.TheApplication;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class MyStringRequest extends StringRequest_Default {
    //uid和token在主线程中获取,getHeaderParams方法是线程中执行的
    private String uid;
    private String token;

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, requestType, tag, onResponseListener);
        setOnHttpStatusListener();
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, requestType, tag, onResponseListener, priority);
        setOnHttpStatusListener();
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding);
        setOnHttpStatusListener();
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs);
        setOnHttpStatusListener();
    }

    public MyStringRequest(String urlString, int requestType, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        super(urlString, requestType, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, cacheInDisk);
        setOnHttpStatusListener();
    }

    public MyStringRequest(String urlString, int requestType, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, boolean cacheInDisk) {
        super(urlString, requestType, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, cacheInDisk);
        setOnHttpStatusListener();
    }

    public MyStringRequest(StringRequest_Default_Params stringRequest_Default_Params) {
        super(stringRequest_Default_Params);
        setOnHttpStatusListener();
    }


    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        headerParams.put("uid", "5c0b4645579bcb6796cc79b9");
        headerParams.put("token", "11e79db7afa0f62e919fde99e2e870e6_198ddbb613de217c");
        return headerParams;
    }

    @Override
    public String convertResponseType(Request<String> request, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        return EncryptionTool.stringToDecode(httpResponse.getResult());
    }

    private void setOnHttpStatusListener() {
        if (HttpConnectTool.USEHTTPS) {
            setOnHttpStatusListener(new OnHttpStatusListener() {
                @Override
                public void onHttpInit(HttpURLConnection httpURLConnection, Object object) {
                }

                @Override
                public void onHttpsInit(HttpsURLConnection httpsURLConnection, Object object) {
                    if (httpsURLConnection != null) {
                        httpsURLConnection.setSSLSocketFactory(HttpConnectTool.getInstanceSslSocketFactory(TheApplication.getTheApplication()));
                        HttpConnectTool.setHostnameVerifier(httpsURLConnection);
                    }
                }

                @Override
                public void onHttpCompleted(HttpURLConnection httpURLConnection, Object object) {
                }

                @Override
                public void onHttpsCompleted(HttpsURLConnection httpsURLConnection, Object object) {
                }
            });
        }
    }
}

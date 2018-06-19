package com.my.utils;

import com.beyondphysics.network.Request;
import com.beyondphysics.network.UploadRequest_Default;
import com.beyondphysics.network.UploadRequest_Default_Params;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;

import java.util.HashMap;
import java.util.Map;


public class MyUploadRequest extends UploadRequest_Default {


    public MyUploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, names, values, fileNames, filePaths, tag, onResponseListener);
    }

    public MyUploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, names, values, fileNames, filePaths, tag, onResponseListener, priority);
    }

    public MyUploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, names, values, fileNames, filePaths, tag, onResponseListener, priority, encoding);
    }

    public MyUploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnUploadProgressListener onUploadProgressListener) {
        super(urlString, names, values, fileNames, filePaths, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, onUploadProgressListener);
    }

    public MyUploadRequest(String urlString, String[] names, String[] values, String[] fileNames, String[] filePaths, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnUploadProgressListener onUploadProgressListener) {
        super(urlString, names, values, fileNames, filePaths, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, onUploadProgressListener);
    }

    public MyUploadRequest(UploadRequest_Default_Params uploadRequest_Default_Params) {
        super(uploadRequest_Default_Params);
    }


    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "multipart/form-data;charset=" + getEncoding() + ";boundary=*****");
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

package com.my.utils;

import com.beyondphysics.network.BreakpointDownloadRequest_Default;
import com.beyondphysics.network.BreakpointDownloadRequest_Default_Params;
import com.beyondphysics.network.utils.SuperKey;

import java.util.HashMap;
import java.util.Map;

public class MyBreakpointDownloadRequest extends BreakpointDownloadRequest_Default {

    public MyBreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, savePath, beginAddress, tag, onResponseListener);
    }

    public MyBreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, savePath, beginAddress, tag, onResponseListener, priority);
    }

    public MyBreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, savePath, beginAddress, tag, onResponseListener, priority, encoding);
    }

    public MyBreakpointDownloadRequest(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, savePath, beginAddress, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, onDownloadProgressListener);
    }

    public MyBreakpointDownloadRequest(String urlString, String savePath, int beginAddress, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, savePath, beginAddress, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, onDownloadProgressListener);
    }

    public MyBreakpointDownloadRequest(BreakpointDownloadRequest_Default_Params breakpointDownloadRequest_Default_Params) {
        super(breakpointDownloadRequest_Default_Params);
    }


    @Override
    public Map<String, String> getHeaderParams() {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Accept-Charset", getEncoding());
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=" + getEncoding());
        HttpConnectTool.addReferer(headerParams);
        return headerParams;
    }

}

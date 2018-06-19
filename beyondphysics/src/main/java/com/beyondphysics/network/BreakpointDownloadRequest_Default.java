package com.beyondphysics.network;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;

/**
 * Created by xihuan22 on 2017/9/19.
 */

public class BreakpointDownloadRequest_Default extends BreakpointDownloadRequest<String> {

    public BreakpointDownloadRequest_Default(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, savePath, beginAddress, tag, onResponseListener);
    }

    public BreakpointDownloadRequest_Default(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, savePath, beginAddress, tag, onResponseListener, priority);
    }

    public BreakpointDownloadRequest_Default(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, savePath, beginAddress, tag, onResponseListener, priority, encoding);
    }

    public BreakpointDownloadRequest_Default(String urlString, String savePath, int beginAddress, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, savePath, beginAddress, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, onDownloadProgressListener);
    }

    public BreakpointDownloadRequest_Default(String urlString, String savePath, int beginAddress, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, savePath, beginAddress, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, onDownloadProgressListener);
    }

    public BreakpointDownloadRequest_Default(BreakpointDownloadRequest_Default_Params breakpointDownloadRequest_Default_Params) {
        super(breakpointDownloadRequest_Default_Params.getUrlString(), breakpointDownloadRequest_Default_Params.getSavePath(), breakpointDownloadRequest_Default_Params.getBeginAddress(), breakpointDownloadRequest_Default_Params.getTag(), breakpointDownloadRequest_Default_Params.getOnResponseListener(), breakpointDownloadRequest_Default_Params.getPriority(), breakpointDownloadRequest_Default_Params.getEncoding(), breakpointDownloadRequest_Default_Params.getConnectTimeoutMs(), breakpointDownloadRequest_Default_Params.getReadTimeoutMs(), breakpointDownloadRequest_Default_Params.getOnDownloadProgressListener());
    }

    @Override
    public String convertResponseType(Request<String> request, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        return httpResponse.getResult();
    }
}

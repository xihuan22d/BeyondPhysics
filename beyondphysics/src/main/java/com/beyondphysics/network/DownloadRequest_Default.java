package com.beyondphysics.network;

import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperKey;

/**
 * Created by xihuan22 on 2017/9/19.
 */

public class DownloadRequest_Default extends DownloadRequest<String> {
    public DownloadRequest_Default(String urlString, String savePath, String tag, OnResponseListener<String> onResponseListener) {
        super(urlString, savePath, tag, onResponseListener);
    }

    public DownloadRequest_Default(String urlString, String savePath, String tag, OnResponseListener<String> onResponseListener, int priority) {
        super(urlString, savePath, tag, onResponseListener, priority);
    }

    public DownloadRequest_Default(String urlString, String savePath, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding) {
        super(urlString, savePath, tag, onResponseListener, priority, encoding);
    }

    public DownloadRequest_Default(String urlString, String savePath, String tag, OnResponseListener<String> onResponseListener, int priority, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, savePath, tag, onResponseListener, priority, encoding, connectTimeoutMs, readTimeoutMs, onDownloadProgressListener);
    }

    public DownloadRequest_Default(String urlString, String savePath, SuperKey superKey, OnResponseListener<String> onResponseListener, String encoding, int connectTimeoutMs, int readTimeoutMs, OnDownloadProgressListener onDownloadProgressListener) {
        super(urlString, savePath, superKey, onResponseListener, encoding, connectTimeoutMs, readTimeoutMs, onDownloadProgressListener);
    }

    public DownloadRequest_Default(DownloadRequest_Default_Params downloadRequest_Default_Params) {
        super(downloadRequest_Default_Params.getUrlString(), downloadRequest_Default_Params.getSavePath(), downloadRequest_Default_Params.getTag(), downloadRequest_Default_Params.getOnResponseListener(), downloadRequest_Default_Params.getPriority(), downloadRequest_Default_Params.getEncoding(), downloadRequest_Default_Params.getConnectTimeoutMs(), downloadRequest_Default_Params.getReadTimeoutMs(), downloadRequest_Default_Params.getOnDownloadProgressListener());
    }

    @Override
    public String convertResponseType(Request<String> request, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        return httpResponse.getResult();
    }
}

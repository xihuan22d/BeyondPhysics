package com.my.beyondphysicsapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beyondphysics.network.BreakpointDownloadRequest;
import com.beyondphysics.network.BreakpointDownloadRequest_Default;
import com.beyondphysics.network.Request;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.MD5Tool;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkStateTool;

import java.io.File;

public class BreakpointDownloadActivity extends NewBaseActivity {

    private ImageView imageViewBack;
    private EditText editTextUrl;
    private ProgressBar progressBar;
    private TextView textViewProgress;
    private Button buttonDownload;
    private Button buttonCancel;

    private View.OnClickListener onClickListener;

    private BreakpointDownloadRequest<?> breakpointDownloadRequest;

    private boolean downloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakpoint_download);
        initAll();
    }

    @Override
    protected void initHandler() {
    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewProgress = (TextView) findViewById(R.id.textViewProgress);
        buttonDownload = (Button) findViewById(R.id.buttonDownload);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        String url = "http://47.97.113.47:4126/perfectwallpaper_files/specifys/test.png";
        editTextUrl.setText(url);
        int[] progress = getFileProgress(BreakpointDownloadActivity.this, url);
        if (progress[0] > 0 && progress[1] > 0) {
            progressBar.setMax(progress[1]);
            progressBar.setProgress(progress[0]);
            int x = (int) ((long) progress[0] * 100 / progress[1]);
            textViewProgress.setText(x + "%");
        }
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.buttonDownload:
                        if (!downloading) {
                            goToDownload();
                            downloading = true;
                        }
                        break;
                    case R.id.buttonCancel:
                        BeyondPhysicsManager.getInstance(BreakpointDownloadActivity.this).cancelRequestWithRequest(breakpointDownloadRequest, true);
                        downloading = false;
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonDownload.setOnClickListener(onClickListener);
        buttonCancel.setOnClickListener(onClickListener);
    }

    private void goToDownload() {
        String url = editTextUrl.getText().toString();
        if (!NetworkStateTool.isNetworkConnected(BreakpointDownloadActivity.this)) {
            BaseActivity.showShortToast(BreakpointDownloadActivity.this, TheApplication.NETOPENERROR);
        } else {
            final String savePath = getSavePath(url);
            breakpointDownloadRequest = new BreakpointDownloadRequest_Default(url, savePath, -1, activityKey, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    BaseActivity.showShortToast(BreakpointDownloadActivity.this, response + "文件位于" + savePath);
                }

                @Override
                public void onErrorResponse(String error) {
                    BaseActivity.showShortToast(BreakpointDownloadActivity.this, error);
                }
            }, 9, null, 8000, 22000, new BreakpointDownloadRequest.OnDownloadProgressListener() {
                @Override
                public void maxProgress(BreakpointDownloadRequest<?> breakpointDownloadRequest, int totalSize) {
                    progressBar.setMax(totalSize);
                }

                @Override
                public void updateProgress(BreakpointDownloadRequest<?> breakpointDownloadRequest, int currentSize, int totalSize) {
                    progressBar.setProgress(currentSize);
                    int x = (int) ((long) currentSize * 100 / totalSize);
                    textViewProgress.setText(x + "%");
                }
            });
            breakpointDownloadRequest.setReceiveCancel(true);
            BeyondPhysicsManager.getInstance(BreakpointDownloadActivity.this).addRequest(breakpointDownloadRequest);
        }
    }


    public static String getSavePath(String url) {
        if (url == null) {
            return null;
        }
        String downloadPath = FileTool.getDefaultRootPath();
        String md5 = MD5Tool.stringToMD5(url);
        if (downloadPath != null && md5 != null) {
            String lastType = url.substring(url.lastIndexOf("."));
            String path = downloadPath + File.separator + md5 + lastType;
            return path;
        } else {
            return null;
        }
    }

    /**
     * return NotNull
     */
    public static int[] getFileProgress(BaseActivity baseActivity, String url) {
        int[] progress = new int[2];
        if (url == null) {
            return progress;
        }
        CacheItem cacheItem = BeyondPhysicsManager.getInstance(baseActivity).getCacheItemByKey(RequestManager.getRequestKey(url, Request.GET), RequestManager.CacheItemTAG.BREAKPOINTDOWNLOADREQUEST);
        if (cacheItem != null) {
            int contentLength = cacheItem.getContentLength();
            String savePath = getSavePath(url);
            if (savePath != null) {
                File file = new File(savePath);
                int beginAddress = 0;
                if (file.exists() && file.isFile()) {
                    beginAddress = (int) file.length();
                }
                progress[0] = beginAddress;
                progress[1] = contentLength;
            }

        }
        return progress;
    }


    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }
}

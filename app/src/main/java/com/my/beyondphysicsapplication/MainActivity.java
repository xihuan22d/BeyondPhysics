package com.my.beyondphysicsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.beyondphysics.network.BitmapRequest_Default_Params;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.NetworkGifImageViewHelp;
import com.beyondphysics.ui.views.NetworkGifImageView;

public class MainActivity extends NewBaseActivity {

    private int activity_main_networkGifImageView_width;
    private Button buttonWaterfallFlow;
    private Button buttonGifList;
    private Button buttonGallery;
    private Button buttonDownload;
    private Button buttonDownloadList;
    private Button buttonUpload;
    private Button buttonClear;
    private Button buttonTest;
    private NetworkGifImageView networkGifImageView;
    private NetworkGifImageView networkGifImageView1;
    private View.OnClickListener onClickListener;
    private int testPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAll();
    }

    @Override
    protected void initHandler() {
    }

    @Override
    protected void initUi() {
        activity_main_networkGifImageView_width = getResources().getDimensionPixelSize(R.dimen.activity_main_networkGifImageView_width);

        buttonWaterfallFlow = (Button) findViewById(R.id.buttonWaterfallFlow);
        buttonGifList = (Button) findViewById(R.id.buttonGifList);
        buttonGallery = (Button) findViewById(R.id.buttonGallery);
        buttonDownload = (Button) findViewById(R.id.buttonDownload);
        buttonDownloadList = (Button) findViewById(R.id.buttonDownloadList);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonTest = (Button) findViewById(R.id.buttonTest);
        networkGifImageView = (NetworkGifImageView) findViewById(R.id.networkGifImageView);
        networkGifImageView1 = (NetworkGifImageView) findViewById(R.id.networkGifImageView1);

        onClickListener();
    }

    @Override
    protected void initConfigUi() {

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
                    case R.id.buttonWaterfallFlow:
                        Intent intentWaterfallFlowActivity = new Intent(MainActivity.this,
                                WaterfallFlowActivity.class);
                        startActivity(intentWaterfallFlowActivity);
                        break;
                    case R.id.buttonGifList:
                        Intent intentGifListActivity = new Intent(MainActivity.this,
                                GifListActivity.class);
                        startActivity(intentGifListActivity);
                        break;
                    case R.id.buttonGallery:
                        Intent intentPictureChooseActivity = new Intent(MainActivity.this,
                                PictureChooseActivity.class);
                        startActivity(intentPictureChooseActivity);
                        break;
                    case R.id.buttonDownload:
                        Intent intentBreakpointDownloadActivity = new Intent(MainActivity.this,
                                BreakpointDownloadActivity.class);
                        startActivity(intentBreakpointDownloadActivity);
                        break;
                    case R.id.buttonDownloadList:
                        Intent intentBreakpointDownloadListActivity = new Intent(MainActivity.this,
                                BreakpointDownloadListActivity.class);
                        startActivity(intentBreakpointDownloadListActivity);
                        break;
                    case R.id.buttonUpload:
                        Intent intentUploadActivity = new Intent(MainActivity.this,
                                UploadActivity.class);
                        startActivity(intentUploadActivity);
                        break;
                    case R.id.buttonClear:
                        BeyondPhysicsManager.getInstance(MainActivity.this).clearAllCacheItem();
                        BaseActivity.showShortToast(MainActivity.this, "清除缓存");
                        break;
                    case R.id.buttonTest:
                        if (testPosition > 1) {
                            testPosition = 0;
                        }
                        if (testPosition == 0) {
                            buttonTest.setText(getResources().getString(R.string.activity_main_buttonTest) + "0");
                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView, getBitmapRequestParams("http://server.52wallpaper.com:4126/perfectwallpaper_files/specifys/randomAvatar1.png", activity_main_networkGifImageView_width, activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView1, getBitmapRequestParams("http://server.52wallpaper.com:4126/perfectwallpaper_files/specifys/randomAvatar2.png", activity_main_networkGifImageView_width, activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
                        } else if (testPosition == 1) {
                            buttonTest.setText(getResources().getString(R.string.activity_main_buttonTest) + "1");
                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView, getBitmapRequestParams("http://server.52wallpaper.com:4126/perfectwallpaper_files/specifys/randomAvatar2.png", activity_main_networkGifImageView_width, activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView1, getBitmapRequestParams("http://server.52wallpaper.com:4126/perfectwallpaper_files/specifys/randomAvatar1.png", activity_main_networkGifImageView_width, activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
                        }
//                        if (testPosition == 0) {
//                            buttonTest.setText(getResources().getString(R.string.activity_main_buttonTest) + "0");
//                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView, getBitmapRequestParams(NetworkImageViewHelp.getDiskCacheUrlString("b1.gif",NetworkImageViewHelp.DISKCACHETYPE_ASSETS),activity_main_networkGifImageView_width,activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
//                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView1, getBitmapRequestParams(NetworkImageViewHelp.getDiskCacheUrlString("b2.gif",NetworkImageViewHelp.DISKCACHETYPE_ASSETS),activity_main_networkGifImageView_width,activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
//                        } else if (testPosition == 1) {
//                            buttonTest.setText(getResources().getString(R.string.activity_main_buttonTest) + "1");
//                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView, getBitmapRequestParams(NetworkImageViewHelp.getDiskCacheUrlString(String.valueOf(R.mipmap.b2)+".gif",NetworkImageViewHelp.DISKCACHETYPE_RESOURCE),activity_main_networkGifImageView_width,activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
//                            NetworkGifImageViewHelp.getImageByBitmapRequestParams(networkGifImageView1, getBitmapRequestParams(NetworkImageViewHelp.getDiskCacheUrlString(String.valueOf(R.mipmap.b1)+".gif",NetworkImageViewHelp.DISKCACHETYPE_RESOURCE),activity_main_networkGifImageView_width,activity_main_networkGifImageView_width), R.mipmap.normal_loading, R.mipmap.normal_loading_error);
//                        }
                        testPosition = testPosition + 1;
                        break;

                    default:
                        break;
                }
            }
        };
        buttonWaterfallFlow.setOnClickListener(onClickListener);
        buttonGifList.setOnClickListener(onClickListener);
        buttonGallery.setOnClickListener(onClickListener);
        buttonDownload.setOnClickListener(onClickListener);
        buttonDownloadList.setOnClickListener(onClickListener);
        buttonUpload.setOnClickListener(onClickListener);
        buttonClear.setOnClickListener(onClickListener);
        buttonTest.setOnClickListener(onClickListener);
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }


    private BitmapRequest_Default_Params getBitmapRequestParams(String urlString, int width, int height) {
        BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
        bitmapRequest_Default_Params.setUrlString(urlString);
        bitmapRequest_Default_Params.setTag(activityKey);
        bitmapRequest_Default_Params.setWidth(width);
        bitmapRequest_Default_Params.setHeight(height);
        bitmapRequest_Default_Params.setCacheInDisk(false);
        bitmapRequest_Default_Params.setCacheInMemory(false);
        bitmapRequest_Default_Params.setContext(MainActivity.this);
        bitmapRequest_Default_Params.setDecodeGif(true);
        return bitmapRequest_Default_Params;
    }

}

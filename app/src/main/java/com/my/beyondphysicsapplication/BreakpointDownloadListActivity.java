package com.my.beyondphysicsapplication;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.beyondphysics.network.CacheItemRequest;
import com.beyondphysics.network.Request;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.cache.CacheItem;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.my.adapters.recyclerviewadapter.BreakpointDownloadListActivity_RecyclerViewAdapter;
import com.my.modelhttpfunctions.WaterfallFlowHttpFunction;
import com.my.models.Wallpaper;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.MainActivity_Home_GetWallpaper_Default_GsonModel;
import com.my.utils.HttpConnectTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BreakpointDownloadListActivity extends NewBaseActivity {

    private ImageView imageViewBack;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private BreakpointDownloadListActivity_RecyclerViewAdapter breakpointDownloadListActivity_RecyclerViewAdapter;

    private String kind = Wallpaper.kind_image;

    private View.OnClickListener onClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakpoint_download_list);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
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
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
    }

    private void initBaseRecyclerViewFromFrameLayout() {
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInitBySort();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }

    protected void getInitBySort() {
        WaterfallFlowHttpFunction.mainActivity_home_getWallpaperBySort(BreakpointDownloadListActivity.this, kind, getSortType(), "-1", getScreenNumber(), new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                initOrRefreshAdapter(null);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<MainActivity_Home_GetWallpaper_Default_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(BreakpointDownloadListActivity.this, error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(BreakpointDownloadListActivity.this, tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(MainActivity_Home_GetWallpaper_Default_GsonModel.Data data) {
                if (data == null || data.getWallpapers() == null) {
                    BaseActivity.showShortToast(BreakpointDownloadListActivity.this, TheApplication.SERVERERROR);
                } else {
                    List<Wallpaper> wallpapers = data.getWallpapers();
                    doCacheItemsWithCallback(wallpapers, true);
                }
            }
        });

    }

    protected void getNextBySort(Wallpaper wallpaper) {
        WaterfallFlowHttpFunction.mainActivity_home_getWallpaperBySort(BreakpointDownloadListActivity.this, kind, getSortType(), String.valueOf(wallpaper.getVisitSort()), getScreenNumber(), new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreError();
                    }
                }, TheApplication.LOADMOREERRORDELAY);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<MainActivity_Home_GetWallpaper_Default_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(BreakpointDownloadListActivity.this, error);
                loadMoreError();
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(BreakpointDownloadListActivity.this, tips);
                loadMoreError();
            }

            @Override
            public void success(MainActivity_Home_GetWallpaper_Default_GsonModel.Data data) {
                if (data == null || data.getWallpapers() == null) {
                    BaseActivity.showShortToast(BreakpointDownloadListActivity.this, TheApplication.SERVERERROR);
                } else {
                    List<Wallpaper> wallpapers = data.getWallpapers();
                    doCacheItemsWithCallback(wallpapers, false);
                }
            }
        });

    }


    private String getScreenNumber() {
        return "-1";
    }

    private String getSortType() {
        return Wallpaper.SORTTYPE_VISITSORT;
    }

    private void loadMoreError() {
        TheApplication.loadMoreErrorFormBaseRecyclerViewAdapter(breakpointDownloadListActivity_RecyclerViewAdapter);
    }


    private void doCacheItemsWithCallback(final List<Wallpaper> wallpapers, final boolean init) {
        final List<ViewItem> viewItems = new ArrayList<ViewItem>();
        if (wallpapers == null) {
            BaseActivity.showShortToast(BreakpointDownloadListActivity.this, TheApplication.SERVERERROR);
            if (init) {
                initOrRefreshAdapter(viewItems);
            }
        } else {
            final List<String> urls = new ArrayList<String>();
            List<String> requestKeys = new ArrayList<String>();
            for (int i = 0; i < wallpapers.size(); i++) {
                Wallpaper wallpaper = wallpapers.get(i);
                if (wallpaper != null) {
                    urls.add(wallpaper.getWallpaperUrl_absolute());
                    requestKeys.add(RequestManager.getRequestKey(wallpaper.getWallpaperUrl_absolute(), Request.GET));
                }
            }
            CacheItemRequest cacheItemRequest = new CacheItemRequest(CacheItemRequest.GETCACHEITEMS, activityKey, requestKeys, RequestManager.CacheItemTAG.BREAKPOINTDOWNLOADREQUEST, new CacheItemRequest.OnCacheItemListener() {
                @Override
                public void onSuccessResponse(List<CacheItem> cacheItems) {
                    if (breakpointDownloadListActivity_RecyclerViewAdapter != null) {
                        breakpointDownloadListActivity_RecyclerViewAdapter.convertWallpapers(wallpapers);
                    }
                    if (cacheItems != null && wallpapers.size() == cacheItems.size()) {
                        for (int i = 0; i < wallpapers.size(); i++) {
                            Wallpaper wallpaper = wallpapers.get(i);
                            CacheItem cacheItem = cacheItems.get(i);
                            setProgress(wallpaper, cacheItem, urls.get(i));
                            viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, wallpaper));
                        }
                    }
                    if (init) {
                        initOrRefreshAdapter(viewItems);
                    } else {
                        TheApplication.addAllFormBaseRecyclerViewAdapter(breakpointDownloadListActivity_RecyclerViewAdapter, viewItems);
                    }
                }

                @Override
                public void onErrorResponse(String error) {
                    BaseActivity.showShortToast(BreakpointDownloadListActivity.this, error);
                }
            });
            BeyondPhysicsManager.getInstance(BreakpointDownloadListActivity.this).doCacheItemsWithCallback(cacheItemRequest);
        }
    }


    private void setProgress(Wallpaper wallpaper, CacheItem cacheItem, String url) {
        if (wallpaper != null && cacheItem != null) {
            int contentLength = cacheItem.getContentLength();
            String savePath = BreakpointDownloadActivity.getSavePath(url);
            if (savePath != null) {
                File file = new File(savePath);
                int beginAddress = 0;
                if (file.exists() && file.isFile()) {
                    beginAddress = (int) file.length();
                }
                wallpaper.setCurrentSize(beginAddress);
                wallpaper.setTotalSize(contentLength);
                if (beginAddress == contentLength) {
                    wallpaper.setDownloadingStatus(Wallpaper.DOWNLOADSUCCESS);
                }
            }
        }
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (breakpointDownloadListActivity_RecyclerViewAdapter == null) {
            breakpointDownloadListActivity_RecyclerViewAdapter = new BreakpointDownloadListActivity_RecyclerViewAdapter(BreakpointDownloadListActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems, R.layout.normal_more_progress, HttpConnectTool.pageSize);
            breakpointDownloadListActivity_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewItem viewItem) {
                    if (viewItem == null || viewItem.getModel() == null) {
                        BaseActivity.showShortToast(BreakpointDownloadListActivity.this, TheApplication.SERVERERROR);
                    } else {
                        final Wallpaper wallpaper = (Wallpaper) (viewItem.getModel());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNextBySort(wallpaper);
                            }
                        }, TheApplication.LOADMOREDELAY);
                    }
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(breakpointDownloadListActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(breakpointDownloadListActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }

    }


    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }
}

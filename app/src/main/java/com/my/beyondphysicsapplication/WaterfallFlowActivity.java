package com.my.beyondphysicsapplication;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.adapters.recyclerviewadapter.WaterfallFlowActivity_RecyclerViewAdapter;
import com.my.modelhttpfunctions.WaterfallFlowHttpFunction;
import com.my.models.Wallpaper;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.MainActivity_Home_GetWallpaper_Default_GsonModel;
import com.my.utils.HttpConnectTool;
import com.my.utils.RecyclerViewItemDecorationDefault;

import java.util.ArrayList;
import java.util.List;


public class WaterfallFlowActivity extends NewBaseActivity {
    public static final String FROM_KEY = "from_key";
    public static final String FROM_MAINACTIVITY = "from_mainActivity";
    public static final String FROM_UPLOADACTIVITY = "from_uploadActivity";

    public static final String SCROLL_MODE_KEY = "scroll_mode_key";
    public static final int SCROLL_UNLOAD = 1;

    private String from;
    private int scroll_mode;

    private ImageView imageViewBack;
    private TextView textViewToolbarMode;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private WaterfallFlowActivity_RecyclerViewAdapter waterfallFlowActivity_RecyclerViewAdapter;

    private String kind = Wallpaper.kind_image;

    private View.OnClickListener onClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterfallflow);
        Intent intent = getIntent();
        from = intent.getStringExtra(FROM_KEY);
        scroll_mode = intent.getIntExtra(SCROLL_MODE_KEY, 0);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewToolbarMode = (TextView) findViewById(R.id.textViewToolbarMode);

        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        if (scroll_mode != SCROLL_UNLOAD) {
            textViewToolbarMode.setText("切换成滑动不加载");
        } else {
            textViewToolbarMode.setText("切换成滑动加载");
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
                    case R.id.textViewToolbarMode:
                        Intent intentWaterfallFlowActivity = new Intent(WaterfallFlowActivity.this,
                                WaterfallFlowActivity.class);
                        if (scroll_mode != SCROLL_UNLOAD) {
                            intentWaterfallFlowActivity.putExtra(SCROLL_MODE_KEY, SCROLL_UNLOAD);
                        }
                        startActivity(intentWaterfallFlowActivity);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        textViewToolbarMode.setOnClickListener(onClickListener);

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        baseRecyclerViewFromFrameLayout.getRecyclerView().addItemDecoration(new RecyclerViewItemDecorationDefault(WaterfallFlowActivity.this, getSpanCount()));
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (from == null || from.equals(FROM_MAINACTIVITY)) {
                    getInitBySort();
                } else {
                    getInitByCreateTime();
                }
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        if (scroll_mode == SCROLL_UNLOAD) {
            baseRecyclerViewFromFrameLayout.openBitmapScrollOptimization(WaterfallFlowActivity.this);
        }
    }

    private void getInitByCreateTime() {
        WaterfallFlowHttpFunction.mainActivity_home_getWallpaperByCreateTime(WaterfallFlowActivity.this, kind, "-1", getScreenNumber(), new Request.OnResponseListener<String>() {
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
                BaseActivity.showShortToast(WaterfallFlowActivity.this, error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(WaterfallFlowActivity.this, tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(MainActivity_Home_GetWallpaper_Default_GsonModel.Data data) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (data == null || data.getWallpapers() == null) {
                    BaseActivity.showShortToast(WaterfallFlowActivity.this, TheApplication.SERVERERROR);
                } else {
                    List<Wallpaper> wallpapers = data.getWallpapers();
                    for (int i = 0; i < wallpapers.size(); i++) {
                        viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, wallpapers.get(i)));
                    }
                }
                initOrRefreshAdapter(viewItems);
            }
        });
    }

    private void getNextByCreateTime(Wallpaper wallpaper) {
        WaterfallFlowHttpFunction.mainActivity_home_getWallpaperByCreateTime(WaterfallFlowActivity.this, kind, String.valueOf(wallpaper.getCreateTime()), getScreenNumber(), new Request.OnResponseListener<String>() {
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
                BaseActivity.showShortToast(WaterfallFlowActivity.this, error);
                loadMoreError();
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(WaterfallFlowActivity.this, tips);
                loadMoreError();
            }

            @Override
            public void success(MainActivity_Home_GetWallpaper_Default_GsonModel.Data data) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (data == null || data.getWallpapers() == null) {
                    BaseActivity.showShortToast(WaterfallFlowActivity.this, TheApplication.SERVERERROR);
                } else {
                    List<Wallpaper> wallpapers = data.getWallpapers();
                    for (int i = 0; i < wallpapers.size(); i++) {
                        viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, wallpapers.get(i)));
                    }
                }
                TheApplication.addAllFormBaseRecyclerViewAdapter(waterfallFlowActivity_RecyclerViewAdapter, viewItems);
            }
        });

    }


    private void getInitBySort() {
        WaterfallFlowHttpFunction.mainActivity_home_getWallpaperBySort(WaterfallFlowActivity.this, kind, getSortType(), "-1", getScreenNumber(), new Request.OnResponseListener<String>() {
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
                BaseActivity.showShortToast(WaterfallFlowActivity.this, error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(WaterfallFlowActivity.this, tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(MainActivity_Home_GetWallpaper_Default_GsonModel.Data data) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (data == null || data.getWallpapers() == null) {
                    BaseActivity.showShortToast(WaterfallFlowActivity.this, TheApplication.SERVERERROR);
                } else {
                    List<Wallpaper> wallpapers = data.getWallpapers();
                    for (int i = 0; i < wallpapers.size(); i++) {
                        viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, wallpapers.get(i)));
                    }
                }
                initOrRefreshAdapter(viewItems);
            }
        });

    }

    private void getNextBySort(Wallpaper wallpaper) {
        WaterfallFlowHttpFunction.mainActivity_home_getWallpaperBySort(WaterfallFlowActivity.this, kind, getSortType(), String.valueOf(wallpaper.getVisitSort()), getScreenNumber(), new Request.OnResponseListener<String>() {
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
                BaseActivity.showShortToast(WaterfallFlowActivity.this, error);
                loadMoreError();
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(WaterfallFlowActivity.this, tips);
                loadMoreError();
            }

            @Override
            public void success(MainActivity_Home_GetWallpaper_Default_GsonModel.Data data) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (data == null || data.getWallpapers() == null) {
                    BaseActivity.showShortToast(WaterfallFlowActivity.this, TheApplication.SERVERERROR);
                } else {
                    List<Wallpaper> wallpapers = data.getWallpapers();
                    for (int i = 0; i < wallpapers.size(); i++) {
                        viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, wallpapers.get(i)));
                    }
                }
                TheApplication.addAllFormBaseRecyclerViewAdapter(waterfallFlowActivity_RecyclerViewAdapter, viewItems);
            }
        });

    }


    private String getScreenNumber() {
        return "-1";
    }

    private int getSpanCount() {
        return 2;
    }

    private String getSortType() {
        return Wallpaper.SORTTYPE_VISITSORT;
    }

    private void loadMoreError() {
        TheApplication.loadMoreErrorFormBaseRecyclerViewAdapter(waterfallFlowActivity_RecyclerViewAdapter);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (waterfallFlowActivity_RecyclerViewAdapter == null) {
            waterfallFlowActivity_RecyclerViewAdapter = new WaterfallFlowActivity_RecyclerViewAdapter(WaterfallFlowActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), getSpanCount(), viewItems, R.layout.normal_more_progress, HttpConnectTool.pageSize);
            waterfallFlowActivity_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewItem viewItem) {
                    if (viewItem == null || viewItem.getModel() == null) {
                        BaseActivity.showShortToast(WaterfallFlowActivity.this, TheApplication.SERVERERROR);
                    } else {
                        final Wallpaper wallpaper = (Wallpaper) (viewItem.getModel());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (from == null || from.equals(FROM_MAINACTIVITY)) {
                                    getNextBySort(wallpaper);
                                } else {
                                    getNextByCreateTime(wallpaper);
                                }
                            }
                        }, TheApplication.LOADMOREDELAY);
                    }
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(waterfallFlowActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(waterfallFlowActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }

    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }
}

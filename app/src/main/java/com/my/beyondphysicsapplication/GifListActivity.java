package com.my.beyondphysicsapplication;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.adapters.recyclerviewadapter.GifListActivity_RecyclerViewAdapter;
import com.my.utils.HttpConnectTool;
import com.my.utils.RecyclerViewItemDecorationDefault;

import java.util.ArrayList;
import java.util.List;


public class GifListActivity extends NewBaseActivity {

    private ImageView imageViewBack;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private GifListActivity_RecyclerViewAdapter gifListActivity_RecyclerViewAdapter;

    private View.OnClickListener onClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giflist);
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
        baseRecyclerViewFromFrameLayout.getRecyclerView().addItemDecoration(new RecyclerViewItemDecorationDefault(GifListActivity.this, 2));
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInit();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }

    private void getInit() {
        List<ViewItem> viewItems = new ArrayList<ViewItem>();
        for (int i = 1; i <= HttpConnectTool.pageSize; i++) {
            String string = "http://server.52wallpaper.com:4126/perfectwallpaper_files/specifys/gif/" + i + ".gif";
            viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, string));
        }
        initOrRefreshAdapter(viewItems);
    }

    private void getNext() {
        List<ViewItem> viewItems = new ArrayList<ViewItem>();
        for (int i = 1; i <= HttpConnectTool.pageSize; i++) {
            String string = "http://server.52wallpaper.com:4126/perfectwallpaper_files/specifys/gif/" + i + ".gif";
            viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, string));
        }
        TheApplication.addAllFormBaseRecyclerViewAdapter(gifListActivity_RecyclerViewAdapter, viewItems);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (gifListActivity_RecyclerViewAdapter == null) {
            gifListActivity_RecyclerViewAdapter = new GifListActivity_RecyclerViewAdapter(GifListActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), 2, viewItems, R.layout.normal_more_progress, HttpConnectTool.pageSize);
            gifListActivity_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewItem viewItem) {
                    if (viewItem == null) {
                        BaseActivity.showShortToast(GifListActivity.this, TheApplication.SERVERERROR);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNext();
                            }
                        }, TheApplication.LOADMOREDELAY);
                    }
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(gifListActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(gifListActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }

    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }
}

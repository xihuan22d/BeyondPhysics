package com.my.beyondphysicsapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.adapters.recyclerviewadapter.WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.beyondphysicsapplication.WallpaperDetailsActivity;
import com.my.modelhttpfunctions.WallpaperDetailsActivityHttpFunction;
import com.my.models.Wallpaper;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.WallpaperDetailsActivity_GetWallpaperDetails_GsonModel;
import com.my.utils.HttpConnectTool;
import com.my.utils.RecyclerViewItemDecorationDefault;

import java.util.ArrayList;
import java.util.List;


public class WallpaperDetailsActivity_Fragment_Details extends BaseFragment {
    private String wallpaper_id;

    private View view;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private boolean alreadyInitUi = false;

    private WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter;

    private String kind = Wallpaper.kind_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wallpaper_details_fragment_details, container, false);
        Bundle bundle = getArguments();
        wallpaper_id = bundle.getString(WallpaperDetailsActivity.WALLPAPER_ID_KEY);
        initAll();
        return view;
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) view.findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        alreadyInitUi = true;
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


    private void initBaseRecyclerViewFromFrameLayout() {
        baseRecyclerViewFromFrameLayout.getRecyclerView().addItemDecoration(new RecyclerViewItemDecorationDefault(getBaseActivity(), getSpanCount()));

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
        WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_getWallpaperDetails(getBaseActivity(), wallpaper_id, kind, "-1", new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                initOrRefreshAdapter(null);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(getBaseActivity(), error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(getBaseActivity(), tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getWallpaper() == null) {
                    BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
                } else {
                    Wallpaper wallpaper = data.getWallpaper();
                    WallpaperDetailsActivity wallpaperDetailsActivity = (WallpaperDetailsActivity) getBaseActivity();
                    wallpaperDetailsActivity.updateInformation(wallpaper.getPreviewImageUrl_absolute(), wallpaper.getCommentCount());
                    convertToViewLayerItems(data, data.getRecommendWallpapers(), viewLayerItems);
                }
                initOrRefreshAdapter(viewLayerItems);
            }
        });

    }

    private void getNext(Wallpaper wallpaper) {
        WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_getWallpaperDetails(getBaseActivity(), wallpaper_id, kind, String.valueOf(wallpaper.getCreateTime()), new Request.OnResponseListener<String>() {
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
        }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(getBaseActivity(), error);
                loadMoreError();
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(getBaseActivity(), tips);
                loadMoreError();
            }

            @Override
            public void success(WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null) {
                    BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
                } else {
                    convertToViewLayerItems(null, data.getRecommendWallpapers(), viewLayerItems);
                }
                TheApplication.addAllFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter, viewLayerItems);
            }
        });
    }

    private int getSpanCount() {
        return 2;
    }

    private void loadMoreError() {
        TheApplication.loadMoreErrorFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter);
    }


    private List<ViewLayerItem> convertToViewLayerItems(WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data data, List<Wallpaper> recommendWallpapers, List<ViewLayerItem> viewLayerItems) {
        if (viewLayerItems == null) {
            return new ArrayList<ViewLayerItem>();
        }
        if (data != null && data.getWallpaper() != null) {
            viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, data, 2, null));
        }
        if (recommendWallpapers != null) {
            for (int i = 0; i < recommendWallpapers.size(); i++) {
                Wallpaper recommendWallpaper = recommendWallpapers.get(i);
                viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2, recommendWallpaper, ViewLayerItem.LAYER_ROOT, recommendWallpaper));
            }
        }

        return viewLayerItems;
    }

    private void initOrRefreshAdapter(List<ViewLayerItem> viewLayerItems) {
        if (viewLayerItems == null) {
            viewLayerItems = new ArrayList<ViewLayerItem>();
        }
        if (wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter == null) {
            wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter = new WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter(getBaseActivity(), baseRecyclerViewFromFrameLayout.getRecyclerView(), getSpanCount(), viewLayerItems, R.layout.normal_more_progress, HttpConnectTool.pageSize);
            wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreLayerRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewLayerItem viewLayerItem) {
                    if (viewLayerItem == null || viewLayerItem.getRootModel() == null) {
                        BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
                    } else {
                        final Wallpaper wallpaper = (Wallpaper) (viewLayerItem.getRootModel());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNext(wallpaper);
                            }
                        }, TheApplication.LOADMOREDELAY);
                    }
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter, viewLayerItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }

    }

    public void doRefresh() {
        if (alreadyInitUi) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

}
package com.my.adapters.recyclerviewadapter;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beyondphysics.network.BreakpointDownloadRequest;
import com.beyondphysics.network.BreakpointDownloadRequest_Default;
import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.my.beyondphysicsapplication.BreakpointDownloadActivity;
import com.my.beyondphysicsapplication.R;
import com.my.models.Wallpaper;

import java.util.HashMap;
import java.util.List;


public class BreakpointDownloadListActivity_RecyclerViewAdapter extends LoadMoreRecyclerViewAdapter {

    private HashMap<String, Wallpaper> hashMapActiveWallpapers = new HashMap<String, Wallpaper>();
    private BaseActivity baseActivity;
    private RecyclerView recyclerView;

    public BreakpointDownloadListActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas) {
        super(datas);
        init(baseActivity, recyclerView);
    }

    public BreakpointDownloadListActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas, int layoutId) {
        super(datas, layoutId);
        init(baseActivity, recyclerView);
    }

    public BreakpointDownloadListActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas, int layoutId, int pageCount) {
        super(datas, layoutId, pageCount);
        init(baseActivity, recyclerView);
    }

    private void init(BaseActivity baseActivity, RecyclerView recyclerView) {
        this.baseActivity = baseActivity;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.activity_breakpoint_download_list_item;
        } else {
            return com.beyondphysics.R.layout.beyondphysics_recyclerview_lostviewtype_item;
        }
    }

    @Override
    public void onBindNormalViewHolder(final BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {//api13以及以下的recyclerView复用的view会有不显示的bug,不过不确定'com.android.support:recyclerview-v7:23.2.0'以上是否修复了bug
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    holder.itemView.setAlpha(1.0f);
                }
            });
        }
        ViewItem viewItem = getDatas().get(position);
        if (viewItem != null) {
            if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
                final Wallpaper wallpaper = (Wallpaper) viewItem.getModel();
                if (wallpaper != null && wallpaper.get_id() != null) {
                    final String url = wallpaper.getWallpaperUrl_absolute();
                    final Button buttonDownload = (Button) holder.getView(R.id.buttonDownload);
                    ProgressBar progressBar = (ProgressBar) holder.getView(R.id.progressBar);
                    TextView textViewProgress = (TextView) holder.getView(R.id.textViewProgress);
                    holder.setText(R.id.textViewUrl, url);
                    int currentSize = wallpaper.getCurrentSize();
                    int totalSize = wallpaper.getTotalSize();
                    if (totalSize > 0) {
                        progressBar.setMax(totalSize);
                        progressBar.setProgress(currentSize);
                        int x = (int) ((long) currentSize * 100 / totalSize);
                        textViewProgress.setText(x + "%");
                    } else {
                        progressBar.setMax(totalSize);
                        progressBar.setProgress(0);
                        textViewProgress.setText(0 + "%");
                    }
                    if (wallpaper.getDownloadingStatus() == Wallpaper.UNDOWNLOADING) {
                        buttonDownload.setBackgroundResource(R.drawable.normal_solid_background);
                        buttonDownload.setText("开始下载");
                    } else if (wallpaper.getDownloadingStatus() == Wallpaper.DOWNLOADING) {
                        buttonDownload.setBackgroundResource(R.drawable.normal_stroke_background);
                        buttonDownload.setText("暂停下载");
                    } else {
                        buttonDownload.setBackgroundResource(R.drawable.normal_stroke_background1);
                        buttonDownload.setText("下载完毕");
                    }
                    buttonDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (wallpaper.getDownloadingStatus() == Wallpaper.DOWNLOADING) {
                                buttonDownload.setBackgroundResource(R.drawable.normal_solid_background);
                                buttonDownload.setText("开始下载");
                                wallpaper.setDownloadingStatus(Wallpaper.UNDOWNLOADING);
                                BreakpointDownloadRequest<?> breakpointDownloadRequest = (BreakpointDownloadRequest<?>) wallpaper.getBreakpointDownloadRequest();
                                if (breakpointDownloadRequest != null) {
                                    BeyondPhysicsManager.getInstance(baseActivity).cancelRequestWithRequest(breakpointDownloadRequest, true);
                                    wallpaper.setBreakpointDownloadRequest(null);
                                }
                                putActiveWallpaper(wallpaper);
                            } else if (wallpaper.getDownloadingStatus() == Wallpaper.UNDOWNLOADING) {
                                buttonDownload.setBackgroundResource(R.drawable.normal_stroke_background);
                                buttonDownload.setText("暂停下载");
                                wallpaper.setDownloadingStatus(Wallpaper.DOWNLOADING);
                                goToDownload(wallpaper, url);
                                putActiveWallpaper(wallpaper);
                            } else {
                                BaseActivity.showShortToast(baseActivity, "该文件下载已完毕");
                            }
                        }
                    });
                    holder.itemView.setTag(new ViewHolderTag(wallpaper.get_id(), holder));
                }


            }
        }
    }

    private void putActiveWallpaper(Wallpaper wallpaper) {
        if (wallpaper != null && wallpaper.get_id() != null) {
            hashMapActiveWallpapers.put(wallpaper.get_id(), wallpaper);
        }
    }


    private Wallpaper getActiveWallpaper(Wallpaper wallpaper) {
        if (wallpaper == null || wallpaper.get_id() == null) {
            return null;
        }
        if (hashMapActiveWallpapers.containsKey(wallpaper.get_id())) {
            return hashMapActiveWallpapers.get(wallpaper.get_id());
        } else {
            return null;
        }
    }

    private BaseRecyclerViewHolder getViewHolderIfVisible(RecyclerView recyclerView, String tag) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
        for (int i = firstVisibleItemPosition; i <= lastVisiblePosition; i++) {
            View itemView = recyclerView.getChildAt(i - firstVisibleItemPosition);
            if (itemView != null) {
                ViewHolderTag viewHolderTag = (ViewHolderTag) itemView.getTag();
                if (viewHolderTag != null) {
                    String theTag = (String) viewHolderTag.getTag();
                    if (tag != null && theTag != null && tag.equals(theTag)) {
                        return viewHolderTag.getBaseRecyclerViewHolder();
                    }
                }
            }
        }
        return null;
    }

    private Wallpaper findWallpaperByTag(String tag) {
        if (tag == null) {
            return null;
        }
        List<ViewItem> viewItems = getDatas();
        for (int i = 0; i < viewItems.size(); i++) {
            ViewItem viewItem = viewItems.get(i);
            if (viewItem != null && viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
                Wallpaper wallpaper = (Wallpaper) viewItem.getModel();
                if (wallpaper != null && wallpaper.get_id() != null && tag.equals(wallpaper.get_id())) {
                    return wallpaper;
                }
            }
        }
        return null;
    }

    private void goToDownload(Wallpaper wallpaper, String url) {
        final String savePath = BreakpointDownloadActivity.getSavePath(url);
        if (wallpaper == null || savePath == null) {
            return;
        }
        final String tag = wallpaper.get_id();
        BreakpointDownloadRequest<?> breakpointDownloadRequest = new BreakpointDownloadRequest_Default(url, savePath, -1, baseActivity.activityKey, new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
                BaseRecyclerViewHolder baseRecyclerViewHolder = getViewHolderIfVisible(recyclerView, tag);
                if (baseRecyclerViewHolder != null) {
                    ProgressBar progressBar = (ProgressBar) baseRecyclerViewHolder.getView(R.id.progressBar);
                    TextView textViewProgress = (TextView) baseRecyclerViewHolder.getView(R.id.textViewProgress);
                    progressBar.setProgress(progressBar.getMax());
                    textViewProgress.setText("100%");
                    Button buttonDownload = (Button) baseRecyclerViewHolder.getView(R.id.buttonDownload);
                    buttonDownload.setBackgroundResource(R.drawable.normal_stroke_background1);
                    buttonDownload.setText("下载完毕");
                }
                Wallpaper wallpaper = findWallpaperByTag(tag);
                if (wallpaper != null) {
                    wallpaper.setDownloadingStatus(Wallpaper.DOWNLOADSUCCESS);
                    wallpaper.setBreakpointDownloadRequest(null);
                }
                BaseActivity.showShortToast(baseActivity, response + "文件位于" + savePath);
                putActiveWallpaper(wallpaper);
            }

            @Override
            public void onErrorResponse(String error) {
                BaseActivity.showShortToast(baseActivity, error);
            }

        }, 10, null, 8000, 22000, new BreakpointDownloadRequest.OnDownloadProgressListener() {
            @Override
            public void maxProgress(BreakpointDownloadRequest<?> breakpointDownloadRequest, int totalSize) {
                BaseRecyclerViewHolder baseRecyclerViewHolder = getViewHolderIfVisible(recyclerView, tag);
                if (baseRecyclerViewHolder != null) {
                    ProgressBar progressBar = (ProgressBar) baseRecyclerViewHolder.getView(R.id.progressBar);
                    progressBar.setMax(totalSize);
                }
                Wallpaper wallpaper = findWallpaperByTag(tag);
                if (wallpaper != null) {
                    wallpaper.setTotalSize(totalSize);
                }
                putActiveWallpaper(wallpaper);
            }

            @Override
            public void updateProgress(BreakpointDownloadRequest<?> breakpointDownloadRequest, int currentSize, int totalSize) {
                BaseRecyclerViewHolder baseRecyclerViewHolder = getViewHolderIfVisible(recyclerView, tag);
                if (baseRecyclerViewHolder != null) {
                    ProgressBar progressBar = (ProgressBar) baseRecyclerViewHolder.getView(R.id.progressBar);
                    TextView textViewProgress = (TextView) baseRecyclerViewHolder.getView(R.id.textViewProgress);
                    progressBar.setMax(totalSize);
                    progressBar.setProgress(currentSize);
                    int x = (int) ((long) currentSize * 100 / totalSize);
                    textViewProgress.setText(x + "%");
                }
                Wallpaper wallpaper = findWallpaperByTag(tag);
                if (wallpaper != null) {
                    wallpaper.setCurrentSize(currentSize);
                    wallpaper.setTotalSize(totalSize);
                }
                putActiveWallpaper(wallpaper);
            }
        });
        BeyondPhysicsManager.getInstance(baseActivity).addRequest(breakpointDownloadRequest);
        wallpaper.setBreakpointDownloadRequest(breakpointDownloadRequest);
    }

    public void convertWallpapers(List<Wallpaper> wallpapers) {
        if (wallpapers != null) {
            for (int i = 0; i < wallpapers.size(); i++) {
                Wallpaper wallpaper = wallpapers.get(i);
                convertWallpaper(wallpaper);
            }
        }
    }

    public void convertWallpaper(Wallpaper wallpaper) {
        if (wallpaper != null) {
            Wallpaper activeWallpaper = getActiveWallpaper(wallpaper);
            if (activeWallpaper != null) {
                wallpaper.setDownloadingStatus(activeWallpaper.getDownloadingStatus());
                wallpaper.setCurrentSize(activeWallpaper.getCurrentSize());
                wallpaper.setTotalSize(activeWallpaper.getTotalSize());
                wallpaper.setBreakpointDownloadRequest(activeWallpaper.getBreakpointDownloadRequest());
            }
        }
    }


    private class ViewHolderTag {
        private String tag;
        private BaseRecyclerViewHolder baseRecyclerViewHolder;

        public ViewHolderTag(String tag, BaseRecyclerViewHolder baseRecyclerViewHolder) {
            this.tag = tag;
            this.baseRecyclerViewHolder = baseRecyclerViewHolder;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public BaseRecyclerViewHolder getBaseRecyclerViewHolder() {
            return baseRecyclerViewHolder;
        }

        public void setBaseRecyclerViewHolder(BaseRecyclerViewHolder baseRecyclerViewHolder) {
            this.baseRecyclerViewHolder = baseRecyclerViewHolder;
        }

        @Override
        public String toString() {
            return "ViewHolderTag{" +
                    "tag='" + tag + '\'' +
                    ", baseRecyclerViewHolder=" + baseRecyclerViewHolder +
                    '}';
        }
    }

}

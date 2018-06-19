package com.beyondphysics.ui.recyclerviewlibrary.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.beyondphysics.R;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2016/3/8.
 */
public abstract class LoadMoreLayerRecyclerViewAdapter extends BaseRecyclerViewAdapter<ViewLayerItem> {
    public static final int USE_DEFAULT_LOADMORELAYOUTID = -1;

    public static final int USE_DEFAULT_LOADCOMPLETEDLAYOUTID = -1;
    public static final int UNSHOW_LOADCOMPLETEDLAYOUTID = 0;

    private final int loadMoreLayoutId;
    private final int pageCount;
    private final int loadCompletedLayoutId;
    private LoadMoreCallback loadMoreCallback;
    private boolean loading = false;
    private boolean loadCompleted = false;


    public LoadMoreLayerRecyclerViewAdapter(List<ViewLayerItem> datas) {
        super(datas);
        this.loadMoreLayoutId = R.layout.beyondphysics_layout_more_progress;
        pageCount = 25;
        loadCompletedLayoutId = R.layout.beyondphysics_layout_load_completed;
        if (getViewLayerItemsRootCount(getDatas()) >= pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }


    public LoadMoreLayerRecyclerViewAdapter(List<ViewLayerItem> datas, int loadMoreLayoutId) {
        super(datas);
        if (loadMoreLayoutId == USE_DEFAULT_LOADMORELAYOUTID) {
            loadMoreLayoutId = R.layout.beyondphysics_layout_more_progress;
        }
        this.loadMoreLayoutId = loadMoreLayoutId;
        pageCount = 25;
        loadCompletedLayoutId = R.layout.beyondphysics_layout_load_completed;
        if (getViewLayerItemsRootCount(getDatas()) >= pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public LoadMoreLayerRecyclerViewAdapter(List<ViewLayerItem> datas, int loadMoreLayoutId, int pageCount) {
        super(datas);
        if (loadMoreLayoutId == USE_DEFAULT_LOADMORELAYOUTID) {
            loadMoreLayoutId = R.layout.beyondphysics_layout_more_progress;
        }
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (pageCount <= 0) {
            pageCount = 25;
        }
        this.pageCount = pageCount;
        loadCompletedLayoutId = R.layout.beyondphysics_layout_load_completed;
        if (getViewLayerItemsRootCount(getDatas()) >= this.pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public LoadMoreLayerRecyclerViewAdapter(List<ViewLayerItem> datas, int loadMoreLayoutId, int pageCount, int loadCompletedLayoutId) {
        super(datas);
        if (loadMoreLayoutId == USE_DEFAULT_LOADMORELAYOUTID) {
            loadMoreLayoutId = R.layout.beyondphysics_layout_more_progress;
        }
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (pageCount <= 0) {
            pageCount = 25;
        }
        this.pageCount = pageCount;
        if (loadCompletedLayoutId == USE_DEFAULT_LOADCOMPLETEDLAYOUTID) {
            loadCompletedLayoutId = R.layout.beyondphysics_layout_load_completed;
        }
        this.loadCompletedLayoutId = loadCompletedLayoutId;
        if (getViewLayerItemsRootCount(getDatas()) >= this.pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public abstract int getNormalLayoutId(int viewType);

    public abstract void onBindNormalViewHolder(BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position);

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED) {
            return loadCompletedLayoutId;
        } else if (viewType == ViewItem.VIEW_TYPE_ITEM_LOAD_MORE) {
            return loadMoreLayoutId;
        } else {
            return getNormalLayoutId(viewType);
        }
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position) {
        List<ViewLayerItem> datas = getDatas();
        if (datas.get(position) != null) {
            if (datas.get(position).getViewType() == ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED) {
                ViewGroup.LayoutParams viewGroupLayoutParams = holder.itemView.getLayoutParams();
                if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
                    staggeredGridLayoutManagerLayoutParams.setFullSpan(true);
                }
            } else if (datas.get(position).getViewType() == ViewItem.VIEW_TYPE_ITEM_LOAD_MORE) {
                ViewGroup.LayoutParams viewGroupLayoutParams = holder.itemView.getLayoutParams();
                if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
                    staggeredGridLayoutManagerLayoutParams.setFullSpan(true);
                }
                if (position == datas.size() - 1 && !loadCompleted && !loading) {
                    loading = true;
                    if (loadMoreCallback != null) {
                        ViewLayerItem viewLayerItem = null;
                        if (position - 1 >= 0 && datas.get(position - 1) != null) {
                            viewLayerItem = datas.get(position - 1);
                        }
                        loadMoreCallback.loadMore(viewLayerItem);
                    }
                }
            } else {
                onBindNormalViewHolder(holder, position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        ViewLayerItem viewLayerItem = getDatas().get(position);
        if (viewLayerItem == null) {
            return -1;
        } else {
            return viewLayerItem.getViewType();
        }
    }


    @Override
    public void addAll(List<ViewLayerItem> newDatas) {
        super.addAll(convertAddAllDatas(newDatas, false));
        loading = false;
    }

    @Override
    public void addAllUseNotifyDataSetChanged(List<ViewLayerItem> newDatas) {
        super.addAllUseNotifyDataSetChanged(convertAddAllDatas(newDatas, true));
        loading = false;
    }

    @Override
    public void replaceAll(List<ViewLayerItem> newDatas) {
        super.replaceAll(convertReplaceAllDatas(newDatas));
        loading = false;
    }

    @Override
    public void replaceAllWithScrollTop(List<ViewLayerItem> newDatas, RecyclerView recyclerView) {
        super.replaceAllWithScrollTop(convertReplaceAllDatas(newDatas), recyclerView);
        loading = false;
    }

    @Override
    public void replaceAllUseNotifyDataSetChanged(List<ViewLayerItem> newDatas) {
        super.replaceAllUseNotifyDataSetChanged(convertReplaceAllDatas(newDatas));
        loading = false;
    }

    @Override
    public void replaceAllUseNotifyDataSetChangedWithScrollTop(List<ViewLayerItem> newDatas, RecyclerView recyclerView) {
        super.replaceAllUseNotifyDataSetChangedWithScrollTop(convertReplaceAllDatas(newDatas), recyclerView);
        loading = false;
    }


    public void loadMoreError() {
        hideLoadMore(false);
        if (getViewLayerItemsRootCount(getDatas()) >= pageCount) {
            List<ViewLayerItem> newDatas = new ArrayList<ViewLayerItem>();
            newDatas.add(getLoadMoreItem());
            super.addAll(newDatas);
        }
        loading = false;
    }

    public void loadMoreErrorUseNotifyDataSetChanged() {
        hideLoadMore(true);
        if (getViewLayerItemsRootCount(getDatas()) >= pageCount) {
            List<ViewLayerItem> newDatas = new ArrayList<ViewLayerItem>();
            newDatas.add(getLoadMoreItem());
            super.addAllUseNotifyDataSetChanged(newDatas);
        }
        loading = false;
    }


    private List<ViewLayerItem> convertAddAllDatas(List<ViewLayerItem> newDatas, boolean useNotifyDataSetChanged) {
        if (newDatas == null) {
            newDatas = new ArrayList<ViewLayerItem>();
        }
        hideLoadMore(useNotifyDataSetChanged);
        if (getViewLayerItemsRootCount(newDatas) < pageCount) {
            List<ViewLayerItem> datas = new ArrayList<ViewLayerItem>();
            datas.addAll(getDatas());
            datas.addAll(newDatas);
            if (datas.size() != 0 && loadCompletedLayoutId != UNSHOW_LOADCOMPLETEDLAYOUTID) {
                newDatas.add(getLoadCompletedItem());
            }
            loadCompleted = true;
        } else {
            newDatas.add(getLoadMoreItem());
        }
        return newDatas;
    }

    private List<ViewLayerItem> convertReplaceAllDatas(List<ViewLayerItem> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<ViewLayerItem>();
        }
        loadCompleted = false;
        if (getViewLayerItemsRootCount(newDatas) < pageCount) {
            loadCompleted(newDatas);
            loadCompleted = true;
        } else {
            newDatas.add(getLoadMoreItem());
        }
        return newDatas;
    }

    private void hideLoadMore(boolean useNotifyDataSetChanged) {
        List<ViewLayerItem> datas = getDatas();
        int lastPosition = datas.size() - 1;
        if (lastPosition >= 0) {
            ViewLayerItem viewLayerItem = datas.get(lastPosition);
            if (viewLayerItem != null && viewLayerItem.getViewType() == ViewItem.VIEW_TYPE_ITEM_LOAD_MORE) {
                datas.remove(lastPosition);
                if (!useNotifyDataSetChanged) {
                    notifyItemRemoved(lastPosition);
                }
            }
        }
    }

    private int getViewLayerItemsRootCount(List<ViewLayerItem> viewLayerItems) {
        int count = 0;
        if (viewLayerItems == null) {
            return count;
        }
        for (int i = 0; i < viewLayerItems.size(); i++) {
            ViewLayerItem viewLayerItem = viewLayerItems.get(i);
            if (viewLayerItem != null && viewLayerItem.getLayer() == ViewLayerItem.LAYER_ROOT) {
                count = count + 1;
            }
        }
        return count;
    }

    private ViewLayerItem getLoadCompletedItem() {
        return new ViewLayerItem(ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED, null, ViewLayerItem.LOAD_COMPLETED, null);
    }

    private ViewLayerItem getLoadMoreItem() {
        return new ViewLayerItem(ViewItem.VIEW_TYPE_ITEM_LOAD_MORE, null, ViewLayerItem.LOAD_MORE, null);
    }

    private void loadCompleted(List<ViewLayerItem> datas) {
        if (datas != null && datas.size() != 0 && loadCompletedLayoutId != UNSHOW_LOADCOMPLETEDLAYOUTID) {
            datas.add(getLoadCompletedItem());
        }
    }

    public int getLoadMoreLayoutId() {
        return loadMoreLayoutId;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getLoadCompletedLayoutId() {
        return loadCompletedLayoutId;
    }


    public void setLoadMoreCallback(LoadMoreCallback loadMoreCallback) {
        this.loadMoreCallback = loadMoreCallback;
    }

    public interface LoadMoreCallback {
        void loadMore(ViewLayerItem viewLayerItem);
    }
}

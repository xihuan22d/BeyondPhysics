package com.my.adapters.recyclerviewadapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.utils.NetworkGifImageViewHelp;
import com.beyondphysics.ui.views.NetworkGifImageView;
import com.beyondphysics.ui.views.RoundedNetworkGifImageView;
import com.my.beyondphysicsapplication.R;
import com.my.utils.RecyclerViewItemDecorationDefault;

import java.util.List;


public class GifListActivity_RecyclerViewAdapter extends LoadMoreRecyclerViewAdapter {
    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private int spanCount = 2;
    private int recyclerViewSpace = 0;
    private int itemWidth = 0;
    private int avatar_normal_width_narrow;

    public GifListActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewItem> datas) {
        super(datas);
        init(baseActivity, recyclerView, spanCount);
    }

    public GifListActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewItem> datas, int loadMoreLayoutId) {
        super(datas, loadMoreLayoutId);
        init(baseActivity, recyclerView, spanCount);
    }

    public GifListActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewItem> datas, int loadMoreLayoutId, int pageCount) {
        super(datas, loadMoreLayoutId, pageCount);
        init(baseActivity, recyclerView, spanCount);
    }

    private void init(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount) {
        this.baseActivity = baseActivity;
        this.recyclerView = recyclerView;
        this.spanCount = spanCount;
        recyclerViewSpace = BaseActivity.getRecyclerViewSpace_Default(this.baseActivity);
        itemWidth = (BaseActivity.getScreenWidth(this.baseActivity) - this.spanCount * 2 * recyclerViewSpace) / this.spanCount;
        avatar_normal_width_narrow = this.baseActivity.getResources().getDimensionPixelSize(
                R.dimen.avatar_normal_width_narrow);
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.activity_giflist_item;
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
                holder.itemView.setTag(RecyclerViewItemDecorationDefault.NEEDUPDATEITEMOFFSETS_WALLPAPER_ITEM);
                String string = (String) viewItem.getModel();
                if (string != null) {
                    RoundedNetworkGifImageView roundedNetworkGifImageViewAvatar = (RoundedNetworkGifImageView) holder.getView(R.id.roundedNetworkGifImageViewAvatar);
                    NetworkGifImageView networkGifImageView = (NetworkGifImageView) holder.getView(R.id.networkGifImageView);

                    NetworkGifImageViewHelp.getImageFromNetwork(roundedNetworkGifImageViewAvatar, string, baseActivity.activityKey, avatar_normal_width_narrow, avatar_normal_width_narrow, R.mipmap.normal_avatar, R.mipmap.normal_avatar_error);

                    networkGifImageView.setOpenUpdateScaleType(true);
                    networkGifImageView.setUpdateScaleTypeWhenGetBitmap(ImageView.ScaleType.FIT_CENTER);
                    NetworkGifImageViewHelp.getImageFromNetworkWithNewParams(networkGifImageView, string, baseActivity.activityKey, itemWidth, itemWidth, R.mipmap.normal_loading, R.mipmap.normal_loading_error);

                }
            }
        }
    }

}

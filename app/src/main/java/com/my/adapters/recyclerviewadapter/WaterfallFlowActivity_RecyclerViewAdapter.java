package com.my.adapters.recyclerviewadapter;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.utils.NetworkImageViewHelp;
import com.beyondphysics.ui.views.NetworkImageView;
import com.beyondphysics.ui.views.RoundedNetworkImageView;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.WallpaperDetailsActivity;
import com.my.models.Wallpaper;
import com.my.utils.RecyclerViewItemDecorationDefault;

import java.util.List;


public class WaterfallFlowActivity_RecyclerViewAdapter extends LoadMoreRecyclerViewAdapter {
    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private int spanCount = 2;
    private int recyclerViewSpace = 0;
    private int itemWidth = 0;
    private int avatar_normal_width_narrow;

    public WaterfallFlowActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewItem> datas) {
        super(datas);
        init(baseActivity, recyclerView, spanCount);
    }

    public WaterfallFlowActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewItem> datas, int loadMoreLayoutId) {
        super(datas, loadMoreLayoutId);
        init(baseActivity, recyclerView, spanCount);
    }

    public WaterfallFlowActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewItem> datas, int loadMoreLayoutId, int pageCount) {
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
            return R.layout.normal_recyclerview_wallpaper_item;
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
                Wallpaper wallpaper = (Wallpaper) viewItem.getModel();
                if (wallpaper != null) {
                    RoundedNetworkImageView roundedNetworkImageViewAvatar = (RoundedNetworkImageView) holder.getView(R.id.roundedNetworkImageViewAvatar);
                    NetworkImageView networkImageView = (NetworkImageView) holder.getView(R.id.networkImageView);

                    holder.setText(R.id.textViewWallpaperName, wallpaper.getName());
                    holder.setText(R.id.textViewVisitCount, String.valueOf(wallpaper.getVisitCount()));
                    final String title;
                    if (wallpaper.getKind() != null && wallpaper.getKind().equals(Wallpaper.kind_video)) {
                        title = wallpaper.getName() + "(视频)";
                    } else {
                        title = wallpaper.getName() + "(图片)";
                    }
                    NetworkImageViewHelp.getImageFromNetwork(roundedNetworkImageViewAvatar, wallpaper.getUploadUser_avatarUrl_absolute(), baseActivity.activityKey, avatar_normal_width_narrow, avatar_normal_width_narrow, R.mipmap.normal_avatar, R.mipmap.normal_avatar_error);
                    int previewImageWidth = itemWidth;
                    int previewImageHeight = previewImageWidth;
                    if (wallpaper.getPreviewImageWidth() != 0) {
                        previewImageHeight = (int) (previewImageWidth * (wallpaper.getPreviewImageHeight() / wallpaper.getPreviewImageWidth()));
                    }
                    networkImageView.setOpenUpdateScaleType(true);
                    networkImageView.setUpdateScaleTypeWhenGetBitmap(ImageView.ScaleType.FIT_XY);
                    NetworkImageViewHelp.getImageFromNetworkWithNewParams(networkImageView, wallpaper.getPreviewImageUrl_absolute(), baseActivity.activityKey, previewImageWidth, previewImageHeight, R.mipmap.normal_loading, R.mipmap.normal_loading_error);

                    final String wallpaper_id = wallpaper.get_id();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(baseActivity, WallpaperDetailsActivity.class);
                            intent.putExtra(WallpaperDetailsActivity.WALLPAPER_ID_KEY, wallpaper_id);
                            intent.putExtra(WallpaperDetailsActivity.TITLE_KEY, title);
                            baseActivity.startActivity(intent);
                        }
                    });
                }
            }
        }
    }

}

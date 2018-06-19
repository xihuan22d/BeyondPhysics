package com.beyondphysics.ui.imagechooselibrary.adapters;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.beyondphysics.R;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.imagechooselibrary.ChooseImageActivity;
import com.beyondphysics.ui.imagechooselibrary.models.ImageItem;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.UnLoadMoreRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.utils.NetworkGifImageViewHelp;
import com.beyondphysics.ui.views.NetworkGifImageView;

import java.util.List;


/**
 * Created by xihuan22 on 2017/7/7.
 */

public class ChooseImageActivity_RecyclerViewAdapter extends UnLoadMoreRecyclerViewAdapter {
    private ChooseImageActivity chooseImageActivity;
    private int itemWidth = 0;
    private int beyondPhysics_activity_chooseimage_item_background_select;
    private int beyondPhysics_activity_chooseimage_item_background_unselect;


    public ChooseImageActivity_RecyclerViewAdapter(ChooseImageActivity chooseImageActivity, List<ViewItem> datas) {
        super(datas);
        init(chooseImageActivity);
    }

    private void init(ChooseImageActivity chooseImageActivity) {
        this.chooseImageActivity = chooseImageActivity;
        int activity_chooseimage_recyclerView_space = BaseActivity.getRecyclerViewSpace_Default(this.chooseImageActivity);
        itemWidth = (BaseActivity.getScreenWidth(this.chooseImageActivity) - 6 * activity_chooseimage_recyclerView_space) / 3;
        beyondPhysics_activity_chooseimage_item_background_select = ContextCompat.getColor(this.chooseImageActivity, R.color.beyondPhysics_activity_chooseimage_item_background_select);
        beyondPhysics_activity_chooseimage_item_background_unselect = ContextCompat.getColor(this.chooseImageActivity, R.color.beyondPhysics_activity_chooseimage_item_background_unselect);
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.beyondphysics_activity_chooseimage_item;
        } else {
            return R.layout.beyondphysics_recyclerview_lostviewtype_item;
        }
    }

    @Override
    public void onBindNormalViewHolder(final BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {//规范一下SDK版本的判断,小于的时候不加等于号,大于时候加等于号,api13以及以下的recyclerView复用的view会有不显示的bug,测试发现最新的'com.android.support:recyclerview-v7:25.3.1'也没有修复
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
                final ImageItem imageItem = (ImageItem) viewItem.getModel();
                if (imageItem != null) {
                    NetworkGifImageView networkGifImageView = (NetworkGifImageView) holder.getView(R.id.networkGifImageView);
                    NetworkGifImageViewHelp.getImageFromDiskCacheWithNewParams(networkGifImageView, imageItem.getPath(), 1, chooseImageActivity.activityKey, itemWidth, itemWidth, R.mipmap.beyondphysics_activity_chooseimage_default_loading, R.mipmap.beyondphysics_activity_chooseimage_default_loading);
                    final CheckBox checkBox = (CheckBox) holder.getView(R.id.checkBox);
                    final FrameLayout frameLayoutCover = (FrameLayout) holder.getView(R.id.frameLayoutCover);
                    ViewGroup.LayoutParams layoutParams = frameLayoutCover.getLayoutParams();
                    layoutParams.width = itemWidth;
                    layoutParams.height = itemWidth;
                    frameLayoutCover.setLayoutParams(layoutParams);
                    if (imageItem.isSelect()) {
                        checkBox.setChecked(true);
                        frameLayoutCover.setBackgroundColor(beyondPhysics_activity_chooseimage_item_background_select);
                    } else {
                        checkBox.setChecked(false);
                        frameLayoutCover.setBackgroundColor(beyondPhysics_activity_chooseimage_item_background_unselect);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (imageItem.isSelect()) {
                                checkBox.setChecked(false);
                                frameLayoutCover.setBackgroundColor(beyondPhysics_activity_chooseimage_item_background_unselect);
                                imageItem.setSelect(false);
                                chooseImageActivity.updateSelectImageItems();
                            } else {
                                if (chooseImageActivity.isCanSelect()) {
                                    checkBox.setChecked(true);
                                    frameLayoutCover.setBackgroundColor(beyondPhysics_activity_chooseimage_item_background_select);
                                    imageItem.setSelect(true);
                                    chooseImageActivity.updateSelectImageItems();
                                }
                            }
                        }
                    });
                }
            }
        }
    }


}

package com.my.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beyondphysics.ui.BaseActivity;

public class RecyclerViewItemDecorationDefault extends RecyclerView.ItemDecoration {
    public static final String NEEDUPDATEITEMOFFSETS_WALLPAPER_ITEM = "needUpdateItemOffsets_wallpaper_item";
    public static final String NEEDUPDATEITEMOFFSETS_FULLSPAN_ITEM_INCLUDE_HORIZONTAL_MARGIN = "needUpdateItemOffsets_fullSpan_item_include_horizontal_margin";

    private BaseActivity baseActivity;
    private int spanCount = 2;
    private int recyclerViewSpace = 0;

    public RecyclerViewItemDecorationDefault(BaseActivity baseActivity, int spanCount) {
        this.baseActivity = baseActivity;
        this.spanCount = spanCount;
        recyclerViewSpace = BaseActivity.getRecyclerViewSpace_Default(this.baseActivity);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Object tag = view.getTag();
        if (tag != null) {
            ViewGroup.LayoutParams viewGroupLayoutParams = view.getLayoutParams();
            if (tag.equals(NEEDUPDATEITEMOFFSETS_WALLPAPER_ITEM)) {
                if (viewGroupLayoutParams instanceof RecyclerView.LayoutParams) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewGroupLayoutParams;
                    layoutParams.setMargins(recyclerViewSpace, recyclerViewSpace, recyclerViewSpace, recyclerViewSpace);
                }
            } else if (tag.equals(NEEDUPDATEITEMOFFSETS_FULLSPAN_ITEM_INCLUDE_HORIZONTAL_MARGIN)) {
                if (viewGroupLayoutParams instanceof RecyclerView.LayoutParams) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewGroupLayoutParams;
                    layoutParams.setMargins(recyclerViewSpace, 0, recyclerViewSpace, 0);
                }
            }
        }

    }
}

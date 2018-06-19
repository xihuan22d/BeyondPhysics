package com.beyondphysics.ui.recyclerviewlibrary.models;

import java.io.Serializable;

/**
 * Created by xihuan22 on 2016/3/8.
 */
public class ViewItem implements Serializable {
    public static final int VIEW_TYPE_ITEM_LOAD_COMPLETED = -1;
    public static final int VIEW_TYPE_ITEM_LOAD_MORE = 0;
    public static final int VIEW_TYPE_ITEM_VIEWPAGER = 1;//BaseRecyclerViewFromFrameLayout里面要沾满
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE1 = 2;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE2 = 3;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE3 = 4;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE4 = 5;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE5 = 6;

    protected int viewType;
    protected Object model;

    public ViewItem(int viewType, Object model) {
        this.viewType = viewType;
        this.model = model;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ViewItem{" +
                "viewType=" + viewType +
                ", model=" + model +
                '}';
    }
}
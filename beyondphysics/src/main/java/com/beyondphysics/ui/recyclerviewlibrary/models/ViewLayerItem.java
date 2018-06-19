package com.beyondphysics.ui.recyclerviewlibrary.models;

/**
 * Created by xihuan22 on 2016/3/8.
 */
public class ViewLayerItem extends ViewItem {

    public static final int LOAD_COMPLETED = -1;
    public static final int LOAD_MORE = 0;
    public static final int LAYER_ROOT = 1;

    private int layer;
    private Object rootModel;

    public ViewLayerItem(int viewType, Object model, int layer, Object rootModel) {
        super(viewType, model);
        this.layer = layer;
        this.rootModel = rootModel;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Object getRootModel() {
        return rootModel;
    }

    public void setRootModel(Object rootModel) {
        this.rootModel = rootModel;
    }

    @Override
    public String toString() {
        return "ViewLayerItem{" +
                "layer=" + layer +
                ", rootModel=" + rootModel +
                '}';
    }
}
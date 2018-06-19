package com.beyondphysics.ui.recyclerviewlibrary.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2016/3/8.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseRecyclerViewHolder> {

    private final List<T> datas = new ArrayList<T>();

    public BaseRecyclerViewAdapter(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<T>();
        }
        this.datas.addAll(datas);
    }


    public abstract int getLayoutId(int viewType);

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        return new BaseRecyclerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    /**
     * 一定会执行notifyItemRangeInserted从而使得adapter.registerAdapterDataObserver获得触发
     */
    public void addAll(List<T> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<T>();
        }
        int start = datas.size();
        datas.addAll(newDatas);
        notifyItemRangeInserted(start, newDatas.size());
    }

    public void addAllUseNotifyDataSetChanged(List<T> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<T>();
        }
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * 一定会执行notifyItemRangeInserted从而使得adapter.registerAdapterDataObserver获得触发
     */
    public void replaceAll(List<T> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<T>();
        }
        clearAll();
        datas.addAll(newDatas);
        notifyItemRangeInserted(0, newDatas.size());
    }

    public void replaceAllWithScrollTop(List<T> newDatas, RecyclerView recyclerView) {
        if (newDatas == null) {
            newDatas = new ArrayList<T>();
        }
        clearAll();
        datas.addAll(newDatas);
        notifyItemRangeInserted(0, newDatas.size());
        if (datas.size() > 0) {
            recyclerView.scrollToPosition(0);
        }
    }

    public void replaceAllUseNotifyDataSetChanged(List<T> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<T>();
        }
        datas.clear();
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void replaceAllUseNotifyDataSetChangedWithScrollTop(List<T> newDatas, RecyclerView recyclerView) {
        if (newDatas == null) {
            newDatas = new ArrayList<T>();
        }
        datas.clear();
        datas.addAll(newDatas);
        notifyDataSetChanged();
        if (datas.size() > 0) {
            recyclerView.scrollToPosition(0);
        }
    }


    private void clearAll() {
        int size = datas.size();
        if (size > 0) {
            datas.clear();//通知recyclerView改变之前必须先修改datas的值
            notifyItemRangeRemoved(0, size);
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views = new SparseArray<View>();

        public BaseRecyclerViewHolder(View view) {
            super(view);
        }

        public <T extends View> T getView(int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 如果某个view需要动态移除或者清除viewId对应的缓存可以调用该方法
         */
        public void removeCacheView(int viewId) {
            views.remove(viewId);
        }


        public void setText(int viewId, String text) {
            TextView textView = getView(viewId);
            textView.setText(text);
        }

    }
}

package com.my.beyondphysicsapplication.uihelp;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ListViewHelp {

    /**
     * 获得listView某一类型的总高度
     */
    public static int getTheItemViewAllHeight(ListView listView, int itemViewHeight, int count) {
        if (count < 1) {
            return 0;
        }
        int totalHeight = itemViewHeight * count + listView.getDividerHeight() * (count - 1);
        return totalHeight;
    }

    /**
     * listView内部的子项根布局得是LinearLayout,这种方法的缺点是调用getView时候因为没有传入复用的view会一次性创建所有的view,数据量大时不推荐使用
     */
    public static int getMeasuredHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View view = listAdapter.getView(i, null, listView);
            if (view != null) {
                view.measure(0, 0);
                totalHeight = totalHeight + view.getMeasuredHeight();
            }
        }
        totalHeight = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
        return totalHeight;
    }

    public static View getItemViewIfVisible(int position, ListView listView, boolean haveHead) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = null;
            if (haveHead) {
                view = listView.getChildAt(position - firstVisiblePosition + 1);
            } else {
                view = listView.getChildAt(position - firstVisiblePosition);
            }
            return view;
        } else {
            return null;
        }
    }

}

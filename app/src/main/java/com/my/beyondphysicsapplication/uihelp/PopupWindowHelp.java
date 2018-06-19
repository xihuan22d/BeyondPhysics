package com.my.beyondphysicsapplication.uihelp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beyondphysics.network.utils.TimeTool;
import com.beyondphysics.ui.BaseActivity;
import com.my.adapters.listviewadapter.Normal_Comment_More_String_ListViewAdapter;
import com.my.adapters.listviewadapter.Normal_String_ListViewAdapter;
import com.my.beyondphysicsapplication.R;
import com.my.models.local.KeyValueItem;

import java.util.List;


public class PopupWindowHelp {
    public static final String WINDOWTOKENISNULL = "windowTokenIsNull";
    public static final String POPUPWINDOWKEY = "popupWindowKey";

    public static void showPopupWindowNormalListViewString(final BaseActivity baseActivity, View view, final View needEnableView, String topTitle, List<KeyValueItem> keyValueItems, int selectPosition, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_normal_listview, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupwindow_vertical_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_vertical_margin);
        int popupwindow_topView_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_topView_height);
        int popupwindow_bottomView_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_bottomView_height);
        int popupwindow_normal_listview_string_item_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_normal_listview_string_item_height);

        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });

        TextView textViewTopTitle = (TextView) popupWindowView.findViewById(R.id.textViewTopTitle);
        textViewTopTitle.setText(topTitle);
        ListView listView = (ListView) popupWindowView.findViewById(R.id.listView);
        TextView textViewCancel = (TextView) popupWindowView.findViewById(R.id.textViewCancel);

        textViewCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        int count = 0;
        if (keyValueItems != null) {
            count = keyValueItems.size();
        }
        int listViewMeasuredHeight = ListViewHelp.getTheItemViewAllHeight(listView, popupwindow_normal_listview_string_item_height, count);
        int listViewMaxVisible = BaseActivity.getScreenHeight(baseActivity) - popupwindow_vertical_margin * 2 - popupwindow_topView_height - popupwindow_bottomView_height;
        if (listViewMeasuredHeight > listViewMaxVisible) {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = listViewMaxVisible;
            listView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(layoutParams);
        }
        Normal_String_ListViewAdapter normal_String_ListViewAdapter = new Normal_String_ListViewAdapter(baseActivity, selectPosition, keyValueItems, listView, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
                popupWindow.dismiss();
            }
        });
        listView.setAdapter(normal_String_ListViewAdapter);

        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public interface OnShowPopupwindowNormalProgressListener {

        void init(ProgressBar progressBar, TextView textViewProgress, PopupWindow popupWindow, View popupWindowView);


        void cancel(PopupWindow popupWindow, View popupWindowView);


        void popupWindowDismiss(PopupWindow popupWindow, View popupWindowView);
    }

    public static void showPopupWindowNormalProgress(final BaseActivity baseActivity, View view, final View needEnableView, String topTitle, final OnShowPopupwindowNormalProgressListener onShowPopupwindowNormalProgressListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        final View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_normal_progress, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onShowPopupwindowNormalProgressListener != null) {
                    onShowPopupwindowNormalProgressListener.popupWindowDismiss(popupWindow, popupWindowView);
                }
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });

        TextView textViewTopTitle = (TextView) popupWindowView.findViewById(R.id.textViewTopTitle);
        textViewTopTitle.setText(topTitle);
        ProgressBar progressBar = (ProgressBar) popupWindowView.findViewById(R.id.progressBar);
        TextView textViewProgress = (TextView) popupWindowView.findViewById(R.id.textViewProgress);
        TextView textViewCancel = (TextView) popupWindowView.findViewById(R.id.textViewCancel);

        textViewCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (onShowPopupwindowNormalProgressListener != null) {
                    onShowPopupwindowNormalProgressListener.cancel(popupWindow, popupWindowView);
                }
                popupWindow.dismiss();
            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        if (onShowPopupwindowNormalProgressListener != null) {
            onShowPopupwindowNormalProgressListener.init(progressBar, textViewProgress, popupWindow, popupWindowView);
        }
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }


    public static void showPopupWindowMenuListViewMore(final BaseActivity baseActivity, View view, final View needEnableView, int rightX, int topY, List<KeyValueItem> keyValueItems, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_menu_listview_more, null);
        int popupwindow_menu_listview_more_string_item_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_menu_listview_more_string_item_height);
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });

        ListView listView = (ListView) popupWindowView.findViewById(R.id.listView);
        int count = 0;
        if (keyValueItems != null) {
            count = keyValueItems.size();
        }
        if (count > 2) {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = popupwindow_menu_listview_more_string_item_height * 2;
            listView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(layoutParams);
        }

        Normal_Comment_More_String_ListViewAdapter normal_Comment_More_String_ListViewAdapter = new Normal_Comment_More_String_ListViewAdapter(baseActivity, keyValueItems, listView, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
                popupWindow.dismiss();
            }
        });
        listView.setAdapter(normal_Comment_More_String_ListViewAdapter);

        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        if (rightX < 0) {
            rightX = 0;
        }
        if (topY < 0) {
            topY = 0;
        }
        popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, rightX, topY);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }


}

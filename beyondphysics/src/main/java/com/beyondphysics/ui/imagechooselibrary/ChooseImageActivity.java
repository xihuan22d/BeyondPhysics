package com.beyondphysics.ui.imagechooselibrary;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyondphysics.R;
import com.beyondphysics.network.utils.TimeTool;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.imagechooselibrary.adapters.ChooseImageActivity_ListViewAdapter;
import com.beyondphysics.ui.imagechooselibrary.adapters.ChooseImageActivity_RecyclerViewAdapter;
import com.beyondphysics.ui.imagechooselibrary.models.ImageFolder;
import com.beyondphysics.ui.imagechooselibrary.models.ImageItem;
import com.beyondphysics.ui.imagechooselibrary.utils.SdcardDataSourceHelper;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by xihuan22 on 2017/7/7.
 */

public class ChooseImageActivity extends BaseActivity {
    public static final String CHOOSECOUNT_KEY = "chooseCount_key";
    public static final String SELECTIMAGEPATHS_KEY = "selectImagePaths_key";

    private int chooseCount = 8;
    private List<String> selectImagePaths;

    private boolean isInit = false;
    private List<ImageFolder> imageFolders;
    private final List<ImageItem> selectImageItems = new ArrayList<ImageItem>();


    private RelativeLayout relativeLayoutRoot;
    private ImageView imageViewBack;
    private TextView textViewTitle;
    private Button buttonOk;
    private Button buttonSelectType;
    private Button buttonPreview;

    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private ChooseImageActivity_RecyclerViewAdapter chooseImageActivity_RecyclerViewAdapter;
    private View.OnClickListener onClickListener;

    private int selectTypePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beyondphysics_activity_chooseimage);
        Intent intent = getIntent();
        chooseCount = intent.getIntExtra(CHOOSECOUNT_KEY, 8);
        selectImagePaths = intent.getStringArrayListExtra(SELECTIMAGEPATHS_KEY);
        removeSamePaths();
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        relativeLayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonSelectType = (Button) findViewById(R.id.buttonSelectType);
        buttonPreview = (Button) findViewById(R.id.buttonPreview);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        updateButton();
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.imageViewBack) {
                    doBack();
                } else if (id == R.id.buttonOk) {
                    ArrayList<String> paths = new ArrayList<String>();
                    for (int i = 0; i < selectImageItems.size(); i++) {
                        if (selectImageItems.get(i) != null) {
                            paths.add(selectImageItems.get(i).getPath());
                        }
                    }
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(SELECTIMAGEPATHS_KEY, paths);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (id == R.id.buttonSelectType) {
                    chooseImageActivity_showSelectTypePopupWindow(ChooseImageActivity.this, relativeLayoutRoot, buttonSelectType, imageFolders, selectTypePosition, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectTypePosition = position;
                            updateDatas();
                        }
                    });
                } else if (id == R.id.buttonPreview) {
                    ArrayList<String> paths = new ArrayList<String>();
                    for (int i = 0; i < selectImageItems.size(); i++) {
                        if (selectImageItems.get(i) != null) {
                            paths.add(selectImageItems.get(i).getPath());
                        }
                    }
                    if (paths.size() > 0) {
                        Intent intent = getPreviewActivityIntent();
                        if (intent != null) {
                            intent.putStringArrayListExtra(SELECTIMAGEPATHS_KEY, paths);
                            startActivity(intent);
                        }
                    }
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonOk.setOnClickListener(onClickListener);
        buttonSelectType.setOnClickListener(onClickListener);
        buttonPreview.setOnClickListener(onClickListener);
    }

    public Intent getPreviewActivityIntent() {
        Intent intent = new Intent(ChooseImageActivity.this,
                PreviewActivity.class);
        return intent;
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public int getNavigationBarHeight() {
        int result = 0;
        boolean isHaveNavigationBar = false;
        int resourceId = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId > 0) {
            isHaveNavigationBar = getResources().getBoolean(resourceId);
        }
        Configuration configuration = getResources().getConfiguration();
        int orientation = configuration.orientation;
        if (isHaveNavigationBar && orientation == Configuration.ORIENTATION_PORTRAIT) {
            int theResourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (theResourceId > 0) {
                result = getResources().getDimensionPixelSize(theResourceId);
            }
        }
        return result;
    }


    private void removeSamePaths() {
        if (selectImagePaths != null) {
            LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>(selectImagePaths);
            selectImagePaths = new ArrayList<String>(linkedHashSet);
        }
    }

    private void initBaseRecyclerViewFromFrameLayout() {
        baseRecyclerViewFromFrameLayout.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        int recyclerView_space = BaseActivity.getRecyclerViewSpace_Default(ChooseImageActivity.this);
        baseRecyclerViewFromFrameLayout.getRecyclerView().addItemDecoration(new SpaceItemDecoration(recyclerView_space));
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new SdcardDataSourceHelper(ChooseImageActivity.this, null, new SdcardDataSourceHelper.OnSdcardDataSourceLoadedListener() {
                    @Override
                    public void sdcardDataSourceLoaded(List<ImageFolder> imageFolders) {
                        selectImageItems.clear();
                        if (!isInit) {
                            if (selectImagePaths != null && imageFolders != null && imageFolders.size() - 1 >= 0 && imageFolders.get(0) != null && imageFolders.get(0).getImageItems() != null) {
                                List<ImageItem> imageItemsAll = imageFolders.get(0).getImageItems();
                                for (int i = 0; i < selectImagePaths.size(); i++) {
                                    if (selectImagePaths.get(i) != null) {
                                        for (int m = 0; m < imageItemsAll.size(); m++) {
                                            ImageItem imageItem = imageItemsAll.get(m);
                                            if (imageItem != null && imageItem.getPath() != null && selectImagePaths.get(i).equals(imageItem.getPath())) {
                                                imageItem.setSelect(true);
                                                selectImageItems.add(imageItem);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            isInit = true;
                        }
                        updateButton();
                        ChooseImageActivity.this.imageFolders = imageFolders;
                        updateDatas();
                    }
                });
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, 0);
    }

    private void updateDatas() {
        if (imageFolders != null) {
            if (selectTypePosition >= 0 && imageFolders.size() - 1 >= selectTypePosition && imageFolders.get(selectTypePosition) != null && imageFolders.get(selectTypePosition).getImageItems() != null) {
                ImageFolder imageFolder = imageFolders.get(selectTypePosition);
                List<ImageItem> imageItems = imageFolder.getImageItems();
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                for (int i = 0; i < imageItems.size(); i++) {
                    viewItems.add(new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, imageItems.get(i)));
                }
                textViewTitle.setText(imageFolder.getName() + "(" + imageItems.size() + ")");
                activeAdapterWithRefresh(viewItems);
            } else {
                activeAdapterWithRefresh(null);
            }
        } else {
            activeAdapterWithRefresh(null);
        }
    }

    private void activeAdapterWithRefresh(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (chooseImageActivity_RecyclerViewAdapter == null) {
            chooseImageActivity_RecyclerViewAdapter = new ChooseImageActivity_RecyclerViewAdapter(ChooseImageActivity.this, viewItems);
            baseRecyclerViewFromFrameLayout.setAdapter(chooseImageActivity_RecyclerViewAdapter);
        } else {
            chooseImageActivity_RecyclerViewAdapter.replaceAll(viewItems);
        }
    }

    public void updateButton() {
        int size = 0;
        if (!isInit) {
            if (selectImagePaths != null) {
                size = selectImagePaths.size();
            }
        } else {
            if (selectImageItems != null) {
                size = selectImageItems.size();
            }
        }
        buttonOk.setText("完成(" + size + "/" + chooseCount + ")");
        buttonPreview.setText("预览(" + size + ")");
    }


    public boolean isCanSelect() {
        if (selectImageItems.size() < chooseCount) {
            return true;
        }
        return false;

    }


    public List<ImageItem> updateSelectImageItems() {
        selectImageItems.clear();
        if (imageFolders != null) {
            if (selectTypePosition >= 0 && imageFolders.size() - 1 >= 0 && imageFolders.get(0) != null && imageFolders.get(0).getImageItems() != null) {
                ImageFolder imageFolder = imageFolders.get(0);
                List<ImageItem> imageItemsAll = imageFolder.getImageItems();
                for (int i = 0; i < imageItemsAll.size(); i++) {
                    ImageItem imageItem = imageItemsAll.get(i);
                    if (imageItem != null && imageItem.isSelect()) {
                        selectImageItems.add(imageItem);
                    }
                }
            }
        }
        updateButton();
        return selectImageItems;
    }


    public void chooseImageActivity_showSelectTypePopupWindow(final ChooseImageActivity chooseImageActivity, View view, final View needEnableView, List<ImageFolder> imageFolders, int selectPosition, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog("WindowTokenIsNull");
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = chooseImageActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.beyondphysics_activity_chooseimage_popupwindow_selecttype, null);
        int beyondPhysics_activity_chooseimage_relativeLayoutTop_height = chooseImageActivity.getResources().getDimensionPixelSize(
                R.dimen.beyondPhysics_activity_chooseimage_relativeLayoutTop_height);
        final String popupWindowKey = "popupWindowKey" + TimeTool.getOnlyTimeWithoutSleep();
        int height = (int) ((BaseActivity.getScreenHeight(chooseImageActivity) - chooseImageActivity.getStatusBarHeight() - beyondPhysics_activity_chooseimage_relativeLayoutTop_height * 2) * 0.6f);
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = chooseImageActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                chooseImageActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                chooseImageActivity.removePopupWindow(popupWindowKey);
            }
        });
        final ListView listView = (ListView) popupWindowView.findViewById(R.id.listView);
        ChooseImageActivity_ListViewAdapter chooseImageActivity_ListViewAdapter = new ChooseImageActivity_ListViewAdapter(chooseImageActivity, selectPosition, imageFolders, listView, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
                popupWindow.dismiss();
            }
        });
        listView.setAdapter(chooseImageActivity_ListViewAdapter);
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        WindowManager.LayoutParams windowManagerLayoutParams = chooseImageActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        chooseImageActivity.getWindow().setAttributes(windowManagerLayoutParams);
        int marginBottom = beyondPhysics_activity_chooseimage_relativeLayoutTop_height + chooseImageActivity.getNavigationBarHeight();
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, marginBottom);
        chooseImageActivity.addPopupWindow(popupWindowKey, popupWindow);
    }


    @Override
    protected void doBack() {
        super.doBack();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.top = space;
            outRect.bottom = space;
        }
    }
}

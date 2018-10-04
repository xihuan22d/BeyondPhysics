package com.my.beyondphysicsapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.my.modelhttpfunctions.HotCommentActivityHttpFunction;
import com.my.models.Comment;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.HotCommentActivity_GetWallpaperCommentBySort_GsonModel;

import java.util.ArrayList;
import java.util.List;

public class HotCommentActivity extends BaseCommentActivity {

    private String wallpaper_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        wallpaper_id = intent.getStringExtra(WallpaperDetailsActivity.WALLPAPER_ID_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        super.initHandler();
    }

    @Override
    protected void initUi() {
        super.initUi();
    }

    @Override
    protected void initConfigUi() {
        super.initConfigUi();
        textViewToolbarTitle.setText("热门评论");
    }

    @Override
    protected void initHttp() {
        super.initHttp();
    }

    @Override
    protected void initOther() {
        super.initOther();
    }


    @Override
    protected void getInit() {
        HotCommentActivityHttpFunction.hotCommentActivity_getWallpaperCommentBySort(HotCommentActivity.this, wallpaper_id, "-1", new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                initOrRefreshAdapter(null);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(HotCommentActivity.this, error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(HotCommentActivity.this, tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getComments() == null) {
                    BaseActivity.showShortToast(HotCommentActivity.this, TheApplication.SERVERERROR);
                } else {
                    convertToViewLayerItems(data.getComments(), viewLayerItems);
                }
                initOrRefreshAdapter(viewLayerItems);
            }
        });
    }

    @Override
    protected void getNext(Comment comment) {
        HotCommentActivityHttpFunction.hotCommentActivity_getWallpaperCommentBySort(HotCommentActivity.this, wallpaper_id, String.valueOf(comment.getChildCommentSort()), new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreError();
                    }
                }, TheApplication.LOADMOREERRORDELAY);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(HotCommentActivity.this, error);
                TheApplication.addAllFormBaseRecyclerViewAdapter(baseCommentActivity_RecyclerViewAdapter, null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(HotCommentActivity.this, tips);
                TheApplication.addAllFormBaseRecyclerViewAdapter(baseCommentActivity_RecyclerViewAdapter, null);
            }

            @Override
            public void success(HotCommentActivity_GetWallpaperCommentBySort_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getComments() == null) {
                    BaseActivity.showShortToast(HotCommentActivity.this, TheApplication.SERVERERROR);
                } else {
                    convertToViewLayerItems(data.getComments(), viewLayerItems);
                }
                TheApplication.addAllFormBaseRecyclerViewAdapter(baseCommentActivity_RecyclerViewAdapter, viewLayerItems);
            }
        });
    }

    @Override
    public void sendComment(final View view, EditText editTextComment) {
        String parentOrChild = null;
        String parent_id = null;
        if (commentTarget != null) {
            parentOrChild = Comment.PARENTORCHILD_CHILD;
            parent_id = commentTarget.get_id();
        } else {
            parentOrChild = Comment.PARENTORCHILD_PARENT;
            parent_id = wallpaper_id;
        }
        String content = editTextComment.getText().toString();

        if (oldTargetContent != null && content.startsWith(oldTargetContent)) {
            content = content.replaceFirst(oldTargetContent, "");
        }
        if (content.equals("")) {
            BaseActivity.showShortToast(HotCommentActivity.this, "评论内容不能为空");
        } else {
            commentWallpaper(view, parentOrChild, content, parent_id);
        }
    }
}

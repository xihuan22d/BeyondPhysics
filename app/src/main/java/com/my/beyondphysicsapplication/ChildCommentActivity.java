package com.my.beyondphysicsapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.my.adapters.recyclerviewadapter.ChildCommentActivity_RecyclerViewAdapter;
import com.my.modelhttpfunctions.ChildCommentActivityHttpFunction;
import com.my.models.Comment;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel;
import com.my.utils.HttpConnectTool;

import java.util.ArrayList;
import java.util.List;

public class ChildCommentActivity extends BaseCommentActivity {

    public static final String ROOTPARENT_ID_KEY = "rootParent_id_key";

    private String rootParent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        rootParent_id = intent.getStringExtra(ROOTPARENT_ID_KEY);
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
        textViewToolbarTitle.setText("回复详情");
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
        ChildCommentActivityHttpFunction.childCommentActivity_getWallpaperChildCommentByCreateTime(ChildCommentActivity.this, rootParent_id, "-1", new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                initOrRefreshAdapter(null);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(ChildCommentActivity.this, error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(ChildCommentActivity.this, tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getParentComment() == null || data.getComments() == null) {
                    BaseActivity.showShortToast(ChildCommentActivity.this, TheApplication.SERVERERROR);
                } else {
                    viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2, data.getParentComment(), 2, null));
                    viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3, data.getParentComment(), 3, null));
                    convertToViewLayerItems(data.getComments(), viewLayerItems);
                }
                initOrRefreshAdapter(viewLayerItems);
            }
        });
    }

    @Override
    protected void getNext(Comment comment) {
        ChildCommentActivityHttpFunction.childCommentActivity_getWallpaperChildCommentByCreateTime(ChildCommentActivity.this, rootParent_id, String.valueOf(comment.getCreateTime()), new Request.OnResponseListener<String>() {
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
        }, new BaseGsonModel.OnBaseGsonModelListener<ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(ChildCommentActivity.this, error);
                loadMoreError();
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(ChildCommentActivity.this, tips);
                loadMoreError();
            }

            @Override
            public void success(ChildCommentActivity_GetWallpaperChildCommentByCreateTime_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getComments() == null) {
                    BaseActivity.showShortToast(ChildCommentActivity.this, TheApplication.SERVERERROR);
                } else {
                    convertToViewLayerItems(data.getComments(), viewLayerItems);
                }
                TheApplication.addAllFormBaseRecyclerViewAdapter(baseCommentActivity_RecyclerViewAdapter, viewLayerItems);
            }
        });
    }

    @Override
    protected void convertToViewLayerItems(List<Comment> comments, List<ViewLayerItem> viewLayerItems) {
        if (comments == null || viewLayerItems == null) {
            return;
        }
        for (int i = 0; i < comments.size(); i++) {
            viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, comments.get(i), ViewLayerItem.LAYER_ROOT, comments.get(i)));
        }
    }

    @Override
    protected void initOrRefreshAdapter(List<ViewLayerItem> viewLayerItems) {
        if (viewLayerItems == null) {
            viewLayerItems = new ArrayList<ViewLayerItem>();
        }
        if (baseCommentActivity_RecyclerViewAdapter == null) {
            baseCommentActivity_RecyclerViewAdapter = new ChildCommentActivity_RecyclerViewAdapter(ChildCommentActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewLayerItems, R.layout.normal_more_progress, HttpConnectTool.commentPageSize);
            baseCommentActivity_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreLayerRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewLayerItem viewLayerItem) {
                    if (viewLayerItem == null || viewLayerItem.getRootModel() == null) {
                        BaseActivity.showShortToast(ChildCommentActivity.this, TheApplication.SERVERERROR);
                    } else {
                        final Comment comment = (Comment) (viewLayerItem.getRootModel());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNext(comment);
                            }
                        }, TheApplication.LOADMOREDELAY);
                    }
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(baseCommentActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(baseCommentActivity_RecyclerViewAdapter, viewLayerItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }

    @Override
    public void sendComment(final View view, EditText editTextComment) {
        String parentOrChild = Comment.PARENTORCHILD_CHILD;
        String parent_id = null;
        if (commentTarget != null) {
            parent_id = commentTarget.get_id();
        } else {
            parent_id = rootParent_id;
        }
        String content = editTextComment.getText().toString();
        if (content.equals("")) {
            BaseActivity.showShortToast(ChildCommentActivity.this, "评论内容不能为空");
        } else {
            if (oldTargetContent != null && content.startsWith(oldTargetContent)) {
                content = content.replaceFirst(oldTargetContent, "");
            }
            commentWallpaper( view , parentOrChild,  content, parent_id);
        }
    }
}

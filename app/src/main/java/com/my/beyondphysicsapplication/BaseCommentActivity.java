package com.my.beyondphysicsapplication;


import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.adapters.recyclerviewadapter.BaseCommentActivity_RecyclerViewAdapter;
import com.my.beyondphysicsapplication.uihelp.ColorHelp;
import com.my.beyondphysicsapplication.uihelp.ProgressDialogHelp;
import com.my.modelhttpfunctions.WallpaperDetailsActivityHttpFunction;
import com.my.models.Comment;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.WallpaperDetailsActivity_CommentWallpaper_GsonModel;
import com.my.utils.HttpConnectTool;
import com.my.views.PhizAndKeyboardFrameLayout;

import java.util.ArrayList;
import java.util.List;


public class BaseCommentActivity extends NewBaseActivity {

    protected int colorPrimary;
    protected int mainColorDeepen;
    protected int mainColorText;
    protected RelativeLayout relativeLayoutContent;
    protected ImageView imageViewBack;
    protected TextView textViewToolbarTitle;
    protected BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    protected PhizAndKeyboardFrameLayout phizAndKeyboardFrameLayout;
    protected EditText editTextComment;

    protected boolean alreadyInitUi = false;

    protected BaseCommentActivity_RecyclerViewAdapter baseCommentActivity_RecyclerViewAdapter;

    protected TextWatcher textWatcher;
    protected String oldTargetContent;
    protected Comment commentTarget;

    protected View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_comment);
        setWindowType(1);
    }

    @SuppressWarnings("ResourceType")
    protected void getTypeArray() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.mainColorDeepen, R.attr.mainColorText});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
            mainColorDeepen = typedArray.getColor(1, 0xffffffff);
            mainColorText = typedArray.getColor(2, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        getTypeArray();

        relativeLayoutContent = (RelativeLayout) findViewById(R.id.relativeLayoutContent);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewToolbarTitle = (TextView) findViewById(R.id.textViewToolbarTitle);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        phizAndKeyboardFrameLayout = (PhizAndKeyboardFrameLayout) findViewById(R.id.phizAndKeyboardFrameLayout);
        editTextComment = phizAndKeyboardFrameLayout.getEditTextComment();
        initBaseRecyclerViewFromFrameLayout();

        phizAndKeyboardFrameLayout.setOnPhizAndKeyboardFrameLayoutListener(new PhizAndKeyboardFrameLayout.OnPhizAndKeyboardFrameLayoutListener() {
            @Override
            public void onSendComment(View view, EditText editTextComment) {
                sendComment(view, editTextComment);
                removeCommentTarget();
            }
        });
        onClickListener();
        alreadyInitUi = true;
    }

    @Override
    protected void initConfigUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = relativeLayoutContent.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = null;
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            } else {
                marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
            }
            marginLayoutParams.topMargin = getStatusBarHeight();
            relativeLayoutContent.setLayoutParams(marginLayoutParams);
        }
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }


    protected void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
    }

    protected void initBaseRecyclerViewFromFrameLayout() {

        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInit();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }


    protected void getInit() {

    }

    protected void getNext(Comment comment) {

    }


    protected void loadMoreError() {
        TheApplication.loadMoreErrorFormBaseRecyclerViewAdapter(baseCommentActivity_RecyclerViewAdapter);
    }


    protected void convertToViewLayerItems(List<Comment> comments, List<ViewLayerItem> viewLayerItems) {
        if (comments == null || viewLayerItems == null) {
            return;
        }
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            if (comment != null && comment.getChildComments() != null && comment.getChildComments().size() > 0) {
                comment.setShowChildComments(true);
            }
            viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, comment, ViewLayerItem.LAYER_ROOT, comment));
            if (comment != null) {
                List<Comment> childComments = comment.getChildComments();
                int childCommentCount = childComments.size();
                if (childComments != null && childCommentCount > 0) {
                    if (childCommentCount - 1 > 0) {
                        for (int j = 0; j < childCommentCount - 1; j++) {
                            viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2, childComments.get(j), 2, comment));
                        }
                        boolean isShowMore = false;
                        if (comment.getChildCommentCount() > HttpConnectTool.findChildCommentCount) {
                            isShowMore = true;
                        }
                        Comment childComment = childComments.get(childCommentCount - 1);
                        if (childComment != null && isShowMore) {
                            childComment.setShowMoreTips(true);
                            childComment.setMoreTips("共" + comment.getChildCommentCount() + "条回复");
                        }
                        viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3, childComment, 3, comment));
                    } else {
                        Comment childComment = childComments.get(childCommentCount - 1);
                        if (childComment != null) {
                            childComment.setShowMoreTips(false);
                        }
                        viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3, childComment, 3, comment));
                    }
                }
            }
        }
    }

    protected void initOrRefreshAdapter(List<ViewLayerItem> viewLayerItems) {
        if (viewLayerItems == null) {
            viewLayerItems = new ArrayList<ViewLayerItem>();
        }
        if (baseCommentActivity_RecyclerViewAdapter == null) {
            baseCommentActivity_RecyclerViewAdapter = new BaseCommentActivity_RecyclerViewAdapter(BaseCommentActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewLayerItems, R.layout.normal_more_progress, HttpConnectTool.commentPageSize);
            baseCommentActivity_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreLayerRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewLayerItem viewLayerItem) {
                    if (viewLayerItem == null || viewLayerItem.getRootModel() == null) {
                        BaseActivity.showShortToast(BaseCommentActivity.this, TheApplication.SERVERERROR);
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


    public void commentTarget(Comment comment, boolean isParent) {
        if (comment != null) {
            String color = null;
            if (isParent) {
                color = ColorHelp.colorConvert(colorPrimary);
            } else {
                color = ColorHelp.colorConvert(mainColorText);
            }
            editTextComment.setText(Html.fromHtml("回复@<font color=" + color + ">" + comment.getUserName() + "</font>:"));

            if (textWatcher != null) {
                editTextComment.removeTextChangedListener(textWatcher);
            }
            oldTargetContent = editTextComment.getText().toString();
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String content = editTextComment.getText().toString();
                    if (oldTargetContent != null && !content.startsWith(oldTargetContent)) {
                        removeCommentTarget();
                    }
                }
            };
            editTextComment.addTextChangedListener(textWatcher);
            TheApplication.setCursorToLast(editTextComment);
            editTextComment.requestFocus();
            commentTarget = comment;
        }
    }

    public void removeCommentTarget() {
        if (textWatcher != null) {
            editTextComment.removeTextChangedListener(textWatcher);//要先移除,否则死循环
            textWatcher = null;
        }
        editTextComment.setText("");
        oldTargetContent = null;
        commentTarget = null;
    }

    public void sendComment(View view, EditText editTextComment) {

    }


    public void doRefresh() {
        if (alreadyInitUi) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }


    public void commentWallpaper(final View view, String parentOrChild, String content, String parent_id) {
        Object[] objects = ProgressDialogHelp.unEnabledView(BaseCommentActivity.this, view);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_commentWallpaper(BaseCommentActivity.this, parentOrChild, content, parent_id, new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                ProgressDialogHelp.enabledView(BaseCommentActivity.this, progressDialog, progressDialogKey, view);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(BaseCommentActivity.this, error);
                ProgressDialogHelp.enabledView(BaseCommentActivity.this, progressDialog, progressDialogKey, view);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(BaseCommentActivity.this, tips);
                ProgressDialogHelp.enabledView(BaseCommentActivity.this, progressDialog, progressDialogKey, view);
            }

            @Override
            public void success(WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data data) {
                if (data == null) {
                    BaseActivity.showShortToast(BaseCommentActivity.this, TheApplication.SERVERERROR);
                } else {
                    BaseActivity.showShortToast(BaseCommentActivity.this, data.getTips());
                    doRefresh();
                }
                ProgressDialogHelp.enabledView(BaseCommentActivity.this, progressDialog, progressDialogKey, view);
            }
        });
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }
}

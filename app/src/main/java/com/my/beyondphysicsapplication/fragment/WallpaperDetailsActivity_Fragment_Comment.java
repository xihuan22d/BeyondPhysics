package com.my.beyondphysicsapplication.fragment;

import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.adapters.recyclerviewadapter.WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.beyondphysicsapplication.WallpaperDetailsActivity;
import com.my.beyondphysicsapplication.uihelp.ColorHelp;
import com.my.beyondphysicsapplication.uihelp.ProgressDialogHelp;
import com.my.modelhttpfunctions.WallpaperDetailsActivityHttpFunction;
import com.my.models.Comment;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.WallpaperDetailsActivity_CommentWallpaper_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel;
import com.my.utils.HttpConnectTool;
import com.my.views.PhizAndKeyboardFrameLayout;

import java.util.ArrayList;
import java.util.List;


public class WallpaperDetailsActivity_Fragment_Comment extends BaseFragment {

    private String wallpaper_id;

    private View view;

    private int colorPrimary;
    private int mainColorDeepen;
    private int mainColorText;

    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private PhizAndKeyboardFrameLayout phizAndKeyboardFrameLayout;
    private EditText editTextComment;

    private boolean alreadyInitUi = false;

    private WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter;

    private TextWatcher textWatcher;
    private String oldTargetContent;
    private Comment commentTarget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wallpaper_details_fragment_comment, container, false);
        Bundle bundle = getArguments();
        wallpaper_id = bundle.getString(WallpaperDetailsActivity.WALLPAPER_ID_KEY);
        initAll();
        return view;
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArray() {
        TypedArray typedArray = getBaseActivity().getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.mainColorDeepen, R.attr.mainColorText});
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

        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) view.findViewById(R.id.baseRecyclerViewFromFrameLayout);
        phizAndKeyboardFrameLayout = (PhizAndKeyboardFrameLayout) view.findViewById(R.id.phizAndKeyboardFrameLayout);
        editTextComment = phizAndKeyboardFrameLayout.getEditTextComment();
        initBaseRecyclerViewFromFrameLayout();

        phizAndKeyboardFrameLayout.setOnPhizAndKeyboardFrameLayoutListener(new PhizAndKeyboardFrameLayout.OnPhizAndKeyboardFrameLayoutListener() {
            @Override
            public void onSendComment(final View view, EditText editTextComment) {
                sendComment(view, editTextComment);
                removeCommentTarget();
            }
        });
        alreadyInitUi = true;
    }

    @Override
    protected void initConfigUi() {
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }


    private void initBaseRecyclerViewFromFrameLayout() {
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInit();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }


    private void getInit() {
        WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_getWallpaperCommentByCreateTime(getBaseActivity(), wallpaper_id, "-1", new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                initOrRefreshAdapter(null);
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(getBaseActivity(), error);
                initOrRefreshAdapter(null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(getBaseActivity(), tips);
                initOrRefreshAdapter(null);
            }

            @Override
            public void success(WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getComments() == null) {
                    BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
                } else {
                    convertToViewLayerItems(true, data.getHotComments(), viewLayerItems);
                    convertToViewLayerItems(false, data.getComments(), viewLayerItems);
                }
                initOrRefreshAdapter(viewLayerItems);
            }
        });
    }

    private void getNext(Comment comment) {
        WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_getWallpaperCommentByCreateTime(getBaseActivity(), wallpaper_id, String.valueOf(comment.getCreateTime()), new Request.OnResponseListener<String>() {
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
        }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(getBaseActivity(), error);
                TheApplication.addAllFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter, null);
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(getBaseActivity(), tips);
                TheApplication.addAllFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter, null);
            }

            @Override
            public void success(WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.Data data) {
                List<ViewLayerItem> viewLayerItems = new ArrayList<ViewLayerItem>();
                if (data == null || data.getComments() == null) {
                    BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
                } else {
                    convertToViewLayerItems(false, data.getComments(), viewLayerItems);
                }
                TheApplication.addAllFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter, viewLayerItems);
            }
        });
    }


    private void loadMoreError() {
        TheApplication.loadMoreErrorFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter);
    }


    private void convertToViewLayerItems(boolean isHotComments, List<Comment> comments, List<ViewLayerItem> viewLayerItems) {
        if (comments == null || viewLayerItems == null) {
            return;
        }
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            if (isHotComments) {
                if (comment != null) {
                    if (i == comments.size() - 1) {
                        comment.setShowHotMore(true);
                    }
                }
                viewLayerItems.add(new ViewLayerItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE4, comment, 4, null));
            } else {
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
    }

    private void initOrRefreshAdapter(List<ViewLayerItem> viewLayerItems) {
        if (viewLayerItems == null) {
            viewLayerItems = new ArrayList<ViewLayerItem>();
        }
        if (wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter == null) {
            wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter = new WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter(getBaseActivity(), this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewLayerItems, R.layout.normal_more_progress, HttpConnectTool.commentPageSize);
            wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter.setLoadMoreCallback(new LoadMoreLayerRecyclerViewAdapter.LoadMoreCallback() {
                @Override
                public void loadMore(ViewLayerItem viewLayerItem) {
                    if (viewLayerItem == null || viewLayerItem.getRootModel() == null) {
                        BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
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
            baseRecyclerViewFromFrameLayout.setAdapter(wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(wallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter, viewLayerItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }

    }

    public String getWallpaper_id() {
        return wallpaper_id;
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
            BaseActivity.showShortToast(getBaseActivity(), "评论内容不能为空");
        } else {
            Object[] objects = ProgressDialogHelp.unEnabledView(getBaseActivity(), view);
            final ProgressDialog progressDialog = (ProgressDialog) objects[0];
            final String progressDialogKey = (String) objects[1];
            WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_commentWallpaper(getBaseActivity(), parentOrChild, content, parent_id, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    ProgressDialogHelp.enabledView(getBaseActivity(), progressDialog, progressDialogKey, view);
                }

                @Override
                public void onErrorResponse(String error) {
                    ProgressDialogHelp.enabledView(getBaseActivity(), progressDialog, progressDialogKey, view);
                }
            }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data>() {
                @Override
                public void error(String error) {
                    BaseActivity.showShortToast(getBaseActivity(), error);
                }

                @Override
                public void successByTips(String tips) {
                    BaseActivity.showShortToast(getBaseActivity(), tips);
                }

                @Override
                public void success(WallpaperDetailsActivity_CommentWallpaper_GsonModel.Data data) {
                    if (data == null) {
                        BaseActivity.showShortToast(getBaseActivity(), TheApplication.SERVERERROR);
                    } else {
                        BaseActivity.showShortToast(getBaseActivity(), data.getTips());
                        WallpaperDetailsActivity wallpaperDetailsActivity = (WallpaperDetailsActivity) getBaseActivity();
                        wallpaperDetailsActivity.updateTabLayoutCommentCount(data.getCommentCount());
                        doRefresh();
                    }
                }
            });
        }
    }


    public void doRequestFocus() {
        if (alreadyInitUi) {
            if (editTextComment.getText().toString().equals("")) {
                editTextComment.setText("");
                editTextComment.requestFocus();
            }
        }
    }

    public void doRefresh() {
        if (alreadyInitUi) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

}
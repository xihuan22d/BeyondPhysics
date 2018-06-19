package com.my.adapters.recyclerviewadapter;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.utils.NetworkImageViewHelp;
import com.beyondphysics.ui.views.RoundedNetworkImageView;
import com.my.beyondphysicsapplication.BaseCommentActivity;
import com.my.beyondphysicsapplication.ChildCommentActivity;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.beyondphysicsapplication.uihelp.ColorHelp;
import com.my.beyondphysicsapplication.uihelp.PopupWindowHelp;
import com.my.modelhttpfunctions.WallpaperDetailsActivityHttpFunction;
import com.my.models.Comment;
import com.my.models.Follow;
import com.my.models.Praise;
import com.my.models.local.KeyValueItem;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoFollow_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoPraise_GsonModel;
import com.my.utils.DataTool;

import java.util.ArrayList;
import java.util.List;


public class BaseCommentActivity_RecyclerViewAdapter extends LoadMoreLayerRecyclerViewAdapter {
    protected BaseCommentActivity baseCommentActivity;
    protected RecyclerView recyclerView;
    protected int avatar_normal_width_narrow;
    protected int colorPrimary;
    protected int normal_comment_item_normal_help;

    protected Drawable drawableComment;
    protected Drawable drawablePraiseUpUnSelect;
    protected Drawable drawablePraiseUpSelect;
    protected Drawable drawablePraiseDownUnSelect;
    protected Drawable drawablePraiseDownSelect;

    public BaseCommentActivity_RecyclerViewAdapter(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView, List<ViewLayerItem> datas) {
        super(datas);
        init(baseCommentActivity, recyclerView);
    }

    public BaseCommentActivity_RecyclerViewAdapter(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView, List<ViewLayerItem> datas, int loadMoreLayoutId) {
        super(datas, loadMoreLayoutId);
        init(baseCommentActivity, recyclerView);
    }

    public BaseCommentActivity_RecyclerViewAdapter(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView, List<ViewLayerItem> datas, int loadMoreLayoutId, int pageCount) {
        super(datas, loadMoreLayoutId, pageCount);
        init(baseCommentActivity, recyclerView);
    }

    private void init(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView) {
        this.baseCommentActivity = baseCommentActivity;
        this.recyclerView = recyclerView;
        TypedArray typedArray = this.baseCommentActivity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
        normal_comment_item_normal_help = ContextCompat.getColor(this.baseCommentActivity, R.color.normal_comment_item_normal_help);
        avatar_normal_width_narrow = this.baseCommentActivity.getResources().getDimensionPixelSize(R.dimen.avatar_normal_width_narrow);

        drawableComment = this.baseCommentActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_comment);
        drawablePraiseUpUnSelect = this.baseCommentActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_up_unselect);
        drawablePraiseUpSelect = this.baseCommentActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_up_select);
        drawablePraiseDownUnSelect = this.baseCommentActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_down_unselect);
        drawablePraiseDownSelect = this.baseCommentActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_down_select);
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.normal_comment_item_parent;
        } else if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
            return R.layout.normal_comment_item_child;
        } else if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3) {
            return R.layout.normal_comment_item_bottom;
        } else {
            return com.beyondphysics.R.layout.beyondphysics_recyclerview_lostviewtype_item;
        }
    }

    @Override
    public void onBindNormalViewHolder(final BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, final int position) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    holder.itemView.setAlpha(1.0f);
                }
            });
        }
        ViewItem viewItem = getDatas().get(position);
        if (viewItem != null) {
            if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
                final Comment comment = (Comment) viewItem.getModel();
                if (comment != null) {
                    RoundedNetworkImageView roundedNetworkImageViewAvatar = (RoundedNetworkImageView) holder.getView(R.id.roundedNetworkImageViewAvatar);
                    LinearLayout linearLayoutFloorAndTime = (LinearLayout) holder.getView(R.id.linearLayoutFloorAndTime);
                    TextView textViewFollow = (TextView) holder.getView(R.id.textViewFollow);
                    LinearLayout linearLayoutFloorAndTimeLeft = (LinearLayout) holder.getView(R.id.linearLayoutFloorAndTimeLeft);
                    LinearLayout linearLayoutComment = (LinearLayout) holder.getView(R.id.linearLayoutComment);
                    LinearLayout linearLayoutPraiseUp = (LinearLayout) holder.getView(R.id.linearLayoutPraiseUp);
                    LinearLayout linearLayoutPraiseDown = (LinearLayout) holder.getView(R.id.linearLayoutPraiseDown);
                    TextView textViewContent = (TextView) holder.getView(R.id.textViewContent);
                    ImageView imageViewComment = (ImageView) holder.getView(R.id.imageViewComment);
                    ImageView imageViewPraiseUp = (ImageView) holder.getView(R.id.imageViewPraiseUp);
                    ImageView imageViewPraiseDown = (ImageView) holder.getView(R.id.imageViewPraiseDown);
                    ImageView imageViewMore = (ImageView) holder.getView(R.id.imageViewMore);
                    FrameLayout frameLayoutLine = (FrameLayout) holder.getView(R.id.frameLayoutLine);
                    LinearLayout linearLayoutMoreHot = (LinearLayout) holder.getView(R.id.linearLayoutMoreHot);

                    linearLayoutFloorAndTime.setVisibility(View.VISIBLE);
                    textViewFollow.setVisibility(View.GONE);
                    linearLayoutFloorAndTimeLeft.setVisibility(View.GONE);
                    linearLayoutMoreHot.setVisibility(View.GONE);
                    if (comment.isShowChildComments()) {
                        frameLayoutLine.setVisibility(View.GONE);
                    } else {
                        frameLayoutLine.setVisibility(View.VISIBLE);
                    }

                    holder.setText(R.id.textViewUserName, comment.getUserName());
                    holder.setText(R.id.textViewLV, "LV" + comment.getUserLevel());
                    holder.setText(R.id.textViewChildCommentCount, String.valueOf(comment.getChildCommentCount()));
                    holder.setText(R.id.textViewPraiseUpCount, String.valueOf(comment.getPraiseUpCount()));
                    holder.setText(R.id.textViewPraiseDownCount, String.valueOf(comment.getPraiseDownCount()));
                    holder.setText(R.id.textViewTime, DataTool.getTimeRange(comment.getCreateTime()));
                    holder.setText(R.id.textViewFloor, "#" + comment.getFloor());

                    setCommentContent(comment, textViewContent);

                    setCommentDrawableTint(comment.getPraise(), imageViewComment, drawableComment, imageViewPraiseUp, drawablePraiseUpUnSelect, drawablePraiseUpSelect, imageViewPraiseDown, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary);

                    NetworkImageViewHelp.getImageFromNetwork(roundedNetworkImageViewAvatar, comment.getAvatarUrl_absolute(), baseCommentActivity.activityKey, avatar_normal_width_narrow, avatar_normal_width_narrow, R.mipmap.normal_avatar, R.mipmap.normal_avatar_error);

                    linearLayoutPraiseUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doPraise(position, baseCommentActivity, recyclerView, drawablePraiseUpUnSelect, drawablePraiseUpSelect, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary, comment, Praise.PRAISEUPORDOWN_PRAISEUP);
                        }
                    });
                    linearLayoutPraiseDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doPraise(position, baseCommentActivity, recyclerView, drawablePraiseUpUnSelect, drawablePraiseUpSelect, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary, comment, Praise.PRAISEUPORDOWN_PRAISEDOWN);
                        }
                    });
                    imageViewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentMore(baseCommentActivity, view.getRootView(), view, comment, true);
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(baseCommentActivity, ChildCommentActivity.class);
                            intent.putExtra(ChildCommentActivity.ROOTPARENT_ID_KEY, comment.get_id());
                            baseCommentActivity.startActivity(intent);
                        }
                    });
                }
            } else if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
                final Comment comment = (Comment) viewItem.getModel();
                if (comment != null) {
                    TextView textViewContent = (TextView) holder.getView(R.id.textViewContent);

                    ImageView imageViewMore = (ImageView) holder.getView(R.id.imageViewMore);
                    holder.setText(R.id.textViewUserName, comment.getUserName());
                    holder.setText(R.id.textViewTime, DataTool.getTimeRange(comment.getCreateTime()));
                    holder.setText(R.id.textViewContent, comment.getContent());

                    setCommentContent(comment, textViewContent);

                    imageViewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentMore(baseCommentActivity, view.getRootView(), view, comment, false);
                        }
                    });
                }
            } else if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3) {
                final Comment comment = (Comment) viewItem.getModel();
                if (comment != null) {
                    TextView textViewContent = (TextView) holder.getView(R.id.textViewContent);

                    ImageView imageViewMore = (ImageView) holder.getView(R.id.imageViewMore);
                    RelativeLayout relativeLayoutCommentCountTips = (RelativeLayout) holder.getView(R.id.relativeLayoutCommentCountTips);
                    TextView textViewCommentCountTips = (TextView) holder.getView(R.id.textViewCommentCountTips);

                    holder.setText(R.id.textViewUserName, comment.getUserName());
                    holder.setText(R.id.textViewTime, DataTool.getTimeRange(comment.getCreateTime()));
                    holder.setText(R.id.textViewContent, comment.getContent());

                    setCommentContent(comment, textViewContent);

                    if (!comment.isShowMoreTips()) {
                        relativeLayoutCommentCountTips.setVisibility(View.GONE);
                    } else {
                        relativeLayoutCommentCountTips.setVisibility(View.VISIBLE);
                        textViewCommentCountTips.setText(comment.getMoreTips());
                        relativeLayoutCommentCountTips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(baseCommentActivity, ChildCommentActivity.class);
                                intent.putExtra(ChildCommentActivity.ROOTPARENT_ID_KEY, comment.getRootParent_id());
                                baseCommentActivity.startActivity(intent);
                            }
                        });
                    }
                    imageViewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentMore(baseCommentActivity, view.getRootView(), view, comment, false);
                        }
                    });
                }
            }
        }
    }

    protected void showCommentMore(final BaseCommentActivity baseCommentActivity, View rootView, View view, final Comment comment, final boolean isParent) {
        final int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
        keyValueItems.add(new KeyValueItem("回复", "回复"));
        PopupWindowHelp.showPopupWindowMenuListViewMore(baseCommentActivity, rootView, view, -1, locations[1], keyValueItems, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    baseCommentActivity.commentTarget(comment, isParent);
                }
            }
        });
    }

    public static void setCommentContent(Comment comment, TextView textViewContent) {
        if (comment != null) {
            if (comment.getDepth() <= 1) {
                textViewContent.setText(comment.getContent());
            } else {
                textViewContent.setText("回复@" + comment.getTargetUserName() + ":" + comment.getContent());
            }
        }
    }

    public static void setCommentDrawableTint(Praise praise, ImageView imageViewComment, Drawable drawableComment, ImageView imageViewPraiseUp, Drawable drawablePraiseUpUnSelect, Drawable drawablePraiseUpSelect, ImageView imageViewPraiseDown, Drawable drawablePraiseDownUnSelect, Drawable drawablePraiseDownSelect, int unSelectColor, int selectColor) {
        ColorHelp.setImageViewDrawableTint(imageViewComment, drawableComment, unSelectColor);

        if (praise == null || praise.getStatus() == null) {
            ColorHelp.setImageViewDrawableTint(imageViewPraiseUp, drawablePraiseUpUnSelect, unSelectColor);
            ColorHelp.setImageViewDrawableTint(imageViewPraiseDown, drawablePraiseDownUnSelect, unSelectColor);
        } else {
            if (praise.getStatus().equals(Praise.status_praiseUp)) {
                ColorHelp.setImageViewDrawableTint(imageViewPraiseUp, drawablePraiseUpSelect, selectColor);
                ColorHelp.setImageViewDrawableTint(imageViewPraiseDown, drawablePraiseDownUnSelect, unSelectColor);
            } else if (praise.getStatus().equals(Praise.status_praiseDown)) {
                ColorHelp.setImageViewDrawableTint(imageViewPraiseUp, drawablePraiseUpUnSelect, unSelectColor);
                ColorHelp.setImageViewDrawableTint(imageViewPraiseDown, drawablePraiseDownSelect, selectColor);
            } else {
                ColorHelp.setImageViewDrawableTint(imageViewPraiseUp, drawablePraiseUpUnSelect, unSelectColor);
                ColorHelp.setImageViewDrawableTint(imageViewPraiseDown, drawablePraiseDownUnSelect, unSelectColor);
            }
        }
    }

    public static void setFollowStatus(Follow follow, TextView textViewFollow, int unFollowColor, int followColor) {
        if (follow == null || follow.getStatus() == null || follow.getStatus().equals(Follow.status_unFollow)) {
            textViewFollow.setTextColor(unFollowColor);
            textViewFollow.setText(Follow.UNFOLLOW_TIPS);
        } else {
            textViewFollow.setTextColor(followColor);
            textViewFollow.setText(Follow.FOLLOW_TIPS);
        }
    }

    public static void doPraise(final int position, final BaseActivity baseActivity, final RecyclerView recyclerView, final Drawable drawablePraiseUpUnSelect, final Drawable drawablePraiseUpSelect, final Drawable drawablePraiseDownUnSelect, final Drawable drawablePraiseDownSelect, final int unSelectColor, final int selectColor, final Comment comment, String praiseUpOrDown) {
        if (comment != null) {
            WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_doPraise(baseActivity, Praise.type_comment, comment.get_id(), praiseUpOrDown, new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                }

                @Override
                public void onErrorResponse(String error) {
                }
            }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoPraise_GsonModel.Data>() {
                @Override
                public void error(String error) {
                    BaseActivity.showShortToast(baseActivity, error);
                }

                @Override
                public void successByTips(String tips) {
                    BaseActivity.showShortToast(baseActivity, tips);
                }

                @Override
                public void success(WallpaperDetailsActivity_DoPraise_GsonModel.Data data) {
                    if (data == null || data.getPraise() == null) {
                        BaseActivity.showShortToast(baseActivity, TheApplication.SERVERERROR);
                    } else {
                        Praise praise = data.getPraise();
                        comment.setPraiseUpCount(data.getPraiseUpCount());
                        comment.setPraiseDownCount(data.getPraiseDownCount());
                        comment.setPraise(praise);
                        BaseRecyclerViewAdapter.BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewAdapter.BaseRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        if (baseRecyclerViewHolder != null) {
                            ImageView imageViewPraiseUp = (ImageView) baseRecyclerViewHolder.getView(R.id.imageViewPraiseUp);
                            ImageView imageViewPraiseDown = (ImageView) baseRecyclerViewHolder.getView(R.id.imageViewPraiseDown);
                            baseRecyclerViewHolder.setText(R.id.textViewPraiseUpCount, String.valueOf(data.getPraiseUpCount()));
                            baseRecyclerViewHolder.setText(R.id.textViewPraiseDownCount, String.valueOf(data.getPraiseDownCount()));
                            setCommentDrawableTint(praise, null, null, imageViewPraiseUp, drawablePraiseUpUnSelect, drawablePraiseUpSelect, imageViewPraiseDown, drawablePraiseDownUnSelect, drawablePraiseDownSelect, unSelectColor, selectColor);
                        }
                    }
                }
            });
        }
    }


    public static void doFollow(final int position, final BaseActivity baseActivity, final RecyclerView recyclerView, final int unFollowColor, final int followColor, final Comment comment) {
        if (comment != null) {
            WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_doFollow(baseActivity, comment.getUser_id(), new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                }

                @Override
                public void onErrorResponse(String error) {
                }
            }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoFollow_GsonModel.Data>() {
                @Override
                public void error(String error) {
                    BaseActivity.showShortToast(baseActivity, error);
                }

                @Override
                public void successByTips(String tips) {
                    BaseActivity.showShortToast(baseActivity, tips);
                }

                @Override
                public void success(WallpaperDetailsActivity_DoFollow_GsonModel.Data data) {
                    if (data == null || data.getFollow() == null) {
                        BaseActivity.showShortToast(baseActivity, TheApplication.SERVERERROR);
                    } else {
                        Follow follow = data.getFollow();
                        comment.setFollow(follow);
                        BaseRecyclerViewAdapter.BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewAdapter.BaseRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        if (baseRecyclerViewHolder != null) {
                            TextView textViewFollow = (TextView) baseRecyclerViewHolder.getView(R.id.textViewFollow);
                            setFollowStatus(follow, textViewFollow, unFollowColor, followColor);
                        }
                    }
                }
            });
        }
    }
}

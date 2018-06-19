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

import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.utils.NetworkImageViewHelp;
import com.beyondphysics.ui.views.RoundedNetworkImageView;
import com.my.beyondphysicsapplication.ChildCommentActivity;
import com.my.beyondphysicsapplication.HotCommentActivity;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.WallpaperDetailsActivity;
import com.my.beyondphysicsapplication.fragment.WallpaperDetailsActivity_Fragment_Comment;
import com.my.beyondphysicsapplication.uihelp.PopupWindowHelp;
import com.my.models.Comment;
import com.my.models.Praise;
import com.my.models.local.KeyValueItem;
import com.my.utils.DataTool;

import java.util.ArrayList;
import java.util.List;


public class WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter extends LoadMoreLayerRecyclerViewAdapter {
    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private WallpaperDetailsActivity_Fragment_Comment wallpaperDetailsActivity_Fragment_Comment;
    private int avatar_normal_width_narrow;
    private int colorPrimary;
    private int normal_comment_item_normal_help;

    private Drawable drawableComment;
    private Drawable drawablePraiseUpUnSelect;
    private Drawable drawablePraiseUpSelect;
    private Drawable drawablePraiseDownUnSelect;
    private Drawable drawablePraiseDownSelect;

    public WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter(BaseActivity baseActivity, WallpaperDetailsActivity_Fragment_Comment wallpaperDetailsActivity_Fragment_Comment, RecyclerView recyclerView, List<ViewLayerItem> datas) {
        super(datas);
        init(baseActivity, recyclerView, wallpaperDetailsActivity_Fragment_Comment);
    }

    public WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter(BaseActivity baseActivity, WallpaperDetailsActivity_Fragment_Comment wallpaperDetailsActivity_Fragment_Comment, RecyclerView recyclerView, List<ViewLayerItem> datas, int loadMoreLayoutId) {
        super(datas, loadMoreLayoutId);
        init(baseActivity, recyclerView, wallpaperDetailsActivity_Fragment_Comment);
    }

    public WallpaperDetailsActivity_Fragment_Comment_RecyclerViewAdapter(BaseActivity baseActivity, WallpaperDetailsActivity_Fragment_Comment wallpaperDetailsActivity_Fragment_Comment, RecyclerView recyclerView, List<ViewLayerItem> datas, int loadMoreLayoutId, int pageCount) {
        super(datas, loadMoreLayoutId, pageCount);
        init(baseActivity, recyclerView, wallpaperDetailsActivity_Fragment_Comment);
    }

    private void init(BaseActivity baseActivity, RecyclerView recyclerView, WallpaperDetailsActivity_Fragment_Comment wallpaperDetailsActivity_Fragment_Comment) {
        this.baseActivity = baseActivity;
        this.recyclerView = recyclerView;
        this.wallpaperDetailsActivity_Fragment_Comment = wallpaperDetailsActivity_Fragment_Comment;
        TypedArray typedArray = this.baseActivity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
        normal_comment_item_normal_help = ContextCompat.getColor(this.baseActivity, R.color.normal_comment_item_normal_help);
        avatar_normal_width_narrow = this.baseActivity.getResources().getDimensionPixelSize(R.dimen.avatar_normal_width_narrow);

        drawableComment = this.baseActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_comment);
        drawablePraiseUpUnSelect = this.baseActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_up_unselect);
        drawablePraiseUpSelect = this.baseActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_up_select);
        drawablePraiseDownUnSelect = this.baseActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_down_unselect);
        drawablePraiseDownSelect = this.baseActivity.getResources().getDrawable(R.mipmap.normal_comment_item_parent_praise_down_select);
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1 || viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE4) {
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
            if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1 || viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE4) {
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
                    TextView textViewMoreHot = (TextView) holder.getView(R.id.textViewMoreHot);

                    holder.setText(R.id.textViewUserName, comment.getUserName());
                    holder.setText(R.id.textViewLV, "LV" + comment.getUserLevel());
                    holder.setText(R.id.textViewChildCommentCount, String.valueOf(comment.getChildCommentCount()));
                    holder.setText(R.id.textViewPraiseUpCount, String.valueOf(comment.getPraiseUpCount()));
                    holder.setText(R.id.textViewPraiseDownCount, String.valueOf(comment.getPraiseDownCount()));

                    BaseCommentActivity_RecyclerViewAdapter.setCommentContent(comment, textViewContent);
                    BaseCommentActivity_RecyclerViewAdapter.setCommentDrawableTint(comment.getPraise(), imageViewComment, drawableComment, imageViewPraiseUp, drawablePraiseUpUnSelect, drawablePraiseUpSelect, imageViewPraiseDown, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary);


                    if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
                        linearLayoutFloorAndTime.setVisibility(View.VISIBLE);
                        textViewFollow.setVisibility(View.GONE);
                        linearLayoutFloorAndTimeLeft.setVisibility(View.GONE);
                        if (comment.isShowChildComments()) {
                            frameLayoutLine.setVisibility(View.GONE);
                        } else {
                            frameLayoutLine.setVisibility(View.VISIBLE);
                        }
                        linearLayoutMoreHot.setVisibility(View.GONE);
                        holder.setText(R.id.textViewTime, DataTool.getTimeRange(comment.getCreateTime()));
                        holder.setText(R.id.textViewFloor, "#" + comment.getFloor());
                    } else if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE4) {
                        linearLayoutFloorAndTime.setVisibility(View.GONE);
                        textViewFollow.setVisibility(View.VISIBLE);
                        linearLayoutFloorAndTimeLeft.setVisibility(View.VISIBLE);
                        if (comment.isShowHotMore()) {
                            frameLayoutLine.setVisibility(View.GONE);
                            linearLayoutMoreHot.setVisibility(View.VISIBLE);
                            textViewMoreHot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(baseActivity, HotCommentActivity.class);
                                    intent.putExtra(WallpaperDetailsActivity.WALLPAPER_ID_KEY, wallpaperDetailsActivity_Fragment_Comment.getWallpaper_id());
                                    baseActivity.startActivity(intent);
                                }
                            });

                        } else {
                            if (comment.isShowChildComments()) {
                                frameLayoutLine.setVisibility(View.GONE);
                            } else {
                                frameLayoutLine.setVisibility(View.VISIBLE);
                            }
                            linearLayoutMoreHot.setVisibility(View.GONE);
                        }
                        holder.setText(R.id.textViewTimeLeft, DataTool.getTimeRange(comment.getCreateTime()));
                        holder.setText(R.id.textViewFloorLeft, "#" + comment.getFloor());

                        BaseCommentActivity_RecyclerViewAdapter.setFollowStatus(comment.getFollow(), textViewFollow, colorPrimary, normal_comment_item_normal_help);

                        textViewFollow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseCommentActivity_RecyclerViewAdapter.doFollow(position, baseActivity, recyclerView, colorPrimary, normal_comment_item_normal_help, comment);
                            }
                        });
                    }
                    NetworkImageViewHelp.getImageFromNetwork(roundedNetworkImageViewAvatar, comment.getAvatarUrl_absolute(), baseActivity.activityKey, avatar_normal_width_narrow, avatar_normal_width_narrow, R.mipmap.normal_avatar, R.mipmap.normal_avatar_error);

                    linearLayoutPraiseUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BaseCommentActivity_RecyclerViewAdapter.doPraise(position, baseActivity, recyclerView, drawablePraiseUpUnSelect, drawablePraiseUpSelect, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary, comment, Praise.PRAISEUPORDOWN_PRAISEUP);
                        }
                    });
                    linearLayoutPraiseDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BaseCommentActivity_RecyclerViewAdapter.doPraise(position, baseActivity, recyclerView, drawablePraiseUpUnSelect, drawablePraiseUpSelect, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary, comment, Praise.PRAISEUPORDOWN_PRAISEDOWN);
                        }
                    });

                    imageViewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentMore(baseActivity, view.getRootView(), view, comment, true);
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(baseActivity, ChildCommentActivity.class);
                            intent.putExtra(ChildCommentActivity.ROOTPARENT_ID_KEY, comment.get_id());
                            baseActivity.startActivity(intent);
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

                    BaseCommentActivity_RecyclerViewAdapter.setCommentContent(comment, textViewContent);
                    imageViewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentMore(baseActivity, view.getRootView(), view, comment, false);
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

                    BaseCommentActivity_RecyclerViewAdapter.setCommentContent(comment, textViewContent);

                    if (!comment.isShowMoreTips()) {
                        relativeLayoutCommentCountTips.setVisibility(View.GONE);
                    } else {
                        relativeLayoutCommentCountTips.setVisibility(View.VISIBLE);
                        textViewCommentCountTips.setText(comment.getMoreTips());
                        relativeLayoutCommentCountTips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(baseActivity, ChildCommentActivity.class);
                                intent.putExtra(ChildCommentActivity.ROOTPARENT_ID_KEY, comment.getRootParent_id());
                                baseActivity.startActivity(intent);
                            }
                        });
                    }
                    imageViewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCommentMore(baseActivity, view.getRootView(), view, comment, false);
                        }
                    });
                }
            }
        }
    }

    private void showCommentMore(BaseActivity baseActivity, View rootView, View view, final Comment comment, final boolean isParent) {
        final int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
        keyValueItems.add(new KeyValueItem("回复", "回复"));
        PopupWindowHelp.showPopupWindowMenuListViewMore(baseActivity, rootView, view, -1, locations[1], keyValueItems, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    wallpaperDetailsActivity_Fragment_Comment.commentTarget(comment, isParent);
                }
            }
        });
    }

}

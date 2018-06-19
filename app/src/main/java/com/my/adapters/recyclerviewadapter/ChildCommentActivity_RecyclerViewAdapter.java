package com.my.adapters.recyclerviewadapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.utils.NetworkImageViewHelp;
import com.beyondphysics.ui.views.RoundedNetworkImageView;
import com.my.beyondphysicsapplication.BaseCommentActivity;
import com.my.beyondphysicsapplication.R;
import com.my.models.Comment;
import com.my.models.Praise;
import com.my.utils.DataTool;

import java.util.List;


public class ChildCommentActivity_RecyclerViewAdapter extends BaseCommentActivity_RecyclerViewAdapter {

    public ChildCommentActivity_RecyclerViewAdapter(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView, List<ViewLayerItem> datas) {
        super(baseCommentActivity, recyclerView, datas);
    }

    public ChildCommentActivity_RecyclerViewAdapter(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView, List<ViewLayerItem> datas, int loadMoreLayoutId) {
        super(baseCommentActivity, recyclerView, datas, loadMoreLayoutId);
    }

    public ChildCommentActivity_RecyclerViewAdapter(BaseCommentActivity baseCommentActivity, RecyclerView recyclerView, List<ViewLayerItem> datas, int loadMoreLayoutId, int pageCount) {
        super(baseCommentActivity, recyclerView, datas, loadMoreLayoutId, pageCount);
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.normal_comment_item_parent;
        } else if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
            return R.layout.normal_comment_item_parent;
        } else if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3) {
            return R.layout.normal_comment_item_count_tips;
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
            if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1 || viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
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

                    frameLayoutLine.setVisibility(View.VISIBLE);
                    linearLayoutMoreHot.setVisibility(View.GONE);

                    holder.setText(R.id.textViewUserName, comment.getUserName());
                    holder.setText(R.id.textViewLV, "LV" + comment.getUserLevel());
                    holder.setText(R.id.textViewPraiseUpCount, String.valueOf(comment.getPraiseUpCount()));
                    holder.setText(R.id.textViewPraiseDownCount, String.valueOf(comment.getPraiseDownCount()));

                    setCommentContent(comment, textViewContent);

                    setCommentDrawableTint(comment.getPraise(), imageViewComment, drawableComment, imageViewPraiseUp, drawablePraiseUpUnSelect, drawablePraiseUpSelect, imageViewPraiseDown, drawablePraiseDownUnSelect, drawablePraiseDownSelect, normal_comment_item_normal_help, colorPrimary);

                    if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
                        linearLayoutFloorAndTime.setVisibility(View.VISIBLE);
                        textViewFollow.setVisibility(View.GONE);
                        linearLayoutFloorAndTimeLeft.setVisibility(View.GONE);
                        linearLayoutComment.setVisibility(View.GONE);
                        holder.setText(R.id.textViewTime, DataTool.getTimeRange(comment.getCreateTime()));
                        holder.setText(R.id.textViewFloor, "#" + comment.getFloor());
                        imageViewMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showCommentMore(baseCommentActivity, view.getRootView(), view, comment, false);
                            }
                        });
                    } else if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
                        linearLayoutFloorAndTime.setVisibility(View.GONE);
                        textViewFollow.setVisibility(View.VISIBLE);
                        linearLayoutFloorAndTimeLeft.setVisibility(View.VISIBLE);
                        linearLayoutComment.setVisibility(View.VISIBLE);
                        holder.setText(R.id.textViewTimeLeft, DataTool.getTimeRange(comment.getCreateTime()));
                        holder.setText(R.id.textViewFloorLeft, "#" + comment.getFloor());
                        holder.setText(R.id.textViewChildCommentCount, String.valueOf(comment.getChildCommentCount()));

                        BaseCommentActivity_RecyclerViewAdapter.setFollowStatus(comment.getFollow(), textViewFollow, colorPrimary, normal_comment_item_normal_help);

                        textViewFollow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseCommentActivity_RecyclerViewAdapter.doFollow(position, baseCommentActivity, recyclerView, colorPrimary, normal_comment_item_normal_help, comment);
                            }
                        });

                        imageViewMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showCommentMore(baseCommentActivity, view.getRootView(), view, comment, true);
                            }
                        });
                    }
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

                }
            } else if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE3) {
                Comment comment = (Comment) viewItem.getModel();
                if (comment != null) {
                    holder.setText(R.id.textViewCommentCountTips, "相关回复共" + comment.getChildCommentCount() + "条");
                }
            }
        }
    }

}

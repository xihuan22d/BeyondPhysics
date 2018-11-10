package com.my.adapters.recyclerviewadapter;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyondphysics.network.Request;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewLayerItem;
import com.beyondphysics.ui.utils.NetworkImageViewHelp;
import com.beyondphysics.ui.views.NetworkImageView;
import com.beyondphysics.ui.views.RoundedNetworkImageView;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.beyondphysicsapplication.WallpaperDetailsActivity;
import com.my.beyondphysicsapplication.uihelp.BackgroundHelp;
import com.my.beyondphysicsapplication.uihelp.ColorHelp;
import com.my.modelhttpfunctions.WallpaperDetailsActivityHttpFunction;
import com.my.models.Collection;
import com.my.models.Follow;
import com.my.models.Praise;
import com.my.models.User;
import com.my.models.Wallpaper;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoCollection_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoFollow_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_DoPraise_GsonModel;
import com.my.models.net.WallpaperDetailsActivity_GetWallpaperDetails_GsonModel;
import com.my.utils.DataTool;
import com.my.utils.HttpConnectTool;
import com.my.utils.RecyclerViewItemDecorationDefault;
import com.my.utils.StringTool;

import java.util.ArrayList;
import java.util.List;


public class WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter extends LoadMoreLayerRecyclerViewAdapter {
    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private int spanCount = 2;
    private int recyclerViewSpace = 0;
    private int itemWidth = 0;
    private int avatar_normal_width_narrow;
    private int drawable_normal_stroke_narrow;

    private int colorPrimary;
    private int unSelectColor_default;


    private Drawable drawablePraiseUpUnSelect;
    private Drawable drawablePraiseUpSelect;
    private Drawable drawablePraiseDownUnSelect;
    private Drawable drawablePraiseDownSelect;
    private Drawable drawableCollectionUnSelect;
    private Drawable drawableCollectionSelect;
    private Drawable drawableDownload;

    public WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewLayerItem> datas) {
        super(datas);
        init(baseActivity, recyclerView, spanCount);
    }

    public WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewLayerItem> datas, int loadMoreLayoutId) {
        super(datas, loadMoreLayoutId);
        init(baseActivity, recyclerView, spanCount);
    }

    public WallpaperDetailsActivity_Fragment_Details_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount, List<ViewLayerItem> datas, int loadMoreLayoutId, int pageCount) {
        super(datas, loadMoreLayoutId, pageCount);
        init(baseActivity, recyclerView, spanCount);
    }

    private void init(BaseActivity baseActivity, RecyclerView recyclerView, int spanCount) {
        this.baseActivity = baseActivity;
        this.recyclerView = recyclerView;
        this.spanCount = spanCount;
        TypedArray typedArray = this.baseActivity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
        unSelectColor_default = ContextCompat.getColor(this.baseActivity, R.color.unSelectColor_default);

        recyclerViewSpace = BaseActivity.getRecyclerViewSpace_Default(this.baseActivity);
        itemWidth = (BaseActivity.getScreenWidth(this.baseActivity) - this.spanCount * 2 * recyclerViewSpace) / this.spanCount;
        avatar_normal_width_narrow = this.baseActivity.getResources().getDimensionPixelSize(R.dimen.avatar_normal_width_narrow);
        drawable_normal_stroke_narrow = this.baseActivity.getResources().getDimensionPixelSize(R.dimen.drawable_normal_stroke_narrow);

        drawablePraiseUpUnSelect = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_praise_up_unselect);
        drawablePraiseUpSelect = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_praise_up_select);
        drawablePraiseDownUnSelect = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_praise_down_unselect);
        drawablePraiseDownSelect = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_praise_down_select);
        drawableCollectionUnSelect = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_collection_unselect);
        drawableCollectionSelect = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_collection_select);
        drawableDownload = this.baseActivity.getResources().getDrawable(R.mipmap.activity_wallpaper_details_download);

    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.activity_wallpaper_details_fragment_details_item_top;
        } else if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
            return R.layout.normal_recyclerview_wallpaper_item;
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
                ViewGroup.LayoutParams viewGroupLayoutParams = holder.itemView.getLayoutParams();
                if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
                    staggeredGridLayoutManagerLayoutParams.setFullSpan(true);
                }
                final WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data data = (WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data) viewItem.getModel();
                if (data != null) {
                    Wallpaper wallpaper = data.getWallpaper();
                    User uploadUser = data.getUploadUser();

                    ImageView imageViewPraiseUp = (ImageView) holder.getView(R.id.imageViewPraiseUp);
                    ImageView imageViewPraiseDown = (ImageView) holder.getView(R.id.imageViewPraiseDown);
                    ImageView imageViewCollection = (ImageView) holder.getView(R.id.imageViewCollection);
                    ImageView imageViewDownload = (ImageView) holder.getView(R.id.imageViewDownload);

                    Button buttonFollow = (Button) holder.getView(R.id.buttonFollow);

                    if (wallpaper != null) {
                        holder.setText(R.id.textViewWallpaperName, wallpaper.getName());
                        holder.setText(R.id.textViewSize, "大小:" + StringTool.getFormatSize(wallpaper.getSize()));
                        holder.setText(R.id.textViewGoldCount, String.valueOf(wallpaper.getNeedGold()));
                        holder.setText(R.id.textViewVisitCount, String.valueOf(wallpaper.getVisitCount()));
                        holder.setText(R.id.textViewTime, DataTool.getTimeRange(wallpaper.getCreateTime()) + "上传");
                        holder.setText(R.id.textViewDescribe, "描述:" + wallpaper.getDescribe());
                        holder.setText(R.id.textViewPraiseUpCount, String.valueOf(wallpaper.getPraiseUpCount()));
                        holder.setText(R.id.textViewPraiseDownCount, String.valueOf(wallpaper.getPraiseDownCount()));
                        holder.setText(R.id.textViewCollectionCount, String.valueOf(wallpaper.getCollectionCount()));
                        holder.setText(R.id.textViewDownloadCount, String.valueOf(wallpaper.getDownloadCount()));
                        holder.setText(R.id.textViewUserName, String.valueOf(wallpaper.getUploadUser_userName()));
                    }


                    setPraiseDrawableTint(wallpaper.getPraise(), imageViewPraiseUp, drawablePraiseUpUnSelect, drawablePraiseUpSelect, imageViewPraiseDown, drawablePraiseDownUnSelect, drawablePraiseDownSelect, unSelectColor_default, colorPrimary);
                    setCollectionDrawableTint(wallpaper.getCollection(), imageViewCollection, drawableCollectionUnSelect, drawableCollectionSelect, unSelectColor_default, colorPrimary);
                    ColorHelp.setImageViewDrawableTint(imageViewDownload, drawableDownload, unSelectColor_default);
                    setFollowStatus(wallpaper.getFollow(), buttonFollow, colorPrimary, unSelectColor_default);

                    if (uploadUser != null) {
                        RoundedNetworkImageView roundedNetworkImageViewAvatar = (RoundedNetworkImageView) holder.getView(R.id.roundedNetworkImageViewAvatar);
                        NetworkImageViewHelp.getImageFromNetwork(roundedNetworkImageViewAvatar, uploadUser.getAvatarUrl_absolute(), baseActivity.activityKey, avatar_normal_width_narrow, avatar_normal_width_narrow, R.mipmap.normal_avatar, R.mipmap.normal_avatar_error);
                        holder.setText(R.id.textViewFollowCount, uploadUser.getFansCount() + "人关注");
                    }
                    String feature = wallpaper.getFeature();
                    if (feature != null) {
                        List<String> childFeatures = getChildFeatures(feature);

                        TextView[] textViews = new TextView[3];
                        textViews[0] = (TextView) holder.getView(R.id.textViewFeature1);
                        textViews[1] = (TextView) holder.getView(R.id.textViewFeature2);
                        textViews[2] = (TextView) holder.getView(R.id.textViewFeature3);
                        for (int i = 0; i < textViews.length; i++) {
                            TextView textView = textViews[i];
                            textView.setVisibility(View.INVISIBLE);
                        }
                        for (int i = 0; i < childFeatures.size(); i++) {
                            if (i > 2) {
                                break;
                            }
                            TextView textView = textViews[i];
                            final String childFeature = childFeatures.get(i);
                            textView.setVisibility(View.VISIBLE);
                            BackgroundHelp.setGradientDrawableStroke(textView, colorPrimary, drawable_normal_stroke_narrow);
                            textView.setText(childFeature);
                        }
                    }

                    imageViewPraiseUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doPraise(position, baseActivity, recyclerView, drawablePraiseUpUnSelect, drawablePraiseUpSelect, drawablePraiseDownUnSelect, drawablePraiseDownSelect, unSelectColor_default, colorPrimary, data, Praise.PRAISEUPORDOWN_PRAISEUP);
                        }
                    });
                    imageViewPraiseDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doPraise(position, baseActivity, recyclerView, drawablePraiseUpUnSelect, drawablePraiseUpSelect, drawablePraiseDownUnSelect, drawablePraiseDownSelect, unSelectColor_default, colorPrimary, data, Praise.PRAISEUPORDOWN_PRAISEDOWN);
                        }
                    });
                    imageViewCollection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doCollection(position, baseActivity, recyclerView, drawableCollectionUnSelect, drawableCollectionSelect, unSelectColor_default, colorPrimary, data);
                        }
                    });
                    imageViewDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });

                    buttonFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doFollow(position, baseActivity, recyclerView, colorPrimary, unSelectColor_default, data);
                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            } else if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE2) {
                holder.itemView.setTag(RecyclerViewItemDecorationDefault.NEEDUPDATEITEMOFFSETS_WALLPAPER_ITEM);
                Wallpaper wallpaper = (Wallpaper) viewItem.getModel();
                if (wallpaper != null) {
                    RoundedNetworkImageView roundedNetworkImageViewAvatar = (RoundedNetworkImageView) holder.getView(R.id.roundedNetworkImageViewAvatar);
                    NetworkImageView networkImageView = (NetworkImageView) holder.getView(R.id.networkImageView);
                    holder.setText(R.id.textViewWallpaperName, wallpaper.getName());
                    holder.setText(R.id.textViewVisitCount, String.valueOf(wallpaper.getVisitCount()));
                    final String title;
                    if (wallpaper.getKind() != null && wallpaper.getKind().equals(Wallpaper.kind_video)) {
                        title = wallpaper.getName() + "(视频)";
                    } else {
                        title = wallpaper.getName() + "(图片)";
                    }
                    NetworkImageViewHelp.getImageFromNetwork(roundedNetworkImageViewAvatar, wallpaper.getUploadUser_avatarUrl_absolute(), baseActivity.activityKey, avatar_normal_width_narrow, avatar_normal_width_narrow, R.mipmap.normal_avatar, R.mipmap.normal_avatar_error);
                    int previewImageWidth = itemWidth;
                    int previewImageHeight = previewImageWidth;
                    if (wallpaper.getPreviewImageWidth() != 0) {
                        previewImageHeight = (int) (previewImageWidth * (wallpaper.getPreviewImageHeight() / wallpaper.getPreviewImageWidth()));
                    }
                    networkImageView.setOpenUpdateScaleType(true);
                    networkImageView.setUpdateScaleTypeWhenGetBitmap(ImageView.ScaleType.FIT_XY);
                    NetworkImageViewHelp.getImageFromNetworkWithNewParams(networkImageView, wallpaper.getPreviewImageUrl_absolute(), baseActivity.activityKey, previewImageWidth, previewImageHeight, R.mipmap.normal_loading, R.mipmap.normal_loading_error);

                    final String wallpaper_id = wallpaper.get_id();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(baseActivity, WallpaperDetailsActivity.class);
                            intent.putExtra(WallpaperDetailsActivity.WALLPAPER_ID_KEY, wallpaper_id);
                            intent.putExtra(WallpaperDetailsActivity.TITLE_KEY, title);
                            baseActivity.startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    private List<String> getChildFeatures(String feature) {
        List<String> resultChildFeatures = new ArrayList<String>();
        if (feature == null) {
            return resultChildFeatures;
        }
        feature = feature.replaceAll(HttpConnectTool.featureNeedReplaceSplitSign, HttpConnectTool.featureSplitSign);
        String[] childFeatures = feature.split(HttpConnectTool.featureSplitSign);
        for (int i = 0; i < childFeatures.length; i++) {
            String childFeature = childFeatures[i];
            if (!childFeature.equals("")) {
                resultChildFeatures.add(childFeature);
            }
        }
        return resultChildFeatures;
    }

    private void setPraiseDrawableTint(Praise praise, ImageView imageViewPraiseUp, Drawable drawablePraiseUpUnSelect, Drawable drawablePraiseUpSelect, ImageView imageViewPraiseDown, Drawable drawablePraiseDownUnSelect, Drawable drawablePraiseDownSelect, int unSelectColor, int selectColor) {
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

    private void setCollectionDrawableTint(Collection collection, ImageView imageViewCollection, Drawable drawableCollectionUnSelect, Drawable drawableCollectionSelect, int unSelectColor, int selectColor) {
        if (collection == null || collection.getStatus() == null) {
            ColorHelp.setImageViewDrawableTint(imageViewCollection, drawableCollectionUnSelect, unSelectColor);
        } else {
            if (collection.getStatus().equals(Collection.status_collection)) {
                ColorHelp.setImageViewDrawableTint(imageViewCollection, drawableCollectionSelect, selectColor);
            } else {
                ColorHelp.setImageViewDrawableTint(imageViewCollection, drawableCollectionUnSelect, unSelectColor);
            }
        }
    }

    private void doPraise(final int position, final BaseActivity baseActivity, final RecyclerView recyclerView, final Drawable drawablePraiseUpUnSelect, final Drawable drawablePraiseUpSelect, final Drawable drawablePraiseDownUnSelect, final Drawable drawablePraiseDownSelect, final int unSelectColor, final int selectColor, final WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data theData, String praiseUpOrDown) {
        if (theData != null && theData.getWallpaper() != null) {
            final Wallpaper wallpaper = theData.getWallpaper();
            WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_doPraise(baseActivity, Praise.type_wallpaper, wallpaper.get_id(), praiseUpOrDown, new Request.OnResponseListener<String>() {
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
                        wallpaper.setPraiseUpCount(data.getPraiseUpCount());
                        wallpaper.setPraiseDownCount(data.getPraiseDownCount());
                        wallpaper.setPraise(praise);
                        BaseRecyclerViewAdapter.BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewAdapter.BaseRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        if (baseRecyclerViewHolder != null) {
                            ImageView imageViewPraiseUp = (ImageView) baseRecyclerViewHolder.getView(R.id.imageViewPraiseUp);
                            ImageView imageViewPraiseDown = (ImageView) baseRecyclerViewHolder.getView(R.id.imageViewPraiseDown);
                            baseRecyclerViewHolder.setText(R.id.textViewPraiseUpCount, String.valueOf(data.getPraiseUpCount()));
                            baseRecyclerViewHolder.setText(R.id.textViewPraiseDownCount, String.valueOf(data.getPraiseDownCount()));
                            setPraiseDrawableTint(praise, imageViewPraiseUp, drawablePraiseUpUnSelect, drawablePraiseUpSelect, imageViewPraiseDown, drawablePraiseDownUnSelect, drawablePraiseDownSelect, unSelectColor, selectColor);
                        }
                    }
                }
            });
        }
    }

    private void doCollection(final int position, final BaseActivity baseActivity, final RecyclerView recyclerView, final Drawable drawableCollectionUnSelect, final Drawable drawableCollectionSelect, final int unSelectColor, final int selectColor, final WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data theData) {
        if (theData != null && theData.getWallpaper() != null) {
            final Wallpaper wallpaper = theData.getWallpaper();
            WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_doCollection(baseActivity, wallpaper.get_id(), new Request.OnResponseListener<String>() {
                @Override
                public void onSuccessResponse(String response) {
                }

                @Override
                public void onErrorResponse(String error) {
                }
            }, new BaseGsonModel.OnBaseGsonModelListener<WallpaperDetailsActivity_DoCollection_GsonModel.Data>() {
                @Override
                public void error(String error) {
                    BaseActivity.showShortToast(baseActivity, error);
                }

                @Override
                public void successByTips(String tips) {
                    BaseActivity.showShortToast(baseActivity, tips);
                }

                @Override
                public void success(WallpaperDetailsActivity_DoCollection_GsonModel.Data data) {
                    if (data == null || data.getCollection() == null) {
                        BaseActivity.showShortToast(baseActivity, TheApplication.SERVERERROR);
                    } else {
                        Collection collection = data.getCollection();
                        wallpaper.setCollectionCount(data.getCollectionCount());
                        wallpaper.setCollection(collection);
                        BaseRecyclerViewAdapter.BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewAdapter.BaseRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        if (baseRecyclerViewHolder != null) {
                            ImageView imageViewCollection = (ImageView) baseRecyclerViewHolder.getView(R.id.imageViewCollection);
                            baseRecyclerViewHolder.setText(R.id.textViewCollectionCount, String.valueOf(data.getCollectionCount()));
                            setCollectionDrawableTint(data.getCollection(), imageViewCollection, drawableCollectionUnSelect, drawableCollectionSelect, unSelectColor, selectColor);
                        }
                    }
                }
            });
        }
    }


    private void doFollow(final int position, final BaseActivity baseActivity, final RecyclerView recyclerView, final int unFollowColor, final int followColor, final WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.Data theData) {
        if (theData != null && theData.getWallpaper() != null) {
            final Wallpaper wallpaper = theData.getWallpaper();
            WallpaperDetailsActivityHttpFunction.wallpaperDetailsActivity_doFollow(baseActivity, wallpaper.getUploadUser_id(), new Request.OnResponseListener<String>() {
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
                        User uploadUser = theData.getUploadUser();
                        uploadUser.setFansCount(data.getTargetUserFansCount());
                        wallpaper.setFollow(follow);

                        BaseRecyclerViewAdapter.BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewAdapter.BaseRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        if (baseRecyclerViewHolder != null) {
                            Button buttonFollow = (Button) baseRecyclerViewHolder.getView(R.id.buttonFollow);
                            baseRecyclerViewHolder.setText(R.id.textViewFollowCount, String.valueOf(data.getTargetUserFansCount()) + "人关注");
                            setFollowStatus(follow, buttonFollow, unFollowColor, followColor);
                        }
                    }
                }
            });
        }
    }

    private void setFollowStatus(Follow follow, Button buttonFollow, int unFollowColor, int followColor) {
        if (follow == null || follow.getStatus() == null || follow.getStatus().equals(Follow.status_unFollow)) {
            BackgroundHelp.setGradientDrawableColor(buttonFollow, unFollowColor);
            buttonFollow.setText(Follow.UNFOLLOW_TIPS);
        } else {
            BackgroundHelp.setGradientDrawableColor(buttonFollow, followColor);
            buttonFollow.setText(Follow.FOLLOW_TIPS);
        }
    }

}

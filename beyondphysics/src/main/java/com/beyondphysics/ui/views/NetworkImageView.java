package com.beyondphysics.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.beyondphysics.network.BitmapConfig;
import com.beyondphysics.network.BitmapRequest;
import com.beyondphysics.network.BitmapRequest_Default;
import com.beyondphysics.network.BitmapRequest_Default_Params;
import com.beyondphysics.network.BitmapResponse;
import com.beyondphysics.network.Request;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.RequestManagerUseDispatcher;
import com.beyondphysics.network.http.HttpResponse;
import com.beyondphysics.network.utils.SuperBitmap;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;

/**
 * Created by xihuan22 on 2017/9/15.
 */

public class NetworkImageView extends AppCompatImageView {
    private Context context;
    private BeyondPhysicsManager beyondPhysicsManager;

    private boolean useImageResId = true;
    private int defaultImageResId = 0;
    private int errorImageResId = 0;
    private Bitmap defaultImageBitmap;
    private Bitmap errorImageBitmap;

    private boolean haveChangeBitmap = true;//bitmap是否被改变了,每次在onSuccess里面设置图片后会被置为false
    private boolean openSameKeyFilter = true;//默认开启相同key(key由urlString,width,height,scaleType组成)过滤功能,可以提高一定性能
    private ScaleType defaultAndErrorScaleType = ScaleType.CENTER;//默认为CENTER,可自行设置
    private boolean openUpdateScaleType = false;//true表示可以修改尺寸,默认不可修改
    private ScaleType updateScaleTypeWhenGetBitmap;
    private boolean needUpdateScaleTypeWhenGetBitmap = false;//每次调用setUpdateScaleTypeWhenGetBitmap方法后重置为true
    private boolean openLoadAnimation = true;
    private Animation loadAnimation;
    private BitmapRequest_Default_Params oldBitmapRequest_Default_Params;
    private BitmapRequest<?> bitmapRequest;

    private boolean cancelRequestWhenOnDetachedFromWindow = true;
    private boolean fixedRecyclerViewBug = true;
    private OnNetworkImageViewResponseListener onNetworkImageViewResponseListener;

    /**
     * 为了解决recyclerView最外面项移出屏幕后再移进来不会触发OnBindViewHolder的bug,引入该变量可以减少不必要的请求,因为OnBindViewHolder会在onAttachedToWindow之前执行,
     * 所以如果fromOnBindViewHolder为true,就不执行onAttachedToWindow里面恢复方法,反之则重新调用请求恢复onDetachedFromWindow里面取消的请求
     * 如果OnBindViewHolder内存在post之类的异步操作,该优化将失效
     */
    private boolean fromOnBindViewHolder = false;

    private boolean haveGetBitmap = false;


    public NetworkImageView(Context context) {
        super(context);
        init(context);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        beyondPhysicsManager = BaseActivity.getBeyondPhysicsManager(context);
        loadAnimation = new AlphaAnimation(0.1f, 1.0f);
        loadAnimation.setDuration(200);
    }


    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        haveChangeBitmap = true;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        haveChangeBitmap = true;
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        haveChangeBitmap = true;
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        haveChangeBitmap = true;
    }

    public BitmapConfig getBitmapConfig() {
        return RequestManager.getDefaultBitmapConfig();
    }


    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public void getImageWithPost(final String urlString, final String tag) {
        post(new Runnable() {
            @Override
            public void run() {
                int width = getWidth();
                int height = getHeight();
                BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
                bitmapRequest_Default_Params.setUrlString(urlString);
                bitmapRequest_Default_Params.setTag(tag);
                bitmapRequest_Default_Params.setWidth(width);
                bitmapRequest_Default_Params.setHeight(height);
                bitmapRequest_Default_Params.setBitmapConfig(getBitmapConfig());
                bitmapRequest_Default_Params.setContext(context);
                getImage(bitmapRequest_Default_Params);
            }
        });
    }

    /**
     * 不建议在复用的view内使用,因为复用的view显示的时候会先显示旧的图片再显示默认图片
     */
    public void getImageWithPost(final String urlString, final String tag, final ScaleType scaleType) {
        post(new Runnable() {
            @Override
            public void run() {
                int width = getWidth();
                int height = getHeight();
                BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
                bitmapRequest_Default_Params.setUrlString(urlString);
                bitmapRequest_Default_Params.setTag(tag);
                bitmapRequest_Default_Params.setWidth(width);
                bitmapRequest_Default_Params.setHeight(height);
                bitmapRequest_Default_Params.setScaleType(scaleType);
                bitmapRequest_Default_Params.setBitmapConfig(getBitmapConfig());
                bitmapRequest_Default_Params.setContext(context);
                getImage(bitmapRequest_Default_Params);
            }
        });
    }


    public void getImage(String urlString, String tag) {
        BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
        bitmapRequest_Default_Params.setUrlString(urlString);
        bitmapRequest_Default_Params.setTag(tag);
        bitmapRequest_Default_Params.setWidth(0);
        bitmapRequest_Default_Params.setHeight(0);//获取最大尺寸的图片
        bitmapRequest_Default_Params.setBitmapConfig(getBitmapConfig());
        bitmapRequest_Default_Params.setContext(context);
        getImage(bitmapRequest_Default_Params);
    }

    public void getImage(String urlString, String tag, ScaleType scaleType) {
        BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
        bitmapRequest_Default_Params.setUrlString(urlString);
        bitmapRequest_Default_Params.setTag(tag);
        bitmapRequest_Default_Params.setWidth(0);
        bitmapRequest_Default_Params.setHeight(0);
        bitmapRequest_Default_Params.setScaleType(scaleType);
        bitmapRequest_Default_Params.setBitmapConfig(getBitmapConfig());
        bitmapRequest_Default_Params.setContext(context);
        getImage(bitmapRequest_Default_Params);
    }

    public void getImage(String urlString, String tag, int width, int height) {
        BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
        bitmapRequest_Default_Params.setUrlString(urlString);
        bitmapRequest_Default_Params.setTag(tag);
        bitmapRequest_Default_Params.setWidth(width);
        bitmapRequest_Default_Params.setHeight(height);
        bitmapRequest_Default_Params.setBitmapConfig(getBitmapConfig());
        bitmapRequest_Default_Params.setContext(context);
        getImage(bitmapRequest_Default_Params);
    }

    public void getImage(String urlString, String tag, int width, int height, ScaleType scaleType) {
        BitmapRequest_Default_Params bitmapRequest_Default_Params = new BitmapRequest_Default_Params();
        bitmapRequest_Default_Params.setUrlString(urlString);
        bitmapRequest_Default_Params.setTag(tag);
        bitmapRequest_Default_Params.setWidth(width);
        bitmapRequest_Default_Params.setHeight(height);
        bitmapRequest_Default_Params.setScaleType(scaleType);
        bitmapRequest_Default_Params.setBitmapConfig(getBitmapConfig());
        bitmapRequest_Default_Params.setContext(context);
        getImage(bitmapRequest_Default_Params);
    }

    public void getImage(BitmapRequest_Default_Params bitmapRequest_Default_Params) {
        fromOnBindViewHolder = true;
        doBitmapRequest(bitmapRequest_Default_Params, true);
    }

    private void getImageFromOnAttachedToWindow(BitmapRequest_Default_Params bitmapRequest_Default_Params) {
        needUpdateScaleTypeWhenGetBitmap = true;
        doBitmapRequest(bitmapRequest_Default_Params, false);
    }


    private void doBitmapRequest(BitmapRequest_Default_Params bitmapRequest_Default_Params, boolean canFilter) {
        if (bitmapRequest_Default_Params == null) {
            return;
        }
        String urlString = bitmapRequest_Default_Params.getUrlString();
        if (urlString == null || urlString.equals("")) {
            cancelRequest();
            if (openUpdateScaleType) {
                setScaleType(defaultAndErrorScaleType);
            }
            if (useImageResId) {
                setImageResource(defaultImageResId);
            } else {
                setImageBitmap(defaultImageBitmap);
            }
            return;
        }
        String key = RequestManager.getBitmapRequestKey(urlString, bitmapRequest_Default_Params.getWidth(), bitmapRequest_Default_Params.getHeight(), bitmapRequest_Default_Params.getScaleType(), bitmapRequest_Default_Params.getBitmapConfig());
        if (canFilter && openSameKeyFilter && !haveChangeBitmap && oldBitmapRequest_Default_Params != null) {
            String oldKey = RequestManager.getBitmapRequestKey(oldBitmapRequest_Default_Params.getUrlString(), oldBitmapRequest_Default_Params.getWidth(), oldBitmapRequest_Default_Params.getHeight(), oldBitmapRequest_Default_Params.getScaleType(), bitmapRequest_Default_Params.getBitmapConfig());
            if (key != null && oldKey != null && key.equals(oldKey)) {
                //继续使用原图,也不要修改尺寸
                return;
            }
        }
        cancelRequest();
        if (openUpdateScaleType) {
            setScaleType(defaultAndErrorScaleType);
        }
        if (useImageResId) {
            setImageResource(defaultImageResId);
        } else {
            setImageBitmap(defaultImageBitmap);
        }
        Request.OnResponseListener<BitmapResponse> onResponseListener = bitmapRequest_Default_Params.getOnResponseListener();
        if (onResponseListener == null) {
            onResponseListener = new Request.OnResponseListener<BitmapResponse>() {
                @Override
                public void onSuccessResponse(BitmapResponse response) {
                    if (response != null && response.getSuperBitmap() != null) {
                        if (openUpdateScaleType) {//必须先开启允许修改尺寸
                            if (needUpdateScaleTypeWhenGetBitmap) {
                                if (updateScaleTypeWhenGetBitmap != null) {
                                    setScaleType(updateScaleTypeWhenGetBitmap);
                                }
                                needUpdateScaleTypeWhenGetBitmap = false;
                            }
                        }
                        SuperBitmap superBitmap = response.getSuperBitmap();
                        if (!superBitmap.isDecodeGif()) {
                            setImageBitmap(response.getSuperBitmap().getBitmap());
                        }
                        if (openLoadAnimation && (response.getBitmapGetFrom() == HttpResponse.BITMAP_GETFROM_NETWORK || response.getBitmapGetFrom() == HttpResponse.BITMAP_GETFROM_FILE || response.getBitmapGetFrom() == HttpResponse.BITMAP_GETFROM_ASSETS || response.getBitmapGetFrom() == HttpResponse.BITMAP_GETFROM_RESOURCE)) {
                            clearAnimation();
                            if (loadAnimation != null) {
                                startAnimation(loadAnimation);
                            }
                        }
                        haveChangeBitmap = false;//因为setImageBitmap会修改为true,一定要放在后面
                    }
                    bitmapRequest = null;
                    haveGetBitmap = true;
                    if (onNetworkImageViewResponseListener != null) {
                        onNetworkImageViewResponseListener.onSuccessResponse(response);
                    }
                }

                @Override
                public void onErrorResponse(String error) {
                    if (openUpdateScaleType) {
                        setScaleType(defaultAndErrorScaleType);
                    }
                    if (useImageResId) {
                        setImageResource(errorImageResId);
                    } else {
                        setImageBitmap(errorImageBitmap);
                    }
                    bitmapRequest = null;
                    oldBitmapRequest_Default_Params = null;
                    if (onNetworkImageViewResponseListener != null) {
                        onNetworkImageViewResponseListener.onErrorResponse(error);
                    }
                }
            };
            bitmapRequest_Default_Params.setOnResponseListener(onResponseListener);
        }

        bitmapRequest = getBitmapRequest(bitmapRequest_Default_Params);
        addOrCancelBitmapRequest(1, bitmapRequest);
        oldBitmapRequest_Default_Params = bitmapRequest_Default_Params;
    }

    public BitmapRequest<?> getBitmapRequest(BitmapRequest_Default_Params bitmapRequest_Default_Params) {
        return new BitmapRequest_Default(bitmapRequest_Default_Params);
    }


    /**
     * 比如用户头像在用户退出登录后需要改变成默认头像,这时候就需要先取消请求,再设置成默认头像,这样就不会导致未执行请求重新回来了
     */
    public void cancelRequest() {
        if (bitmapRequest != null) {
            addOrCancelBitmapRequest(2, bitmapRequest);//如果没有使用RequestManagerUseDispatcher,该方法可以提升较大的流畅性,因为putAndSortRequest数据越多,其中的this锁占用时间越大,即时的移除可以减少数据个数
            bitmapRequest = null;
        }
        oldBitmapRequest_Default_Params = null;
        haveGetBitmap = false;
    }

    public void cancelRequestButKeepOldRequestParams() {
        if (bitmapRequest != null) {
            addOrCancelBitmapRequest(2, bitmapRequest);
            bitmapRequest = null;
        }
    }

    private void addOrCancelBitmapRequest(int type, BitmapRequest<?> bitmapRequest) {
        RequestManager requestManager = beyondPhysicsManager.getRequestManager();
        RequestManagerUseDispatcher requestManagerUseDispatcher = null;
        if (requestManager instanceof RequestManagerUseDispatcher) {
            requestManagerUseDispatcher = (RequestManagerUseDispatcher) requestManager;
        }
        if (requestManagerUseDispatcher != null) {
            if (type == 1) {
                requestManagerUseDispatcher.addBitmapRequestToDispatcher(bitmapRequest);
            } else if (type == 2) {
                requestManagerUseDispatcher.cancelBitmapRequestWithRequestToDispatcher(bitmapRequest, true);
            }
        } else {
            if (type == 1) {
                requestManager.addBitmapRequest(bitmapRequest);
            } else if (type == 2) {
                requestManager.cancelBitmapRequestWithRequest(bitmapRequest, true);
            }
        }
    }


    public boolean isUseImageResId() {
        return useImageResId;
    }

    public void setUseImageResId(boolean useImageResId) {
        this.useImageResId = useImageResId;
    }

    public int getDefaultImageResId() {
        return defaultImageResId;
    }

    public void setDefaultImageResId(int defaultImageResId) {
        this.defaultImageResId = defaultImageResId;
    }

    public int getErrorImageResId() {
        return errorImageResId;
    }

    public void setErrorImageResId(int errorImageResId) {
        this.errorImageResId = errorImageResId;
    }

    public Bitmap getDefaultImageBitmap() {
        return defaultImageBitmap;
    }

    public void setDefaultImageBitmap(Bitmap defaultImageBitmap) {
        this.defaultImageBitmap = defaultImageBitmap;
    }

    public Bitmap getErrorImageBitmap() {
        return errorImageBitmap;
    }

    public void setErrorImageBitmap(Bitmap errorImageBitmap) {
        this.errorImageBitmap = errorImageBitmap;
    }

    public boolean isOpenSameKeyFilter() {
        return openSameKeyFilter;
    }

    public void setOpenSameKeyFilter(boolean openSameKeyFilter) {
        this.openSameKeyFilter = openSameKeyFilter;
    }

    public ScaleType getDefaultAndErrorScaleType() {
        return defaultAndErrorScaleType;
    }

    public void setDefaultAndErrorScaleType(ScaleType defaultAndErrorScaleType) {
        this.defaultAndErrorScaleType = defaultAndErrorScaleType;
    }

    public boolean isOpenUpdateScaleType() {
        return openUpdateScaleType;
    }

    public void setOpenUpdateScaleType(boolean openUpdateScaleType) {
        this.openUpdateScaleType = openUpdateScaleType;
    }

    public ScaleType getUpdateScaleTypeWhenGetBitmap() {
        return updateScaleTypeWhenGetBitmap;
    }

    public void setUpdateScaleTypeWhenGetBitmap(ScaleType scaleType) {
        updateScaleTypeWhenGetBitmap = scaleType;
        needUpdateScaleTypeWhenGetBitmap = true;
    }

    public boolean isOpenLoadAnimation() {
        return openLoadAnimation;
    }

    public void setOpenLoadAnimation(boolean openLoadAnimation) {
        this.openLoadAnimation = openLoadAnimation;
    }

    public Animation getLoadAnimation() {
        return loadAnimation;
    }

    public void setLoadAnimation(Animation loadAnimation) {
        this.loadAnimation = loadAnimation;
    }

    public boolean isCancelRequestWhenOnDetachedFromWindow() {
        return cancelRequestWhenOnDetachedFromWindow;
    }

    public void setCancelRequestWhenOnDetachedFromWindow(boolean cancelRequestWhenOnDetachedFromWindow) {
        this.cancelRequestWhenOnDetachedFromWindow = cancelRequestWhenOnDetachedFromWindow;
    }

    public boolean isFixedRecyclerViewBug() {
        return fixedRecyclerViewBug;
    }

    public void setFixedRecyclerViewBug(boolean fixedRecyclerViewBug) {
        this.fixedRecyclerViewBug = fixedRecyclerViewBug;
    }

    public OnNetworkImageViewResponseListener getOnNetworkImageViewResponseListener() {
        return onNetworkImageViewResponseListener;
    }

    public void setOnNetworkImageViewResponseListener(OnNetworkImageViewResponseListener onNetworkImageViewResponseListener) {
        this.onNetworkImageViewResponseListener = onNetworkImageViewResponseListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!haveGetBitmap && cancelRequestWhenOnDetachedFromWindow && fixedRecyclerViewBug && !fromOnBindViewHolder && oldBitmapRequest_Default_Params != null) {
            getImageFromOnAttachedToWindow(oldBitmapRequest_Default_Params);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (cancelRequestWhenOnDetachedFromWindow) {
            cancelRequestButKeepOldRequestParams();
        }
        fromOnBindViewHolder = false;
        super.onDetachedFromWindow();
    }
}

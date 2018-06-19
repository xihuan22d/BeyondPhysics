package com.my.beyondphysicsapplication;

import android.app.Application;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.beyondphysics.ui.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.my.utils.CatchBugManagerWithSend;

import java.io.File;
import java.util.List;


public class TheApplication extends Application {

    public static final String DEFAULT_ROOT_PATH = "BeyondPhysicsApplication";
    public static final String UPLOADCACHE_DIR_NAME = "uploadCache";

    public static final boolean SHOWTIMEOUTTIPS = true;

    public static final String[] CONNECTNETTIPS = {"正在给服务器使眼色......", "正在尝试买通服务器......", "正在跟服务器讨价还价......", "正在往服务器口袋塞毛爷爷......", "正在跟服务器探讨人生......", "正在跟服务器玩摇一摇......", "正在跟服务器商讨怎么偷地瓜......", "正在羞辱服务器......"};


    public static final String NETOPENERROR = "网络异常,请检查网络状态!";

    public static final String NETTIMEOUTERROR = "请求超时!";

    public static final String CANCELTIPS = "请求已取消!";


    public static final String WITHOUTCACHE = "暂无缓存!";


    public static final String SERVERERROR = "与服务器通信异常,请检查是否有新版本!";

    public static final int AUTOREFRESHDELAY = 0;

    public static final int LOADMOREDELAY = 200;

    public static final int LOADMOREERRORDELAY = 1000;


    private static TheApplication theApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        CatchBugManagerWithSend.getInstance(this);
        RequestManager.openDebug = true;
        theApplication = this;
    }

    public static TheApplication getTheApplication() {
        return theApplication;
    }


    public static void setColorSchemeColors(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(0xfff2a670, 0xffff82a5, 0xfffd584b, 0xff94db41);
    }


    public static void setCursorToLast(EditText editText) {
        CharSequence charSequence = editText.getText();
        if (charSequence != null && charSequence instanceof Spannable) {
            Spannable spannable = (Spannable) charSequence;
            Selection.setSelection(spannable, spannable.length());
        }
    }

    public static void setLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != -1) {
            layoutParams.width = width;
        }
        if (height != -1) {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    public static <T> void addAllFormBaseRecyclerViewAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter, List<T> newDatas) {
        if (baseRecyclerViewAdapter != null) {
            baseRecyclerViewAdapter.addAll(newDatas);
        }
    }

    public static <T> void replaceAllFormBaseRecyclerViewAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter, List<T> newDatas, RecyclerView recyclerView) {
        if (baseRecyclerViewAdapter != null) {
            baseRecyclerViewAdapter.replaceAllWithScrollTop(newDatas,recyclerView);
        }
    }

    public static <T> void loadMoreErrorFormBaseRecyclerViewAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter) {
        if (baseRecyclerViewAdapter != null) {
            if (baseRecyclerViewAdapter instanceof LoadMoreRecyclerViewAdapter) {
                LoadMoreRecyclerViewAdapter loadMoreRecyclerViewAdapter = (LoadMoreRecyclerViewAdapter) baseRecyclerViewAdapter;
                loadMoreRecyclerViewAdapter.loadMoreError();
            } else if (baseRecyclerViewAdapter instanceof LoadMoreLayerRecyclerViewAdapter) {
                LoadMoreLayerRecyclerViewAdapter loadMoreLayerRecyclerViewAdapter = (LoadMoreLayerRecyclerViewAdapter) baseRecyclerViewAdapter;
                loadMoreLayerRecyclerViewAdapter.loadMoreError();
            }
        }
    }

    public static String getFilesDirRootPath(Context context) {
        File fileCacheDir = new File(context.getFilesDir(), DEFAULT_ROOT_PATH);
        String path = fileCacheDir.getAbsolutePath();
        if (!fileCacheDir.exists()) {
            FileTool.mkdirs(path);
        }
        return path;
    }

    public static String getUploadCachePath(Context context) {
        String uploadCachePath = null;
        String rootPath = getFilesDirRootPath(context);
        if (rootPath != null) {
            uploadCachePath = rootPath + File.separator + UPLOADCACHE_DIR_NAME;
            File fileUploadCacheDir = new File(uploadCachePath);
            if (!fileUploadCacheDir.exists()) {
                fileUploadCacheDir.mkdir();
            }
        }
        return uploadCachePath;
    }

}

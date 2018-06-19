package com.beyondphysics.ui.recyclerviewlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.beyondphysics.R;
import com.beyondphysics.network.RequestStatusItem;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.recyclerviewlibrary.models.ViewItem;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;

/**
 * Created by xihuan22 on 2016/3/8.
 */
public class BaseRecyclerViewFromFrameLayout extends FrameLayout {
    private Context context;
    //attrs参数初始化,为BaseRecyclerViewFromFrameLayout(Context context)构造方法配置默认值
    private int mainId = R.layout.beyondphysics_layout_main;
    private int emptyId = 0;
    private boolean clipToPadding = false;
    private int padding = -1;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int layoutManager = 1;
    private int spanCount = 1;
    private int layoutManagerOrientation = 1;
    private int scrollbarStyle = 0;

    //一些view的初始化
    private FrameLayout frameLayoutEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    private boolean openBitmapScrollOptimization = false;

    private RequestStatusItem requestStatusItem;

    /**
     * 使用默认值
     */
    public BaseRecyclerViewFromFrameLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BaseRecyclerViewFromFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        initView();
    }

    public BaseRecyclerViewFromFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.extendedRecyclerView);
        try {
            mainId = typedArray.getResourceId(R.styleable.extendedRecyclerView_layout_main, R.layout.beyondphysics_layout_main);
            emptyId = typedArray.getResourceId(R.styleable.extendedRecyclerView_layout_empty, 0);
            clipToPadding = typedArray.getBoolean(R.styleable.extendedRecyclerView_recyclerClipToPadding, false);
            padding = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPadding, -1.0f);
            paddingLeft = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingLeft, 0.0f);
            paddingRight = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingRight, 0.0f);
            paddingTop = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingTop, 0.0f);
            paddingBottom = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingBottom, 0.0f);
            layoutManager = typedArray.getInt(R.styleable.extendedRecyclerView_recyclerLayoutManager, 1);
            spanCount = typedArray.getInt(R.styleable.extendedRecyclerView_recyclerSpanCount, 1);
            layoutManagerOrientation = typedArray.getInt(R.styleable.extendedRecyclerView_recyclerLayoutManagerOrientation, 1);
            scrollbarStyle = typedArray.getInt(R.styleable.extendedRecyclerView_scrollbarStyle, 0);
        } finally {
            typedArray.recycle();
        }
    }

    private void initView() {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(getContext()).inflate(mainId, this, true);
            frameLayoutEmpty = (FrameLayout) view.findViewById(R.id.frameLayoutEmpty);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            initFrameLayoutEmpty(frameLayoutEmpty);
            initSwipeRefreshLayout(swipeRefreshLayout);
            initRecyclerView(recyclerView);
        }
    }

    private void initFrameLayoutEmpty(FrameLayout frameLayoutEmpty) {
        if (emptyId != 0) {
            LayoutInflater.from(getContext()).inflate(emptyId, frameLayoutEmpty, true);
        }
        frameLayoutEmpty.setVisibility(View.GONE);
    }

    private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(0xfff2a670, 0xffff82a5, 0xfffd584b, 0xff94db41);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(clipToPadding);
        if (padding != -1) {
            recyclerView.setPadding(padding, padding, padding, padding);
        } else {
            recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
        if (scrollbarStyle == 1) {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        } else if (scrollbarStyle == 2) {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        } else if (scrollbarStyle == 3) {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        } else {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        }
        initLayoutManager();
    }

    private void initLayoutManager() {
        if (layoutManager == 3) {
            if (layoutManagerOrientation == 1) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL));
            }
        } else if (layoutManager == 2) {
            GridLayoutManager gridLayoutManager = null;
            if (layoutManagerOrientation == 1) {
                gridLayoutManager = new GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false);
            } else {
                gridLayoutManager = new GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false);
            }
            recyclerView.setLayoutManager(gridLayoutManager);
            final int theSpanCount = spanCount;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RecyclerView.Adapter recyclerViewAdapter = recyclerView.getAdapter();
                    if (recyclerViewAdapter != null) {
                        switch (recyclerViewAdapter.getItemViewType(position)) {
                            case ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED:
                                return theSpanCount;
                            case ViewItem.VIEW_TYPE_ITEM_LOAD_MORE:
                                return theSpanCount;
                            case ViewItem.VIEW_TYPE_ITEM_VIEWPAGER:
                                return theSpanCount;
                            default:
                                return 1;
                        }
                    }
                    return -1;
                }
            });
        } else {
            if (layoutManagerOrientation == 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }

    /**
     * 如果数据为空就显示空白页,adapter一次赋值后不应该再修改
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return;
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        //例如执行notifyItemRangeInserted(0, 0)这种情况,依然会触发下面的方法的,所以很靠谱的
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                update();
            }

            //可以简化下拉刷新控件的控制,而且能保证数据更新完毕后才设置下拉完成
            private void update() {
                swipeRefreshLayout.setRefreshing(false);
                if (emptyId != 0) {
                    if (recyclerView.getAdapter().getItemCount() == 0) {//adapter一定不为null,是null就return了不初始化
                        frameLayoutEmpty.setVisibility(View.VISIBLE);
                    } else {
                        frameLayoutEmpty.setVisibility(View.GONE);
                    }
                }
            }
        });
        if (emptyId != 0) {
            if (adapter.getItemCount() == 0) {
                frameLayoutEmpty.setVisibility(View.VISIBLE);
            } else {
                frameLayoutEmpty.setVisibility(View.GONE);
            }
        }
    }

    public FrameLayout getFrameLayoutEmpty() {
        return frameLayoutEmpty;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this.onRefreshListener);
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public void autoRefresh() {
        if (onRefreshListener != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            });
        }
    }

    public void autoRefresh(Handler handler, long delayMillis) {
        if (handler != null && onRefreshListener != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            }, delayMillis);
        }
    }

    /**
     * 图片显示优化,SCROLL_STATE_IDLE后恢复网络框架的图片显示
     */
    public void openBitmapScrollOptimization(final BaseActivity baseActivity) {
        if (!openBitmapScrollOptimization) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (requestStatusItem != null) {
                            BeyondPhysicsManager.getInstance(baseActivity).removeRequestStatusItem(requestStatusItem);
                            requestStatusItem = null;
                        }

                    } else {
                        if (requestStatusItem == null) {
                            requestStatusItem = BeyondPhysicsManager.getInstance(baseActivity).addRequestStatusItem(RequestStatusItem.STATUS_PAUSE, RequestStatusItem.KIND_BITMAP_REQUEST, baseActivity.activityKey);
                        }
                    }
                }
            });
            openBitmapScrollOptimization = true;
        }

    }
}

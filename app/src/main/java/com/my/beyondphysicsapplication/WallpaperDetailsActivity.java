package com.my.beyondphysicsapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.NetworkImageViewHelp;
import com.beyondphysics.ui.views.NetworkImageView;
import com.my.adapters.viewpageradapter.WallpaperDetailsActivity_ViewPagerAdapter;
import com.my.beyondphysicsapplication.fragment.WallpaperDetailsActivity_Fragment_Comment;

import java.util.List;


public class WallpaperDetailsActivity extends NewBaseActivity {

    public static final String WALLPAPER_ID_KEY = "wallpaper_id_key";
    public static final String TITLE_KEY = "title_key";

    private String wallpaper_id;
    private String title;

    private String[] titles;

    private int activity_wallpaper_details_collapsingToolbarLayout_height;

    private AppBarLayout appBarLayout;
    private ImageView imageViewBack;
    private TextView textViewToolbarTitle;
    private NetworkImageView networkImageViewPreview;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private View.OnClickListener onClickListener;

    private WallpaperDetailsActivity_ViewPagerAdapter wallpaperDetailsActivity_ViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_details);
        Intent intent = getIntent();
        wallpaper_id = intent.getStringExtra(WALLPAPER_ID_KEY);
        title = intent.getStringExtra(TITLE_KEY);
        setWindowType(1);
        initAll();
    }


    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        titles = getResources().getStringArray(R.array.activity_wallpaper_details_tabLayout_titles);
        activity_wallpaper_details_collapsingToolbarLayout_height = getResources().getDimensionPixelSize(R.dimen.activity_wallpaper_details_collapsingToolbarLayout_height);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewToolbarTitle = (TextView) findViewById(R.id.textViewToolbarTitle);
        networkImageViewPreview = (NetworkImageView) findViewById(R.id.networkImageViewPreview);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) <= appBarLayout.getTotalScrollRange() / 2) {
                    floatingActionButton.animate().scaleX(1f).scaleY(1f).setInterpolator(new OvershootInterpolator()).start();
                    floatingActionButton.setClickable(true);
                } else {
                    floatingActionButton.animate().scaleX(0f).scaleY(0f).setInterpolator(new AccelerateInterpolator()).start();
                    floatingActionButton.setClickable(false);
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    List<Fragment> fragments = WallpaperDetailsActivity.this.getSupportFragmentManager().getFragments();
                    if (fragments != null) {
                        for (int i = 0; i < fragments.size(); i++) {
                            Fragment fragment = fragments.get(i);
                            if (fragment != null && fragment instanceof WallpaperDetailsActivity_Fragment_Comment) {
                                WallpaperDetailsActivity_Fragment_Comment wallpaperDetailsActivity_Fragment_Comment = (WallpaperDetailsActivity_Fragment_Comment) fragment;
                                wallpaperDetailsActivity_Fragment_Comment.doRequestFocus();
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewToolbarTitle.setText(title);
        wallpaperDetailsActivity_ViewPagerAdapter = new WallpaperDetailsActivity_ViewPagerAdapter(getSupportFragmentManager(), titles, wallpaper_id);
        viewPager.setAdapter(wallpaperDetailsActivity_ViewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initHttp() {
    }

    @Override
    protected void initOther() {
    }


    private void onClickListener() {
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

    public void updateInformation(String previewImageUrl_absolute, long commentCount) {
        networkImageViewPreview.setOpenUpdateScaleType(true);
        networkImageViewPreview.setUpdateScaleTypeWhenGetBitmap(ImageView.ScaleType.FIT_CENTER);
        NetworkImageViewHelp.getImageFromNetwork(networkImageViewPreview, previewImageUrl_absolute, activityKey, BaseActivity.getScreenWidth(WallpaperDetailsActivity.this), activity_wallpaper_details_collapsingToolbarLayout_height, R.mipmap.normal_loading, R.mipmap.normal_loading_error);

        if (titles.length >= 2 && tabLayout.getTabCount() >= 2) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.setText(titles[1] + "(" + commentCount + ")");
        }
    }

    public void updateTabLayoutCommentCount(long commentCount) {
        if (titles.length >= 2 && tabLayout.getTabCount() >= 2) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.setText(titles[1] + "(" + commentCount + ")");
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }


}

package com.my.adapters.viewpageradapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.my.beyondphysicsapplication.WallpaperDetailsActivity;
import com.my.beyondphysicsapplication.fragment.BaseFragment;
import com.my.beyondphysicsapplication.fragment.WallpaperDetailsActivity_Fragment_Comment;
import com.my.beyondphysicsapplication.fragment.WallpaperDetailsActivity_Fragment_Details;


public class WallpaperDetailsActivity_ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private String wallpaper_id;

    public WallpaperDetailsActivity_ViewPagerAdapter(FragmentManager fragmentManager, String[] titles, String wallpaper_id) {
        super(fragmentManager);
        if (titles == null) {
            throw new NullPointerException("titles为null");
        }
        this.titles = titles;
        this.wallpaper_id = wallpaper_id;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.BUNDLEKEY_POSITION_KEY, position);
        Fragment fragment = null;
        if (position == 0) {
            fragment = new WallpaperDetailsActivity_Fragment_Details();
            bundle.putString(WallpaperDetailsActivity.WALLPAPER_ID_KEY, wallpaper_id);
            fragment.setArguments(bundle);
        } else if (position == 1) {
            fragment = new WallpaperDetailsActivity_Fragment_Comment();
            bundle.putString(WallpaperDetailsActivity.WALLPAPER_ID_KEY, wallpaper_id);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
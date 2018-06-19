package com.my.adapters.girdviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.NetworkGifImageViewHelp;
import com.beyondphysics.ui.views.NetworkGifImageView;
import com.my.beyondphysicsapplication.PictureChooseActivity;
import com.my.beyondphysicsapplication.R;

import java.util.ArrayList;
import java.util.List;


public class PictureChooseActivity_GirdViewAdapter extends BaseAdapter {
    private BaseActivity baseActivity;
    private List<String> paths;
    private LayoutInflater layoutInflater;
    private OnPictureChooseListener onPictureChooseListener;


    public PictureChooseActivity_GirdViewAdapter(BaseActivity baseActivity, List<String> paths, OnPictureChooseListener onPictureChooseListener) {
        this.baseActivity = baseActivity;
        if (paths == null) {
            paths = new ArrayList<String>();
        }
        this.paths = paths;
        layoutInflater = this.baseActivity.getLayoutInflater();
        this.onPictureChooseListener = onPictureChooseListener;

    }

    public void addPaths(List<String> paths) {
        if (paths != null) {
            this.paths.addAll(paths);
        }
    }

    @Override
    public int getCount() {
        if (paths.size() >= PictureChooseActivity.CHOOSECOUNT) {
            return PictureChooseActivity.CHOOSECOUNT;
        } else {
            return paths.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.activity_picture_choose_item, parent, false);
            viewHolder.networkGifImageView = (NetworkGifImageView) convertView.findViewById(R.id.networkGifImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == paths.size()) {
            viewHolder.networkGifImageView.cancelRequest();
            viewHolder.networkGifImageView.setImageResource(R.mipmap.activity_picture_choose_add);
            viewHolder.networkGifImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPictureChooseListener != null) {
                        onPictureChooseListener.addClick();
                    }
                }
            });
        } else {
            final String path = paths.get(position);
            NetworkGifImageViewHelp.getImageFromDiskCache(viewHolder.networkGifImageView, path, 1, baseActivity.activityKey, 0, 0);
            viewHolder.networkGifImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPictureChooseListener != null) {
                        onPictureChooseListener.chooseClick(path);
                    }
                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        private NetworkGifImageView networkGifImageView;
    }

    public interface OnPictureChooseListener {

        void addClick();

        void chooseClick(String path);
    }

}

package com.beyondphysics.ui.imagechooselibrary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beyondphysics.R;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.imagechooselibrary.models.ImageFolder;
import com.beyondphysics.ui.utils.NetworkGifImageViewHelp;
import com.beyondphysics.ui.views.NetworkGifImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2017/7/7.
 */

public class ChooseImageActivity_ListViewAdapter extends BaseAdapter {
    private BaseActivity baseActivity;
    private int selectPosition = 0;
    private List<ImageFolder> imageFolders;
    private ListView listView;
    private LayoutInflater layoutInflater;
    private int beyondPhysics_activity_chooseimage_popupwindow_selecttype_item_thumbnail_width;

    public ChooseImageActivity_ListViewAdapter(BaseActivity baseActivity, int selectPosition, List<ImageFolder> imageFolders, ListView listView, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        this.baseActivity = baseActivity;
        this.selectPosition = selectPosition;
        if (imageFolders == null) {
            imageFolders = new ArrayList<ImageFolder>();
        }
        this.imageFolders = imageFolders;
        this.listView = listView;
        layoutInflater = this.baseActivity.getLayoutInflater();
        beyondPhysics_activity_chooseimage_popupwindow_selecttype_item_thumbnail_width = this.baseActivity.getResources().getDimensionPixelSize(
                R.dimen.beyondPhysics_activity_chooseimage_popupwindow_selecttype_item_thumbnail_width);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != ChooseImageActivity_ListViewAdapter.this.selectPosition) {
                    ViewHolder selectViewHolder = getItemViewViewHolderIfVisible(ChooseImageActivity_ListViewAdapter.this.selectPosition, ChooseImageActivity_ListViewAdapter.this.listView);
                    ViewHolder viewHolder = getItemViewViewHolderIfVisible(position, ChooseImageActivity_ListViewAdapter.this.listView);
                    if (selectViewHolder != null) {
                        selectViewHolder.imageViewSelect.setVisibility(View.INVISIBLE);
                    }
                    if (viewHolder != null) {
                        viewHolder.imageViewSelect.setVisibility(View.VISIBLE);
                    }
                    ChooseImageActivity_ListViewAdapter.this.selectPosition = position;
                    if (adapterViewOnItemClickListener != null) {
                        adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                    }
                }
            }
        });
    }

    public void addImageFolders(List<ImageFolder> imageFolders) {
        if (imageFolders != null) {
            this.imageFolders.addAll(imageFolders);
        }
    }

    @Override
    public int getCount() {
        return imageFolders.size();
    }

    @Override
    public Object getItem(int position) {
        return imageFolders.get(position);
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
            convertView = layoutInflater.inflate(R.layout.beyondphysics_activity_chooseimage_popupwindow_selecttype_item, parent, false);
            viewHolder.networkGifImageViewThumbnail = (NetworkGifImageView) convertView.findViewById(R.id.networkGifImageViewThumbnail);
            viewHolder.textViewFolderName = (TextView) convertView.findViewById(R.id.textViewFolderName);
            viewHolder.textViewCount = (TextView) convertView.findViewById(R.id.textViewCount);
            viewHolder.imageViewSelect = (ImageView) convertView.findViewById(R.id.imageViewSelect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageFolder imageFolder = imageFolders.get(position);
        if (imageFolder != null && imageFolder.getImageItemThumbnail() != null) {
            NetworkGifImageViewHelp.getImageFromDiskCache(viewHolder.networkGifImageViewThumbnail, imageFolder.getImageItemThumbnail().getPath(), 1, baseActivity.activityKey, beyondPhysics_activity_chooseimage_popupwindow_selecttype_item_thumbnail_width, beyondPhysics_activity_chooseimage_popupwindow_selecttype_item_thumbnail_width, 0, 0);
            viewHolder.textViewFolderName.setText(imageFolder.getName());
            int count = 0;
            if (imageFolder.getImageItems() != null) {
                count = imageFolder.getImageItems().size();
            }
            viewHolder.textViewCount.setText("共" + count + "张");
            if (position == selectPosition) {
                viewHolder.imageViewSelect.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imageViewSelect.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        private NetworkGifImageView networkGifImageViewThumbnail;
        private TextView textViewFolderName;
        private TextView textViewCount;
        private ImageView imageViewSelect;
    }

    private ViewHolder getItemViewViewHolderIfVisible(int position, ListView listView) {
        View itemView = getItemViewIfVisible(position, listView, false);
        if (itemView != null) {
            ViewHolder viewHolder = (ViewHolder) itemView.getTag();
            return viewHolder;
        } else {
            return null;
        }
    }

    private View getItemViewIfVisible(int position, ListView listView, boolean haveHead) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = null;
            if (haveHead) {
                view = listView.getChildAt(position - firstVisiblePosition + 1);//因为实际上listView的child项只有几项,listView加上了header所以需要+1
            } else {
                view = listView.getChildAt(position - firstVisiblePosition);
            }
            return view;
        } else {
            return null;
        }
    }
}

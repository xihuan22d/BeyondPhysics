package com.my.adapters.listviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.beyondphysics.ui.BaseActivity;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.uihelp.ListViewHelp;
import com.my.models.local.KeyValueItem;

import java.util.ArrayList;
import java.util.List;


public class Normal_String_ListViewAdapter extends BaseAdapter {
    private BaseActivity baseActivity;
    private int selectPosition = -1;
    private List<KeyValueItem> keyValueItems;
    private ListView listView;
    private LayoutInflater layoutInflater;

    public Normal_String_ListViewAdapter(BaseActivity baseActivity, int selectPosition, List<KeyValueItem> keyValueItems, ListView listView, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        this.baseActivity = baseActivity;
        this.selectPosition = selectPosition;
        if (keyValueItems == null) {
            keyValueItems = new ArrayList<KeyValueItem>();
        }
        this.keyValueItems = keyValueItems;
        this.listView = listView;
        layoutInflater = this.baseActivity.getLayoutInflater();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selectViewHolder = getItemViewViewHolderIfVisible(Normal_String_ListViewAdapter.this.selectPosition, Normal_String_ListViewAdapter.this.listView);
                ViewHolder viewHolder = getItemViewViewHolderIfVisible(position, Normal_String_ListViewAdapter.this.listView);
                if (selectViewHolder != null) {
                    selectViewHolder.radioButton.setChecked(false);
                }
                if (viewHolder != null) {
                    viewHolder.radioButton.setChecked(true);
                }
                Normal_String_ListViewAdapter.this.selectPosition = position;
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    public void addKeyValueItems(List<KeyValueItem> keyValueItems) {
        if (keyValueItems != null) {
            this.keyValueItems.addAll(keyValueItems);
        }
    }

    public List<KeyValueItem> getKeyValueItems() {
        return keyValueItems;
    }

    @Override
    public int getCount() {
        return keyValueItems.size();
    }

    @Override
    public Object getItem(int position) {
        return keyValueItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.popupwindow_normal_listview_string_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KeyValueItem keyValueItem = keyValueItems.get(position);
        if (keyValueItem != null) {
            viewHolder.textView.setText(keyValueItem.getKey());
            if (position == selectPosition) {
                viewHolder.radioButton.setChecked(true);
            } else {
                viewHolder.radioButton.setChecked(false);
            }
        }
        return convertView;
    }

    public ViewHolder getItemViewViewHolderIfVisible(int position, ListView listView) {
        View itemView = ListViewHelp.getItemViewIfVisible(position, listView, false);
        if (itemView != null) {
            ViewHolder viewHolder = (ViewHolder) itemView.getTag();
            return viewHolder;
        } else {
            return null;
        }
    }

    public class ViewHolder {
        private TextView textView;
        private RadioButton radioButton;
    }

}

package com.my.adapters.listviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beyondphysics.ui.BaseActivity;
import com.my.beyondphysicsapplication.R;
import com.my.models.local.KeyValueItem;

import java.util.ArrayList;
import java.util.List;


public class Normal_Comment_More_String_ListViewAdapter extends BaseAdapter {
    private BaseActivity baseActivity;
    private List<KeyValueItem> keyValueItems;
    private ListView listView;
    private LayoutInflater layoutInflater;

    public Normal_Comment_More_String_ListViewAdapter(BaseActivity baseActivity, List<KeyValueItem> keyValueItems, ListView listView, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        this.baseActivity = baseActivity;
        if (keyValueItems == null) {
            keyValueItems = new ArrayList<KeyValueItem>();
        }
        this.keyValueItems = keyValueItems;
        this.listView = listView;
        layoutInflater = this.baseActivity.getLayoutInflater();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            convertView = layoutInflater.inflate(R.layout.popupwindow_menu_listview_more_string_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KeyValueItem keyValueItem = keyValueItems.get(position);
        if (keyValueItem != null) {
            viewHolder.textView.setText(keyValueItem.getKey());
        }
        return convertView;
    }


    public class ViewHolder {
        private TextView textView;
    }

}

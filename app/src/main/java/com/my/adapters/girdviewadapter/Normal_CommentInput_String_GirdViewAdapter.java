package com.my.adapters.girdviewadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.my.beyondphysicsapplication.R;

import java.util.ArrayList;
import java.util.List;


public class Normal_CommentInput_String_GirdViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<String> strings;
    private GridView gridView;
    private LayoutInflater layoutInflater;

    public Normal_CommentInput_String_GirdViewAdapter(Activity activity, List<String> strings, GridView gridView, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        this.activity = activity;
        if (strings == null) {
            strings = new ArrayList<String>();
        }
        this.strings = strings;
        this.gridView = gridView;
        layoutInflater = this.activity.getLayoutInflater();
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    public void addStrings(List<String> strings) {
        if (strings != null) {
            this.strings.addAll(strings);
        }
    }

    public List<String> getStrings() {
        return strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
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
            convertView = layoutInflater.inflate(R.layout.normal_comment_input_girdview_string_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(strings.get(position));
        return convertView;
    }


    public class ViewHolder {
        private TextView textView;
    }

}

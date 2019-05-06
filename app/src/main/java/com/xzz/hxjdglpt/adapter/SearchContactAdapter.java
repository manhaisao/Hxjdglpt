package com.xzz.hxjdglpt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.GridZrr;
import com.xzz.hxjdglpt.model.User;

import java.util.List;

/**
 * Created by dbz on 2017/7/26.
 */

public class SearchContactAdapter extends BaseAdapter {

    private List<User> mSearchList;

    private LayoutInflater mInflater;

    public SearchContactAdapter(LayoutInflater mInflater, List<User> mSearchList) {
        this.mSearchList = mSearchList;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return mSearchList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SearchContactAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (SearchContactAdapter.ViewHolder) view.getTag();
        } else {
            viewHolder = new SearchContactAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.dialog_item_contact, null);
            viewHolder.contactName = (TextView) view.findViewById(R.id.dialog_contact_name);
            viewHolder.cb = (CheckBox) view.findViewById(R.id.dialog_item_checkBox);
            view.setTag(viewHolder);
        }
        viewHolder.contactName.setText(mSearchList.get(position).getRealName());
        viewHolder.cb.setChecked(false);
        viewHolder.cb.setTag(mSearchList.get(position).getUserId());

        return view;
    }

    private class ViewHolder {
        TextView contactName;
        CheckBox cb;
    }
}

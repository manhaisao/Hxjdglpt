package com.xzz.hxjdglpt.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.GridZrr;

import java.util.List;

/**
 * Created by dbz on 2017/7/26.
 */

public class SearchContactGridZrrAdapter extends BaseAdapter {

    private List<GridZrr> mSearchList;

    private LayoutInflater mInflater;

    public SearchContactGridZrrAdapter(LayoutInflater mInflater, List<GridZrr> mSearchList) {
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
        SearchContactGridZrrAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (SearchContactGridZrrAdapter.ViewHolder) view.getTag();
        } else {
            viewHolder = new SearchContactGridZrrAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.dialog_item_contact, null);
            viewHolder.contactName = (TextView) view.findViewById(R.id.dialog_contact_name);
            viewHolder.cb = (CheckBox) view.findViewById(R.id.dialog_item_checkBox);
            view.setTag(viewHolder);
        }
        StringBuffer sb = new StringBuffer();
        if (mSearchList.get(position).getvName() == null || TextUtils.isEmpty(mSearchList.get
                (position).getvName()) || mSearchList.get(position).getGridId() == null ||
                TextUtils.isEmpty(mSearchList.get(position).getGridId())) {
            sb.append(mSearchList.get(position).getRealName());
        } else {
            sb.append(mSearchList.get(position).getRealName()).append("（").append(mSearchList.get
                    (position).getvName()).append("，").append(mSearchList.get(position).getGridId
                    ()).append("）");
        }
        viewHolder.contactName.setText(sb.toString());
        viewHolder.cb.setChecked(false);
        viewHolder.cb.setTag(mSearchList.get(position).getUserId());

        return view;
    }

    private class ViewHolder {
        TextView contactName;
        CheckBox cb;
    }
}

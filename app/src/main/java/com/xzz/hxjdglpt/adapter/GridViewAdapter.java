package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.utils.LogUtil;

import java.util.List;

/**
 * Created by chen on 2018/9/12.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private GridView mGv;
    private List<PartyMember> list;

    public GridViewAdapter(Context context, GridView gv, List<PartyMember> list) {
        this.context = context;
        this.mGv = gv;
        this.list = list;

    }

    @Override
    public int getCount() {
        if (null != list) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_text, null);
            holder.tv = (TextView) convertView.findViewById(R.id.gzdt_title);
            //设置标题
            holder.tv.setText(list.get(position).getName());
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        //高度计算
//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                mGv.getHeight() / 3);
//
//        convertView.setLayoutParams(param);
        return convertView;
    }

    class Holder {
        TextView tv;
    }

}

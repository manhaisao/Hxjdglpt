package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Gzdt;
import com.xzz.hxjdglpt.model.Mqztcgl;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class MqztcglAdapter extends BaseAdapter {

    private List<Mqztcgl> list = new ArrayList<Mqztcgl>();
    private Context context;
    private String twrXm;

    public MqztcglAdapter(Context context, List<Mqztcgl> list, String twrXm) {
        this.list = list;
        this.context = context;
        this.twrXm = twrXm;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.mqztcgl_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.mqztc_item_title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //区分1：回答人2：回复人
        StringBuffer content = new StringBuffer();
        if (list.get(position).getType() == 1) {
            content.append("<font color='#20B2AA'>").append(list.get(position).getWtrname()).append("：</font>").append(list.get(position).getWt());
        } else if (list.get(position).getType() == 2) {
            content.append("<font color='#20B2AA'>").append(list.get(position).getWtrname()).append("</font>回复").append("<font color='#20B2AA'>").append(twrXm).append("：</font>").append(list.get(position).getWt());
        }
        holder.tvTitle.setText(Html.fromHtml(content.toString()));
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
    }

}

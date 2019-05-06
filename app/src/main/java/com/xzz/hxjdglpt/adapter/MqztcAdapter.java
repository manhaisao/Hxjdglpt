package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Mqztc;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 民情直通车适配器
 *
 * @author dbz
 */
public class MqztcAdapter extends BaseAdapter {

    private List<Mqztc> list = new ArrayList<Mqztc>();
    private Context context;

    public MqztcAdapter(Context context, List<Mqztc> list) {
        this.list = list;
        this.context = context;
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
            convertView = View.inflate(context, R.layout.mqztc_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.mqztc_title);
            holder.tvContent = (TextView) convertView.findViewById(R.id.mqztc_content);
            holder.tvIndex = (TextView) convertView.findViewById(R.id.mqztc_index);
            holder.lines = convertView.findViewById(R.id.mqztc_lines);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        if (!TextUtils.isEmpty(list.get(position).getAnswer())) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(list.get(position).getAnswer());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
        holder.tvIndex.setText(String.valueOf(position + 1) + ".");
        if (position + 1 == list.size()) {
            holder.lines.setVisibility(View.GONE);
        } else {
            holder.lines.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvIndex;
        private View lines;
    }

}

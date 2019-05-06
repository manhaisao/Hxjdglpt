package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Qd;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻动态适配器
 *
 * @author dbz
 */
public class SignAdapter extends BaseAdapter {

    private List<Qd> list = new ArrayList<Qd>();
    private Context context;

    public SignAdapter(Context context, List<Qd> list) {
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
            convertView = View.inflate(context, R.layout.sign_item, null);
            holder.tvNo = (TextView) convertView.findViewById(R.id.sign_no);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.sign_address);
            holder.tvTime = (TextView) convertView.findViewById(R.id.sign_time);
            holder.tvBz = (TextView) convertView.findViewById(R.id.sign_bz);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (list.get(position).getType() == 1) {
            holder.tvNo.setText("第" + (list.size() - position) + "次签到");
        } else if (list.get(position).getType() == 2) {
            holder.tvNo.setText("第" + (list.size() - position) + "次请假");
        }
        holder.tvAddress.setText(list.get(position).getAddress());
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvBz.setText(list.get(position).getBz());
        return convertView;
    }

    public static class Holder {
        private TextView tvNo;
        private TextView tvTime;
        private TextView tvAddress;
        private TextView tvBz;
    }

}

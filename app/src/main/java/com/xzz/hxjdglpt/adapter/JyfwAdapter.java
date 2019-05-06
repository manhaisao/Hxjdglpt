package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Jyfw;
import com.xzz.hxjdglpt.model.Worklog;
import com.xzz.hxjdglpt.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class JyfwAdapter extends BaseAdapter {

    private List<Jyfw> list = new ArrayList<Jyfw>();
    private Context context;

    public JyfwAdapter(Context context, List<Jyfw> list) {
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
            convertView = View.inflate(context, R.layout.jyfw_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.jyfw_item_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.jyfw_item_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvTime.setText(list.get(position).getFbsj().substring(0, 10));
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvTime;
    }

}

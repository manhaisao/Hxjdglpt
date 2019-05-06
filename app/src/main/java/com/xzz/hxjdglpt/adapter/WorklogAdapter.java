package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Worklog;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class WorklogAdapter extends BaseAdapter {

    private List<Worklog> list = new ArrayList<Worklog>();
    private Context context;

    public WorklogAdapter(Context context, List<Worklog> list) {
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
            convertView = View.inflate(context, R.layout.task_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.task_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.task_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getName());
        holder.tvTime.setText(list.get(position).getTime());
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvTime;
    }

}

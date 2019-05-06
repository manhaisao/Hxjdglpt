package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.GzdtVillageDj;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class GzdtVillageDjAdapter extends BaseAdapter {

    private List<GzdtVillageDj> list = new ArrayList<GzdtVillageDj>();
    private Context context;

    private String[] types;

    public GzdtVillageDjAdapter(Context context, List<GzdtVillageDj> list, String[] types) {
        this.list = list;
        this.context = context;
        this.types = types;
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
            convertView = View.inflate(context, R.layout.gzdt_dj_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.gzdt_title);
            holder.tvType = (TextView) convertView.findViewById(R.id.gzdt_type);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        if (list.get(position).getType() == 1) {
            holder.tvType.setText(types[0]);
        } else if (list.get(position).getType() == 2) {
            holder.tvType.setText(types[1]);
        } else if (list.get(position).getType() == 3) {
            holder.tvType.setText(types[2]);
        } else if (list.get(position).getType() == 4) {
            holder.tvType.setText(types[3]);
        }
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvType;
    }

}

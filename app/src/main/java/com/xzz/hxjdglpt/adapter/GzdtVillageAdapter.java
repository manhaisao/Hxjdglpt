package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Gzdt;
import com.xzz.hxjdglpt.model.GzdtVillage;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class GzdtVillageAdapter extends BaseAdapter {

    private List<GzdtVillage> list = new ArrayList<GzdtVillage>();
    private Context context;

    public GzdtVillageAdapter(Context context, List<GzdtVillage> list) {
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
            convertView = View.inflate(context, R.layout.gzdt_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.gzdt_title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
    }

}

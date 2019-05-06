package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.PositionDetail;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.DwType;
import com.xzz.hxjdglpt.model.Village;

import java.util.ArrayList;
import java.util.List;

/**
 * 村展示适配器
 *
 * @author dbz
 */
public class DwDetailAdapter extends BaseAdapter {

    private List<DwType> list = new ArrayList<DwType>();
    private Context context;

    public DwDetailAdapter(Context context, List<DwType> list) {
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
            convertView = View.inflate(context, R.layout.item_village, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.village_item_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvName.setText(list.get(position).getPositionType());
        return convertView;
    }

    public static class Holder {
        private TextView tvName;
    }

}

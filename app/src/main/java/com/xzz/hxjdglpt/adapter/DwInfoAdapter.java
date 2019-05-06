package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.xzz.hxjdglpt.PositionDetail;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Qfhx;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dbz
 */
public class DwInfoAdapter extends BaseAdapter {

    private List<PositionDetail> list = new ArrayList<PositionDetail>();
    private Context context;
    private ImageLoader mImageLoader;

    public DwInfoAdapter(Context context, List<PositionDetail> list) {
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
            convertView = View.inflate(context, R.layout.dwinfo_item, null);
            holder.cityinfo_d_cun = (TextView) convertView.findViewById(R.id.cityinfo_d_cun);
            holder.cityinfo_d_grid = (TextView) convertView.findViewById(R.id.cityinfo_d_grid);
            holder.cityinfo_d_dy = (TextView) convertView.findViewById(R.id.cityinfo_d_dy);
            holder.cityinfo_d_username = (TextView) convertView.findViewById(R.id.cityinfo_d_username);
            holder.cityinfo_d_phone = (TextView) convertView.findViewById(R.id.cityinfo_d_phone);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.cityinfo_d_cun.setText(list.get(position).getPositionName());
        holder.cityinfo_d_grid.setText(list.get(position).getVillage());
        holder.cityinfo_d_dy.setText(list.get(position).getAddress());
        holder.cityinfo_d_username.setText(list.get(position).getResponsibleUser());
        holder.cityinfo_d_phone.setText(list.get(position).getTel());
        return convertView;
    }

    public static class Holder {
        private TextView cityinfo_d_cun;
        private TextView cityinfo_d_grid;
        private TextView cityinfo_d_dy;
        private TextView cityinfo_d_username;
        private TextView cityinfo_d_phone;
    }

}

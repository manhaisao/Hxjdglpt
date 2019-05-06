package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Mqztc;
import com.xzz.hxjdglpt.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class UserListAdapter extends BaseAdapter {

    private List<User> list = new ArrayList<User>();
    private Context context;

    public UserListAdapter(Context context, List<User> list) {
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
            convertView = View.inflate(context, R.layout.user_pop_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.user_pop_name);
            holder.ivChoose = (ImageView) convertView.findViewById(R.id.user_pop_img);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).getRealName())) {
            StringBuffer sb = new StringBuffer();
            sb.append(list.get(position).getRealName()).append("（").append(list.get(position)
                    .getUserName()).append("）");
            holder.tvName.setText(sb.toString());
        } else {
            holder.tvName.setText(list.get(position).getUserName());
        }
        return convertView;
    }

    public static class Holder {
        private TextView tvName;
        private ImageView ivChoose;
    }

}

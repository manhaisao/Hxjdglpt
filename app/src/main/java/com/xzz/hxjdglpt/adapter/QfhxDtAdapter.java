package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Qfhx;
import com.xzz.hxjdglpt.model.TRdxw;
import com.xzz.hxjdglpt.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbz
 */
public class QfhxDtAdapter extends BaseAdapter {

    private List<Qfhx> list = new ArrayList<Qfhx>();
    private Context context;

    public QfhxDtAdapter(Context context, List<Qfhx> list) {
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
            convertView = View.inflate(context, R.layout.rdzjdt_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.rddt_item_title);
            holder.tvUser = (TextView) convertView.findViewById(R.id.rddt_item_user);
            holder.tvTime = (TextView) convertView.findViewById(R.id.rddt_item_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        String user = "发布人:" + list.get(position).getRealName();
        String time = "创建时间:" + list.get(position).getCreateTime();
        LogUtil.i("user/" + user + "/time" + time);
        holder.tvUser.setText(user);
        holder.tvTime.setText(time);
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvUser;
        private TextView tvTime;
    }

}

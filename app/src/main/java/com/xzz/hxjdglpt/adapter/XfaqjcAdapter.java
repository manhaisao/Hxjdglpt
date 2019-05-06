package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Xfaqjc;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class XfaqjcAdapter extends BaseAdapter {

    private List<Xfaqjc> list = new ArrayList<Xfaqjc>();
    private Context context;

    public XfaqjcAdapter(Context context, List<Xfaqjc> list) {
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
        Xfaqjc xfaqjc = list.get(position);
        StringBuffer sb = new StringBuffer();
        if(xfaqjc.getJcstime() != null&&xfaqjc.getJcstime().length() >= 10)
        sb.append(xfaqjc.getJcstime().substring(0, 10));
        if (xfaqjc.getType() == 1) {
            sb.append("消防安全检查");
        } else if (xfaqjc.getType() == 2) {
            sb.append("安全生产检查");
        }
        holder.tvTitle.setText(sb.toString());
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
    }

}

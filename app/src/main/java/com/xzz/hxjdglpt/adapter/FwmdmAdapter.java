package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Fwmdm;
import com.xzz.hxjdglpt.model.News;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务面对面适配器
 *
 * @author dbz
 */
public class FwmdmAdapter extends BaseAdapter {

    private List<Fwmdm> list = new ArrayList<Fwmdm>();
    private Context context;

    public FwmdmAdapter(Context context, List<Fwmdm> list) {
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
            convertView = View.inflate(context, R.layout.fwmdm_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.fwmdm_title);
            holder.tvContent = (TextView) convertView.findViewById(R.id.fwmdm_content);
            holder.tvIndex = (TextView) convertView.findViewById(R.id.fwmdm_index);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        if (!TextUtils.isEmpty(list.get(position).getAnswer())) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(list.get(position).getAnswer());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
        holder.tvIndex.setText(String.valueOf(position + 1) + ".");
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvIndex;
    }

}

package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.model.Cpbz;
import com.xzz.hxjdglpt.model.TDbfc;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbz
 */
public class CpbzAdapter extends BaseAdapter {

    private List<Cpbz> list = new ArrayList<Cpbz>();
    private Context context;
    private ImageLoader mImageLoader;

    public CpbzAdapter(Context context, List<Cpbz> list, ImageLoader mImageLoader) {
        this.list = list;
        this.context = context;
        this.mImageLoader = mImageLoader;
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
            convertView = View.inflate(context, R.layout.cpbz_item, null);
            holder.txt = (TextView) convertView.findViewById(R.id.cpbz_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.txt.setText((position + 1) + "." + list.get(position).getStandardName());
        return convertView;
    }

    public static class Holder {
        private TextView txt;
    }

}

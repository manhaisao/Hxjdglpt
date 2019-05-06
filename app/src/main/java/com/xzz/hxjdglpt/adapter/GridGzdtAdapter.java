package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.GridGzdt;
import com.xzz.hxjdglpt.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class GridGzdtAdapter extends BaseAdapter {

    private List<GridGzdt> list = new ArrayList<GridGzdt>();
    private Context context;

    public GridGzdtAdapter(Context context, List<GridGzdt> list) {
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
        if (list.get(position).getType() == 1) {
//            if (!TextUtils.isEmpty(list.get(position).getFilename())) {
//                String[] arr = list.get(position).getFilename().split(",");
//                if (arr.length > 0) {
//                    String[] arr1 = arr[0].split(".");
//                    if (arr1.length > 0) {
//                        holder.tvTitle.setText(arr1[0]);
//                    }
//                }
//            }
            holder.tvTitle.setText(list.get(position).getFbsj());
        } else {
            holder.tvTitle.setText(list.get(position).getTitle());
        }

        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
    }

}

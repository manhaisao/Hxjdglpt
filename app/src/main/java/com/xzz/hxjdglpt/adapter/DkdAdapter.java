package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Daikai;
import com.xzz.hxjdglpt.model.News;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻动态适配器
 *
 * @author dbz
 */
public class DkdAdapter extends BaseAdapter {

    private List<Daikai> list = new ArrayList<Daikai>();
    private Context context;
    private ListItemClickHelp listItemClickHelp;
    private String isFrom;

    public DkdAdapter(Context context, List<Daikai> list, ListItemClickHelp listItemClickHelp,
                      String isFrom) {
        this.list = list;
        this.context = context;
        this.listItemClickHelp = listItemClickHelp;
        this.isFrom = isFrom;
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
            convertView = View.inflate(context, R.layout.item_list, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.text_view);
            holder.btnDel = (Button) convertView.findViewById(R.id.item_delete);
            holder.btnUpt = (Button) convertView.findViewById(R.id.item_update);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (isFrom != null && "index".equals(isFrom)) {
            holder.btnDel.setVisibility(View.GONE);
            holder.btnUpt.setVisibility(View.GONE);
        } else {
            holder.btnDel.setVisibility(View.VISIBLE);
            holder.btnUpt.setVisibility(View.VISIBLE);
        }
        holder.tvLabel.setText(list.get(position).getName());
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickHelp.onDelClick(v, position);
            }
        });
        holder.btnUpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickHelp.onUptClick(v, position);
            }
        });
        return convertView;
    }

    public static class Holder {
        private TextView tvLabel;
        private Button btnDel;
        private Button btnUpt;
    }

}

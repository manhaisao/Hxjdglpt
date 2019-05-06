package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.ZfzzGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class GkxzAdapter extends BaseAdapter {

    private List<ZfzzGroup> list = new ArrayList<ZfzzGroup>();
    private Context context;
    private ListItemClickHelp listItemClickHelp;

    public GkxzAdapter(Context context, List<ZfzzGroup> list, ListItemClickHelp listItemClickHelp) {
        this.list = list;
        this.context = context;
        this.listItemClickHelp = listItemClickHelp;
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
            convertView = View.inflate(context, R.layout.gkxz_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.gkxz_title);
            holder.btnDel = (Button) convertView.findViewById(R.id.gkxz_delete);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getName());
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickHelp.onDelClick(v, position);
            }
        });
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private Button btnDel;
    }

}

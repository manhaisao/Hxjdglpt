package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.PlotUserList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbz
 */
public class PlotSQUserAdapter extends BaseAdapter {

    private List<PlotUserList> list = new ArrayList<PlotUserList>();
    private Context context;
    private ListItemClickHelp listItemClickHelp;

    public PlotSQUserAdapter(Context context, List<PlotUserList> list, ListItemClickHelp listItemClickHelp) {
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
            convertView = View.inflate(context, R.layout.item_plot_sq_user, null);
            holder.btnDel = (Button) convertView.findViewById(R.id.item_delete);
            holder.btnUpt = (Button) convertView.findViewById(R.id.item_update);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.text_label);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.text_status);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PlotUserList p = list.get(position);
        StringBuffer content = new StringBuffer();

        if (p.getLx() == 1) {
            content.append(p.getYqrName()).append("邀请").append(p.getFbrXm()).append("加入").append(p.getPlotName()).append("论坛");
            holder.btnDel.setVisibility(View.GONE);
            holder.btnUpt.setVisibility(View.GONE);
        } else {
            if (p.getStatus() == 1) {
                holder.btnDel.setVisibility(View.GONE);
                holder.btnUpt.setVisibility(View.GONE);
                holder.tvStatus.setText("已同意");
            } else {
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
            }
            content.append(p.getFbrXm()).append("申请加入").append(p.getPlotName()).append("论坛");
        }
        holder.tvLabel.setText(content.toString());
        return convertView;
    }

    public static class Holder {
        private TextView tvLabel;
        private Button btnDel;
        private Button btnUpt;
        private TextView tvStatus;
    }

}

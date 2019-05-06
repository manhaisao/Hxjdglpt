package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Fwmdm;
import com.xzz.hxjdglpt.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 *
 * @author dbz
 */
public class SxAdapter extends BaseAdapter {

    private List<Task> list = new ArrayList<Task>();
    private Context context;
    private int isFrom;//1：任务分派  2：代办事项

    public SxAdapter(Context context, List<Task> list, int isFrom) {
        this.list = list;
        this.context = context;
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
            convertView = View.inflate(context, R.layout.task_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.task_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.task_time);
            holder.tvRen = (TextView) convertView.findViewById(R.id.task_ren);
            holder.tvRen1 = (TextView) convertView.findViewById(R.id.task_ren1);
            holder.mLay = (LinearLayout) convertView.findViewById(R.id.task_item_lay);
            holder.mLay1 = (LinearLayout) convertView.findViewById(R.id.task_item_lay1);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.task_title_tatus);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getName());
        holder.tvTime.setText(list.get(position).getCreatetime());
        if (isFrom == 1) {
            holder.tvRen1.setText(list.get(position).getReceiveuserName());
            holder.mLay1.setVisibility(View.VISIBLE);
            holder.mLay.setVisibility(View.GONE);
            switch (list.get(position).getStatus()) {
                case 0:
                    holder.tvStatus.setText("已完成");
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
                    break;
                case 1:
                    holder.tvStatus.setText("进行中");
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.yellow_1));
                    break;
            }
        } else if (isFrom == 2) {
            holder.tvRen.setText(list.get(position).getCreatoruserName());
            holder.mLay1.setVisibility(View.GONE);
            holder.mLay.setVisibility(View.VISIBLE);
        } else if (isFrom == 3) {
            holder.tvRen.setText(list.get(position).getReceiveuserName());
            holder.tvRen1.setText(list.get(position).getCreatoruserName());
            holder.mLay1.setVisibility(View.VISIBLE);
            holder.mLay.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvRen;
        private TextView tvRen1;
        private LinearLayout mLay;
        private LinearLayout mLay1;
        private TextView tvStatus;
    }

}

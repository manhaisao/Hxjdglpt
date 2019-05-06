package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Abry;
import com.xzz.hxjdglpt.model.Dzb;
import com.xzz.hxjdglpt.model.Dzz;
import com.xzz.hxjdglpt.model.Liangxin;
import com.xzz.hxjdglpt.model.LsDzz;
import com.xzz.hxjdglpt.model.Zddw;

import java.util.ArrayList;
import java.util.List;

/**
 * 党建适配器
 *
 * @author dbz
 */
public class DzzAdapter extends BaseAdapter {

    private List<Object> list = new ArrayList<Object>();
    private Context context;
    private ListItemClickHelp listItemClickHelp;
    private String isFrom;

    public DzzAdapter(Context context, List<Object> list) {
        this.list = list;
        this.context = context;
    }

    public DzzAdapter(Context context, List<Object> list, ListItemClickHelp listItemClickHelp, String isFrom) {
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
            holder.tvStatus = (TextView) convertView.findViewById(R.id.text_status);
            holder.tvLx = (TextView) convertView.findViewById(R.id.text_label);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String content = "";
        Object obj = list.get(position);

        if (obj instanceof Zddw) {
            content = ((Zddw) obj).getName();
        } else if (obj instanceof Liangxin) {
            content = ((Liangxin) obj).getName();
        } else if (obj instanceof Dzz) {
            content = ((Dzz) obj).getName();
        } else if (obj instanceof Dzb) {
            content = ((Dzb) obj).getDzbname();
        } else if (obj instanceof LsDzz) {
            content = ((LsDzz) obj).getName();
        }
        holder.tvLabel.setText(content);
        holder.btnDel.setVisibility(View.GONE);
        holder.btnUpt.setVisibility(View.GONE);
        return convertView;
    }

    public static class Holder {
        private TextView tvLabel;
        private Button btnDel;
        private Button btnUpt;
        private TextView tvStatus;
        private TextView tvLx;
    }

}

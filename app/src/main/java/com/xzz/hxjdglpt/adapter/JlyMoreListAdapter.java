package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Azbjry;
import com.xzz.hxjdglpt.model.CityInfo;
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Company;
import com.xzz.hxjdglpt.model.Dbry;
import com.xzz.hxjdglpt.model.Dszn;
import com.xzz.hxjdglpt.model.Huli;
import com.xzz.hxjdglpt.model.Jlfz;
import com.xzz.hxjdglpt.model.Jskn;
import com.xzz.hxjdglpt.model.Jzgy;
import com.xzz.hxjdglpt.model.Ldrk;
import com.xzz.hxjdglpt.model.Lsrt;
import com.xzz.hxjdglpt.model.Management;
import com.xzz.hxjdglpt.model.ManagementQt;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.Shjy;
import com.xzz.hxjdglpt.model.Sqfxry;
import com.xzz.hxjdglpt.model.Sqjdry;
import com.xzz.hxjdglpt.model.Tkgy;
import com.xzz.hxjdglpt.model.Tshd;
import com.xzz.hxjdglpt.model.Xjry;
import com.xzz.hxjdglpt.model.Yfdx;
import com.xzz.hxjdglpt.model.Ylfn;
import com.xzz.hxjdglpt.model.Zdqsn;
import com.xzz.hxjdglpt.model.Zdry;
import com.xzz.hxjdglpt.model.ZfzzInfo;
import com.xzz.hxjdglpt.model.ZfzzInfoVillage;
import com.xzz.hxjdglpt.model.Zlj;
import com.xzz.hxjdglpt.model.Zszhjsbhz;

import java.util.ArrayList;
import java.util.List;

/**
 * 社会养老适配器
 *
 * @author dbz
 */
public class JlyMoreListAdapter extends BaseAdapter {

    private List<Object> list = new ArrayList<Object>();
    private Context context;
    private ListItemClickHelp listItemClickHelp;
    private String isFrom;
    private boolean isDm = false;//是否查看打码

    public JlyMoreListAdapter(Context context, List<Object> list, ListItemClickHelp listItemClickHelp,
                              String isFrom) {
        this.list = list;
        this.context = context;
        this.listItemClickHelp = listItemClickHelp;
        this.isFrom = isFrom;
    }

    public JlyMoreListAdapter(Context context, List<Object> list, ListItemClickHelp listItemClickHelp,
                              String isFrom, boolean isDm) {
        this.list = list;
        this.context = context;
        this.listItemClickHelp = listItemClickHelp;
        this.isFrom = isFrom;
        this.isDm = isDm;
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

        if (obj instanceof Huli) {
            content = ((Huli) obj).getName();
        } else if (obj instanceof Jzgy) {
            content = ((Jzgy) obj).getName();
        } else if (obj instanceof Shjy) {
            content = ((Shjy) obj).getName();
        }
        if("index".equals(isFrom)){
            holder.btnDel.setVisibility(View.GONE);
            holder.btnUpt.setVisibility(View.GONE);
        }else {
            holder.btnDel.setVisibility(View.VISIBLE);
            holder.btnUpt.setVisibility(View.VISIBLE);
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
        holder.tvLabel.setText(content);
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

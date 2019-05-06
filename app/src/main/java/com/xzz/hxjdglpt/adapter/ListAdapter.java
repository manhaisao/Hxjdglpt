package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Azbjry;
import com.xzz.hxjdglpt.model.Chengpin;
import com.xzz.hxjdglpt.model.CityInfo;
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Company;
import com.xzz.hxjdglpt.model.Dbry;
import com.xzz.hxjdglpt.model.Dszn;
import com.xzz.hxjdglpt.model.Hospital;
import com.xzz.hxjdglpt.model.JktDetail;
import com.xzz.hxjdglpt.model.Jlfz;
import com.xzz.hxjdglpt.model.Jskn;
import com.xzz.hxjdglpt.model.Jyh;
import com.xzz.hxjdglpt.model.Jzgd;
import com.xzz.hxjdglpt.model.Ldrk;
import com.xzz.hxjdglpt.model.Lsrt;
import com.xzz.hxjdglpt.model.Management;
import com.xzz.hxjdglpt.model.ManagementQt;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.model.Pfwsp;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.Roupidouzhi;
import com.xzz.hxjdglpt.model.Sqfxry;
import com.xzz.hxjdglpt.model.Sqjdry;
import com.xzz.hxjdglpt.model.Tezhongweixian;
import com.xzz.hxjdglpt.model.Tkgy;
import com.xzz.hxjdglpt.model.Tshd;
import com.xzz.hxjdglpt.model.Xjry;
import com.xzz.hxjdglpt.model.Yanhua;
import com.xzz.hxjdglpt.model.Yfdx;
import com.xzz.hxjdglpt.model.Yjdw;
import com.xzz.hxjdglpt.model.Ylfn;
import com.xzz.hxjdglpt.model.Zdqsn;
import com.xzz.hxjdglpt.model.Zdry;
import com.xzz.hxjdglpt.model.ZfzzInfo;
import com.xzz.hxjdglpt.model.ZfzzInfoVillage;
import com.xzz.hxjdglpt.model.Zlj;
import com.xzz.hxjdglpt.model.Zszhjsbhz;
import com.xzz.hxjdglpt.model.ls.Baozhang;
import com.xzz.hxjdglpt.model.ls.BaozhangJbr;
import com.xzz.hxjdglpt.model.ls.Shbx;
import com.xzz.hxjdglpt.model.ls.ShbxJbr;
import com.xzz.hxjdglpt.model.ls.Work;
import com.xzz.hxjdglpt.model.ls.Zhongcai;
import com.xzz.hxjdglpt.model.ls.ZhongcaiJbr;
import com.xzz.hxjdglpt.utils.FljktDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * 党建适配器
 *
 * @author dbz
 */
public class ListAdapter extends BaseAdapter {

    private List<Object> list = new ArrayList<Object>();
    private Context context;
    private ListItemClickHelp listItemClickHelp;
    private String isFrom;
    private boolean isDm = false;//是否查看打码
    private DoubleLinClickHelp doubleLinClickHelp;

    public ListAdapter(Context context, List<Object> list, ListItemClickHelp listItemClickHelp,
                       String isFrom, DoubleLinClickHelp doubleLinClickHelp) {
        this.list = list;
        this.context = context;
        this.listItemClickHelp = listItemClickHelp;
        this.isFrom = isFrom;
        this.doubleLinClickHelp = doubleLinClickHelp;
    }

    public ListAdapter(Context context, List<Object> list, ListItemClickHelp listItemClickHelp,
                       String isFrom) {
        this.list = list;
        this.context = context;
        this.listItemClickHelp = listItemClickHelp;
        this.isFrom = isFrom;
    }

    public ListAdapter(Context context, List<Object> list, ListItemClickHelp listItemClickHelp,
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
            holder.imgLt = (ImageView) convertView.findViewById(R.id.item_lt);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String content = "";
        Object obj = list.get(position);
        if (isFrom != null && "index".equals(isFrom)) {
            holder.btnDel.setVisibility(View.GONE);
            holder.btnUpt.setVisibility(View.GONE);
            if (obj instanceof Plot) {
                holder.imgLt.setVisibility(View.VISIBLE);
                holder.imgLt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doubleLinClickHelp.onOneClick(v, position);
                    }
                });
            }
            if (obj instanceof Jlfz) {
                switch (((Jlfz) obj).getIsRz()) {
                    case 0:
                        holder.tvStatus.setText("未认证");
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                        break;
                    case 1:
                        holder.tvStatus.setText("已认证");
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color
                                .green));
                        break;
                    case 2:
                        holder.tvStatus.setText("认证中");
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color
                                .yellow_1));
                        break;
                    case 3:
                        holder.tvStatus.setText("已退回");
                        holder.tvStatus.setTextColor(context.getResources().getColor(R.color
                                .blue_1));
                        break;
                }
            }
        } else {
            holder.btnDel.setVisibility(View.VISIBLE);
            holder.btnUpt.setVisibility(View.VISIBLE);
            holder.imgLt.setVisibility(View.GONE);
        }

        if (obj instanceof PartyMember) {
            PartyMember p = (PartyMember) obj;
            content = p.getName();
            if ("02".equals(p.getType())) {
                if ("3".equals(p.getIsliuru())) {
                    holder.tvLx.setText("流出|困难");
                } else {
                    holder.tvLx.setText("困难");
                }
            } else if ("3".equals(p.getIsliuru())) {
                holder.tvLx.setText("流出");
            } else {
                holder.tvLx.setText("");
            }
        } else if (obj instanceof Dbry) {
            content = ((Dbry) obj).getName();
        } else if (obj instanceof Roupidouzhi) {
            content = ((Roupidouzhi) obj).getName();
        } else if (obj instanceof Yanhua) {
            content = ((Yanhua) obj).getName();
        } else if (obj instanceof Tezhongweixian) {
            content = ((Tezhongweixian) obj).getName();
        } else if (obj instanceof Chengpin) {
            content = ((Chengpin) obj).getName();
        } else if (obj instanceof Dbry) {
            content = ((Dbry) obj).getName();
        } else if (obj instanceof Cjry) {
            content = ((Cjry) obj).getName();
        } else if (obj instanceof Ylfn) {
            content = ((Ylfn) obj).getName();
        } else if (obj instanceof Hospital) {
            content = ((Hospital) obj).getName();
        } else if (obj instanceof Company) {
            content = ((Company) obj).getName();
        } else if (obj instanceof Management) {
            content = ((Management) obj).getName();
        } else if (obj instanceof Plot) {
            content = ((Plot) obj).getName();
        } else if (obj instanceof Lsrt) {
            content = ((Lsrt) obj).getName();
        } else if (obj instanceof Yfdx) {
            content = ((Yfdx) obj).getName();
        } else if (obj instanceof Tkgy) {
            content = ((Tkgy) obj).getName();
        } else if (obj instanceof Zlj) {
            content = ((Zlj) obj).getName();
        } else if (obj instanceof CityInfo) {
            content = ((CityInfo) obj).getName();
        } else if (obj instanceof ZfzzInfo) {
            content = ((ZfzzInfo) obj).getName();
        } else if (obj instanceof Dszn) {
            content = ((Dszn) obj).getZnName();
        } else if (obj instanceof Jlfz) {
            content = ((Jlfz) obj).getName();
        } else if (obj instanceof Ldrk) {
            content = ((Ldrk) obj).getName();
            holder.tvLx.setText(((Ldrk) obj).getLdlx());
            if (!TextUtils.isEmpty(((Ldrk) obj).getLdlx())) {
                if (((Ldrk) obj).getLdlx().contains("流出")) {
                    holder.tvLx.setTextColor(context.getResources().getColor(R.color.green));
                } else if (((Ldrk) obj).getLdlx().contains("流入")) {
                    holder.tvLx.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
        } else if (obj instanceof Jskn) {
            content = ((Jskn) obj).getName();
        } else if (obj instanceof ManagementQt) {
            content = ((ManagementQt) obj).getName();
        } else if (obj instanceof Tshd) {
            content = ((Tshd) obj).getTitle();
            holder.btnUpt.setVisibility(View.GONE);
        } else if (obj instanceof ZfzzInfoVillage) {
            content = ((ZfzzInfoVillage) obj).getName();
        } else if (obj instanceof Zdry) {
            content = ((Zdry) obj).getName();
        } else if (obj instanceof Yjdw) {
            content = ((Yjdw) obj).getName();
        } else if (obj instanceof Jzgd) {
            content = ((Jzgd) obj).getName();
        } else if (obj instanceof Zszhjsbhz) {
            if (isDm) {
                content = ((Zszhjsbhz) obj).getName();
            } else {
                if (((Zszhjsbhz) obj).getName() != null && ((Zszhjsbhz) obj).getName().length() >
                        1) {
                    content = ((Zszhjsbhz) obj).getName().substring(0, 1) + "**";
                }
            }
        } else if (obj instanceof Zdqsn) {
            if (isDm) {
                content = ((Zdqsn) obj).getName();
            } else {
                if (((Zdqsn) obj).getName() != null && ((Zdqsn) obj).getName().length() > 1) {
                    content = ((Zdqsn) obj).getName().substring(0, 1) + "**";
                }
            }
        } else if (obj instanceof Sqfxry) {
            if (isDm) {
                content = ((Sqfxry) obj).getName();
            } else {
                if (((Sqfxry) obj).getName() != null && ((Sqfxry) obj).getName().length() > 1) {
                    content = ((Sqfxry) obj).getName().substring(0, 1) + "**";
                }
            }
        } else if (obj instanceof Sqjdry) {
            if (isDm) {
                content = ((Sqjdry) obj).getName();
            } else {
                if (((Sqjdry) obj).getName() != null && ((Sqjdry) obj).getName().length() > 1) {
                    content = ((Sqjdry) obj).getName().substring(0, 1) + "**";
                }
            }
        } else if (obj instanceof Xjry) {
            if (isDm) {
                content = ((Xjry) obj).getName();
            } else {
                if (((Xjry) obj).getName() != null && ((Xjry) obj).getName().length() > 1) {
                    content = ((Xjry) obj).getName().substring(0, 1) + "**";
                }
            }
        } else if (obj instanceof Azbjry) {
            if (isDm) {
                content = ((Azbjry) obj).getName();
            } else {
                if (((Azbjry) obj).getName() != null && ((Azbjry) obj).getName().length() > 1) {
                    content = ((Azbjry) obj).getName().substring(0, 1) + "**";
                }
            }
        } else if (obj instanceof Work) {
            content = ((Work) obj).getName();
        } else if (obj instanceof Shbx) {
            content = ((Shbx) obj).getFzr();
        } else if (obj instanceof Baozhang) {
            content = ((Baozhang) obj).getFzr();
        } else if (obj instanceof Zhongcai) {
            content = ((Zhongcai) obj).getBumen();
        } else if (obj instanceof ShbxJbr) {
            content = ((ShbxJbr) obj).getJbr();
        } else if (obj instanceof BaozhangJbr) {
            content = ((BaozhangJbr) obj).getJbr();
        } else if (obj instanceof ZhongcaiJbr) {
            content = ((ZhongcaiJbr) obj).getXly();
        } else if (obj instanceof Jyh) {
            content = ((Jyh) obj).getName();
        } else if (obj instanceof Pfwsp) {
            content = ((Pfwsp) obj).getName();
        } else if (obj instanceof JktDetail) {
            content = ((JktDetail) obj).getTitle();
        }else if(obj instanceof FljktDetail)
        {
            content = ((FljktDetail) obj).getTitle();
        }
        holder.tvLabel.setText(content);
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
        private TextView tvStatus;
        private TextView tvLx;
        private ImageView imgLt;
    }

}

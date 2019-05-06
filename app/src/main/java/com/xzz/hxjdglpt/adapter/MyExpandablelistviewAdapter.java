package com.xzz.hxjdglpt.adapter;

import android.text.TextUtils;
import android.widget.BaseExpandableListAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class MyExpandablelistviewAdapter extends BaseExpandableListAdapter {
    private Context context;

    private List<Village> villages;

    private ExListItemClickHelp exListItemClickHelp;

    private int type;

    //引入一个字段context，方便Activity实例化MyExpandablelistviewAdapter
    public MyExpandablelistviewAdapter(Context context, List<Village> villages,
                                       ExListItemClickHelp exListItemClickHelp, int type) {
        this.context = context;
        this.villages = villages;
        this.exListItemClickHelp = exListItemClickHelp;
        this.type = type;
    }

    public int getGroupCount() {
        return villages.size();
    }

    public int getChildrenCount(int groupPosition) {
        return villages.get(groupPosition).getgList().size();
    }

    //获取每个一级菜单子项对象
    public Village getGroup(int groupPosition) {
        return villages.get(groupPosition);
    }

    //获取每个二级菜单子项对象
    public Grid getChild(int groupPosition, int childPosition) {
        return villages.get(groupPosition).getgList().get(childPosition);
    }

    //获取每个一级菜单子项对象Id
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取每个二级菜单子项对象Id
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    /**
     * hasStableIds有关于MyExpandablelistviewAdapter适配器刷新顺序
     * getGroupId和getChildId两个方法获取对象Id，获取到的Id,ExpandableListView会根据这个Id确定位置显示内容
     * 然而Id是否有效稳定是由hasStableIds决定的，也就是说：这个方法就是判断item的id是否稳定，
     * 如果有自己的id也就是true，那就是稳定，否则不稳定，则根据item位置来确定id
     */
    public boolean hasStableIds() {
        return true;
    }

    //渲染一级菜单
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView,
                             final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater minflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.expand_xxcj_group_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.expand_xxcj_group_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.expand_xxcj_group_click);
        tv.setText(villages.get(groupPosition).getName());
        if (type == 0) {
            imageView.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exListItemClickHelp.onItemClick(groupPosition, isExpanded, v);
            }
        });
        return convertView;
    }

    //渲染二级菜单
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater minflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.expand_xxcj_child_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.expand_xxcj_child_name);

        String gName = villages.get(groupPosition).getgList().get(childPosition).getZm();
        String gId = villages.get(groupPosition).getgList().get(childPosition).getId();
        if (gName == null && TextUtils.isEmpty(gName)) {
            tv.setText(villages.get(groupPosition).getgList().get(childPosition).getId());
        } else if (gId == null && TextUtils.isEmpty(gId)) {
            tv.setText(villages.get(groupPosition).getgList().get(childPosition).getZm());
        } else {
            tv.setText(gName + "（" + villages.get(groupPosition).getgList().get(childPosition)
                    .getId() + "）");
        }

        //记得return convertView
        return convertView;
    }

    //true:让子项可选 ;     false:让子项不可选
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

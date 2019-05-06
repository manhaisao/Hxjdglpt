package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.qly.Camera;
import com.xzz.hxjdglpt.model.qly.SubjectInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 党建适配器
 *
 * @author dbz
 */
public class QlyListAdapter extends BaseAdapter {

    private List<Object> list = new ArrayList<Object>();
    private Context context;

    public QlyListAdapter(Context context, List<Object> list) {
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
            convertView = View.inflate(context, R.layout.qly_text_item, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.text_label);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.qly_icon);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String content = "";
        Object obj = list.get(position);
        if (obj instanceof SubjectInfo) {
            content = ((SubjectInfo) obj).getSubjectName();
            holder.ivIcon.setImageResource(R.mipmap.camera_icon);
        } else if (obj instanceof Camera) {
            content = ((Camera) obj).getName();
            if ("1".equals(((Camera) obj).getRunStatus())) {
                holder.ivIcon.setImageResource(R.mipmap.qly_icon);
            } else {
                holder.ivIcon.setImageResource(R.mipmap.un_qly_icon);
            }
        }
        holder.tvLabel.setText(content);
        return convertView;
    }

    public static class Holder {
        private TextView tvLabel;
        private ImageView ivIcon;
    }

}

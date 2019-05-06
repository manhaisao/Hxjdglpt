package com.xzz.hxjdglpt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.GridZrr;
import com.xzz.hxjdglpt.model.User;

import java.util.List;
import java.util.Map;

/**
 * 搜索用户适配器
 * Created by dbz on 2017/7/26.
 */

public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    private List<User> mList;

    private LayoutInflater mInflater;

    private Map<String, Integer> indexMap;

    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ContactAdapter(LayoutInflater mInflater, List<User> mList, Map<String, Integer>
            indexMap) {
        this.mList = mList;
        this.mInflater = mInflater;
        this.indexMap = indexMap;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ContactAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (ContactAdapter.ViewHolder) view.getTag();
        } else {
            viewHolder = new ContactAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.dialog_item_contact, null);
            viewHolder.contactFirstChar = (TextView) view.findViewById(R.id
                    .dialog_contact_first_char);
            viewHolder.contactName = (TextView) view.findViewById(R.id.dialog_contact_name);
            viewHolder.cb = (CheckBox) view.findViewById(R.id.dialog_item_checkBox);
            view.setTag(viewHolder);
        }
        viewHolder.contactFirstChar.setVisibility(View.GONE);
        if (position == 0 || (position > 0 && mList.get(position - 1).getTag() != mList.get
                (position).getTag())) {
            viewHolder.contactFirstChar.setText(String.valueOf(mList.get(position).getTag()));
            viewHolder.contactFirstChar.setVisibility(View.VISIBLE);
        }
        viewHolder.contactName.setText(mList.get(position).getRealName());
        viewHolder.cb.setChecked(false);
        viewHolder.cb.setTag(mList.get(position).getUserId());
        return view;
    }

    private class ViewHolder {
        TextView contactFirstChar;
        TextView contactName;
        CheckBox cb;
    }

    @Override
    public int getPositionForSection(int section) {
        String c = String.valueOf(mSections.charAt(section));
        for (int i = section; i >= 0; i--) {
            if (indexMap.containsKey(c)) {
                return indexMap.get(c);
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }
}

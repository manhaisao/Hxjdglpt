package com.xzz.hxjdglpt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.server.widget.SelectableRoundedImageView;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.List;
import java.util.Map;

import io.rong.imageloader.core.ImageLoader;

/**
 * 搜索用户适配器
 * Created by dbz on 2017/7/26.
 */

public class MIContactAdapter extends BaseAdapter implements SectionIndexer {

    private List<Friend> mList;

    private LayoutInflater mInflater;

    private Map<String, Integer> indexMap;

    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public MIContactAdapter(LayoutInflater mInflater, List<Friend> mList, Map<String, Integer>
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
        MIContactAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (MIContactAdapter.ViewHolder) view.getTag();
        } else {
            viewHolder = new MIContactAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.friend_item, null);
            viewHolder.contactFirstChar = (TextView) view.findViewById(R.id
                    .dialog_contact_first_char);
            viewHolder.contactName = (TextView) view.findViewById(R.id.friendname);
            viewHolder.mImageView = (SelectableRoundedImageView) view.findViewById(R.id.frienduri);
            view.setTag(viewHolder);
        }
        viewHolder.contactFirstChar.setVisibility(View.GONE);
        if (position == 0 || (position > 0 && mList.get(position - 1).getTag() != mList.get
                (position).getTag())) {
            viewHolder.contactFirstChar.setText(String.valueOf(mList.get(position).getTag()));
            viewHolder.contactFirstChar.setVisibility(View.VISIBLE);
        }
        viewHolder.contactName.setText(mList.get(position).getDisplayName());
        String portraitUri = ConstantUtil.FILE_DOWNLOAD_URL + mList.get(position).getDiaplayPic();
        ImageLoader.getInstance().displayImage(portraitUri, viewHolder.mImageView,
                BaseApplication.getOptions());
        return view;
    }

    private class ViewHolder {
        TextView contactFirstChar;
        TextView contactName;
        SelectableRoundedImageView mImageView;
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

package com.xzz.hxjdglpt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.server.widget.SelectableRoundedImageView;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.List;

import io.rong.imageloader.core.ImageLoader;

/**
 * Created by dbz on 2017/7/26.
 */

public class MISearchContactAdapter extends BaseAdapter {

    private List<Friend> mSearchList;

    private LayoutInflater mInflater;

    public MISearchContactAdapter(LayoutInflater mInflater, List<Friend> mSearchList) {
        this.mSearchList = mSearchList;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return mSearchList.size();
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
        MISearchContactAdapter.ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (MISearchContactAdapter.ViewHolder) view.getTag();
        } else {
            viewHolder = new MISearchContactAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.friend_item, null);
            viewHolder.contactName = (TextView) view.findViewById(R.id.friendname);
            viewHolder.mImageView = (SelectableRoundedImageView) view.findViewById(R.id.frienduri);
            view.setTag(viewHolder);
        }
        viewHolder.contactName.setText(mSearchList.get(position).getDisplayName());
        String portraitUri = ConstantUtil.FILE_DOWNLOAD_URL + mSearchList.get(position)
                .getDiaplayPic();
        ImageLoader.getInstance().displayImage(portraitUri, viewHolder.mImageView,
                BaseApplication.getOptions());
        return view;
    }

    private class ViewHolder {
        TextView contactName;
        SelectableRoundedImageView mImageView;
    }
}

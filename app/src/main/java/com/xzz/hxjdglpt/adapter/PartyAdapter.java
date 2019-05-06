package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Party;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 党建适配器
 *
 * @author dbz
 */
public class PartyAdapter extends BaseAdapter {

    private List<Party> list = new ArrayList<Party>();
    private Context context;
    private ImageLoader mImageLoader;

    public PartyAdapter(Context context, List<Party> list, ImageLoader mImageLoader) {
        this.list = list;
        this.context = context;
        this.mImageLoader = mImageLoader;
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
            convertView = View.inflate(context, R.layout.party_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.party_item_title);
            holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.party_img);
            holder.tvUser = (TextView) convertView.findViewById(R.id.party_item_user);
            holder.tvTime = (TextView) convertView.findViewById(R.id.party_item_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        String user = "发布人：" + list.get(position).getRealName();
        String time = "创建时间：" + list.get(position).getCreateTime();
        holder.tvUser.setText(user);
        holder.tvTime.setText(time);
        holder.networkImageView.setDefaultImageResId(R.mipmap.default_icon);
        holder.networkImageView.setErrorImageResId(R.mipmap.default_icon);
        if (!TextUtils.isEmpty(list.get(position).getFilePath())) {
            String[] pathUrl = list.get(position).getFilePath().split(",");
            for (String url : pathUrl) {
                if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".JPG") || url
                        .endsWith(".PNG") || url.endsWith(".jpeg") || url.endsWith(".JPEG") ||
                        url.endsWith(".BMP") || url.endsWith(".bmp") || url.endsWith(".gif") ||
                        url.endsWith(".GIF")) {
                    holder.networkImageView.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + url,
                            mImageLoader);
                    break;
                }
            }
        } else {
            holder.networkImageView.setImageUrl("", mImageLoader);
        }
        return convertView;
    }

    public static class Holder {
        private NetworkImageView networkImageView;
        private TextView tvTitle;
        private TextView tvUser;
        private TextView tvTime;
    }

}

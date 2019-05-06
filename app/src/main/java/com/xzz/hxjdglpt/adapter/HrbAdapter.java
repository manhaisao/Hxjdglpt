package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.model.Haoren;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbz
 */
public class HrbAdapter extends BaseAdapter {

    private List<Haoren> list = new ArrayList<Haoren>();
    private Context context;
    private ImageLoader mImageLoader;

    public HrbAdapter(Context context, List<Haoren> list, ImageLoader mImageLoader) {
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
            convertView = View.inflate(context, R.layout.hrb_item, null);
            holder.networkImageView = (CircleNetImageView) convertView.findViewById(R.id.dbml_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.dbml_item_name);
            holder.tvUser = (TextView) convertView.findViewById(R.id.dbml_item_phone);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getName());
        String user = "村居:" + (list.get(position).getCunju() == null ? "" : list.get(position)
                .getCunju());
        holder.tvUser.setText(user);
        holder.networkImageView.setDefaultImageResId(R.mipmap.user_icon);
        holder.networkImageView.setErrorImageResId(R.mipmap.user_icon);
        if (list.get(position).getFilepath() != null && !TextUtils.isEmpty(list.get(position).getFilepath())) {
            String[] pathUrl = list.get(position).getFilepath().split(",");
            for (String url : pathUrl) {
                if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".JPG") || url
                        .endsWith(".PNG") || url.endsWith(".jpeg") || url.endsWith(".JPEG") || url
                        .endsWith(".BMP") || url.endsWith(".bmp") || url.endsWith(".gif") || url
                        .endsWith(".GIF")) {
                    holder.networkImageView.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + url,
                            mImageLoader);
                    break;
                }
            }
        }
        return convertView;
    }

    public static class Holder {
        private TextView tvTitle;
        private TextView tvUser;
        private CircleNetImageView networkImageView;
    }

}

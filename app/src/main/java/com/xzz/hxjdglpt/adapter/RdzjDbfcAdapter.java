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
import com.xzz.hxjdglpt.model.TDbfc;
import com.xzz.hxjdglpt.model.TDbml;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbz
 */
public class RdzjDbfcAdapter extends BaseAdapter {

    private List<TDbfc> list = new ArrayList<TDbfc>();
    private Context context;
    private ImageLoader mImageLoader;

    public RdzjDbfcAdapter(Context context, List<TDbfc> list, ImageLoader mImageLoader) {
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
            convertView = View.inflate(context, R.layout.rdzjdbfc_item, null);
            holder.networkImageView = (CircleNetImageView) convertView.findViewById(R.id.dbfc_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.dbfc_item_title);
            holder.tvJj = (TextView) convertView.findViewById(R.id.dbfc_item_jj);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getName());
        holder.tvJj.setText(list.get(position).getContent());
        holder.networkImageView.setDefaultImageResId(R.mipmap.user_icon);
        holder.networkImageView.setErrorImageResId(R.mipmap.user_icon);
        if (list.get(position).getFilePath() != null && !TextUtils.isEmpty(list.get(position).getFilePath())) {
            String[] pathUrl = list.get(position).getFilePath().split(",");
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
        private CircleNetImageView networkImageView;
        private TextView tvTitle;
        private TextView tvJj;
    }

}

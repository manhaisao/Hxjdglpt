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
import com.xzz.hxjdglpt.model.PlotLt;
import com.xzz.hxjdglpt.model.TDbml;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbz
 */
public class PlotltAdapter extends BaseAdapter {

    private List<PlotLt> list = new ArrayList<PlotLt>();
    private Context context;
    private ImageLoader mImageLoader;

    public PlotltAdapter(Context context, List<PlotLt> list, ImageLoader mImageLoader) {
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
            convertView = View.inflate(context, R.layout.plotlt_item, null);
            holder.networkImageView = (CircleNetImageView) convertView.findViewById(R.id.plot_lt_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.plot_lt_item_name);
            holder.tvUser = (TextView) convertView.findViewById(R.id.plot_lt_item_sj);
            holder.tvContent = (TextView) convertView.findViewById(R.id.plot_lt_item_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getFbrXm());
        holder.tvUser.setText(list.get(position).getFbsj());
        holder.tvContent.setText(list.get(position).getContent());
        holder.networkImageView.setDefaultImageResId(R.mipmap.user_icon);
        holder.networkImageView.setErrorImageResId(R.mipmap.user_icon);
        if (list.get(position).getPicture() != null && !TextUtils.isEmpty(list.get(position).getPicture())) {
            String[] pathUrl = list.get(position).getPicture().split(",");
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
        private TextView tvContent;
        private CircleNetImageView networkImageView;
    }

}

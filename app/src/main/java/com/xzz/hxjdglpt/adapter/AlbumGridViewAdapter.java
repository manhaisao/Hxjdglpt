package com.xzz.hxjdglpt.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.xzz.hxjdglpt.model.ImageItem;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.ImageBitmapCache;
import com.xzz.hxjdglpt.activity.R;

import java.util.ArrayList;

public class AlbumGridViewAdapter extends BaseAdapter {
    final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private ArrayList<ImageItem> selectedDataList;
    private DisplayMetrics dm;
    ImageBitmapCache cache;

    public AlbumGridViewAdapter(Context c, ArrayList<ImageItem> dataList,
                                ArrayList<ImageItem> selectedDataList) {
        mContext = c;
        cache = new ImageBitmapCache();
        this.dataList = dataList;
        this.selectedDataList = selectedDataList;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
    }

    public int getCount() {
        return dataList.size();
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    ImageBitmapCache.ImageCallback callback = new ImageBitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    private class ViewHolder {
        public ImageView imageView;
        public ToggleButton toggleButton;
        public Button choosetoggle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.plugin_camera_select_imageview, null);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image_view);
            viewHolder.toggleButton = (ToggleButton) convertView
                    .findViewById(R.id.toggle_button);
            viewHolder.choosetoggle = (Button) convertView
                    .findViewById(R.id.choosedbt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String path;
        if (dataList != null && dataList.size() > position)
            path = dataList.get(position).imagePath;
        else
            path = "camera_default";
        if (path.contains("camera_default")) {
            viewHolder.imageView.setImageBitmap(BitmapUtil.readBitMap(mContext, R.mipmap.plugin_camera_no_pictures));
        } else {
            final ImageItem item = dataList.get(position);
            viewHolder.imageView.setTag(item.imagePath);
            cache.displayBmp(viewHolder.imageView, item.thumbnailPath,
                    item.imagePath, callback);
        }
        viewHolder.toggleButton.setTag(position);
        viewHolder.choosetoggle.setTag(position);
        viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(
                viewHolder.choosetoggle));
        if (selectedDataList.contains(dataList.get(position))) {
            viewHolder.toggleButton.setChecked(true);
            viewHolder.choosetoggle.setVisibility(View.VISIBLE);
        } else {
            viewHolder.toggleButton.setChecked(false);
            viewHolder.choosetoggle.setVisibility(View.GONE);
        }
        return convertView;
    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

    private class ToggleClickListener implements OnClickListener {
        Button chooseBt;

        public ToggleClickListener(Button choosebt) {
            this.chooseBt = choosebt;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (dataList != null && mOnItemClickListener != null
                        && position < dataList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position,
                            toggleButton.isChecked(), chooseBt);
                }
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        public void onItemClick(ToggleButton view, int position,
                                boolean isChecked, Button chooseBt);
    }

}

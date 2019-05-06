package com.xzz.hxjdglpt.activity;


import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xzz.hxjdglpt.adapter.AlbumGridViewAdapter;
import com.xzz.hxjdglpt.model.ImageBucket;
import com.xzz.hxjdglpt.model.ImageItem;
import com.xzz.hxjdglpt.utils.AlbumHelper;
import com.xzz.hxjdglpt.activity.R;

/**
 * 相册
 * Created by dbz on 2017/5/26.
 */

public class AlbumActivity extends BaseActivity implements OnClickListener {
    // 显示手机里的所有图片的列表控件
    private GridView gridView;
    // 当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    // gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    // 完成按钮
    private Button okButton;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    private List<ImageBucket> contentList;
    private ImageView ivBack;
    private ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_camera_album);
        // 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        initView();
        initListener();
        // 这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy() {
        this.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
            String[] imagePath = new String[tempSelectBitmap.size()];
            for (int i = 0; i < tempSelectBitmap.size(); i++) {
                imagePath[i] = ((ImageItem) tempSelectBitmap.get(i))
                        .getImagePath();
            }
            Intent intent = new Intent();
            intent.putExtra("moreImagePath", imagePath);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    // 初始化，给一些对象赋值
    public void initView() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        ivBack = (ImageView) findViewById(R.id.album_iv_back);
        ivBack.setOnClickListener(this);
        contentList = helper.getImagesBucketList(true);
        dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
                tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(R.id.myText);
        gridView.setEmptyView(tv);

        okButton = (Button) findViewById(R.id.ok_button);
//        okButton.setText(getString(R.string.finish) + "("
//                + tempSelectBitmap.size() + "/"
//                + (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount) + ")");
    }

    private void initListener() {
        gridImageAdapter
                .setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, boolean isChecked, Button chooseBt) {
//                        if (tempSelectBitmap.size() >= (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount)) {
//                            toggleButton.setChecked(false);
//                            chooseBt.setVisibility(View.GONE);
//                            if (!removeOneData(dataList.get(position))) {
//                                Toast.makeText(AlbumActivity.this,
//                                        getString(R.string.only_choose_num),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            return;
//                        }
                        if (isChecked) {
                            chooseBt.setVisibility(View.VISIBLE);
                            tempSelectBitmap.add(dataList.get(position));
//                            okButton.setText(getString(R.string.finish)
//                                    + "("
//                                    + tempSelectBitmap.size()
//                                    + "/"
//                                    + (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount)
//                                    + ")");
                        } else {
                            tempSelectBitmap.remove(dataList.get(position));
                            chooseBt.setVisibility(View.GONE);
//                            okButton.setText(getString(R.string.finish)
//                                    + "("
//                                    + tempSelectBitmap.size()
//                                    + "/"
//                                    + (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount)
//                                    + ")");
                        }
                        isShowOkBt();
                    }
                });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(ImageItem imageItem) {
        if (tempSelectBitmap.contains(imageItem)) {
            tempSelectBitmap.remove(imageItem);
//            okButton.setText(getString(R.string.finish) + "("
//                    + tempSelectBitmap.size() + "/"
//                    + (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount)
//                    + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (tempSelectBitmap.size() > 0) {
//            okButton.setText(getString(R.string.finish) + "("
//                    + tempSelectBitmap.size() + "/"
//                    + (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount)
//                    + ")");
            okButton.setPressed(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
        } else {
//            okButton.setText(getString(R.string.finish) + "("
//                    + tempSelectBitmap.size() + "/"
//                    + (ConstantUtil.MAX_NUM - NewSpeakingActivity.imageCount)
//                    + ")");
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}

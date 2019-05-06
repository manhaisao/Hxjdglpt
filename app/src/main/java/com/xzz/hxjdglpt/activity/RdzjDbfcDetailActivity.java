package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.model.TDbfc;
import com.xzz.hxjdglpt.model.TDbml;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 人大之家详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_rdzjdbfc_detail)
public class RdzjDbfcDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.dbfc_name)
    private TextView tvName;
    @ViewInject(R.id.dbfc_age)
    private TextView tvAge;
    @ViewInject(R.id.dbfc_jc)
    private TextView tvJc;

    private TDbfc tDbfc;
    @ViewInject(R.id.rdzjdbfc_head_img)
    private CircleNetImageView pHead;

    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tDbfc = getIntent().getParcelableExtra("tDbfc");
        mImageLoader = application.getImageLoader();
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("代表风采详情");
    }


    public void initData() {
        tvName.setText(tDbfc.getName() != null ? tDbfc.getName() : "");
        tvAge.setText(tDbfc.getAge() != null ? String.valueOf(tDbfc.getAge()) : "");
        tvJc.setText(tDbfc.getContent() != null ? tDbfc.getContent() : "");
        if (tDbfc.getFilePath() != null) {
            String[] pathUrl = tDbfc.getFilePath().split(",");
            pHead.setDefaultImageResId(R.mipmap.user_icon);
            pHead.setErrorImageResId(R.mipmap.user_icon);
            for (String url : pathUrl) {
                if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".JPG") || url
                        .endsWith(".PNG") || url.endsWith(".jpeg") || url.endsWith(".JPEG") ||
                        url.endsWith(".BMP") || url.endsWith(".bmp") || url.endsWith(".gif") ||
                        url.endsWith(".GIF")) {
                    pHead.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + url, mImageLoader);
                    break;
                }
            }
        }
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

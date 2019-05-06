package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
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
@ContentView(R.layout.aty_rdzjbmzz_detail)
public class RdzjDbmlDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.dbml_name)
    private TextView tvName;
    @ViewInject(R.id.dbml_xq)
    private TextView tvXq;
    @ViewInject(R.id.dbml_dw)
    private TextView tvDw;
    @ViewInject(R.id.dbml_sex)
    private TextView tvSex;
    @ViewInject(R.id.dbml_age)
    private TextView tvAge;
    @ViewInject(R.id.dbml_jc)
    private TextView tvJc;
    @ViewInject(R.id.dbml_phone)
    private TextView tvPhone;
    @ViewInject(R.id.dbml_csrq)
    private TextView tvCsrq;
    @ViewInject(R.id.dbml_xl)
    private TextView tvXl;
    @ViewInject(R.id.dbml_dp)
    private TextView tvDp;
    @ViewInject(R.id.rdzjdbfc_head_img)
    private CircleNetImageView pHead;

    private ImageLoader mImageLoader;
    private TDbml tDbml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tDbml = getIntent().getParcelableExtra("tDbml");
        mImageLoader = application.getImageLoader();
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("代表名录详情");
    }


    public void initData() {
        tvName.setText(tDbml.getName() != null ? tDbml.getName() : "");
        tvXq.setText(tDbml.getXq() != null ? tDbml.getXq() : "");
        tvDw.setText(tDbml.getDanwei() != null ? tDbml.getDanwei() : "");
        tvSex.setText(tDbml.getSex() != null ? tDbml.getSex() : "");
        tvAge.setText(tDbml.getAge() != null ? String.valueOf(tDbml.getAge()) : "");
        tvJc.setText(tDbml.getDbcj() != null ? tDbml.getDbcj() : "");
        tvPhone.setText(tDbml.getPhone() != null ? tDbml.getPhone() : "");
        tvCsrq.setText(tDbml.getCreateTime() != null ? tDbml.getCreateTime() : "");
        tvXl.setText(tDbml.getXueli() != null ? tDbml.getXueli() : "");
        tvDp.setText(tDbml.getDangpai() != null ? tDbml.getDangpai() : "");
        if (tDbml.getFilepath() != null) {
            String[] pathUrl = tDbml.getFilepath().split(",");
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

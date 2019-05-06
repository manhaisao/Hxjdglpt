package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.model.Youxiu;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_yxdy_detail)
public class YxdyDetailActivity extends BaseActivity {
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
    @ViewInject(R.id.dbml_jc)
    private WebView tvJc;

    private Youxiu youxiu;

    @ViewInject(R.id.rdzjdbfc_head_img)
    private CircleNetImageView pHead;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        youxiu = getIntent().getParcelableExtra("yxdy");
        mImageLoader = application.getImageLoader();

        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("优秀党员");
    }


    public void initData() {
        tvName.setText(youxiu.getName() != null ? youxiu.getName() : "");
        String dw = youxiu.getDanwie() != null ? youxiu.getDanwie() : "";
        tvDw.setText("工作单位及职务：" + dw);
        String dzb = youxiu.getDzb() != null ? youxiu.getDzb() : "";
        tvXq.setText("所属党支部：" + dzb);
        tvSex.setText(youxiu.getSex() != null ? youxiu.getSex() : "");
        String zysj = youxiu.getShiji() != null ? youxiu.getShiji() : "";
        tvJc.loadDataWithBaseURL(null, zysj, "text/html", "utf-8", null);
        if (youxiu.getFilepath() != null) {
            String[] pathUrl = youxiu.getFilepath().split(",");
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

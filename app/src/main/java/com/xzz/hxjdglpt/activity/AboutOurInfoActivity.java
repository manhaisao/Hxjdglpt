package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.VersionInfoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 关于我们
 */
@ContentView(R.layout.aty_about_our_info)
public class AboutOurInfoActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.setting_version_number)
    private TextView tvVersion;
    @ViewInject(R.id.about_our_url)
    private TextView tvUrl;
    @ViewInject(R.id.my_lay_qcode)
    private RelativeLayout qCode;
    @ViewInject(R.id.show_qcode)
    private ImageView mQCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(getText(R.string.about_us));
        tvUrl.setText(ConstantUtil.BASE_URL);
        qCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQCode.setVisibility(View.VISIBLE);
            }
        });
    }

    public void initData() {
        String version = VersionInfoUtil.getVersionName(AboutOurInfoActivity.this);
        tvVersion.setText(version);

    }

    @Event(value = {R.id.iv_back, R.id.show_qcode, R.id.my_lay_jszc}, type = View.OnClickListener
            .class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.show_qcode:
                mQCode.setVisibility(View.GONE);
                break;
            case R.id.my_lay_jszc:
                Intent intent = new Intent();
                intent.setClass(AboutOurInfoActivity.this, JszcActivity.class);
                startActivity(intent);
                break;
        }
    }

}

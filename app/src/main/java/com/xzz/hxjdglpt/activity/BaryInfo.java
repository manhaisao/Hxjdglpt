package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.Abry;
import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_bary_info)
public class BaryInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Abry bary;
    @ViewInject(R.id.jsbhz_d_name)
    private TextView mName;
    @ViewInject(R.id.jsbhz_d_cun)
    private TextView mCun;
    @ViewInject(R.id.jsbhz_d_xq)
    private TextView mXq;

    private String xq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        bary = getIntent().getParcelableExtra("bary");
        xq = getIntent().getStringExtra("plotName");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("保安信息");
        mName.setText(bary.getName());
        mCun.setText(bary.getPhone());
        mXq.setText(xq);
    }

    public void initData() {
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Event(value = {R.id.iv_back}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

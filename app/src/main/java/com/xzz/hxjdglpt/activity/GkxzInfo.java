package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.ZfzzGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * 留守儿童基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_gkxz_info)
public class GkxzInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private ZfzzGroup zfzzGroup;
    @ViewInject(R.id.gkxz_d_name)
    private TextView mName;
    @ViewInject(R.id.gkxz_d_dh)
    private TextView mPhone;
    @ViewInject(R.id.gkxz_d_ryxz)
    private TextView mRyxz;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        zfzzGroup = getIntent().getParcelableExtra("zfzzGroup");
        type = getIntent().getStringExtra("type");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        if ("2".equals(type) || "3".equals(type) || "4".equals(type) || "7".equals(type)) {
            tvTitle.setText("管控小组人员");
        } else if ("5".equals(type)) {
            tvTitle.setText("矫正小组人员");
        } else if ("6".equals(type)) {
            tvTitle.setText("帮教小组人员");
        }
    }

    public void initData() {
        mName.setText(zfzzGroup.getName());
        mPhone.setText(zfzzGroup.getPhone());
        mRyxz.setText(zfzzGroup.getRyxz());
    }


    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

package com.xzz.hxjdglpt.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.Huli;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 护理人员信息
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_hlry_info)
public class HlryInfo extends BaseActivity  {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.hlry_qk)
    private TextView tvQk;


    @ViewInject(R.id.hlry_name)
    private TextView tvName;//姓名

    @ViewInject(R.id.hlry_sfzh)
    private TextView tvSfzh;//身份证

    @ViewInject(R.id.hlry_lxdh)
    private TextView tvLxdh;//联系电话

    @ViewInject(R.id.hlry_time)
    private TextView tvTime;//检查时间

    @ViewInject(R.id.hlry_bz)
    private TextView tvBz;//备注

    private User user;

    private Huli hl;

    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        initView();
        initData();
    }
    //打码权限
    private boolean isContain() {
        for (Role r : roles) {
            if ("4257".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        tvTitle.setText("护理人员信息");
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
            hl = getIntent().getParcelableExtra("hlry");
            tvQk.setText(hl.getQingkuang());
            tvName.setText(hl.getName());//姓名
            if(isContain()){
                tvSfzh.setText(hl.getNo());
            }else if (!TextUtils.isEmpty(hl.getNo()) && hl.getNo().length() > 8) {
                String sfzh = hl.getNo().substring(0, hl.getNo().length() - 8);
                tvSfzh.setText(sfzh+"********");
            }
            tvLxdh.setText(hl.getPhone());
            tvTime.setText(hl.getTime());
            tvBz.setText(hl.getBz());//备注
    }



    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}

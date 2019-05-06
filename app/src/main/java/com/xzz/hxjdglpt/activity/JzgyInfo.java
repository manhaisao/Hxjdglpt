package com.xzz.hxjdglpt.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.Jzgy;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 集中供養信息
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_shjy_info)
public class JzgyInfo extends BaseActivity  {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.shjy_name)
    private TextView tvName;//姓名

    @ViewInject(R.id.shjy_sfzh)
    private TextView tvSfzh;//身份证

    @ViewInject(R.id.shjy_cj)
    private TextView tvCj;//

    @ViewInject(R.id.shjy_dj)
    private TextView tvDj;//

    @ViewInject(R.id.shjy_bz)
    private TextView tvBz;//备注

    private User user;

    private Jzgy shjy;

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
        tvTitle.setText("集中供养信息");
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        shjy = getIntent().getParcelableExtra("jzgy");
            tvName.setText(shjy.getName());//姓名
            if(isContain()){
                tvSfzh.setText(shjy.getNo());
            }else if (!TextUtils.isEmpty(shjy.getNo()) && shjy.getNo().length() > 8) {
                String sfzh = shjy.getNo().substring(0, shjy.getNo().length() - 8);
                tvSfzh.setText(sfzh+"********");
            }
            tvCj.setText(shjy.getCunju());
            tvDj.setText(shjy.getDengji());
            tvBz.setText(shjy.getBz());//备注
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

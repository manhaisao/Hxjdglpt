package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.Bjxx;
import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2019/4/9 0009.
 */

@ContentView(R.layout.aty_bjxxinfo)
public class BjxxInfoActicity extends  BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView hx_title;
    @ViewInject(R.id.bjxx_dl)
    private TextView bjxx_dl;

    @ViewInject(R.id.bjxx_cunju)
    private TextView bjxx_cunju;
    @ViewInject(R.id.bjxx_wangge)
    private TextView bjxx_wangge;
    @ViewInject(R.id.bjxx_beizhu)
    private TextView bjxx_beizhu;
    private User user;
    private BaseApplication application;
    private Bjxx bjxx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        bjxx= (Bjxx) getIntent().getSerializableExtra("bjxx");
        hx_title.setText(bjxx.getRoadName());
        bjxx_dl.setText(bjxx.getRoadName());
        bjxx_cunju.setText(bjxx.getVillage());
        bjxx_wangge.setText(bjxx.getGrid());
        bjxx_beizhu.setText(bjxx.getRemark());
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

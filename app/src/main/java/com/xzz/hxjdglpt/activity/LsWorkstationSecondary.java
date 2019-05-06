package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.ls.Work;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 工作站详情
 */
@ContentView(R.layout.activity_ls_workstation_secondary)
public class LsWorkstationSecondary extends BaseActivity {

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.tv_fuwu)
    private TextView tvFuwu;
    @ViewInject(R.id.tv_address)
    private TextView tvAddress;
    @ViewInject(R.id.tv_fzr)
    private TextView tvFzr;
    @ViewInject(R.id.tv_fzrphone)
    private TextView tvFzrphone;

    private Bundle bundle;
    private Work work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        bundle =  getIntent().getExtras();
        work = (Work) bundle.getSerializable("work");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText(work.getName());
        tvFuwu.setText(work.getFuwu());
        tvAddress.setText(work.getAddress());
        tvFzr.setText(work.getFzr());
        tvFzrphone.setText(work.getFzrphone());
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
        }
    }
}

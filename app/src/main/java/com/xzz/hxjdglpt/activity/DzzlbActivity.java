package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * 村级党组织：×
 * 驻地党组织：×
 * 两新党组织：×
 * 临时党组织：×
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_dzzlb)
public class DzzlbActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.qygz_ltqy)
    private TextView tvLtqy;
    @ViewInject(R.id.qygz_myqy)
    private TextView tvMzqy;
    @ViewInject(R.id.qygz_jtqy)
    private TextView tvJtqy;

    @ViewInject(R.id.aqsc_zbqy)
    private TextView tvZbqy;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("党组织");
        tvJtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvLtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvMzqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvZbqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvLtqy.setText(String.valueOf(getIntent().getIntExtra("cjdzz", 0)));
        tvMzqy.setText(String.valueOf(getIntent().getIntExtra("zddzz", 0)));
        tvJtqy.setText(String.valueOf(getIntent().getIntExtra("lxdzz", 0)));
        tvZbqy.setText(String.valueOf(getIntent().getIntExtra("lsdzz", 0)));
    }

    public void initData() {
    }


    @Event(value = {R.id.iv_back, R.id.qygz_ltqy, R.id.qygz_myqy, R.id.qygz_jtqy, R.id.aqsc_zbqy}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.qygz_ltqy:
                intent.setClass(DzzlbActivity.this, DzzListActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.qygz_myqy:
                intent.setClass(DzzlbActivity.this, DzzListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.qygz_jtqy:
                intent.setClass(DzzlbActivity.this, DzzListActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_zbqy:
                intent.setClass(DzzlbActivity.this, DzzListActivity.class);
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
        }
    }
}

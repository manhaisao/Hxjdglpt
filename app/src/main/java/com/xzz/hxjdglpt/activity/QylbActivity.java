package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * 列统企业：×
 * 民营企业：×
 * 集体企业：×
 * 总部企业：×
 * 建筑工地：×
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_qylb)
public class QylbActivity extends BaseActivity {

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

    @ViewInject(R.id.aqsc_jzgd)
    private TextView tvJzgd;
    @ViewInject(R.id.aqsc_zbqy)
    private TextView tvZbqy;

    private User user;

    private String gridId = "";
    private String vId = "";

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
        tvTitle.setText(R.string.qy);
        gridId = getIntent().getStringExtra("gridId");
        vId = getIntent().getStringExtra("vId");
        tvJtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvLtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvMzqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvJzgd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvZbqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvJtqy.setText(String.valueOf(getIntent().getIntExtra("jt", 0)));
        tvLtqy.setText(String.valueOf(getIntent().getIntExtra("lt", 0)));
        tvMzqy.setText(String.valueOf(getIntent().getIntExtra("my", 0)));
        tvJzgd.setText(String.valueOf(getIntent().getIntExtra("jz", 0)));
        tvZbqy.setText(String.valueOf(getIntent().getIntExtra("zb", 0)));
    }

    public void initData() {
    }


    @Event(value = {R.id.iv_back, R.id.aqsc_jzgd, R.id.qygz_ltqy, R.id.qygz_myqy, R.id.qygz_jtqy, R.id.aqsc_zbqy}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        if (getIntent().getStringExtra("isYjfw") != null) {
            intent.putExtra("isYjfw", getIntent().getStringExtra("isYjfw"));
        }
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.aqsc_jzgd:
                if (gridId != null) {
                    intent.setClass(QylbActivity.this, ListActivity.class);
                    intent.putExtra("gridId", "9999");
                } else {
                    intent.putExtra("vId", vId);
                    intent.setClass(QylbActivity.this, ZhfwListActivity.class);
                }
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 46);
                startActivity(intent);
                break;
            case R.id.qygz_ltqy:
                if (gridId != null) {
                    intent.setClass(QylbActivity.this, ListActivity.class);
                    intent.putExtra("gridId", "9999");
                } else {
                    intent.putExtra("vId", vId);
                    intent.setClass(QylbActivity.this, ZhfwListActivity.class);
                }
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 40);
                startActivity(intent);
                break;
            case R.id.qygz_myqy:
                if (gridId != null) {
                    intent.setClass(QylbActivity.this, ListActivity.class);
                    intent.putExtra("gridId", "9999");
                } else {
                    intent.putExtra("vId", vId);
                    intent.setClass(QylbActivity.this, ZhfwListActivity.class);
                }
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 41);
                startActivity(intent);
                break;
            case R.id.qygz_jtqy:
                if (gridId != null) {
                    intent.setClass(QylbActivity.this, ListActivity.class);
                    intent.putExtra("gridId", "9999");
                } else {
                    intent.putExtra("vId", vId);
                    intent.setClass(QylbActivity.this, ZhfwListActivity.class);
                }
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 42);
                startActivity(intent);
                break;
            case R.id.aqsc_zbqy:
                if (gridId != null) {
                    intent.setClass(QylbActivity.this, ListActivity.class);
                    intent.putExtra("gridId", "9999");
                } else {
                    intent.putExtra("vId", vId);
                    intent.setClass(QylbActivity.this, ZhfwListActivity.class);
                }
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 100);
                startActivity(intent);
                break;
        }
    }
}

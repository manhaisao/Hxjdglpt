package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 网格功能选择界面
 * Created by dbz on 2017/6/21.
 */
@ContentView(R.layout.aty_grid_function)
public class GridFunctionActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private String gId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.grid_function));
        gId = getIntent().getStringExtra("gridId");
    }

    @Event(value = {R.id.iv_back, R.id.grid_function_btn1, R.id.grid_function_btn2, R.id
            .grid_function_btn3, R.id.grid_function_btn4}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.grid_function_btn1:
                intent.setClass(this, GridIntroductionActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
                break;
            case R.id.grid_function_btn2:
                intent.setClass(this, GridSxgzActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
                break;
            case R.id.grid_function_btn3:
                intent.putExtra("gridId", gId);
                intent.setClass(this, WorkSendListActivity.class);
                startActivity(intent);
                break;
            case R.id.grid_function_btn4:
                intent.setClass(this, WorkBackListActivity.class);
                startActivity(intent);
                break;
        }
    }

}

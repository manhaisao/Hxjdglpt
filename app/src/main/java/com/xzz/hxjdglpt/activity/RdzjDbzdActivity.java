package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.BaseActivity;
import com.xzz.hxjdglpt.activity.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 */
@ContentView(R.layout.aty_rdzj_dbzd)
public class RdzjDbzdActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("代表制度");
    }

    public void initData() {

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

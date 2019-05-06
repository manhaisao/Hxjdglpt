package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.Fwmdm;
import com.xzz.hxjdglpt.model.Mqztc;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 服务面对面详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_fwmdm_detail)
public class FwmdmDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.fwmdm_detail_title)
    private TextView tvTi;
    @ViewInject(R.id.fwmdm_detail_label)
    private TextView tvLabel;
    @ViewInject(R.id.fwmdm_detail_time)
    private TextView tvTime;
    @ViewInject(R.id.fwmdm_detail_content)
    private TextView tvContent;
    private Fwmdm fwmdm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        fwmdm = getIntent().getParcelableExtra("fwmdm");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(getText(R.string.fwmdm_detail));
    }

    public void initData() {
        if (fwmdm.getTitle() != null) {
            tvTi.setText(fwmdm.getTitle());
        }
        if (fwmdm.getDescription() != null) {
            tvLabel.setText(fwmdm.getDescription());
        }
        if (fwmdm.getQuestionTime() != null) {
            tvTime.setText(fwmdm.getQuestionTime());
        }
        if (fwmdm.getAnswer() != null) {
            tvContent.setText(fwmdm.getAnswer());
        }
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

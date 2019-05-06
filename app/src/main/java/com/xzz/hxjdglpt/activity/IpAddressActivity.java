package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.xzz.hxjdglpt.utils.ConstantUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * IP地址输入
 * Created by dbz on 2017/6/13.
 */
@ContentView(R.layout.aty_ip_address)
public class IpAddressActivity extends BaseActivity {
    @ViewInject(R.id.ip_account)
    private AutoCompleteTextView ip;

    @ViewInject(R.id.hx_title)
    private TextView mtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mtitle.setText("设置IP");
    }

    @Event(value = {R.id.ip_address_ok, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.ip_address_ok:
                StringBuffer sb = new StringBuffer();
                sb.append("http://").append(ip.getText().toString().trim()).append("/hx");
                ConstantUtil.BASE_URL = sb.toString();
                StringBuffer sb1 = new StringBuffer();
                sb1.append("http://").append(ip.getText().toString().trim()).append("/");
                ConstantUtil.FILE_DOWNLOAD_URL = sb1.toString();
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

}

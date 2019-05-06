package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

/**
 * Created by dbz on 2017/5/15.
 */

@ContentView(R.layout.aty_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.hx_title_)
    private TextView tvTitle;
    @ViewInject(R.id.register_account)
    private EditText edtAccount;
    @ViewInject(R.id.register_realname)
    private EditText edtName;
    @ViewInject(R.id.register_phone)
    private EditText edtPhone;
    @ViewInject(R.id.register_pwd)
    private EditText edtPwd;
    @ViewInject(R.id.register_code)
    private EditText edtCode;
    @ViewInject(R.id.register_btn)
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.register));
    }

    private void register() {
        String account = edtAccount.getText().toString().trim();
        String realName = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        String pwd1 = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.show(this, R.string.account_empty);
            return;
        }
        if (TextUtils.isEmpty(realName)) {
            ToastUtil.show(this, R.string.name_empty);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(this, R.string.phone_empty);
            return;
        }
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd1)) {
            ToastUtil.show(this, R.string.pwd_empty);
            return;
        }
        if (!pwd.equals(pwd1)) {
            ToastUtil.show(this, R.string.pwd_nq);
            return;
        }
        SuccinctProgress.showSuccinctProgress(RegisterActivity.this, "注册中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("pwd", pwd);
        params.put("phone", phone);
        params.put("name", realName);
        String url = ConstantUtil.BASE_URL + "/user/register";
        VolleyRequest.RequestPost(this, url, "register_request", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    LogUtil.i(resultCode);
                    // resultCode 1:存在用户 ；2：不存在；3：查询失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(RegisterActivity.this, R.string.reg_success);
                        finish();
                    } else if ("4".equals(resultCode)) {
                        ToastUtil.show(RegisterActivity.this, R.string.user_exist);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(RegisterActivity.this, R.string.reg_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                ToastUtil.show(RegisterActivity.this, R.string.reg_fail);
            }
        });

    }

    @Event(value = {R.id.register_btn}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                register();
                break;
        }
    }
}

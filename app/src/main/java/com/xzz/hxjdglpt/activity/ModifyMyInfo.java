package com.xzz.hxjdglpt.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * 修改用户信息
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_modify_myinfo)
public class ModifyMyInfo extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.modify_info_save)
    private Button btnSave;
    @ViewInject(R.id.modify_info_input)
    private EditText edtValue;
    private User user;
    private static final int INFO_RESULT_VALUE = 8;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        type = getIntent().getStringExtra("type");

        initView();
        initData();
    }

    @Event(value = {R.id.iv_back, R.id.modify_info_save}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.modify_info_save:
                save();
                break;
        }
    }

    public void initData() {
        if ("name".equals(type)) {
            tvTitle.setText(getText(R.string.modify_name));
            edtValue.setText(user.getRealName());
        } else if ("qq".equals(type)) {
            tvTitle.setText(getText(R.string.modify_qq));
            edtValue.setText(user.getQqcode());
        } else if ("email".equals(type)) {
            tvTitle.setText(getText(R.string.modify_email));
            edtValue.setText(user.getEmail());
        } else if ("phone".equals(type)) {
            tvTitle.setText(getText(R.string.modify_phone));
            edtValue.setText(user.getPhone());
        }
    }

    public void save() {
        String value = edtValue.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            ToastUtil.show(this, R.string.input_value);
            return;
        }
        modifyInfo(value);
    }


    private void modifyInfo(final String value) {
        SuccinctProgress.showSuccinctProgress(ModifyMyInfo.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/user/updateUser";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", type);
        params.put("value", value);
        VolleyRequest.RequestPost(this, url, "update_user", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:修改成功 ；2；token失效 3：修改失败
                    if ("1".equals(resultCode)) {
                        if ("name".equals(type)) {
                            user.setRealName(value);
                        } else if ("qq".equals(type)) {
                            user.setQqcode(value);
                        } else if ("email".equals(type)) {
                            user.setEmail(value);
                        } else if ("phone".equals(type)) {
                            user.setPhone(value);
                        } else if ("picture".equals(type)) {
                            user.setPicture(value);
                        }
                        application.setUser(user);
                        saveUser(user);
                        Intent intent = new Intent();
                        intent.putExtra("value", value);
                        setResult(INFO_RESULT_VALUE, intent);
                        finish();
                        ToastUtil.show(ModifyMyInfo.this, R.string.modify_success);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ModifyMyInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ModifyMyInfo.this, R.string.modify_fail);
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
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    /**
     * 将修改后的信息保存到SharedPreferences里面
     *
     * @param user
     */
    public void saveUser(User user) {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_user", Activity
                .MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            SharedPreferences.Editor editor = mySharedP.edit();
            String userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("userInfo", userBase64);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("update_user");
    }
}

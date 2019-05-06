package com.xzz.hxjdglpt.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import android.app.Activity;
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

/**
 * 修改密码
 */
@ContentView(R.layout.aty_modify_my_password)
public class ModifyMyPasswordActivity extends BaseActivity {
    @ViewInject(R.id.modify_my_password_before)
    private EditText edtBefore;
    @ViewInject(R.id.modify_my_password_new)
    private EditText edtNew;
    @ViewInject(R.id.modify_my_password_confirm)
    private EditText edtConfirm;
    @ViewInject(R.id.modify_my_password_save)
    private Button butSave;
    private User user;
    private String newPwd;
    private String oldPwd;
    private String comfirmPwd;
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

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
        tvTitle.setText(R.string.modify_pwd);
    }


    @Event(value = {R.id.iv_back, R.id.modify_my_password_save}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.modify_my_password_save:
                save();
                break;
        }

    }

    private void save() {
        newPwd = edtNew.getText().toString().trim();
        oldPwd = edtBefore.getText().toString().trim();
        comfirmPwd = edtConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtil.show(this, R.string.oldpwd_comfirm);
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            ToastUtil.show(this, R.string.newpwd_comfirm);
            return;
        }
        if (TextUtils.isEmpty(comfirmPwd)) {
            ToastUtil.show(this, R.string.pwd_comfirm);
            return;
        }
        if (!newPwd.equals(comfirmPwd)) {
            ToastUtil.show(this, R.string.pwd_not_equal);
            return;
        }
        SuccinctProgress.showSuccinctProgress(ModifyMyPasswordActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/user/updatePwd";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("newpwd", newPwd);
        params.put("oldpwd", oldPwd);
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
                        user.setPassword(newPwd);
                        application.setUser(user);
                        saveUser(user);
                        ToastUtil.show(ModifyMyPasswordActivity.this, R.string.modify_success);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ModifyMyPasswordActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ModifyMyPasswordActivity.this, R.string.modify_fail);
                    } else if ("4".equals(resultCode)) {
                        ToastUtil.show(ModifyMyPasswordActivity.this, R.string.oldpwd_error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("update_user");
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

}

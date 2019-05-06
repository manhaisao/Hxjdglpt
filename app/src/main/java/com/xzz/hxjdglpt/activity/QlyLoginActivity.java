package com.xzz.hxjdglpt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.qly.ResultInfo;
import com.xzz.hxjdglpt.model.qly.UserInfo;
import com.xzz.hxjdglpt.utils.ConstantUtil;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


/**
 * Created by dbz on 2017/5/11.
 */
@ContentView(R.layout.aty_qly_login)
public class QlyLoginActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.qly_account)
    private AutoCompleteTextView mEmailView;
    @ViewInject(R.id.qly_pwd_login)
    private EditText mPasswordView;
    @ViewInject(R.id.qly_login)
    private Button mEmailSignInButton;
    @ViewInject(R.id.qly_autologin)
    private CheckBox mAutoLogin;
    @ViewInject(R.id.qly_rember)
    private CheckBox mRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        UserInfo userInfo = getQlyUser();
        tvTitle.setText(getText(R.string.qly));
        if (getAutoLogin()) {
            mAutoLogin.setChecked(true);
        }
        if (isRemember()) {
            mRemember.setChecked(true);
            if (userInfo != null) {
                mEmailView.setText(userInfo.getUserName());
                mPasswordView.setText(userInfo.getMyPassW());
            }
        }
        mAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saveAutoLogin(true);
                } else {
                    saveAutoLogin(false);
                }
            }
        });
        mRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saveRemember(true);
                } else {
                    saveRemember(false);
                }
            }
        });
    }

    @Event(value = {R.id.qly_login, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.qly_login:
                attemptLogin();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Event(value = {R.id.pwd_login}, type = TextView.OnEditorActionListener.class)
    private boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.pwd_login || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    private boolean getAutoLogin() {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_qly", Activity
                .MODE_PRIVATE);
        return mySharedP.getBoolean("autoLogin", false);
    }

    public void saveAutoLogin(boolean autoLogin) {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_qly", Activity
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedP.edit();
        editor.putBoolean("autoLogin", autoLogin);
        editor.commit();
    }

    public void saveRemember(boolean isRemember) {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_qly", Activity
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedP.edit();
        editor.putBoolean("isRemember", isRemember);
        editor.commit();
    }

    private boolean isRemember() {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_qly", Activity
                .MODE_PRIVATE);
        return mySharedP.getBoolean("isRemember", false);
    }

    public void saveUser(UserInfo user) {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_qly", Activity
                .MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            SharedPreferences.Editor editor = mySharedP.edit();
            String userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("qly_userInfo", userBase64);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserInfo getQlyUser() {
        UserInfo user = null;
        try {
            SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_qly", Activity
                    .MODE_PRIVATE);
            String base64User = mySharedP.getString("qly_userInfo", "");
            byte[] userBytes = Base64.decode(base64User.getBytes(), Base64.DEFAULT);
            if (userBytes.length > 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(userBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                user = (UserInfo) ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 登录验证
     */
    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_username_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!email.contains("@")) {
            mPasswordView.setError(getString(R.string.error_username_at));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> params = new HashMap<>();
            String[] u = email.split("@");
            params.put("name", u[0]);
            params.put("domain", u[1]);
            params.put("pass", password);
            requestLogin(params);
        }
    }


    /**
     * 登录请求
     */
    private void requestLogin(final HashMap<String, String> params) {
        SuccinctProgress.showSuccinctProgress(QlyLoginActivity.this, "登录中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qly/qly_login";
        VolleyRequest.RequestPost(this, url, "qly_login", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    Gson gson = new Gson();
                    ResultInfo resultInfo = (ResultInfo) gson.fromJson(result.getJSONObject
                            ("resultInfo").toString(), ResultInfo.class);
                    if ("OK".equals(resultInfo.getValue())) {
                        UserInfo user = (UserInfo) gson.fromJson(result.getJSONObject("data")
                                .toString(), UserInfo.class);
                        user.setMyPassW(params.get("pass"));
                        application.setUserInfo(user);
                        saveUser(user);
                        Intent intent = new Intent();
                        intent.setClass(QlyLoginActivity.this, QlyListActicity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.show(QlyLoginActivity.this, R.string.account_pwd_inc);
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
                ToastUtil.show(QlyLoginActivity.this, R.string.login_fail);
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("qly_login");
    }

}


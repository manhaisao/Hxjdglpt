package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;

/**
 * Created by dbz on 2017/5/24.
 */
@ContentView(R.layout.aty_gkxz_fb)
public class GkxzFbActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.gkxz_name)
    private EditText edtName;//姓名
    @ViewInject(R.id.gkxz_phone)
    private EditText edtPhone;//手机号
    @ViewInject(R.id.gkxz_xz)
    private EditText edtRyxz;//人员性质
    @ViewInject(R.id.gkxz_fb_commit)
    private Button btnCommit;
    private User user;
    private String type;

    private String pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        pId = getIntent().getStringExtra("pId");
        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }

    public void initView() {
        if ("2".equals(type) || "3".equals(type) || "4".equals(type) || "7".equals(type)) {
            tvTitle.setText("管控小组人员录入");
        } else if ("5".equals(type)) {
            tvTitle.setText("矫正小组人员录入");
        } else if ("6".equals(type)) {
            tvTitle.setText("帮教小组人员录入");
        }
    }

    @Event(value = {R.id.iv_back, R.id.gkxz_fb_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.gkxz_fb_commit:
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    ToastUtil.show(this, "请输入姓名");
                } else {
                    commit();
                }
                break;
        }
    }

    private void commit() {
        SuccinctProgress.showSuccinctProgress(GkxzFbActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zfzzGroup/commitZfzzGroup";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", String.valueOf(type));
        params.put("pId", pId);
        params.put("ryxz", edtRyxz.getText().toString());
        params.put("name", edtName.getText().toString());
        params.put("phone", edtPhone.getText().toString());
        VolleyRequest.RequestPost(this, url, "commitZfzzGroup", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(GkxzFbActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GkxzFbActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GkxzFbActivity.this, R.string.commit_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitZfzzGroup");
    }

}

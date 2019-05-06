package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Fwmdm;
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
 * 服务面对面-我要咨询
 * Created by dbz on 2017/5/24.
 */
@ContentView(R.layout.aty_fwmdm_consult)
public class FwmdmConsultActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.fwmdm_e_title)
    private EditText edtTitle;
    @ViewInject(R.id.fwmdm_e_des)
    private EditText edtDes;
    @ViewInject(R.id.fwmdm_commit)
    private Button btnCommit;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.consult_self));
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    @Event(value = {R.id.iv_back, R.id.fwmdm_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fwmdm_commit:
                if (TextUtils.isEmpty(edtTitle.getText().toString())) {
                    ToastUtil.show(FwmdmConsultActivity.this, R.string.fwmdm_title_null);
                } else if (TextUtils.isEmpty(edtDes.getText().toString())) {
                    ToastUtil.show(FwmdmConsultActivity.this, R.string.fwmdm_des_null);
                } else {
                    commit();
                }
                break;

        }
    }


    private void commit() {
        SuccinctProgress.showSuccinctProgress(FwmdmConsultActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/m_fwmdm/addFwmdm";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("title", edtTitle.getText().toString());
        params.put("describe", edtDes.getText().toString());
        VolleyRequest.RequestPost(this, url, "fwmdm_add", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Fwmdm fwmdm = gson.fromJson(result.getJSONObject("data").toString(),
                                Fwmdm.class);
                        ToastUtil.show(FwmdmConsultActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(FwmdmConsultActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(FwmdmConsultActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("fwmdm_add");
    }

}

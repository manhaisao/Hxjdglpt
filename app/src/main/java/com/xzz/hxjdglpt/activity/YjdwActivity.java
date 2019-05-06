package com.xzz.hxjdglpt.activity;


import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Yjdw;
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
import java.util.Map;

/**
 * 信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_yjdw)
public class YjdwActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.jsbhz_commit)
    private Button btnCommit;

    @ViewInject(R.id.jsbhz_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.jsbhz_address)
    private EditText edtAddress;//地址

    @ViewInject(R.id.jsbhz_sfzh)
    private AppCompatSpinner spLx;

    @ViewInject(R.id.jsbhz_phone)
    private EditText edtPhone;//手机号
    @ViewInject(R.id.jsbhz_px)
    private EditText edtPx;//排序


    private User user;

    private boolean isAdd;//判断是否新增

    private Yjdw yjdw;

    private String[] type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("应急单位");
        type = getResources().getStringArray(R.array.hotline_type_list);
        //适配器
        ArrayAdapter cjTypeAdpate = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, type);
        //设置样式
        cjTypeAdpate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spLx.setAdapter(cjTypeAdpate);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            yjdw = getIntent().getParcelableExtra("yjdw");
            edtName.setText(yjdw.getName());//姓名
            edtPx.setText(String.valueOf(yjdw.getPx()));
            edtPhone.setText(yjdw.getPhone());
            edtAddress.setText(yjdw.getAddress());
            for (int i = 0; i < type.length; i++) {
                if (type[i].equals(yjdw.getType())) {
                    spLx.setSelection(i);
                    break;
                }
            }
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/yjdw/commitYjdw";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(spLx.getSelectedItem().toString())) {
            params.put("type", spLx.getSelectedItem().toString());
        } else {
            params.put("type", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtPx.getText().toString())) {
            params.put("px", edtPx.getText().toString());
        } else {
            params.put("px", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitYjdw", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(YjdwActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(YjdwActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(YjdwActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.jsbhz_commit, R.id.iv_back}, type
            = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.jsbhz_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitYjdw");
        BaseApplication.getRequestQueue().cancelAll("updateYjdw");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/yjdw/updateYjdw";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(yjdw.getId()));
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(spLx.getSelectedItem().toString())) {
            params.put("type", spLx.getSelectedItem().toString());
        } else {
            params.put("type", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtPx.getText().toString())) {
            params.put("px", edtPx.getText().toString());
        } else {
            params.put("px", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateYjdw", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(YjdwActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(YjdwActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(YjdwActivity.this, R.string.modify_fail);
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


}

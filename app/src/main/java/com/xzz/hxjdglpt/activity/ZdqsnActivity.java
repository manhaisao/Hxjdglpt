package com.xzz.hxjdglpt.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zdqsn;
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
@ContentView(R.layout.aty_zdqsn)
public class ZdqsnActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.jsbhz_commit)
    private Button btnCommit;

    @ViewInject(R.id.jsbhz_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.jsbhz_sfzh)
    private EditText edtCode;//身份证号

    @ViewInject(R.id.jsbhz_address)
    private EditText edtAddress;//住址

    @ViewInject(R.id.jsbhz_phone)
    private EditText edtPhone;//手机号

    @ViewInject(R.id.jsbhz_jhr)
    private EditText edtJhr;//责任人
    @ViewInject(R.id.jsbhz_jhrgx)
    private Spinner spinner;//监护人与当事人关系
    @ViewInject(R.id.jsbhz_jhrphone)
    private EditText edtJhrdh;//联系方式
    @ViewInject(R.id.jsbhz_gkxz_add)
    private TextView mAdd;//管控小组新增
    @ViewInject(R.id.jsbhz_t_gkxz_lay)
    private RelativeLayout gkxzLay;//管控小组

    @ViewInject(R.id.jsbhz_gkxz)
    private TextView tvGk;//管控小组数量

    private User user;

    private boolean isAdd;//判断是否新增

    private String gridId;//网格ID

    private Zdqsn zdqsn;

    private String[] cjType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("重点青少年信息");
        if (isAdd) {
            gkxzLay.setVisibility(View.GONE);
        } else {
            gkxzLay.setVisibility(View.VISIBLE);
        }
        cjType = getResources().getStringArray(R.array.relationType);
        //适配器
        ArrayAdapter cjTypeAdpate = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, cjType);
        //设置样式
        cjTypeAdpate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(cjTypeAdpate);

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            zdqsn = getIntent().getParcelableExtra("zdqsn");
            edtName.setText(zdqsn.getName());//姓名
            edtAddress.setText(zdqsn.getAddress());//地址
            edtCode.setText(zdqsn.getSfzh());
            edtPhone.setText(zdqsn.getPhone());
            edtJhr.setText(zdqsn.getJhr());
            edtJhrdh.setText(zdqsn.getJhrPhone());
            //监护人与当事人关系----1,夫妻 2,父子 3,父女 4,母子 5,母女 6,婆媳 7,兄弟 8,兄妹
            spinner.setSelection(zdqsn.getRelationType() - 1);
            getXzCount();
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }

    private void getXzCount() {
        String url = ConstantUtil.BASE_URL + "/zfzzGroup/getCount";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pId", String.valueOf(zdqsn.getId()));
        params.put("type", "3");
        VolleyRequest.RequestPost(this, url, "getCount", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        long count = result.getLong("count");
                        tvGk.setText(String.valueOf(count));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZdqsnActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZdqsnActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/zdqsn/commitZdqsn";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtCode.getText().toString())) {
            params.put("sfzh", edtCode.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
//            if (edtPhone.getText().toString().length() != 11) {
//                ToastUtil.show(this, "请输入11位手机号");
//                return;
//            }
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtJhr.getText().toString())) {
            params.put("jhr", edtJhr.getText().toString());
        } else {
            params.put("jhr", "");
        }
        if (!TextUtils.isEmpty(edtJhrdh.getText().toString())) {
            params.put("jhrPhone", edtJhrdh.getText().toString());
        } else {
            params.put("jhrPhone", "");
        }

        for (int i = 0; i < cjType.length; i++) {
            if (cjType[i].equals(spinner.getSelectedItem().toString())) {
                params.put("relationType", String.valueOf(i + 1));
            }
        }
        params.put("bz", "");
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitZdqsn", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(ZdqsnActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZdqsnActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZdqsnActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.jsbhz_commit, R.id.iv_back, R.id.jsbhz_gkxz_add}, type = View
            .OnClickListener.class)
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
            case R.id.jsbhz_gkxz_add:
                Intent intent = new Intent();
                intent.setClass(ZdqsnActivity.this, GkxzListActivity.class);
                intent.putExtra("type", "3");
                intent.putExtra("pId", String.valueOf(zdqsn.getId()));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitZdqsn");
        BaseApplication.getRequestQueue().cancelAll("updateZdqsn");
        BaseApplication.getRequestQueue().cancelAll("getCount");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/zdqsn/updateZdqsn";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(zdqsn.getId()));
        params.put("gridId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtCode.getText().toString())) {
            params.put("sfzh", edtCode.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
//            if (edtPhone.getText().toString().length() != 11) {
//                ToastUtil.show(this, "请输入11位手机号");
//                return;
//            }
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtJhr.getText().toString())) {
            params.put("jhr", edtJhr.getText().toString());
        } else {
            params.put("jhr", "");
        }
        if (!TextUtils.isEmpty(edtJhrdh.getText().toString())) {
            params.put("jhrPhone", edtJhrdh.getText().toString());
        } else {
            params.put("jhrPhone", "");
        }

        for (int i = 0; i < cjType.length; i++) {
            if (cjType[i].equals(spinner.getSelectedItem().toString())) {
                params.put("relationType", String.valueOf(i + 1));
            }
        }
        params.put("bz", "");
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateZdqsn", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(ZdqsnActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZdqsnActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZdqsnActivity.this, R.string.modify_fail);
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

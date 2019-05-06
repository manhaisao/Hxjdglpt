package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.BusinessJob;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.VillageSxgzInfo;
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
 * 十项——企业工作录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_village_qygz)
public class VillageQygzActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.village_qygz_commit)
    private Button btnCommit;

    @ViewInject(R.id.village_qygz_zrr)
    private TextView edtZrr;
    @ViewInject(R.id.village_qygz_zrrphone)
    private EditText edtZrrPhone;

    @ViewInject(R.id.village_qygz_hszrr)
    private TextView edtHsZrr;
    @ViewInject(R.id.village_qygz_hszrrphone)
    private EditText edtHsZrrPhone;

    @ViewInject(R.id.village_qygz_ltqy)
    private TextView tvLtqy;
    @ViewInject(R.id.village_qygz_myqy)
    private TextView tvMzqy;
    @ViewInject(R.id.village_qygz_jtqy)
    private TextView tvJtqy;
    @ViewInject(R.id.village_qygz_zbqy)
    private TextView tvZb;
    @ViewInject(R.id.village_qygz_jzgd)
    private TextView tvJzgd;
    @ViewInject(R.id.village_qygz_gt)
    private TextView tvGt;
    @ViewInject(R.id.village_qygz_dkd)
    private TextView tvDkd;
    @ViewInject(R.id.village_qygz_bz)
    private EditText edtBz;


    private User user;
    private BaseApplication application;

    private Village village;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.qygz));
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        village = getIntent().getParcelableExtra("village");
        type = getIntent().getIntExtra("type", 0);
        initData();
        initView();
    }

    public void initData() {
        loadDataByGid();
        loadByGid();
    }

    public void initView() {

    }


    @Event(value = {R.id.iv_back, R.id.village_qygz_commit, R.id.village_qygz_add_dkd}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_qygz_commit:
                commit();
                break;
            case R.id.village_qygz_add_dkd:
                Intent intent = new Intent();
                intent.setClass(VillageQygzActivity.this, DkdListActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/villageSxgzInfo/commitVillageSxgzInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        params.put("type", String.valueOf(type));
        if (!TextUtils.isEmpty(edtZrr.getText().toString())) {
            params.put("wgz", edtZrr.getText().toString());
        } else {
            params.put("wgz", "");
        }
        if (!TextUtils.isEmpty(edtZrrPhone.getText().toString())) {
            if (edtZrrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wgzPhone", edtZrrPhone.getText().toString());
        } else {
            params.put("wgzPhone", "");
        }
        if (!TextUtils.isEmpty(edtHsZrr.getText().toString())) {
            params.put("hszrr", edtHsZrr.getText().toString());
        } else {
            params.put("hszrr", "");
        }
        if (!TextUtils.isEmpty(edtHsZrrPhone.getText().toString())) {
            if (edtHsZrrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("hszrrphone", edtHsZrrPhone.getText().toString());
        } else {
            params.put("hszrrphone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(VillageQygzActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "commitVillageSxgzInfo", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            if ("1".equals(resultCode)) {
                                ToastUtil.show(VillageQygzActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageQygzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageQygzActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("commitVillageSxgzInfo");
        BaseApplication.getRequestQueue().cancelAll("queryBusinessByVId");
        BaseApplication.getRequestQueue().cancelAll("queryVillageSxgzInfo");
    }

    private void loadByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageDjActivity.this, "请求数据中···",
//                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/villageSxgzInfo/queryVillageSxgzInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        params.put("type", String.valueOf(type));
        VolleyRequest.RequestPost(this, url, "queryVillageSxgzInfo", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                VillageSxgzInfo sp = (VillageSxgzInfo) gson.fromJson(result.getJSONObject
                                        ("data").toString(), VillageSxgzInfo.class);
                                edtZrr.setText(sp.getWgz() == null ? "" : sp.getWgz());
                                edtZrrPhone.setText(sp.getWgzPhone() == null ? "" : sp.getWgzPhone());
                                edtHsZrr.setText(sp.getHszrr() == null ? "" : sp.getHszrr());
                                edtHsZrrPhone.setText(sp.getHszrrphone() == null ? "" : sp.getHszrrphone());
                                edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageQygzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageQygzActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
//                SuccinctProgress.dismiss();
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    private void loadDataByGid() {
        SuccinctProgress.showSuccinctProgress(VillageQygzActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qygz/queryBusinessByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryBusinessByVId", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                BusinessJob sp = (BusinessJob) gson.fromJson(result.getJSONObject("data")
                                        .toString(), BusinessJob.class);
                                tvJtqy.setText(String.valueOf(sp.getJt()));
                                tvLtqy.setText(String.valueOf(sp.getLt()));
                                tvMzqy.setText(String.valueOf(sp.getMy()));
                                tvGt.setText(String.valueOf(sp.getGt()));
                                tvJzgd.setText(String.valueOf(sp.getJz()));
                                tvDkd.setText(String.valueOf(sp.getDkd()));
                                tvZb.setText(String.valueOf(sp.getZbs()));
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageQygzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageQygzActivity.this, R.string.load_fail);
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
    public void onDestroy() {
        super.onDestroy();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(VillageQygzActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

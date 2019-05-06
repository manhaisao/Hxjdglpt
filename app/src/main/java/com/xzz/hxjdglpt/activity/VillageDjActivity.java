package com.xzz.hxjdglpt.activity;

import android.content.Intent;
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
import com.xzz.hxjdglpt.model.PartyBuilding;
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
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_village_dj)
public class VillageDjActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.village_dj_commit)
    private Button btnCommit;

    @ViewInject(R.id.village_dj_zrr)
    private EditText edtZrr;
    @ViewInject(R.id.village_dj_zrrPhone)
    private EditText edtZrrPhone;
    @ViewInject(R.id.village_dj_dxzs)
    private TextView tvDxzs;
    @ViewInject(R.id.village_dj_dys)
    private TextView tvDys;
    @ViewInject(R.id.village_dj_kndys)
    private TextView tvKnDys;
    @ViewInject(R.id.village_dj_tshd)
    private TextView tvTshd;
    @ViewInject(R.id.village_dj_bz)
    private EditText edtBz;
    @ViewInject(R.id.village_dj_fgr)
    private EditText edtFgr;
    @ViewInject(R.id.village_dj_fgrPhone)
    private EditText edtFgrPhone;


    private User user;
    private BaseApplication application;

    private Village village;

    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.dj));
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
        loadDjByGid();
    }

    public void initView() {
    }


    @Override
    public void onResume() {
        super.onResume();
        loadDjByGid_();
    }


    @Event(value = {R.id.iv_back, R.id.village_dj_commit, R.id.village_dj_add_tshd}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_dj_commit:
                commit();
                break;
            case R.id.village_dj_add_tshd:
                //链接
                Intent intent = new Intent();
                intent.putExtra("vId", village.getId());
                intent.putExtra("type", -1);
                intent.setClass(VillageDjActivity.this, ListActivity.class);
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
            params.put("zrr", edtZrr.getText().toString());
        }
        if (!TextUtils.isEmpty(edtZrrPhone.getText().toString())) {
            if (edtZrrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("zrrPhone", edtZrrPhone.getText().toString());
        } else {
            params.put("zrrPhone", "");
        }
        if (!TextUtils.isEmpty(edtFgr.getText().toString())) {
            params.put("fgr", edtFgr.getText().toString());
        }
        if (!TextUtils.isEmpty(edtFgrPhone.getText().toString())) {
            params.put("fgrphone", edtFgrPhone.getText().toString());
        } else {
            params.put("fgrphone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(VillageDjActivity.this, "数据提交中···",
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
                                ToastUtil.show(VillageDjActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageDjActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageDjActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryVillageSxgzInfo");
        BaseApplication.getRequestQueue().cancelAll("queryPartyBuildingByVid");
    }


    private void loadDjByGid() {
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
                                edtZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                                edtZrrPhone.setText(sp.getZrrPhone() == null ? "" : sp.getZrrPhone());
                                edtFgr.setText(sp.getFgr() == null ? "" : sp.getFgr());
                                edtFgrPhone.setText(sp.getFgrphone() == null ? "" : sp.getFgrphone());
                                edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageDjActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageDjActivity.this, R.string.load_fail);
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


    private void loadDjByGid_() {
        SuccinctProgress.showSuccinctProgress(VillageDjActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dj/queryPartyBuildingByVid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryPartyBuildingByVid", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                PartyBuilding sp = (PartyBuilding) gson.fromJson(result.getJSONObject
                                        ("data").toString(), PartyBuilding.class);
                                tvDxzs.setText(String.valueOf(sp.getDxznum()));
                                if (TextUtils.isEmpty(sp.getDnum())) {
                                    tvDys.setText("0");
                                } else {
                                    tvDys.setText(String.valueOf(sp.getDnum()));
                                }
                                tvKnDys.setText(String.valueOf(sp.getKndynum()));
                                tvTshd.setText(String.valueOf(sp.getTshdNum()));
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageDjActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageDjActivity.this, R.string.load_fail);
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
    }
}

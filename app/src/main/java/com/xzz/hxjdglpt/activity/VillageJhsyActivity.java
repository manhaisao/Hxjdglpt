package com.xzz.hxjdglpt.activity;

import android.content.Context;
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
import com.xzz.hxjdglpt.model.FamilyPlanning;
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
 * 卫生健康信息录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_village_jhsy)
public class VillageJhsyActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.village_jhsy_commit)
    private Button btnCommit;

    @ViewInject(R.id.village_jhsy_zrr)
    private EditText edtZrr;
    @ViewInject(R.id.village_jhsy_zrrphone)
    private EditText edtZrrPhone;
    @ViewInject(R.id.village_jhsy_sqwss)
    private EditText edtSqwss;
    @ViewInject(R.id.village_jhsy_sqwssphone)
    private EditText edtSqwssPhone;

    @ViewInject(R.id.village_jhsy_ylfns)
    private TextView tvYlfn;//育龄妇女
    @ViewInject(R.id.village_jhsy_dszn)
    private TextView tvDszn;//独生子女
    @ViewInject(R.id.village_jhsy_jlfz)
    private TextView tvJlfz;//省扶
    @ViewInject(R.id.village_jhsy_jlfz_sf)
    private TextView tvJlfz_sf;//市扶
    @ViewInject(R.id.village_jhsy_jlfz_sd)
    private TextView tvJlfz_sd;//失独
    @ViewInject(R.id.village_jhsy_jlfz_sc)
    private TextView tvJlfz_sc;//伤残
    @ViewInject(R.id.village_jhsy_ldrk)
    private TextView tvLdrk;//流动人口
    @ViewInject(R.id.village_jhsy_jskn)
    private TextView tvJskn;//计生困难
    @ViewInject(R.id.village_jhsy_bz)
    private EditText edtBz;


    private User user;
    private BaseApplication application;

    private Village village;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.jhsy));
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        village = getIntent().getParcelableExtra("village");
        type = getIntent().getIntExtra("type", 0);
        initView();
        initData();
    }

    public void initData() {
        loadDataByGid();
        loadByGid();
    }

    public void initView() {
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Event(value = {R.id.iv_back, R.id.village_jhsy_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_jhsy_commit:
                commit();
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
        if (!TextUtils.isEmpty(edtSqwss.getText().toString())) {
            params.put("sqwss", edtSqwss.getText().toString());
        }
        if (!TextUtils.isEmpty(edtSqwssPhone.getText().toString())) {
//            if (edtSqwssPhone.getText().toString().length() != 11) {
//                ToastUtil.show(this, "请输入11位手机号");
//                return;
//            }
            params.put("sqwssphone", edtSqwssPhone.getText().toString());
        } else {
            params.put("sqwssphone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(VillageJhsyActivity.this, "数据提交中···",
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
                        ToastUtil.show(VillageJhsyActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageJhsyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageJhsyActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("jhsy_commit");
        BaseApplication.getRequestQueue().cancelAll("queryJhsyByVId");
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
                        edtZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        edtZrrPhone.setText(sp.getZrrPhone() == null ? "" : sp.getZrrPhone());
                        edtSqwss.setText(sp.getSqwss() == null ? "" : sp.getSqwss());
                        edtSqwssPhone.setText(sp.getSqwssphone() == null ? "" : sp.getSqwssphone());
                        edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageJhsyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageJhsyActivity.this, R.string.load_fail);
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
        SuccinctProgress.showSuccinctProgress(VillageJhsyActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jhsy/queryJhsyByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryJhsyByVId", params, new
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
                        FamilyPlanning sp = (FamilyPlanning) gson.fromJson(result.getJSONObject
                                ("data").toString(), FamilyPlanning.class);
                        tvYlfn.setText(String.valueOf(sp.getYlfnnum()));//育龄妇女
                        tvDszn.setText(String.valueOf(sp.getOnechildfamilynum()));//独生子女
                        tvJlfz.setText(String.valueOf(sp.getShengfNum()));//省扶
                        tvJlfz_sf.setText(String.valueOf(sp.getShifNum()));//市扶
                        tvJlfz_sd.setText(String.valueOf(sp.getSdNum()));//失独
                        tvJlfz_sc.setText(String.valueOf(sp.getScNum()));//伤残
                        tvLdrk.setText(String.valueOf(sp.getLdrknum()));//流动人口
                        tvJskn.setText(String.valueOf(sp.getJsknnum()));//计生困难
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageJhsyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageJhsyActivity.this, R.string.load_fail);
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
                .hideSoftInputFromWindow(VillageJhsyActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

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
import com.xzz.hxjdglpt.model.Minzheng;
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
 * 民政信息录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_village_mz)
public class VillageMzActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.village_mz_commit)
    private Button btnCommit;

    @ViewInject(R.id.village_mz_zrr)
    private EditText edtZrr;
    @ViewInject(R.id.village_mz_zrrphone)
    private EditText edtZrrPhone;
    @ViewInject(R.id.village_mz_ddy)
    private EditText edtDdy;
    @ViewInject(R.id.village_mz_ddyphone)
    private EditText edtDdyPhone;
    @ViewInject(R.id.village_mz_ncdb)
    private TextView edtNcdb;//农村低保
    @ViewInject(R.id.village_mz_csdb)
    private TextView edtCsdb;//城市低保
    @ViewInject(R.id.village_mz_cjr)
    private TextView tvCjr;//残疾人
    @ViewInject(R.id.village_mz_lset)
    private TextView tvLsrt;//留守儿童
    @ViewInject(R.id.village_mz_yfdx)
    private TextView tvYfdx;//优抚对象
    @ViewInject(R.id.village_mz_cjr_1)
    private TextView tvCjr_1;//持证残疾人
    @ViewInject(R.id.village_mz_cjr_2)
    private TextView tvCjr_2;//重残生活补贴
    @ViewInject(R.id.village_mz_cjr_3)
    private TextView tvCjr_3;//其他类生活补贴
    @ViewInject(R.id.village_mz_cjr_4)
    private TextView tvCjr_4;//一级护理补贴
    @ViewInject(R.id.village_mz_cjr_5)
    private TextView tvCjr_5;//二级护理补贴
    @ViewInject(R.id.village_mz_tkgy)
    private TextView tvTkgy;//特困(五保)供养
    @ViewInject(R.id.village_mz_tkgy_1)
    private TextView tvTkgy_1;//集中供养人员
    @ViewInject(R.id.village_mz_tkgy_2)
    private TextView tvTkgy_2;//分散供养人员
    @ViewInject(R.id.village_mz_tkgy_3)
    private TextView tvTkgy_3;//城市三无人员
    @ViewInject(R.id.village_mz_tkgy_4)
    private TextView tvTkgy_4;//孤儿及困境儿童
    @ViewInject(R.id.village_mz_zlj)
    private TextView tvZlj;//80岁以上尊老金
    @ViewInject(R.id.village_mz_zlj_1)
    private TextView tvZlj_1;//80-89周岁人员
    @ViewInject(R.id.village_mz_zlj_2)
    private TextView tvZlj_2;//90-99周岁人员
    @ViewInject(R.id.village_mz_zlj_3)
    private TextView tvZlj_3;//100周岁以上

    @ViewInject(R.id.village_mz_bz)
    private EditText edtBz;
    private User user;
    private BaseApplication application;

    private Village village;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.mz));
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
        loadMzByGid();
        loadDataByGid();
    }

    public void initView() {
    }


    @Event(value = {R.id.iv_back, R.id.village_mz_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_mz_commit:
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
        if (!TextUtils.isEmpty(edtDdy.getText().toString())) {
            params.put("mzd", edtDdy.getText().toString());
        }
        if (!TextUtils.isEmpty(edtDdyPhone.getText().toString())) {
            if (edtDdyPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("mzdphone", edtDdyPhone.getText().toString());
        } else {
            params.put("mzdphone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(VillageMzActivity.this, "数据提交中···",
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
                        ToastUtil.show(VillageMzActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageMzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageMzActivity.this, R.string.commit_fail);
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


    private void loadMzByGid() {
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
                        edtDdy.setText(sp.getMzd() == null ? "" : sp.getMzd());
                        edtDdyPhone.setText(sp.getMzdphone() == null ? "" : sp.getMzdphone());
                        edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageMzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageMzActivity.this, R.string.load_fail);
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

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitVillageSxgzInfo");
        BaseApplication.getRequestQueue().cancelAll("queryVillageSxgzInfo");
        BaseApplication.getRequestQueue().cancelAll("queryMzByVId");
    }

    private void loadDataByGid() {
        SuccinctProgress.showSuccinctProgress(VillageMzActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/mz/queryMzByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryMzByVId", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Minzheng sp = (Minzheng) gson.fromJson(result.getJSONObject("data")
                                .toString(), Minzheng.class);
//                        edtZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
//                        edtZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        edtNcdb.setText(sp.getNcdbhs() == null ? "" : String.valueOf(sp.getNcdbhs
                                ()));
                        edtCsdb.setText(sp.getCsdbhs() == null ? "" : String.valueOf(sp.getCsdbhs
                                ()));
                        tvCjr.setText(sp.getCjrnum() == null ? "" : String.valueOf(sp.getCjrnum()));
                        tvLsrt.setText(sp.getLsrtnum() == null ? "" : String.valueOf(sp
                                .getLsrtnum()));
                        tvYfdx.setText(sp.getYfdx() == null ? "" : String.valueOf(sp.getYfdx()));
//                        edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                        tvCjr_1.setText(String.valueOf(sp.getCzCjrNum()));
                        tvCjr_2.setText(String.valueOf(sp.getZcCjrNum()));
                        tvCjr_3.setText(String.valueOf(sp.getQtCjrNum()));
                        tvCjr_4.setText(String.valueOf(sp.getYjCjrNum()));
                        tvCjr_5.setText(String.valueOf(sp.getRjCjrNum()));
                        tvTkgy.setText(String.valueOf(sp.getTkgyNum()));
                        tvTkgy_1.setText(String.valueOf(sp.getTkgyNum_1()));
                        tvTkgy_2.setText(String.valueOf(sp.getTkgyNum_2()));
                        tvTkgy_3.setText(String.valueOf(sp.getTkgyNum_3()));
                        tvTkgy_4.setText(String.valueOf(sp.getTkgyNum_4()));
                        tvZlj.setText(String.valueOf(sp.getZljNum()));
                        tvZlj_1.setText(String.valueOf(sp.getZljNum_1()));
                        tvZlj_2.setText(String.valueOf(sp.getZljNum_2()));
                        tvZlj_3.setText(String.valueOf(sp.getZljNum_3()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageMzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageMzActivity.this, R.string.load_fail);
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
                .hideSoftInputFromWindow(VillageMzActivity.this.getCurrentFocus().getWindowToken
                        (), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

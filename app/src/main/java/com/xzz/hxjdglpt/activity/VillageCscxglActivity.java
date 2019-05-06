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
import com.xzz.hxjdglpt.model.CityManage;
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
 * 城市长效管理信息录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_village_cscxgl)
public class VillageCscxglActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.village_cscxgl_commit)
    private Button btnCommit;

    @ViewInject(R.id.village_cscxgl_zrr)
    private EditText edtZrr;
    @ViewInject(R.id.village_cscxgl_zrrphone)
    private EditText edtZrrPhone;
    @ViewInject(R.id.village_cscxgl_sqgj)
    private EditText edtSqgj;
    @ViewInject(R.id.village_cscxgl_sqgjphone)
    private EditText edtSqgjPhone;
    @ViewInject(R.id.village_cscxgl_area)
    private TextView tvArea;//面积
    @ViewInject(R.id.village_cscxgl_hu)
    private TextView tvHu;//户数
    @ViewInject(R.id.village_cscxgl_wgz)
    private EditText edtWgz;//网格长
    @ViewInject(R.id.village_cscxgl_wgzphone)
    private EditText edtWgzPhone;//网格长电话
    @ViewInject(R.id.village_cscxgl_ljqyy)
    private TextView tvLjqyy;//垃圾清运员
    @ViewInject(R.id.village_cscxgl_ldbjy)
    private TextView tvLdbjy;//流动保洁员
    @ViewInject(R.id.village_cscxgl_ljc)
    private TextView tvLjc;//垃圾池
    @ViewInject(R.id.village_cscxgl_ljt)
    private TextView tvLjt;//垃圾桶
    @ViewInject(R.id.village_cscxgl_ljrcl)
    private TextView tvRcl;//垃圾日产量
    @ViewInject(R.id.village_cscxgl_ljysd)
    private TextView tvYsd;//垃圾运输点
    @ViewInject(R.id.village_cscxgl_qt)
    private EditText edtQt;//其他管理设备
    @ViewInject(R.id.village_cscxgl_content)
    private EditText edtContent;//其他关于网格内容信息
    @ViewInject(R.id.village_cscxgl_bz)
    private EditText edtBz;
    @ViewInject(R.id.village_cscxgl_jdwgy)
    private TextView village_cscxgl_jdwgy;//街道网格员
    @ViewInject(R.id.village_cscxgl_cjwgy)
    private TextView village_cscxgl_cjwgy;//村居网格员
    @ViewInject(R.id.village_cscxgl_ld)
    private TextView village_cscxgl_ld;//路灯
    @ViewInject(R.id.village_cscxgl_rk)
    private TextView village_cscxgl_rk;//人口总数
    @ViewInject(R.id.village_cscxgl_xqzs)
    private TextView village_cscxgl_xqzs;//小区总数
    @ViewInject(R.id.village_cscxgl_bjcl)
    private TextView village_cscxgl_bjcl;//保洁车辆
    @ViewInject(R.id.village_cscxgl_ba)
    private TextView village_cscxgl_ba;//保安

    @ViewInject(R.id.village_cscxgl_jhgb)
    private EditText village_cscxgl_jhgb;
    @ViewInject(R.id.village_cscxgl_jhgbphone)
    private EditText village_cscxgl_jhgbphone;

    private User user;
    private BaseApplication application;

    private Village village;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.cscxgl));
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

    @Event(value = {R.id.iv_back, R.id.village_cscxgl_commit,R.id.cscxgl_add_ba}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_cscxgl_commit:
                commit();
                break;
            case R.id.cscxgl_add_ba:
                Intent intent = new Intent();
                intent.setClass(VillageCscxglActivity.this, BaryListActivity.class);
                intent.putExtra("pid", village.getId());
                intent.putExtra("plotName", village.getName());
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
        if (!TextUtils.isEmpty(edtWgz.getText().toString())) {
            params.put("wgz", edtWgz.getText().toString());
        }
        if (!TextUtils.isEmpty(edtWgzPhone.getText().toString())) {
            if (edtWgzPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wgzPhone", edtWgzPhone.getText().toString());
        } else {
            params.put("wgzPhone", "");
        }
        if (!TextUtils.isEmpty(edtSqgj.getText().toString())) {
            params.put("sqgj", edtSqgj.getText().toString());
        }
        if (!TextUtils.isEmpty(edtSqgjPhone.getText().toString())) {
//            if (edtSqgjPhone.getText().toString().length() != 11) {
//                ToastUtil.show(this, "请输入11位手机号");
//                return;
//            }
            params.put("sqgjphone", edtSqgjPhone.getText().toString());
        } else {
            params.put("sqgjphone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtContent.getText().toString())) {
            params.put("qtwgnr", edtContent.getText().toString());
        } else {
            params.put("qtwgnr", "");
        }
        if (!TextUtils.isEmpty(edtQt.getText().toString())) {
            params.put("qtglsb", edtQt.getText().toString());
        } else {
            params.put("qtglsb", "");
        }

        if (!TextUtils.isEmpty(village_cscxgl_jhgb.getText().toString())) {
            params.put("jhgb", village_cscxgl_jhgb.getText().toString());
        } else {
            params.put("jhgb", "");
        }
        if (!TextUtils.isEmpty(village_cscxgl_jhgbphone.getText().toString())) {
            params.put("jhgbphone", village_cscxgl_jhgbphone.getText().toString());
        } else {
            params.put("jhgbphone", "");
        }



        SuccinctProgress.showSuccinctProgress(VillageCscxglActivity.this, "数据提交中···",
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
                                ToastUtil.show(VillageCscxglActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageCscxglActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageCscxglActivity.this, R.string.commit_fail);
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
                                edtWgz.setText(sp.getWgz() == null ? "" : sp.getWgz());
                                edtWgzPhone.setText(sp.getWgzPhone() == null ? "" : sp.getWgzPhone());
                                edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                                edtContent.setText(sp.getQtwgnr() == null ? "" : sp.getQtwgnr());
                                edtQt.setText(sp.getQtglsb() == null ? "" : sp.getQtglsb());
                                edtSqgj.setText(sp.getSqgj() == null ? "" : sp.getSqgj());
                                edtSqgjPhone.setText(sp.getSqgjphone() == null ? "" : sp.getSqgjphone());
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageCscxglActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageCscxglActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryCscxglByVId");
    }

    private void loadDataByGid() {
        SuccinctProgress.showSuccinctProgress(VillageCscxglActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cscxgl/queryCscxglByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryCscxglByVId", params, new
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
                                CityManage sp = (CityManage) gson.fromJson(result.getJSONObject("data").toString(), CityManage.class);
                                tvArea.setText(sp.getArea() == null ? "0" : sp.getArea());
                                tvHu.setText(sp.getHs() == null ? "0" : sp.getHs());
                                tvLjqyy.setText(String.valueOf(sp.getLjclear()));
                                tvLdbjy.setText(sp.getLiudong() == null ? "0" : sp.getLiudong());
                                tvLjc.setText(sp.getLjc() == null ? "0" : sp.getLjc());
                                tvLjt.setText(sp.getLjt() == null ? "0" : sp.getLjt());
                                tvYsd.setText(sp.getLjaddress() == null ? "0" : sp.getLjaddress());
                                tvRcl.setText(sp.getLjnum() == null ? "0" : sp.getLjnum());
                                village_cscxgl_jdwgy.setText(sp.getJdwgy()==null?"0":sp.getJdwgy());
                                village_cscxgl_cjwgy.setText(sp.getCjwgy()==null?"0":sp.getCjwgy());
                                village_cscxgl_ld.setText(sp.getLd()==null?"0":sp.getLd());
                                village_cscxgl_rk.setText(sp.getRk()==null?"0":sp.getRk());
                                village_cscxgl_xqzs.setText(sp.getXqzs()==null?"0":sp.getXqzs());
                                village_cscxgl_bjcl.setText(sp.getBjcl()==null?"0":sp.getBjcl());
                                village_cscxgl_ba.setText(sp.getBa()==null?"0":sp.getBa());
                                village_cscxgl_jhgb.setText(sp.getJhgb()==null?"":sp.getJhgb());
                                village_cscxgl_jhgbphone.setText(sp.getJhgbphone()==null?"":sp.getJhgbphone());

                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(VillageCscxglActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(VillageCscxglActivity.this, R.string.load_fail);
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
                .hideSoftInputFromWindow(VillageCscxglActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

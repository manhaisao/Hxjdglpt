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
import com.xzz.hxjdglpt.model.Sfxz;
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
 * 司法行政信息录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_village_sfxz)
public class VillageSfxzActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.village_sfxz_commit)
    private Button btnCommit;

    @ViewInject(R.id.village_sfxz_zrr)
    private EditText edtZrr;
    @ViewInject(R.id.village_sfxz_zrrphone)
    private EditText edtZrrPhone;
    @ViewInject(R.id.village_sfxz_jsb)
    private TextView tvJsb;//肇事肇祸精神病患者
    @ViewInject(R.id.village_sfxz_zdqsn)
    private TextView tvZdqsn;//重点青少年
    @ViewInject(R.id.village_sfxz_sqfx)
    private TextView tvSqfx;//社区服刑
    @ViewInject(R.id.village_sfxz_sqjdry)
    private TextView tvSqjd;//社区戒毒人员
    @ViewInject(R.id.village_sfxz_xjry)
    private TextView tvXjry;//邪教人员
    @ViewInject(R.id.village_sfxz_azbj)
    private TextView tvAzbj;//安置帮教人员
    @ViewInject(R.id.village_sfxz_bz)
    private EditText edtBz;
    @ViewInject(R.id.village_sfxz_djy)
    private TextView tvDjy;//矛盾纠纷调解员
    @ViewInject(R.id.village_sfxz_xxy)
    private TextView tvXxy;//矛盾纠纷信息员
    @ViewInject(R.id.village_sfxz_xcy)
    private TextView tvXcy;//法制宣传员
    @ViewInject(R.id.village_sfxz_zyz)
    private TextView tvZyz;//普法志愿者
    @ViewInject(R.id.village_sfxz_flgw)
    private TextView tvFlgw;//法律顾问

    private User user;
    private BaseApplication application;

    private Village village;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.sfxz));
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
        loadByGid();
    }

    public void initView() {
    }


    @Event(value = {R.id.iv_back, R.id.village_sfxz_commit, R.id.village_sfxz_add_djy, R.id
            .village_sfxz_add_xxy, R.id.village_sfxz_add_xcy, R.id.village_sfxz_add_zyz, R.id
            .village_sfxz_add_flgw}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("vId", village.getId());
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_sfxz_commit:
                commit();
                break;
            case R.id.village_sfxz_add_djy:
                intent.putExtra("type", 50);
                intent.setClass(VillageSfxzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.village_sfxz_add_xxy:
                intent.putExtra("type", 51);
                intent.setClass(VillageSfxzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.village_sfxz_add_xcy:
                intent.putExtra("type", 52);
                intent.setClass(VillageSfxzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.village_sfxz_add_zyz:
                intent.putExtra("type", 53);
                intent.setClass(VillageSfxzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.village_sfxz_add_flgw:
                intent.putExtra("type", 54);
                intent.setClass(VillageSfxzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataByGid();
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
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(VillageSfxzActivity.this, "数据提交中···",
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
                        ToastUtil.show(VillageSfxzActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageSfxzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageSfxzActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("querySfxzByVId");
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
                        edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageSfxzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageSfxzActivity.this, R.string.load_fail);
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
        SuccinctProgress.showSuccinctProgress(VillageSfxzActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sfxz/querySfxzByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "querySfxzByVId", params, new
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
                        Sfxz sp = (Sfxz) gson.fromJson(result.getJSONObject("data").toString(),
                                Sfxz.class);
                        tvZdqsn.setText(String.valueOf(sp.getZdqsn()));
                        tvXjry.setText(String.valueOf(sp.getXjry()));
                        tvSqfx.setText(String.valueOf(sp.getSqfsman()));
                        tvSqjd.setText(String.valueOf(sp.getSqjd()));
                        tvAzbj.setText(String.valueOf(sp.getAzbjry()));
                        tvJsb.setText(String.valueOf(sp.getZsjsb()));
                        tvDjy.setText(String.valueOf(sp.getMdjfdjy()));
                        tvFlgw.setText(String.valueOf(sp.getFlgw()));
                        tvXcy.setText(String.valueOf(sp.getFzxcy()));
                        tvXxy.setText(String.valueOf(sp.getMdjfxxy()));
                        tvZyz.setText(String.valueOf(sp.getPfzyz()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageSfxzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageSfxzActivity.this, R.string.load_fail);
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
                .hideSoftInputFromWindow(VillageSfxzActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

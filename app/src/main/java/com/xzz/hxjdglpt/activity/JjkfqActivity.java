package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.BusinessJob;
import com.xzz.hxjdglpt.model.Role;
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
 * 地图详情
 * Created by dbz on 2017/7/13.
 */
@ContentView(R.layout.aty_jjkfq)
public class JjkfqActivity extends BaseActivity {

    private Village village;

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;

    private BaseApplication application;

    //企业服务
    @ViewInject(R.id.village_v_t_zrr)
    private TextView tvWgz_t;
    @ViewInject(R.id.village_v_zrr)
    private TextView tvWgz_v;
    @ViewInject(R.id.village_v_t_zrrphone)
    private TextView tvWgz_t_p;
    @ViewInject(R.id.village_v_zrrphone)
    private TextView tvWgz_v_p;
    @ViewInject(R.id.village_v_hszrr)
    private TextView tvHs_v;
    @ViewInject(R.id.village_v_hszrrphone)
    private TextView tvHs_v_p;
    @ViewInject(R.id.qygz_ltqy)
    private TextView tvLtqy;
    @ViewInject(R.id.qygz_myqy)
    private TextView tvMzqy;
    private int lt = 0, my = 0, jt = 0, zb = 0, jz = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        village = getIntent().getParcelableExtra("village");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(village.getName());
        loadQygzData();
    }

    public void initData() {
    }


    private void loadQygzData() {
        SuccinctProgress.showSuccinctProgress(JjkfqActivity.this, "请求数据中···",
                SuccinctProgress
                        .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qygz/queryJjkfq";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryListVillage", params, new
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
                                BusinessJob sp = (BusinessJob) gson.fromJson(result.getJSONObject("data")
                                        .toString(), BusinessJob.class);
                                tvWgz_v.setText(sp.getWgzrr() == null ? "" : sp.getWgzrr());
                                tvWgz_v_p.setText(sp.getWgzrrdh() == null ? "" : sp.getWgzrrdh());
                                tvHs_v.setText(sp.getHszrr() == null ? "" : sp.getHszrr());
                                tvHs_v_p.setText(sp.getHszrrphone() == null ? "" : sp.getHszrrphone());
                                tvLtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                tvMzqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                int num = sp.getJt() + sp.getJz() + sp.getLt() + sp.getMy() + sp.getZbs();
                                lt = sp.getLt();
                                my = sp.getMy();
                                jt = sp.getJt();
                                zb = sp.getZbs();
                                jz = sp.getJz();
                                tvLtqy.setText(String.valueOf(num));
                                tvMzqy.setText(String.valueOf(sp.getGt()));
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(JjkfqActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(JjkfqActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            SuccinctProgress.dismiss();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }


    @Event(value = {R.id.iv_back, R.id.qygz_ltqy, R.id.qygz_myqy},
            type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.qygz_ltqy:
                intent.putExtra("lt", lt);
                intent.putExtra("my", my);
                intent.putExtra("jt", jt);
                intent.putExtra("zb", zb);
                intent.putExtra("jz", jz);
                intent.putExtra("gridId", "9999");
                intent.putExtra("isYjfw", "0");
                intent.setClass(JjkfqActivity.this, QylbActivity.class);
                startActivity(intent);
                break;
            case R.id.qygz_myqy:
                intent.setClass(JjkfqActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", "9999");
                intent.putExtra("type", 5);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryListByVIdAndType");
        BaseApplication.getRequestQueue().cancelAll("queryListVillage");
        BaseApplication.getRequestQueue().cancelAll("queryVillageSxgzInfo");

    }
}

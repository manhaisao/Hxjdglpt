package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Azbjry;
import com.xzz.hxjdglpt.model.Menu;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_azbjry_info)
public class AzbjryInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Azbjry azbjry;
    private String gridId;
    @ViewInject(R.id.jsbhz_d_name)
    private TextView mName;
    @ViewInject(R.id.jsbhz_d_cun)
    private TextView mCun;
    @ViewInject(R.id.jsbhz_d_grid)
    private TextView mGrid;
    @ViewInject(R.id.jsbhz_d_birth)
    private TextView mSfzh;
    @ViewInject(R.id.jsbhz_d_address)
    private TextView mAddress;
    @ViewInject(R.id.jsbhz_d_type)
    private TextView mPhone;
    @ViewInject(R.id.jsbhz_d_zm)
    private TextView mYzlb;//原罪类别
    @ViewInject(R.id.jsbhz_d_xqxz)
    private TextView mFxjs;//服刑监所
    @ViewInject(R.id.jsbhz_d_jzlb)
    private TextView mXq;//刑期
    @ViewInject(R.id.jsbhz_d_jztime)
    private TextView mTime;//刑满日期
    @ViewInject(R.id.jsbhz_d_xz)
    private TextView mXz;

    @ViewInject(R.id.jsbhz_info_btn)
    private Button btnGzrz;

    private String isFrom = "";
    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        azbjry = getIntent().getParcelableExtra("azbjry");
        gridId = getIntent().getStringExtra("gridId");
        isFrom = getIntent().getStringExtra("isFrom");
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("安置帮教人员");
    }

    public void initData() {
        if (!("index".equals(isFrom))) {
            mXz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mXz.setTextColor(getResources().getColor(R.color.title_bg));
        }
        loadData();
        getXzCount();
    }

    private void getXzCount() {
        String url = ConstantUtil.BASE_URL + "/zfzzGroup/getCount";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pId", String.valueOf(azbjry.getId()));
        params.put("type", "6");
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
                        mXz.setText(String.valueOf(count));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(AzbjryInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(AzbjryInfo.this, R.string.load_fail);
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

    //打码权限
    private boolean isContain() {
        for (Role r : roles) {
            if ("4257".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(AzbjryInfo.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageByGridId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Village v = (Village) gson.fromJson(result.getJSONObject("data").toString
                                (), Village.class);
//                        mName.setText(azbjry.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
//                        mSfzh.setText(azbjry.getSfzh());
//                        mPhone.setText(azbjry.getPhone());
                        if (isContain()) {
                            mName.setText(azbjry.getName());
                            mSfzh.setText(azbjry.getSfzh());
                            mPhone.setText(azbjry.getPhone());
                        } else {
                            if (!TextUtils.isEmpty(azbjry.getSfzh()) && azbjry.getSfzh().length()
                                    > 8) {
                                String sfzh = azbjry.getSfzh().substring(0, azbjry.getSfzh()
                                        .length() - 8);
                                mSfzh.setText(sfzh + "********");
                            }
                            if (azbjry.getName() != null && azbjry.getName().length() > 1) {
                                String name = azbjry.getName().substring(0, 1);
                                mName.setText(name + "**");
                            }
                            if (!TextUtils.isEmpty(azbjry.getPhone()) && azbjry.getPhone().length
                                    () > 4) {
                                String phone = azbjry.getPhone().substring(0, azbjry.getPhone()
                                        .length() - 4);
                                mPhone.setText(phone + "****");
                            }
                        }
                        mAddress.setText(azbjry.getAddress());
                        mXq.setText(azbjry.getXq());
                        mFxjs.setText(azbjry.getFxjs());
                        mYzlb.setText(azbjry.getYzType());
                        mTime.setText(azbjry.getXmrq());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(AzbjryInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(AzbjryInfo.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
        BaseApplication.getRequestQueue().cancelAll("getCount");
    }

    @Event(value = {R.id.iv_back, R.id.jsbhz_info_btn, R.id.jsbhz_d_xz}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.jsbhz_info_btn:
                intent.setClass(AzbjryInfo.this, ZdryGzrzActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("pId", String.valueOf(azbjry.getId()));
                startActivity(intent);
                break;
            case R.id.jsbhz_d_xz:
                if (!("index".equals(isFrom))) {
                    intent.setClass(AzbjryInfo.this, GkxzListActivity.class);
                    intent.putExtra("type", "6");
                    intent.putExtra("pId", String.valueOf(azbjry.getId()));
                    startActivity(intent);
                }
                break;
        }
    }
}

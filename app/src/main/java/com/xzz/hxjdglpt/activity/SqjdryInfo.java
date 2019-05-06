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
import com.xzz.hxjdglpt.model.Menu;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.Sqjdry;
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
 * 留守儿童基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_sqjdry_info)
public class SqjdryInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Sqjdry sqjdry;
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
    @ViewInject(R.id.jsbhz_d_startTime)
    private TextView mTime;
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
        sqjdry = getIntent().getParcelableExtra("sqjdry");
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        isFrom = getIntent().getStringExtra("isFrom");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("社区戒毒人员");
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
        params.put("pId", String.valueOf(sqjdry.getId()));
        params.put("type", "4");
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
                        DialogUtil.showTipsDialog(SqjdryInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SqjdryInfo.this, R.string.load_fail);
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
        SuccinctProgress.showSuccinctProgress(SqjdryInfo.this, "数据请求中···", SuccinctProgress
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
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
                        if (isContain()) {
                            mName.setText(sqjdry.getName());
                            mSfzh.setText(sqjdry.getSfzh());
                            mPhone.setText(sqjdry.getPhone());
                        } else {
                            if (!TextUtils.isEmpty(sqjdry.getSfzh()) && sqjdry.getSfzh().length() >
                                    8) {
                                String sfzh = sqjdry.getSfzh().substring(0, sqjdry.getSfzh().length
                                        () - 8);
                                mSfzh.setText(sfzh + "********");
                            }
                            if (sqjdry.getName() != null && sqjdry.getName().length() > 1) {
                                String name = sqjdry.getName().substring(0, 1);
                                mName.setText(name + "**");
                            }
                            if (!TextUtils.isEmpty(sqjdry.getPhone()) && sqjdry.getPhone().length()
                                    > 4) {
                                String phone = sqjdry.getPhone().substring(0, sqjdry.getPhone()
                                        .length() - 4);
                                mPhone.setText(phone + "****");
                            }
                        }
                        mAddress.setText(sqjdry.getAddress());
                        mTime.setText(sqjdry.getStartTime());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SqjdryInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SqjdryInfo.this, R.string.load_fail);
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
                intent.setClass(SqjdryInfo.this, ZdryGzrzActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("pId", String.valueOf(sqjdry.getId()));
                startActivity(intent);
                break;
            case R.id.jsbhz_d_xz:
                if (!("index".equals(isFrom))) {
                    intent.setClass(SqjdryInfo.this, GkxzListActivity.class);
                    intent.putExtra("type", "4");
                    intent.putExtra("pId", String.valueOf(sqjdry.getId()));
                    startActivity(intent);
                }
                break;
        }
    }
}

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
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.Zszhjsbhz;
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
@ContentView(R.layout.aty_zszhjsbhz_info)
public class ZszhjsbhzInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Zszhjsbhz zszhjsbhz;
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
    @ViewInject(R.id.jsbhz_d_question)
    private TextView mBy;//病因
    @ViewInject(R.id.jsbhz_d_jhr)
    private TextView mJhr;
    @ViewInject(R.id.jsbhz_d_jhrdh)
    private TextView mJhrDh;
    @ViewInject(R.id.jsbhz_d_gx)
    private TextView mGx;
    @ViewInject(R.id.jsbhz_d_xz)
    private TextView mXz;

    @ViewInject(R.id.jsbhz_info_btn)
    private Button btnGzrz;

    private String[] cjType;

    private String isFrom = "";
    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        zszhjsbhz = getIntent().getParcelableExtra("zszhjsbhz");
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
        tvTitle.setText("肇事肇祸精神病患者");
    }

    public void initData() {
        cjType = getResources().getStringArray(R.array.relationType);
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
        params.put("pId", String.valueOf(zszhjsbhz.getId()));
        params.put("type", "2");
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
                        DialogUtil.showTipsDialog(ZszhjsbhzInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZszhjsbhzInfo.this, R.string.load_fail);
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
        SuccinctProgress.showSuccinctProgress(ZszhjsbhzInfo.this, "数据请求中···", SuccinctProgress
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
                            mName.setText(zszhjsbhz.getName());
                            mSfzh.setText(zszhjsbhz.getSfzh());
                            mPhone.setText(zszhjsbhz.getPhone());
                            mJhr.setText(zszhjsbhz.getJhr());
                            mJhrDh.setText(zszhjsbhz.getJhrPhone());
                        } else {
                            if (!TextUtils.isEmpty(zszhjsbhz.getSfzh()) && zszhjsbhz.getSfzh()
                                    .length() > 8) {
                                String sfzh = zszhjsbhz.getSfzh().substring(0, zszhjsbhz.getSfzh
                                        ().length() - 8);
                                mSfzh.setText(sfzh + "********");
                            }
                            if (zszhjsbhz.getName() != null && zszhjsbhz.getName().length() > 1) {
                                String name = zszhjsbhz.getName().substring(0, 1);
                                mName.setText(name + "**");
                            }
                            if (!TextUtils.isEmpty(zszhjsbhz.getPhone()) && zszhjsbhz.getPhone()
                                    .length() > 4) {
                                String phone = zszhjsbhz.getPhone().substring(0, zszhjsbhz
                                        .getPhone().length() - 4);
                                mPhone.setText(phone + "****");
                            }
                            if (zszhjsbhz.getJhr() != null && zszhjsbhz.getJhr().length() > 1) {
                                String name = zszhjsbhz.getJhr().substring(0, 1);
                                mJhr.setText(name + "**");
                            }
                            if (!TextUtils.isEmpty(zszhjsbhz.getJhrPhone()) && zszhjsbhz
                                    .getJhrPhone().length() > 4) {
                                String phone = zszhjsbhz.getJhrPhone().substring(0, zszhjsbhz
                                        .getJhrPhone().length() - 4);
                                mJhrDh.setText(phone + "****");
                            }
                        }

                        mBy.setText(zszhjsbhz.getByin());
                        mAddress.setText(zszhjsbhz.getAddress());
                        switch (zszhjsbhz.getRelationType()) {
                            case 1:
                                mGx.setText(cjType[0]);
                                break;
                            case 2:
                                mGx.setText(cjType[1]);
                                break;
                            case 3:
                                mGx.setText(cjType[2]);
                                break;
                            case 4:
                                mGx.setText(cjType[3]);
                                break;
                            case 5:
                                mGx.setText(cjType[4]);
                                break;
                            case 6:
                                mGx.setText(cjType[5]);
                                break;
                            case 7:
                                mGx.setText(cjType[6]);
                                break;
                            case 8:
                                mGx.setText(cjType[7]);
                                break;
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZszhjsbhzInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZszhjsbhzInfo.this, R.string.load_fail);
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
                intent.setClass(ZszhjsbhzInfo.this, ZdryGzrzActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("pId", String.valueOf(zszhjsbhz.getId()));
                startActivity(intent);
                break;
            case R.id.jsbhz_d_xz:
                if (!("index".equals(isFrom))) {
                    intent.setClass(ZszhjsbhzInfo.this, GkxzListActivity.class);
                    intent.putExtra("type", "2");
                    intent.putExtra("pId", String.valueOf(zszhjsbhz.getId()));
                    startActivity(intent);
                }
                break;
        }
    }
}

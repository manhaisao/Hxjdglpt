package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Daikai;
import com.xzz.hxjdglpt.model.Jdfzr;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * 企业基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_spdkd_info)
public class SpdkdInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Daikai daikai;
    @ViewInject(R.id.company_mc)
    private TextView mMc;//业主名称
    @ViewInject(R.id.company_danwei)
    private TextView mAddress;
    @ViewInject(R.id.company_kpqk)
    private TextView mLxr;
    @ViewInject(R.id.company_time)
    private TextView mTime;//
    @ViewInject(R.id.company_bz)
    private TextView mBz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        daikai = getIntent().getParcelableExtra("daikai");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("税票代开点");

    }

    public void initData() {
        loadData();
    }

    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(SpdkdInfo.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/daikai/queryLxrList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(daikai.getId()));
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        Gson gson = new Gson();
                        List<Jdfzr> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jdfzr>>() {
                                }.getType());
                        if (daikai != null) {
                            mMc.setText(daikai.getName());
                            mAddress.setText(daikai.getAddress());
                            mTime.setText(daikai.getTime());
                            mBz.setText(daikai.getBz());
                        }
                        if (objs.size() >= 2) {
                            mLxr.setText(objs.get(0).getName()+"\t"+objs.get(0).getPhone()+"\n"+objs.get(1).getName()+"\t"+objs.get(1).getPhone());
                        } else if (objs.size() == 1) {
                            mLxr.setText(objs.get(0).getName()+"\t"+objs.get(0).getPhone());
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SpdkdInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SpdkdInfo.this, R.string.load_fail);
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
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

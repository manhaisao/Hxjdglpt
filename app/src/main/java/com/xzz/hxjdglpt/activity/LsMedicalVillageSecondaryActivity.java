package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.ls.LsGrid;
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
 * 网格险医疗明细
 */
@ContentView(R.layout.activity_ls_medical_village_secondary)
public class LsMedicalVillageSecondaryActivity extends BaseActivity {

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.tv_medical_num)
    private TextView tvMedicalNum;
    @ViewInject(R.id.tv_wgzrr)
    private TextView tvWgzrr;
    @ViewInject(R.id.tv_wgzrr_phone)
    private TextView tvWgzrrPhone;

    private String gId = "";
    private User user;
    private Grid grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        gId = getIntent().getStringExtra("gridId");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        loadGrid(gId);
        getGridInfo();
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
        }
    }

    private void loadGrid(final String gId) {
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/grid/queryGridById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gId);
        VolleyRequest.RequestPost(this, url, "queryGridById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    LogUtil.i(result.toString());
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        grid = (Grid) gson.fromJson(result.getJSONObject("data").toString(), Grid
                                .class);
                        if (grid.getZm() != null && !TextUtils.isEmpty(grid.getZm())) {
                            hXtitle.setText(grid.getZm());
                        } else {
                            hXtitle.setText(gId);
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsMedicalVillageSecondaryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsMedicalVillageSecondaryActivity.this, R.string.load_fail);
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

    /**
     * 获取网格信息(注释：未使用grid)
     */
    private void getGridInfo(){
        SuccinctProgress.showSuccinctProgress(LsMedicalVillageSecondaryActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/selectbyid?gridid="+gId+"&type=2";
        VolleyRequest.RequestGet(this, url, "selectbyid", new VolleyListenerInterface
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
                        Gson gson = new Gson();
                        LsGrid lsGrid = (LsGrid) gson.fromJson(result.getJSONObject("data").toString
                                (), LsGrid.class);
                        tvMedicalNum.setText(lsGrid.getJbylbx());
                        tvWgzrr.setText(lsGrid.getGridzrr());
                        tvWgzrrPhone.setText(lsGrid.getGridzrrphone());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsMedicalVillageSecondaryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsMedicalVillageSecondaryActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryGridById");
        BaseApplication.getRequestQueue().cancelAll("selectbyid");
    }
}

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
import com.xzz.hxjdglpt.model.Plot;
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
 * 低保人员基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_plot_info)
public class PlotInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Plot plot;
    private String gridId;
    @ViewInject(R.id.plot_name)
    private TextView mName;
    @ViewInject(R.id.plot_cun)
    private TextView mCun;
    @ViewInject(R.id.plot_grid)
    private TextView mGrid;

    @ViewInject(R.id.plot_area)
    private TextView mArea;
    @ViewInject(R.id.plot_dnum)
    private TextView mDnum;
    @ViewInject(R.id.plot_zhs)
    private TextView mZhs;
    @ViewInject(R.id.plot_wgrs)
    private TextView mWgrs;
    @ViewInject(R.id.plot_bjrs)
    private TextView mBjrs;
    @ViewInject(R.id.plot_wygs)
    private TextView mWygs;
    @ViewInject(R.id.plot_wyphone)
    private TextView mWyPhone;
    @ViewInject(R.id.plot_zjl)
    private TextView mZjl;
    @ViewInject(R.id.plot_zrr)
    private TextView mZrr;
    @ViewInject(R.id.plot_zrrdh)
    private TextView mZrrdh;
    @ViewInject(R.id.plot_d_bz)
    private TextView mBz;
    @ViewInject(R.id.plot_zjldh)
    private TextView mZjldh;
    @ViewInject(R.id.plot_wyfzr)
    private TextView mWyfzr;
    @ViewInject(R.id.plot_wyfzrdh)
    private TextView mWyfzedh;
    @ViewInject(R.id.plot_ba)
    private TextView mBaNum;


    protected List<String> sourceImageList = new ArrayList<>();

    @ViewInject(R.id.sxgz_gzdt_btn)
    private Button gzdtBtn;

    @ViewInject(R.id.plot_lt_btn)
    private Button ltBtn;

    private String isFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        plot = getIntent().getParcelableExtra("plot");
        gridId = getIntent().getStringExtra("gridId");
        isFrom = getIntent().getStringExtra("isFrom");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(R.string.plot_info_);
        if (isContain()) {
            ltBtn.setVisibility(View.VISIBLE);
        } else {
            ltBtn.setVisibility(View.GONE);
        }
    }

    public void initData() {
        loadData();
    }

    private boolean isContain() {
        if (!TextUtils.isEmpty(plot.getZrrid())) {
            String[] ids = plot.getZrrid().split(",");
            for (String id : ids) {
                if (user.getUserId().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(PlotInfo.this, "数据请求中···", SuccinctProgress
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
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Village v = (Village) gson.fromJson(result.getJSONObject("data").toString
                                (), Village.class);
                        mName.setText(plot.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);

                        mArea.setText(plot.getArea());
                        mDnum.setText(String.valueOf(plot.getpNum()));
                        mZhs.setText(String.valueOf(plot.gethNum()));
                        mWgrs.setText(String.valueOf(plot.getwNum()));
                        mBjrs.setText(String.valueOf(plot.getbNum()));
                        mWygs.setText(plot.getWygs());
                        mWyPhone.setText(plot.getWyphone());
                        mZjl.setText(plot.getWyzjl());
                        mZrr.setText(plot.getvMan());
                        mZrrdh.setText(plot.getvManPhone());
                        mBz.setText(plot.getBz());
                        mZjldh.setText(plot.getWyzjlphone());
                        mWyfzr.setText(plot.getWyfzr());
                        mWyfzedh.setText(plot.getWyfzrphone());
                        mBaNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        LogUtil.i("banum" + plot.getBaNum());
                        mBaNum.setText(String.valueOf(plot.getBaNum()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PlotInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotInfo.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back, R.id.sxgz_gzdt_btn, R.id.plot_ba, R.id.plot_lt_btn}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.plot_lt_btn:
                Intent intent2 = new Intent();
                intent2.setClass(PlotInfo.this, PlotSQUserActivity.class);
                intent2.putExtra("plotid", plot.getId());
                startActivity(intent2);
                break;
            case R.id.sxgz_gzdt_btn:
                Intent intent = new Intent();
                intent.setClass(PlotInfo.this, PlotGzdtActivity.class);
                intent.putExtra("plotId", plot.getId());
                startActivity(intent);
                break;
            case R.id.plot_ba:
                Intent intent1 = new Intent();
                intent1.putExtra("pid", plot.getId());
                intent1.putExtra("plotName", plot.getName());
                intent1.putExtra("isFrom", isFrom);
                intent1.setClass(PlotInfo.this, BaryListActivity.class);
                startActivity(intent1);
                break;
        }
    }
}

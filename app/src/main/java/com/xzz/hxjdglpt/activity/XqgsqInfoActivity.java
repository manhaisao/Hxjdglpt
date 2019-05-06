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
@ContentView(R.layout.aty_xqgsq_info)
public class XqgsqInfoActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Plot plot;
    private String gridId;
    @ViewInject(R.id.plot_name)
    private TextView mName;
//    @ViewInject(R.id.plot_cun)
//    private TextView mCun;
//    @ViewInject(R.id.plot_grid)
//    private TextView mGrid;

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


    @ViewInject(R.id.plot_d_bz)
    private TextView mBz;

    @ViewInject(R.id.plot_wyfzr)
    private TextView mWyfzr;
    @ViewInject(R.id.plot_wyfzrdh)
    private TextView mWyfzedh;
    @ViewInject(R.id.plot_ba)
    private TextView mBaNum;

    @ViewInject(R.id.plot_jzmj)
    private TextView plot_jzmj;
    @ViewInject(R.id.plot_dys)
    private TextView plot_dys;
    @ViewInject(R.id.plot_wgz)
    private TextView plot_wgz;
    @ViewInject(R.id.plot_lxdh)
    private TextView plot_lxdh;
    @ViewInject(R.id.plot_jdwgy)
    private TextView plot_jdwgy;
    @ViewInject(R.id.plot_jdwgy_phone)
    private TextView plot_jdwgy_phone;
    @ViewInject(R.id.plot_cjwgy)
    private TextView plot_cjwgy;
    @ViewInject(R.id.plot_cjwgy_phone)
    private TextView plot_cjwgy_phone;
    @ViewInject(R.id.plot_wygs)
    private TextView plot_wygs;
    @ViewInject(R.id.plot_wyfr)
    private TextView plot_wyfr;
    @ViewInject(R.id.plot_wyphone)
    private TextView plot_wyphone;
    @ViewInject(R.id.plot_wyfzr)
    private TextView plot_wyfzr;
    @ViewInject(R.id.plot_wyfzrdh)
    private TextView plot_wyfzrdh;
    @ViewInject(R.id.plot_wgrs)
    private TextView plot_wgrs;
    @ViewInject(R.id.plot_ba)
    private TextView plot_ba;
    @ViewInject(R.id.plot_bjrs)
    private TextView plot_bjrs;
    @ViewInject(R.id.plot_jyh)
    private TextView plot_jyh;
    @ViewInject(R.id.plot_d_bz)
    private TextView plot_d_bz;


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
        mName.setText(plot.getName());//小区名称
        plot_jzmj.setText(plot.getJzmj());//建筑面积
        mArea.setText(plot.getArea());//面积
        mDnum.setText(String.valueOf(plot.getpNum()));//房子栋数
        plot_dys.setText(String.valueOf(plot.getDyNum()));//单元数
        mZhs.setText(String.valueOf(plot.gethNum()));//户数
        plot_wgz.setText(plot.getWgz());
        plot_lxdh.setText(plot.getWgzphone());
        plot_jdwgy.setText(plot.getJdwgy() != null ? plot.getJdwgy() : "");
        plot_jdwgy_phone.setText(plot.getJdwagphone());
        plot_cjwgy.setText(plot.getCjwgy());
        plot_cjwgy_phone.setText(plot.getCjwgyphone());
        plot_wygs.setText(plot.getWygs());//物业公司
        plot_wyfr.setText(plot.getWyzjl());
        plot_wyphone.setText(plot.getWyphone());
        plot_wyfzr.setText(plot.getWyfzr());
        plot_wyfzrdh.setText(plot.getWyfzrphone());
        plot_bjrs.setText(String.valueOf(plot.getbNum()));//保洁员人数
        plot_wgrs.setText(String.valueOf(plot.getwNum()));//物管人员数量
        plot_d_bz.setText(plot.getBz());//备注
        plot_ba.setText(String.valueOf(plot.getBaNum()));
        plot_jyh.setText(String.valueOf(plot.getJyhNum()));
        plot_ba.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        plot_jyh.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    }

    public void initData() {
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
        SuccinctProgress.showSuccinctProgress(XqgsqInfoActivity.this, "数据请求中···", SuccinctProgress
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
//                        mCun.setText(v.getName());
//                        mGrid.setText(gridId);

                        mArea.setText(plot.getArea());
                        mDnum.setText(String.valueOf(plot.getpNum()));
                        mZhs.setText(String.valueOf(plot.gethNum()));
                        mWgrs.setText(String.valueOf(plot.getwNum()));
                        mBjrs.setText(String.valueOf(plot.getbNum()));
                        mWygs.setText(plot.getWygs());
                        mWyPhone.setText(plot.getWyphone());
                        mBz.setText(plot.getBz());
                        mWyfzr.setText(plot.getWyfzr());
                        mWyfzedh.setText(plot.getWyfzrphone());
                        mBaNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        LogUtil.i("banum" + plot.getBaNum());
                        mBaNum.setText(String.valueOf(plot.getBaNum()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XqgsqInfoActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XqgsqInfoActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back, R.id.sxgz_gzdt_btn, R.id.plot_ba, R.id.plot_lt_btn,R.id.plot_jyh}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.plot_lt_btn:
                Intent intent2 = new Intent();
                intent2.setClass(XqgsqInfoActivity.this, PlotSQUserActivity.class);
                intent2.putExtra("plotid", plot.getId());
                startActivity(intent2);
                break;
            case R.id.sxgz_gzdt_btn:
                Intent intent = new Intent();
                intent.setClass(XqgsqInfoActivity.this, PlotGzdtActivity.class);
                intent.putExtra("plotId", plot.getId());
                startActivity(intent);
                break;
            case R.id.plot_ba:
                Intent intent1 = new Intent();
                intent1.putExtra("pid", plot.getId());
                intent1.putExtra("plotName", plot.getName());
                intent1.putExtra("isFrom", isFrom);
                intent1.setClass(XqgsqInfoActivity.this, BaryListActivity.class);
                startActivity(intent1);
                break;
            case R.id.plot_jyh:
                Intent intent3 = new Intent();
                intent3.putExtra("pid", plot.getId());
                intent3.setClass(XqgsqInfoActivity.this, JyhListActivity.class);
                startActivity(intent3);
                break;
        }
    }
}

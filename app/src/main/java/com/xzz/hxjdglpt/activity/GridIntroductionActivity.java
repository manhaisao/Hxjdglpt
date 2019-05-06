package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.GridTj;
import com.xzz.hxjdglpt.model.Role;
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
import java.util.Map;

/**
 * 网格简介
 * Created by dbz on 2017/6/6.
 */
@ContentView(R.layout.aty_grid_introduction)
public class GridIntroductionActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.g_intro_grid)
    private TextView tvGrid;
    @ViewInject(R.id.g_intro_area)
    private TextView tvArea;//面积
    @ViewInject(R.id.g_intro_hnum)
    private TextView tvHnum;//户数
    @ViewInject(R.id.g_intro_rknum)
    private TextView tvRknum;//人口
    @ViewInject(R.id.g_intro_dynum)
    private TextView tvDy;//党员
    @ViewInject(R.id.g_intro_csdbnum)
    private TextView tvCsdb;//城市低保
    @ViewInject(R.id.g_intro_ncdbnum)
    private TextView tvNcdb;//农村低保
    @ViewInject(R.id.g_intro_cjrnum)
    private TextView tvCjr;//残疾人
    @ViewInject(R.id.g_intro_zdrq)
    private TextView tvZdrq;//信访诉求人员
    @ViewInject(R.id.g_intro_lsetnum)
    private TextView tvLset;//留守儿童
    @ViewInject(R.id.g_intro_ylfnnum)
    private TextView tvYlfn;//育龄妇女
    @ViewInject(R.id.g_intro_jlfznum)
    private TextView tvJlfz;//奖励扶助
    @ViewInject(R.id.g_intro_ldrknum)
    private TextView tvLdrk;//流动人口
    @ViewInject(R.id.g_intro_jsknnum)
    private TextView tvJskn;//计生困难
    @ViewInject(R.id.g_intro_dsznnum)
    private TextView tvDszn;//独生子女
    //    @ViewInject(R.id.g_intro_sdjtnum)
//    private TextView tvSdjt;//失独家庭
    @ViewInject(R.id.g_intro_qynum)
    private TextView tvQy;//企业
    @ViewInject(R.id.g_intro_gtnum)
    private TextView tvGt;//个体
//    @ViewInject(R.id.g_intro_zrr)
//    private TextView tvZrr;
//    @ViewInject(R.id.g_intro_hjghr)
//    private TextView tvHjghr;//
//    @ViewInject(R.id.g_intro_ywxcy)
//    private TextView tvYwxcy;//义务宣传员

    @ViewInject(R.id.g_intro_xqnum)
    private TextView tvXqnum;//小区

    @ViewInject(R.id.g_intro_ykbynum)
    private TextView tvYkby;//特困(五保)供养

    @ViewInject(R.id.g_intro_zljnum)
    private TextView tvZlj;//80岁以上尊老金

    @ViewInject(R.id.g_intro_yfdx)
    private TextView tvYfdx;//优抚对象

    @ViewInject(R.id.g_intro_jsbnum)
    private TextView tvJsb;//肇事肇祸精神病患者
    @ViewInject(R.id.g_intro_zdqsnnum)
    private TextView tvZdqsn;//重点青少年
    @ViewInject(R.id.g_intro_sqjdnum)
    private TextView tvSqjd;//社区戒毒人员
    @ViewInject(R.id.g_intro_mdjfdjynnum)
    private TextView tvMdjfdjy;//矛盾纠纷调解员
    @ViewInject(R.id.g_intro_mdjfxxynum)
    private TextView tvMdjfxxy;//矛盾纠纷信息员
    @ViewInject(R.id.g_intro_xjrynnum)
    private TextView tvXjry;//邪教人员
    @ViewInject(R.id.g_intro_fzxcynum)
    private TextView tvFzxcy;//法制宣传员
    @ViewInject(R.id.g_intro_pfzyznnum)
    private TextView tvPfzyzn;//普法志愿者
    @ViewInject(R.id.g_intro_azbjrynum)
    private TextView tvAzbj;//安置帮教人员
    @ViewInject(R.id.g_intro_sqfxrynnum)
    private TextView tvSqfz;//社区服刑人员
    @ViewInject(R.id.g_intro_flgwnum)
    private TextView tvFlgw;//法律顾问

    @ViewInject(R.id.g_intro_jzgdnum)
    private TextView tvJzgd;//建筑工地
    @ViewInject(R.id.g_intro_tzsbnum)
    private TextView tvTzsb;//特种设备
    @ViewInject(R.id.g_intro_wxhxpnum)
    private TextView tvWxp;//危险化学品
    @ViewInject(R.id.g_intro_cpynum)
    private TextView tvCpy;//成品油

    @ViewInject(R.id.g_intro_wgz)
    private TextView tvWgz;//网格长
    @ViewInject(R.id.g_intro_wgzphone)
    private TextView tvWgzphone;//网格长电话
    @ViewInject(R.id.g_intro_sqgj)
    private TextView tvSqgj;//社区干警
    @ViewInject(R.id.g_intro_sqgjphone)
    private TextView tvSqgjphone;//干警电话

    private User user;

    private String gridId;

    private GridTj grid;

    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        gridId = getIntent().getStringExtra("gridId");
        roles = application.getRolesList();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void initView() {
        if (roles != null && roles.size() > 0) {
            tvJsb.setTextColor(getResources().getColor(R.color.title_bg));
            tvZdqsn.setTextColor(getResources().getColor(R.color.title_bg));
            tvSqjd.setTextColor(getResources().getColor(R.color.title_bg));
            tvXjry.setTextColor(getResources().getColor(R.color.title_bg));
            tvAzbj.setTextColor(getResources().getColor(R.color.title_bg));
            tvSqfz.setTextColor(getResources().getColor(R.color.title_bg));
        }
    }

    public void initData() {
        loadGrid(gridId);
    }

    private void loadGrid(String gId) {
        SuccinctProgress.showSuccinctProgress(GridIntroductionActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/grid/tjGridById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gId);
        VolleyRequest.RequestPost(this, url, "grid_queryGrid", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    LogUtil.i(result.toString());
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        grid = (GridTj) gson.fromJson(result.getJSONObject("data").toString(),
                                GridTj.class);
                        if (grid.getZm() != null && !TextUtils.isEmpty(grid.getZm())) {
                            tvTitle.setText(grid.getZm());
                        } else {
                            tvTitle.setText(gridId);
                        }
                        appendContent();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridIntroductionActivity.this, R.string.load_fail);
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

    private void appendContent() {
        tvGrid.setText(gridId);
        tvArea.setText(grid.getArea() == null ? "0" : grid.getArea());
        tvHnum.setText(grid.gethNum() == null ? "0" : String.valueOf(grid.gethNum()));
        tvRknum.setText(grid.getRkNum() == null ? "0" : String.valueOf(grid.getRkNum()));
        tvDy.setText(String.valueOf(grid.getDynum()));
        tvCsdb.setText(String.valueOf(grid.getCsdbnum()));
        tvNcdb.setText(String.valueOf(grid.getNcdbnum()));
        tvCjr.setText(String.valueOf(grid.getCjnum()));
        tvZdrq.setText(String.valueOf(grid.getZdrq()));
        tvYlfn.setText(String.valueOf(grid.getYlfnNum()));
        tvJlfz.setText(String.valueOf(grid.getJlfzNum()));
        tvLdrk.setText(String.valueOf(grid.getLdrkNum()));
        tvJskn.setText(String.valueOf(grid.getJsknNum()));
        tvDszn.setText(String.valueOf(grid.getDszn()));
        tvQy.setText(String.valueOf(grid.getQynum()));
        tvGt.setText(String.valueOf(grid.getGtnum()));
        tvXqnum.setText(String.valueOf(grid.getPlotNum()));
        tvLset.setText(String.valueOf(grid.getLset()));

        tvYkby.setText(String.valueOf(grid.getTkwb()));//特困(五保)供养
        tvZlj.setText(String.valueOf(grid.getZlj()));//80岁以上尊老金
        tvYfdx.setText(String.valueOf(grid.getYfdx()));//优抚对象
        tvJsb.setText(String.valueOf(grid.getJsb()));//肇事肇祸精神病患者
        tvZdqsn.setText(String.valueOf(grid.getZdrq()));//重点青少年
        tvSqjd.setText(String.valueOf(grid.getSqjd()));//社区戒毒人员
        tvMdjfdjy.setText(String.valueOf(grid.getJftjy()));//矛盾纠纷调解员
        tvMdjfxxy.setText(String.valueOf(grid.getJfxxy()));//矛盾纠纷信息员
        tvXjry.setText(String.valueOf(grid.getXj()));//邪教人员
        tvFzxcy.setText(String.valueOf(grid.getFzxcy()));//法制宣传员
        tvPfzyzn.setText(String.valueOf(grid.getPfzyz()));//普法志愿者
        tvAzbj.setText(String.valueOf(grid.getAzbj()));//安置帮教人员
        tvSqfz.setText(String.valueOf(grid.getSqfsMan()));//社区服刑人员
        tvFlgw.setText(String.valueOf(grid.getFlgw()));//法律顾问
        tvJzgd.setText(String.valueOf(grid.getJzgd()));//建筑工地
        tvTzsb.setText(String.valueOf(grid.getTzsb()));//特种设备
        tvWxp.setText(String.valueOf(grid.getWxhxp()));//危险化学品
        tvCpy.setText(String.valueOf(grid.getCpy()));//成品油
        tvWgz.setText(TextUtils.isEmpty(grid.getWgz()) ? "" : grid.getWgz());//网格长
        tvWgzphone.setText(TextUtils.isEmpty(grid.getWgzPhone()) ? "" : grid.getWgzPhone());//网格长电话
        tvSqgj.setText(TextUtils.isEmpty(grid.getSqgj()) ? "" : grid.getSqgj());//社区干警
        tvSqgjphone.setText(TextUtils.isEmpty(grid.getSqgjPhone()) ? "" : grid.getSqgjPhone());//干警电话
    }

    @Event(value = {R.id.iv_back, R.id.g_intro_dynum, R.id.g_intro_cjrnum, R.id.g_intro_ylfnnum,
            R.id.g_intro_qynum, R.id.g_intro_gtnum, R.id.g_intro_xqnum, R.id.g_intro_ykbynum, R
            .id.g_intro_zljnum, R.id.g_intro_lsetnum, R.id.g_intro_yfdx, R.id.g_intro_jlfznum, R
            .id.g_intro_ldrknum, R.id.g_intro_jsknnum, R.id.g_intro_dsznnum, R.id
            .g_intro_mdjfdjynnum, R.id.g_intro_mdjfxxynum, R.id.g_intro_flgwnum, R.id
            .g_intro_fzxcynum, R.id.g_intro_pfzyznnum, R.id.g_intro_jzgdnum, R.id
            .g_intro_tzsbnum, R.id.g_intro_wxhxpnum, R.id.g_intro_cpynum, R.id.g_intro_ncdbnum, R
            .id.g_intro_csdbnum, R.id.g_intro_zdrq, R.id.g_intro_jsbnum, R.id.g_intro_zdqsnnum, R
            .id.g_intro_sqjdnum, R.id.g_intro_xjrynnum, R.id.g_intro_azbjrynum, R.id
            .g_intro_sqfxrynnum}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.g_intro_jsbnum:
                if (roles != null && roles.size() > 0) {
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 57);
                    intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.g_intro_zdqsnnum:
                if (roles != null && roles.size() > 0) {
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 58);
                    intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.g_intro_sqjdnum:
                if (roles != null && roles.size() > 0) {
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 59);
                    intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.g_intro_xjrynnum:
                if (roles != null && roles.size() > 0) {
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 60);
                    intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.g_intro_azbjrynum:
                if (roles != null && roles.size() > 0) {
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 62);
                    intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.g_intro_sqfxrynnum:
                if (roles != null && roles.size() > 0) {
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 61);
                    intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.g_intro_zdrq:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 56);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.g_intro_cpynum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 49);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_wxhxpnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 48);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_tzsbnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 47);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_jzgdnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 46);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_flgwnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 25);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_fzxcynum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 23);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_pfzyznnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 24);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_dsznnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 26);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_mdjfdjynnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 21);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_mdjfxxynum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 22);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_jsknnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 32);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_ldrknum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 31);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_yfdx:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 14);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_jlfznum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 55);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_lsetnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 13);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_zljnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 16);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_ykbynum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 15);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_dynum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 0);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_csdbnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 1);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_ncdbnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 2);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_cjrnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 6);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_ylfnnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 3);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_qynum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 4);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_gtnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 5);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.g_intro_xqnum:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 7);
                intent.setClass(GridIntroductionActivity.this, ListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("grid_queryGrid");
    }
}

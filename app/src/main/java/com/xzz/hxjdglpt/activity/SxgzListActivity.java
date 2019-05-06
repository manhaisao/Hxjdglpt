package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.VillageAdapter;
import com.xzz.hxjdglpt.customview.MyListView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.BusinessJob;
import com.xzz.hxjdglpt.model.CityManage;
import com.xzz.hxjdglpt.model.Djzz;
import com.xzz.hxjdglpt.model.FamilyPlanning;
import com.xzz.hxjdglpt.model.Fwzw;
import com.xzz.hxjdglpt.model.Minzheng;
import com.xzz.hxjdglpt.model.PartyBuilding;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.SafeProduce;
import com.xzz.hxjdglpt.model.Sfxz;
import com.xzz.hxjdglpt.model.Sxgz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.Xfwd;
import com.xzz.hxjdglpt.model.Xshs;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.StringUtil;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbz on 2017/6/5.
 */
@ContentView(R.layout.aty_sxgz_list)
public class SxgzListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private User user;
    private BaseApplication application;
    @ViewInject(R.id.clist)
    private TextView clist;
    @ViewInject(R.id.sxgz_zzgl_ll)
    private LinearLayout sxgz_zzgl_ll;

    private List<Village> villages = new ArrayList<>();

    @ViewInject(R.id.sxgz_list)
    private MyListView listview;

    @ViewInject(R.id.sxgz_content)
    private TextView mContent;

    private VillageAdapter villageAdapter;

    private int type = 0;

    @ViewInject(R.id.sxgz_btn)
    private Button allBtn;

    @ViewInject(R.id.sxgz_gzdt_btn)
    private Button gzdtBtn;

    private List<Role> roles;

    @ViewInject(R.id.sxgz_list_dj)
    private LinearLayout lin1;
    @ViewInject(R.id.sxgz_list_mz)
    private LinearLayout lin2;
    @ViewInject(R.id.sxgz_list_qyfw)
    private LinearLayout lin3;
    @ViewInject(R.id.sxgz_list_xshs)
    private LinearLayout lin4;
    @ViewInject(R.id.sxgz_list_aqsc)
    private LinearLayout lin5;
    @ViewInject(R.id.sxgz_list_xfwd)
    private LinearLayout lin6;
    @ViewInject(R.id.sxgz_list_jhsy)
    private LinearLayout lin7;
    @ViewInject(R.id.sxgz_list_zfzz)
    private LinearLayout lin8;
    @ViewInject(R.id.sxgz_list_cxgl)
    private LinearLayout lin9;
    @ViewInject(R.id.sxgz_list_fwzw)
    private LinearLayout lin10;
    @ViewInject(R.id.sxgz_list_xqgsq)
    private LinearLayout lin11;

    //党建
    @ViewInject(R.id.dj_dxzs)
    private TextView tvDxzs;
    @ViewInject(R.id.dj_dys)
    private TextView tvDys;
    @ViewInject(R.id.dj_kndys)
    private TextView tvKnDys;
    @ViewInject(R.id.dj_gggs)
    private TextView tvGggs;
    @ViewInject(R.id.dj_yxdy)
    private TextView tvYxdy;
    @ViewInject(R.id.dj_hrb)
    private TextView tvHrb;
    @ViewInject(R.id.dj_dzzs)
    private TextView tvDzz;
    @ViewInject(R.id.dj_lrdys)
    private TextView tvLrdy;

    private int cjdzz = 0, zddzz = 0, lxdzz = 0, lsdzz = 0;

    //民政
    @ViewInject(R.id.mz_ncdb)
    private TextView tvNcdb;//农村低保
    @ViewInject(R.id.mz_csdb)
    private TextView tvCsdb;//城市低保
    @ViewInject(R.id.mz_cjr)
    private TextView tvCjr;//残疾人
    @ViewInject(R.id.mz_lset)
    private TextView tvLsrt;//留守儿童
    @ViewInject(R.id.mz_yfdx)
    private TextView tvYfdx;//优抚对象
    @ViewInject(R.id.mz_tkgy)
    private TextView tvTkgy;//特困(五保)供养
    @ViewInject(R.id.mz_zlj)
    private TextView tvZlj;//80岁以上尊老金
    //企业服务
    @ViewInject(R.id.qygz_ltqy)
    private TextView tvLtqy;
    @ViewInject(R.id.qygz_myqy)
    private TextView tvMzqy;
    @ViewInject(R.id.qygz_jtqy)
    private TextView tvJtqy;
    //协税护税
    @ViewInject(R.id.xshs_qy)
    private TextView tvQy;
    @ViewInject(R.id.xshs_gt)
    private TextView tvGt;
    @ViewInject(R.id.xshs_sbdkd)
    private TextView tvSpdkd;//河下街道税票代开点
    private int lt = 0, my = 0, jt = 0, zb = 0, jz = 0;
    //应急服务
    @ViewInject(R.id.aqsc_ltqy)
    private TextView tvLt;
    @ViewInject(R.id.aqsc_myqy)
    private TextView tvMy;
    @ViewInject(R.id.aqsc_jyyhbzs)
    private TextView tvYhbz;
    @ViewInject(R.id.aqsc_rpzf)
    private TextView tvRpzf;
    @ViewInject(R.id.aqsc_dzpjgzf)
    private TextView tvDzjgf;
    @ViewInject(R.id.aqsc_jzgd)
    private TextView tvJzgd;
    @ViewInject(R.id.aqsc_tzsb)
    private TextView tvTzsb;
    @ViewInject(R.id.aqsc_wxhxp)
    private TextView tvWxhxp;
    @ViewInject(R.id.aqsc_cpy)
    private TextView tvCpy;
    @ViewInject(R.id.aqsc_xfaq)
    private TextView tvXfaq;
    @ViewInject(R.id.aqsc_yjdw)
    private TextView tvYjdw;

    //和谐社区
    @ViewInject(R.id.xfwd_zdrq)
    private TextView tvZdrq;//信访诉求人员
    //卫生健康
    @ViewInject(R.id.jhsy_ylfns)
    private TextView tvYlfn;//育龄妇女
    @ViewInject(R.id.jhsy_dszn)
    private TextView tvDszn;//独生子女
    @ViewInject(R.id.jhsy_jlfz)
    private TextView tvJlfz;//省扶
    @ViewInject(R.id.jhsy_jlfz_sf)
    private TextView tvJlfz_sf;//市扶
    @ViewInject(R.id.jhsy_jlfz_sd)
    private TextView tvJlfz_sd;//失独
    @ViewInject(R.id.jhsy_jlfz_sc)
    private TextView tvJlfz_sc;//伤残
    @ViewInject(R.id.jhsy_ldrk)
    private TextView tvLdrk;//流动人口
    @ViewInject(R.id.jhsy_jskn)
    private TextView tvJskn;//计生困难
    @ViewInject(R.id.jhsy_yy)
    private TextView tvYy;//
    //政法综治
    @ViewInject(R.id.sfxz_jsb)
    private TextView tvJsb;//肇事肇祸精神病患者
    @ViewInject(R.id.sfxz_zdqsn)
    private TextView tvZdqsn;//重点青少年
    @ViewInject(R.id.sfxz_sqfx)
    private TextView tvSqfx;//社区服刑
    @ViewInject(R.id.sfxz_sqjdry)
    private TextView tvSqjd;//社区戒毒人员
    @ViewInject(R.id.sfxz_xjry)
    private TextView tvXjry;//邪教人员
    @ViewInject(R.id.sfxz_azbj)
    private TextView tvAzbj;//安置帮教人员
    @ViewInject(R.id.sfxz_djy)
    private TextView tvDjy;//矛盾纠纷调解员
    @ViewInject(R.id.sfxz_xxy)
    private TextView tvXxy;//矛盾纠纷信息员
    @ViewInject(R.id.sfxz_xcy)
    private TextView tvXcy;//法制宣传员
    @ViewInject(R.id.sfxz_zyz)
    private TextView tvZyz;//普法志愿者
    @ViewInject(R.id.sfxz_flgw)
    private TextView tvFlgw;//法律顾问
    @ViewInject(R.id.sfxz_djy_g)
    private TextView tvDjy_g;//矛盾纠纷调解员
    @ViewInject(R.id.sfxz_xxy_g)
    private TextView tvXxy_g;//矛盾纠纷信息员
    @ViewInject(R.id.sfxz_xcy_g)
    private TextView tvXcy_g;//法制宣传员
    @ViewInject(R.id.sfxz_zyz_g)
    private TextView tvZyz_g;//普法志愿者
    @ViewInject(R.id.sfxz_flgw_g)
    private TextView tvFlgw_g;//法律顾问

    //城市长效管理
    @ViewInject(R.id.cscxgl_area)
    private TextView tvArea;//面积
    @ViewInject(R.id.cscxgl_hu)
    private TextView tvHu;//户数
    @ViewInject(R.id.cscxgl_dlts)
    private TextView tvDlts;//道路条数
    @ViewInject(R.id.cscxgl_htts)
    private TextView tvHtts;//河塘条数
    @ViewInject(R.id.cscxgl_ljqyy)
    private TextView tvLjqyy;//垃圾清运员
    @ViewInject(R.id.cscxgl_ldbjy)
    private TextView tvLdbjy;//流动保洁员
    @ViewInject(R.id.cscxgl_ljc)
    private TextView tvLjc;//垃圾池
    @ViewInject(R.id.cscxgl_ljt)
    private TextView tvLjt;//垃圾桶
    @ViewInject(R.id.cscxgl_ljrcl)
    private TextView tvRcl;//垃圾日产量
    @ViewInject(R.id.cscxgl_ljysd)
    private TextView tvYsd;//垃圾运输点

    //防伪治违
    @ViewInject(R.id.fwzw_jdphone)
    private TextView tvJdPhone;

    //小区归社区
    @ViewInject(R.id.xqgsq_ls)
    private TextView tvXqsl;

    @ViewInject(R.id.sxgz_content_lay)
    private RelativeLayout mLay;

    @ViewInject(R.id.dj_title)
    private LinearLayout djLay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();

        initView();
        initData();
    }

    public void initView() {

    }

    public void initData() {
        villageAdapter = new VillageAdapter(this, villages);
        listview.setAdapter(villageAdapter);
        listview.setOnItemClickListener(this);
        request();
        type = getIntent().getIntExtra("type", 1);
        StringBuffer sb = new StringBuffer();
        getSxgzById(type);

        switch (type) {
            case 12:
                tvTitle.setText(R.string.xqgsq);
                sb.append(getString(R.string.xqgsq));
                loadXqgsqData();
                lin11.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvTitle.setText(R.string.cscxgl);
                sb.append(getString(R.string.cscxgl));
                loadCscxData();
                lin9.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvTitle.setText(R.string.aqsc);
                sb.append(getString(R.string.aqsc));
                loadAqscData();
                lin5.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvTitle.setText(R.string.jhsy);
                sb.append(getString(R.string.jhsy));
                loadJhsyData();
                lin7.setVisibility(View.VISIBLE);
                break;
            case 4:
                clist.setText("列表");
                tvTitle.setText(R.string.qygz);
                sb.append(getString(R.string.qygz));
//                loadQygzData();
//                lin3.setVisibility(View.VISIBLE);
                loadXshsData();
                lin4.setVisibility(View.VISIBLE);
                break;
            case 5:
                tvTitle.setText(R.string.mz);
                sb.append(getString(R.string.mz));
                loadMzData();
                lin2.setVisibility(View.VISIBLE);
                break;
            case 6:
                tvTitle.setText(R.string.sfxz);
                sb.append(getString(R.string.sfxz));
                loadZfzzData();
                lin8.setVisibility(View.VISIBLE);
                break;
//            case 7:
//                tvTitle.setText(R.string.xshs);
//                sb.append(getString(R.string.xshs));
//                loadXshsData();
//                lin4.setVisibility(View.VISIBLE);
//                break;
            case 8:
//                tvTitle.setText(R.string.dj);
//                sb.append(getString(R.string.dj));
                tvTitle.setText("组织管理");
                sb.append("组织管理");
                djLay.setVisibility(View.GONE);
//                loadDjData();
                loadDzz();
                lin1.setVisibility(View.VISIBLE);
                break;
            case 9:
                tvTitle.setText(R.string.xfwd);
                sb.append(getString(R.string.xfwd));
                loadWfwdData();
                lin6.setVisibility(View.VISIBLE);
                break;
            case 10:
                tvTitle.setText(R.string.fwzw);
                sb.append(getString(R.string.fwzw));
                loadFwzwData();
                lin10.setVisibility(View.VISIBLE);
                break;
        }
        sb.append("工作动态");
//        if (isContain()) {
        gzdtBtn.setText(sb.toString());
        gzdtBtn.setVisibility(View.VISIBLE);
//        } else {
//            gzdtBtn.setVisibility(View.GONE);
//        }
        if (type != 8) {
            sxgz_zzgl_ll.setVisibility(View.GONE);
        }
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4242".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    private void loadXqgsqData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/getPlotSize";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        String size = result.getString("data");
                        tvXqsl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvXqsl.setText(size);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadFwzwData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/fwzw/queryFwzwInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Fwzw sp = (Fwzw) gson.fromJson(result.getJSONObject("data").toString(),
                                Fwzw.class);
                        tvJdPhone.setText(sp.getJdphone() == null ? "" : sp.getJdphone());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadZfzzData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sfxz/querySfxzInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Sfxz sp = (Sfxz) gson.fromJson(result.getJSONObject("data").toString(),
                                Sfxz.class);
                        tvDjy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvFlgw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvXcy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvXxy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZyz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDjy_g.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvFlgw_g.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvXcy_g.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvXxy_g.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZyz_g.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        if (roles != null && roles.size() > 0) {
                            tvJsb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvZdqsn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvXjry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvSqfx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvSqjd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvAzbj.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        }

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
                        tvDjy_g.setText(String.valueOf(sp.getMdjfdjy_g()));
                        tvFlgw_g.setText(String.valueOf(sp.getFlgw_g()));
                        tvXcy_g.setText(String.valueOf(sp.getFzxcy_g()));
                        tvXxy_g.setText(String.valueOf(sp.getMdjfxxy_g()));
                        tvZyz_g.setText(String.valueOf(sp.getPfzyz_g()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadWfwdData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xfwd/queryXfwdInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Xfwd sp = (Xfwd) gson.fromJson(result.getJSONObject("data").toString(),
                                Xfwd.class);
                        tvZdrq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZdrq.setText(sp.getZdrq() == null ? "" : sp.getZdrq());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadAqscData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/aqsc/querySafeProduceInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        SafeProduce sp = (SafeProduce) gson.fromJson(result.getJSONObject("data")
                                .toString(), SafeProduce.class);
                        tvCpy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDzjgf.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJzgd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvMy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvRpzf.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTzsb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvWxhxp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYhbz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvXfaq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYjdw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCpy.setText(String.valueOf(sp.getCheng()));
                        tvDzjgf.setText(String.valueOf(sp.getDou()));
                        tvJzgd.setText(String.valueOf(sp.getJian()));
                        tvLt.setText(String.valueOf(sp.getLtqy()));
                        tvMy.setText(String.valueOf(sp.getMyqy()));
                        tvRpzf.setText(String.valueOf(sp.getRou()));
                        tvTzsb.setText(String.valueOf(sp.getTe()));
                        tvWxhxp.setText(String.valueOf(sp.getWei()));
                        tvYhbz.setText(String.valueOf(sp.getYan()));
                        tvXfaq.setText(String.valueOf(sp.getXfaq()));
                        tvYjdw.setText(String.valueOf(sp.getYjdw()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadXshsData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/queryXshsInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Xshs sp = (Xshs) gson.fromJson(result.getJSONObject("data").toString(),
                                Xshs.class);
                        tvGt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvQy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvSpdkd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        int num = sp.getJt() + sp.getJz() + sp.getLt() + sp.getMy() + sp.getZbs();
                        lt = sp.getLt();
                        my = sp.getMy();
                        jt = sp.getJt();
                        zb = sp.getZbs();
                        jz = sp.getJz();
                        tvQy.setText(String.valueOf(num));
                        tvGt.setText(String.valueOf(sp.getGeti()));
//                        tvQy.setText(String.valueOf(sp.getQiye()));
                        tvSpdkd.setText(String.valueOf(sp.getSpdkd()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadQygzData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qygz/queryBusinessInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        BusinessJob sp = (BusinessJob) gson.fromJson(result.getJSONObject("data")
                                .toString(), BusinessJob.class);
                        tvJtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvMzqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJtqy.setText(String.valueOf(sp.getJt()));
                        tvLtqy.setText(String.valueOf(sp.getLt()));
                        tvMzqy.setText(String.valueOf(sp.getMy()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadDjData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dj/queryPartyBuildingInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        PartyBuilding sp = (PartyBuilding) gson.fromJson(result.getJSONObject
                                ("data").toString(), PartyBuilding.class);
                        tvKnDys.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDys.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvGggs.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYxdy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvHrb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDzz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLrdy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDxzs.setText(sp.getDxznum() == null ? "" : sp.getDxznum());
                        tvDys.setText(sp.getDnum() == null ? "0" : sp.getDnum());
                        tvKnDys.setText(sp.getKndynum() == null ? "" : sp.getKndynum());
                        tvGggs.setText(sp.getGggs() == null ? "" : sp.getGggs());
                        tvYxdy.setText(sp.getYxdy() == null ? "" : sp.getYxdy());
                        tvHrb.setText(sp.getHrb() == null ? "" : sp.getHrb());
                        tvDzz.setText(sp.getDxznum() == null ? "0" : sp.getHrb());
                        tvLrdy.setText(String.valueOf(sp.getLrdy()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadDzz() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dzz/queryDzzList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
//                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                tvDys.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                tvDzz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                tvLrdy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                Gson gson = new Gson();
                                Djzz djzz = (Djzz) gson.fromJson(result.getJSONObject("data").toString(),
                                        Djzz.class);
                                if (djzz != null) {
                                    tvDys.setText(String.valueOf(djzz.getDynum()));
                                    tvLrdy.setText(String.valueOf(djzz.getLddynum()));
                                    int l = 0;
                                    int f = 0;
                                    if (djzz.getlDzb() != null) {
                                        l = djzz.getlDzb().size();
                                    }
                                    if (djzz.getfDzb() != null) {
                                        f = djzz.getfDzb().size();
                                    }
                                    cjdzz = djzz.getCjdzz();
                                    zddzz = djzz.getzDzz().size();
                                    lxdzz = f + l;
                                    lsdzz = djzz.getLsDzzList().size();
                                    tvDzz.setText(String.valueOf(lxdzz + cjdzz + zddzz + lsdzz));
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(SxgzListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            SuccinctProgress.dismiss();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
//                        SuccinctProgress.dismiss();
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    private void loadMzData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/mz/queryMzInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Minzheng sp = (Minzheng) gson.fromJson(result.getJSONObject("data")
                                .toString(), Minzheng.class);
                        tvNcdb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCsdb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLsrt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYfdx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTkgy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZlj.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvNcdb.setText(sp.getNcdbhs() == null ? "" : String.valueOf(sp.getNcdbhs
                                ()));
                        tvCsdb.setText(sp.getCsdbhs() == null ? "" : String.valueOf(sp.getCsdbhs
                                ()));
                        tvCjr.setText(sp.getCjrnum() == null ? "" : String.valueOf(sp.getCjrnum()));
                        tvLsrt.setText(sp.getLsrtnum() == null ? "" : String.valueOf(sp
                                .getLsrtnum()));
                        tvYfdx.setText(sp.getYfdx() == null ? "" : String.valueOf(sp.getYfdx()));
//                        tvCjr_1.setText(String.valueOf(sp.getCzCjrNum()));
//                        tvCjr_2.setText(String.valueOf(sp.getZcCjrNum()));
//                        tvCjr_3.setText(String.valueOf(sp.getQtCjrNum()));
//                        tvCjr_4.setText(String.valueOf(sp.getYjCjrNum()));
//                        tvCjr_5.setText(String.valueOf(sp.getRjCjrNum()));
                        tvTkgy.setText(String.valueOf(sp.getTkgyNum()));
//                        tvTkgy_1.setText(String.valueOf(sp.getTkgyNum_1()));
//                        tvTkgy_2.setText(String.valueOf(sp.getTkgyNum_2()));
//                        tvTkgy_3.setText(String.valueOf(sp.getTkgyNum_3()));
//                        tvTkgy_4.setText(String.valueOf(sp.getTkgyNum_4()));
                        tvZlj.setText(String.valueOf(sp.getZljNum()));
//                        tvZlj_1.setText(String.valueOf(sp.getZljNum_1()));
//                        tvZlj_2.setText(String.valueOf(sp.getZljNum_2()));
//                        tvZlj_3.setText(String.valueOf(sp.getZljNum_3()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadJhsyData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jhsy/queryJhsyInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        FamilyPlanning sp = (FamilyPlanning) gson.fromJson(result.getJSONObject
                                ("data").toString(), FamilyPlanning.class);
                        tvYlfn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDszn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz_sf.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz_sd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz_sc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLdrk.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJskn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

                        tvYlfn.setText(String.valueOf(sp.getYlfnnum()));//育龄妇女
                        tvDszn.setText(String.valueOf(sp.getOnechildfamilynum()));//独生子女
                        tvJlfz.setText(String.valueOf(sp.getShengfNum()));//省扶
                        tvJlfz_sf.setText(String.valueOf(sp.getShifNum()));//市扶
                        tvJlfz_sd.setText(String.valueOf(sp.getSdNum()));//失独
                        tvJlfz_sc.setText(String.valueOf(sp.getScNum()));//伤残
                        tvLdrk.setText(String.valueOf(sp.getLdrknum()));//流动人口
                        tvJskn.setText(String.valueOf(sp.getJsknnum()));//计生困难
                        tvYy.setText(String.valueOf(sp.getYyNum()));//计生困难
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void loadCscxData() {
//        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cscxgl/queryCscxglInfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
//                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        CityManage sp = (CityManage) gson.fromJson(result.getJSONObject("data")
                                .toString(), CityManage.class);
                        try {
                            Double area = Double.valueOf(sp.getArea());
                            java.text.DecimalFormat df = new java.text.DecimalFormat("######0.000");
                            tvArea.setText(String.valueOf(df.format(area)));
                        } catch (Exception e) {
                            tvArea.setText(sp.getArea() == null ? "0" : sp.getArea());
                        }
                        tvDlts.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvHtts.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLjqyy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLdbjy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvHu.setText(sp.getHs() == null ? "" : sp.getHs());
                        tvDlts.setText(sp.getDaolu() == null ? "" : sp.getDaolu());
                        tvHtts.setText(sp.getHetang() == null ? "" : sp.getHetang());
                        tvLjqyy.setText(String.valueOf(sp.getLjclear()));
                        tvLdbjy.setText(sp.getLiudong() == null ? "" : sp.getLiudong());
                        tvLjc.setText(sp.getLjc() == null ? "" : sp.getLjc());
                        tvLjt.setText(sp.getLjt() == null ? "" : sp.getLjt());
                        tvYsd.setText(sp.getLjaddress() == null ? "" : sp.getLjaddress());
                        tvRcl.setText(sp.getLjnum() == null ? "" : sp.getLjnum());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void request() {
        SuccinctProgress.showSuccinctProgress(SxgzListActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "village_queryList", params, new
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
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<Village> v = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Village>>() {
                                        }.getType());
                                villages.addAll(v);
                                if (type == 4) {
                                    Village village = new Village();
                                    village.setName("经济开发区");
                                    village.setId("jjkfq");
                                    villages.add(village);
                                }
                                villageAdapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(SxgzListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
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

    private void getSxgzById(int id) {
        String url = ConstantUtil.BASE_URL + "/sxgz/queryById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(id));
        VolleyRequest.RequestPost(this, url, "queryById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        LogUtil.i("result=" + result.getJSONObject("data").toString());
                        Gson gson = new Gson();
                        Sxgz sxgz = (Sxgz) gson.fromJson(result.getJSONObject("data").toString(),
                                Sxgz.class);
                        if (!StringUtil.isEmpty(sxgz.getContent())) {
                            mContent.setText(sxgz.getContent());
                        } else {
                            mLay.setVisibility(View.GONE);
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SxgzListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SxgzListActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
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

    @Event(value = {R.id.iv_back, R.id.sxgz_btn, R.id.sxgz_gzdt_btn, R.id.dj_dys, R.id.dj_kndys,
            R.id.mz_ncdb, R.id.mz_csdb, R.id.mz_cjr, R.id.mz_lset, R.id.mz_yfdx, R.id.mz_tkgy, R
            .id.mz_zlj, R.id.sfxz_djy, R.id.sfxz_xxy, R.id.sfxz_xcy, R.id.sfxz_zyz, R.id
            .sfxz_flgw, R.id.sfxz_djy_g, R.id.sfxz_xxy_g, R.id.sfxz_xcy_g, R.id.sfxz_zyz_g, R.id
            .sfxz_flgw_g, R.id.qygz_ltqy, R.id.qygz_myqy, R.id.qygz_jtqy, R.id.xshs_qy, R.id
            .xshs_gt, R.id.xshs_sbdkd, R.id.aqsc_ltqy, R.id.aqsc_myqy, R.id.aqsc_jyyhbzs, R.id.aqsc_rpzf, R.id
            .aqsc_dzpjgzf, R.id.aqsc_jzgd, R.id.aqsc_tzsb, R.id.aqsc_wxhxp, R.id.aqsc_cpy, R.id
            .aqsc_xfaq, R.id.aqsc_yjdw, R.id.jhsy_ylfns, R.id.jhsy_dszn, R.id.jhsy_jlfz, R.id.jhsy_jlfz_sf, R.id
            .jhsy_jlfz_sd, R.id.jhsy_jlfz_sc, R.id.jhsy_ldrk, R.id.jhsy_jskn, R.id.jhsy_yy, R.id.cscxgl_dlts, R
            .id.cscxgl_htts, R.id.cscxgl_ljqyy, R.id.cscxgl_ldbjy, R.id.xqgsq_ls, R.id.xfwd_zdrq,
            R.id.sfxz_jsb, R.id.sfxz_zdqsn, R.id.sfxz_sqfx, R.id.sfxz_sqjdry, R.id.sfxz_xjry, R
            .id.sfxz_azbj, R.id.dj_title_gsgg, R.id.dj_title_yxdy, R.id.dj_title_hrb, R.id.dj_title_dzz, R.id.dj_title_dy, R.id.dj_title_lrdy, R.id.dj_lrdys, R.id.dj_dzzs, R.id.zzgl_yxgcdy, R.id.zzgl_dfyn}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dj_lrdys:
                intent.setClass(this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 0);
                intent.putExtra("dyldType", "2");
                startActivity(intent);
                break;
            case R.id.dj_dzzs:
                intent.setClass(this, DzzlbActivity.class);
                intent.putExtra("cjdzz", cjdzz);
                intent.putExtra("zddzz", zddzz);
                intent.putExtra("lxdzz", lxdzz);
                intent.putExtra("lsdzz", lsdzz);
                startActivity(intent);
                break;
            case R.id.xshs_sbdkd:
                intent.putExtra("isFrom", "index");
                intent.setClass(SxgzListActivity.this, DkdListActivity.class);
                startActivity(intent);
                break;
            case R.id.dj_title_lrdy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 0);
                intent.putExtra("dyldType", "2");
                startActivity(intent);
                break;
            case R.id.dj_title_dy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
//                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.dj_title_dzz:
                intent.setClass(SxgzListActivity.this, DjZzjgActivity.class);
                startActivity(intent);
                break;
            case R.id.dj_title_gsgg:
                intent.setClass(SxgzListActivity.this, GgActivity.class);
                startActivity(intent);
                break;
            case R.id.dj_title_yxdy:
                intent.setClass(SxgzListActivity.this, YxdyActivity.class);
                startActivity(intent);
                break;
            case R.id.dj_title_hrb:
                intent.setClass(SxgzListActivity.this, HrbActivity.class);
                startActivity(intent);
                break;
            case R.id.sfxz_jsb:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 57);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_zdqsn:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 58);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_sqfx:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 61);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_sqjdry:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 59);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_xjry:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 60);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_azbj:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 62);
                    startActivity(intent);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.sxgz_btn:
                if ("展开".equals(allBtn.getText())) {
                    allBtn.setText("收起");
                    Drawable drawable = getResources().getDrawable(R.mipmap.min_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    mContent.setMaxLines(10000);
                } else if ("收起".equals(allBtn.getText())) {
                    allBtn.setText("展开");
                    Drawable drawable = getResources().getDrawable(R.mipmap.all_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    mContent.setMaxLines(5);
                }
                break;
            case R.id.sxgz_gzdt_btn:
                intent.setClass(SxgzListActivity.this, GzdtActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.dj_dys:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("dyldType", "1");
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.dj_kndys:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 0);
                intent.putExtra("dyType", "02");
                startActivity(intent);
                break;
            case R.id.mz_ncdb:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.mz_csdb:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.mz_cjr:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 6);
                startActivity(intent);
                break;
            case R.id.mz_lset:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 13);
                startActivity(intent);
                break;
            case R.id.mz_yfdx:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 14);
                startActivity(intent);
                break;
            case R.id.mz_tkgy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 15);
                startActivity(intent);
                break;
            case R.id.mz_zlj:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 16);
                startActivity(intent);
                break;
            case R.id.sfxz_djy_g:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 21);
                startActivity(intent);
                break;
            case R.id.sfxz_xxy_g:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 22);
                startActivity(intent);
                break;
            case R.id.sfxz_xcy_g:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 23);
                startActivity(intent);
                break;
            case R.id.sfxz_zyz_g:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 24);
                startActivity(intent);
                break;
            case R.id.sfxz_flgw_g:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 25);
                startActivity(intent);
                break;
            case R.id.sfxz_djy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 50);
                startActivity(intent);
                break;
            case R.id.sfxz_xxy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 51);
                startActivity(intent);
                break;
            case R.id.sfxz_xcy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 52);
                startActivity(intent);
                break;
            case R.id.sfxz_zyz:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 53);
                startActivity(intent);
                break;
            case R.id.sfxz_flgw:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 54);
                startActivity(intent);
                break;
            case R.id.qygz_ltqy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 40);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
            case R.id.qygz_myqy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 41);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
            case R.id.qygz_jtqy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 42);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
            case R.id.xshs_qy:
//                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
//                intent.putExtra("isFrom", "index");
//                intent.putExtra("type", 4);
//                startActivity(intent);
                intent.putExtra("lt", lt);
                intent.putExtra("my", my);
                intent.putExtra("jt", jt);
                intent.putExtra("zb", zb);
                intent.putExtra("jz", jz);
                intent.putExtra("isYjfw", "0");
                intent.setClass(SxgzListActivity.this, QylbActivity.class);
                startActivity(intent);
                break;
            case R.id.xshs_gt:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 5);
                startActivity(intent);
                break;
            case R.id.aqsc_ltqy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 40);
                intent.putExtra("xfaqType", 2);
                intent.putExtra("isYjfw", "1");
                startActivity(intent);
                break;
            case R.id.aqsc_myqy:
                intent.putExtra("xfaqType", 2);
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 41);
                intent.putExtra("isYjfw", "1");
                startActivity(intent);
                break;
            case R.id.aqsc_jyyhbzs:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 43);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_rpzf:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 44);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_dzpjgzf:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 45);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_jzgd:
//                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
//                intent.putExtra("isFrom", "index");
//                intent.putExtra("type", 46);
//                startActivity(intent);
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 46);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_tzsb:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 47);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_wxhxp:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 48);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_cpy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 49);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_xfaq:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 4);
                intent.putExtra("xfaqType", 1);
                intent.putExtra("isYjfw", "1");
                startActivity(intent);
                break;
            case R.id.aqsc_yjdw:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 63);
                startActivity(intent);
                break;
            case R.id.jhsy_ylfns:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.xfwd_zdrq:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 56);
                startActivity(intent);
                break;
            case R.id.jhsy_dszn:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 26);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 27);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz_sf:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 28);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz_sd:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 29);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz_sc:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 30);
                startActivity(intent);
                break;
            case R.id.jhsy_ldrk:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 31);
                startActivity(intent);
                break;
            case R.id.jhsy_jskn:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 32);
                startActivity(intent);
                break;
            case R.id.jhsy_yy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 64);
                startActivity(intent);
                break;
            case R.id.cscxgl_dlts:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 17);
                startActivity(intent);
                break;
            case R.id.cscxgl_htts:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 18);
                startActivity(intent);
                break;
            case R.id.cscxgl_ljqyy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 19);
                startActivity(intent);
                break;
            case R.id.cscxgl_ldbjy:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 20);
                startActivity(intent);
                break;
            case R.id.xqgsq_ls:
                intent.setClass(SxgzListActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 7);
                startActivity(intent);
                break;
            case R.id.zzgl_yxgcdy:
                intent.setClass(SxgzListActivity.this, YxdyActivity.class);
                startActivity(intent);
                break;
            case R.id.zzgl_dfyn:
                String package_name1 = "cn.jsb.china";
                PackageManager packageManager1 = getPackageManager();
                Intent it1 = packageManager1.getLaunchIntentForPackage(package_name1);
                if (it1 != null) {
                    startActivity(it1);
                } else {
                    //没有默认的入口 Activity
                    ToastUtil.show(SxgzListActivity.this, "未发现江苏银行应用，请下载");
                }
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("village_queryList");
        BaseApplication.getRequestQueue().cancelAll("queryById");
        BaseApplication.getRequestQueue().cancelAll("queryList");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (type == 0) {
//            Intent intent = new Intent();
//            intent.setClass(this, XxcjVillageListActivity.class);
//            intent.putExtra("title", getString(R.string.xqgsq));
//            intent.putExtra("type", 0);
//            intent.putExtra("isFrom", "index");
//            startActivity(intent);
//        } else {
        Village vv = villages.get(position);
        Intent intent = new Intent();
        if ("jjkfq".equals(vv.getId())) {
            //经济开发区
            intent.setClass(SxgzListActivity.this, JjkfqActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("village", vv);
            intent.putExtras(bundle);
        } else {
            intent.setClass(SxgzListActivity.this, VillageSxgzActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("village", vv);
            intent.putExtras(bundle);
            intent.putExtra("type", type);
        }
        startActivity(intent);
//        }
    }

}

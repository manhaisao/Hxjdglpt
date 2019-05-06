package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.BusinessJob;
import com.xzz.hxjdglpt.model.CityManage;
import com.xzz.hxjdglpt.model.FamilyPlanning;
import com.xzz.hxjdglpt.model.Fwzw;
import com.xzz.hxjdglpt.model.GridSxgz;
import com.xzz.hxjdglpt.model.Minzheng;
import com.xzz.hxjdglpt.model.PartyBuilding;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.SafeProduce;
import com.xzz.hxjdglpt.model.Sfxz;
import com.xzz.hxjdglpt.model.Sxgz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Xfwd;
import com.xzz.hxjdglpt.model.Xshs;
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
 * 地图详情
 * Created by dbz on 2017/7/13.
 */
@ContentView(R.layout.aty_grid_sxgz_detail)
public class GridSxgzDetailActivity extends BaseActivity {

    private String gridId;

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;

    private BaseApplication application;

    @ViewInject(R.id.grid_sxgz_content)
    private TextView mContent;
    @ViewInject(R.id.sxgz_gzdt_btn)
    private Button gzdtBtn;
    private int type;

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

    @ViewInject(R.id.village_v_zrr)
    private TextView tvZrr;
    @ViewInject(R.id.village_v_zrrphone)
    private TextView tvZrrPhone;

    //党建
    @ViewInject(R.id.dj_dxzs)
    private TextView tvDxzs;
    @ViewInject(R.id.dj_dys)
    private TextView tvDys;
    @ViewInject(R.id.dj_kndys)
    private TextView tvKnDys;
    @ViewInject(R.id.village_dj_tshd)
    private TextView tvTshd;
    @ViewInject(R.id.village_v_fgr)
    private TextView tvFgr;
    @ViewInject(R.id.village_v_fgrphone)
    private TextView tvFgrphone;
    @ViewInject(R.id.dj_zddzz)
    private TextView tvZddw;
    @ViewInject(R.id.dj_lxdzz)
    private TextView tvLxdzz;
    @ViewInject(R.id.dj_lsdzz)
    private TextView tvLsdzz;

    //民政
    @ViewInject(R.id.mz_ncdb)
    private TextView tvNcdb;//农村低保
    @ViewInject(R.id.mz_csdb)
    private TextView tvCsdb;//城市低保
    @ViewInject(R.id.mz_cjr)
    private TextView tvCjr;//残疾人
    @ViewInject(R.id.village_mz_cjr_1)
    private TextView tvCjr_1;//持证残疾人
    @ViewInject(R.id.village_mz_cjr_2)
    private TextView tvCjr_2;//重残生活补贴
    @ViewInject(R.id.village_mz_cjr_3)
    private TextView tvCjr_3;//其他类生活补贴
    @ViewInject(R.id.village_mz_cjr_4)
    private TextView tvCjr_4;//一级护理补贴
    @ViewInject(R.id.village_mz_cjr_5)
    private TextView tvCjr_5;//二级护理补贴
    @ViewInject(R.id.mz_lset)
    private TextView tvLsrt;//留守儿童
    @ViewInject(R.id.mz_yfdx)
    private TextView tvYfdx;//优抚对象
    @ViewInject(R.id.mz_tkgy)
    private TextView tvTkgy;//特困(五保)供养
    @ViewInject(R.id.village_mz_tkgy_1)
    private TextView tvTkgy_1;//集中供养人员
    @ViewInject(R.id.village_mz_tkgy_2)
    private TextView tvTkgy_2;//分散供养人员
    @ViewInject(R.id.village_mz_tkgy_3)
    private TextView tvTkgy_3;//城市三无人员
    @ViewInject(R.id.village_mz_tkgy_4)
    private TextView tvTkgy_4;//孤儿及困境儿童
    @ViewInject(R.id.mz_zlj)
    private TextView tvZlj;//80岁以上尊老金
    @ViewInject(R.id.village_mz_zlj_1)
    private TextView tvZlj_1;//80-89周岁人员
    @ViewInject(R.id.village_mz_zlj_2)
    private TextView tvZlj_2;//90-99周岁人员
    @ViewInject(R.id.village_mz_zlj_3)
    private TextView tvZlj_3;//100周岁以上
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
    //协税护税
    @ViewInject(R.id.xshs_qy)
    private TextView tvQy;
    @ViewInject(R.id.xshs_gt)
    private TextView tvGt;
    @ViewInject(R.id.village_xshs_wjh)
    private TextView tvHys;
    @ViewInject(R.id.village_xshs_hsy_phone)
    private TextView tvHsyPhone;
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

    //和谐社区
    @ViewInject(R.id.xfwd_zdrq)
    private TextView tvZdrq;//重点人群
    @ViewInject(R.id.village_xfwd_ajy)
    private TextView tvAjy;
    @ViewInject(R.id.village_xfwd_ajyphone)
    private TextView tvAjyPhone;
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
    @ViewInject(R.id.village_cscxgl_wgz)
    private TextView tvWgz;//网格长
    @ViewInject(R.id.village_cscxgl_wgzphone)
    private TextView tvWgzPhone;//网格长电话
    @ViewInject(R.id.village_cscxgl_qt)
    private TextView tvQt;//其他管理设备
    @ViewInject(R.id.village_cscxgl_content)
    private TextView tvContent;//其他关于网格内容信息

    //防伪治违
    @ViewInject(R.id.fwzw_jdphone)
    private TextView tvJdPhone;

    //小区数量
    @ViewInject(R.id.village_v_xqsq)
    private TextView tvXqsl;

    @ViewInject(R.id.village_v_plot)
    private RelativeLayout m1;
    @ViewInject(R.id.village_v_1)
    private RelativeLayout m2;
    @ViewInject(R.id.village_v_2)
    private RelativeLayout m3;


    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        type = getIntent().getIntExtra("type", 1);
        roles = application.getRolesList();
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(gridId);
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case 12:
                m1.setVisibility(View.VISIBLE);
                m2.setVisibility(View.GONE);
                m3.setVisibility(View.GONE);
                sb.append(getString(R.string.xqgsq));
                loadPlotSizeByGId();
                break;
            case 1:
                m2.setVisibility(View.GONE);
                m3.setVisibility(View.GONE);
                loadCscxData();
                sb.append(getString(R.string.cscxgl));
                lin9.setVisibility(View.VISIBLE);
                break;
            case 2:
                loadAqscData();
                sb.append(getString(R.string.aqsc));
                lin5.setVisibility(View.VISIBLE);
                break;
            case 3:
                loadJhsyData();
                sb.append(getString(R.string.jhsy));
                lin7.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvWgz_t.setText("网格责任人：");
                tvWgz_t_p.setText("网格责任人联系电话：");
                loadQygzData();
                sb.append(getString(R.string.qygz));
                lin3.setVisibility(View.VISIBLE);
                break;
            case 5:
                loadMzData();
                sb.append(getString(R.string.mz));
                lin2.setVisibility(View.VISIBLE);
                break;
            case 6:
                loadZfzzData();
                sb.append(getString(R.string.sfxz));
                lin8.setVisibility(View.VISIBLE);
                break;
            case 7:
                loadXshsData();
                sb.append(getString(R.string.xshs));
                lin4.setVisibility(View.VISIBLE);
                break;
            case 8:
                tvWgz_t.setText("网格员：");
                tvWgz_t_p.setText("联系号码：");
                loadDjData();
                sb.append(getString(R.string.dj));
                lin1.setVisibility(View.VISIBLE);
                break;
            case 9:
                loadWfwdData();
                sb.append(getString(R.string.xfwd));
                lin6.setVisibility(View.VISIBLE);
                break;
            case 10:
                loadFwzwData();
                sb.append(getString(R.string.fwzw));
                lin10.setVisibility(View.VISIBLE);
                break;
        }
        sb.append("工作动态");
        if (type == 1 || type == 8) {
            gzdtBtn.setText(sb.toString());
            gzdtBtn.setVisibility(View.VISIBLE);
        } else {
            gzdtBtn.setVisibility(View.GONE);
        }
    }


    public void initData() {
        getSxgzById();
    }

    private void loadPlotSizeByGId() {
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/getPlotSize";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/fwzw/queryFwzwById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        tvZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        tvJdPhone.setText(sp.getJdphone() == null ? "" : sp.getJdphone());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sfxz/querySfxzById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        if (roles != null && roles.size() > 0) {
                            tvJsb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvZdqsn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvXjry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvSqfx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvSqjd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                            tvAzbj.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        }
                        tvZrr.setText(sp.getHeadname() == null ? "" : sp.getHeadname());
                        tvZrrPhone.setText(sp.getHeadphone() == null ? "" : sp.getHeadphone());
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
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xfwd/queryXfwdlById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvZrr.setText(sp.getHeadName() == null ? "" : sp.getHeadName());
                        tvZrrPhone.setText(sp.getHeadPhone() == null ? "" : sp.getHeadPhone());
                        tvAjy.setText(sp.getBaMan() == null ? "" : sp.getBaMan());
                        tvAjyPhone.setText(sp.getBaManPhone() == null ? "" : sp.getBaManPhone());
                        tvZdrq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZdrq.setText(sp.getZdrq() == null ? "" : sp.getZdrq());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/aqsc/queryAqscById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        tvZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
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
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/queryXshsById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        tvZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        tvHys.setText(sp.getHsy() == null ? "" : sp.getHsy());
                        tvHsyPhone.setText(sp.getHsyphone() == null ? "" : sp.getHsyphone());
                        tvGt.setText(String.valueOf(sp.getGeti()));
                        tvQy.setText(String.valueOf(sp.getQiye()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qygz/queryQygzById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvLtqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvMzqy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvWgz_v.setText(sp.getWgzrr() == null ? "" : sp.getWgzrr());
                        tvWgz_v_p.setText(sp.getWgzrrdh() == null ? "" : sp.getWgzrrdh());
                        tvHs_v.setText(sp.getHszrr() == null ? "" : sp.getHszrr());
                        tvHs_v_p.setText(sp.getHszrrphone() == null ? "" : sp.getHszrrphone());
                        int num = sp.getJt() + sp.getJz() + sp.getLt() + sp.getMy() + sp.getZbs();
                        lt = sp.getLt();
                        my = sp.getMy();
                        jt = sp.getJt();
                        zb = sp.getZbs();
                        jz = sp.getJz();
                        tvLtqy.setText(String.valueOf(num));
                        tvMzqy.setText(String.valueOf(sp.getGt()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dj/queryPartyBuildingByGid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvTshd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZddw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLxdzz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLsdzz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        tvZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        tvDxzs.setText(sp.getDxznum() == null ? "0" : sp.getDxznum());
                        tvDys.setText(sp.getDnum() == null ? "0" : sp.getDnum());
                        tvKnDys.setText(String.valueOf(sp.getLrdy()));
                        tvTshd.setText(sp.getTshdNum() == null ? "0" : sp.getTshdNum());
                        tvFgr.setText(sp.getFgr());
                        tvFgrphone.setText(sp.getFgrphone());
                        tvZddw.setText(String.valueOf(sp.getZddwnum()));
                        tvLxdzz.setText(String.valueOf(sp.getLxnum()));
                        tvLsdzz.setText(String.valueOf(sp.getLsnum()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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

    private void loadMzData() {
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/mz/queryMzByGId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        tvZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        tvNcdb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCsdb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLsrt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYfdx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr_1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr_2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr_3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvCjr_5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTkgy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTkgy_1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTkgy_2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTkgy_3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvTkgy_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZlj.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZlj_1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZlj_2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvZlj_3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvNcdb.setText(sp.getNcdbhs() == null ? "" : String.valueOf(sp.getNcdbhs
                                ()));
                        tvCsdb.setText(sp.getCsdbhs() == null ? "" : String.valueOf(sp.getCsdbhs
                                ()));
                        tvCjr.setText(sp.getCjrnum() == null ? "" : String.valueOf(sp.getCjrnum()));
                        tvLsrt.setText(sp.getLsrtnum() == null ? "" : String.valueOf(sp
                                .getLsrtnum()));
                        tvYfdx.setText(sp.getYfdx() == null ? "" : String.valueOf(sp.getYfdx()));
                        tvCjr_1.setText(String.valueOf(sp.getCzCjrNum()));
                        tvCjr_2.setText(String.valueOf(sp.getZcCjrNum()));
                        tvCjr_3.setText(String.valueOf(sp.getQtCjrNum()));
                        tvCjr_4.setText(String.valueOf(sp.getYjCjrNum()));
                        tvCjr_5.setText(String.valueOf(sp.getRjCjrNum()));
                        tvTkgy.setText(String.valueOf(sp.getTkgyNum()));
                        tvTkgy_1.setText(String.valueOf(sp.getTkgyNum_1()));
                        tvTkgy_2.setText(String.valueOf(sp.getTkgyNum_2()));
                        tvTkgy_3.setText(String.valueOf(sp.getTkgyNum_3()));
                        tvTkgy_4.setText(String.valueOf(sp.getTkgyNum_4()));
                        tvZlj.setText(String.valueOf(sp.getZljNum()));
                        tvZlj_1.setText(String.valueOf(sp.getZljNum_1()));
                        tvZlj_2.setText(String.valueOf(sp.getZljNum_2()));
                        tvZlj_3.setText(String.valueOf(sp.getZljNum_3()));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jhsy/queryJhsyById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvZrr.setText(sp.getHead() == null ? "" : sp.getHead());
                        tvZrrPhone.setText(sp.getHeadphone() == null ? "" : sp.getHeadphone());
                        tvYlfn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvDszn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz_sf.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz_sd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJlfz_sc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLdrk.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvJskn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvYlfn.setText(String.valueOf(sp.getYlfnnum()));//育龄妇女
                        tvDszn.setText(String.valueOf(sp.getOnechildfamilynum()));//独生子女
                        tvJlfz.setText(String.valueOf(sp.getShengfNum()));//省扶
                        tvJlfz_sf.setText(String.valueOf(sp.getShifNum()));//市扶
                        tvJlfz_sd.setText(String.valueOf(sp.getSdNum()));//失独
                        tvJlfz_sc.setText(String.valueOf(sp.getScNum()));//伤残
                        tvLdrk.setText(String.valueOf(sp.getLdrknum()));//流动人口
                        tvJskn.setText(String.valueOf(sp.getJsknnum()));//计生困难
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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

    @ViewInject(R.id.village_cscxgl_jdwly)
    private TextView village_cscxgl_jdwly;
    @ViewInject(R.id.village_cscxgl_jdwlyphone)
    private TextView village_cscxgl_jdwlyphone;
    @ViewInject(R.id.village_cscxgl_cjwly)
    private TextView village_cscxgl_cjwly;
    @ViewInject(R.id.village_cscxgl_cjwlyphone)
    private TextView village_cscxgl_cjwlyphone;
    @ViewInject(R.id.cscxgl_zrk)
    private TextView cscxgl_zrk;
    @ViewInject(R.id.cscxgl_bjxx)
    private TextView cscxgl_bjxx;
    @ViewInject(R.id.cscxgl_ld)
    private TextView cscxgl_ld;
    @ViewInject(R.id.cscxgl_htgq)
    private TextView cscxgl_htgq;
    @ViewInject(R.id.cscxgl_t_bjcl)
    private TextView cscxgl_t_bjcl;


    private void loadCscxData() {
//        SuccinctProgress.showSuccinctProgress(GridSxgzDetailActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cscxgl/queryCscxglById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        VolleyRequest.RequestPost(this, url, "queryListGrid", params, new VolleyListenerInterface
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
                        tvDlts.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvHtts.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLjqyy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLdbjy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

                        cscxgl_bjxx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        cscxgl_htgq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        tvLdbjy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        village_cscxgl_jdwly.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                        village_cscxgl_cjwly.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

                        village_cscxgl_jdwly.setText(sp.getJdwgy() == null ? "" : sp.getJdwgy());
                        village_cscxgl_jdwlyphone.setText(sp.getJdwgyphone() == null ? "" : sp.getJdwgyphone());
                        village_cscxgl_cjwly.setText(sp.getCjwgy() == null ? "" : sp.getCjwgy());
                        village_cscxgl_cjwlyphone.setText(sp.getCjwgyphone() == null ? "" : sp.getCjwgyphone());
                        cscxgl_zrk.setText(sp.getRk() == null ? "" : sp.getRk());
                        cscxgl_bjxx.setText(sp.getDaolu() == null ? "0" : sp.getDaolu());
                        cscxgl_ld.setText(sp.getLd() == null ? "0" : sp.getLd());
                        cscxgl_htgq.setText(sp.getHetang() == null ? "0" : sp.getHetang());

                        tvDlts.setText(sp.getDaolu() == null ? "" : sp.getDaolu());
                        tvHtts.setText(sp.getHetang() == null ? "" : sp.getHetang());
                        tvZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        tvZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        tvWgz.setText(sp.getWgz() == null ? "" : sp.getWgz());
                        tvWgzPhone.setText(sp.getWgzphone() == null ? "" : sp.getWgzphone());
                        tvArea.setText(sp.getArea() == null ? "0" : sp.getArea());
                        tvHu.setText(sp.getHs() == null ? "0" : sp.getHs());
                        tvLjqyy.setText(String.valueOf(sp.getLjclear()));
                        tvLdbjy.setText(sp.getLiudong() == null ? "0" : sp.getLiudong());
                        tvLjc.setText(sp.getLjc() == null ? "0" : sp.getLjc());
                        tvLjt.setText(sp.getLjt() == null ? "0" : sp.getLjt());
                        tvYsd.setText(sp.getLjaddress() == null ? "0" : sp.getLjaddress());
                        tvRcl.setText(sp.getLjnum() == null ? "0" : sp.getLjnum());
                        tvQt.setText(sp.getOthersb() == null ? "" : sp.getOthersb());
                        tvContent.setText(sp.getOtherinfo() == null ? "" : sp.getOtherinfo());

                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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

    private void getSxgzById() {
        String url = ConstantUtil.BASE_URL + "/gridSxgz/queryListByGIdAndType";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        params.put("sxtype", String.valueOf(type));
        VolleyRequest.RequestPost(this, url, "queryListByGIdAndType", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = result.getJSONArray("data");
                                List<GridSxgz> sfxzs = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<GridSxgz>>() {
                                        }.getType());
                                if (sfxzs.size() > 0) {
                                    mContent.setText(sfxzs.get(0).getContent());
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(GridSxgzDetailActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(GridSxgzDetailActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back, R.id.dj_dys, R.id.dj_kndys, R.id.mz_ncdb, R.id.mz_csdb, R.id
            .mz_cjr, R.id.village_mz_cjr_1, R.id.village_mz_cjr_2, R.id.village_mz_cjr_3, R.id
            .village_mz_cjr_4, R.id.village_mz_cjr_5, R.id.mz_lset, R.id.mz_yfdx, R.id.mz_tkgy, R
            .id.village_mz_tkgy_1, R.id.village_mz_tkgy_2, R.id.village_mz_tkgy_3, R.id
            .village_mz_tkgy_4, R.id.mz_zlj, R.id.village_mz_zlj_1, R.id.village_mz_zlj_2, R.id
            .village_mz_zlj_3, R.id.sfxz_djy, R.id.sfxz_xxy, R.id.sfxz_xcy, R.id.sfxz_zyz, R.id
            .sfxz_flgw, R.id.qygz_ltqy, R.id.qygz_myqy, R.id.qygz_jtqy, R.id.xshs_qy, R.id
            .xshs_gt, R.id.aqsc_ltqy, R.id.aqsc_myqy, R.id.aqsc_jyyhbzs, R.id.aqsc_rpzf, R.id
            .aqsc_dzpjgzf, R.id.aqsc_jzgd, R.id.aqsc_tzsb, R.id.aqsc_wxhxp, R.id.aqsc_cpy, R.id
            .aqsc_xfaq, R.id.jhsy_ylfns, R.id.jhsy_dszn, R.id.jhsy_jlfz, R.id.jhsy_jlfz_sf, R.id
            .jhsy_jlfz_sd, R.id.jhsy_jlfz_sc, R.id.jhsy_ldrk, R.id.jhsy_jskn, R.id.cscxgl_dlts, R
            .id.cscxgl_htts, R.id.cscxgl_ljqyy, R.id.cscxgl_ldbjy, R.id.village_v_xqsq, R.id
            .sxgz_gzdt_btn, R.id.xfwd_zdrq, R.id.sfxz_jsb, R.id.sfxz_zdqsn, R.id.sfxz_sqfx, R.id
            .sfxz_sqjdry, R.id.sfxz_xjry, R.id.sfxz_azbj, R.id.dj_zddzz, R.id.dj_lxdzz, R.id.dj_lsdzz,R.id.cscxgl_bjxx,R.id.cscxgl_htgq,R.id.village_cscxgl_jdwly
    ,R.id.village_cscxgl_cjwly}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.village_cscxgl_jdwly:
                intent.setClass(GridSxgzDetailActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 65);
                startActivity(intent);
                break;
            case R.id.village_cscxgl_cjwly:
                intent.setClass(GridSxgzDetailActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 66);
                startActivity(intent);
                break;
            case R.id.cscxgl_bjxx:
                //背街小巷
//                intent.setClass(GridSxgzDetailActivity.this, BjxxActivity.class);
//                startActivity(intent);
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 17);
                startActivity(intent);
                break;
            case R.id.cscxgl_htgq:
                //河塘沟渠
//                intent.setClass(GridSxgzDetailActivity.this, HdqgActivity.class);
//                startActivity(intent);
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 18);
                startActivity(intent);
                break;
            case R.id.dj_zddzz:
                intent.setClass(GridSxgzDetailActivity.this, DzzListActivity.class);
                intent.putExtra("gId", gridId);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.dj_lxdzz:
                intent.setClass(GridSxgzDetailActivity.this, DzzListActivity.class);
                intent.putExtra("gId", gridId);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.dj_lsdzz:
                intent.setClass(GridSxgzDetailActivity.this, DzzListActivity.class);
                intent.putExtra("gId", gridId);
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
            case R.id.sfxz_jsb:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 57);
                    intent.putExtra("isFrom", "index");
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_zdqsn:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("type", 58);
                    intent.putExtra("isFrom", "index");
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_sqfx:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 61);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_sqjdry:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 59);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_xjry:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 60);
                    startActivity(intent);
                }
                break;
            case R.id.sfxz_azbj:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                    intent.putExtra("gridId", gridId);
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("type", 62);
                    startActivity(intent);
                }
                break;
            case R.id.xfwd_zdrq:
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 56);
                intent.putExtra("isFrom", "index");
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.sxgz_gzdt_btn:
                intent.setClass(GridSxgzDetailActivity.this, GridGzdtActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.dj_dys:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("isFrom", "index");
                intent.putExtra("dyldType", "1");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.dj_kndys:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("isFrom", "index");
//                intent.putExtra("dyType", "02");
                intent.putExtra("dyldType", "2");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_ncdb:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_csdb:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_cjr:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 6);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_cjr_1:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 8);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_cjr_2:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 9);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_cjr_3:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 10);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_cjr_4:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 11);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                break;
            case R.id.village_mz_cjr_5:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 12);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_lset:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 13);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_yfdx:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 14);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_tkgy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 15);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_tkgy_1:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 33);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_tkgy_2:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 34);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_tkgy_3:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 35);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_tkgy_4:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 36);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.mz_zlj:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 16);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_zlj_1:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 37);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_zlj_2:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 38);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.village_mz_zlj_3:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 39);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                break;
            case R.id.sfxz_djy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 21);
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.sfxz_xxy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 22);
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.sfxz_xcy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 23);
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.sfxz_zyz:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 24);
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.sfxz_flgw:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 25);
                intent.putExtra("gridId", gridId);
                startActivity(intent);
                break;
            case R.id.qygz_ltqy:
//                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
//                intent.putExtra("isFrom", "index");
//                intent.putExtra("type", 40);
//                intent.putExtra("gridId", gridId);
//                startActivity(intent);
                intent.setClass(GridSxgzDetailActivity.this, QylbActivity.class);
                intent.putExtra("lt", lt);
                intent.putExtra("my", my);
                intent.putExtra("jt", jt);
                intent.putExtra("zb", zb);
                intent.putExtra("jz", jz);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
            case R.id.qygz_myqy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 5);
                intent.putExtra("gridId", gridId);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
            case R.id.qygz_jtqy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 42);
                intent.putExtra("gridId", gridId);
                intent.putExtra("isYjfw", "0");
                startActivity(intent);
                break;
            case R.id.xshs_qy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
            case R.id.xshs_gt:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 5);
                startActivity(intent);
                break;
            case R.id.aqsc_ltqy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 40);
                intent.putExtra("xfaqType", 2);
                intent.putExtra("isYjfw", "1");
                startActivity(intent);
                break;
            case R.id.aqsc_myqy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 41);
                intent.putExtra("xfaqType", 2);
                intent.putExtra("isYjfw", "1");
                startActivity(intent);
                break;
            case R.id.aqsc_jyyhbzs:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 43);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_rpzf:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 44);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_dzpjgzf:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 45);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_jzgd:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 46);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_tzsb:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 47);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_wxhxp:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 48);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_cpy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 49);
                intent.putExtra("xfaqType", 2);
                startActivity(intent);
                break;
            case R.id.aqsc_xfaq:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("xfaqType", 1);
                intent.putExtra("isYjfw", "1");
                startActivity(intent);
                break;
            case R.id.jhsy_ylfns:
                intent.setClass(GridSxgzDetailActivity.this, ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.jhsy_dszn:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 26);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 27);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz_sf:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 28);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz_sd:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 29);
                startActivity(intent);
                break;
            case R.id.jhsy_jlfz_sc:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 30);
                startActivity(intent);
                break;
            case R.id.jhsy_ldrk:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 31);
                startActivity(intent);
                break;
            case R.id.jhsy_jskn:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 32);
                startActivity(intent);
                break;
            case R.id.cscxgl_dlts:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 17);
                startActivity(intent);
                break;
            case R.id.cscxgl_htts:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 18);
                startActivity(intent);
                break;
            case R.id.cscxgl_ljqyy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 19);
                startActivity(intent);
                break;
            case R.id.cscxgl_ldbjy:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 20);
                startActivity(intent);
                break;
            case R.id.village_v_xqsq:
                intent.setClass(GridSxgzDetailActivity.this, ListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("gridId", gridId);
                intent.putExtra("type", 7);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryListByGIdAndType");
        BaseApplication.getRequestQueue().cancelAll("queryListGrid");
    }
}

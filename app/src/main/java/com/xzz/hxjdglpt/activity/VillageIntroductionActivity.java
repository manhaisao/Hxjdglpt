package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.BusinessJob;
import com.xzz.hxjdglpt.model.CityManage;
import com.xzz.hxjdglpt.model.FamilyPlanning;
import com.xzz.hxjdglpt.model.Fwzw;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.GridTj;
import com.xzz.hxjdglpt.model.Minzheng;
import com.xzz.hxjdglpt.model.PartyBuilding;
import com.xzz.hxjdglpt.model.SafeProduce;
import com.xzz.hxjdglpt.model.Sfxz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.VillageSxgzInfo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 村简介
 * Created by dbz on 2017/6/6.
 */
@ContentView(R.layout.aty_village_introduction)
public class VillageIntroductionActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.v_intro_content)
    private TextView tvContent;
    @ViewInject(R.id.v_intro_cxgl)
    private TextView tvCxgl;
    @ViewInject(R.id.v_intro_aqsc)
    private TextView tvAqsc;
    @ViewInject(R.id.v_intro_jhsy)
    private TextView tvJhsy;
    @ViewInject(R.id.v_intro_qygz)
    private TextView tvQygz;
    @ViewInject(R.id.v_intro_mz)
    private TextView tvMz;
    @ViewInject(R.id.v_intro_sfxz)
    private TextView tvSfxz;
    @ViewInject(R.id.v_intro_dj)
    private TextView tvDj;
    @ViewInject(R.id.v_intro_xshs)
    private TextView tvXshs;
    @ViewInject(R.id.v_intro_xfwd)
    private TextView tvXfwd;
    @ViewInject(R.id.v_intro_fwzw)
    private TextView tvFwzw;

    private User user;

    private Village village;

    private List<GridTj> grids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        village = getIntent().getParcelableExtra("village");
        initView();
        initData();
    }

    public void initView() {
        if (village != null) {
            tvTitle.setText(village.getName());
        }
    }

    public void initData() {
//        loadGrid(village.getId());
        loadCscxglByGid();
        loadAqscByGid();
        loadQygzByGid();
        loadJhsyDByGid();
        loadMzByGid();
        loadSfxzByGid();
        loadDjByGid();
        loadXshsByGid();
        loadXfwdByGid();
        loadFwzfByGid();
    }

    private void loadCscxglByGid() {
        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cscxgl/queryCscxglByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryCscxglById", params, new
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
                        Gson gson = new Gson();
                        CityManage sp = (CityManage) gson.fromJson(result.getJSONObject("data")
                                .toString(), CityManage.class);
                        String zrr = sp.getZrr() == null ? "暂无" : sp.getZrr();
                        String zrrPhone = sp.getZrrphone() == null ? "暂无" : sp.getZrrphone();
                        String area = "0";
                        try {
                            Double a = Double.valueOf(sp.getArea());
                            java.text.DecimalFormat df = new java.text.DecimalFormat("######0.000");
                            area = String.valueOf(df.format(a));
                        } catch (Exception e) {
                        }
                        String hu = sp.getHs() == null ? "0" : sp.getHs();
                        String rk = sp.getRk() == null ? "0" : sp.getRk();
                        String dl = sp.getDaolu() == null ? "0" : sp.getDaolu();
                        String ht = sp.getHetang() == null ? "" : sp.getHetang();
                        int ljqyy = sp.getLjclear();
                        String ldbjy = sp.getLiudong() == null ? "0" : sp.getLiudong();
                        String ljc = sp.getLjc() == null ? "0" : sp.getLjc();
                        String ljt = sp.getLjt() == null ? "0" : sp.getLjt();
                        String ljysd = sp.getLjaddress() == null ? "0" : sp.getLjaddress();
                        String rcl = sp.getLjnum() == null ? "0" : sp.getLjnum();
                        StringBuffer sb1 = new StringBuffer();
                        sb1.append("城市长效管理工作责任人").append(zrr).append("、电话").append(zrrPhone)
                                .append("；面积").append(area).append("平方公里；共有").append(hu).append
                                ("户，").append(rk).append("人口。网格内共有").append(dl).append("道路，")
                                .append(ht).append("河塘；网格内垃圾清运员有").append(ljqyy).append
                                ("个；流动保洁员有").append(ldbjy).append("个；网格内垃圾桶共有").append(ljt)
                                .append("个、垃圾池").append(ljc).append("个，日产垃圾").append(rcl).append
                                ("吨，垃圾输送点有").append(ljysd).append("个。");
                        tvCxgl.setText(sb1.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadAqscByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/aqsc/querySafeProduceByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryAqscById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        SafeProduce sp = (SafeProduce) gson.fromJson(result.getJSONObject("data")
                                .toString(), SafeProduce.class);

                        String zrr = sp.getZrr() == null ? "暂无" : sp.getZrr();
                        String zrrPhone = sp.getZrrphone() == null ? "暂无" : sp.getZrrphone();
                        int cpy = sp.getCheng();
                        int dou = sp.getDou();
                        int jian = sp.getJian();
                        int lt = sp.getLtqy();
                        int my = sp.getMyqy();
                        int rou = sp.getRou();
                        int te = sp.getTe();
                        int wei = sp.getWei();
                        int yan = sp.getYan();
                        StringBuffer sb1 = new StringBuffer();
                        sb1.append("应急服务工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内列统企业有").append(lt).append("个、民营企业有").append(my).append
                                ("个、经营烟花爆竹商户").append(yan).append("个、肉皮作坊").append(rou).append
                                ("个、豆制品加工").append(dou).append("个、建筑工地").append(jian).append
                                ("个、特种设备").append(te).append("个、危险化学品").append(wei).append
                                ("个、成品油").append(cpy).append("个。");
                        tvAqsc.setText(sb1.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadJhsyDByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jhsy/queryJhsyByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryJhsyById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        FamilyPlanning sp = (FamilyPlanning) gson.fromJson(result.getJSONObject
                                ("data").toString(), FamilyPlanning.class);
                        String zrr = sp.getHead() == null ? "暂无" : sp.getHead();
                        String zrrPhone = sp.getHeadphone() == null ? "暂无" : sp.getHeadphone();
                        int ylfn = sp.getYlfnnum();//育龄妇女
                        int dszn = sp.getOnechildfamilynum();//独生子女
                        int sf = sp.getShengfNum();//省扶
                        int shif = sp.getShifNum();//市扶
                        int sd = sp.getSdNum();//失独
                        int sc = sp.getScNum();//伤残
                        int ldrk = sp.getLdrknum();//流动人口
                        int jskn = sp.getJsknnum();//计生困难
                        StringBuffer sb1 = new StringBuffer();
                        sb1.append("卫生健康工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内育龄妇女共有").append(ylfn).append("个、独生子女共有").append(dszn)
                                .append("户，省扶有").append(sf).append("个、市扶有").append(shif).append
                                ("个、失独有").append(sd).append("个、伤残有").append(sc).append("个，流动人口")
                                .append(ldrk).append("个，计生困难户").append(jskn).append("户。");
                        tvJhsy.setText(sb1.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadQygzByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qygz/queryBusinessByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryQygzById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        BusinessJob sp = (BusinessJob) gson.fromJson(result.getJSONObject("data")
                                .toString(), BusinessJob.class);
                        String zrr = sp.getHeadName() == null ? "暂无" : sp.getHeadName();
                        String zrrPhone = sp.getHeadPhone() == null ? "暂无" : sp.getHeadPhone();
                        int jt = sp.getJt();
                        int lt = sp.getLt();
                        int my = sp.getMy();
                        StringBuffer sb = new StringBuffer();
                        sb.append("企业服务工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内共有列统企业").append(lt).append("个、民营企业").append(my).append
                                ("个、集体企业").append(jt).append("个。");
                        tvQygz.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadMzByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/mz/queryMzByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryMzByGId", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Minzheng sp = (Minzheng) gson.fromJson(result.getJSONObject("data")
                                .toString(), Minzheng.class);
                        String zrr = sp.getZrr() == null ? "暂无" : sp.getZrr();
                        String zrrPhone = sp.getZrrphone() == null ? "暂无" : sp.getZrrphone();
                        String ncdb = sp.getNcdbhs() == null ? "0" : String.valueOf(sp.getNcdbhs());
                        String csdb = sp.getCsdbhs() == null ? "0" : String.valueOf(sp.getCsdbhs());
                        String cjr = sp.getCjrnum() == null ? "0" : String.valueOf(sp.getCjrnum());
                        String lset = sp.getLsrtnum() == null ? "0" : String.valueOf(sp
                                .getLsrtnum());
                        String yfdx = sp.getYfdx() == null ? "0" : String.valueOf(sp.getYfdx());
                        String tkgy = String.valueOf(sp.getTkgyNum());
                        String zlj = String.valueOf(sp.getZljNum());
                        StringBuffer sb = new StringBuffer();
                        sb.append("民政工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内农村低保户有").append(ncdb).append("个、城镇低保户有").append(csdb)
                                .append("个、残疾人").append(cjr).append("个、留守儿童").append(lset).append
                                ("个、优抚对象").append(yfdx).append("个、特困(五保)供养有").append(tkgy).append
                                ("个，80岁以上尊老金有").append(zlj).append("个。");
                        tvMz.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadFwzfByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/fwzw/queryFwzwByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryFwzwById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Fwzw sp = (Fwzw) gson.fromJson(result.getJSONObject("data").toString(),
                                Fwzw.class);
                        String zrr = sp.getZrr() == null ? "暂无" : sp.getZrr();
                        String zrrPhone = sp.getZrrphone() == null ? "暂无" : sp.getZrrphone();
                        String jddh = sp.getJdphone() == null ? "" : sp.getJdphone();
                        StringBuffer sb = new StringBuffer();
                        sb.append("综合执法工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("综合执法监督电话").append(jddh);
                        tvFwzw.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadSfxzByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sfxz/querySfxzByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "querySfxzById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Sfxz sp = (Sfxz) gson.fromJson(result.getJSONObject("data").toString(),
                                Sfxz.class);
                        String zrr = sp.getHeadname() == null ? "暂无" : sp.getHeadname();
                        String zrrPhone = sp.getHeadphone() == null ? "暂无" : sp.getHeadphone();
                        int zdqsn = sp.getZdqsn();
                        int xj = sp.getXjry();
                        int sqfx = sp.getSqfsman();
                        int sqjd = sp.getSqjd();
                        int azbj = sp.getAzbjry();
                        int jsb = sp.getZsjsb();
                        int djy = sp.getMdjfdjy();
                        int flgw = sp.getFlgw();
                        int xcy = sp.getFzxcy();
                        int xxy = sp.getMdjfxxy();
                        int zyz = sp.getPfzyz();
                        StringBuffer sb = new StringBuffer();
                        sb.append("政法综治工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内肇事肇祸精神病患者共有").append(jsb).append("个、重点青少年").append(zdqsn)
                                .append("个、社区戒毒人员").append(sqjd).append("个、邪教人员").append(xj)
                                .append("个、社区服刑人员").append(sqfx).append("个、安置帮教人员").append(azbj)
                                .append("个、法制宣传员").append(xcy).append("个、普法志愿者").append(zyz)
                                .append("个、法律顾问").append(flgw).append("个、矛盾纠纷调解员").append(djy)
                                .append("个、矛盾纠纷信息员").append(xxy).append("个。");
                        tvSfxz.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadDjByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dj/queryPartyBuildingByVid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryPartyBuildingByGid", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

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
                        String zrr = sp.getZrr() == null ? "暂无" : sp.getZrr();
                        String zrrPhone = sp.getZrrphone() == null ? "暂无" : sp.getZrrphone();
                        String dxz = sp.getDxznum() == null ? "" : sp.getDxznum();
                        String dys = sp.getDnum() == null ? "" : sp.getDnum();
                        String kndy = sp.getKndynum() == null ? "" : sp.getKndynum();
                        StringBuffer sb = new StringBuffer();
                        sb.append("党建工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内共有").append(dxz).append("个党小组，共有").append(dys).append
                                ("个党员，共有").append(kndy).append("个困难党员。");
                        tvDj.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadXshsByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/queryXshsByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryXshsById", params, new VolleyListenerInterface
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
                        String zrr = sp.getZrr() == null ? "暂无" : sp.getZrr();
                        String zrrPhone = sp.getZrrphone() == null ? "暂无" : sp.getZrrphone();
                        String hsy = sp.getHsy() == null ? "暂无" : sp.getHsy();
                        String hsyPhone = sp.getHsyphone() == null ? "暂无" : sp.getHsyphone();
                        String gt = String.valueOf(sp.getGeti());
                        String qy = String.valueOf(sp.getQiye());
                        StringBuffer sb = new StringBuffer();
                        sb.append("协税护税工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；协税员").append(hsy).append("、电话").append(hsyPhone).append
                                ("；网格内共有企业").append(qy).append("个、个体工商户").append(gt).append("个。");
                        tvXshs.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    private void loadXfwdByGid() {
//        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
// SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xfwd/queryXfwdByVId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", village.getId());
        VolleyRequest.RequestPost(this, url, "queryXfwdlById", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Xfwd sp = (Xfwd) gson.fromJson(result.getJSONObject("data").toString(),
                                Xfwd.class);
                        String zrr = sp.getHeadName() == null ? "暂无" : sp.getHeadName();
                        String zrrPhone = sp.getHeadPhone() == null ? "暂无" : sp.getHeadPhone();
                        String ajy = sp.getBaMan() == null ? "暂无" : sp.getBaMan();
                        String ajyphone = sp.getBaManPhone() == null ? "暂无" : sp.getBaManPhone();
                        String zdrq = sp.getZdrq() == null ? "" : sp.getZdrq();
                        StringBuffer sb = new StringBuffer();
                        sb.append("和谐社区工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；包案人").append(ajy).append("、电话").append(ajyphone).append
                                ("；网格内信访诉求人员共有").append(zdrq).append("个。");
                        tvXfwd.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private void loadGrid(String vId) {
        SuccinctProgress.showSuccinctProgress(VillageIntroductionActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/grid/tjGrid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", vId);
        VolleyRequest.RequestPost(this, url, "grid_queryList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        grids = gson.fromJson(jsonArray.toString(), new TypeToken<List<GridTj>>() {
                        }.getType());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageIntroductionActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageIntroductionActivity.this, R.string.load_fail);
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


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("grid_queryList");

        BaseApplication.getRequestQueue().cancelAll("queryCscxglById");
        BaseApplication.getRequestQueue().cancelAll("queryAqscById");
        BaseApplication.getRequestQueue().cancelAll("queryJhsyById");
        BaseApplication.getRequestQueue().cancelAll("queryQygzById");
        BaseApplication.getRequestQueue().cancelAll("queryMzByGId");

        BaseApplication.getRequestQueue().cancelAll("queryFwzwById");
        BaseApplication.getRequestQueue().cancelAll("querySfxzById");
        BaseApplication.getRequestQueue().cancelAll("queryPartyBuildingByGid");
        BaseApplication.getRequestQueue().cancelAll("queryXshsById");
        BaseApplication.getRequestQueue().cancelAll("queryXfwdlById");
    }
}

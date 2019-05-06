package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
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
import com.xzz.hxjdglpt.model.Xfwd;
import com.xzz.hxjdglpt.model.Xshs;
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
 * 网格简介
 * Created by dbz on 2017/6/6.
 */
@ContentView(R.layout.aty_grid_sxgz)
public class GridSxgzActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.grid_sxgz_cxgl)
    private TextView tvCxgl;
    @ViewInject(R.id.grid_sxgz_aqsc)
    private TextView tvAqsc;
    @ViewInject(R.id.grid_sxgz_jhsy)
    private TextView tvJhsy;
    @ViewInject(R.id.grid_sxgz_qygz)
    private TextView tvQygz;
    @ViewInject(R.id.grid_sxgz_mz)
    private TextView tvMz;
    @ViewInject(R.id.grid_sxgz_sfxz)
    private TextView tvSfxz;
    @ViewInject(R.id.grid_sxgz_dj)
    private TextView tvDj;
    @ViewInject(R.id.grid_sxgz_xshs)
    private TextView tvXshs;
    @ViewInject(R.id.grid_sxgz_xfwd)
    private TextView tvXfwd;
    @ViewInject(R.id.grid_sxgz_fwzw)
    private TextView tvFwzw;

    private User user;

    private String gridId;

    private Grid grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        gridId = getIntent().getStringExtra("gridId");
        initView();
        initData();
    }

    public void initView() {
    }

    public void initData() {
        loadGrid(gridId);
    }

    private void loadGrid(String gId) {
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
                            tvTitle.setText(grid.getZm());
                        } else {
                            tvTitle.setText(gridId);
                        }
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
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view);
        }

    }

    private void loadCscxglByGid() {
        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cscxgl/queryCscxglById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
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
                                String area = sp.getArea() == null ? "0" : sp.getArea();
                                String hu = TextUtils.isEmpty(sp.getHs()) ? "0" : sp.getHs();
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
                                sb1.append("网格号").append(grid.getId());
                                if (!TextUtils.isEmpty(grid.getZm())) {
                                    sb1.append("（").append(grid.getZm()).append("）");
                                }
                                sb1.append("，城市长效管理工作责任人").append(zrr).append("、电话").append(zrrPhone)
                                        .append("；面积").append(area).append("平方公里；共有").append(hu).append
                                        ("户，").append(rk).append("人口。网格内共有");
                                String dlStr_1 = sb1.toString();
                                sb1.append(dl).append("条道路");
                                String dlStr_2 = sb1.toString();
                                sb1.append("，有河塘");
                                String htStr_1 = sb1.toString();
                                sb1.append(ht);
                                String htStr_2 = sb1.toString();
                                sb1.append("个；网格内垃圾清运员有");
                                String qyy_1 = sb1.toString();
                                sb1.append(ljqyy);
                                String qyy_2 = sb1.toString();
                                sb1.append("个；流动保洁员有");
                                String bjy_1 = sb1.toString();
                                sb1.append(ldbjy);
                                String bjy_2 = sb1.toString();
                                sb1.append("个；网格内垃圾桶共有").append(ljt).append("个、垃圾池").append(ljc).append
                                        ("个，日产垃圾").append(rcl).append("吨，垃圾输送点有").append(ljysd).append
                                        ("个。");
                                SpannableString span = new SpannableString(sb1.toString());
                                span.setSpan(new Clickable(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.putExtra("gridId", grid.getId());
                                        intent.putExtra("type", 17);
                                        intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                        startActivity(intent);
                                    }
                                }), dlStr_1.length() - 1, dlStr_2.length(), Spanned.SPAN_MARK_MARK);
                                span.setSpan(new Clickable(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.putExtra("gridId", grid.getId());
                                        intent.putExtra("type", 18);
                                        intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                        startActivity(intent);
                                    }
                                }), htStr_1.length() - 3, htStr_2.length() + 1, Spanned.SPAN_MARK_MARK);
                                span.setSpan(new Clickable(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.putExtra("gridId", grid.getId());
                                        intent.putExtra("type", 19);
                                        intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                        startActivity(intent);
                                    }
                                }), qyy_1.length() - 6, qyy_2.length() + 1, Spanned.SPAN_MARK_MARK);
                                span.setSpan(new Clickable(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.putExtra("gridId", grid.getId());
                                        intent.putExtra("type", 20);
                                        intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                        startActivity(intent);
                                    }
                                }), bjy_1.length() - 6, bjy_2.length() + 1, Spanned.SPAN_MARK_MARK);
                                tvCxgl.setText(span);
                                tvCxgl.setMovementMethod(LinkMovementMethod.getInstance());
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(GridSxgzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/aqsc/queryAqscById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        sb1.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb1.append("（").append(grid.getZm()).append("）");
                        }
                        sb1.append("，应急服务工作责任人").append(zrr).append("、电话").append(zrrPhone)
                                .append("；网格内列统企业有");
                        String ltStr_1 = sb1.toString();
                        sb1.append(lt);
                        String ltStr_2 = sb1.toString();
                        sb1.append("个、民营企业有");
                        String myStr_1 = sb1.toString();
                        sb1.append(my);
                        String myStr_2 = sb1.toString();
                        sb1.append("个、经营烟花爆竹商户");
                        String yStr_1 = sb1.toString();
                        sb1.append(yan);
                        String yStr_2 = sb1.toString();
                        sb1.append("个、肉皮作坊");
                        String rStr_1 = sb1.toString();
                        sb1.append(rou);
                        String rStr_2 = sb1.toString();
                        sb1.append("个、豆制品加工");
                        String dStr_1 = sb1.toString();
                        sb1.append(dou);
                        String dStr_2 = sb1.toString();
                        sb1.append("个、建筑工地");
                        String jiStr_1 = sb1.toString();
                        sb1.append(jian);
                        String jiStr_2 = sb1.toString();
                        sb1.append("个、特种设备");
                        String tStr_1 = sb1.toString();
                        sb1.append(te);
                        String tStr_2 = sb1.toString();
                        sb1.append("个、危险化学品");
                        String wStr_1 = sb1.toString();
                        sb1.append(wei);
                        String wStr_2 = sb1.toString();
                        sb1.append("个、成品油");
                        String cStr_1 = sb1.toString();
                        sb1.append(cpy);
                        String cStr_2 = sb1.toString();
                        sb1.append("个。");

                        SpannableString span = new SpannableString(sb1.toString());
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 40);
                                intent.putExtra("isYjfw", "1");
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), ltStr_1.length() - 5, ltStr_2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 41);
                                intent.putExtra("isYjfw", "1");
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), myStr_1.length() - 5, myStr_2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 43);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), yStr_1.length() - 8, yStr_2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 44);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), rStr_1.length() - 4, rStr_2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 45);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), dStr_1.length() - 5, dStr_2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 46);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), jiStr_1.length() - 4, jiStr_2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 47);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), tStr_1.length() - 4, tStr_2.length() + 1, Spanned.SPAN_MARK_MARK);


                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 48);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), wStr_1.length() - 5, wStr_2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 48);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), cStr_1.length() - 3, cStr_2.length() + 1, Spanned.SPAN_MARK_MARK);
                        tvAqsc.setText(span);
                        tvAqsc.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jhsy/queryJhsyById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        sb1.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb1.append("（").append(grid.getZm()).append("）");
                        }
                        sb1.append("，卫生健康工作责任人").append(zrr).append("、电话").append(zrrPhone)
                                .append("；网格内育龄妇女共有");
                        String ylStr1 = sb1.toString();
                        sb1.append(ylfn);
                        String ylStr2 = sb1.toString();
                        sb1.append("个、独生子女共有");
                        String dsStr1 = sb1.toString();
                        sb1.append(dszn);
                        String dsStr2 = sb1.toString();
                        sb1.append("户，省扶有");
                        String sfStr1 = sb1.toString();
                        sb1.append(sf);
                        String sfStr2 = sb1.toString();
                        sb1.append("个、市扶有");
                        String shifStr1 = sb1.toString();
                        sb1.append(shif);
                        String shifStr2 = sb1.toString();
                        sb1.append("个、失独有");
                        String sdStr1 = sb1.toString();
                        sb1.append(sd);
                        String sdStr2 = sb1.toString();
                        sb1.append("个、伤残有");
                        String scStr1 = sb1.toString();
                        sb1.append(sc);
                        String scStr2 = sb1.toString();
                        sb1.append("个，流动人口");
                        String ldStr1 = sb1.toString();
                        sb1.append(ldrk);
                        String ldStr2 = sb1.toString();
                        sb1.append("个，计生困难户");
                        String jsStr1 = sb1.toString();
                        sb1.append(jskn);
                        String jsStr2 = sb1.toString();
                        sb1.append("户。");

                        SpannableString span = new SpannableString(sb1.toString());
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 3);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), ylStr1.length() - 6, ylStr2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 26);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), dsStr1.length() - 6, dsStr2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 27);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), sfStr1.length() - 3, sfStr2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 28);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), shifStr1.length() - 3, shifStr2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 29);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), sdStr1.length() - 3, sdStr2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 30);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), scStr1.length() - 3, scStr2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 31);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), ldStr1.length() - 4, ldStr2.length() + 1, Spanned.SPAN_MARK_MARK);


                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 32);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), jsStr1.length() - 5, jsStr2.length() + 1, Spanned.SPAN_MARK_MARK);
                        tvJhsy.setText(span);
                        tvJhsy.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qygz/queryQygzById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        sb.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb.append("（").append(grid.getZm()).append("）");
                        }
                        sb.append("，企业服务工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内共有列统企业");
                        String lt1 = sb.toString();
                        sb.append(lt);
                        String lt2 = sb.toString();
                        sb.append("个、民营企业");
                        String my1 = sb.toString();
                        sb.append(my);
                        String my2 = sb.toString();
                        sb.append("个、集体企业");
                        String jt1 = sb.toString();
                        sb.append(jt);
                        String jt2 = sb.toString();
                        sb.append("个。");

                        SpannableString span = new SpannableString(sb.toString());
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 40);
                                intent.putExtra("isYjfw", "0");
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), lt1.length() - 5, lt2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 41);
                                intent.putExtra("isYjfw", "0");
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), my1.length() - 4, my2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 42);
                                intent.putExtra("isYjfw", "0");
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), jt1.length() - 4, jt2.length() + 1, Spanned.SPAN_MARK_MARK);
                        tvQygz.setText(span);
                        tvQygz.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/mz/queryMzByGId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        sb.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb.append("（").append(grid.getZm()).append("）");
                        }
                        sb.append("，民政工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内农村低保户有");
                        String ncdb1 = sb.toString();
                        sb.append(ncdb);
                        String ncdb2 = sb.toString();
                        sb.append("个、城市低保户有");
                        String csdb1 = sb.toString();
                        sb.append(csdb);
                        String csdb2 = sb.toString();
                        sb.append("个、残疾人");
                        String cj1 = sb.toString();
                        sb.append(cjr);
                        String cj2 = sb.toString();
                        sb.append("个、留守儿童");
                        String ls1 = sb.toString();
                        sb.append(lset);
                        String ls2 = sb.toString();
                        sb.append("个、优抚对象");
                        String yf1 = sb.toString();
                        sb.append(yfdx);
                        String yf2 = sb.toString();
                        sb.append("个、特困(五保)供养有");
                        String tk1 = sb.toString();
                        sb.append(tkgy);
                        String tk2 = sb.toString();
                        sb.append("个，80岁以上尊老金有");
                        String zl1 = sb.toString();
                        sb.append(zlj);
                        String zl2 = sb.toString();
                        sb.append("个。");
                        SpannableString span = new SpannableString(sb.toString());
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 1);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), csdb1.length() - 6, csdb1.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 2);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), ncdb1.length() - 6, ncdb2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 6);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), cj1.length() - 3, cj2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 13);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), ls1.length() - 4, ls2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 14);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), yf1.length() - 4, yf2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 15);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), tk1.length() - 9, tk2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 16);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), zl1.length() - 9, zl2.length() + 1, Spanned.SPAN_MARK_MARK);
                        tvMz.setText(span);
                        tvMz.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/fwzw/queryFwzwById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        sb.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb.append("（").append(grid.getZm()).append("）");
                        }
                        sb.append("，综合执法工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("综合执法监督电话").append(jddh);
                        tvFwzw.setText(sb.toString());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sfxz/querySfxzById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        sb.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb.append("（").append(grid.getZm()).append("）");
                        }
                        sb.append("，政法综治工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；网格内肇事肇祸精神病患者共有").append(jsb).append("个、重点青少年").append(zdqsn)
                                .append("个、社区戒毒人员").append(sqjd).append("个、邪教人员").append(xj)
                                .append("个、社区服刑人员").append(sqfx).append("个、安置帮教人员").append(azbj)
                                .append("个、法制宣传员");
                        String xcy1 = sb.toString();
                        sb.append(xcy);
                        String xcy2 = sb.toString();
                        sb.append("个、普法志愿者");
                        String zyz1 = sb.toString();
                        sb.append(zyz);
                        String zyz2 = sb.toString();
                        sb.append("个、法律顾问");
                        String fl1 = sb.toString();
                        sb.append(flgw);
                        String fl2 = sb.toString();
                        sb.append("个、矛盾纠纷调解员");
                        String djy1 = sb.toString();
                        sb.append(djy);
                        String djy2 = sb.toString();
                        sb.append("个、矛盾纠纷信息员");
                        String xxy1 = sb.toString();
                        sb.append(xxy);
                        String xxy2 = sb.toString();
                        sb.append("个。");

                        SpannableString span = new SpannableString(sb.toString());
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 24);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), zyz1.length() - 5, zyz2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 23);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), xcy1.length() - 5, xcy2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 25);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), fl1.length() - 4, fl2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 21);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), djy1.length() - 7, djy2.length() + 1, Spanned.SPAN_MARK_MARK);

                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 22);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), xxy1.length() - 7, xxy2.length() + 1, Spanned.SPAN_MARK_MARK);
                        tvSfxz.setText(span);
                        tvSfxz.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dj/queryPartyBuildingByGid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", grid.getId());
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
                                sb.append("网格号").append(grid.getId());
                                if (!TextUtils.isEmpty(grid.getZm())) {
                                    sb.append("（").append(grid.getZm()).append("）");
                                }
                                sb.append("，党建工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                        ("；网格内共有").append(dxz).append("个党小组，共有");
                                String dy1 = sb.toString();
                                sb.append(dys);
                                String dy2 = sb.toString();
                                sb.append("个党员，共有").append(kndy).append("个困难党员。");

                                SpannableString span = new SpannableString(sb.toString());
                                span.setSpan(new Clickable(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.putExtra("gridId", grid.getId());
                                        intent.putExtra("type", 0);
                                        intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                        startActivity(intent);
                                    }
                                }), dy1.length() - 1, dy2.length() + 3, Spanned.SPAN_MARK_MARK);
                                tvDj.setText(span);
                                tvDj.setMovementMethod(LinkMovementMethod.getInstance());
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(GridSxgzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/queryXshsById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                        String hsy = TextUtils.isEmpty(sp.getHsy()) ? "暂无" : sp.getHsy();
                        String hsyPhone = TextUtils.isEmpty(sp.getHsyphone()) ? "暂无" : sp
                                .getHsyphone();
                        String gt = String.valueOf(sp.getGeti());
                        String qy = String.valueOf(sp.getQiye());
                        StringBuffer sb = new StringBuffer();
                        sb.append("网格号").append(grid.getId());
                        if (!TextUtils.isEmpty(grid.getZm())) {
                            sb.append("（").append(grid.getZm()).append("）");
                        }
                        sb.append("，协税护税工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                ("；协税员").append(hsy).append("、电话").append(hsyPhone).append
                                ("；网格内共有企业");
                        String qy1 = sb.toString();
                        sb.append(qy);
                        String qy2 = sb.toString();
                        sb.append("个、个体工商户");
                        String gt1 = sb.toString();
                        sb.append(gt);
                        String gt2 = sb.toString();
                        sb.append("个。");
                        SpannableString span = new SpannableString(sb.toString());
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 4);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), qy1.length() - 3, qy2.length() + 1, Spanned.SPAN_MARK_MARK);
                        span.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("gridId", grid.getId());
                                intent.putExtra("type", 5);
                                intent.setClass(GridSxgzActivity.this, ListActivity.class);
                                startActivity(intent);
                            }
                        }), gt1.length() - 5, gt2.length() + 1, Spanned.SPAN_MARK_MARK);
                        tvXshs.setText(span);
                        tvXshs.setMovementMethod(LinkMovementMethod.getInstance());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(GridSxgzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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
//        SuccinctProgress.showSuccinctProgress(GridSxgzActivity.this, "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xfwd/queryXfwdlById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
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
                                String ajy = TextUtils.isEmpty(sp.getBaMan()) ? "暂无" : sp.getBaMan();
                                String ajyphone = TextUtils.isEmpty(sp.getBaManPhone()) ? "暂无" : sp
                                        .getBaManPhone();
                                String zdrq = sp.getZdrq() == null ? "" : sp.getZdrq();
                                StringBuffer sb = new StringBuffer();
                                sb.append("网格号").append(grid.getId());
                                if (!TextUtils.isEmpty(grid.getZm())) {
                                    sb.append("（").append(grid.getZm()).append("）");
                                }
                                sb.append("，和谐社区工作责任人").append(zrr).append("、电话").append(zrrPhone).append
                                        ("；包案人").append(ajy).append("、电话").append(ajyphone).append
                                        ("；网格内信访诉求人员共有").append(zdrq).append("个。");
                                tvXfwd.setText(sb.toString());
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(GridSxgzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(GridSxgzActivity.this, R.string.load_fail);
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

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryGridById");
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

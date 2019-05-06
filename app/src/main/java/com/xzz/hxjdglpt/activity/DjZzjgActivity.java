package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.ViewPagerAdapter;
import com.xzz.hxjdglpt.customview.CustomViewPager;
import com.xzz.hxjdglpt.customview.DzzDialog;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Azbjry;
import com.xzz.hxjdglpt.model.Dangxiaozu;
import com.xzz.hxjdglpt.model.Djzz;
import com.xzz.hxjdglpt.model.Dxz;
import com.xzz.hxjdglpt.model.Dzb;
import com.xzz.hxjdglpt.model.Dzz;
import com.xzz.hxjdglpt.model.Liangxin;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.Zddw;
import com.xzz.hxjdglpt.model.Zhibu;
import com.xzz.hxjdglpt.model.Zongzhi;
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
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_dj_zzjg)
public class DjZzjgActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    protected LayoutInflater mInflater;
    private User user;
    private List<Role> roles;
    @ViewInject(R.id.dj_dzb_num)
    private TextView mDzb;//党支部数量
    @ViewInject(R.id.dj_dxz_num)
    private TextView mDxz;//党小组数量
    @ViewInject(R.id.dj_dy_num)
    private TextView mDys;//党员数量
    @ViewInject(R.id.dj_lddy_num)
    private TextView mLddy;//流动党员数量

    @ViewInject(R.id.cdzznum)
    private TextView mCdzz;//村党组织数量
    @ViewInject(R.id.zddzznum)
    private TextView mZddzz;//驻地党组织数量
    @ViewInject(R.id.lxdnum)
    private TextView mLxd;//两新党组织数量
    @ViewInject(R.id.shttnum)
    private TextView mShtt;//社会团体组织数量
    @ViewInject(R.id.fgqynum)
    private TextView mFgqy;//非公企业组织数量
    @ViewInject(R.id.dj_cdzzb)
    private CustomViewPager mViewPager;//村党总支或村党支部
    @ViewInject(R.id.dj_cdzb)
    private CustomViewPager mViewPager_zb;//村党支部或党小组
    @ViewInject(R.id.dj_zddzz)
    private ViewPager mViewPager_dw;//驻地单位党组织-党委
    //    @ViewInject(R.id.dj_zddzb)
//    private ViewPager mViewPager_dwdzb;//驻地单位党组织-党支部
    @ViewInject(R.id.dj_shtdzz)
    private ViewPager mViewPager_sh;//社会团体组织
    @ViewInject(R.id.dj_fgqyzz)
    private ViewPager mViewPager_fg;//非公企业组织
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPagerAdapter viewPagerAdapter_zb;
    private ViewPagerAdapter viewPagerAdapter_dw;
    //    private ViewPagerAdapter viewPagerAdapter_dwdzb;
    private ViewPagerAdapter viewPagerAdapter_sh;
    private ViewPagerAdapter viewPagerAdapter_fg;
    private List<View> items = new ArrayList<>();
    private List<View> items_zb = new ArrayList<>();
    //    private List<View> items_dwdzb = new ArrayList<>();
    private List<View> items_dw = new ArrayList<>();
    private List<View> items_fg = new ArrayList<>();
    private List<View> items_sh = new ArrayList<>();

    private Djzz djzz = null;

    private Context mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        mContent = this;
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        roles = application.getRolesList();
        viewPagerAdapter = new ViewPagerAdapter(items);
        viewPagerAdapter_zb = new ViewPagerAdapter(items_zb);
        viewPagerAdapter_dw = new ViewPagerAdapter(items_dw);
//        viewPagerAdapter_dwdzb = new ViewPagerAdapter(items_dwdzb);
        viewPagerAdapter_sh = new ViewPagerAdapter(items_sh);
        viewPagerAdapter_fg = new ViewPagerAdapter(items_fg);
        initView();
        initData();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            items_zb.clear();
            final Dzz d = (Dzz) msg.obj;
            if (d.getHasDzz() == 1) {
                LogUtil.i("党支部");
                List<Dzb> list = d.getDzb();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        final Dzb b = list.get(i);
                        View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_zb, false);
                        TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                        textView.setText(b.getDzbname());
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SuccinctProgress.showSuccinctProgress(DjZzjgActivity.this, "请求数据中···", SuccinctProgress
                                        .THEME_ULTIMATE, false, true);
                                String url = ConstantUtil.BASE_URL + "/dzz/getObject";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userId", user.getUserId());
                                params.put("token", user.getToken());
                                params.put("pId", String.valueOf(b.getBid()));
                                params.put("type", "3");
                                VolleyRequest.RequestPost(mContent, url, "getObject", params, new
                                        VolleyListenerInterface(mContent, VolleyListenerInterface.mListener,
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
                                                        Zhibu z = (Zhibu) gson.fromJson(result.getJSONObject("data").toString(),
                                                                Zhibu.class);
                                                        LogUtil.i("dzb===" + result.getJSONObject("data").toString());
                                                        DzzDialog dialog = new DzzDialog(mContext);
                                                        dialog.setObj(z);
                                                        dialog.setType(1);
//                                                        dialog.setVname(d.getVname());
                                                        dialog.show();
                                                    } else if ("2".equals(resultCode)) {
                                                        DialogUtil.showTipsDialog(DjZzjgActivity.this);
                                                    } else if ("3".equals(resultCode)) {
                                                        ToastUtil.show(DjZzjgActivity.this, R.string.load_fail);
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
                        });
                        items_zb.add(v);
                    }
                    viewPagerAdapter_zb.notifyDataSetChanged();
                } else {
                    View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_zb, false);
                    TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                    textView.setText("暂无党支部");
                    items_zb.add(v);
                    viewPagerAdapter_zb.notifyDataSetChanged();
                }
            } else {
                LogUtil.i("党小组");
                List<Dzb> dzbs = d.getDzb();
                if (dzbs != null && dzbs.size() > 0) {
                    List<Dxz> list = dzbs.get(0).getDxz();
                    for (int i = 0; i < list.size(); i++) {
                        final Dxz b = list.get(i);
                        View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_zb, false);
                        TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                        textView.setText(b.getXname());
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SuccinctProgress.showSuccinctProgress(DjZzjgActivity.this, "请求数据中···", SuccinctProgress
                                        .THEME_ULTIMATE, false, true);
                                String url = ConstantUtil.BASE_URL + "/dzz/getObject";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userId", user.getUserId());
                                params.put("token", user.getToken());
                                params.put("pId", String.valueOf(b.getXid()));
                                params.put("type", "5");
                                VolleyRequest.RequestPost(mContent, url, "getObject", params, new
                                        VolleyListenerInterface(mContent, VolleyListenerInterface.mListener,
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
                                                        Dangxiaozu z = (Dangxiaozu) gson.fromJson(result.getJSONObject("data").toString(),
                                                                Dangxiaozu.class);
                                                        LogUtil.i("dzb===" + result.getJSONObject("data").toString());
                                                        DzzDialog d = new DzzDialog(mContext);
                                                        d.setObj(z);
                                                        d.setType(2);
                                                        d.show();
                                                    } else if ("2".equals(resultCode)) {
                                                        DialogUtil.showTipsDialog(DjZzjgActivity.this);
                                                    } else if ("3".equals(resultCode)) {
                                                        ToastUtil.show(DjZzjgActivity.this, R.string.load_fail);
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
                        });
                        items_zb.add(v);
                        viewPagerAdapter_zb.notifyDataSetChanged();
                    }
                } else {
                    View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_zb, false);
                    TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                    textView.setText("暂无党小组");
                    items_zb.add(v);
                    viewPagerAdapter_zb.notifyDataSetChanged();
                }
            }
        }
    };

    public void initView() {
        tvTitle.setText("党建组织图");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.i("onPageScrolled");
                if (djzz.getcDzz() != null && djzz.getcDzz().size() > 0) {
                    Message msg = handler.obtainMessage();
                    msg.obj = djzz.getcDzz().get(position);
                    handler.sendMessage(msg);
                }
//                viewPagerAdapter_zb.notifyDataSetChanged();
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i("onPageSelected");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.i("onPageScrollStateChanged");
            }
        });
        mViewPager.setAdapter(viewPagerAdapter);


        mViewPager_zb.setAdapter(viewPagerAdapter_zb);


//        mViewPager_dw.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                LogUtil.i("onPageScrolled");
//                items_dwdzb.clear();
//                if (djzz.getzDzz() != null && djzz.getzDzz().size() > 0) {
//                    Dzz d = djzz.getzDzz().get(position);
//                    if (d.getHasDzz() == 1) {
//                        List<Dzb> list = d.getDzb();
//                        if (list.size() > 0) {
//                            for (int i = 0; i < list.size(); i++) {
//                                final Dzb b = list.get(i);
//                                View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_dwdzb, false);
//                                TextView textView = (TextView) v.findViewById(R.id.view_page_string);
//                                textView.setText(b.getDzbname());
//                                v.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        SuccinctProgress.showSuccinctProgress(DjZzjgActivity.this, "请求数据中···", SuccinctProgress
//                                                .THEME_ULTIMATE, false, true);
//                                        String url = ConstantUtil.BASE_URL + "/dzz/getObject";
//                                        Map<String, String> params = new HashMap<String, String>();
//                                        params.put("userId", user.getUserId());
//                                        params.put("token", user.getToken());
//                                        params.put("pId", String.valueOf(b.getBid()));
//                                        params.put("type", "3");
//                                        VolleyRequest.RequestPost(mContent, url, "getObject", params, new
//                                                VolleyListenerInterface(mContent, VolleyListenerInterface.mListener,
//                                                        VolleyListenerInterface.mErrorListener) {
//
//                                                    @Override
//                                                    public void onMySuccess(JSONObject result) {
//                                                        SuccinctProgress.dismiss();
//                                                        LogUtil.i("onMySuccess");
//                                                        try {
//                                                            String resultCode = result.getString("resultCode");
//                                                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
//                                                            if ("1".equals(resultCode)) {
//                                                                Gson gson = new Gson();
//                                                                Zhibu z = (Zhibu) gson.fromJson(result.getJSONObject("data").toString(),
//                                                                        Zhibu.class);
//                                                                LogUtil.i("dzb===" + result.getJSONObject("data").toString());
//                                                                DzzDialog d = new DzzDialog(mContext);
//                                                                d.setObj(z);
//                                                                d.setType(3);
//                                                                d.show();
//                                                            } else if ("2".equals(resultCode)) {
//                                                                DialogUtil.showTipsDialog(DjZzjgActivity.this);
//                                                            } else if ("3".equals(resultCode)) {
//                                                                ToastUtil.show(DjZzjgActivity.this, R.string.load_fail);
//                                                            }
//                                                        } catch (JSONException e) {
//                                                            SuccinctProgress.dismiss();
//                                                            e.printStackTrace();
//                                                        }
//
//                                                    }
//
//                                                    @Override
//                                                    public void onMyError(VolleyError error) {
//                                                        // 返回失败的原因
//                                                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
//                                                    }
//                                                });
//                                    }
//                                });
//                                items_dwdzb.add(v);
//                                viewPagerAdapter_dwdzb.notifyDataSetChanged();
//                            }
//                        } else {
//                            View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_dwdzb, false);
//                            TextView textView = (TextView) v.findViewById(R.id.view_page_string);
//                            textView.setText("暂无党支部");
//                            items_dwdzb.add(v);
//                            viewPagerAdapter_dwdzb.notifyDataSetChanged();
//                        }
//                    }
//                } else {
//                    View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager, false);
//                    TextView textView = (TextView) v.findViewById(R.id.view_page_string);
//                    textView.setText("暂无信息");
//                    items_dwdzb.add(v);
//                    viewPagerAdapter_dwdzb.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                LogUtil.i("onPageSelected");
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                LogUtil.i("onPageScrollStateChanged");
//            }
//        });
        mViewPager_dw.setAdapter(viewPagerAdapter_dw);
//
//        mViewPager_dwdzb.setAdapter(viewPagerAdapter_dwdzb);

        mViewPager_sh.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.i("onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i("onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.i("onPageScrollStateChanged");
            }
        });
        mViewPager_sh.setAdapter(viewPagerAdapter_sh);


        mViewPager_fg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.i("onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i("onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.i("onPageScrollStateChanged");
            }
        });
        mViewPager_fg.setAdapter(viewPagerAdapter_fg);
    }

    public void initData() {
        request();
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(DjZzjgActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dzz/queryDzzList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryDzzList", params, new
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
                                djzz = (Djzz) gson.fromJson(result.getJSONObject("data").toString(),
                                        Djzz.class);
                                if (djzz != null) {
                                    mCdzz.setText("村级党组织\n" + String.valueOf(djzz.getCjdzz()));
                                    mDzb.setText("党支部：" + String.valueOf(djzz.getDzbnum()));
                                    mDxz.setText("党小组：" + String.valueOf(djzz.getDxznum()));
                                    mDys.setText("党员总数：" + String.valueOf(djzz.getDynum()));
                                    mLddy.setText("流入党员数：" + String.valueOf(djzz.getLddynum()));
                                    if (djzz.getcDzz() != null) {
                                        getView(djzz.getcDzz());
                                    }
                                    if (djzz.getzDzz() != null && djzz.getzDzz().size() > 0) {
                                        getView_dw(djzz.getzDzz());
                                        mZddzz.setText("驻地单位党组织\n" + String.valueOf(djzz.getzDzz().size()));
                                    } else {
                                        mZddzz.setText("驻地单位党组织\n0");
                                        View v = mInflater.inflate(R.layout.view_page_item, mViewPager_dw, false);
                                        TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                                        textView.setText("暂无信息");
                                        items_dw.add(v);
                                        viewPagerAdapter_dw.notifyDataSetChanged();
                                    }
                                    int l = 0;
                                    int f = 0;
                                    if (djzz.getlDzb() != null) {
                                        l = djzz.getlDzb().size();
                                        mShtt.setText("社会团体组织\n" + String.valueOf(l));
                                        getView_sh(djzz.getlDzb());
                                    }
                                    if (djzz.getfDzb() != null) {
                                        f = djzz.getfDzb().size();
                                        mFgqy.setText("非公企业组织\n" + String.valueOf(f));
                                        getView_fg(djzz.getfDzb());
                                    }

                                    mLxd.setText("两新党组织\n" + String.valueOf(f + l));
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(DjZzjgActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(DjZzjgActivity.this, R.string.load_fail);
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

    private void getView(List<Dzz> list) {
        items.clear();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final Dzz d = list.get(i);
                if (d.getHasDzz() == 1) {
                    View v = mInflater.inflate(R.layout.view_page_item, mViewPager, false);
                    TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                    TextView textView1 = (TextView) v.findViewById(R.id.view_page_string_1);
                    textView.setText(d.getName());
                    textView1.setText("党总支");
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SuccinctProgress.showSuccinctProgress(DjZzjgActivity.this, "请求数据中···", SuccinctProgress
                                    .THEME_ULTIMATE, false, true);
                            String url = ConstantUtil.BASE_URL + "/dzz/getObject";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userId", user.getUserId());
                            params.put("token", user.getToken());
                            params.put("pId", String.valueOf(d.getId()));
                            params.put("type", "1");
                            VolleyRequest.RequestPost(mContent, url, "getObject", params, new
                                    VolleyListenerInterface(mContent, VolleyListenerInterface.mListener,
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
                                                    Zongzhi z = (Zongzhi) gson.fromJson(result.getJSONObject("data").toString(),
                                                            Zongzhi.class);
                                                    LogUtil.i("dzb===" + result.getJSONObject("data").toString());
                                                    DzzDialog d = new DzzDialog(mContext);
                                                    d.setObj(z);
                                                    d.setType(1);
                                                    d.show();
                                                } else if ("2".equals(resultCode)) {
                                                    DialogUtil.showTipsDialog(DjZzjgActivity.this);
                                                } else if ("3".equals(resultCode)) {
                                                    ToastUtil.show(DjZzjgActivity.this, R.string.load_fail);
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
                    });
                    items.add(v);
                } else {
                    List<Dzb> dzbs = d.getDzb();
                    if (dzbs != null && dzbs.size() > 0) {
                        final Dzb dd = dzbs.get(0);
                        View v = mInflater.inflate(R.layout.view_page_item, mViewPager, false);
                        TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                        TextView textView1 = (TextView) v.findViewById(R.id.view_page_string_1);
                        textView1.setText("党支部");
                        textView.setText(dd.getDzbname());
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SuccinctProgress.showSuccinctProgress(DjZzjgActivity.this, "请求数据中···", SuccinctProgress
                                        .THEME_ULTIMATE, false, true);
                                String url = ConstantUtil.BASE_URL + "/dzz/getObject";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userId", user.getUserId());
                                params.put("token", user.getToken());
                                params.put("pId", String.valueOf(dd.getBid()));
                                params.put("type", "4");
                                VolleyRequest.RequestPost(mContent, url, "getObject", params, new
                                        VolleyListenerInterface(mContent, VolleyListenerInterface.mListener,
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
                                                        Zhibu z = (Zhibu) gson.fromJson(result.getJSONObject("data").toString(),
                                                                Zhibu.class);
                                                        LogUtil.i("dzb===" + result.getJSONObject("data").toString());
                                                        DzzDialog d = new DzzDialog(mContext);
                                                        d.setObj(z);
                                                        d.setType(2);
                                                        d.show();
                                                    } else if ("2".equals(resultCode)) {
                                                        DialogUtil.showTipsDialog(DjZzjgActivity.this);
                                                    } else if ("3".equals(resultCode)) {
                                                        ToastUtil.show(DjZzjgActivity.this, R.string.load_fail);
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
                        });
                        items.add(v);
                    }
                }
            }
        } else {
            View v = mInflater.inflate(R.layout.view_page_item, mViewPager, false);
            TextView textView = (TextView) v.findViewById(R.id.view_page_string);
            textView.setText("暂无信息");
            items.add(v);
        }
        viewPagerAdapter.notifyDataSetChanged();
    }


    private void getView_dw(List<Zddw> list) {
        items_dw.clear();
        for (int i = 0; i < list.size(); i++) {
            final Zddw d = list.get(i);
            View v = mInflater.inflate(R.layout.view_page_item, mViewPager_dw, false);
            TextView textView = (TextView) v.findViewById(R.id.view_page_string);
            TextView textView1 = (TextView) v.findViewById(R.id.view_page_string_1);
            textView.setText(d.getName());
            if ("1".equals(d.getType())) {
                textView1.setText("党委");
            } else if ("2".equals(d.getType())) {
                textView1.setText("党支部");
            } else if ("3".equals(d.getType())) {
                textView1.setText("党小组");
            }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DzzDialog dialog = new DzzDialog(mContent);
                    dialog.setObj(d);
                    dialog.setType(Integer.valueOf(d.getType()));
                    dialog.show();
                }
            });
            items_dw.add(v);
        }
        viewPagerAdapter_dw.notifyDataSetChanged();
    }

    private void getView_sh(List<Liangxin> list) {
        items_sh.clear();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final Liangxin lx = list.get(i);
                View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_sh, false);
                TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                textView.setText(lx.getName());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DzzDialog d = new DzzDialog(mContent);
                        d.setObj(lx);
                        d.setType(Integer.valueOf(lx.getType()));
                        d.show();
                    }
                });
                items_sh.add(v);
            }
        } else {
            View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_sh, false);
            TextView textView = (TextView) v.findViewById(R.id.view_page_string);
            textView.setText("暂无信息");
            items_sh.add(v);
        }
        viewPagerAdapter_sh.notifyDataSetChanged();
    }

    private void getView_fg(List<Liangxin> list) {
        items_fg.clear();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final Liangxin lx = list.get(i);
                View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_fg, false);
                TextView textView = (TextView) v.findViewById(R.id.view_page_string);
                textView.setText(lx.getName());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DzzDialog d = new DzzDialog(mContent);
                        d.setObj(lx);
                        d.setType(Integer.valueOf(lx.getType()));
                        d.show();
                    }
                });
                items_fg.add(v);
            }
        } else {
            View v = mInflater.inflate(R.layout.view_page_item_1, mViewPager_fg, false);
            TextView textView = (TextView) v.findViewById(R.id.view_page_string);
            textView.setText("暂无信息");
            items_fg.add(v);
        }
        viewPagerAdapter_fg.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryDzzList");
        BaseApplication.getRequestQueue().cancelAll("getObject");
    }

    @Event(value = {R.id.iv_back}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

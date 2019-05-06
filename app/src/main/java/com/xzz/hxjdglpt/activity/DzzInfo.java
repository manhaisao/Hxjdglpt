package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Dzb;
import com.xzz.hxjdglpt.model.Dzz;
import com.xzz.hxjdglpt.model.Liangxin;
import com.xzz.hxjdglpt.model.LsDzz;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zddw;
import com.xzz.hxjdglpt.model.Zhibu;
import com.xzz.hxjdglpt.model.Zongzhi;
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
import java.util.List;
import java.util.Map;


/**
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_dzz_info)
public class DzzInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.jsbhz_info_btn)
    private Button jsbhz_info_btn;

    private User user;
    private TextView tlab1;
    private TextView lab1;
    private TextView tlab2;
    private TextView lab2;
    private TextView tlab3;
    private TextView lab3;
    private TextView tlab4;
    private TextView lab4;
    private TextView tlab5;
    private TextView lab5;
    private TextView tlab6;
    private TextView lab6;
    private TextView lab7;
    private TextView mGb;
    private TextView tlab8;
    private TextView lab8;

    private Object obj = null;

    private Context mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        obj = getIntent().getParcelableExtra("dzz");
        if (obj instanceof Zddw) {
//            tvTitle.setText("驻地党组织");
            tvTitle.setText(((Zddw) obj).getName());
        } else if (obj instanceof Liangxin) {
//            tvTitle.setText("两新党组织");
            tvTitle.setText(((Liangxin) obj).getName());
        } else if (obj instanceof Dzz) {
            Dzz dzz = (Dzz) obj;
            tvTitle.setText(dzz.getName());
        } else if (obj instanceof LsDzz) {
            tvTitle.setText(((LsDzz) obj).getName());
        }
        if (user == null) {
            user = application.getUser();
        }
        mContent = this;
        initView();
        initData();
    }

    public void initView() {
        tlab1 = (TextView) findViewById(R.id.dzz_t_lab1);
        lab1 = (TextView) findViewById(R.id.dzz_lab1);
        tlab2 = (TextView) findViewById(R.id.dzz_t_lab2);
        lab2 = (TextView) findViewById(R.id.dzz_lab2);
        tlab3 = (TextView) findViewById(R.id.dzz_t_lab3);
        lab3 = (TextView) findViewById(R.id.dzz_lab3);
        tlab4 = (TextView) findViewById(R.id.dzz_t_lab4);
        lab4 = (TextView) findViewById(R.id.dzz_lab4);
        tlab5 = (TextView) findViewById(R.id.dzz_t_lab5);
        lab5 = (TextView) findViewById(R.id.dzz_lab5);
        tlab6 = (TextView) findViewById(R.id.dzz_t_lab6);
        lab6 = (TextView) findViewById(R.id.dzz_lab6);
        lab7 = (TextView) findViewById(R.id.dzz_lab7);
        tlab8 = (TextView) findViewById(R.id.dzz_t_lab8);
        lab8 = (TextView) findViewById(R.id.dzz_lab8);
        if (obj instanceof Zddw) {
            Zddw d = (Zddw) obj;
            if ("1".equals(d.getType())) {
                tlab1.setText("党委书记：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("党委副书记：");
                lab3.setText(d.getFuname());
                tlab4.setText("联系号码：");
                lab4.setText(d.getFuphone());
                tlab5.setText("党委成员：");
                lab5.setText(d.getChengyuannum());
                tlab6.setText("所属网格：");
                lab6.setText(d.getGridId());
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                tlab8.setText("所属村居：");
                lab8.setText(d.getVname());
                lab7.setText("党委下设" + d.getDxznum() + "个党支部，共有党员" + d.getDynum() + "名");
            } else if ("2".equals(d.getType())) {
                tlab1.setText("支部书记：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("支部副书记：");
                lab3.setText(d.getFuname());
                tlab4.setText("联系号码：");
                lab4.setText(d.getFuphone());
                tlab5.setText("支部委员：");
                lab5.setText(d.getChengyuannum());
                tlab6.setText("所属网格：");
                lab6.setText(d.getGridId());
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                tlab8.setText("所属村居：");
                lab8.setText(d.getVname());
                lab7.setText("支部下设" + d.getDxznum() + "个党小组，共有党员" + d.getDynum() + "名");

            } else if ("3".equals(d.getType())) {
                tlab1.setText("组长：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("党员数：");
                lab3.setText(d.getDynum());
                tlab4.setVisibility(View.GONE);
                lab4.setVisibility(View.GONE);
                tlab4.setText("所属村居：");
                lab4.setText(d.getVname());
                tlab5.setText("所属网格：");
                lab5.setText(d.getGridId());
                tlab6.setVisibility(View.GONE);
                lab6.setVisibility(View.GONE);
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                lab7.setVisibility(View.GONE);
            }

        } else if (obj instanceof Liangxin) {
            Liangxin d = (Liangxin) obj;
            if (d.getType() == 2) {
                tlab1.setText("支部书记：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("支部副书记：");
                lab3.setText(d.getFuname());
                tlab4.setText("联系号码：");
                lab4.setText(d.getFuphone());
                tlab5.setText("支部委员：");
                lab5.setText(d.getChengyuannum());
                tlab6.setText("所属网格：");
                lab6.setText(d.getGridid());
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                tlab8.setText("所属村居：");
                lab8.setText(d.getVname());
                lab7.setText("支部下设" + d.getDxznum() + "个党小组，共有党员" + d.getDynum() + "名");
            } else if (d.getType() == 3) {
                tlab1.setText("组长：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("党员数：");
                lab3.setText(d.getDynum());
                tlab4.setVisibility(View.GONE);
                lab4.setVisibility(View.GONE);
                tlab4.setText("所属村居：");
                lab4.setText(d.getVname());
                tlab5.setText("所属网格：");
                lab5.setText(d.getGridid());
                tlab6.setVisibility(View.GONE);
                lab6.setVisibility(View.GONE);
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                lab7.setVisibility(View.GONE);
            }
        } else if (obj instanceof Dzz) {
            Dzz d = (Dzz) obj;
//            if (d.getHasDzz() == 1) {
            loadDzz(String.valueOf(d.getId()), 1);
//            } else {
//                List<Dzb> dzbs = d.getDzb();
//                if (dzbs != null && dzbs.size() > 0) {
//                    Dzb dd = dzbs.get(0);
//                    loadDzz(String.valueOf(dd.getBid()), 4);
//                }
//            }
        } else if (obj instanceof Dzb) {
            Dzb dd = (Dzb) obj;
            loadDzz(String.valueOf(dd.getBid()), 4);
        } else if (obj instanceof LsDzz) {
            LsDzz d = (LsDzz) obj;
            if (d.getType() == 2) {
                tlab1.setText("支部书记：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("支部副书记：");
                lab3.setText(d.getFuname());
                tlab4.setText("联系号码：");
                lab4.setText(d.getFuphone());
                tlab5.setText("支部委员：");
                lab5.setText(d.getChengyuannum());
                tlab6.setText("所属网格：");
                lab6.setText(d.getGridid());
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                tlab8.setText("所属村居：");
                lab8.setText(d.getVname());
                lab7.setText("支部下设" + d.getDxznum() + "个党小组，共有党员" + d.getDynum() + "名");
            } else if (d.getType() == 3) {
                tlab1.setText("组长：");
                lab1.setText(d.getZongname());
                tlab2.setText("联系号码：");
                lab2.setText(d.getZongphone());
                tlab3.setText("党员数：");
                lab3.setText(d.getDynum());
                tlab4.setVisibility(View.GONE);
                lab4.setVisibility(View.GONE);
                tlab4.setText("所属村居：");
                lab4.setText(d.getVname());
                tlab5.setText("所属网格：");
                lab5.setText(d.getGridid());
                tlab6.setVisibility(View.GONE);
                lab6.setVisibility(View.GONE);
                tlab8.setVisibility(View.GONE);
                lab8.setVisibility(View.GONE);
                lab7.setVisibility(View.GONE);
            }
        }
    }

    private void loadDzz(String pId, final int type) {
        SuccinctProgress.showSuccinctProgress(DzzInfo.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dzz/getObject";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pId", pId);
        params.put("type", String.valueOf(type));
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
                                if (type == 1) {
                                    Zongzhi d = (Zongzhi) gson.fromJson(result.getJSONObject("data").toString(),
                                            Zongzhi.class);
                                    tlab1.setText("总支书记：");
                                    lab1.setText(d.getZongname());
                                    tlab2.setText("联系号码：");
                                    lab2.setText(d.getZongphone());
                                    tlab3.setText("总支副书记：");
                                    lab3.setText(d.getFuname());
                                    tlab4.setText("联系号码：");
                                    lab4.setText(d.getFuphone());
                                    tlab5.setText("总支委员：");
                                    lab5.setText(d.getChengyuannum());
                                    tlab6.setText("所属村居：");
                                    lab6.setText(d.getVname());
                                    final String dnum = d.getDangzhibunum() == null ? "0" : d.getDangzhibunum();
                                    Dzz dzz = (Dzz) obj;
                                    int dynum = dzz.getDzzDynum();

                                    SpannableStringBuilder style = new SpannableStringBuilder();
                                    String s1 = "党总支下设";
                                    String s2 = s1 + dnum;
                                    String s3 = "个党支部，共有党员" + dynum + "名";
                                    //设置文字
                                    style.append(s2 + s3);
                                    //设置部分文字点击事件
                                    ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            if (Integer.valueOf(dnum) > 0) {
                                                Intent intent = new Intent();
                                                Bundle bundle = new Bundle();
                                                intent.setClass(DzzInfo.this, Dzz_DzbDxzListActivity.class);
                                                bundle.putParcelable("data", (Dzz) obj);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            ds.setUnderlineText(false);
                                        }
                                    };
                                    style.setSpan(clickableSpan, s1.length() - 3, s2.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    lab7.setText(style);

                                    //设置部分文字颜色
                                    ForegroundColorSpan foregroundColorSpan_s = new ForegroundColorSpan(Color.parseColor("#4f4f4f"));
                                    style.setSpan(foregroundColorSpan_s, s1.length() - 3, s2.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //设置部分文字颜色
                                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                                    style.setSpan(foregroundColorSpan, s1.length(), s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //设置下划线
                                    UnderlineSpan underlineSpan = new UnderlineSpan();
                                    style.setSpan(underlineSpan, s1.length(), s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //配置给TextView
                                    lab7.setMovementMethod(LinkMovementMethod.getInstance());
                                    lab7.setText(style);

//                                    lab7.setText("党总支下设" + dnum + "个党支部，共有党员" + dynum + "名");
                                    tlab8.setVisibility(View.GONE);
                                    lab8.setVisibility(View.GONE);
                                } else if (type == 4) {
                                    Zhibu d = (Zhibu) gson.fromJson(result.getJSONObject("data").toString(),
                                            Zhibu.class);
                                    tlab1.setText("支部书记：");
                                    lab1.setText(d.getZhibuname());
                                    tlab2.setText("联系号码：");
                                    lab2.setText(d.getZhifuphone());
                                    tlab3.setText("支部副书记：");
                                    lab3.setText(d.getFuname());
                                    tlab4.setText("联系号码：");
                                    lab4.setText(d.getFuphone());
                                    tlab5.setText("支部委员：");
                                    lab5.setText(d.getChengyuannum());
                                    tlab6.setText("所属网格：");
                                    lab6.setText(d.getGridid());
                                    tlab8.setText("所属村居：");
                                    lab8.setText(d.getVname());
                                    final String dnum = d.getXiaozunum() == null ? "0" : d.getXiaozunum();
                                    Dzb dzb = (Dzb) obj;
                                    int dynum = dzb.getDbzDynum();
//                                    String dynum = d.getDangyuannum() == null ? "0" : d.getDangyuannum();

                                    SpannableStringBuilder style = new SpannableStringBuilder();
                                    String s1 = "党支部下设";
                                    String s2 = s1 + dnum;
                                    String s3 = "个党小组，共有党员" + dynum + "名";
                                    //设置文字
                                    style.append(s2 + s3);
                                    //设置部分文字点击事件
                                    ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            if (Integer.valueOf(dnum) > 0) {
                                                Intent intent = new Intent();
                                                Bundle bundle = new Bundle();
                                                intent.setClass(DzzInfo.this, Dzz_DxzListActivity.class);
                                                bundle.putParcelable("data", (Dzb) obj);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            ds.setUnderlineText(false);
                                        }
                                    };
                                    style.setSpan(clickableSpan, s1.length() - 3, s2.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    lab7.setText(style);
                                    //设置部分文字颜色
                                    ForegroundColorSpan foregroundColorSpan_s = new ForegroundColorSpan(Color.parseColor("#4f4f4f"));
                                    style.setSpan(foregroundColorSpan_s, s1.length() - 3, s2.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //设置部分文字颜色
                                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                                    style.setSpan(foregroundColorSpan, s1.length(), s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //设置下划线
                                    UnderlineSpan underlineSpan = new UnderlineSpan();
                                    style.setSpan(underlineSpan, s1.length(), s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //配置给TextView
                                    lab7.setMovementMethod(LinkMovementMethod.getInstance());
                                    lab7.setText(style);
//                                    lab7.setText("党支部下设" + dnum + "个党小组，共有党员" + dynum + "名");
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(DzzInfo.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(DzzInfo.this, R.string.load_fail);
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

    public void initData() {
        jsbhz_info_btn.setText("党建工作动态");
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("getObject");
    }


    @Event(value = {R.id.iv_back, R.id.jsbhz_info_btn}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.jsbhz_info_btn:
                Intent intent = new Intent(DzzInfo.this, GzzdtActivity.class);
                if (obj instanceof Zddw) {
                    intent.putExtra("dyId", ((Zddw) obj).getId());
                } else if (obj instanceof Liangxin) {
                    intent.putExtra("dyId", ((Liangxin) obj).getId());
                } else if (obj instanceof Dzz) {
                    intent.putExtra("dyId", ((Dzz) obj).getId());
                } else if (obj instanceof LsDzz) {
                    intent.putExtra("dyId", ((LsDzz) obj).getId());
                }
                startActivity(intent);
                break;
        }
    }
}

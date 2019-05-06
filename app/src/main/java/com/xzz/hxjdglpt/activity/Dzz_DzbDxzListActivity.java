package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.GridViewAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Dangxiaozu;
import com.xzz.hxjdglpt.model.Dxz;
import com.xzz.hxjdglpt.model.Dzb;
import com.xzz.hxjdglpt.model.Dzz;
import com.xzz.hxjdglpt.model.Liangxin;
import com.xzz.hxjdglpt.model.Party;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zddw;
import com.xzz.hxjdglpt.model.Zhibu;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.StringUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

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
 * 展示信息LIST
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_dzz_dzb_dxz_list)
public class Dzz_DzbDxzListActivity extends BaseActivity {

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.dzz_dzb_dxz)
    private LinearLayout mLay;


    private User user;

    private Dzz obj = null;

    private StringBuffer strIds = new StringBuffer();

    private LayoutInflater layoutInflater;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        obj = getIntent().getParcelableExtra("data");
        mContext = this;
        layoutInflater = LayoutInflater.from(this);
        initView();
        initData();
    }

    public void initView() {

    }


    public void initData() {
        if (obj.getHasDzz() == 1) {
            List<Dzb> list = obj.getDzb();
            for (Dzb d : list) {
                strIds.append(d.getBid()).append(",");
            }
        } else {
            List<Dzb> dzbs = obj.getDzb();
            if (dzbs != null && dzbs.size() > 0) {
                List<Dxz> list = dzbs.get(0).getDxz();
                for (Dxz d : list) {
                    strIds.append(d.getXid()).append(",");
                }
            }
        }
        request();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<Zhibu> result = (List<Zhibu>) msg.obj;
                    for (Zhibu z : result) {
                        List<Dzb> dzbs = obj.getDzb();
                        Dzb dd = null;
                        for (Dzb d : dzbs) {
                            if (d.getBid() == z.getPid()) {
                                dd = d;
                                break;
                            }
                        }

                        View v = layoutInflater.inflate(R.layout.dzz_dzb_item, null);
                        TextView tvName = (TextView) v.findViewById(R.id.dzz_dzb_name);
                        tvName.setText(dd.getDzbname());

                        TextView tab1 = (TextView) v.findViewById(R.id.dzz_lab1);
                        tab1.setText(z.getZhibuname());

                        TextView tab2 = (TextView) v.findViewById(R.id.dzz_lab2);
                        tab2.setText(z.getZhifuphone());

                        TextView tab3 = (TextView) v.findViewById(R.id.dzz_lab3);
                        tab3.setText(z.getFuname());

                        TextView tab4 = (TextView) v.findViewById(R.id.dzz_lab4);
                        tab4.setText(z.getFuphone());

                        TextView tab5 = (TextView) v.findViewById(R.id.dzz_lab5);
                        tab5.setText(z.getChengyuannum());

                        TextView tab8 = (TextView) v.findViewById(R.id.dzz_lab8);
                        tab8.setText(z.getVname());

                        TextView tab6 = (TextView) v.findViewById(R.id.dzz_lab6);
                        tab6.setText(z.getGridid());

                        TextView tab7 = (TextView) v.findViewById(R.id.dzz_lab7);
                        final String dnum = z.getXiaozunum() == null ? "0" : z.getXiaozunum();
                        int dynum = dd.getDbzDynum();

                        SpannableStringBuilder style = new SpannableStringBuilder();
                        String s1 = "党支部下设";
                        String s2 = s1 + dnum;
                        String s3 = "个党小组，共有党员" + dynum + "名";
                        //设置文字
                        style.append(s2 + s3);
                        final Dzb zz = dd;
                        //设置部分文字点击事件
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                if (Integer.valueOf(dnum) > 0) {
                                    if (zz != null) {
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        intent.setClass(Dzz_DzbDxzListActivity.this, Dzz_DxzListActivity.class);
                                        bundle.putParcelable("data", zz);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                ds.setUnderlineText(false);
                            }
                        };
                        style.setSpan(clickableSpan, s1.length() - 3, s2.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tab7.setText(style);
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
                        tab7.setMovementMethod(LinkMovementMethod.getInstance());
                        tab7.setText(style);
//                        tab7.setText("党支部下设" + dnum + "个党小组，共有党员" + dynum + "名");

                        mLay.addView(v);
                    }
                    break;
                case 2:
                    List<Dangxiaozu> list = (List<Dangxiaozu>) msg.obj;
                    for (Dangxiaozu z : list) {

                        List<Dzb> dzbs = obj.getDzb();
                        Dxz dd = null;
                        if (dzbs != null && dzbs.size() > 0) {
                            List<Dxz> dxzs = dzbs.get(0).getDxz();
                            for (Dxz d : dxzs) {
                                if (d.getXid() != null && String.valueOf(d.getXid()).equals(z.getPid())) {
                                    dd = d;
                                    break;
                                }
                            }
                        }

                        View v = layoutInflater.inflate(R.layout.dzz_dxz_item, null);
                        TextView tvName = (TextView) v.findViewById(R.id.dzz_dxz_name);
                        if (!StringUtil.isEmpty(dd.getXname()))
                            tvName.setText(dd.getXname());

                        TextView tab1 = (TextView) v.findViewById(R.id.dzz_lab1);
                        tab1.setText(z.getName());

                        TextView tab2 = (TextView) v.findViewById(R.id.dzz_lab2);
                        tab2.setText(z.getPhone());

                        GridView tab3 = (GridView) v.findViewById(R.id.dzz_gridview);
                        GridViewAdapter adapter = new GridViewAdapter(Dzz_DzbDxzListActivity.this, tab3, z.getXzcy());
                        tab3.setAdapter(adapter);
                        final List<PartyMember> data = z.getXzcy();
                        tab3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                PartyMember p = (PartyMember) data.get(i);
                                intent.setClass(Dzz_DzbDxzListActivity.this, PartyMemberInfo.class);
                                bundle.putParcelable("partyMember", p);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        setGridViewHeight(tab3);
                        adapter.notifyDataSetChanged();
                        TextView tab4 = (TextView) v.findViewById(R.id.dzz_lab4);
                        tab4.setText(z.getGridid());

                        TextView tab5 = (TextView) v.findViewById(R.id.dzz_lab5);
                        tab5.setText(z.getWgy());

                        TextView tab8 = (TextView) v.findViewById(R.id.dzz_lab8);
                        tab8.setText(z.getWgyphone());

                        mLay.addView(v);
                    }
                    break;
            }
        }
    };

    public void setGridViewHeight(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Event(value = {R.id.iv_back_tv}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
        }
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(Dzz_DzbDxzListActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dzz/getZhibuOrXiaozuList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("ids", strIds.toString());
        params.put("hasDzz", String.valueOf(obj.getHasDzz()));
        VolleyRequest.RequestPost(this, url, "queryList", params, new
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
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                if (obj.getHasDzz() == 1) {
                                    tvTitle.setText("党支部");
                                    List<Zhibu> objs = gson.fromJson(jsonArray.toString(), new
                                            TypeToken<List<Zhibu>>() {
                                            }.getType());
                                    Message msg = handler.obtainMessage();
                                    msg.what = 1;
                                    msg.obj = objs;
                                    handler.sendMessage(msg);
                                } else if (obj.getHasDzz() == 0) {
                                    tvTitle.setText("党小组");
                                    List<Dangxiaozu> objs = gson.fromJson(jsonArray.toString(), new
                                            TypeToken<List<Dangxiaozu>>() {
                                            }.getType());
                                    Message msg = handler.obtainMessage();
                                    msg.what = 2;
                                    msg.obj = objs;
                                    handler.sendMessage(msg);
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(Dzz_DzbDxzListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(Dzz_DzbDxzListActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            SuccinctProgress.dismiss();
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
        BaseApplication.getRequestQueue().cancelAll("queryList");
    }


}

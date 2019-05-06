package com.xzz.hxjdglpt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.title.TitleDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.CpDetail;
import com.xzz.hxjdglpt.model.Cpbz;
import com.xzz.hxjdglpt.model.Qfhx;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019\3\25 0025.
 */

@ContentView(R.layout.aty_cpbz_detail)
public class CpbzDetailActivity extends BaseActivity {

    //标题
    @ViewInject(R.id.hx_title)
    TextView hx_title;

    //右侧菜单
    @ViewInject(R.id.hx_btn_right)
    ImageView hx_btn_right;
    @ViewInject(R.id.cpbb_detail_label)
    private TextView cpbb_detail_label;

    @ViewInject(R.id.ll_kcnr)
    private LinearLayout ll_kcnr;
    @ViewInject(R.id.ll_kcbz)
    private LinearLayout ll_kcbz;

    //    //图表
//    @ViewInject(R.id.table)
//    SmartTable table;
    Cpbz cpbz;
    private User user;
    private BaseApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        ll_kcbz.setVisibility(View.INVISIBLE);
        ll_kcnr.setVisibility(View.INVISIBLE);
        initView();
        initData();
    }

    public void initView() {
        cpbz = (Cpbz) getIntent().getSerializableExtra("top");
        hx_title.setText(cpbz.getStandardName());
        hx_btn_right.setVisibility(View.GONE);
        loadData(AutoListView.REFRESH);
    }

    public void initData() {

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {

        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AutoListView.REFRESH:
                    List<CpDetail> mlist = (List<CpDetail>) msg.obj;
                    ll_kcbz.addView(addTextView("考察标准"));
                    ll_kcnr.addView(addTextView("考察内容"));
                    if (mlist != null && mlist.size() != 0) {
                        for (CpDetail cpDetail : mlist) {
                            final TextView contentTV = addTextView(cpDetail.getInvestigateContent());
                            contentTV.setGravity(Gravity.LEFT);
                            final TextView contentSD = addTextView(cpDetail.getInvestigateStandard());
                            contentSD.setGravity(Gravity.LEFT);
                            ll_kcnr.addView(contentTV);
                            ll_kcbz.addView(contentSD);
                            ll_kcbz.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (contentTV.getHeight() >= contentSD.getHeight()) {
                                        contentSD.setHeight(contentTV.getHeight());
                                    } else {
                                        contentTV.setHeight(contentSD.getHeight());
                                    }
                                    ll_kcbz.setVisibility(View.VISIBLE);
                                    ll_kcnr.setVisibility(View.VISIBLE);
                                }
                            }, 300);
                        }
                    }
                    else
                    {
                        ll_kcbz.setVisibility(View.VISIBLE);
                        ll_kcnr.setVisibility(View.VISIBLE);
                    }


                    if (mlist != null && mlist.size() != 0) {
                        cpbb_detail_label.setText("备注：" + mlist.get(0).getRemark());
                    } else {
                        cpbb_detail_label.setText("备注：");
                    }
                    break;
                case AutoListView.LOAD:

                    break;
            }

        }
    };


    private int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        return height;
    }

    private TextView addTextView(String content) {
        TextView textView = new TextView(this);
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.text_black_color));
        textView.setGravity(Gravity.CENTER);
        textView.setText(content);
        textView.setPadding(10, 5, 10, 5);
        textView.setBackgroundResource(R.drawable.textview_cpbz);
        return textView;
    }


    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/evaluationDetails/" + cpbz.getId();
        VolleyRequest.RequestGet(this, url, "evaluationDetails", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("0".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("result");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<CpDetail> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<CpDetail>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(CpbzDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CpbzDetailActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
        }
    }
}

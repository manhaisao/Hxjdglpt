package com.xzz.hxjdglpt.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jly;
import com.xzz.hxjdglpt.model.User;
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
 * 残疾人员信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_jly_info)
public class JlyInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.jly_zfzr)
    private TextView edtZfzr;
    @ViewInject(R.id.jly_zfzrphne)
    private TextView edtZfzrPhone;

    @ViewInject(R.id.jly_gly)
    private TextView edtGly;
    @ViewInject(R.id.jly_glyphone)
    private TextView edtGlyPhone;

    @ViewInject(R.id.jly_aqy)
    private TextView edtAqy;
    @ViewInject(R.id.jly_aqyphone)
    private TextView edtAqyPhone;

    @ViewInject(R.id.jly_wsy)
    private TextView edtWsy;
    @ViewInject(R.id.jly_wsyphone)
    private TextView edtWsyPhone;

    @ViewInject(R.id.jly_hly)
    private TextView tvHly;
    @ViewInject(R.id.jly_jzgy)
    private TextView tvJzgy;
    @ViewInject(R.id.jly_shjy)
    private TextView tvShjy;

    private User user;

    private int isHas = 0;

    private Jly jly = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }


    public void initView() {
        tvTitle.setText("社会养老");
        tvHly.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvJzgy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvShjy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        request();
    }

    private void request() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", "0");
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        SuccinctProgress.showSuccinctProgress(JlyInfo.this, "请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jly/queryListByPage";
        VolleyRequest.RequestPost(this, url, "queryListByPage", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    LogUtil.i(resultCode);
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        jly = (Jly) gson.fromJson(result.getJSONObject("data").toString(),
                                Jly.class);
                        isHas = result.getInt("isHas");
                        if (isHas == 1) {
                            edtZfzr.setText(jly.getFzr());
                            edtZfzrPhone.setText(jly.getFzrphone());
                            edtGly.setText(jly.getGly());
                            edtGlyPhone.setText(jly.getGlyphone());
                            edtAqy.setText(jly.getAqy());
                            edtAqyPhone.setText(jly.getAqyphone());
                            edtWsy.setText(jly.getWsy());
                            edtWsyPhone.setText(jly.getWsyphone());
                        }
                        tvHly.setText(jly.getHly());
                        tvJzgy.setText(jly.getJizhong());
                        tvShjy.setText(jly.getShej());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyInfo.this, "查询失败！");
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


    @Event(value = {R.id.iv_back, R.id.jly_hly, R.id.jly_jzgy, R.id.jly_shjy}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.jly_hly:
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("isFrom", "index");
                intent.setClass(JlyInfo.this, JlyMoreListActivity.class);
                startActivity(intent);
                break;
            case R.id.jly_jzgy:
                Intent intent1 = new Intent();
                intent1.putExtra("type", 2);
                intent1.putExtra("isFrom", "index");
                intent1.setClass(JlyInfo.this, JlyMoreListActivity.class);
                startActivity(intent1);
                break;
            case R.id.jly_shjy:
                Intent intent2 = new Intent();
                intent2.putExtra("type", 3);
                intent2.putExtra("isFrom", "index");
                intent2.setClass(JlyInfo.this, JlyMoreListActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryListByPage");
    }


}

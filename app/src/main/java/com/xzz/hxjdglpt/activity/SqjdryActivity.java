package com.xzz.hxjdglpt.activity;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.dateview.JudgeDate;
import com.xzz.hxjdglpt.customview.dateview.ScreenInfo;
import com.xzz.hxjdglpt.customview.dateview.WheelMain;
import com.xzz.hxjdglpt.model.Sqjdry;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zszhjsbhz;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DateUtil;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_sqjdry)
public class SqjdryActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.jsbhz_commit)
    private Button btnCommit;

    @ViewInject(R.id.jsbhz_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.jsbhz_sfzh)
    private EditText edtCode;//身份证号

    @ViewInject(R.id.jsbhz_address)
    private EditText edtAddress;//住址

    @ViewInject(R.id.jsbhz_phone)
    private EditText edtPhone;//手机号

    @ViewInject(R.id.jsbhz_jhrphone)
    private TextView tvStartTime;//开始戒毒时间

    @ViewInject(R.id.jsbhz_gkxz_add)
    private TextView mAdd;//管控小组新增
    @ViewInject(R.id.jsbhz_t_gkxz_lay)
    private RelativeLayout gkxzLay;//管控小组

    @ViewInject(R.id.jsbhz_gkxz)
    private TextView tvGk;//管控小组数量

    private User user;

    private boolean isAdd;//判断是否新增

    private String gridId;//网格ID

    private Sqjdry sqjdry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("社区戒毒人员信息");
        if (isAdd) {
            gkxzLay.setVisibility(View.GONE);
        } else {
            gkxzLay.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            sqjdry = getIntent().getParcelableExtra("sqjdry");
            edtName.setText(sqjdry.getName());//姓名
            edtAddress.setText(sqjdry.getAddress());//地址
            edtCode.setText(sqjdry.getSfzh());
            edtPhone.setText(sqjdry.getPhone());
            tvStartTime.setText(sqjdry.getStartTime());
            getXzCount();
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }

    private void getXzCount() {
        String url = ConstantUtil.BASE_URL + "/zfzzGroup/getCount";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pId", String.valueOf(sqjdry.getId()));
        params.put("type", "4");
        VolleyRequest.RequestPost(this, url, "getCount", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        long count = result.getLong("count");
                        tvGk.setText(String.valueOf(count));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SqjdryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SqjdryActivity.this, R.string.load_fail);
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

    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/sqjdry/commitSqjdry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtCode.getText().toString())) {
            params.put("sfzh", edtCode.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(tvStartTime.getText().toString())) {
            params.put("startTime", tvStartTime.getText().toString());
        } else {
            params.put("startTime", "");
        }
        params.put("bz", "");
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitSqjdry", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(SqjdryActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SqjdryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SqjdryActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.jsbhz_commit, R.id.iv_back, R.id.jsbhz_gkxz_add, R.id.jsbhz_jhrphone},
            type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.jsbhz_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.jsbhz_gkxz_add:
                Intent intent = new Intent();
                intent.setClass(SqjdryActivity.this, GkxzListActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("pId", String.valueOf(sqjdry.getId()));
                startActivity(intent);
                break;
            case R.id.jsbhz_jhrphone:
                showBottoPopupWindow();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitSqjdry");
        BaseApplication.getRequestQueue().cancelAll("updateSqjdry");
        BaseApplication.getRequestQueue().cancelAll("getCount");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/sqjdry/updateSqjdry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(sqjdry.getId()));
        params.put("gridId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtCode.getText().toString())) {
            params.put("sfzh", edtCode.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(tvStartTime.getText().toString())) {
            params.put("startTime", tvStartTime.getText().toString());
        } else {
            params.put("startTime", "");
        }
        params.put("bz", "");
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateSqjdry", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(SqjdryActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SqjdryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SqjdryActivity.this, R.string.modify_fail);
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

    private WheelMain wheelMainDate;
    private String beginTime;

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    public void showBottoPopupWindow() {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_date_popup_window, null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int) (width * 0.8), ActionBar
                .LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtil.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMainDate.initDateTimePicker(year, month, day);
        final String currentTime = wheelMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tvStartTime, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择日期");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                tvStartTime.setText(beginTime.substring(0, 10));
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

}

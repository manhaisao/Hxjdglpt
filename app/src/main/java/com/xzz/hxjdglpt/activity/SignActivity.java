package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.baidumap.LocationService;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.DateModel;
import com.xzz.hxjdglpt.model.Qd;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.signView.RegistrationAdapter;
import com.xzz.hxjdglpt.signView.SpecialCalendar;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 本月日历
 *
 * @author aiyang
 */
public class SignActivity extends BaseActivity implements GridView.OnItemClickListener, View
        .OnClickListener {
    private GridView registration_calendar_gv;
    private TextView today;
    private RegistrationAdapter adapter;
    int mYear = 0;//年
    int mMonth = 0;//月
    int mDay = 0;//日
    //    int mHour = 0;// 时
    private TextView tvTitle;
    private ImageView ivBack;
    private User user;
    private List<Qd> qds = new ArrayList<>();//当月签到信息
    private EditText mEdt;
    private LocationService locationService;

    private String mAddress;
    private Button mQd;
    private Button mQj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        if (user == null) {
            user = application.getUser();
        }
        registration_calendar_gv = (GridView) findViewById(R.id.registration_calendar_gv);
        today = (TextView) findViewById(R.id.today);
        tvTitle = (TextView) findViewById(R.id.hx_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mEdt = (EditText) findViewById(R.id.dq_bz);
        mQd = (Button) findViewById(R.id.qd);
        mQj = (Button) findViewById(R.id.qj);

        tvTitle.setText("每日签到");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mQd.setOnClickListener(this);
        mQj.setOnClickListener(this);
        queryCurrentTime();
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestPermission(new String[]{android.Manifest.permission.READ_PHONE_STATE, android
                .Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission
                .ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0001);
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            Log.e("insert",location.getRadius()+"============");
            if (null != location && location.getLocType() != BDLocation.TypeServerError && location.getRadius() <= 300) {
                if (mAddress == null)
                    mAddress = location.getAddrStr();
                else if (mAddress != null && location.getAddrStr() != null && !mAddress.equals(location.getAddrStr()))
                    mAddress = location.getAddrStr();
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        String today = mYear + "-" + mMonth + "-" + l;
        if (l != 0) {
//            Toast.makeText(view.getContext(), "您选择的日期：" + today, Toast.LENGTH_SHORT).show();
            StringBuffer sb = new StringBuffer();
            sb.append(mYear);
            if (mMonth < 10) {
                sb.append("0");
            }
            sb.append(mMonth);
            if (l < 10) {
                sb.append("0");
            }
            sb.append(l);
            Intent intent = new Intent();
            intent.setClass(SignActivity.this, SignListActivity.class);
            intent.putExtra("time", sb.toString());
            startActivity(intent);
        }
    }

    private void queryCurrentTime() {
        SuccinctProgress.showSuccinctProgress(this, "数据请求中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/qd/getCurrentTime";
        HashMap<String, String> params = new HashMap<>();
        VolleyRequest.RequestPost(this, url, "getCurrentTime", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
                        try {
                            Gson gson = new Gson();
                            DateModel qd = (DateModel) gson.fromJson(result.getJSONObject("data")
                                    .toString(), DateModel.class);
                            mYear = qd.getmYear();
                            mMonth = qd.getmMonth();
                            mDay = qd.getmDay();
//                    mHour = qd.getmHour();
                            SpecialCalendar mCalendar = new SpecialCalendar();
                            boolean isLeapYear = mCalendar.isLeapYear(mYear);
                            int mDays = mCalendar.getDaysOfMonth(isLeapYear, mMonth);
                            int week = mCalendar.getWeekdayOfMonth(mYear, mMonth - 1);

                            adapter = new RegistrationAdapter(SignActivity.this, mDays, week, mDay, qds);
                            registration_calendar_gv.setAdapter(adapter);
                            registration_calendar_gv.setOnItemClickListener(SignActivity.this);
                            today.setText(mYear + "年" + mMonth + "月" + mDay + "日");
                            queryQdList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(final VolleyError error) {
                        LogUtil.i("onMyError");
                        SuccinctProgress.dismiss();

                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    private void qdAction(final String type, final String bz) {
        String url = ConstantUtil.BASE_URL + "/qd/commitQd";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", type);
        params.put("address", mAddress);
        params.put("bz", bz);
        if(TextUtils.isEmpty(mAddress))
        {
            ToastUtil.show(this,"定位失败！");
            return;
        }
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitQd", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(final JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:修改成功 ；2；token失效 3：修改失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Qd qd = (Qd) gson.fromJson(result.getJSONObject("data").toString(), Qd.class);
                        qds.add(qd);
                        adapter.notifyDataSetChanged();
//                        if (qd.getType() == 1) {
//                            mQd.setText("已签到");
//                            mQd.setEnabled(false);
//                            mQj.setText("请假");
//                            mQj.setEnabled(false);
//                            mEdt.setEnabled(false);
//                            ToastUtil.show(SignActivity.this, "签到成功");
//                        } else if (qd.getType() == 2) {
//                            mQj.setText("已请假");
//                            mQj.setEnabled(false);
//                            mQd.setText("签到");
//                            mQd.setEnabled(false);
//                            mEdt.setEnabled(false);
//                            ToastUtil.show(SignActivity.this, "已请假");
//                        }
//                        qds.clear();
//                        queryQdList();
                        mEdt.setEnabled(false);
                        if ("1".equals(type)) {
                            ToastUtil.show(SignActivity.this, "签到成功");
                        } else if ("2".equals(type)) {
                            ToastUtil.show(SignActivity.this, "请假成功");
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SignActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SignActivity.this,"签到失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(final VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    private void queryQdList() {
//        SuccinctProgress.showSuccinctProgress(this, "数据请求中···", SuccinctProgress.THEME_ULTIMATE,
//                false, true);
        String url = ConstantUtil.BASE_URL + "/qd/queryQdByUserId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryQdByUserId", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:修改成功 ；2；token失效 3：修改失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                List<Qd> list = gson.fromJson(result.getJSONArray("data").toString(), new
                                        TypeToken<List<Qd>>() {
                                        }.getType());
                                qds.addAll(list);
                                adapter.notifyDataSetChanged();
//                        for (Qd qd : qds) {
//                            String dayStr = qd.getTime().substring(8, 10);
//                            if (Integer.valueOf(dayStr) == Integer.valueOf(mDay)) {
//                                mEdt.setText(qd.getBz());
//                                if (mHour <= 12) {
//                                    switch (qd.getType()) {
//                                        case 1:
//                                            mQd.setText("上午已签到");
//                                            mQd.setEnabled(false);
//                                            mQj.setText("请假");
//                                            mQj.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 2:
//                                            mQd.setText("上午已签到");
//                                            mQd.setEnabled(false);
//                                            mQj.setText("请假");
//                                            mQj.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 3:
//                                            mQj.setText("上午已请假");
//                                            mQj.setEnabled(false);
//                                            mQd.setText("签到");
//                                            mQd.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 4:
//                                            mQj.setText("上午已请假");
//                                            mQj.setEnabled(false);
//                                            mQd.setText("签到");
//                                            mQd.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 5:
//                                            mQd.setText("上午已签到");
//                                            mQd.setEnabled(false);
//                                            mQj.setText("请假");
//                                            mQj.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 6:
//                                            mQj.setText("上午已请假");
//                                            mQj.setEnabled(false);
//                                            mQd.setText("签到");
//                                            mQd.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 7:
//                                            mEdt.setText("");
//                                            mEdt.setEnabled(true);
//                                            break;
//                                        case 8:
//                                            mEdt.setText("");
//                                            mEdt.setEnabled(true);
//                                            break;
//                                    }
//                                } else {
//                                    switch (qd.getType()) {
//                                        case 1:
//                                            mQd.setText("下午已签到");
//                                            mQd.setEnabled(false);
//                                            mQj.setText("请假");
//                                            mQj.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 2:
//                                            mQj.setText("下午已请假");
//                                            mQj.setEnabled(false);
//                                            mQd.setText("签到");
//                                            mQd.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 3:
//                                            mQd.setText("下午已签到");
//                                            mQd.setEnabled(false);
//                                            mQj.setText("请假");
//                                            mQj.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 4:
//                                            mQj.setText("下午已请假");
//                                            mQj.setEnabled(false);
//                                            mQd.setText("签到");
//                                            mQd.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 5:
//                                            mEdt.setText("");
//                                            mEdt.setEnabled(true);
//                                            break;
//                                        case 6:
//                                            mEdt.setText("");
//                                            mEdt.setEnabled(true);
//                                            break;
//                                        case 7:
//                                            mQd.setText("下午已签到");
//                                            mQd.setEnabled(false);
//                                            mQj.setText("请假");
//                                            mQj.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                        case 8:
//                                            mQj.setText("下午已请假");
//                                            mQj.setEnabled(false);
//                                            mQd.setText("签到");
//                                            mQd.setEnabled(false);
//                                            mEdt.setEnabled(false);
//                                            break;
//                                    }
//                                }
//                                break;
//                            }
//                        }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(SignActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(SignActivity.this, "签到失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        LogUtil.i("onMyError");
//                SuccinctProgress.dismiss();
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitQd");
        BaseApplication.getRequestQueue().cancelAll("queryQdByUserId");
        BaseApplication.getRequestQueue().cancelAll("getCurrentTime");
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0001:
                // -----------location config ------------
                locationService = ((BaseApplication) getApplication()).locationService;
                //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity
                // ，都是通过此种方式获取locationservice实例的
                locationService.registerListener(mListener);
                //注册监听
                LocationClientOption option = new LocationClientOption();//定位方式参数设置
                option.setOpenGps(true); // 打开gps
                option.setAddrType("all");
                option.setCoorType("bd09ll"); // 设置坐标类型
                option.setScanSpan(1000);//周期性请求定位，1秒返回一次位置
                locationService.setLocationOption(option);
                locationService.start();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String bz = mEdt.getText().toString();
        switch (v.getId()) {
            case R.id.qd:
                qdAction("1", bz);
                break;
            case R.id.qj:
                if (TextUtils.isEmpty(bz)) {
                    ToastUtil.show(this, "请在备注内说明请假事由！");
                    return;
                }
                qdAction("2", bz);
                break;
        }
    }
}

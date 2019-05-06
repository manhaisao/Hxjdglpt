package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Daikai;
import com.xzz.hxjdglpt.model.Jdfzr;
import com.xzz.hxjdglpt.model.User;
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
 * 税票代开点
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_spdkd)
public class SpdkdActivity extends BaseActivity implements View.OnTouchListener {


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.c_commit)
    private Button btnCommit;

    @ViewInject(R.id.c_name)
    private EditText edtName;//街道办负责人一
    @ViewInject(R.id.c_phone1)
    private EditText edtPhone;//
    @ViewInject(R.id.c_name2)
    private EditText edtName2;//街道办负责人er
    @ViewInject(R.id.c_phone2)
    private EditText edtPhone2;//
    @ViewInject(R.id.c_mc)
    private EditText edtMc;//代开点名称
    @ViewInject(R.id.c_lxr1)
    private EditText edtLxr1;//税票代开点联系人一
    @ViewInject(R.id.c_lxr_phone1)
    private EditText edtLxrPhone1;//
    @ViewInject(R.id.c_lxr2)
    private EditText edtLxr2;//税票代开点联系人er
    @ViewInject(R.id.c_lxr_phone2)
    private EditText edtLxrPhone2;//
    @ViewInject(R.id.c_zczb)
    private EditText edtTime;//工作時間
    @ViewInject(R.id.c_address)
    private EditText edtAddress;//

    @ViewInject(R.id.c_bz)
    private EditText edtBz;//备注

    private User user;
    private BaseApplication application;


    private boolean isAdd;//判断是否新增

    private Daikai daikai;

    private List<Jdfzr> objs = null;

    private List<Jdfzr> jdfzrs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText("税票代开点");
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        initView();
        initData();
    }

    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            daikai = getIntent().getParcelableExtra("daikai");
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
        loadData();
    }

    public void initView() {
        edtAddress.setOnTouchListener(this);
        edtBz.setOnTouchListener(this);
    }

    private void loadData() {
        SuccinctProgress.showSuccinctProgress(SpdkdActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/daikai/queryLxrList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (daikai != null)
            params.put("id", String.valueOf(daikai.getId()));
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        Gson gson = new Gson();
                        objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jdfzr>>() {
                                }.getType());
                        JSONArray jsonArray1 = result.getJSONArray("jdfzr");
                        jdfzrs = gson.fromJson(jsonArray1.toString(), new
                                TypeToken<List<Jdfzr>>() {
                                }.getType());
                        if (daikai != null) {
                            edtMc.setText(daikai.getName());
                            edtTime.setText(daikai.getTime());
                            edtBz.setText(daikai.getBz());
                            edtAddress.setText(daikai.getAddress());
                        }
                        if (jdfzrs.size() >= 2) {
                            edtName.setText(jdfzrs.get(0).getName());
                            edtPhone.setText(jdfzrs.get(0).getPhone());
                            edtName2.setText(jdfzrs.get(1).getName());
                            edtPhone2.setText(jdfzrs.get(1).getPhone());
                        } else if (jdfzrs.size() == 1) {
                            edtName.setText(jdfzrs.get(0).getName());
                            edtPhone.setText(jdfzrs.get(0).getPhone());
                        }
                        if (objs.size() >= 2) {
                            edtLxr1.setText(objs.get(0).getName());
                            edtLxrPhone1.setText(objs.get(0).getPhone());
                            edtLxr2.setText(objs.get(1).getName());
                            edtLxrPhone2.setText(objs.get(1).getPhone());
                        } else if (objs.size() == 1) {
                            edtLxr1.setText(objs.get(0).getName());
                            edtLxrPhone1.setText(objs.get(0).getPhone());
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SpdkdActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SpdkdActivity.this, R.string.load_fail);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.c_address && canVerticalScroll(edtAddress))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.c_bz && canVerticalScroll(edtBz))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向能否够滚动
     *
     * @param editText 须要推断的EditText
     * @return true：能够滚动   false：不能够滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText
                .getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Event(value = {R.id.iv_back, R.id.c_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.c_commit:
                if (TextUtils.isEmpty(edtMc.getText().toString())) {
                    ToastUtil.show(SpdkdActivity.this, "请输入税票代开点名称！");
                    return;
                }
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
        }
    }

    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/daikai/commitData";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("named1", edtName.getText().toString());
        } else {
            params.put("named1", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            params.put("phoned1", edtPhone.getText().toString());
        } else {
            params.put("phoned1", "");
        }
        if (!TextUtils.isEmpty(edtName2.getText().toString())) {
            params.put("named2", edtName2.getText().toString());
        } else {
            params.put("named2", "");
        }
        if (!TextUtils.isEmpty(edtPhone2.getText().toString())) {
            params.put("phoned2", edtPhone2.getText().toString());
        } else {
            params.put("phoned2", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtLxr1.getText().toString())) {
            params.put("name1", edtLxr1.getText().toString());
        } else {
            params.put("name1", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone1.getText().toString())) {
            params.put("phone1", edtLxrPhone1.getText().toString());
        } else {
            params.put("phone1", "");
        }
        if (!TextUtils.isEmpty(edtLxr2.getText().toString())) {
            params.put("name2", edtLxr2.getText().toString());
        } else {
            params.put("name2", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone2.getText().toString())) {
            params.put("phone2", edtLxrPhone2.getText().toString());
        } else {
            params.put("phone2", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtTime.getText().toString())) {
            params.put("time", edtTime.getText().toString());
        } else {
            params.put("time", "");
        }
        if (!TextUtils.isEmpty(edtMc.getText().toString())) {
            params.put("name", edtMc.getText().toString());
        } else {
            params.put("name", "");
        }
        if (jdfzrs.size() >= 2) {
            params.put("id3", String.valueOf(jdfzrs.get(0).getId()));
            params.put("id4", String.valueOf(jdfzrs.get(1).getId()));
        } else if (jdfzrs.size() == 1) {
            params.put("id3", String.valueOf(jdfzrs.get(0).getId()));
        }
        SuccinctProgress.showSuccinctProgress(SpdkdActivity.this, "请求提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "company_commit", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            if ("1".equals(resultCode)) {
                                ToastUtil.show(SpdkdActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(SpdkdActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(SpdkdActivity.this, R.string.commit_fail);
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

    /**
     * 提交信息
     */
    private void update() {
        String url = ConstantUtil.BASE_URL + "/daikai/updateData";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(daikai.getId()));
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("named1", edtName.getText().toString());
        } else {
            params.put("named1", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            params.put("phoned1", edtPhone.getText().toString());
        } else {
            params.put("phoned1", "");
        }
        if (!TextUtils.isEmpty(edtName2.getText().toString())) {
            params.put("named2", edtName2.getText().toString());
        } else {
            params.put("named2", "");
        }
        if (!TextUtils.isEmpty(edtPhone2.getText().toString())) {
            params.put("phoned2", edtPhone2.getText().toString());
        } else {
            params.put("phoned2", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtLxr1.getText().toString())) {
            params.put("name1", edtLxr1.getText().toString());
        } else {
            params.put("name1", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone1.getText().toString())) {
            params.put("phone1", edtLxrPhone1.getText().toString());
        } else {
            params.put("phone1", "");
        }
        if (!TextUtils.isEmpty(edtLxr2.getText().toString())) {
            params.put("name2", edtLxr2.getText().toString());
        } else {
            params.put("name2", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone2.getText().toString())) {
            params.put("phone2", edtLxrPhone2.getText().toString());
        } else {
            params.put("phone2", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtTime.getText().toString())) {
            params.put("time", edtTime.getText().toString());
        } else {
            params.put("time", "");
        }
        if (!TextUtils.isEmpty(edtMc.getText().toString())) {
            params.put("name", edtMc.getText().toString());
        } else {
            params.put("name", "");
        }

        if (jdfzrs.size() >= 2) {
            params.put("id3", String.valueOf(jdfzrs.get(0).getId()));
            params.put("id4", String.valueOf(jdfzrs.get(1).getId()));
        } else if (jdfzrs.size() == 1) {
            params.put("id3", String.valueOf(jdfzrs.get(0).getId()));
        }
        if (objs.size() >= 2) {
            params.put("id1", String.valueOf(objs.get(0).getId()));
            params.put("id1", String.valueOf(objs.get(1).getId()));
        } else if (objs.size() == 1) {
            params.put("id1", String.valueOf(objs.get(0).getId()));
        }
        SuccinctProgress.showSuccinctProgress(SpdkdActivity.this, "请求提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "updateCompany", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(SpdkdActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SpdkdActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SpdkdActivity.this, R.string.modify_fail);
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
        BaseApplication.getRequestQueue().cancelAll("updateCompany");
        BaseApplication.getRequestQueue().cancelAll("company_commit");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

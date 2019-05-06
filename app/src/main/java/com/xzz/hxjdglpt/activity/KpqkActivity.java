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
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Kpqk;
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
 * 开票情况
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_kpqk)
public class KpqkActivity extends BaseActivity implements View.OnTouchListener {


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.c_commit)
    private Button btnCommit;

    @ViewInject(R.id.c_name)
    private EditText edtZe;//纳税总额
    @ViewInject(R.id.c_bz)
    private EditText edtBz;//备注

    private User user;
    private BaseApplication application;

    private Kpqk kpqk = null;

    private String comId;//建築ID

    private String type;//季度类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        comId = getIntent().getStringExtra("comId");
        type = getIntent().getStringExtra("type");
        if ("1".equals(type)) {
            tvTitle.setText("第一季度开票情况");
        } else if ("2".equals(type)) {
            tvTitle.setText("第二季度开票情况");
        } else if ("3".equals(type)) {
            tvTitle.setText("第三季度开票情况");
        } else if ("4".equals(type)) {
            tvTitle.setText("第四季度开票情况");
        }
        initView();
        initData();
    }

    public void initData() {
        loadData();
    }

    public void initView() {
        edtBz.setOnTouchListener(this);
    }

    private void loadData() {
        SuccinctProgress.showSuccinctProgress(KpqkActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/kpqk/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", type);
        params.put("pid", comId);
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
                        Gson gson = new Gson();
                        kpqk = (Kpqk) gson.fromJson(result.getJSONObject("data").toString(),
                                Kpqk.class);
                        edtZe.setText(kpqk.getJe());
                        edtBz.setText(kpqk.getBz());
                        btnCommit.setText(getString(R.string.update_space_1));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(KpqkActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(KpqkActivity.this, R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                        //没有查到的情况
                        btnCommit.setText(getString(R.string.comfirm_space_1));
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
        if ((view.getId() == R.id.c_bz && canVerticalScroll(edtBz))) {
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
                if (kpqk == null) {
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
        String url = ConstantUtil.BASE_URL + "/kpqk/commitData";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (!TextUtils.isEmpty(edtZe.getText().toString())) {
            params.put("je", edtZe.getText().toString());
        } else {
            params.put("je", "");
        }
        params.put("pid", comId);
        params.put("type", type);
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(KpqkActivity.this, "请求提交中···", SuccinctProgress
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
                                ToastUtil.show(KpqkActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(KpqkActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(KpqkActivity.this, R.string.commit_fail);
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
        String url = ConstantUtil.BASE_URL + "/kpqk/updateData";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(kpqk.getId()));
        if (!TextUtils.isEmpty(edtZe.getText().toString())) {
            params.put("je", edtZe.getText().toString());
        } else {
            params.put("je", "");
        }
        params.put("pid", comId);
        params.put("type", type);
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(KpqkActivity.this, "请求提交中···", SuccinctProgress
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
                        ToastUtil.show(KpqkActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(KpqkActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(KpqkActivity.this, R.string.modify_fail);
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

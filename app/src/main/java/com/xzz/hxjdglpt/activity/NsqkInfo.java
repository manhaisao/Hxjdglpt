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
import com.xzz.hxjdglpt.model.Jidu;
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
 * 纳税情况
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_nsqk_info)
public class NsqkInfo extends BaseActivity {


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.c_name)
    private TextView edtZe;//纳税总额
    @ViewInject(R.id.c_phone1)
    private TextView edtSj;//实缴税款
    @ViewInject(R.id.c_name2)
    private TextView edtYj;//预缴税款
    @ViewInject(R.id.c_bz)
    private TextView edtBz;//备注
    @ViewInject(R.id.c_lb)
    private TextView edtLb;//

    private User user;
    private BaseApplication application;

    private Jidu jd = null;

    private String comType;//公司类型

    private String comId;//公司ID

    private String type;//季度类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        comType = getIntent().getStringExtra("comType");
        comId = getIntent().getStringExtra("comId");
        type = getIntent().getStringExtra("type");
        if ("1".equals(type)) {
            tvTitle.setText("第一季度纳税情况");
        } else if ("2".equals(type)) {
            tvTitle.setText("第二季度纳税情况");
        } else if ("3".equals(type)) {
            tvTitle.setText("第三季度纳税情况");
        } else if ("4".equals(type)) {
            tvTitle.setText("第四季度纳税情况");
        }
        edtLb.setText(comType);
        initView();
        initData();
    }

    public void initData() {
        loadData();
    }

    public void initView() {

    }

    private void loadData() {
        SuccinctProgress.showSuccinctProgress(NsqkInfo.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/nsqk/queryList";
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
                        jd = (Jidu) gson.fromJson(result.getJSONObject("data").toString(),
                                Jidu.class);
                        edtZe.setText(jd.getNasuinum());
                        edtSj.setText(jd.getShijiao());
                        edtYj.setText(jd.getYujiao());
                        edtBz.setText(jd.getBz());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(NsqkInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(NsqkInfo.this, R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                        //没有查到的情况
                        ToastUtil.show(NsqkInfo.this, "暂无纳税信息");
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
        BaseApplication.getRequestQueue().cancelAll("queryList");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

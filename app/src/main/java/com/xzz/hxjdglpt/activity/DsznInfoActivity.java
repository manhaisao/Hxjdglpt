package com.xzz.hxjdglpt.activity;


import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Dszn;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Ylfn;
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
 * 信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_dszninfo)
public class DsznInfoActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.dszninfo_commit)
    private Button btnCommit;

    @ViewInject(R.id.dszninfo_znname)
    private EditText edtZNName;//子女姓名

    @ViewInject(R.id.dszninfo_sex)
    private AppCompatSpinner spSex;//性别

    @ViewInject(R.id.dszninfo_fname)
    private EditText edtFName;//父亲名称

    @ViewInject(R.id.dszninfo_fsfzh)
    private EditText edtFSfzh;//父亲身份证
    @ViewInject(R.id.dszninfo_mname)
    private EditText edtMName;//母亲名称

    @ViewInject(R.id.dszninfo_msfzh)
    private EditText edtMSfzh;//母亲身份证
    @ViewInject(R.id.dszninfo_address)
    private EditText edtAddress;//家庭地址
    @ViewInject(R.id.dszninfo_phone)
    private EditText edtPhone;//联系电话
    @ViewInject(R.id.dszninfo_bz)
    private EditText edtBz;//备注


    private User user;

    private boolean isAdd;//判断是否新增

    private String gridId;//网格ID

    private Dszn dszn;

    private LayoutInflater mInflater;

    private String[] types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        types = getResources().getStringArray(R.array.sex_list);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("独生子女信息");
        edtBz.setOnTouchListener(this);
        //适配器
        ArrayAdapter typeAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, types);
        //设置样式
        typeAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spSex.setAdapter(typeAd);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.dszninfo_bz && canVerticalScroll(edtBz))) {
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


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            dszn = getIntent().getParcelableExtra("dszn");
            edtZNName.setText(dszn.getZnName());//姓名
            edtAddress.setText(dszn.getAddress());
            edtFName.setText(dszn.getfName());
            edtFSfzh.setText(dszn.getfSfzh());
            edtMName.setText(dszn.getmName());
            edtMSfzh.setText(dszn.getmSfzh());
            edtPhone.setText(dszn.getPhone());
            if (types[0].equals(dszn.getSex())) {
                spSex.setSelection(0);
            } else if (types[1].equals(dszn.getSex())) {
                spSex.setSelection(1);
            }
            edtBz.setText(dszn.getBz());//备注
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/dszn/commitDszn";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtZNName.getText().toString())) {
            params.put("znName", edtZNName.getText().toString());
        } else {
            params.put("znName", "");
        }
        params.put("sex", spSex.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtFName.getText().toString())) {
            params.put("fName", edtFName.getText().toString());
        } else {
            params.put("fName", "");
        }
        if (!TextUtils.isEmpty(edtFSfzh.getText().toString())) {
            params.put("fSfzh", edtFSfzh.getText().toString());
        } else {
            params.put("fSfzh", "");
        }
        if (!TextUtils.isEmpty(edtMName.getText().toString())) {
            params.put("mName", edtMName.getText().toString());
        } else {
            params.put("mName", "");
        }
        if (!TextUtils.isEmpty(edtMSfzh.getText().toString())) {
            params.put("mSfzh", edtMSfzh.getText().toString());
        } else {
            params.put("mSfzh", "");
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
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitDszn", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(DsznInfoActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DsznInfoActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DsznInfoActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.dszninfo_commit, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.dszninfo_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitDszn");
        BaseApplication.getRequestQueue().cancelAll("updateYlfn");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/dszn/updateDszn";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(dszn.getId()));
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtZNName.getText().toString())) {
            params.put("znName", edtZNName.getText().toString());
        } else {
            params.put("znName", "");
        }
        params.put("sex", spSex.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtFName.getText().toString())) {
            params.put("fName", edtFName.getText().toString());
        } else {
            params.put("fName", "");
        }
        if (!TextUtils.isEmpty(edtFSfzh.getText().toString())) {
            params.put("fSfzh", edtFSfzh.getText().toString());
        } else {
            params.put("fSfzh", "");
        }
        if (!TextUtils.isEmpty(edtMName.getText().toString())) {
            params.put("mName", edtMName.getText().toString());
        } else {
            params.put("mName", "");
        }
        if (!TextUtils.isEmpty(edtMSfzh.getText().toString())) {
            params.put("mSfzh", edtMSfzh.getText().toString());
        } else {
            params.put("mSfzh", "");
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
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateDszn", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(DsznInfoActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DsznInfoActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DsznInfoActivity.this, R.string.modify_fail);
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

}

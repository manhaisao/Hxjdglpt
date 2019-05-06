package com.xzz.hxjdglpt.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.CityInfo;
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
@ContentView(R.layout.aty_cityinfo)
public class CityInfoActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.cityinfo_commit)
    private Button btnCommit;

    @ViewInject(R.id.cityinfo_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.cityinfo_t_name)
    private TextView tvName;//姓名


    @ViewInject(R.id.cityinfo_dy)
    private EditText edtDy;//待遇

    @ViewInject(R.id.cityinfo_phone)
    private EditText edtPhone;//电话

    @ViewInject(R.id.cityinfo_bz)
    private EditText edtBz;//备注

    @ViewInject(R.id.cityinfo_lay2)
    private RelativeLayout ral1;
    @ViewInject(R.id.cityinfo_lay1)
    private RelativeLayout ral2;

    private User user;

    private boolean isAdd;//判断是否新增

    private String gridId;//网格ID

    private CityInfo cityInfo;

    private LayoutInflater mInflater;

    private int type;

    private String sType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        type = getIntent().getIntExtra("type", 0);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        switch (type) {
            case 17:
                tvTitle.setText("道路信息");
                tvName.setText("道路名称：");
                ral1.setVisibility(View.GONE);
                ral2.setVisibility(View.GONE);
                sType = "01";
                break;
            case 18:
                tvTitle.setText("河塘信息");
                tvName.setText("河塘名称：");
                ral1.setVisibility(View.GONE);
                ral2.setVisibility(View.GONE);
                sType = "02";
                break;
            case 19:
                tvTitle.setText("垃圾清运员");
                tvName.setText("姓名：");
                ral1.setVisibility(View.VISIBLE);
                ral2.setVisibility(View.VISIBLE);
                sType = "03";
                break;
            case 20:
                tvTitle.setText("流动保洁员");
                tvName.setText("姓名：");
                ral1.setVisibility(View.VISIBLE);
                ral2.setVisibility(View.VISIBLE);
                sType = "04";
                break;
            case 76:
                tvTitle.setText("街道网格员");
                tvName.setText("姓名：");
                ral1.setVisibility(View.VISIBLE);
                ral2.setVisibility(View.VISIBLE);
                sType = "05";
                break;
            case 77:
                tvTitle.setText("村居网格员");
                tvName.setText("姓名：");
                ral1.setVisibility(View.VISIBLE);
                ral2.setVisibility(View.VISIBLE);
                sType = "06";
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            cityInfo = getIntent().getParcelableExtra("cityInfo");
            edtName.setText(cityInfo.getName());//姓名
            if (type == 19 || type == 20) {
                edtDy.setText(cityInfo.getDaiyu());
                edtPhone.setText(cityInfo.getPhone());
            }
            edtBz.setText(cityInfo.getBz());//备注
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
        edtBz.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.cityinfo_bz && canVerticalScroll(edtBz))) {
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

    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/cityinfo/commitCityinfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (type == 19 || type == 20) {
            if (!TextUtils.isEmpty(edtDy.getText().toString())) {
                params.put("daiyu", edtDy.getText().toString());
            } else {
                params.put("daiyu", "");
            }
            if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
                if (edtPhone.getText().toString().length() != 11) {
                    ToastUtil.show(this, "请输入11位手机号");
                    return;
                }
                params.put("phone", edtPhone.getText().toString());
            } else {
                params.put("phone", "");
            }
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("type", sType);
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitCityinfo", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(CityInfoActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(CityInfoActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CityInfoActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.cityinfo_commit, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.cityinfo_commit:
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
        BaseApplication.getRequestQueue().cancelAll("commitCityinfo");
        BaseApplication.getRequestQueue().cancelAll("updateCityinfo");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/cityinfo/updateCityinfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(cityInfo.getId()));
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (type == 19 || type == 20) {
            if (!TextUtils.isEmpty(edtDy.getText().toString())) {
                params.put("daiyu", edtDy.getText().toString());
            } else {
                params.put("daiyu", "");
            }
            if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
                if (edtPhone.getText().toString().length() != 11) {
                    ToastUtil.show(this, "请输入11位手机号");
                    return;
                }
                params.put("phone", edtPhone.getText().toString());
            } else {
                params.put("phone", "");
            }
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("type", sType);
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateCityinfo", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(CityInfoActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(CityInfoActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CityInfoActivity.this, R.string.modify_fail);
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

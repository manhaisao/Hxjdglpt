package com.xzz.hxjdglpt.activity;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Huli;
import com.xzz.hxjdglpt.model.Role;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 护理人员信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_hlry)
public class HlryActivity extends BaseActivity implements View.OnFocusChangeListener, View
        .OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.hlry_qk)
    private AppCompatSpinner spQk;

    @ViewInject(R.id.hlry_commit)
    private Button btnCommit;

    @ViewInject(R.id.hlry_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.hlry_sfzh)
    private EditText edtSfzh;//身份证

    @ViewInject(R.id.hlry_lxdh)
    private EditText edtLxdh;//联系电话

    @ViewInject(R.id.hlry_time)
    private EditText edtTime;//检查时间

    @ViewInject(R.id.hlry_bz)
    private EditText edtBz;//备注

    private User user;

    private boolean isAdd;//判断是否新增


    //定义显示时间控件
    private Calendar calendar; //通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;

    private Huli hl;

    private String[] hlType;

    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        calendar = Calendar.getInstance();
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        initView();
        initData();
        edtBz.setOnTouchListener(this);
    }
    //打码权限
    private boolean isContain() {
        for (Role r : roles) {
            if ("4257".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.hlry_bz && canVerticalScroll(edtBz))) {
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

    public void initView() {
        tvTitle.setText("护理人员");
        hlType = getResources().getStringArray(R.array.jcqk_array);
        //适配器
        ArrayAdapter cjTypeAdpate = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, hlType);
        //设置样式
        cjTypeAdpate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spQk.setAdapter(cjTypeAdpate);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            hl = getIntent().getParcelableExtra("hlry");
            for (int i = 0; i < hlType.length; i++) {
                if (hlType[i].equals(hl.getQingkuang())) {
                    spQk.setSelection(i);
                    break;
                }
            }
            edtName.setText(hl.getName());//姓名
//            if(isContain()){
                edtSfzh.setText(hl.getNo());
//            }else if (!TextUtils.isEmpty(hl.getNo()) && hl.getNo().length() > 8) {
//                String sfzh = hl.getNo().substring(0, hl.getNo().length() - 8);
//                edtSfzh.setText(sfzh+"********");
//            }
            edtLxdh.setText(hl.getPhone());
            edtTime.setText(hl.getTime());
            edtBz.setText(hl.getBz());//备注
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/hly/commitHly";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtLxdh.getText().toString())) {
            params.put("phone", edtLxdh.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtSfzh.getText().toString())) {
            params.put("no", edtSfzh.getText().toString());
        } else {
            params.put("no", "");
        }
        if (!TextUtils.isEmpty(edtTime.getText().toString())) {
            params.put("time", edtTime.getText().toString());
        } else {
            params.put("time", "");
        }
        params.put("qingkuang", spQk.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        VolleyRequest.RequestPost(this, url, "commitHly", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(HlryActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(HlryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(HlryActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.hlry_commit, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.hlry_commit:
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
        BaseApplication.getRequestQueue().cancelAll("commitHly");
        BaseApplication.getRequestQueue().cancelAll("updateHly");
    }


    private void update() {
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/hly/updateHly";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(hl.getId()));
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtLxdh.getText().toString())) {
            params.put("phone", edtLxdh.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtSfzh.getText().toString())) {
            params.put("no", edtSfzh.getText().toString());
        } else {
            params.put("no", "");
        }
        if (!TextUtils.isEmpty(edtTime.getText().toString())) {
            params.put("time", edtTime.getText().toString());
        } else {
            params.put("time", "");
        }
        params.put("qingkuang", spQk.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        VolleyRequest.RequestPost(this, url, "updateHly", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(HlryActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(HlryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(HlryActivity.this, R.string.modify_fail);
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
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                mYear = year;
                mMonth = month;
                mDay = day;
                //更新EditText控件日期 小于10加0
//                edtBirth.setText(new StringBuilder().append(mYear).append("-").append((mMonth +
//                        1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append((mDay <
//                        10) ? "0" + mDay : mDay));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
                .DAY_OF_MONTH)).show();
    }
}

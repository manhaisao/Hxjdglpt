package com.xzz.hxjdglpt.activity;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.dateview.JudgeDate;
import com.xzz.hxjdglpt.customview.dateview.ScreenInfo;
import com.xzz.hxjdglpt.customview.dateview.WheelMain;
import com.xzz.hxjdglpt.model.Tkgy;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Yfdx;
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
@ContentView(R.layout.aty_tkgy)
public class TkgyActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.tkgy_commit)
    private Button btnCommit;

    @ViewInject(R.id.tkgy_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.tkgy_birth)
    private TextView tvBirth;//出生日月

    @ViewInject(R.id.tkgy_bz)
    private EditText edtBz;//备注

    private User user;

    private boolean isAdd;//判断是否新增残疾人

    private String gridId;//网格ID

    private Tkgy tkgy;

    private LayoutInflater mInflater;

    @ViewInject(R.id.tkgy_lx)
    private AppCompatSpinner spLx;

    private String[] type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        edtBz.setOnTouchListener(this);
        initView();
        initData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.tkgy_bz && canVerticalScroll(edtBz))) {
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
        tvTitle.setText("特困(五保)供养");
        type = getResources().getStringArray(R.array.tkgy_type_list);
        //适配器
        ArrayAdapter cjTypeAdpate = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, type);
        //设置样式
        cjTypeAdpate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spLx.setAdapter(cjTypeAdpate);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            tkgy = getIntent().getParcelableExtra("tkgy");
            edtName.setText(tkgy.getName());//姓名
            if (tkgy.getCsrq() != null)
                try {
                    tvBirth.setText(DateUtil.dateToString(new Date(Long.valueOf(tkgy.getCsrq())), "yyyy-MM-dd"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            for (int i = 0; i < type.length; i++) {
                if (type[i].equals(tkgy.getType())) {
                    spLx.setSelection(i);
                    break;
                }
            }
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
        String url = ConstantUtil.BASE_URL + "/tkgy/commitTkgy";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        params.put("type", spLx.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(tvBirth.getText().toString())) {
            params.put("csrq", tvBirth.getText().toString());
        } else {
            params.put("csrq", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        VolleyRequest.RequestPost(this, url, "commitTkgy", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(TkgyActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TkgyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TkgyActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.tkgy_commit, R.id.iv_back, R.id.tkgy_birth}, type = View.OnClickListener
            .class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.tkgy_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
            case R.id.tkgy_birth:
                showBottoPopupWindow();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitTkgy");
        BaseApplication.getRequestQueue().cancelAll("updateTkgy");
    }


    private void update() {
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/tkgy/updateTkgy";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(tkgy.getId()));
        params.put("gId", gridId);
        params.put("type", spLx.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(tvBirth.getText().toString())) {
            params.put("csrq", tvBirth.getText().toString());
        } else {
            params.put("csrq", "");
        }
        VolleyRequest.RequestPost(this, url, "updateYfdx", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(TkgyActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TkgyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TkgyActivity.this, R.string.modify_fail);
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


    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    private PopupWindow mPopupWindow;

    public void showBottoPopupWindow() {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_date_popup_window, null);
        mPopupWindow = new PopupWindow(menuView, (int) (width * 0.8), ActionBar
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
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tvBirth, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("请选择出生日期");
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
                String bTime = wheelMainDate.getTime().toString();
                tvBirth.setText(bTime.substring(0, 10));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
}

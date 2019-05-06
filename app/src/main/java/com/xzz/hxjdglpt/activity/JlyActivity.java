package com.xzz.hxjdglpt.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Jly;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 残疾人员信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_jly)
public class JlyActivity extends BaseActivity  {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.cjry_commit)
    private Button btnCommit;

    @ViewInject(R.id.jly_zfzr)
    private EditText edtZfzr;
    @ViewInject(R.id.jly_zfzrphne)
    private EditText edtZfzrPhone;

    @ViewInject(R.id.jly_gly)
    private EditText edtGly;
    @ViewInject(R.id.jly_glyphone)
    private EditText edtGlyPhone;

    @ViewInject(R.id.jly_aqy)
    private EditText edtAqy;
    @ViewInject(R.id.jly_aqyphone)
    private EditText edtAqyPhone;

    @ViewInject(R.id.jly_wsy)
    private EditText edtWsy;
    @ViewInject(R.id.jly_wsyphone)
    private EditText edtWsyPhone;

    @ViewInject(R.id.jly_hly)
    private TextView tvHly;
    @ViewInject(R.id.jly_jzgy)
    private TextView tvJzgy;
    @ViewInject(R.id.jly_shjy)
    private TextView tvShjy;

    private User user;

    private int isHas=0;

    private Jly jly=null;

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
        SuccinctProgress.showSuccinctProgress(JlyActivity.this, "请求中···", SuccinctProgress
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
                            btnCommit.setText(getString(R.string.update_space_1));
                        } else {
                            btnCommit.setText(getString(R.string.comfirm_space_1));
                        }
                        tvHly.setText(jly.getHly());
                        tvJzgy.setText(jly.getJizhong());
                        tvShjy.setText(jly.getShej());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyActivity.this, "查询失败！");
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
     * 提交信息
     */
    private void commit() {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/jly/commitJly";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (!TextUtils.isEmpty(edtZfzr.getText().toString())) {
            params.put("fzr", edtZfzr.getText().toString());
        } else {
            params.put("fzr", "");
        }
        if (!TextUtils.isEmpty(edtZfzrPhone.getText().toString())) {
            params.put("fzrphone", edtZfzrPhone.getText().toString());
        } else {
            params.put("fzrphone", "");
        }
        if (!TextUtils.isEmpty(edtGly.getText().toString())) {
            params.put("gly", edtGly.getText().toString());
        } else {
            params.put("gly", "");
        }
        if (!TextUtils.isEmpty(edtGlyPhone.getText().toString())) {
            params.put("glyphone", edtGlyPhone.getText().toString());
        } else {
            params.put("glyphone", "");
        }

        if (!TextUtils.isEmpty(edtAqy.getText().toString())) {
            params.put("aqy", edtAqy.getText().toString());
        } else {
            params.put("aqy", "");
        }
        if (!TextUtils.isEmpty(edtAqyPhone.getText().toString())) {
            params.put("aqyphone", edtAqyPhone.getText().toString());
        } else {
            params.put("aqyphone", "");
        }
        if (!TextUtils.isEmpty(edtWsy.getText().toString())) {
            params.put("wsy", edtWsy.getText().toString());
        } else {
            params.put("wsy", "");
        }
        if (!TextUtils.isEmpty(edtWsyPhone.getText().toString())) {
            params.put("wsyphone", edtWsyPhone.getText().toString());
        } else {
            params.put("wsyphone", "");
        }

        VolleyRequest.RequestPost(this, url, "jly_commit", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(JlyActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.cjry_commit, R.id.iv_back,R.id.jly_add_hly,R.id.jly_add_jzgy,R.id.jly_add_shjy}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.cjry_commit:
                if (isHas==0) {
                    commit();
                } else if(isHas==1){
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.jly_add_hly:
                Intent intent=new Intent();
                intent.putExtra("type",1);
                intent.setClass(JlyActivity.this,JlyMoreListActivity.class);
                startActivity(intent);
                break;
            case R.id.jly_add_jzgy:
                Intent intent1=new Intent();
                intent1.putExtra("type",2);
                intent1.setClass(JlyActivity.this,JlyMoreListActivity.class);
                startActivity(intent1);
                break;
            case R.id.jly_add_shjy:
                Intent intent2=new Intent();
                intent2.putExtra("type",3);
                intent2.setClass(JlyActivity.this,JlyMoreListActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("jly_commit");
        BaseApplication.getRequestQueue().cancelAll("updateJly");
        BaseApplication.getRequestQueue().cancelAll("queryListByPage");
    }


    private void update() {
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/jly/updateJly";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if(jly!=null)
        params.put("id", String.valueOf(jly.getId()));
        if (!TextUtils.isEmpty(edtZfzr.getText().toString())) {
            params.put("fzr", edtZfzr.getText().toString());
        } else {
            params.put("fzr", "");
        }
        if (!TextUtils.isEmpty(edtZfzrPhone.getText().toString())) {
            params.put("fzrphone", edtZfzrPhone.getText().toString());
        } else {
            params.put("fzrphone", "");
        }
        if (!TextUtils.isEmpty(edtGly.getText().toString())) {
            params.put("gly", edtGly.getText().toString());
        } else {
            params.put("gly", "");
        }
        if (!TextUtils.isEmpty(edtGlyPhone.getText().toString())) {
            params.put("glyphone", edtGlyPhone.getText().toString());
        } else {
            params.put("glyphone", "");
        }

        if (!TextUtils.isEmpty(edtAqy.getText().toString())) {
            params.put("aqy", edtAqy.getText().toString());
        } else {
            params.put("aqy", "");
        }
        if (!TextUtils.isEmpty(edtAqyPhone.getText().toString())) {
            params.put("aqyphone", edtAqyPhone.getText().toString());
        } else {
            params.put("aqyphone", "");
        }
        if (!TextUtils.isEmpty(edtWsy.getText().toString())) {
            params.put("wsy", edtWsy.getText().toString());
        } else {
            params.put("wsy", "");
        }
        if (!TextUtils.isEmpty(edtWsyPhone.getText().toString())) {
            params.put("wsyphone", edtWsyPhone.getText().toString());
        } else {
            params.put("wsyphone", "");
        }
        VolleyRequest.RequestPost(this, url, "updateJly", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(JlyActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyActivity.this, R.string.modify_fail);
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

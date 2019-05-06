package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.ls.Bx;
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

/**
 * 城乡居民医疗保险信息
 */
@ContentView(R.layout.activity_ls_collect_06)
public class LsCollect06Activity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.et_bxjbxx)
    private EditText etBxjbxx;
    @ViewInject(R.id.et_bxzgbm)
    private EditText etBxzgbm;
    @ViewInject(R.id.et_bxlxdh)
    private EditText etBxlxdh;
    @ViewInject(R.id.et_bxznbm)
    private EditText etBxznbm;
    @ViewInject(R.id.et_bxfzr)
    private EditText etBxfzr;
    @ViewInject(R.id.et_bxfzrhm)
    private EditText etBxfzrhm;
    @ViewInject(R.id.et_bxjbr)
    private EditText etBxjbr;
    @ViewInject(R.id.et_bxjbrhm)
    private EditText etBxjbrhm;
    @ViewInject(R.id.btn_sb)
    private Button btnSb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText("城乡居民医疗保险信息");
    }

    @Override
    public void initData() {
        super.initData();
        getInit();
    }

    @Event(value = {R.id.iv_back,R.id.btn_sb}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.btn_sb:
                formSb();
                break;
        }
    }

    /**
     * 获取初始化参数
     */
    private void getInit() {
        String url = ConstantUtil.BASE_URL + "/xshs/queryinfo?type=2";
        VolleyRequest.RequestGet(this, url, "queryinfo", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Bx bx = (Bx) gson.fromJson(result.getJSONObject("data").toString
                                (), Bx.class);
                        //初始化
                        etBxjbxx.setText(bx.getContent());
                        etBxzgbm.setText(bx.getBumen());
                        etBxlxdh.setText(bx.getPhone());
                        etBxznbm.setText(bx.getZhineng());
                        etBxfzr.setText(bx.getFzr());
                        etBxfzrhm.setText(bx.getFzrphone());
                        etBxjbr.setText(bx.getJbr());
                        etBxjbrhm.setText(bx.getJbrphone());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect06Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect06Activity.this, R.string.load_fail);
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
     * 数据提交
     */
    private void formSb() {
        SuccinctProgress.showSuccinctProgress(LsCollect06Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/work1/updateUserjichu";
        HashMap<String, String> params = new HashMap<>();
        params.put("type","2");
        params.put("content", etBxjbxx.getText().toString());
        params.put("bumen", etBxzgbm.getText().toString());
        params.put("phone", etBxlxdh.getText().toString());
        params.put("zhineng", etBxznbm.getText().toString());
        params.put("fzr", etBxfzr.getText().toString());
        params.put("fzrphone", etBxfzrhm.getText().toString());
        params.put("jbr", etBxjbr.getText().toString());
        params.put("jbr", etBxjbrhm.getText().toString());
        VolleyRequest.RequestPost(this, url, "updateUserjichu", params, new VolleyListenerInterface
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
                        ToastUtil.show(LsCollect06Activity.this,R.string.commit_success);
                        LsCollect06Activity.this.finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect06Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect06Activity.this, R.string.load_fail);
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
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryinfo");
        BaseApplication.getRequestQueue().cancelAll("updateUserjichu");
    }

}

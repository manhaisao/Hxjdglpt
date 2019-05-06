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
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.ls.VillageAll;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 村
 */
@ContentView(R.layout.activity_ls_collect_07)
public class LsCollect07Activity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.tv_insurance_num_show)
    private TextView tvInsuranceNumShow;
    @ViewInject(R.id.tv_medical_num_show)
    private TextView tvMedicalNumShow;

    @ViewInject(R.id.et_bxfzr)
    private EditText etBxfzr;
    @ViewInject(R.id.et_bxfzrhm)
    private EditText etBxfzrhm;
    @ViewInject(R.id.et_bxjbr)
    private EditText etBxjbr;
    @ViewInject(R.id.et_bxjbrhm)
    private EditText etBxjbrhm;

    @ViewInject(R.id.et_bxfzr1)
    private EditText etBxfzr1;
    @ViewInject(R.id.et_bxfzrhm1)
    private EditText etBxfzrhm1;
    @ViewInject(R.id.et_bxjbr1)
    private EditText etBxjbr1;
    @ViewInject(R.id.et_bxjbrhm1)
    private EditText etBxjbrhm1;

    @ViewInject(R.id.btn_sb)
    private Button btnSb;


    private Village village;
    private VillageAll villageAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        village = getIntent().getParcelableExtra("village");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText(village.getName());
    }

    @Override
    public void initData() {
        super.initData();
        try {
            getVillageData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.iv_back,R.id.btn_sb}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.btn_sb:
                fromSb();
                break;
        }
    }

    /**
     * 获取村数据
     */
    private void getVillageData() throws UnsupportedEncodingException {
        SuccinctProgress.showSuccinctProgress(LsCollect07Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/queryBycun?vname="+ URLEncoder.encode(village.getName(),"utf-8");
        VolleyRequest.RequestGet(this, url, "queryBycun", new VolleyListenerInterface
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
                        villageAll = (VillageAll) gson.fromJson(result.getJSONObject("data").toString
                                (), VillageAll.class);
                        int yanglaos = result.isNull("yanglaos")?0:result.getInt("yanglaos");
                        int yiliaos = result.isNull("yiliaos")?0:result.getInt("yiliaos");

                        //设置值
                        tvInsuranceNumShow.setText(String.valueOf(yanglaos));
                        tvMedicalNumShow.setText(String.valueOf(yiliaos));

                        etBxfzr.setText(villageAll.getFzr());
                        etBxfzrhm.setText(villageAll.getFzrphone());
                        etBxjbr.setText(villageAll.getJbr());
                        etBxjbrhm.setText(villageAll.getJbrphone());

                        etBxfzr1.setText(villageAll.getFzr1());
                        etBxfzrhm1.setText(villageAll.getFzrphone1());
                        etBxjbr1.setText(villageAll.getJbr1());
                        etBxjbrhm1.setText(villageAll.getJbrphone1());

                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect07Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect07Activity.this, R.string.load_fail);
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
     * 数据提交
     */
    private void fromSb() {
        SuccinctProgress.showSuccinctProgress(LsCollect07Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/insertapp";
        if(null != villageAll){
               url= ConstantUtil.BASE_URL + "/xshs/updateapp";
        }
        HashMap<String, String> params = new HashMap<>();
        if(null != villageAll){
            params.put("id", String.valueOf(villageAll.getId()));
        }
        params.put("vname",village.getName());
        params.put("type",String.valueOf(1));
        params.put("fzr",etBxfzr.getText().toString());
        params.put("fzrphone", etBxfzrhm.getText().toString());
        params.put("jbr",etBxjbr.getText().toString());
        params.put("jbrphone",etBxjbrhm.getText().toString());
        params.put("fzr1",etBxfzr1.getText().toString());
        params.put("fzrphone1", etBxfzrhm1.getText().toString());
        params.put("jbr1",etBxjbr1.getText().toString());
        params.put("jbrphone1",etBxjbrhm1.getText().toString());
        VolleyRequest.RequestPost(this, url, "xshs", params, new VolleyListenerInterface
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
                        ToastUtil.show(LsCollect07Activity.this,R.string.commit_success);
                        LsCollect07Activity.this.finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect07Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect07Activity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryBycun");
        BaseApplication.getRequestQueue().cancelAll("xshs");
    }
}

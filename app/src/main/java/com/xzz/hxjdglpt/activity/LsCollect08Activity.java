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
import com.xzz.hxjdglpt.model.ls.LsGrid;
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
 * 网格
 */
@ContentView(R.layout.activity_ls_collect_08)
public class LsCollect08Activity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.et_insurance_num)
    private EditText etInsuranceNum;
    @ViewInject(R.id.et_medical_num)
    private EditText etMedicalNum;

    @ViewInject(R.id.et_ls_wgzrr)
    private EditText etLsWgzrr;
    @ViewInject(R.id.et_ls_wgzrr_phone)
    private EditText etLsWgzrrPhone;

    @ViewInject(R.id.et_ls_wgzrr1)
    private EditText etLsWgzrr1;
    @ViewInject(R.id.et_ls_wgzrr_phone1)
    private EditText etLsWgzrrPhone1;

    @ViewInject(R.id.btn_sb)
    private Button btnSb;

    private Grid grid;
    private LsGrid lsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        grid = getIntent().getParcelableExtra("grid");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText(grid.getZm());
    }

    @Override
    public void initData() {
        super.initData();
        getGridData();
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
    private void getGridData() {
        SuccinctProgress.showSuccinctProgress(LsCollect08Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/listbygid";
        HashMap<String, String> params = new HashMap<>();
        params.put("gid",grid.getId());
        VolleyRequest.RequestPost(this, url,"listbygid",params,new VolleyListenerInterface
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
                        lsGrid = (LsGrid) gson.fromJson(result.getJSONObject("data").toString
                                (), LsGrid.class);

                        //设置值
                        etInsuranceNum.setText(lsGrid.getYlbx());
                        etMedicalNum.setText(lsGrid.getJbylbx());

                        etLsWgzrr.setText(lsGrid.getGridzrr());
                        etLsWgzrrPhone.setText(lsGrid.getGridzrrphone());

                        etLsWgzrr1.setText(lsGrid.getGridzrryiliao());
                        etLsWgzrrPhone1.setText(lsGrid.getGridzrrphoneyiliao());


                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect08Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect08Activity.this, R.string.load_fail);
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
        SuccinctProgress.showSuccinctProgress(LsCollect08Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/insertappgrid";
        if(null != lsGrid && lsGrid.getId() !=0){
            url= ConstantUtil.BASE_URL + "/xshs/updateappgrid";
        }
        HashMap<String, String> params = new HashMap<>();
        if(null != lsGrid && lsGrid.getId() !=0){
            params.put("id", String.valueOf(lsGrid.getId()));
        }
        params.put("pid",grid.getvId());
        params.put("type",String.valueOf(1));
        params.put("gridid",grid.getId());
        params.put("ylbx",etInsuranceNum.getText().toString());
        params.put("jbylbx",etMedicalNum.getText().toString());
        params.put("gridzrr",etLsWgzrr.getText().toString());
        params.put("gridzrrphone", etLsWgzrrPhone.getText().toString());
        params.put("gridzrryiliao",etLsWgzrr1.getText().toString());
        params.put("gridzrrphoneyiliao",etLsWgzrrPhone1.getText().toString());
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
                        ToastUtil.show(LsCollect08Activity.this,R.string.commit_success);
                        LsCollect08Activity.this.finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect08Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect08Activity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("listbygid");
        BaseApplication.getRequestQueue().cancelAll("xshs");
    }
}

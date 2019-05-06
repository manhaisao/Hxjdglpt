package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.ls.Baozhang;
import com.xzz.hxjdglpt.model.ls.Zhongcai;
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
 * 劳动监察、劳动争议仲裁信息
 */
@ContentView(R.layout.activity_ls_collect_04)
public class LsCollect04Activity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.et_jcbumen)
    private EditText etJcbumen;
    @ViewInject(R.id.et_jcadress)
    private EditText etJcadress;
    @ViewInject(R.id.tv_jcjdgly_num)
    private TextView tvJcjdglyNum;
    @ViewInject(R.id.iv_jcjdgly)
    private ImageView ivJcjdgly;
    @ViewInject(R.id.btn_sb)
    private Button btnSb;

    private int id;

    private boolean isAdd;
    private Zhongcai zhongcai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        isAdd = getIntent().getBooleanExtra("isAdd",true);
        zhongcai = (Zhongcai) bundle.getSerializable("zhongcai");
        x.view().inject(this);
        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText("劳动监察、劳动争议仲裁");
        if(!isAdd){
            id = zhongcai.getId();
            etJcbumen.setText(zhongcai.getBumen());
            etJcadress.setText(zhongcai.getAddress());
            tvJcjdglyNum.setText(zhongcai.getPs().size()+"");
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Event(value = {R.id.iv_back,R.id.iv_jcjdgly,R.id.btn_sb}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_jcjdgly:
                if(id == 0){
                    ToastUtil.show(this,"请先保存当前数据");
                }else{
                    intent.putExtra("pid",id);
                    intent.putExtra("type", 75);
                    intent.setClass(LsCollect04Activity.this, ListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_sb:
                formSb();
                break;
        }
    }

    /**
     * 数据提交
     */
    private void formSb() {
        SuccinctProgress.showSuccinctProgress(LsCollect04Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zyzc/insertUser";
        if(!isAdd){
               url = ConstantUtil.BASE_URL + "/zyzc/updateUser";
        }
        HashMap<String, String> params = new HashMap<>();
        if(!isAdd){
            params.put("id",String.valueOf(zhongcai.getId()));
        }
        params.put("bumen", etJcbumen.getText().toString());
        params.put("address", etJcadress.getText().toString());
        VolleyRequest.RequestPost(this, url, "insertUser", params, new VolleyListenerInterface
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
                        if(isAdd){
                            id = result.getInt("id");
                        }
                        ToastUtil.show(LsCollect04Activity.this, R.string.commit_success);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect04Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect04Activity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("insertUser");
    }
}

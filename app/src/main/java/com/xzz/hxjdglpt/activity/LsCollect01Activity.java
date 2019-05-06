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
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.ls.Work;
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
 * 社会劳动保障工作站信息
 */
@ContentView(R.layout.activity_ls_collect_01)
public class LsCollect01Activity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.et_gzzmc)
    private EditText etGzzmc;
    @ViewInject(R.id.et_gzzfwfw)
    private EditText etGzzfwfw;
    @ViewInject(R.id.et_gzzbgdz)
    private EditText etGzzbgdz;
    @ViewInject(R.id.et_gzzfzr)
    private EditText etGzzfzr;
    @ViewInject(R.id.et_gzzfzrdh)
    private EditText etGzzfzrdh;
    @ViewInject(R.id.btn_gzz_sb)
    private Button btnGzzSb;

    private boolean isAdd;
    private Work work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        isAdd = getIntent().getBooleanExtra("isAdd",true);
        work = (Work) bundle.getSerializable("work");
        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText("社会劳动保障工作站");
        if(!isAdd){
            etGzzmc.setText(work.getName());
            etGzzfwfw.setText(work.getFuwu());
            etGzzbgdz.setText(work.getAddress());
            etGzzfzr.setText(work.getFzr());
            etGzzfzrdh.setText(work.getFzrphone());
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Event(value = {R.id.iv_back,R.id.btn_gzz_sb}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.btn_gzz_sb:
                gzzSb();
                break;
        }
    }

    /**
     * 提交工作站信息
     */
    private void gzzSb() {
        SuccinctProgress.showSuccinctProgress(LsCollect01Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/work1/insertUser";
        if(!isAdd){
               url = ConstantUtil.BASE_URL + "/work1/updateUser";
        }
        HashMap<String, String> params = new HashMap<>();
        if(!isAdd){
            params.put("id",String.valueOf(work.getId()));
        }
        params.put("name", etGzzmc.getText().toString());
        params.put("fuwu", etGzzfwfw.getText().toString());
        params.put("address", etGzzbgdz.getText().toString());
        params.put("fzr", etGzzfzr.getText().toString());
        params.put("fzrphone", etGzzfzrdh.getText().toString());
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
                        LsCollect01Activity.this.finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect01Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect01Activity.this, R.string.load_fail);
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

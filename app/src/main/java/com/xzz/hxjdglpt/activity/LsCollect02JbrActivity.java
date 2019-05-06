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
import com.xzz.hxjdglpt.model.ls.Shbx;
import com.xzz.hxjdglpt.model.ls.ShbxJbr;
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
 * 部门经办人
 */
@ContentView(R.layout.activity_ls_collect_02_jbr)
public class LsCollect02JbrActivity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.et_bmfzr)
    private EditText etBmfzr;
    @ViewInject(R.id.et_fzrdh)
    private EditText etFzrdh;
    @ViewInject(R.id.btn_sb)
    private Button btnSb;

    private int id;

    private boolean isAdd;
    private int pid;
    private ShbxJbr shbxJbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        isAdd = getIntent().getBooleanExtra("isAdd",true);
        pid = getIntent().getIntExtra("pid",0);
        shbxJbr = (ShbxJbr) bundle.getSerializable("shbxJbr");
        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText("部门经办人");
        if(!isAdd){
            id = shbxJbr.getId();
            etBmfzr.setText(shbxJbr.getJbr());
            etFzrdh.setText(shbxJbr.getJbrphone());
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Event(value = {R.id.iv_back,R.id.btn_sb}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
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
     * 数据提交
     */
    private void formSb() {
        SuccinctProgress.showSuccinctProgress(LsCollect02JbrActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/shbx/insertUser";
        if(!isAdd){
            url = ConstantUtil.BASE_URL + "/shbx/updateUser";
        }
        HashMap<String, String> params = new HashMap<>();
        if(!isAdd){
            params.put("id",String.valueOf(shbxJbr.getId()));
        }
        params.put("pid",String.valueOf(pid));
        params.put("jbr", etBmfzr.getText().toString());
        params.put("jbrphone", etFzrdh.getText().toString());
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
                        ToastUtil.show(LsCollect02JbrActivity.this, R.string.commit_success);
                        LsCollect02JbrActivity.this.finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect02JbrActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect02JbrActivity.this, R.string.load_fail);
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

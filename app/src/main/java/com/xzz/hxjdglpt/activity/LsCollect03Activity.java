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
import com.xzz.hxjdglpt.model.ls.Shbx;
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
 * 被征地农民保障服务信息
 */
@ContentView(R.layout.activity_ls_collect_03)
public class LsCollect03Activity extends BaseActivity{

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
    @ViewInject(R.id.tv_jbr_num)
    private TextView tvJbrNum;
    @ViewInject(R.id.iv_jbr)
    private ImageView ivJbr;
    @ViewInject(R.id.btn_sb)
    private Button btnSb;

    private int id;

    private boolean isAdd;
    private Baozhang baozhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        isAdd = getIntent().getBooleanExtra("isAdd",true);
        baozhang = (Baozhang) bundle.getSerializable("baozhang");
        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText("被征地农民保障服务");
        if(!isAdd){
            id = baozhang.getId();
            etBmfzr.setText(baozhang.getFzr());
            etFzrdh.setText(baozhang.getFzrphone());
            tvJbrNum.setText(baozhang.getPs().size()+"");
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Event(value = {R.id.iv_back,R.id.iv_jbr,R.id.btn_sb}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_jbr:
                if(id == 0){
                    ToastUtil.show(this,"请先保存当前数据");
                }else{
                    intent.putExtra("pid",id);
                    intent.putExtra("type", 74);
                    intent.setClass(LsCollect03Activity.this, ListActivity.class);
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
        SuccinctProgress.showSuccinctProgress(LsCollect03Activity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/bzfw/insertUser";
        if(!isAdd){
               url = ConstantUtil.BASE_URL + "/bzfw/updateUser";
        }
        HashMap<String, String> params = new HashMap<>();
        if(!isAdd){
            params.put("id",String.valueOf(baozhang.getId()));
        }
        params.put("fzr", etBmfzr.getText().toString());
        params.put("fzrphone", etFzrdh.getText().toString());
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
                        ToastUtil.show(LsCollect03Activity.this, R.string.commit_success);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsCollect03Activity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsCollect03Activity.this, R.string.load_fail);
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

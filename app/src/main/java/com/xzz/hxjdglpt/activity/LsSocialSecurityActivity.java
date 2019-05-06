package com.xzz.hxjdglpt.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.ls.Shbx;
import com.xzz.hxjdglpt.model.ls.Work;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * 代办社会保险
 */
@ContentView(R.layout.activity_ls_social_security)
public class LsSocialSecurityActivity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.sxgz_content)
    private TextView mContent;
    @ViewInject(R.id.sxgz_btn)
    private Button allBtn;

    @ViewInject(R.id.tv_bmfzr)
    private TextView tvBmfzr;
    @ViewInject(R.id.tv_bmfzr_phone)
    private TextView tvBmfzrPhone;
    @ViewInject(R.id.tv_bmjbr)
    private TextView tvBmjbr;

    private int pageIndex = 0;

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
        hXtitle.setText(R.string.ls_cx_04);
    }

    @Override
    public void initData() {
        super.initData();
        loadData();
    }

    @Event(value = {R.id.iv_back,R.id.sxgz_btn}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.sxgz_btn:
                if ("展开".equals(allBtn.getText())) {
                    allBtn.setText("收起");
                    Drawable drawable = getResources().getDrawable(R.mipmap.min_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    mContent.setMaxLines(10000);
                } else if ("收起".equals(allBtn.getText())) {
                    allBtn.setText("展开");
                    Drawable drawable = getResources().getDrawable(R.mipmap.all_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    mContent.setMaxLines(5);
                }
                break;
        }
    }

    /**
     * 获取代办社会保险信息
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(LsSocialSecurityActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/shbx/queryByPage?pageIndex="+String.valueOf(pageIndex)+"&pageSize="+String.valueOf(ConstantUtil.PAGE_SIZE);
        VolleyRequest.RequestGet(this, url, "queryShbx", new VolleyListenerInterface
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
                        JSONArray jsonArray  = result.getJSONArray("data");
                        if(jsonArray.length()>0){
                            //部门负责人
                            JSONObject object = (JSONObject) jsonArray.get(0);
                            tvBmfzr.setText(object.getString("fzr"));
                            tvBmfzrPhone.setText(object.getString("fzrphone"));
                            //部门经办人
                            JSONArray jbrArr = object.getJSONArray("ps");
                            Gson gson = new Gson();
                            List<Shbx> objs = gson.fromJson(jbrArr.toString(), new
                                    TypeToken<List<Shbx>>() {
                                    }.getType());
                            StringBuffer sb  = new StringBuffer();
                            for (Shbx shbx:objs){
                                if(sb == null || sb.toString().equals("")){
                                    sb.append(shbx.getJbr()+"  "+shbx.getJbrphone());
                                }else{
                                    sb.append("\n"+shbx.getJbr()+"  "+shbx.getJbrphone());
                                }
                            }
                            tvBmjbr.setText(sb.toString());
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsSocialSecurityActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsSocialSecurityActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryShbx");
    }
}

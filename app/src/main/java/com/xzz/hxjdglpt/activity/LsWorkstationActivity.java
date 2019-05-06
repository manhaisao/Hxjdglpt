package com.xzz.hxjdglpt.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.LsVillageAdapter;
import com.xzz.hxjdglpt.adapter.LsWorkstationAdapter;
import com.xzz.hxjdglpt.customview.NoScrollRecyclerView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Tshd;
import com.xzz.hxjdglpt.model.Village;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 劳动保障工作站
 */
@ContentView(R.layout.activity_ls_workstation)
public class LsWorkstationActivity extends BaseActivity{

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

    @ViewInject(R.id.rv_workstation)
    private NoScrollRecyclerView rvWorkstation;

    private int pageIndex = 0;
    private List<Work> list = new ArrayList<>();
    private LsWorkstationAdapter adapter;

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
        hXtitle.setText(R.string.ls_cx_03_title);
        //初始化recyleView
        adapter = new LsWorkstationAdapter(this,list);
        rvWorkstation.setLayoutManager(new LinearLayoutManager(this));
        rvWorkstation.setAdapter(adapter);
        rvWorkstation.setNestedScrollingEnabled(false);
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
     * 获取工作站
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(LsWorkstationActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/work1/queryByPage?pageIndex="+String.valueOf(pageIndex)+"&pageSize="+String.valueOf(ConstantUtil.PAGE_SIZE);
        VolleyRequest.RequestGet(this, url, "queryWorkstation", new VolleyListenerInterface
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
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Work> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Work>>() {
                                }.getType());
                       list.addAll(objs);
                       adapter.notifyDataSetChanged();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsWorkstationActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsWorkstationActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryWorkstation");
    }
}

package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Tkgy;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.Zlj;
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
import java.util.HashMap;
import java.util.List;

/**
 * 基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_zlj_info)
public class ZljInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Zlj zlj;
    private String gridId;
    @ViewInject(R.id.zlj_d_name)
    private TextView mName;
    @ViewInject(R.id.zlj_d_cun)
    private TextView mCun;
    @ViewInject(R.id.zlj_d_grid)
    private TextView mGrid;
    @ViewInject(R.id.zlj_d_bz)
    private TextView mBz;
    @ViewInject(R.id.zlj_type)
    private TextView mType;

//    @ViewInject(R.id.viewpager)
//    private ViewPager viewPager;
//    private List<View> views = new ArrayList<View>();
//    @ViewInject(R.id.container)
//    private LinearLayout container;

//    private String[] path;

    private LayoutInflater mInflater;

    protected List<String> sourceImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        zlj = getIntent().getParcelableExtra("zlj");
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        path = cjry.getPicpath().split(",");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("80岁以上尊老金");
    }

    public void initData() {
        loadData();
    }


    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(ZljInfo.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageByGridId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
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
                        Village v = (Village) gson.fromJson(result.getJSONObject("data").toString
                                (), Village.class);
                        mName.setText(zlj.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
                        mBz.setText(zlj.getBz());
                        mType.setText(zlj.getType());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZljInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZljInfo.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryList");
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

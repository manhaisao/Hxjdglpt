package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.LsVillageAdapter;
import com.xzz.hxjdglpt.adapter.OnItemClickListener;
import com.xzz.hxjdglpt.customview.NoScrollRecyclerView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.ls.Bx;
import com.xzz.hxjdglpt.model.ls.Shbx;
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
 * 城乡居民基本养老保险
 */
@ContentView(R.layout.activity_ls_residents_insurance)
public class LsResidentsInsuranceActivity extends BaseActivity {

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

    @ViewInject(R.id.tv_insurance_num)
    private TextView tvInsuranceNum;
    @ViewInject(R.id.tv_sjywzgbm)
    private TextView tvSjywzgbm;
    @ViewInject(R.id.tv_lxdh)
    private TextView tvLxdh;
    @ViewInject(R.id.tv_jdznbm)
    private TextView tvJdznbm;
    @ViewInject(R.id.tv_bmfzr)
    private TextView tvBmfzr;
    @ViewInject(R.id.tv_bmfzr_phone)
    private TextView tvBmfzrPhone;
    @ViewInject(R.id.tv_bmjbr)
    private TextView tvBmjbr;
    @ViewInject(R.id.tv_bmjbr_phone)
    private TextView tvBmjbrPhone;

    @ViewInject(R.id.rv_village)
    private NoScrollRecyclerView rvVillage;

    private User user;
    private int type = 0;

    private List<Village> list = new ArrayList<>();
    private LsVillageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText(R.string.ls_cx_01_title);

        //初始化recyleView
        adapter = new LsVillageAdapter(this,list);
        rvVillage.setLayoutManager(new LinearLayoutManager(this));
        rvVillage.setAdapter(adapter);
        rvVillage.setNestedScrollingEnabled(false);
        //设置点击事件
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Village village = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("village",village);
                Intent intent = new Intent(LsResidentsInsuranceActivity.this,LsInsuranceVillageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        getResidentsInsurance();
        getVillage();
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
     * 获取村信息
     */
    public void getVillage(){
        SuccinctProgress.showSuccinctProgress(LsResidentsInsuranceActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageAndGridList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryVillageAndGridList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<Village> villages = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Village>>() {
                                        }.getType());
                                if (type == 5) {
                                    for (Village v : villages) {
                                        if ("8".equals(v.getId())) {
                                            Grid g = new Grid();
//                                            g.setId("jly");
                                            g.setZm(getString(R.string.jly));
                                            v.getgList().add(g);
                                        }
                                    }
                                } else if (type == 2) {
                                    Village v = new Village();
                                    v.setName("应急单位");
                                    villages.add(v);
                                } else if (type == 3) {
                                    Village v = new Village();
                                    v.setName("医院");
                                    villages.add(v);
                                }
                                list.addAll(villages);
                                adapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(LsResidentsInsuranceActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(LsResidentsInsuranceActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMyError");
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }
    /**
     * 获取养老保险信息
     */
    private void getResidentsInsurance() {
        String url = ConstantUtil.BASE_URL + "/xshs/queryinfoBytype?type=1";
        VolleyRequest.RequestGet(this, url, "queryinfoBytype", new VolleyListenerInterface
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
                        int insrurenNum = result.getInt("size");
                        //设置值
                        tvInsuranceNum.setText(String.valueOf(insrurenNum));
                        tvSjywzgbm.setText(bx.getBumen());
                        tvLxdh.setText(bx.getPhone());
                        tvJdznbm.setText(bx.getZhineng());
                        tvBmfzr.setText(bx.getFzr());
                        tvBmfzrPhone.setText(bx.getFzrphone());
                        tvBmjbr.setText(bx.getJbr());
                        tvBmjbrPhone.setText(bx.getJbrphone());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsResidentsInsuranceActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsResidentsInsuranceActivity.this, R.string.load_fail);
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

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryVillageAndGridList");
        BaseApplication.getRequestQueue().cancelAll("queryinfoBytype");
    }
}

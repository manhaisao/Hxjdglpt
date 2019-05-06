package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
 * 劳动保障
 */
@ContentView(R.layout.activity_ls_index)
public class LsIndexActivity extends BaseActivity {

    /*@ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;*/

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.sxgz_content)
    private TextView mContent;
    @ViewInject(R.id.sxgz_btn)
    private Button allBtn;

    @ViewInject(R.id.tv_insurance_num)
    private TextView tvInsuranceNum;
    @ViewInject(R.id.tv_medical_num)
    private TextView tvMedicalNum;
    @ViewInject(R.id.tv_gzz_num)
    private TextView tvGzzNum;
    @ViewInject(R.id.tv_shbx)
    private TextView tvShbx;
    @ViewInject(R.id.tv_bzd)
    private TextView tvBzd;
    @ViewInject(R.id.tv_ldjc)
    private TextView tvLdjc;
    @ViewInject(R.id.rv_village)
    private NoScrollRecyclerView rvVillage;

    @ViewInject(R.id.sxgz_gzdt_btn)
    private Button gzdtBtn;

    private User user;
    private int type = 0;
    private int pageIndex = 0;

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
        tvTitle.setText(R.string.ls);
        gzdtBtn.setText("劳动保障工作动态");
        gzdtBtn.setVisibility(View.VISIBLE);
        //设默认值
        tvInsuranceNum.setText("0");
        tvMedicalNum.setText("0");
        tvGzzNum.setText("0");
        //添加下划线
        tvInsuranceNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvInsuranceNum.getPaint().setAntiAlias(true);
        tvMedicalNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvMedicalNum.getPaint().setAntiAlias(true);
        tvGzzNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvGzzNum.getPaint().setAntiAlias(true);
        tvShbx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvShbx.getPaint().setAntiAlias(true);
        tvBzd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvBzd.getPaint().setAntiAlias(true);
        tvLdjc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvLdjc.getPaint().setAntiAlias(true);
        //初始化recyleView
        adapter = new LsVillageAdapter(this, list);
        rvVillage.setLayoutManager(new LinearLayoutManager(this));
        rvVillage.setAdapter(adapter);
        rvVillage.setNestedScrollingEnabled(false);
        //设置item点击事件
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Village village = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("village", village);
                Intent intent = new Intent(LsIndexActivity.this, LsVillageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        loadData();
        SuccinctProgress.showSuccinctProgress(LsIndexActivity.this, "请求数据中···", SuccinctProgress
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
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
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
                                DialogUtil.showTipsDialog(LsIndexActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(LsIndexActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.sxgz_gzdt_btn, R.id.iv_back, R.id.tv_insurance_num, R.id.tv_medical_num, R.id.tv_gzz_num, R.id.tv_shbx, R.id.tv_bzd, R.id.tv_ldjc, R.id.sxgz_btn}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.sxgz_gzdt_btn:
                intent.setClass(this, GzdtActivity.class);
                intent.putExtra("type", 20);
                startActivity(intent);
                break;
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.tv_insurance_num:
                intent.setClass(this, LsResidentsInsuranceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_medical_num:
                intent.setClass(this, LsResidentsMedicalActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_gzz_num:
                intent.setClass(this, LsWorkstationActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shbx:
                intent.setClass(this, LsSocialSecurityActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_bzd:
                intent.setClass(this, LsLandlessSafeguardActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_ldjc:
                intent.setClass(this, LsLaborSupervisionActivity.class);
                startActivity(intent);
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
     * 获取数量统计
     */
    private void loadData() {
        String url = ConstantUtil.BASE_URL + "/xshs/querySum";
        VolleyRequest.RequestGet(this, url, "querySum", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        tvInsuranceNum.setText("" + result.getInt("yanglaos"));
                        tvMedicalNum.setText("" + result.getInt("yiliaos"));
                        tvGzzNum.setText("" + result.getInt("works"));
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsIndexActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsIndexActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryShbx");
    }
}

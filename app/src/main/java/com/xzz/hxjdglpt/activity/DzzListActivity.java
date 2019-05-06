package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.DzzAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Dzb;
import com.xzz.hxjdglpt.model.Dzz;
import com.xzz.hxjdglpt.model.Liangxin;
import com.xzz.hxjdglpt.model.LsDzz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zddw;
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
import java.util.Map;

/**
 * 展示信息LIST
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_dzz_list)
public class DzzListActivity extends BaseActivity implements
        AdapterView.OnItemClickListener {

    @ViewInject(R.id.list_listview)
    private ListView listView;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private DzzAdapter adapter;
    private List<Object> list = new ArrayList<Object>();

    private int type = 0;

    private String gId;
    private String vId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        type = getIntent().getIntExtra("type", 0);
        gId = getIntent().getStringExtra("gId");
        vId = getIntent().getStringExtra("vId");
//        tvRight.setText("新增");
        initView();
//        initData();
    }

    public void initView() {
        tvRight.setVisibility(View.GONE);
        if (type == 1) {//驻地单位
            tvTitle.setText("驻地党组织");
        } else if (type == 2) {
            tvTitle.setText("两新党组织");
        } else if (type == 3) {
            tvTitle.setText("村级党组织");
        } else if (type == 4) {
            tvTitle.setText("临时党组织");
        }
        adapter = new DzzAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        initData();
    }


    public void initData() {
        request();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(DzzListActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dzz/queryList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", String.valueOf(type));
        if (gId != null) params.put("gId", gId);
        if (vId != null) params.put("vId", vId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new
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
                                Gson gson = new Gson();
                                JSONArray jsonArray = result.getJSONArray("data");
                                if (type == 1) {//驻地单位
                                    List<Zddw> objs = gson.fromJson(jsonArray.toString(), new
                                            TypeToken<List<Zddw>>() {
                                            }.getType());
                                    list.addAll(objs);
                                    adapter.notifyDataSetChanged();
                                } else if (type == 2) {
                                    List<Liangxin> objs = gson.fromJson(jsonArray.toString(), new
                                            TypeToken<List<Liangxin>>() {
                                            }.getType());
                                    list.addAll(objs);
                                    adapter.notifyDataSetChanged();
                                } else if (type == 3) {
                                    List<Dzz> objs = gson.fromJson(jsonArray.toString(), new
                                            TypeToken<List<Dzz>>() {
                                            }.getType());
                                    for (Dzz d : objs) {
                                        if (d.getHasDzz() == 1) {
                                            list.add(d);
                                        } else {
                                            List<Dzb> dzbs = d.getDzb();
                                            if (dzbs != null && dzbs.size() > 0) {
                                                Dzb dd = dzbs.get(0);
                                                list.add(dd);
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } else if (type == 4) {
                                    List<LsDzz> objs = gson.fromJson(jsonArray.toString(), new
                                            TypeToken<List<LsDzz>>() {
                                            }.getType());
                                    list.addAll(objs);
                                    adapter.notifyDataSetChanged();
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(DzzListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(DzzListActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            SuccinctProgress.dismiss();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }


    @Event(value = {R.id.iv_back_tv}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
        BaseApplication.getRequestQueue().cancelAll("delData");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < list.size()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setClass(DzzListActivity.this, DzzInfo.class);
//            intent.putExtra("type", type);
            if (type == 1) {//驻地单位
                bundle.putParcelable("dzz", (Zddw) list.get(position));
            } else if (type == 2) {
                bundle.putParcelable("dzz", (Liangxin) list.get(position));
            } else if (type == 3) {
                if (list.get(position) instanceof Dzz) {
                    bundle.putParcelable("dzz", (Dzz) list.get(position));
                } else if (list.get(position) instanceof Dzb) {
                    bundle.putParcelable("dzz", (Dzb) list.get(position));
                }
            } else if (type == 4) {
                bundle.putParcelable("dzz", (LsDzz) list.get(position));
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}

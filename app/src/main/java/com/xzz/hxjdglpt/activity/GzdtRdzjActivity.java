package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.GzdtRdzjAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.GzdtRdzj;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
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
 * Created by dbz on 2017/5/23.
 */

@ContentView(R.layout.aty_gzdt_rdzj)
public class GzdtRdzjActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.gzdt_listview)
    private AutoListView listView;
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private GzdtRdzjAdapter gzdtAdapter;
    private List<GzdtRdzj> list = new ArrayList<GzdtRdzj>();
    private int pageIndex = 0;

    private int type = 0;

    private List<Role> roles;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<GzdtRdzj> result = (List<GzdtRdzj>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            gzdtAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            tvTitle.setText("部门工作动态");
        } else if (type == 2) {
            tvTitle.setText("人大代表动态");
        }
        roles = application.getRolesList();
        initView();
        initData();
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4241".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        if (isContain()) {
            tvRight.setText("添加");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }


        gzdtAdapter = new GzdtRdzjAdapter(this, list);
        listView.setAdapter(gzdtAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        pageIndex = 0;
        loadData(AutoListView.REFRESH);
    }

    /**
     * 新闻获取
     */
    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/gzdtrdzj/queryGzdtByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", String.valueOf(type));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryGzdtByPage", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<GzdtRdzj> newses = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<GzdtRdzj>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(GzdtRdzjActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(GzdtRdzjActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                this.finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.setClass(GzdtRdzjActivity.this, GzdtRdzjFbActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryGzdtByPage");
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex + ConstantUtil.PAGE_SIZE;
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Intent intent = new Intent();
            intent.setClass(GzdtRdzjActivity.this, GzdtRdzjDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("gzdt", list.get(position - 1));
            intent.putExtras(bundle);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }
}

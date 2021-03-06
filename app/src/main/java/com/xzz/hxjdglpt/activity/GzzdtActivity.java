package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.GzdtAdapter;
import com.xzz.hxjdglpt.adapter.QfhxDtAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Gzdt;
import com.xzz.hxjdglpt.model.Qfhx;
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

@ContentView(R.layout.aty_gzdt)
public class GzzdtActivity extends BaseActivity implements AutoListView.OnRefreshListener,
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
    private QfhxDtAdapter gzdtAdapter;
    private List<Qfhx> list = new ArrayList<Qfhx>();
    private int pageIndex = 0;
    private List<Role> roles;

    private int dyId;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Qfhx> result = (List<Qfhx>) msg.obj;
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
        dyId = getIntent().getIntExtra("dyId", 0);
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
        tvTitle.setText("党建工作动态");
        gzdtAdapter = new QfhxDtAdapter(this, list);
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
        String url = ConstantUtil.BASE_URL + "/hxjdDynamic/queryHxjdDynamicByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("dyId", String.valueOf(dyId));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryHxjdDynamicByPage", params, new
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
                                List<Qfhx> newses = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Qfhx>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(GzzdtActivity.this);
                                List<Qfhx> newses = new ArrayList<>();
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(GzzdtActivity.this, R.string.load_fail);
                                List<Qfhx> newses = new ArrayList<>();
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
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
                        List<Qfhx> newses = new ArrayList<>();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
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
                intent.setClass(GzzdtActivity.this, GzzdtFbActivity.class);
                intent.putExtra("dyId", dyId);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryHxjdDynamicByPage");
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
            intent.setClass(GzzdtActivity.this, GzzdtDtDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("qfhx", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

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
import com.xzz.hxjdglpt.adapter.GzdtRdzjAdapter;
import com.xzz.hxjdglpt.adapter.RdzjDtAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.GzdtRdzj;
import com.xzz.hxjdglpt.model.TRdxw;
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
 * 人大之家
 * Created by dbz on 2017/7/13.
 */
@ContentView(R.layout.aty_rdzj)
public class RdzjActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private User user;
    private GzdtRdzjAdapter rdzjDtAdapter;
    private List<GzdtRdzj> list = new ArrayList<GzdtRdzj>();
    private int pageIndex = 0;
    @ViewInject(R.id.rdzj_listview)
    private AutoListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(getString(R.string.rdzj));
        rdzjDtAdapter = new GzdtRdzjAdapter(this, list);
        listView.setAdapter(rdzjDtAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

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
            rdzjDtAdapter.notifyDataSetChanged();
        }
    };

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/gzdtrdzj/queryGzdtByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryRdxwList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

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
                        DialogUtil.showTipsDialog(RdzjActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(RdzjActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.rdzj_gzdt_btn, R.id.rdzj_gzdy_btn, R.id.rdzj_bmzz, R.id.iv_back, R.id.rdzj_dbml, R.id.rdzj_dbfc, R.id.rdzj_qlyw, R.id.rdzj_dbzd}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rdzj_gzdt_btn:
                intent.setClass(this, GzdtRdzjFbActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
//                intent.setClass(this, GzdtRdzjActivity.class);
//                intent.putExtra("type", 1);
//                startActivity(intent);
                break;
            case R.id.rdzj_gzdy_btn:
                intent.setClass(this, GzdtRdzjActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.rdzj_bmzz:
                intent.setClass(RdzjActivity.this, RdzjBmzzActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.rdzj_dbml:
                intent.setClass(RdzjActivity.this, RdzjDbmlActivity.class);
                startActivity(intent);
                break;
            case R.id.rdzj_dbfc:
                intent.setClass(RdzjActivity.this, RdzjDbfcActivity.class);
                startActivity(intent);
                break;
            case R.id.rdzj_qlyw:
                intent.setClass(RdzjActivity.this, RdzjQlywActivity.class);
                startActivity(intent);
                break;
            case R.id.rdzj_dbzd:
                intent.setClass(RdzjActivity.this, RdzjDbzdActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryRdxwList");
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
            intent.setClass(RdzjActivity.this, RdzjDtDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("trdxw", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

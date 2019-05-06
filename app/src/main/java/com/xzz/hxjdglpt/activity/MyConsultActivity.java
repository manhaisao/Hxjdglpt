package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.FwmdmAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Fwmdm;
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
 * 我的咨询
 * Created by dbz on 2017/5/23.
 */
@ContentView(R.layout.aty_myconsult)
public class MyConsultActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.myconsult_listview)
    private AutoListView listView;

    private User user;
    private FwmdmAdapter fwmdmAdapter;
    private List<Fwmdm> list = new ArrayList<Fwmdm>();
    private int pageIndex = 0;

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

    public void initView() {
        tvTitle.setText(getText(R.string.my_consult));
        fwmdmAdapter = new FwmdmAdapter(this, list);
        listView.setAdapter(fwmdmAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Fwmdm> result = (List<Fwmdm>) msg.obj;
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
            fwmdmAdapter.notifyDataSetChanged();
        }
    };

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/m_fwmdm/queryFwmdmListByUserId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("title", "");
        VolleyRequest.RequestPost(this, url, "fwmdm_queryList", params, new
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
                        List<Fwmdm> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Fwmdm>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(MyConsultActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(MyConsultActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("fwmdm_queryList");
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
            intent.setClass(MyConsultActivity.this, FwmdmDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("fwmdm", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}

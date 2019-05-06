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
import com.xzz.hxjdglpt.adapter.MqztcAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Mqztc;
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
 * 民情直通车
 * Created by dbz on 2017/5/23.
 */
@ContentView(R.layout.aty_mqztc)
public class MqztcActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;

    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.mqztc_listview)
    private AutoListView listView;

    private User user;
    private MqztcAdapter mqztcAdapter;
    private List<Mqztc> list = new ArrayList<Mqztc>();
    private int pageIndex = 0;
    @ViewInject(R.id.mqztc_search_edt)
    private EditText edtSearch;
    @ViewInject(R.id.mqztc_search)
    private Button btnSearch;

    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        initView();
    }

    public void initView() {
        tvTitle.setText(getText(R.string.mqztc));
        tvRight.setText(getText(R.string.question_self));
        mqztcAdapter = new MqztcAdapter(this, list);
        listView.setAdapter(mqztcAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4245".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageIndex = 0;
        initData();
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Mqztc> result = (List<Mqztc>) msg.obj;
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
            mqztcAdapter.notifyDataSetChanged();
        }
    };

    private void loadData(final int what) {
        String url = "";
        if (isContain()) {
            url = ConstantUtil.BASE_URL + "/m_mqztc/queryAllMqztcListByRole";
        } else {
            url = ConstantUtil.BASE_URL + "/m_mqztc/queryMqztcListByKey";
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("title", edtSearch.getText().toString());
        VolleyRequest.RequestPost(this, url, "mqztc_queryList", params, new
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
                        List<Mqztc> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Mqztc>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(MqztcActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(MqztcActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right, R.id.mqztc_search}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.setClass(MqztcActivity.this, MqztcQuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.mqztc_search:
                SuccinctProgress.showSuccinctProgress(MqztcActivity.this, "数据查找中···",
                        SuccinctProgress.THEME_ULTIMATE, false, true);
                loadData(AutoListView.REFRESH);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("mqztc_queryList");
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        edtSearch.setText("");
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
            intent.setClass(MqztcActivity.this, MqztcDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("mqztc", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

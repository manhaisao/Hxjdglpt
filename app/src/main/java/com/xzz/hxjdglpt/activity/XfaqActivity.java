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
import com.xzz.hxjdglpt.adapter.XfaqjcAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Xfaqjc;
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
 * 信访诉求人员工作日志
 * Created by dbz on 2017/5/23.
 */
@ContentView(R.layout.aty_gzdt)
public class XfaqActivity extends BaseActivity implements AutoListView.OnRefreshListener,
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
    private XfaqjcAdapter adapter;
    private List<Xfaqjc> list = new ArrayList<Xfaqjc>();
    private int pageIndex = 0;

    private String pId;

    private int type;

    private String gridId;

    private List<Role> roles;

    private String currDate;

    private String cType;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Xfaqjc> result = (List<Xfaqjc>) msg.obj;
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
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        pId = getIntent().getStringExtra("pId");
        type = getIntent().getIntExtra("type", 0);
        gridId = getIntent().getStringExtra("gridId");
        currDate = getIntent().getStringExtra("currDate");
        cType = getIntent().getStringExtra("cType");
        initView();
        initData();
    }


    private boolean isXfaq() {
        for (Role r : roles) {
            if ("4253".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isScaq() {
        for (Role r : roles) {
            if ("4252".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        if (type == 1 && isXfaq()) {
            tvRight.setText("添加");
            tvRight.setVisibility(View.VISIBLE);
        } else if (type == 2 && isScaq()) {
            tvRight.setText("添加");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        if (type == 1) {
            tvTitle.setText("消防安全检查");
        } else if (type == 2) {
            tvTitle.setText("安全生产检查");
        }
        adapter = new XfaqjcAdapter(this, list);
        listView.setAdapter(adapter);
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

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/xfaqjc/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (pId != null) params.put("pId", pId);
        if (gridId != null) params.put("gridId", gridId);
        params.put("type", String.valueOf(type));
        if (currDate != null) params.put("jcstime", currDate);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        if (cType != null) params.put("ctype", cType);
        VolleyRequest.RequestPost(this, url, "queryListByPage", params, new
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
                                List<Xfaqjc> newses = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Xfaqjc>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(XfaqActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(XfaqActivity.this, R.string.load_fail);
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
                finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.setClass(XfaqActivity.this, XfaqFbActivity.class);
                if (pId != null) intent.putExtra("pId", pId);
                if (gridId != null) intent.putExtra("gridId", gridId);
                intent.putExtra("type", type);
                intent.putExtra("cType", cType);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryListByPage");
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
            intent.setClass(XfaqActivity.this, XfaqDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("xfaqjc", list.get(position - 1));
            intent.putExtras(bundle);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }
}

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
import com.xzz.hxjdglpt.adapter.JyfwAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnLoadListener;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnRefreshListener;
import com.xzz.hxjdglpt.model.Jyfw;
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
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_jyfw)
public class JyfwActivity extends BaseActivity implements OnRefreshListener, OnLoadListener,
        AdapterView.OnItemClickListener {

    @ViewInject(R.id.jyfw_listview)
    private AutoListView listView;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;

    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private JyfwAdapter sxAdapter;
    private List<Jyfw> list = new ArrayList<Jyfw>();
    private int pageIndex = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Jyfw> result = (List<Jyfw>) msg.obj;
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
            sxAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        initView();
//        initData();
    }

    public void initView() {
        tvTitle.setText("就业服务");
        if (isGly()) {
            tvRight.setText("发布");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        sxAdapter = new JyfwAdapter(this, list);
        listView.setAdapter(sxAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    private boolean isGly() {
        for (Role role : application.getRolesList()) {
            if ("4237".equals(role.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initData() {
        pageIndex = 0;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 工作反馈获取
     */
    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/jyfw/queryJyfwList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryJyfwList", params, new VolleyListenerInterface
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
                        List<Jyfw> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jyfw>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyfwActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyfwActivity.this, R.string.load_fail);
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
                intent.setClass(this, JyfwFbActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryJyfwList");
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
            intent.setClass(JyfwActivity.this, JyfwDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("jyfw", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

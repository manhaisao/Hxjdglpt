package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.YzyhAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Yzyh;
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
import java.util.List;

/**
 * Created by Administrator on 2019\3\31 0031.
 */
@ContentView(R.layout.aty_gzdt)
public class YzyhActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.rl_search)
    private RelativeLayout rl_search;
    @ViewInject(R.id.mqztc_search_edt)
    private EditText mqztc_search_edt;

    @ViewInject(R.id.gzdt_listview)
    private AutoListView listView;
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private YzyhAdapter gzdtAdapter;
    private List<Yzyh> list = new ArrayList<Yzyh>();
    private int pageIndex = 1;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Yzyh> result = (List<Yzyh>) msg.obj;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }
//    private boolean isContain() {
//        for (Role r : roles) {
//            if ("4241".equals(r.getRoleId())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public void initView() {
//        if (isContain()) {
//            tvRight.setText("添加");
//            tvRight.setVisibility(View.VISIBLE);
//        } else {
        tvRight.setVisibility(View.GONE);
//        }
        tvTitle.setText("应知应会");
        gzdtAdapter = new YzyhAdapter(this, list);
        listView.setAdapter(gzdtAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
        rl_search.setVisibility(View.VISIBLE);
        pageIndex = 1;
        loadData(AutoListView.REFRESH);
    }

    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadData(final int what) {
        String url;
        if (TextUtils.isEmpty(mqztc_search_edt.getText().toString())) {
            url = ConstantUtil.BASE_URL + "/yzyh?pageNum=" + pageIndex + "&pageSize=" + ConstantUtil.PAGE_SIZE;
        } else {
            url = ConstantUtil.BASE_URL + "/yzyh?pageNum=" + pageIndex + "&pageSize=" + ConstantUtil.PAGE_SIZE + "&title=" + mqztc_search_edt.getText().toString();
        }
        VolleyRequest.RequestGet(this, url, "yzyh", new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("code");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("0".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("result");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<Yzyh> newses = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Yzyh>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(YzyhActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(YzyhActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.mqztc_search}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                this.finish();
                break;
            case R.id.mqztc_search:
                pageIndex = 1;
                loadData(AutoListView.REFRESH);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("yzyh");
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
            intent.setClass(YzyhActivity.this, YzyhDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("news", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

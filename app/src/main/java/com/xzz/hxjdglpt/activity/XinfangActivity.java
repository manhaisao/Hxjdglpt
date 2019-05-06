package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.GgAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.fragment.RsrmFragment;
import com.xzz.hxjdglpt.model.Gonggao;
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
 * 公告公示
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_xinfang)
public class XinfangActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

//    @ViewInject(R.id.gg_tab)
//    private RadioGroup rgChannel;
//    private RsrmFragment rsrmFragment_1;
//        private RsrmFragment rsrmFragment_2;
    @ViewInject(R.id.gg_listview)
    private AutoListView listView;

    private GgAdapter newsAdapter;
    private List<Gonggao> list = new ArrayList<Gonggao>();
    private User user;
    private BaseApplication application;
    private ImageLoader mImageLoader;
    private int pageIndex = 0;

    private String type = "";
        private String tabNames[] = {"人事任免", "公告栏"};
//
    private FragmentManager manager;
    private FragmentTransaction transaction;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Gonggao> result = (List<Gonggao>) msg.obj;
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
            newsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(type);
        newsAdapter = new GgAdapter(this, list, mImageLoader);
        listView.setAdapter(newsAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);

//
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.
//                SOFT_INPUT_ADJUST_PAN);
//        //锁定屏幕
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        rgChannel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                onItemSelected(checkedId);
//            }
//        });
//        initTab();//动态产生RadioButton
//        rgChannel.check(0);
//    }
//
//        public void onItemSelected(int tabId) {
//        manager = getFragmentManager();
//        transaction = manager.beginTransaction();
//        switch (tabId) {
//            case 0:
//                hideFragment();
//                if (rsrmFragment_1 == null) {
//                    rsrmFragment_1 = new RsrmFragment();
//                    rsrmFragment_1.setType(tabNames[0]);
//                    transaction.add(R.id.main_content, rsrmFragment_1);
//                } else {
//                    transaction.show(rsrmFragment_1);
//                }
//                transaction.commit();
//                break;
//            case 1:
//                hideFragment();
//                if (rsrmFragment_2 == null) {
//                    rsrmFragment_2 = new RsrmFragment();
//                    rsrmFragment_2.setType(tabNames[1]);
//                    transaction.add(R.id.main_content, rsrmFragment_2);
//                } else {
//                    transaction.show(rsrmFragment_2);
//                }
//                transaction.commit();
//                break;
//        }
    }

//    private void hideFragment() {
//        if (rsrmFragment_1 != null) {
//            transaction.hide(rsrmFragment_1);
//        }
//        if (rsrmFragment_2 != null) {
//            transaction.hide(rsrmFragment_2);
//        }
//    }
//
//    private void initTab() {
//        for (int i = 0; i < tabNames.length; i++) {
//            RadioButton rb = (RadioButton) LayoutInflater.from(this).
//                    inflate(R.layout.tab_rb, null);
//            rb.setId(i);
//            rb.setText(tabNames[i]);
//            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams
//                    .WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f);
//            rb.setLayoutParams(params);
//            rgChannel.addView(rb, i);
//        }
//    }
    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/gg/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("type", type);
        params.put("pageIndex", String.valueOf(pageIndex));
        VolleyRequest.RequestPost(XinfangActivity.this, url, "queryListByPage", params,
                new VolleyListenerInterface(XinfangActivity.this,
                        VolleyListenerInterface.mListener,
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
                                List<Gonggao> newses = gson.fromJson(jsonArray.toString(), new TypeToken<List<Gonggao>>() {
                                }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(XinfangActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(XinfangActivity.this, R.string.load_fail);
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
            intent.setClass(this, GgDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("gg", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }


}

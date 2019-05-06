package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.CpbzAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Cpbz;
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
 * Created by Administrator on 2019/3/25 0025.
 */


@ContentView(R.layout.aty_cpbz)
public class CpbzActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    //标题
    @ViewInject(R.id.hx_title)
    TextView hx_title;

    //右侧菜单
    @ViewInject(R.id.hx_btn_right)
    ImageView hx_btn_right;

    @ViewInject(R.id.cpbz_listview)
    AutoListView listView;

    private User user;
    private BaseApplication application;
    private CpbzAdapter rdzjDtAdapter;
    private List<Cpbz> list = new ArrayList<Cpbz>();
    private int pageIndex = 1;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        initView();
        initData();
    }

    public void initView() {
        hx_title.setText("测评标准");
        hx_btn_right.setVisibility(View.GONE);
        rdzjDtAdapter = new CpbzAdapter(this, list, mImageLoader);
        listView.setAdapter(rdzjDtAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    @Event(value = {R.id.iv_back}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Cpbz> result = (List<Cpbz>) msg.obj;
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
        String url = ConstantUtil.BASE_URL + "/evaluationStandards?pageNum=" + pageIndex + "&pageSize=" + ConstantUtil.PAGE_SIZE;
        Log.e("insert", url);
        VolleyRequest.RequestGet(this, url, "evaluationStandards", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                Log.e("insert", result.toString());
//                LogUtil.i(result.toString());
                try {
                    String resultCode = result.getString("result");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if (!TextUtils.isEmpty(resultCode)) {
//                        JSONArray jsonArray = result.getJSONArray("data");
//                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Cpbz> newses = gson.fromJson(resultCode, new
                                TypeToken<List<Cpbz>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else {
                        ToastUtil.show(CpbzActivity.this, R.string.load_fail);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Intent intent = new Intent();
            intent.setClass(CpbzActivity.this, CpbzDetailActivity.class);
            intent.putExtra("top", list.get(position - 1));
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex + 1;
        loadData(AutoListView.LOAD);
    }
}

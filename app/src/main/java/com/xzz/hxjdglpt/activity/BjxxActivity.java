package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.BjxxAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Bjxx;
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
import java.util.List;



/**
 * Created by Administrator on 2019/4/9 0009.
 */
@ContentView(R.layout.aty_bjxx)
public class BjxxActivity extends  BaseActivity  implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.rdzj_listview)
    private AutoListView listView;
    private ImageLoader mImageLoader;
    private User user;
    private BaseApplication application;
    private List<Role> roles;
    private BjxxAdapter adapter;
    private List<Bjxx> list = new ArrayList<Bjxx>();
    private int pageIndex = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        roles = application.getRolesList();
        tvTitle.setText("背街小巷");
        adapter=new BjxxAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
        loadData(AutoListView.REFRESH);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Bjxx> result = (List<Bjxx>) msg.obj;
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

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/tHjxxInfos?pageNum="+pageIndex+"&pageSize=20";

        VolleyRequest.RequestGet(this, url, "queryQfhxList", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                Log.e("insert",result.toString());
//                LogUtil.i(result.toString());
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("0".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("result");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Bjxx> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Bjxx>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(BjxxActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(BjxxActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("queryQfhxList");
    }
    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex ++;
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i - 1 < list.size()) {
            Intent intent = new Intent();
            intent.setClass(BjxxActivity.this, BjxxInfoActicity.class);
            intent.putExtra("bjxx", list.get(i - 1));
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

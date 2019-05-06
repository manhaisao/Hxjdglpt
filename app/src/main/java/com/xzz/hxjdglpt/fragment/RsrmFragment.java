package com.xzz.hxjdglpt.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.GgDetailActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.adapter.GgAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
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
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 人事任免
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.fragment_rsrm)
public class RsrmFragment extends BaseFragment implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.gg_listview)
    private AutoListView listView;

    private GgAdapter newsAdapter;
    private List<Gonggao> list = new ArrayList<Gonggao>();
    private User user;
    private BaseApplication application;
    private ImageLoader mImageLoader;
    private int pageIndex = 0;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        application = (BaseApplication) getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application
                .getImageLoader();
        initView();
        initData();
        return view;
    }

    public void initView() {
        newsAdapter = new GgAdapter(getActivity(), list, mImageLoader);
        listView.setAdapter(newsAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

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
        VolleyRequest.RequestPost(getActivity(), url, "queryListByPage", params,
                new VolleyListenerInterface(getActivity(),
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
                                DialogUtil.showTipsDialog(getActivity());
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(getActivity(), R.string.load_fail);
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
            intent.setClass(getActivity(), GgDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("gg", list.get(position - 1));
            bundle.putString("type",type);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

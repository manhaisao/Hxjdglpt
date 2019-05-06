package com.xzz.hxjdglpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.PositionDetail;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.DwDetailInfoActivity;
import com.xzz.hxjdglpt.activity.QfhxActivity;
import com.xzz.hxjdglpt.activity.QfhxDtDetailActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.adapter.DwDetailAdapter;
import com.xzz.hxjdglpt.adapter.QfhxAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.DwType;
import com.xzz.hxjdglpt.model.Qfhx;
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
 * Created by xieyu on 2019/3/30.
 */
@ContentView(R.layout.fragment_dw_detail)
public class DwTypeFragment extends BaseFragment   implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener  {

    @ViewInject(R.id.dw_list)
    public AutoListView listView;
    private ImageLoader mImageLoader;
    private DwDetailAdapter rdzjDtAdapter;
    private List<DwType> list = new ArrayList<DwType>();
    private int pageIndex = 1;
    private User user;
    private BaseApplication application;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        application =(BaseApplication)getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        initView();
        initData();
        return view;
    }

    public void initView() {
        rdzjDtAdapter = new DwDetailAdapter(getActivity(), list);
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
            List<DwType> result = (List<DwType>) msg.obj;
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
        String url = ConstantUtil.BASE_URL + "/positionTypes?pageNum="+pageIndex+"&pageSize=20";
        VolleyRequest.RequestGet(getActivity(), url, "positionDetail", new VolleyListenerInterface
                (getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
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
                        List<DwType> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<DwType>>() {
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
        BaseApplication.getRequestQueue().cancelAll("positionDetail");
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i - 1 < list.size()) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DwDetailInfoActivity.class);
            intent.putExtra("positionDetail", list.get(i - 1));
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        pageIndex =1;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex++;
        loadData(AutoListView.LOAD);
    }
}

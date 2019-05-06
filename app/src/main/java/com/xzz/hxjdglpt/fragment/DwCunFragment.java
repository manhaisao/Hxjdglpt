package com.xzz.hxjdglpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.DwDetailInfoActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.activity.SxgzListActivity;
import com.xzz.hxjdglpt.adapter.VillageAdapter;
import com.xzz.hxjdglpt.customview.MyListView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
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
import java.util.Map;

/**
 * Created by xieyu on 2019/3/30.
 */
@ContentView(R.layout.fragment_dw_village)
public class DwCunFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<Village> villages = new ArrayList<>();

    @ViewInject(R.id.sxgz_list)
    private MyListView listview;
    private VillageAdapter villageAdapter;
    private User user;
    private BaseApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        application = (BaseApplication) getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        villageAdapter = new VillageAdapter(getActivity(), villages);
        listview.setAdapter(villageAdapter);
        listview.setOnItemClickListener(this);
        request();
        return view;
    }


    private void request() {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(getActivity(), url, "village_queryList", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
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
                                List<Village> v = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Village>>() {
                                        }.getType());
                                villages.addAll(v);
                                villageAdapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(getActivity());
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(getActivity(), R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            SuccinctProgress.dismiss();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DwDetailInfoActivity.class);
        intent.putExtra("name", villages.get(i).getName());
        intent.putExtra("id", villages.get(i).getId());
        startActivity(intent);
    }
}

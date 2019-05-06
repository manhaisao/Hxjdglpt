package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.DoubleLinClickHelp;
import com.xzz.hxjdglpt.adapter.ListAdapter;
import com.xzz.hxjdglpt.adapter.ListItemClickHelp;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Pfwsp;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.Tshd;
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
 * 法律精课堂
 */
@ContentView(R.layout.aty_list)
public class FljktActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener,
        AdapterView.OnItemClickListener, ListItemClickHelp, DoubleLinClickHelp {

    @ViewInject(R.id.hx_title_tv)
    public TextView hx_title_tv;

    @ViewInject(R.id.hx_title_right)
    public TextView hx_title_right;

    private User user;
    private BaseApplication application;
    @ViewInject(R.id.list_listview)
    private AutoListView listView;
    private List<Object> list = new ArrayList<Object>();
    private int pageIndex = 1;
    private ListAdapter adapter;
    private boolean isRefresh = false;

    private List<Role> roles;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Object> result = (List<Object>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    if (!listView.isStackFromBottom()) {
                        listView.setStackFromBottom(true);
                    }
                    listView.setStackFromBottom(false);
                    listView.setResultSize(result.size());
                    adapter.notifyDataSetChanged();
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    list.addAll(result);
                    listView.setResultSize(result.size());
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        if (isContain()) {
            hx_title_right.setVisibility(View.VISIBLE);
            hx_title_right.setText("添加");
        } else {
            hx_title_right.setVisibility(View.GONE);
        }

        initView();
        initData();

    }

    public void initView() {
        hx_title_tv.setText("法律精课堂");
        if (isContain()) {
            adapter = new ListAdapter(this, list, this, "isFrom");
        } else {
            adapter = new ListAdapter(this, list, this, "index");
        }
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4241".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }


    public void initData() {
        loadTsdjhdoData(AutoListView.REFRESH);
    }

    private void loadTsdjhdoData(final int what) {
        SuccinctProgress.showSuccinctProgress(FljktActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/lawTypes?pageNum=" + pageIndex + "&pageSize=20";
        VolleyRequest.RequestGet(this, url, "queryList", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    Log.e("insert", result.toString());
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("0".equals(resultCode)) {
                        JSONObject object = result.getJSONObject("result");
                        JSONArray jsonArray = object.getJSONArray("records");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Pfwsp> objs = gson.fromJson(jsonArray.toString(), new TypeToken<List<Pfwsp>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(FljktActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(FljktActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                intent.setClass(this, FljktTypefbActivity.class);
                startActivity(intent);
                isRefresh = true;
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            pageIndex = 1;
            initData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("pfwsp", (Pfwsp) list.get(position - 1));
            intent.putExtras(bundle);
            intent.setClass(this, FljktTwoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onOneClick(View widget, int position) {

    }

    @Override
    public void onTwoClick(View widget, int position) {

    }

    @Override
    public void onUptClick(View widget, int position) {
        isRefresh = true;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("pfwsp", (Pfwsp) list.get(position));
        intent.putExtras(bundle);
        intent.setClass(this, FljktTypefbActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(FljktActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        TextView tvContent = (TextView) view.findViewById(R.id.dialog_content);
        Button butOk = (Button) view.findViewById(R.id.dialog_ok);
        Button butCancle = (Button) view.findViewById(R.id.dialog_cancel);
        butOk.setText("确认");
        butCancle.setText("取消");
        butCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        tvContent.setText("确认删除?");
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                StringBuffer url = new StringBuffer();
                url.append(ConstantUtil.BASE_URL);
                delData(url.toString(), ((Pfwsp) list.get(position)).getId()+"");

            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void delData(String url, String id) {
        SuccinctProgress.showSuccinctProgress(FljktActivity.this, "数据删除中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String urls = ConstantUtil.BASE_URL + "/lawTypes/" + id;
        VolleyRequest.RequestDelete(this, urls, "delData", new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("0".equals(resultCode)) {
                        ToastUtil.show(FljktActivity.this, R.string.del_success);
                        onRefresh();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(FljktActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(FljktActivity.this, R.string.del_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadTsdjhdoData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex++;
        loadTsdjhdoData(AutoListView.LOAD);
    }
}

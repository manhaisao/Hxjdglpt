package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.JlyMoreListAdapter;
import com.xzz.hxjdglpt.adapter.ListAdapter;
import com.xzz.hxjdglpt.adapter.ListItemClickHelp;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnLoadListener;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnRefreshListener;
import com.xzz.hxjdglpt.model.Azbjry;
import com.xzz.hxjdglpt.model.CityInfo;
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Company;
import com.xzz.hxjdglpt.model.Dbry;
import com.xzz.hxjdglpt.model.Dszn;
import com.xzz.hxjdglpt.model.Huli;
import com.xzz.hxjdglpt.model.Jlfz;
import com.xzz.hxjdglpt.model.Jskn;
import com.xzz.hxjdglpt.model.Jzgy;
import com.xzz.hxjdglpt.model.Ldrk;
import com.xzz.hxjdglpt.model.Lsrt;
import com.xzz.hxjdglpt.model.Management;
import com.xzz.hxjdglpt.model.ManagementQt;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.Shjy;
import com.xzz.hxjdglpt.model.Sqfxry;
import com.xzz.hxjdglpt.model.Sqjdry;
import com.xzz.hxjdglpt.model.Tkgy;
import com.xzz.hxjdglpt.model.Tshd;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Xjry;
import com.xzz.hxjdglpt.model.Yfdx;
import com.xzz.hxjdglpt.model.Ylfn;
import com.xzz.hxjdglpt.model.Zdqsn;
import com.xzz.hxjdglpt.model.Zdry;
import com.xzz.hxjdglpt.model.ZfzzInfo;
import com.xzz.hxjdglpt.model.ZfzzInfoVillage;
import com.xzz.hxjdglpt.model.Zlj;
import com.xzz.hxjdglpt.model.Zszhjsbhz;
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
 * 护理人员\集中供养\社会寄养
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_list)
public class JlyMoreListActivity extends BaseActivity implements OnRefreshListener, OnLoadListener,
        AdapterView.OnItemClickListener, ListItemClickHelp {

    @ViewInject(R.id.list_listview)
    private AutoListView listView;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private JlyMoreListAdapter adapter;
    private List<Object> list = new ArrayList<Object>();
    private int pageIndex = 0;

    private String isFrom;

    private boolean isRefresh = false;

    private List<Role> roles;

    private int type=0;//1:护理人员\2:集中供养\3:社会寄养

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        tvRight.setText("新增");
        isFrom = getIntent().getStringExtra("isFrom");
        type = getIntent().getIntExtra("type",0);
        initView();
        initData();
    }

    public void initView() {
        if (isFrom != null && "index".equals(isFrom)) {
            tvRight.setVisibility(View.GONE);
        }
        switch (type) {
            case 1:
                tvTitle.setText("护理人员");
                break;
            case 2:
                tvTitle.setText("集中供养");
                break;
            case 3:
                tvTitle.setText("社会寄养");
                break;
        }
        adapter = new JlyMoreListAdapter(this, list, this, isFrom);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    //打码权限
    private boolean isContain() {
        for (Role r : roles) {
            if ("4257".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initData() {
        switch (type) {
            case 1:
                loadHlry(AutoListView.REFRESH);
                break;
            case 2:
                loadJzgy(AutoListView.REFRESH);
                break;
            case 3:
                loadShjy(AutoListView.REFRESH);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            pageIndex = 0;
            initData();
        }
    }

    private void loadHlry(final int what) {
        SuccinctProgress.showSuccinctProgress(JlyMoreListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/hly/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Huli> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Huli>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyMoreListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyMoreListActivity.this, R.string.load_fail);
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

    private void loadJzgy(final int what) {
        SuccinctProgress.showSuccinctProgress(JlyMoreListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jzgy/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Jzgy> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jzgy>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyMoreListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyMoreListActivity.this, R.string.load_fail);
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

    private void loadShjy(final int what) {
        SuccinctProgress.showSuccinctProgress(JlyMoreListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/shjy/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Shjy> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Shjy>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyMoreListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyMoreListActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                //添加
                Intent intent = new Intent();
                intent.putExtra("isAdd", true);
                isRefresh = true;
                switch (type) {
                    case 1:
                        intent.setClass(JlyMoreListActivity.this, HlryActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(JlyMoreListActivity.this, JzgyActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(JlyMoreListActivity.this, ShjyActivity.class);
                        startActivity(intent);
                        break;
                }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
        BaseApplication.getRequestQueue().cancelAll("delData");
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        switch (type) {
            case 1:
                loadHlry(AutoListView.REFRESH);
                break;
            case 2:
                loadJzgy(AutoListView.REFRESH);
                break;
            case 3:
                loadShjy(AutoListView.REFRESH);
                break;
        }
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex + ConstantUtil.PAGE_SIZE;
        switch (type) {
            case 1:
                loadHlry(AutoListView.LOAD);
                break;
            case 2:
                loadJzgy(AutoListView.LOAD);
                break;
            case 3:
                loadShjy(AutoListView.LOAD);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            switch (type) {
                case 1:
                    intent.setClass(JlyMoreListActivity.this, HlryInfo.class);
                    bundle.putParcelable("hlry", (Huli) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 2:
                    intent.setClass(JlyMoreListActivity.this, JzgyInfo.class);
                    bundle.putParcelable("jzgy", (Jzgy) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 3:
                    intent.setClass(JlyMoreListActivity.this,ShjyInfo.class);
                    bundle.putParcelable("shjy", (Shjy) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onUptClick(View widget, int position) {
        isRefresh = true;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (type) {
            case 1:
                Huli h = (Huli) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("hlry", h);
                intent.putExtras(bundle);
                intent.setClass(JlyMoreListActivity.this, HlryActivity.class);
                startActivity(intent);
                break;
            case 2:
                Jzgy j = (Jzgy) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("jzgy", j);
                intent.putExtras(bundle);
                intent.setClass(JlyMoreListActivity.this, JzgyActivity.class);
                startActivity(intent);
                break;
            case 3:
                Shjy s = (Shjy) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("shjy", s);
                intent.putExtras(bundle);
                intent.setClass(JlyMoreListActivity.this, ShjyActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(JlyMoreListActivity.this);
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
                switch (type) {
                    case 1:
                        Huli h = (Huli) list.get(position);
                        url.append("/hly/delHly");
                        delData(url.toString(), String.valueOf(h.getId()));
                        break;
                    case 2:
                        Jzgy j = (Jzgy) list.get(position);
                        url.append("/jzgy/delJzgy");
                        delData(url.toString(), String.valueOf(j.getId()));
                        break;
                    case 3:
                        Shjy s = (Shjy) list.get(position);
                        url.append("/shjy/delShjy");
                        delData(url.toString(), String.valueOf(s.getId()));
                        break;
                }
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
        SuccinctProgress.showSuccinctProgress(JlyMoreListActivity.this, "数据删除中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", id);
        VolleyRequest.RequestPost(this, url, "delData", params, new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(JlyMoreListActivity.this, R.string.del_success);
                        onRefresh();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlyMoreListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlyMoreListActivity.this, R.string.del_fail);
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
}

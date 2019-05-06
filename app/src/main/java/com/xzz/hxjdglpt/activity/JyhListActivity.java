package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.xzz.hxjdglpt.adapter.DoubleLinClickHelp;
import com.xzz.hxjdglpt.adapter.ListAdapter;
import com.xzz.hxjdglpt.adapter.ListItemClickHelp;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.message.PlotEvent;
import com.xzz.hxjdglpt.model.Jyh;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.greenrobot.eventbus.EventBus;
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
 * Created by Administrator on 2019\4\14 0014.
 */
@ContentView(R.layout.aty_list)
public class JyhListActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener,
        AdapterView.OnItemClickListener, ListItemClickHelp, DoubleLinClickHelp {


    @ViewInject(R.id.list_listview)
    private AutoListView listView;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private ListAdapter adapter;
    private List<Object> list = new ArrayList<Object>();
    private int pageIndex = 1;


    private boolean isRefresh = false;

    private List<Role> roles;


    private String isFrom;

    private String pid;

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
        isFrom = getIntent().getStringExtra("isFrom");
        pid = getIntent().getStringExtra("pid");
        tvRight.setText("新增");
        initView();
        initData();
    }

    public void initView() {
        if (isFrom != null && isFrom.equals("index")) {
            tvRight.setVisibility(View.GONE);
        } else {
            tvRight.setVisibility(View.VISIBLE);
        }
        tvTitle.setText("经营户信息");
        adapter = new ListAdapter(this, list, this, isFrom, this);
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
        loadGtData(AutoListView.REFRESH);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            pageIndex = 1;
            initData();
        }
    }


    private void loadGtData(final int what) {
        SuccinctProgress.showSuccinctProgress(JyhListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/" + pid + "/jyhs?pageNum=" + pageIndex + "&pageSize=20";
        VolleyRequest.RequestGet(this, url, "queryList", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("0".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("result");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Jyh> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jyh>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyhListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyhListActivity.this, R.string.load_fail);
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
                intent.putExtra("pid", pid + "");
                intent.setClass(JyhListActivity.this, JyhAddActivity.class);
                startActivity(intent);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
        BaseApplication.getRequestQueue().cancelAll("delData");
        BaseApplication.getRequestQueue().cancelAll("isJoin");
        BaseApplication.getRequestQueue().cancelAll("updateStatus");
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadGtData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex++;
        loadGtData(AutoListView.LOAD);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Jyh jyh = (Jyh) list.get(position - 1);
            Intent intent = new Intent();
            intent.putExtra("jyh", jyh);
            intent.setClass(JyhListActivity.this, JyhInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onUptClick(View widget, int position) {
        isRefresh = true;
        Intent intent = new Intent();
        intent.putExtra("isAdd", false);
        isRefresh = true;
        intent.putExtra("jyh", (Jyh) list.get(position));
        intent.putExtra("pid", pid + "");
        intent.setClass(JyhListActivity.this, JyhAddActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(JyhListActivity.this);
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
                Jyh jyh = (Jyh) list.get(position);
                delData(jyh.getId() + "");
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void delData(String id) {
        SuccinctProgress.showSuccinctProgress(JyhListActivity.this, "数据删除中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", id);
        if (getIntent().getStringExtra("isYjfw") != null) {
            params.put("isYjfw", getIntent().getStringExtra("isYjfw"));
        }
        String url = ConstantUtil.BASE_URL + "/plot/jyhs/" + id;
        VolleyRequest.RequestDelete(this, url, "delData", new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("0".equals(resultCode)) {
                        EventBus.getDefault().post(new PlotEvent(false));
                        ToastUtil.show(JyhListActivity.this, R.string.del_success);
                        onRefresh();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyhListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyhListActivity.this, R.string.del_fail);
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
    public void onOneClick(View widget, int position) {

    }

    @Override
    public void onTwoClick(View widget, int position) {

    }

    private void postDos(final Plot plot) {
        SuccinctProgress.showSuccinctProgress(JyhListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pid", plot.getId());
        String url = ConstantUtil.BASE_URL + "/plotlt/isJoin";
        VolleyRequest.RequestPost(this, url, "isJoin", params, new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:申请成功 ；2：token不一致；3：添加失败；4：未加入小区论坛；5：正在申请中；
                    if ("1".equals(resultCode)) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("plot", plot);
                        intent.putExtras(bundle);
                        intent.setClass(JyhListActivity.this, PlotLtActivity.class);
                        startActivity(intent);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyhListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyhListActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        //申请加入小区论坛
                        new AlertDialog.Builder(JyhListActivity.this).setTitle("确认加入" + plot.getName() + "的论坛吗？").setPositiveButton("取消", new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton("确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                joinLt(plot);
                            }
                        }).show();
                    } else if ("5".equals(resultCode)) {
                        ToastUtil.show(JyhListActivity.this, "您的请求还在审核中");
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

    private void joinLt(final Plot plot) {
        SuccinctProgress.showSuccinctProgress(JyhListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("plotid", plot.getId());
        String url = ConstantUtil.BASE_URL + "/plotlt/updateStatus";
        VolleyRequest.RequestPost(this, url, "updateStatus", params, new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:xxx ；2：token不一致；3：添加失败；4：同意加入论坛；5：申请加入小区论坛；
                    if ("1".equals(resultCode)) {
                        LogUtil.i("此处不应该有这个返回值");
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyhListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyhListActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        LogUtil.i("此处不应该有这个返回值");
                    } else if ("5".equals(resultCode)) {
                        ToastUtil.show(JyhListActivity.this, "申请发送成功，请等待审核...");
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
package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xzz.hxjdglpt.adapter.DoubleLinClickHelp;
import com.xzz.hxjdglpt.adapter.ListAdapter;
import com.xzz.hxjdglpt.adapter.ListItemClickHelp;
import com.xzz.hxjdglpt.customview.MyListView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView2;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.Sxgz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.StringUtil;
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
import java.util.Map;

/**
 * 展示信息LIST
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_list_xqgsq)
public class XqgsqActivity extends BaseActivity implements AdapterView.OnItemClickListener, ListItemClickHelp, DoubleLinClickHelp {

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_right)
    private TextView tvRight;
    @ViewInject(R.id.list_listview)
    private ListView listView;
    @ViewInject(R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    private User user;
    private ListAdapter adapter;
    private List<Object> list = new ArrayList<Object>();
    private int pageIndex = 0;

    private String villageId = "";

    private String isFrom;
    private List<Role> roles;
    @ViewInject(R.id.qfhx_content)
    private TextView qfhx_content;
    @ViewInject(R.id.qfhx_rl)
    private RelativeLayout qfhx_rl;
    @ViewInject(R.id.qfhx_btn)
    private Button allBtn;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Object> result = (List<Object>) msg.obj;
            switch (msg.what) {
                case AutoListView2.REFRESH:
//                    listView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    if (!listView.isStackFromBottom()) {
                        listView.setStackFromBottom(true);
                    }
                    listView.setStackFromBottom(false);
//                    listView.setResultSize(result.size());
                    adapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    break;
                case AutoListView2.LOAD:
//                    listView.onLoadComplete();
                    list.addAll(result);
//                    listView.setResultSize(result.size());
                    adapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
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
        tvRight.setVisibility(View.GONE);
        qfhx_rl.setVisibility(View.GONE);
        isFrom = getIntent().getStringExtra("isFrom");
        initView();
        initData();
    }

    public void initView() {
        villageId = getIntent().getStringExtra("villageId");
        if (isFrom != null && "index".equals(isFrom)) {
            tvRight.setVisibility(View.GONE);
        }
        getSxgzById(12);
        tvTitle.setText(R.string.xqgsq);
        adapter = new ListAdapter(this, list, this, isFrom, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 0;
                loadXqData(AutoListView2.REFRESH);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                pageIndex = pageIndex + 20;
                loadXqData(AutoListView2.LOAD);
            }
        });
        refreshLayout.autoRefresh();
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

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void getSxgzById(int id) {
        String url = ConstantUtil.BASE_URL + "/sxgz/queryById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(id));
        VolleyRequest.RequestPost(this, url, "queryById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        LogUtil.i("result=" + result.getJSONObject("data").toString());
                        Gson gson = new Gson();
                        final Sxgz sxgz = (Sxgz) gson.fromJson(result.getJSONObject("data").toString(),
                                Sxgz.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!StringUtil.isEmpty(sxgz.getContent())) {
                                    qfhx_content.setText(sxgz.getContent());
                                    qfhx_rl.setVisibility(View.VISIBLE);
                                } else {
                                    qfhx_rl.setVisibility(View.GONE);
                                }
                            }
                        });

                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XqgsqActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XqgsqActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
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


    /**
     * 小区信息获取
     */
    private void loadXqData(final int what) {
        SuccinctProgress.showSuccinctProgress(XqgsqActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/queryPlotList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (villageId != null)
            params.put("vId", villageId);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        if (villageId != null && !TextUtils.isEmpty(villageId)) params.put("vId", villageId);
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
                        List<Plot> objs = gson.fromJson(jsonArray.toString(), new TypeToken<List<Plot>>() {
                        }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XqgsqActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XqgsqActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right, R.id.qfhx_btn}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                //添加
                Intent intent = new Intent();
//                intent.putExtra("gridId", gridId);
                intent.putExtra("isAdd", true);

                intent.setClass(XqgsqActivity.this, PlotActivity.class);
                startActivity(intent);
                break;
            case R.id.qfhx_btn:
                if ("展开".equals(allBtn.getText())) {
                    allBtn.setText("收起");
                    Drawable drawable = getResources().getDrawable(R.mipmap.min_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    qfhx_content.setMaxLines(10000);
                } else if ("收起".equals(allBtn.getText())) {
                    allBtn.setText("展开");
                    Drawable drawable = getResources().getDrawable(R.mipmap.all_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    qfhx_content.setMaxLines(5);
                }

                break;
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

//    @Override
//    public void onRefresh() {
//        pageIndex = 0;
//        loadXqData(AutoListView2.REFRESH);
//
//    }
//
//    @Override
//    public void onLoad() {
//        pageIndex = pageIndex + ConstantUtil.PAGE_SIZE;
//        loadXqData(AutoListView2.LOAD);
//
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        Plot pl = (Plot) list.get(position);
        intent.setClass(XqgsqActivity.this, XqgsqInfoActivity.class);
        bundle.putParcelable("plot", pl);
        intent.putExtra("isFrom", "index");
        intent.putExtra("gridId", pl.getGridId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onUptClick(View widget, int position) {
        Intent intent = new Intent();
//        intent.putExtra("gridId", gridId);
        Bundle bundle = new Bundle();

        Plot plot = (Plot) list.get(position);
        intent.putExtra("isAdd", false);
        bundle.putParcelable("plot", plot);
        intent.putExtra("isFrom", "index");
        intent.putExtras(bundle);
        intent.setClass(XqgsqActivity.this, PlotActivity.class);
        startActivity(intent);


    }

    @Override
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(XqgsqActivity.this);
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

                Plot plot = (Plot) list.get(position);
                url.append("/plot/delPlot");
                delData(url.toString(), plot.getId());


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
        SuccinctProgress.showSuccinctProgress(XqgsqActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", id);
        if (getIntent().getStringExtra("isYjfw") != null) {
            params.put("isYjfw", getIntent().getStringExtra("isYjfw"));
        }
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
                        ToastUtil.show(XqgsqActivity.this, R.string.del_success);

                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XqgsqActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XqgsqActivity.this, R.string.del_fail);
                    }
//                    pulltorefreshscrollview.onRefreshComplete();


                } catch (JSONException e) {
                    e.printStackTrace();
//                    pulltorefreshscrollview.onRefreshComplete();

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                });
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                });
//                pulltorefreshscrollview.onRefreshComplete();
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onOneClick(View widget, int position) {
        Plot plot = (Plot) list.get(position);
        postDos(plot);
    }

    @Override
    public void onTwoClick(View widget, int position) {

    }

    private void postDos(final Plot plot) {
        SuccinctProgress.showSuccinctProgress(XqgsqActivity.this, "数据请求中···", SuccinctProgress
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
                        intent.setClass(XqgsqActivity.this, PlotLtActivity.class);
                        startActivity(intent);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XqgsqActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XqgsqActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        //申请加入小区论坛
                        new AlertDialog.Builder(XqgsqActivity.this).setTitle("确认加入" + plot.getName() + "的论坛吗？").setPositiveButton("取消", new DialogInterface
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
                        ToastUtil.show(XqgsqActivity.this, "您的请求还在审核中");
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
        SuccinctProgress.showSuccinctProgress(XqgsqActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("plotid", plot.getId());
        params.put("status", "0");
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
                        DialogUtil.showTipsDialog(XqgsqActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XqgsqActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        LogUtil.i("此处不应该有这个返回值");
                    } else if ("5".equals(resultCode)) {
                        ToastUtil.show(XqgsqActivity.this, "申请发送成功，请等待审核...");
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

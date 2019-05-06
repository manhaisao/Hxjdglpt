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
import com.xzz.hxjdglpt.adapter.BaryAdapter;
import com.xzz.hxjdglpt.adapter.ListItemClickHelp;
import com.xzz.hxjdglpt.adapter.PlotSQUserAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnLoadListener;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnRefreshListener;
import com.xzz.hxjdglpt.model.Abry;
import com.xzz.hxjdglpt.model.PlotUserList;
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
 * 展示小区论坛人员
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_plot_sq_user)
public class PlotSQUserActivity extends BaseActivity implements OnRefreshListener, OnLoadListener, ListItemClickHelp {

    @ViewInject(R.id.plot_sq_user_listview)
    private AutoListView listView;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private PlotSQUserAdapter adapter;
    private List<PlotUserList> list = new ArrayList<PlotUserList>();
    private int pageIndex = 0;

    private String plotid;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<PlotUserList> result = (List<PlotUserList>) msg.obj;
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
        plotid = getIntent().getStringExtra("plotid");
        tvRight.setText("邀请");
        initView();
    }

    public void initView() {
        tvTitle.setText("申请列表");
        adapter = new PlotSQUserAdapter(this, list, this);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
    }


    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    /**
     * 信息获取
     */
    private void loadData(final int what) {
        SuccinctProgress.showSuccinctProgress(PlotSQUserActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plotUser/queryUserList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("plotid", plotid);
        VolleyRequest.RequestPost(this, url, "queryUserList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<PlotUserList> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<PlotUserList>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PlotSQUserActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotSQUserActivity.this, R.string.load_fail);
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
                Intent intent = new Intent();
                intent.putExtra("plotid", plotid);
                intent.setClass(PlotSQUserActivity.this, LtyqUserListActivity.class);
                startActivity(intent);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryUserList");
        BaseApplication.getRequestQueue().cancelAll("delData");
        BaseApplication.getRequestQueue().cancelAll("updateStatus");
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
    public void onUptClick(View widget, int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(PlotSQUserActivity.this);
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
        tvContent.setText("确认同意" + list.get(position).getFbrXm() + "加入" + list.get(position).getPlotName() + "论坛?");
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinLt();
                dialog.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    private void joinLt() {
        SuccinctProgress.showSuccinctProgress(PlotSQUserActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("plotid", plotid);
        params.put("status", "1");
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
                        DialogUtil.showTipsDialog(PlotSQUserActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotSQUserActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        //同意成功
                        onRefresh();
                    } else if ("5".equals(resultCode)) {
                        LogUtil.i("此处不应该有这个返回值");
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
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(PlotSQUserActivity.this);
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
        tvContent.setText("确认拒绝" + list.get(position).getFbrXm() + "加入" + list.get(position).getPlotName() + "论坛?");
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delData(position);
                dialog.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void delData(int position) {
        SuccinctProgress.showSuccinctProgress(PlotSQUserActivity.this, "数据删除中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(list.get(position).getId()));
        VolleyRequest.RequestPost(this, ConstantUtil.BASE_URL + "/plotlt/delPlotUserList", "delData", params, new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(PlotSQUserActivity.this, R.string.del_success);
                        onRefresh();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PlotSQUserActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotSQUserActivity.this, R.string.del_fail);
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

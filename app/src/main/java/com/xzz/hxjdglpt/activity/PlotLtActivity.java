package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.PlotUserListAdapter;
import com.xzz.hxjdglpt.adapter.PlotltAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.PlotLt;
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
 * Created by dbz on 2017/8/17.
 */
@ContentView(R.layout.aty_plot_lt)
public class PlotLtActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AbsListView.OnScrollListener {

    @ViewInject(R.id.hx_title)
    private TextView mTitle;
    @ViewInject(R.id.hx_btn_right)
    private ImageView mRight;

    @ViewInject(R.id.drawerlayout)
    private DrawerLayout drawerLayout;
    @ViewInject(R.id.right)
    private RelativeLayout rightLayout;

    private List<PlotUserList> list = new ArrayList<>();
    private PlotUserListAdapter adapter;


    @ViewInject(R.id.plot_user_list)
    private ListView mUserListView;

    @ViewInject(R.id.plot_lt_listview)
    private AutoListView listView;

    @ViewInject(R.id.plot_lt_content)
    private EditText edtContent;
    @ViewInject(R.id.plot_lt_send)
    private Button btnSend;

    private PlotltAdapter plotltAdapter;
    private List<PlotLt> resList = new ArrayList<PlotLt>();

    private ImageLoader mImageLoader;

    private User user;

    private int pageIndex = 0;

    private Plot plot;

    //-------------------------
    private View moreView;
//    private View headView;

    private LayoutInflater layoutInflater;
    private TextView tv_load_more;
    private ProgressBar pb_load_progress;

    private int lastItem;

    private int pageIndex_user = 0;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<PlotLt> result = (List<PlotLt>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    resList.clear();
                    resList.addAll(result);
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    resList.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            plotltAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        plot = getIntent().getParcelableExtra("plot");
        mImageLoader = application.getImageLoader();

        mTitle.setText(plot.getName() + "论坛");
        mRight.setImageDrawable(getResources().getDrawable(R.mipmap.users_lt_icon));

//        headView = layoutInflater.inflate(R.layout.mqztc_head, null);
        moreView = layoutInflater.inflate(R.layout.footer_more, null);
        tv_load_more = (TextView) moreView.findViewById(R.id.tv_load_more);
        pb_load_progress = (ProgressBar) moreView.findViewById(R.id.pb_load_progress);
        mUserListView.addFooterView(moreView);
//        mUserListView.addHeaderView(headView);
        mUserListView.setOnScrollListener(this);
        tv_load_more.setText("");
        pb_load_progress.setVisibility(View.GONE);

        adapter = new PlotUserListAdapter(this, list, mImageLoader);
        mUserListView.setAdapter(adapter);
        mRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        rightLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });


        plotltAdapter = new PlotltAdapter(this, resList, mImageLoader);
        listView.setAdapter(plotltAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);


        initData();
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
        loadUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/plotlt/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("pid", plot.getId());
        VolleyRequest.RequestPost(this, url, "queryList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
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
                                List<PlotLt> data = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<PlotLt>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = data;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(PlotLtActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(PlotLtActivity.this, R.string.load_fail);
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

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
        BaseApplication.getRequestQueue().cancelAll("queryUserList");
        BaseApplication.getRequestQueue().cancelAll("commitData");
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

    private void loadUserData() {
        String url = ConstantUtil.BASE_URL + "/plotUser/queryUserList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex_user));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("plotid", plot.getId());
        VolleyRequest.RequestPost(this, url, "queryUserList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
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
                                List<PlotUserList> data = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<PlotUserList>>() {
                                        }.getType());
                                list.addAll(data);
                                if (list.size() >= ConstantUtil.PAGE_SIZE) {
                                    tv_load_more.setText(R.string.load_more_data);
                                    pb_load_progress.setVisibility(View.GONE);
                                } else {
                                    tv_load_more.setText(R.string.no_more_data);
                                    pb_load_progress.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(PlotLtActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(PlotLtActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back, R.id.plot_lt_send}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.plot_lt_send:
                if (TextUtils.isEmpty(edtContent.getText().toString())) {
                    ToastUtil.show(PlotLtActivity.this, "请输入内容");
                    return;
                } else {
                    commitData();
                }
                break;
        }
    }

    private void commitData() {
        String url = ConstantUtil.BASE_URL + "/plotlt/commitData";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("content", edtContent.getText().toString());
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("pid", plot.getId());
        VolleyRequest.RequestPost(this, url, "commitData", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                PlotLt p = (PlotLt) gson.fromJson(result.getJSONObject("data").toString(),
                                        PlotLt.class);
                                resList.add(0, p);
                                plotltAdapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(PlotLtActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(PlotLtActivity.this, R.string.load_fail);
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
    public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        LogUtil.i("onScrollStateChanged");
        if ((lastItem - 1) == adapter.getCount() && scrollState == AbsListView
                .OnScrollListener.SCROLL_STATE_IDLE && pageIndex_user - list.size() < ConstantUtil
                .PAGE_SIZE) {
            pageIndex_user += ConstantUtil.PAGE_SIZE;
            loadUserData();
        }
    }
}

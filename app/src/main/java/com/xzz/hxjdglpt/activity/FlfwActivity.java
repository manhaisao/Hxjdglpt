package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xzz.hxjdglpt.adapter.FwmdmAdapter;
import com.xzz.hxjdglpt.adapter.YzyhAdapter;
import com.xzz.hxjdglpt.customview.MyListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Fwmdm;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Yzyh;
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
 * 法律服务
 */
@ContentView(R.layout.aty_flfw)
public class FlfwActivity extends BaseActivity {

    @ViewInject(R.id.smartlayout)
    SmartRefreshLayout smartlayout;
    @ViewInject(R.id.scrollview)
    ScrollView scrollview;
    //标题
    @ViewInject(R.id.hx_title_tv)
    TextView hx_title;

    //法律精课堂
    @ViewInject(R.id.flfw_jkt)
    Button flfw_jkt;

    //普法微视频
    @ViewInject(R.id.flfw_pf)
    Button flfw_pf;

    @ViewInject(R.id.flfw_listview)
    MyListView autoListView;
    //右侧菜单
    @ViewInject(R.id.hx_title_right)
    TextView hx_title_right;


    @ViewInject(R.id.flfw_search)
    Button flfw_search;

    @ViewInject(R.id.flfw_search_edt)
    EditText flfw_search_edt;

    private User user;
    private BaseApplication application;
    private List<Role> roles;

    private List<Fwmdm> list = new ArrayList<Fwmdm>();
    private int pageIndex = 1;

    private FwmdmAdapter fwmdmAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        initView();
        initData();
    }

    public void initView() {
        smartlayout.setRefreshHeader(new ClassicsHeader(this));
        smartlayout.setRefreshFooter(new ClassicsFooter(this));
//        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                smartlayout.setEnabled(scrollview.getScrollY() == 0);
//            }
//        });

        hx_title.setText("法律服务");
        hx_title_right.setVisibility(View.VISIBLE);
        hx_title_right.setText("我要咨询");
        fwmdmAdapter = new FwmdmAdapter(this, list);
        autoListView.setAdapter(fwmdmAdapter);
        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(FlfwActivity.this, FwmdmDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("fwmdm", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        smartlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                loadData(AutoListView.REFRESH, refreshlayout);
            }
        });
        smartlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageIndex++;
                loadData(AutoListView.LOAD, refreshlayout);
            }
        });
        loadData(AutoListView.REFRESH, null);
    }


    @Event(value = {R.id.flfw_jkt, R.id.flfw_pf, R.id.flfw_gzdt_btn, R.id.iv_back_tv, R.id.hx_title_right, R.id.flfw_search}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.flfw_jkt:
                intent.setClass(FlfwActivity.this, FljktActivity.class);
                startActivity(intent);
                break;
            case R.id.flfw_pf:
                //普法微视频
                intent.setClass(FlfwActivity.this, PfwspAcitivity.class);
                startActivity(intent);
                break;
            case R.id.flfw_gzdt_btn:
                intent.setClass(FlfwActivity.this, FlfwGzdtActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                intent.setClass(FlfwActivity.this, FlfwConsultActivity.class);
                startActivity(intent);
                break;
            case R.id.flfw_search:
                smartlayout.autoRefresh();
                break;
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Fwmdm> result = (List<Fwmdm>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    list.clear();
                    if (result != null)
                        list.addAll(result);
                    break;
                case AutoListView.LOAD:
                    if (result != null)
                        list.addAll(result);
                    break;
            }
            fwmdmAdapter.notifyDataSetChanged();
        }
    };

    private void loadData(final int what, final RefreshLayout refreshlayout) {
        String url = ConstantUtil.BASE_URL + "/lawConsultations?pageNum=" + pageIndex + "&pageSize=" + ConstantUtil.PAGE_SIZE + "&title=" + flfw_search_edt.getText().toString();
        VolleyRequest.RequestGet(this, url, "queryQfhxList", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    Log.e("insert", result.toString());
                    if ("0".equals(resultCode)) {
                        JSONObject object = result.getJSONObject("result");
                        JSONArray jsonArray = object.getJSONArray("records");
                        Gson gson = new Gson();
                        List<Fwmdm> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Fwmdm>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        handler.sendMessage(msg);
                    }
                    if (refreshlayout != null) {
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadMore();

                    }

                } catch (JSONException e) {
                    Message msg = handler.obtainMessage();
                    msg.what = what;
                    handler.sendMessage(msg);
                    if (refreshlayout != null) {
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadMore();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                if (refreshlayout != null) {
                    refreshlayout.finishRefresh();
                    refreshlayout.finishLoadMore();
                }

                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

}

package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xzz.hxjdglpt.adapter.QfhxAdapter;
import com.xzz.hxjdglpt.adapter.YzyhAdapter;
import com.xzz.hxjdglpt.customview.MyListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView2;
import com.xzz.hxjdglpt.model.Qfhx;
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
import java.util.HashMap;
import java.util.List;

/**
 * 文明创建
 * Created by Administrator on 2019/3/19 0019.
 */

@ContentView(R.layout.aty_wmcj)
public class WmcjActivity extends BaseActivity {


    @ViewInject(R.id.ll_top_content)
    RelativeLayout ll_top_content;
    @ViewInject(R.id.smartlayout)
    SmartRefreshLayout smartlayout;
    @ViewInject(R.id.scrollview)
    ScrollView scrollview;
    //简介
    @ViewInject(R.id.wmcj_content)
    TextView wmcj_content;

    //展开
    @ViewInject(R.id.wmcj_btn)
    Button wmcj_btn;

    //右侧菜单
    @ViewInject(R.id.hx_btn_right)
    ImageView hx_btn_right;

    //标题
    @ViewInject(R.id.hx_title)
    TextView hx_title;

    //测评标准
    @ViewInject(R.id.wmcj_cpbb)
    Button wmcj_cpbb;

    //点位详情
    @ViewInject(R.id.wmcj_dwxq)
    Button wmcj_dwxq;

    //应知应会
    @ViewInject(R.id.wmcj_yzyh)
    Button wmcj_yzyh;

    //精品展示
    @ViewInject(R.id.wmcj_jpzs)
    Button wmcj_jpzs;

    @ViewInject(R.id.wmcj_listview)
    MyListView autoListView;
    @ViewInject(R.id.wmcj_btn)
    private Button allBtn;
    //工作动态
    @ViewInject(R.id.wmcj_gzdt_btn)
    Button wmcj_gzdt_btn;
    private User user;
    private BaseApplication application;
    private List<Role> roles;
    private YzyhAdapter rdzjDtAdapter;
    private List<Yzyh> list = new ArrayList<Yzyh>();
    private int pageIndex = 1;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        roles = application.getRolesList();
        initView();
        initData();
    }


    private void setlayout(int width, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = width;
        view.setLayoutParams(params);
    }

    public void initView() {
        ll_top_content.setVisibility(View.GONE);
        smartlayout.setRefreshHeader(new ClassicsHeader(this));
        smartlayout.setRefreshFooter(new ClassicsFooter(this));
        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                smartlayout.setEnabled(scrollview.getScrollY() == 0);
            }
        });

        hx_title.setText("文明创建");
        hx_btn_right.setVisibility(View.GONE);
        rdzjDtAdapter = new YzyhAdapter(this, list);
        autoListView.setAdapter(rdzjDtAdapter);
        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(WmcjActivity.this, WmcjDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Yzyh", list.get(position ));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        smartlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                loadData(AutoListView.REFRESH, refreshlayout);
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageIndex++;
                loadData(AutoListView.LOAD, refreshlayout);
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        loadTop(0);
        loadData(AutoListView.REFRESH, null);
    }

    public void initData() {

    }


    @Event(value = {R.id.wmcj_btn, R.id.wmcj_cpbb, R.id.wmcj_dwxq, R.id.wmcj_yzyh, R.id.wmcj_jpzs, R.id.wmcj_gzdt_btn, R.id.iv_back}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.wmcj_btn:

                if ("展开".equals(allBtn.getText())) {
                    allBtn.setText("收起");
                    Drawable drawable = getResources().getDrawable(R.mipmap.min_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    wmcj_content.setMaxLines(10000);
                } else if ("收起".equals(allBtn.getText())) {
                    allBtn.setText("展开");
                    Drawable drawable = getResources().getDrawable(R.mipmap.all_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    wmcj_content.setMaxLines(5);
                }
                break;
            case R.id.wmcj_cpbb:
                intent.setClass(this, CpbzActivity.class);
                startActivity(intent);
                break;
            case R.id.wmcj_dwxq:
                intent.setClass(WmcjActivity.this, DwDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.wmcj_yzyh:
                intent.setClass(WmcjActivity.this, YzyhActivity.class);
                startActivity(intent);
                break;
            case R.id.wmcj_jpzs:
                intent.setClass(WmcjActivity.this, JpzsActivity.class);
                startActivity(intent);
                break;
            case R.id.wmcj_gzdt_btn:
                intent.setClass(WmcjActivity.this, WmcjFbActivity.class);
                intent.putExtra("type", 8);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Yzyh> result = (List<Yzyh>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
//                    autoListView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    break;
                case AutoListView.LOAD:
//                    autoListView.onLoadComplete();
                    list.addAll(result);
                    break;
            }
//            autoListView.setResultSize(result.size());
            rdzjDtAdapter.notifyDataSetChanged();
        }
    };

    private void loadData(final int what, final RefreshLayout refreshlayout) {
        String url = ConstantUtil.BASE_URL + "/wmdt?pageNum=" + pageIndex + "&pageSize=" + ConstantUtil.PAGE_SIZE;
        VolleyRequest.RequestGet(this, url, "queryQfhxList", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    Log.e("insert",result.toString());
                    if ("0".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("result");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Yzyh> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Yzyh>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(WmcjActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(WmcjActivity.this, R.string.load_fail);
                    }
                    if (refreshlayout != null) {
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadMore();
                    }

                } catch (JSONException e) {
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


    private void loadTop(final int what) {
        String url = ConstantUtil.BASE_URL + "/modelInfos/文明创建";
        VolleyRequest.RequestGet(this, url, "modelInfos", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                Log.e("insert", result.toString());
//                LogUtil.i(result.toString());
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("0".equals(resultCode)) {
                        JSONObject object = result.getJSONObject("result");
                        LogUtil.i(object.toString());
//
                        final String description = object.getString("description");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wmcj_content.setText(description);
                                if (wmcj_content.getLineCount() <= 5) {
                                    wmcj_btn.setVisibility(View.GONE);
                                }
                                if (!TextUtils.isEmpty(description))
                                    ll_top_content.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(WmcjActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(WmcjActivity.this, R.string.load_fail);
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

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (position - 1 < list.size()) {
//            Intent intent = new Intent();
//            intent.setClass(WmcjActivity.this, WmcjDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Yzyh", list.get(position - 1));
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        pageIndex = 0;
//        loadData(AutoListView.REFRESH);
//    }
//
//    @Override
//    public void onLoad() {
//        pageIndex = pageIndex + ConstantUtil.PAGE_SIZE;
//        loadData(AutoListView.LOAD);
//    }
}

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
import com.xzz.hxjdglpt.adapter.DkdAdapter;
import com.xzz.hxjdglpt.adapter.ListItemClickHelp;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnLoadListener;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnRefreshListener;
import com.xzz.hxjdglpt.model.Daikai;
import com.xzz.hxjdglpt.model.Jdfzr;
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
 * 河下街道税票代开点
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_dkd)
public class DkdListActivity extends BaseActivity implements OnRefreshListener, OnLoadListener, AdapterView.OnItemClickListener, ListItemClickHelp {

    @ViewInject(R.id.news_listview)
    private AutoListView listView;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.fzr_1)
    private TextView mFzr1;
    @ViewInject(R.id.fzr_2)
    private TextView mFzr2;
    private String isFrom;


    private User user;
    private DkdAdapter dkdAdapter;
    private List<Daikai> list = new ArrayList<Daikai>();
    private int pageIndex = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Daikai> result = (List<Daikai>) msg.obj;
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
            dkdAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        tvRight.setText("新增");

        isFrom = getIntent().getStringExtra("isFrom");
        if (isFrom != null && "index".equals(isFrom)) {
            tvRight.setVisibility(View.GONE);
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("河下街道税票代开点");
        dkdAdapter = new DkdAdapter(this, list, this, isFrom);
        listView.setAdapter(dkdAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    public void initData() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 新闻获取
     */
    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/daikai/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "new_queryList", params,
                new VolleyListenerInterface(this,
                        VolleyListenerInterface.mListener,
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
                                List<Daikai> newses = gson.fromJson(jsonArray.toString(), new TypeToken<List<Daikai>>() {
                                }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);

                                JSONArray jsonArray1 = result.getJSONArray("jdfzr");
                                List<Jdfzr> jdfzrs = gson.fromJson(jsonArray1.toString(), new
                                        TypeToken<List<Jdfzr>>() {
                                        }.getType());
                                if (jdfzrs.size() >= 2) {
                                    mFzr1.setText(jdfzrs.get(0).getName() + "\t" + jdfzrs.get(0).getPhone());
                                    mFzr2.setText(jdfzrs.get(1).getName() + "\t" + jdfzrs.get(1).getPhone());
                                } else if (jdfzrs.size() == 1) {
                                    mFzr1.setText(jdfzrs.get(0).getName() + "\t" + jdfzrs.get(0).getPhone());
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(DkdListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(DkdListActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.putExtra("isAdd", true);
                intent.setClass(DkdListActivity.this, SpdkdActivity.class);
                startActivity(intent);

                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("new_queryList");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Intent intent = new Intent();
            intent.setClass(DkdListActivity.this, SpdkdInfo.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("daikai", list.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onUptClick(View widget, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Daikai pm = (Daikai) list.get(position);
        intent.putExtra("isAdd", false);
        bundle.putParcelable("daikai", pm);
        intent.putExtras(bundle);
        intent.setClass(DkdListActivity.this, SpdkdActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(DkdListActivity.this);
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
                Daikai daikai = (Daikai) list.get(position);
                url.append("/daikai/delData");
                delData(url.toString(), String.valueOf(daikai.getId()));

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
        SuccinctProgress.showSuccinctProgress(DkdListActivity.this, "数据删除中···", SuccinctProgress
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
                        ToastUtil.show(DkdListActivity.this, R.string.del_success);
                        onRefresh();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DkdListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DkdListActivity.this, R.string.del_fail);
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

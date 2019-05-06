package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.NoteAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.model.Note;
import com.xzz.hxjdglpt.model.Role;
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
 * 信访诉求人员工作日志
 * Created by dbz on 2017/5/23.
 */
@ContentView(R.layout.aty_gzdt)
public class ZdryGzrzActivity extends BaseActivity implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.gzdt_listview)
    private AutoListView listView;
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private NoteAdapter adapter;
    private List<Note> list = new ArrayList<Note>();
    private int pageIndex = 0;

    private String pId;

    private String type;

    private List<Role> roles;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Note> result = (List<Note>) msg.obj;
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
            adapter.notifyDataSetChanged();
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
        pId = getIntent().getStringExtra("pId");
        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }

    private boolean isXfwd() {
        for (Role r : roles) {
            if ("4250".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isZfzz() {
        for (Role r : roles) {
            if ("4251".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        if (isXfwd() && "1".equals(type)) {
            tvRight.setText("添加");
            tvRight.setVisibility(View.VISIBLE);
        } else if (isZfzz() && ("2".equals(type) || "3".equals(type) || "4".equals(type) || "5"
                .equals(type) || "6".equals(type) || "7".equals(type))) {
            tvRight.setText("添加");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        if ("1".equals(type)) {
            tvTitle.setText("工作日志");
        } else if ("2".equals(type) || "3".equals(type) || "4".equals(type) || "7".equals(type)) {
            tvTitle.setText("管控记录");
        } else if ("6".equals(type)) {
            tvTitle.setText("安置帮教日志");
        } else if ("5".equals(type)) {
            tvTitle.setText("监管日志");
        }
        adapter = new NoteAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }

    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        pageIndex = 0;
        loadData(AutoListView.REFRESH);
    }

    private void loadData(final int what) {
        String url = ConstantUtil.BASE_URL + "/note/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pId", pId);
        params.put("type", type);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryListByPage", params, new
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
                                List<Note> newses = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Note>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = what;
                                msg.obj = newses;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(ZdryGzrzActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(ZdryGzrzActivity.this, R.string.load_fail);
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
                if ("1".equals(type)) {
                    intent.setClass(ZdryGzrzActivity.this, ZdryGzrzFbActivity.class);
                } else if ("2".equals(type) || "3".equals(type)) {
                    intent.setClass(ZdryGzrzActivity.this, ZszhjsbhzGzrzFbActivity.class);
                } else if ("4".equals(type)) {
                    intent.setClass(ZdryGzrzActivity.this, SqjdryGzrzFbActivity.class);
                } else if ("5".equals(type) || "6".equals(type) || "7".equals(type)) {
                    intent.setClass(ZdryGzrzActivity.this, SqfxryGzrzFbActivity.class);
                }
                intent.putExtra("pId", pId);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryListByPage");
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
            if ("1".equals(type)) {
                intent.setClass(ZdryGzrzActivity.this, ZdryGzrzDetailActivity.class);
            } else if ("2".equals(type) || "3".equals(type)) {
                intent.setClass(ZdryGzrzActivity.this, ZszhjsbhzGzrzDetailActivity.class);
            } else if ("4".equals(type)) {
                intent.setClass(ZdryGzrzActivity.this, SqjdryGzrzDetailActivity.class);
            } else if ("5".equals(type) || "6".equals(type) || "7".equals(type)) {
                intent.setClass(ZdryGzrzActivity.this, SqfxryGzrzDetailActivity.class);
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("note", list.get(position - 1));
            intent.putExtras(bundle);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }
}

package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.GridViewAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Dangxiaozu;
import com.xzz.hxjdglpt.model.Dxz;
import com.xzz.hxjdglpt.model.Dzb;
import com.xzz.hxjdglpt.model.PartyMember;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 展示信息LIST
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_dzz_dzb_dxz_list)
public class Dzz_DxzListActivity extends BaseActivity {

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.dzz_dzb_dxz)
    private LinearLayout mLay;


    private User user;

    private Dzb obj = null;

    private StringBuffer strIds = new StringBuffer();

    private LayoutInflater layoutInflater;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        obj = getIntent().getParcelableExtra("data");
        mContext = this;
        layoutInflater = LayoutInflater.from(this);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("党小组");
    }


    public void initData() {
        List<Dxz> list = obj.getDxz();
        for (Dxz d : list) {
            strIds.append(d.getXid()).append(",");
        }
        request();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Dangxiaozu> list = (List<Dangxiaozu>) msg.obj;
            for (Dangxiaozu z : list) {
                Dxz dd = null;
                List<Dxz> dxzs = obj.getDxz();
                for (Dxz d : dxzs) {
//                    if (d.getXid() == Long.valueOf(z.getPid())) {
                    if (d.getXid() != null && String.valueOf(d.getXid()).equals(z.getPid())) {
                        dd = d;
                        break;
                    }
                }
                if (dd != null) {
                    View v = layoutInflater.inflate(R.layout.dzz_dxz_item, null);
                    TextView tvName = (TextView) v.findViewById(R.id.dzz_dxz_name);
                    if (!StringUtil.isEmpty(dd.getXname()))
                        tvName.setText(dd.getXname());

                    TextView tab1 = (TextView) v.findViewById(R.id.dzz_lab1);
                    tab1.setText(z.getName());

                    TextView tab2 = (TextView) v.findViewById(R.id.dzz_lab2);
                    tab2.setText(z.getPhone());

                    GridView tab3 = (GridView) v.findViewById(R.id.dzz_gridview);
                    GridViewAdapter adapter = new GridViewAdapter(Dzz_DxzListActivity.this, tab3, z.getXzcy());
                    tab3.setAdapter(adapter);
                    final List<PartyMember> data = z.getXzcy();
                    final String gId = z.getGridid();
                    tab3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            PartyMember p = (PartyMember) data.get(i);
                            intent.putExtra("gridId", gId);
                            intent.setClass(Dzz_DxzListActivity.this, PartyMemberInfo.class);
                            bundle.putParcelable("partyMember", p);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    setGridViewHeight(tab3);
                    adapter.notifyDataSetChanged();

                    TextView tab4 = (TextView) v.findViewById(R.id.dzz_lab4);
                    tab4.setText(gId);

                    TextView tab5 = (TextView) v.findViewById(R.id.dzz_lab5);
                    tab5.setText(z.getWgy());

                    TextView tab8 = (TextView) v.findViewById(R.id.dzz_lab8);
                    tab8.setText(z.getWgyphone());

                    mLay.addView(v);
                }
            }
        }
    };

    public void setGridViewHeight(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Event(value = {R.id.iv_back_tv}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
        }
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(Dzz_DxzListActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dzz/getZhibuOrXiaozuList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("ids", strIds.toString());
        params.put("hasDzz", "0");
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
                                Gson gson = new Gson();
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                List<Dangxiaozu> objs = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Dangxiaozu>>() {
                                        }.getType());
                                Message msg = handler.obtainMessage();
                                msg.what = 2;
                                msg.obj = objs;
                                handler.sendMessage(msg);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(Dzz_DxzListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(Dzz_DxzListActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            SuccinctProgress.dismiss();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
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
    }


}

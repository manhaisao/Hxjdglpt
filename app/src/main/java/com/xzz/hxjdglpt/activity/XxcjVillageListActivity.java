package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.ExListItemClickHelp;
import com.xzz.hxjdglpt.adapter.MyExpandablelistviewAdapter;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
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
 * 已办事项
 * Created by dbz on 2017/5/26.
 */
@ContentView(R.layout.aty_xxcj_village_list)
public class XxcjVillageListActivity extends BaseActivity implements ExListItemClickHelp {
    @ViewInject(R.id.xxcj_village_list)
    private ExpandableListView listView;

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private MyExpandablelistviewAdapter adapter;
    private List<Village> list = new ArrayList<Village>();
    private int type = 0;

    private String isFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        type = getIntent().getIntExtra("type", 0);
        isFrom = getIntent().getStringExtra("isFrom");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(getIntent().getStringExtra("title"));
        adapter = new MyExpandablelistviewAdapter(this, list, this, type);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
                    childPosition, long id) {
                //点击childitem
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                switch (type) {
                    case 8://党建
                        intent.setClass(XxcjVillageListActivity.this, DjActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 5://民政
                        Grid g = list.get(groupPosition).getgList().get
                                (childPosition);
                        if (getString(R.string.jly).equals(g.getZm())) {
                            intent.setClass(XxcjVillageListActivity.this, JlyActivity.class);
                        } else {
                            intent.setClass(XxcjVillageListActivity.this, MzActivity.class);
                            bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                    (childPosition));
                            intent.putExtras(bundle);
                        }
                        startActivity(intent);
                        break;
                    case 1://城市长效管理
                        intent.setClass(XxcjVillageListActivity.this, CscxglActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 10://综合执法
                        intent.setClass(XxcjVillageListActivity.this, FwzfActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 9://和谐社区
                        intent.setClass(XxcjVillageListActivity.this, XfwdActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 0://小区归社区
                        intent.putExtra("gridId", list.get(groupPosition).getgList().get
                                (childPosition).getId());
                        intent.putExtra("type", 7);
                        intent.putExtra("isFrom", isFrom);
                        intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                        startActivity(intent);
                        break;
                    case 6://政法综治
                        intent.setClass(XxcjVillageListActivity.this, SfxzActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 3://卫生健康
                        intent.setClass(XxcjVillageListActivity.this, JhsyActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 4://企业服务
                        intent.setClass(XxcjVillageListActivity.this, QygzActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        intent.putExtra("isYjfw", getIntent().getStringExtra("isYjfw"));
                        startActivity(intent);
                        break;
                    case 7://协税护税
                        intent.setClass(XxcjVillageListActivity.this, XshsActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 2://应急服务
                        intent.setClass(XxcjVillageListActivity.this, AqscActivity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        intent.putExtra("isYjfw", getIntent().getStringExtra("isYjfw"));
                        startActivity(intent);
                        break;
                    case 11://劳动保障
                        /*
                        intent.setClass(XxcjVillageListActivity.this, LsCollect09Activity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        bundle.putInt("type",2);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        */
                        intent.setClass(XxcjVillageListActivity.this, LsCollect08Activity.class);
                        bundle.putParcelable("grid", list.get(groupPosition).getgList().get
                                (childPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    public void initData() {
        loadData();
    }


    private void loadData() {
        String url = ConstantUtil.BASE_URL + "/village/queryVillageAndGridList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryVillageAndGridList", params, new
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
                                List<Village> newses = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Village>>() {
                                        }.getType());
                                if (type == 5) {
                                    for (Village v : newses) {
                                        if ("8".equals(v.getId())) {
                                            Grid g = new Grid();
//                                            g.setId("jly");
                                            g.setZm(getString(R.string.jly));
                                            v.getgList().add(g);
                                        }
                                    }
                                } else if (type == 2) {
                                    Village v = new Village();
                                    v.setName("应急单位");
                                    newses.add(v);
                                } else if (type == 3) {
                                    Village v = new Village();
                                    v.setName("医院");
                                    newses.add(v);
                                } else if (type == 4) {
                                    Village village = new Village();
                                    village.setName("经济开发区");
                                    village.setId("jjkfq");
                                    newses.add(village);
                                } else if (type == 11) {
                                    Village shldbzgzz = new Village();
                                    shldbzgzz.setName("社会劳动保障工作站");
                                    Village dbshbx = new Village();
                                    dbshbx.setName("代办社会保险");
                                    Village bzdnmbzfw = new Village();
                                    bzdnmbzfw.setName("被征地农民保障服务");
                                    Village ldjcldzyzc = new Village();
                                    ldjcldzyzc.setName("劳动监察、劳动争议仲裁");
                                    Village cxjmjbylbx = new Village();
                                    cxjmjbylbx.setName("城乡居民基本养老保险");
                                    Village cxjmylbx = new Village();
                                    cxjmylbx.setName("城乡居民医疗保险");
                                    newses.add(shldbzgzz);
                                    newses.add(dbshbx);
                                    newses.add(bzdnmbzfw);
                                    newses.add(ldjcldzyzc);
                                    newses.add(cxjmjbylbx);
                                    newses.add(cxjmylbx);
                                }
                                list.addAll(newses);
                                adapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(XxcjVillageListActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(XxcjVillageListActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryVillageAndGridList");
    }

    @Override
    public void onItemClick(int groupPosition, boolean isExpanded, View view) {

        Intent intent = new Intent();
        intent.putExtra("type", type);
        Bundle bundle = new Bundle();
        switch (type) {
            case 8://党建
                intent.setClass(XxcjVillageListActivity.this, VillageDjActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 5://民政
                intent.setClass(XxcjVillageListActivity.this, VillageMzActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 1://城市长效管理
                intent.setClass(XxcjVillageListActivity.this, VillageCscxglActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 10://综合执法
                intent.setClass(XxcjVillageListActivity.this, VillageFwzfActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 9://和谐社区
                intent.setClass(XxcjVillageListActivity.this, VillageXfwdActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 6://政法综治
                intent.setClass(XxcjVillageListActivity.this, VillageSfxzActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 3://卫生健康
                if ("医院".equals(list.get(groupPosition).getName())) {
                    intent.putExtra("type", 64);
                    intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(XxcjVillageListActivity.this, VillageJhsyActivity.class);
                    bundle.putParcelable("village", list.get(groupPosition));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case 4://企业服务
                Village v = list.get(groupPosition);
                if ("jjkfq".equals(v.getId())) {
                    intent.setClass(XxcjVillageListActivity.this, QygzActivity.class);
                    Grid g = new Grid();
                    g.setId("9999");
                    bundle.putParcelable("grid", g);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    intent.setClass(XxcjVillageListActivity.this, VillageQygzActivity.class);
                    bundle.putParcelable("village", v);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case 7://协税护税
                intent.setClass(XxcjVillageListActivity.this, VillageXshsActivity.class);
                bundle.putParcelable("village", list.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 2://应急服务
                if ("应急单位".equals(list.get(groupPosition).getName())) {
                    intent.putExtra("type", 63);
                    intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(XxcjVillageListActivity.this, VillageAqscActivity.class);
                    bundle.putParcelable("village", list.get(groupPosition));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case 11://劳动保障
                if ("社会劳动保障工作站".equals(list.get(groupPosition).getName())) {
                    intent.putExtra("type", 65);
                    intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                    startActivity(intent);
                } else if ("代办社会保险".equals(list.get(groupPosition).getName())) {
                    intent.putExtra("type", 66);
                    intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                    startActivity(intent);
                } else if ("被征地农民保障服务".equals(list.get(groupPosition).getName())) {
                    intent.putExtra("type", 67);
                    intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                    startActivity(intent);
                } else if ("劳动监察、劳动争议仲裁".equals(list.get(groupPosition).getName())) {
                    intent.putExtra("type", 68);
                    intent.setClass(XxcjVillageListActivity.this, ListActivity.class);
                    startActivity(intent);
                } else if ("城乡居民基本养老保险".equals(list.get(groupPosition).getName())) {
                    intent.setClass(XxcjVillageListActivity.this, LsCollect05Activity.class);
                    startActivity(intent);
                } else if ("城乡居民医疗保险".equals(list.get(groupPosition).getName())) {
                    intent.setClass(XxcjVillageListActivity.this, LsCollect06Activity.class);
                    startActivity(intent);
                } else {
                    /*intent.setClass(XxcjVillageListActivity.this, LsCollect09Activity.class);
                    bundle.putParcelable("village", list.get(groupPosition));
                    bundle.putInt("type",1);
                    intent.putExtras(bundle);
                    startActivity(intent);*/
                    intent.setClass(XxcjVillageListActivity.this, LsCollect07Activity.class);
                    bundle.putParcelable("village", list.get(groupPosition));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }
}

package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnLoadListener;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView.OnRefreshListener;
import com.xzz.hxjdglpt.model.Azbjry;
import com.xzz.hxjdglpt.model.Chengpin;
import com.xzz.hxjdglpt.model.CityInfo;
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Company;
import com.xzz.hxjdglpt.model.Dbry;
import com.xzz.hxjdglpt.model.Dszn;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.Hospital;
import com.xzz.hxjdglpt.model.Jlfz;
import com.xzz.hxjdglpt.model.Jskn;
import com.xzz.hxjdglpt.model.Jzgd;
import com.xzz.hxjdglpt.model.Ldrk;
import com.xzz.hxjdglpt.model.Lsrt;
import com.xzz.hxjdglpt.model.Management;
import com.xzz.hxjdglpt.model.ManagementQt;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.Roupidouzhi;
import com.xzz.hxjdglpt.model.Sqfxry;
import com.xzz.hxjdglpt.model.Sqjdry;
import com.xzz.hxjdglpt.model.Tezhongweixian;
import com.xzz.hxjdglpt.model.Tkgy;
import com.xzz.hxjdglpt.model.Tshd;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.Xjry;
import com.xzz.hxjdglpt.model.Yanhua;
import com.xzz.hxjdglpt.model.Yfdx;
import com.xzz.hxjdglpt.model.Yjdw;
import com.xzz.hxjdglpt.model.Ylfn;
import com.xzz.hxjdglpt.model.Zdqsn;
import com.xzz.hxjdglpt.model.Zdry;
import com.xzz.hxjdglpt.model.ZfzzInfo;
import com.xzz.hxjdglpt.model.ZfzzInfoVillage;
import com.xzz.hxjdglpt.model.Zlj;
import com.xzz.hxjdglpt.model.Zszhjsbhz;
import com.xzz.hxjdglpt.model.ls.Baozhang;
import com.xzz.hxjdglpt.model.ls.BaozhangJbr;
import com.xzz.hxjdglpt.model.ls.Shbx;
import com.xzz.hxjdglpt.model.ls.ShbxJbr;
import com.xzz.hxjdglpt.model.ls.Work;
import com.xzz.hxjdglpt.model.ls.Zhongcai;
import com.xzz.hxjdglpt.model.ls.ZhongcaiJbr;
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
 * 展示信息LIST
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_list)
public class ListActivity extends BaseActivity implements OnRefreshListener, OnLoadListener,
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
    private int pageIndex = 0;

    private String gridId = "";
    private int type = 0;

    private String villageId = "";

    private String isFrom;

    private boolean isRefresh = false;

    private int xfaqType = 0;//消防安全或生产安全检查

    private List<Role> roles;


    private int selectType;
    private int menuType;
    private Village village;
    private Grid grid;

    private int pid;

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
        selectType = getIntent().getIntExtra("selectType", 0);
        menuType = getIntent().getIntExtra("menuType", 0);
        village = getIntent().getParcelableExtra("village");
        grid = getIntent().getParcelableExtra("grid");

        gridId = getIntent().getStringExtra("gridId");
        type = getIntent().getIntExtra("type", 0);
        xfaqType = getIntent().getIntExtra("xfaqType", 0);
        tvRight.setText("新增");
        isFrom = getIntent().getStringExtra("isFrom");

        pid = getIntent().getIntExtra("pid", 0);
        initView();
        initData();
    }

    public void initView() {
        if (isFrom != null && "index".equals(isFrom)) {
            tvRight.setVisibility(View.GONE);
        }
        switch (type) {
            case -1:
                villageId = getIntent().getStringExtra("vId");
                tvTitle.setText("特色党建活动");
                break;
            case 0:
                tvTitle.setText(R.string.party_info_);
                break;
            case 1:
                tvTitle.setText(R.string.csdb);
                break;
            case 2:
                tvTitle.setText(R.string.ncdb);
                break;
            case 3:
                tvTitle.setText(R.string.ylfn);
                break;
            case 4:
                tvTitle.setText(R.string.qy);
                break;
            case 5:
                tvTitle.setText(R.string.gt);
                break;
            case 6:
            case 8://持证残疾人
            case 9://享受重残生活补贴人员
            case 10://享受其他类生活补贴人员
            case 11://享受一级护理补贴人员
            case 12://享受二级护理补贴人员
                tvTitle.setText(R.string.cjr);
                break;
            case 7:
                tvTitle.setText(R.string.xqgsq);
                break;
            case 13:
                tvTitle.setText("留守儿童");
                break;
            case 14:
                tvTitle.setText("优抚对象");
                break;
            case 15:
                tvTitle.setText("特困(五保)供养");
                break;
            case 16:
                tvTitle.setText("80岁以上尊老金");
                break;
            case 17:
                tvTitle.setText("道路信息");
                break;
            case 18:
                tvTitle.setText("河塘信息");
                break;
            case 19:
                tvTitle.setText("垃圾清运员");
                break;
            case 20:
                tvTitle.setText("流动保洁员");
                break;
            case 21:
                tvTitle.setText("矛盾纠纷调解员");
                break;
            case 22:
                tvTitle.setText("矛盾纠纷信息员");
                break;
            case 23:
                tvTitle.setText("法制宣传员");
                break;
            case 24:
                tvTitle.setText("普法志愿者");
                break;
            case 25:
                tvTitle.setText("法律顾问");
                break;
            case 26:
                tvTitle.setText("独生子女");
                break;
            case 27:
                tvTitle.setText("省扶");
                break;
            case 28:
                tvTitle.setText("市扶");
                break;
            case 29:
                tvTitle.setText("失独");
                break;
            case 30:
                tvTitle.setText("伤残");
                break;
            case 31:
                tvTitle.setText("流动人口");
                break;
            case 32:
                tvTitle.setText("计生困难");
                break;
            case 33:
                tvTitle.setText("集中供养人员");
                break;
            case 34:
                tvTitle.setText("分散供养人员");
                break;
            case 35:
                tvTitle.setText("城市三无人员");
                break;
            case 36:
                tvTitle.setText("孤儿及困境儿童");
                break;
            case 37:
                tvTitle.setText("80-89周岁人员");
                break;
            case 38:
                tvTitle.setText("90-99周岁人员");
                break;
            case 39:
                tvTitle.setText("100周岁以上");
                break;
            case 40:
                tvTitle.setText("列统企业");
                break;
            case 41:
                tvTitle.setText("民营企业");
                break;
            case 42:
                tvTitle.setText("集体企业");
                break;
            case 100:
                tvTitle.setText("总部企业");
                break;
            case 43:
                tvTitle.setText(R.string.jyyhbzs_);
                break;
            case 44:
                tvTitle.setText(R.string.rpzf);
                break;
            case 45:
                tvTitle.setText(R.string.dzpjgf);
                break;
            case 46:
                tvTitle.setText("建筑工地");
                break;
            case 47:
                tvTitle.setText("特种设备");
                break;
            case 48:
                tvTitle.setText("危险化学品");
                break;
            case 49:
                tvTitle.setText("成品油");
                break;
            case 50:
                tvTitle.setText("矛盾纠纷调解员");
                villageId = getIntent().getStringExtra("vId");
                break;
            case 51:
                tvTitle.setText("矛盾纠纷信息员");
                villageId = getIntent().getStringExtra("vId");
                break;
            case 52:
                tvTitle.setText("法制宣传员");
                villageId = getIntent().getStringExtra("vId");
                break;
            case 53:
                tvTitle.setText("普法志愿者");
                villageId = getIntent().getStringExtra("vId");
                break;
            case 54:
                tvTitle.setText("法律顾问");
                villageId = getIntent().getStringExtra("vId");
                break;
            case 55:
                tvTitle.setText("奖励扶助");
                break;
            case 56:
                tvTitle.setText("信访诉求人员");
                break;
            case 57:
                tvTitle.setText("肇事肇祸精神病患者");
                break;
            case 58:
                tvTitle.setText("重点青少年");
                break;
            case 59:
                tvTitle.setText("社区戒毒人员");
                break;
            case 60:
                tvTitle.setText("邪教人员");
                break;
            case 61:
                tvTitle.setText("社区服刑人员");
                break;
            case 62:
                tvTitle.setText("安置帮教人员");
                break;
            case 63:
                tvTitle.setText("应急单位");
                break;
            case 64:
                tvTitle.setText("医院");
                break;
            case 65:
                tvTitle.setText("社会劳动保障工作站");
                break;
            case 66:
                tvTitle.setText("代办社会保险");
                break;
            case 67:
                tvTitle.setText("被征地农民保障服务");
                break;
            case 68:
                tvTitle.setText("劳动监察、劳动争议仲裁");
                break;
            case 71:
                tvTitle.setText("城乡居民基础养老保险");
                break;
            case 72:
                tvTitle.setText("城乡居民基础养老保险");
                break;
            case 73:
                tvTitle.setText("部门经办人");
                break;
            case 74:
                tvTitle.setText("部门经办人");
                break;
            case 75:
                tvTitle.setText("街道协理员");
                break;
            case 76:
                tvTitle.setText("街道网格员");
                break;
            case 77:
                tvTitle.setText("村居网格员");
                break;
            case 78:
                tvTitle.setText("保安");
                break;
        }
        adapter = new ListAdapter(this, list, this, isFrom);
        switch (type) {
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
                adapter = new ListAdapter(this, list, this, isFrom, isContain());
                break;
            case 7:
                adapter = new ListAdapter(this, list, this, isFrom, this);
                break;
        }
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
        switch (type) {
            case -1:
                loadTsdjhdoData(AutoListView.REFRESH);
                break;
            case 0:
                loadDyData(AutoListView.REFRESH);
                break;
            case 1:
                loadCsdbData(AutoListView.REFRESH);
                break;
            case 2:
                loadNcdbData(AutoListView.REFRESH);
                break;
            case 3:
                loadYlfnData(AutoListView.REFRESH);
                break;
            case 4:
            case 40:
            case 41:
            case 42:
            case 100:
                loadQyData(AutoListView.REFRESH);
                break;
            case 5:
                loadGtData(AutoListView.REFRESH);
                break;
            case 43:
                loadYanhuaData(AutoListView.REFRESH);
                break;
            case 44:
            case 45:
                loadRoupidouzhiData(AutoListView.REFRESH);
                break;
            case 6:
                loadCjrData(AutoListView.REFRESH);
                break;
            case 7:
                loadXqData(AutoListView.REFRESH);
                break;
            case 8://持证残疾人
            case 9://享受重残生活补贴人员
            case 10://享受其他类生活补贴人员
            case 11://享受一级护理补贴人员
            case 12://享受二级护理补贴人员
                loadCjrDataByType(AutoListView.REFRESH);
                break;
            case 13:
                loadLsetData(AutoListView.REFRESH);
                break;
            case 14:
                loadYfdxData(AutoListView.REFRESH);
                break;
            case 15:
                loadTkgyData(AutoListView.REFRESH);
                break;
            case 16:
                loadZljData(AutoListView.REFRESH);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 76:
            case 77:
                loadCityInfoData(AutoListView.REFRESH);
                break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                loadZfzzInfoData(AutoListView.REFRESH);
                break;
            case 26:
                loadDsznInfoData(AutoListView.REFRESH);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 55:
                loadJlfzInfoData(AutoListView.REFRESH);
                break;
            case 31:
                loadLdrkInfoData(AutoListView.REFRESH);
                break;
            case 32:
                loadJsknInfoData(AutoListView.REFRESH);
                break;
            case 33:
            case 34:
            case 35:
            case 36:
                loadTkgyData(AutoListView.REFRESH);
                break;
            case 37:
            case 38:
            case 39:
                loadZljData(AutoListView.REFRESH);
                break;
            case 46:
                loadJzgdData(AutoListView.REFRESH);
                break;
            case 47:
            case 48:
                loadTezhongweixianData(AutoListView.REFRESH);
                break;
            case 49:
                loadChengpinData(AutoListView.REFRESH);
                break;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                loadZfzzInfoVillageData(AutoListView.REFRESH);
                break;
            case 56:
                loadZdry(AutoListView.REFRESH);
                break;
            case 57:
                loadJsbhz(AutoListView.REFRESH);
                break;
            case 58:
                loadZdqsn(AutoListView.REFRESH);
                break;
            case 59:
                loadSqjd(AutoListView.REFRESH);
                break;
            case 60:
                loadXjry(AutoListView.REFRESH);
                break;
            case 61:
                loadSqfx(AutoListView.REFRESH);
                break;
            case 62:
                loadAzbj(AutoListView.REFRESH);
                break;
            case 63:
                loadYjdw(AutoListView.REFRESH);
                break;
            case 64:
                loadYy(AutoListView.REFRESH);
                break;
            case 65:
                loadWorkstation(AutoListView.REFRESH);
                break;
            case 66:
                loadShbx(AutoListView.REFRESH);
                break;
            case 67:
                loadBzfw(AutoListView.REFRESH);
                break;
            case 68:
                loadZyzc(AutoListView.REFRESH);
                break;
            case 71:
                ToastUtil.show(ListActivity.this, "71");
                break;
            case 72:
                ToastUtil.show(ListActivity.this, "72");
                break;
            case 73:
                loadShbxJbr(AutoListView.REFRESH);
                break;
            case 74:
                loadBzfwJbr(AutoListView.REFRESH);
                break;
            case 75:
                loadZyzcJbr(AutoListView.REFRESH);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            pageIndex = 0;
            initData();
        }
    }

    private void loadWorkstation(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/work1/queryByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
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
                        List<Work> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Work>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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


    private void loadShbx(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/shbx/queryByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
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
                        List<Shbx> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Shbx>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadBzfw(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/bzfw/queryByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
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
                        List<Baozhang> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Baozhang>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadZyzc(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zyzc/queryByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
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
                        List<Zhongcai> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Zhongcai>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadShbxJbr(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/shbx/listbyPid";
        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(pid));
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
                        List<ShbxJbr> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<ShbxJbr>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadBzfwJbr(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/bzfw/listbyPid";
        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(pid));
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
                        List<BaozhangJbr> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<BaozhangJbr>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadZyzcJbr(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zyzc/listbyPid";
        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(pid));
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
                        List<ZhongcaiJbr> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<ZhongcaiJbr>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadYy(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/hospital/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
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
                        List<Hospital> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Hospital>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadYjdw(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/yjdw/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
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
                        List<Yjdw> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Yjdw>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadAzbj(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/azbjry/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Azbjry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Azbjry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadSqfx(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sqfxry/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Sqfxry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Sqfxry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadXjry(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xjry/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Xjry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Xjry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadSqjd(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sqjdry/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Sqjdry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Sqjdry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadZdqsn(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zdqsn/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Zdqsn> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Zdqsn>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadJsbhz(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zszhjsbhz/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Zszhjsbhz> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Zszhjsbhz>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadZdry(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zdry/queryListByPage";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Zdry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Zdry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 信息获取
     */
    private void loadZfzzInfoVillageData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zfzzinfovillage/queryZfzzInfoVillageList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("vId", villageId);
        if (type == 50) {
            params.put("type", "矛盾纠纷调解员");
        } else if (type == 51) {
            params.put("type", "矛盾纠纷信息员");
        } else if (type == 52) {
            params.put("type", "法制宣传员");
        } else if (type == 53) {
            params.put("type", "普法志愿者");
        } else if (type == 54) {
            params.put("type", "法律顾问");
        }
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
                        List<ZfzzInfoVillage> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<ZfzzInfoVillage>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadTsdjhdoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/tshd/queryTshdList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("vId", villageId);
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
                        List<Tshd> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Tshd>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadJzgdData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jzgd/queryJzgdList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Jzgd> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jzgd>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadQtInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/managementQt/queryManagementQtList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 47) {
            params.put("type", "特种设备");
        } else if (type == 48) {
            params.put("type", "危险化学品");
        } else if (type == 49) {
            params.put("type", "成品油");
        }
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
                        List<ManagementQt> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<ManagementQt>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadJsknInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jskn/queryJsknList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Jskn> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jskn>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadLdrkInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/ldrk/queryLdrkList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Ldrk> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Ldrk>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadJlfzInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jlfz/queryJlfzList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 27) {
            params.put("type", "省扶");
        } else if (type == 28) {
            params.put("type", "市扶");
        } else if (type == 29) {
            params.put("type", "失独");
        } else if (type == 30) {
            params.put("type", "伤残");
        }
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
                        List<Jlfz> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Jlfz>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 信息获取
     */
    private void loadDsznInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dszn/queryDsznList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        Gson gson = new Gson();
                        List<Dszn> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Dszn>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 信息获取
     */
    private void loadZfzzInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zfzzinfo/queryZfzzInfoList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 21) {
            params.put("type", "矛盾纠纷调解员");
        } else if (type == 22) {
            params.put("type", "矛盾纠纷信息员");
        } else if (type == 23) {
            params.put("type", "法制宣传员");
        } else if (type == 24) {
            params.put("type", "普法志愿者");
        } else if (type == 25) {
            params.put("type", "法律顾问");
        }
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
                        List<ZfzzInfo> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<ZfzzInfo>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 信息获取
     */
    private void loadCityInfoData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cityinfo/queryCityinfoList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 17) {
            params.put("type", "01");
        } else if (type == 18) {
            params.put("type", "02");
        } else if (type == 19) {
            params.put("type", "03");
        } else if (type == 20) {
            params.put("type", "04");
        }
        else if (type == 76) {
            params.put("type", "05");
        }
        else if (type == 77) {
            params.put("type", "06");
        }
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
                        List<CityInfo> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<CityInfo>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 信息获取
     */
    private void loadTkgyData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/tkgy/queryTkgyList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 33) {
            params.put("type", "集中供养人员");
        } else if (type == 34) {
            params.put("type", "分散供养人员");
        } else if (type == 35) {
            params.put("type", "城市三无人员");
        } else if (type == 36) {
            params.put("type", "孤儿及困境儿童");
        }
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
                        List<Tkgy> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Tkgy>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 信息获取
     */
    private void loadZljData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/zlj/queryZljList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 37) {
            params.put("type", "80-89周岁人员");
        } else if (type == 38) {
            params.put("type", "90-99周岁人员");
        } else if (type == 39) {
            params.put("type", "100周岁以上");
        }
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
                        List<Zlj> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Zlj>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 留守儿童信息获取
     */
    private void loadLsetData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/lset/queryLsetList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Lsrt> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Lsrt>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 优抚对象信息获取
     */
    private void loadYfdxData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/yfdx/queryYfdxList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
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
                        List<Yfdx> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Yfdx>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 小区信息获取
     */
    private void loadXqData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/queryPlotList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    Log.e("insert",resultCode);
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Plot> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Plot>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 党员信息获取
     */
    private void loadDyData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/party/queryPartyMemberList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (getIntent().getStringExtra("dyType") != null) {
            params.put("type", getIntent().getStringExtra("dyType"));
        }
        if (getIntent().getStringExtra("dyldType") != null) {
            params.put("isliuru", getIntent().getStringExtra("dyldType"));
        }
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
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
                        List<PartyMember> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<PartyMember>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 城市低保信息获取
     */
    private void loadCsdbData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dbry/queryDbList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        params.put("type", "0");
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
                        List<Dbry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Dbry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 农村低保信息获取
     */
    private void loadNcdbData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dbry/queryDbList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        params.put("type", "1");
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
                        List<Dbry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Dbry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 残疾人信息获取
     */
    private void loadCjrData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cjry/queryCjryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
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
                        List<Cjry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Cjry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 残疾人信息获取
     */
    private void loadCjrDataByType(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cjry/queryCjryListByType";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 8) {
            params.put("type", "持证残疾人");
        } else if (type == 9) {
            params.put("type", "享受重残生活补贴人员");
        } else if (type == 10) {
            params.put("type", "享受其他类生活补贴人员");
        } else if (type == 11) {
            params.put("type", "享受一级护理补贴人员");
        } else if (type == 12) {
            params.put("type", "享受二级护理补贴人员");
        }
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
                        List<Cjry> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Cjry>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 企业信息获取
     */
    private void loadQyData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/company/queryCompanyList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (getIntent().getStringExtra("isYjfw") != null) {
            params.put("isYjfw", getIntent().getStringExtra("isYjfw"));
        }
        if (type == 40) {
            params.put("type", "01");
        } else if (type == 41) {
            params.put("type", "02");
        } else if (type == 42) {
            params.put("type", "03");
        } else if (type == 100) {
            params.put("type", "04");
        }
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
                        List<Company> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Company>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 育龄妇女信息获取
     */
    private void loadYlfnData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/ylfn/queryYlfnList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
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
                        List<Ylfn> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Ylfn>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 肉皮作坊、豆制品加工
     *
     * @param what
     */
    private void loadRoupidouzhiData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/rpdz/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 44) {
            params.put("type", "肉皮作坊");
        } else if (type == 45) {
            params.put("type", "豆制品加工");
        }
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
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
                        List<Roupidouzhi> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Roupidouzhi>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadTezhongweixianData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/tzwx/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        if (type == 47) {
            params.put("type", "特种设备");
        } else if (type == 48) {
            params.put("type", "危险化学品");
        }
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Tezhongweixian> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Tezhongweixian>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadChengpinData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/cpy/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Chengpin> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Chengpin>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    private void loadYanhuaData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/yh/queryList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Yanhua> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Yanhua>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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

    /**
     * 个体信息获取
     */
    private void loadGtData(final int what) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/management/queryManagementList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        params.put("gridId", gridId);
        params.put("type", "");
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
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
                        List<Management> objs = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Management>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = objs;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.load_fail);
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
                intent.putExtra("gridId", gridId);
                intent.putExtra("isAdd", true);
                isRefresh = true;
                switch (type) {
                    case -1:
                        intent.putExtra("vId", villageId);
                        intent.setClass(ListActivity.this, TsdjhdActivity.class);
                        startActivity(intent);
                        break;
                    case 0:
                        intent.setClass(ListActivity.this, PartyActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                    case 2:
                        intent.setClass(ListActivity.this, DbryActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(ListActivity.this, YlfnInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                    case 40:
                    case 41:
                    case 42:
                    case 100:
                        if (getIntent().getStringExtra("isYjfw") != null) {
                            intent.putExtra("isYjfw", getIntent().getStringExtra("isYjfw"));
                        }
                        intent.setClass(ListActivity.this, CompanyActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent.setClass(ListActivity.this, ManagementActivity.class);
                        startActivity(intent);
                        break;
                    case 43:
                        intent.setClass(ListActivity.this, YanhuaActivity.class);
                        startActivity(intent);
                        break;
                    case 44:
                    case 45:
                        intent.putExtra("type", type);
                        intent.setClass(ListActivity.this, RoupidouzhiActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                    case 8://持证残疾人
                    case 9://享受重残生活补贴人员
                    case 10://享受其他类生活补贴人员
                    case 11://享受一级护理补贴人员
                    case 12://享受二级护理补贴人员
                        intent.setClass(ListActivity.this, CjryActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent.setClass(ListActivity.this, PlotActivity.class);
                        intent.putExtra("gridId",gridId);
                        startActivity(intent);
                        break;
                    case 13:
                        intent.setClass(ListActivity.this, LsrtActivity.class);
                        startActivity(intent);
                        break;
                    case 14:
                        intent.setClass(ListActivity.this, YfdxActivity.class);
                        startActivity(intent);
                        break;
                    case 15:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                        intent.setClass(ListActivity.this, TkgyActivity.class);
                        startActivity(intent);
                        break;
                    case 16:
                    case 37:
                    case 38:
                    case 39:
                        intent.setClass(ListActivity.this, ZljActivity.class);
                        startActivity(intent);
                        break;
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 76:
                    case 77:
                        intent.putExtra("type", type);
                        intent.setClass(ListActivity.this, CityInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                        intent.putExtra("type", type);
                        intent.setClass(ListActivity.this, ZfzzInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 26:
                        intent.putExtra("type", type);
                        intent.setClass(ListActivity.this, DsznInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 55:
                        intent.setClass(ListActivity.this, JlfzActivity.class);
                        startActivity(intent);
                        break;
                    case 31:
                        intent.setClass(ListActivity.this, LdrkActivity.class);
                        startActivity(intent);
                        break;
                    case 32:
                        intent.setClass(ListActivity.this, JsknActivity.class);
                        startActivity(intent);
                        break;
                    case 46:
                        intent.setClass(ListActivity.this, JzgdActivity.class);
                        startActivity(intent);
                        break;
                    case 47:
                    case 48:
                        intent.putExtra("type", type);
                        intent.setClass(ListActivity.this, TezhongweixianActivity.class);
                        startActivity(intent);
                        break;
                    case 49:
                        intent.setClass(ListActivity.this, ChengpinActivity.class);
                        startActivity(intent);
                        break;
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                        intent.putExtra("type", type);
                        intent.putExtra("vId", villageId);
                        intent.setClass(ListActivity.this, ZfzzInfoVillageActivity.class);
                        startActivity(intent);
                        break;
                    case 56:
                        intent.setClass(ListActivity.this, ZdryActivity.class);
                        startActivity(intent);
                        break;
                    case 57:
                        intent.setClass(ListActivity.this, ZszhjsbhzActivity.class);
                        startActivity(intent);
                        break;
                    case 58:
                        intent.setClass(ListActivity.this, ZdqsnActivity.class);
                        startActivity(intent);
                        break;
                    case 59:
                        intent.setClass(ListActivity.this, SqjdryActivity.class);
                        startActivity(intent);
                        break;
                    case 60:
                        intent.setClass(ListActivity.this, XjryActivity.class);
                        startActivity(intent);
                        break;
                    case 61:
                        intent.setClass(ListActivity.this, SqfxryActivity.class);
                        startActivity(intent);
                        break;
                    case 62:
                        intent.setClass(ListActivity.this, AzbjryActivity.class);
                        startActivity(intent);
                        break;
                    case 63:
                        intent.setClass(ListActivity.this, YjdwActivity.class);
                        startActivity(intent);
                        break;
                    case 64:
                        intent.setClass(ListActivity.this, HospitalActivity.class);
                        startActivity(intent);
                        break;
                    case 65:
                        intent.setClass(ListActivity.this, LsCollect01Activity.class);
                        startActivity(intent);
                        break;
                    case 66:
                        intent.setClass(ListActivity.this, LsCollect02Activity.class);
                        startActivity(intent);
                        break;
                    case 67:
                        intent.setClass(ListActivity.this, LsCollect03Activity.class);
                        startActivity(intent);
                        break;
                    case 68:
                        intent.setClass(ListActivity.this, LsCollect04Activity.class);
                        startActivity(intent);
                        break;
                    case 71:
                        intent.setClass(this, LsCollect07Activity.class);
                        intent.putExtra("menuType", 1);
                        intent.putExtra("selectType", selectType);
                        if (selectType == 1) {
                            intent.putExtra("village", village);
                        } else if (selectType == 2) {
                            intent.putExtra("grid", grid);
                        }
                        startActivity(intent);
                        break;
                    case 72:
                        intent.setClass(this, LsCollect08Activity.class);
                        intent.putExtra("menuType", 2);
                        intent.putExtra("selectType", selectType);
                        if (selectType == 1) {
                            intent.putExtra("village", village);
                        } else if (selectType == 2) {
                            intent.putExtra("grid", grid);
                        }
                        startActivity(intent);
                        break;
                    case 73:
                        intent.putExtra("pid", pid);
                        intent.setClass(ListActivity.this, LsCollect02JbrActivity.class);
                        startActivity(intent);
                        break;
                    case 74:
                        intent.putExtra("pid", pid);
                        intent.setClass(ListActivity.this, LsCollect03JbrActivity.class);
                        startActivity(intent);
                        break;
                    case 75:
                        intent.putExtra("pid", pid);
                        intent.setClass(ListActivity.this, LsCollect04JbrActivity.class);
                        startActivity(intent);
                        break;
                }
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
        pageIndex = 0;
        switch (type) {
            case -1:
                loadTsdjhdoData(AutoListView.REFRESH);
                break;
            case 0:
                loadDyData(AutoListView.REFRESH);
                break;
            case 1:
                loadCsdbData(AutoListView.REFRESH);
                break;
            case 2:
                loadNcdbData(AutoListView.REFRESH);
                break;
            case 3:
                loadYlfnData(AutoListView.REFRESH);
                break;
            case 4:
            case 40:
            case 41:
            case 42:
            case 100:
                loadQyData(AutoListView.REFRESH);
                break;
            case 5:
                loadGtData(AutoListView.REFRESH);
                break;
            case 43:
                loadYanhuaData(AutoListView.REFRESH);
                break;
            case 44:
            case 45:
                loadRoupidouzhiData(AutoListView.REFRESH);
                break;
            case 6:
                loadCjrData(AutoListView.REFRESH);
                break;
            case 7:
                loadXqData(AutoListView.REFRESH);
                break;
            case 8://持证残疾人
            case 9://享受重残生活补贴人员
            case 10://享受其他类生活补贴人员
            case 11://享受一级护理补贴人员
            case 12://享受二级护理补贴人员
                loadCjrDataByType(AutoListView.REFRESH);
                break;
            case 13:
                loadLsetData(AutoListView.REFRESH);
                break;
            case 14:
                loadYfdxData(AutoListView.REFRESH);
                break;
            case 15:
            case 33:
            case 34:
            case 35:
            case 36:
                loadTkgyData(AutoListView.REFRESH);
                break;
            case 16:
            case 37:
            case 38:
            case 39:
                loadZljData(AutoListView.REFRESH);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 76:
            case 77:
                loadCityInfoData(AutoListView.REFRESH);
                break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                loadZfzzInfoData(AutoListView.REFRESH);
                break;
            case 26:
                loadDsznInfoData(AutoListView.REFRESH);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 55:
                loadJlfzInfoData(AutoListView.REFRESH);
                break;
            case 31:
                loadLdrkInfoData(AutoListView.REFRESH);
                break;
            case 32:
                loadJsknInfoData(AutoListView.REFRESH);
                break;
            case 46:
                loadJzgdData(AutoListView.REFRESH);
                break;
            case 47:
            case 48:
                loadTezhongweixianData(AutoListView.REFRESH);
                break;
            case 49:
                loadChengpinData(AutoListView.REFRESH);
                break;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                loadZfzzInfoVillageData(AutoListView.REFRESH);
                break;
            case 56:
                loadZdry(AutoListView.REFRESH);
                break;
            case 57:
                loadJsbhz(AutoListView.REFRESH);
                break;
            case 58:
                loadZdqsn(AutoListView.REFRESH);
                break;
            case 59:
                loadSqjd(AutoListView.REFRESH);
                break;
            case 60:
                loadXjry(AutoListView.REFRESH);
                break;
            case 61:
                loadSqfx(AutoListView.REFRESH);
                break;
            case 62:
                loadAzbj(AutoListView.REFRESH);
                break;
            case 63:
                loadYjdw(AutoListView.REFRESH);
                break;
            case 64:
                loadYy(AutoListView.REFRESH);
                break;
            case 65:
                loadWorkstation(AutoListView.REFRESH);
                break;
            case 66:
                loadShbx(AutoListView.REFRESH);
                break;
            case 67:
                loadBzfw(AutoListView.REFRESH);
                break;
            case 68:
                loadZyzc(AutoListView.REFRESH);
                break;
            case 71:
                ToastUtil.show(ListActivity.this, "71");
                break;
            case 72:
                ToastUtil.show(ListActivity.this, "72");
                break;
            case 73:
                loadShbxJbr(AutoListView.REFRESH);
                break;
            case 74:
                loadBzfwJbr(AutoListView.REFRESH);
                break;
            case 75:
                loadZyzcJbr(AutoListView.REFRESH);
                break;
        }
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex + ConstantUtil.PAGE_SIZE;
        switch (type) {
            case -1:
                loadTsdjhdoData(AutoListView.LOAD);
                break;
            case 0:
                loadDyData(AutoListView.LOAD);
                break;
            case 1:
                loadCsdbData(AutoListView.LOAD);
                break;
            case 2:
                loadNcdbData(AutoListView.LOAD);
                break;
            case 3:
                loadYlfnData(AutoListView.LOAD);
                break;
            case 4:
            case 40:
            case 41:
            case 42:
            case 100:
                loadQyData(AutoListView.LOAD);
                break;
            case 5:
                loadGtData(AutoListView.LOAD);
                break;
            case 43:
                loadYanhuaData(AutoListView.LOAD);
                break;
            case 44:
            case 45:
                loadRoupidouzhiData(AutoListView.LOAD);
                break;
            case 6:
                loadCjrData(AutoListView.LOAD);
                break;
            case 7:
                loadXqData(AutoListView.LOAD);
                break;
            case 8://持证残疾人
            case 9://享受重残生活补贴人员
            case 10://享受其他类生活补贴人员
            case 11://享受一级护理补贴人员
            case 12://享受二级护理补贴人员
                loadCjrDataByType(AutoListView.LOAD);
            case 13:
                loadLsetData(AutoListView.LOAD);
                break;
            case 14:
                loadYfdxData(AutoListView.LOAD);
                break;
            case 15:
            case 33:
            case 34:
            case 35:
            case 36:
                loadTkgyData(AutoListView.LOAD);
                break;
            case 16:
            case 37:
            case 38:
            case 39:
                loadZljData(AutoListView.LOAD);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 76:
            case 77:
                loadCityInfoData(AutoListView.LOAD);
                break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                loadZfzzInfoData(AutoListView.LOAD);
                break;
            case 26:
                loadDsznInfoData(AutoListView.LOAD);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 55:
                loadJlfzInfoData(AutoListView.LOAD);
                break;
            case 31:
                loadLdrkInfoData(AutoListView.LOAD);
                break;
            case 32:
                loadJsknInfoData(AutoListView.LOAD);
                break;
            case 46:
                loadJzgdData(AutoListView.LOAD);
                break;
            case 47:
            case 48:
                loadTezhongweixianData(AutoListView.LOAD);
                break;
            case 49:
                loadChengpinData(AutoListView.LOAD);
                break;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                loadZfzzInfoVillageData(AutoListView.LOAD);
                break;
            case 56:
                loadZdry(AutoListView.LOAD);
                break;
            case 57:
                loadJsbhz(AutoListView.LOAD);
                break;
            case 58:
                loadZdqsn(AutoListView.LOAD);
                break;
            case 59:
                loadSqjd(AutoListView.LOAD);
                break;
            case 60:
                loadXjry(AutoListView.LOAD);
                break;
            case 61:
                loadSqfx(AutoListView.LOAD);
                break;
            case 62:
                loadAzbj(AutoListView.LOAD);
                break;
            case 63:
                loadYjdw(AutoListView.LOAD);
                break;
            case 64:
                loadYjdw(AutoListView.LOAD);
                break;
            case 65:
                loadWorkstation(AutoListView.LOAD);
                break;
            case 66:
                loadShbx(AutoListView.LOAD);
                break;
            case 67:
                loadBzfw(AutoListView.LOAD);
                break;
            case 68:
                loadZyzc(AutoListView.LOAD);
                break;
            case 71:
                ToastUtil.show(ListActivity.this, "71");
                break;
            case 72:
                ToastUtil.show(ListActivity.this, "72");
                break;
            case 73:
                loadShbxJbr(AutoListView.LOAD);
                break;
            case 74:
                loadBzfwJbr(AutoListView.LOAD);
                break;
            case 75:
                loadZyzcJbr(AutoListView.LOAD);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position - 1 < list.size()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.putExtra("gridId", gridId);
            isRefresh = false;
            switch (type) {
                case -1:
                    intent.setClass(ListActivity.this, TsdjhdActivity.class);
                    bundle.putParcelable("tshd", (Tshd) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 0:
                    PartyMember p = (PartyMember) list.get(position - 1);
                    intent.setClass(ListActivity.this, PartyMemberInfo.class);
                    bundle.putParcelable("partyMember", p);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 1:
                case 2:
                    Dbry d = (Dbry) list.get(position - 1);
                    intent.setClass(ListActivity.this, DbryInfo.class);
                    bundle.putParcelable("dbry", d);
                    bundle.putInt("type", type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 3:
                    Ylfn y = (Ylfn) list.get(position - 1);
                    intent.setClass(ListActivity.this, YlfnInfo.class);
                    bundle.putParcelable("ylfn", y);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 4:
                case 40:
                case 41:
                case 42:
                case 100:
                    Company cm = (Company) list.get(position - 1);
                    intent.setClass(ListActivity.this, CompanyInfo.class);
                    bundle.putParcelable("company", cm);
                    intent.putExtras(bundle);
                    intent.putExtra("type", xfaqType);
                    if (getIntent().getStringExtra("isYjfw") != null) {
                        intent.putExtra("isYjfw", getIntent().getStringExtra("isYjfw"));
                    }
                    startActivity(intent);
                    break;
                case 5:
                    Management m = (Management) list.get(position - 1);
                    intent.setClass(ListActivity.this, ManagementInfo.class);
                    bundle.putParcelable("management", m);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 43:
                    Yanhua yanhua = (Yanhua) list.get(position - 1);
                    intent.putExtra("type", xfaqType);
                    intent.setClass(ListActivity.this, YanhuaInfo.class);
                    bundle.putParcelable("yanhua", yanhua);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 44:
                case 45:
                    Roupidouzhi roupidouzhi = (Roupidouzhi) list.get(position - 1);
                    intent.setClass(ListActivity.this, RoupidouzhiInfo.class);
                    bundle.putParcelable("roupidouzhi", roupidouzhi);
                    intent.putExtra("type", xfaqType);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 6:
                case 8://持证残疾人
                case 9://享受重残生活补贴人员
                case 10://享受其他类生活补贴人员
                case 11://享受一级护理补贴人员
                case 12://享受二级护理补贴人员
                    intent.setClass(ListActivity.this, CjryInfo.class);
                    bundle.putParcelable("cjry", (Cjry) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 7:
                    intent.setClass(ListActivity.this, XqgsqInfoActivity.class);
                    bundle.putParcelable("plot", (Plot) list.get(position - 1));
                    intent.putExtra("isFrom", "index");
                    intent.putExtra("gridId", gridId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 13:
                    intent.setClass(ListActivity.this, LsrtInfo.class);
                    bundle.putParcelable("lsrt", (Lsrt) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 14:
                    intent.setClass(ListActivity.this, YfdxInfo.class);
                    bundle.putParcelable("yfdx", (Yfdx) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 15:
                case 33:
                case 34:
                case 35:
                case 36:
                    intent.setClass(ListActivity.this, TkgyInfo.class);
                    bundle.putParcelable("tkgy", (Tkgy) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 16:
                case 37:
                case 38:
                case 39:
                    intent.setClass(ListActivity.this, ZljInfo.class);
                    bundle.putParcelable("zlj", (Zlj) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 17://道路
                case 18://河塘
                case 19://垃圾清运员
                case 20://流动保洁员
                case 76:
                case 77:
                    intent.putExtra("type", type);
                    intent.setClass(ListActivity.this, CityInfoInfo.class);
                    bundle.putParcelable("cityInfo", (CityInfo) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                    intent.putExtra("type", type);
                    intent.setClass(ListActivity.this, ZfzzInfoInfo.class);
                    bundle.putParcelable("zfzzInfo", (ZfzzInfo) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 26:
                    intent.setClass(ListActivity.this, DsznInfo.class);
                    bundle.putParcelable("dszn", (Dszn) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 27:
                case 28:
                case 29:
                case 30:
                case 55:
                    isRefresh = true;
                    intent.setClass(ListActivity.this, JlfzInfo.class);
                    bundle.putParcelable("jlfz", (Jlfz) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 31:
                    intent.setClass(ListActivity.this, LdrkInfo.class);
                    bundle.putParcelable("ldrk", (Ldrk) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 32:
                    intent.setClass(ListActivity.this, JsknInfo.class);
                    bundle.putParcelable("jskn", (Jskn) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 46:
                    intent.setClass(ListActivity.this, JzgdInfo.class);
                    bundle.putParcelable("jzgd", (Jzgd) list.get(position - 1));
                    intent.putExtra("type", xfaqType);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 47:
                case 48:
                    intent.setClass(ListActivity.this, TezhongweixianInfo.class);
                    bundle.putParcelable("tezhongweixian", (Tezhongweixian) list.get(position - 1));
                    intent.putExtra("type", xfaqType);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 49:
                    intent.setClass(ListActivity.this, ChengpinInfo.class);
                    bundle.putParcelable("chengpin", (Chengpin) list.get(position - 1));
                    intent.putExtra("type", xfaqType);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                    intent.putExtra("type", type);
                    intent.setClass(ListActivity.this, ZfzzInfoVillageInfo.class);
                    bundle.putParcelable("zfzzInfoVillage", (ZfzzInfoVillage) list.get(position -
                            1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 56:
                    intent.setClass(ListActivity.this, ZdryInfo.class);
                    bundle.putParcelable("zdry", (Zdry) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 57:
                    intent.setClass(ListActivity.this, ZszhjsbhzInfo.class);
                    bundle.putParcelable("zszhjsbhz", (Zszhjsbhz) list.get(position - 1));
                    intent.putExtras(bundle);
                    intent.putExtra("isFrom", isFrom);
                    startActivity(intent);
                    break;
                case 58:
                    intent.setClass(ListActivity.this, ZdqsnInfo.class);
                    bundle.putParcelable("zdqsn", (Zdqsn) list.get(position - 1));
                    intent.putExtras(bundle);
                    intent.putExtra("isFrom", isFrom);
                    startActivity(intent);
                    break;
                case 59:
                    intent.setClass(ListActivity.this, SqjdryInfo.class);
                    bundle.putParcelable("sqjdry", (Sqjdry) list.get(position - 1));
                    intent.putExtras(bundle);
                    intent.putExtra("isFrom", isFrom);
                    startActivity(intent);
                    break;
                case 60:
                    intent.setClass(ListActivity.this, XjryInfo.class);
                    bundle.putParcelable("xjry", (Xjry) list.get(position - 1));
                    intent.putExtras(bundle);
                    intent.putExtra("isFrom", isFrom);
                    startActivity(intent);
                    break;
                case 61:
                    intent.setClass(ListActivity.this, SqfxryInfo.class);
                    bundle.putParcelable("sqfxry", (Sqfxry) list.get(position - 1));
                    intent.putExtras(bundle);
                    intent.putExtra("isFrom", isFrom);
                    startActivity(intent);
                    break;
                case 62:
                    intent.setClass(ListActivity.this, AzbjryInfo.class);
                    bundle.putParcelable("azbjry", (Azbjry) list.get(position - 1));
                    intent.putExtras(bundle);
                    intent.putExtra("isFrom", isFrom);
                    startActivity(intent);
                    break;
                case 63:
                    intent.setClass(ListActivity.this, YjdwInfo.class);
                    bundle.putParcelable("yjdw", (Yjdw) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 64:
                    intent.setClass(ListActivity.this, HospitalInfo.class);
                    bundle.putParcelable("hospital", (Hospital) list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 65:
                    break;
                case 66:
                    break;
                case 67:
                    break;
                case 68:
                    break;
                case 71:
                    ToastUtil.show(ListActivity.this, "71");
                    break;
                case 72:
                    ToastUtil.show(ListActivity.this, "72");
                    break;
                case 73:
                    break;
                case 74:
                    break;
                case 75:
                    break;
            }
        }
    }

    @Override
    public void onUptClick(View widget, int position) {
        isRefresh = true;
        Intent intent = new Intent();
        intent.putExtra("gridId", gridId);
        Bundle bundle = new Bundle();
        switch (type) {
            case -1:
//                loadTsdjhdoData(AutoListView.REFRESH);
                break;
            case 0:
                PartyMember pm = (PartyMember) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("PartyMember", pm);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, PartyActivity.class);
                startActivity(intent);
                break;
            case 1:
            case 2:
                Dbry dbry = (Dbry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("dbry", dbry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, DbryActivity.class);
                startActivity(intent);
                break;
            case 3:
                Ylfn ylfn = (Ylfn) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("ylfn", ylfn);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, YlfnInfoActivity.class);
                startActivity(intent);
                break;
            case 4:
            case 40:
            case 41:
            case 42:
            case 100:
                Company company = (Company) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("company", company);
                intent.putExtras(bundle);
                if (getIntent().getStringExtra("isYjfw") != null) {
                    intent.putExtra("isYjfw", getIntent().getStringExtra("isYjfw"));
                }
                intent.setClass(ListActivity.this, CompanyActivity.class);
                startActivity(intent);
                break;
            case 5:
                Management management = (Management) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("management", management);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ManagementActivity.class);
                startActivity(intent);
                break;
            case 43:
                Yanhua yanhua = (Yanhua) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("yanhua", yanhua);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, YanhuaActivity.class);
                startActivity(intent);
                break;
            case 44:
            case 45:
                Roupidouzhi roupidouzhi = (Roupidouzhi) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("roupidouzhi", roupidouzhi);
                intent.putExtras(bundle);
                intent.putExtra("type", type);
                intent.setClass(ListActivity.this, RoupidouzhiActivity.class);
                startActivity(intent);
                break;
            case 6:
            case 8://持证残疾人
            case 9://享受重残生活补贴人员
            case 10://享受其他类生活补贴人员
            case 11://享受一级护理补贴人员
            case 12://享受二级护理补贴人员
                Cjry cjry = (Cjry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("cjry", cjry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, CjryActivity.class);
                startActivity(intent);
                break;
            case 7:
                Plot plot = (Plot) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("plot", plot);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, PlotActivity.class);
                startActivity(intent);
                break;
            case 13:
                Lsrt lsrt = (Lsrt) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("lsrt", lsrt);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsrtActivity.class);
                startActivity(intent);
                break;
            case 14:
                Yfdx yfdx = (Yfdx) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("yfdx", yfdx);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, YfdxActivity.class);
                startActivity(intent);
                break;
            case 15:
            case 33:
            case 34:
            case 35:
            case 36:
                Tkgy tkgy = (Tkgy) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("tkgy", tkgy);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, TkgyActivity.class);
                startActivity(intent);
                break;
            case 16:
            case 37:
            case 38:
            case 39:
                Zlj zlj = (Zlj) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("zlj", zlj);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ZljActivity.class);
                startActivity(intent);
                break;
            case 17://道路
            case 18://河塘
            case 19://垃圾清运员
            case 20://流动保洁员
            case 76:
            case 77:
                CityInfo cityInfo = (CityInfo) list.get(position);
                intent.putExtra("isAdd", false);
                intent.putExtra("type", type);
                bundle.putParcelable("cityInfo", cityInfo);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, CityInfoActivity.class);
                startActivity(intent);
                break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                ZfzzInfo zfzzInfo = (ZfzzInfo) list.get(position);
                intent.putExtra("isAdd", false);
                intent.putExtra("type", type);
                bundle.putParcelable("zfzzInfo", zfzzInfo);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ZfzzInfoActivity.class);
                startActivity(intent);
                break;
            case 26:
                Dszn dszn = (Dszn) list.get(position);
                intent.putExtra("isAdd", false);
//                intent.putExtra("type", type);
                bundle.putParcelable("dszn", dszn);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, DsznInfoActivity.class);
                startActivity(intent);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 55:
                Jlfz jlfz = (Jlfz) list.get(position);
                intent.putExtra("isAdd", false);
//                intent.putExtra("type", type);
                bundle.putParcelable("jlfz", jlfz);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, JlfzActivity.class);
                startActivity(intent);
                break;
            case 31:
                Ldrk ldrk = (Ldrk) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("ldrk", ldrk);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LdrkActivity.class);
                startActivity(intent);
                break;
            case 32:
                Jskn jskn = (Jskn) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("jskn", jskn);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, JsknActivity.class);
                startActivity(intent);
                break;
            case 46:
                Jzgd jzgd = (Jzgd) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("jzgd", jzgd);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, JzgdActivity.class);
                startActivity(intent);
                break;
            case 47:
            case 48:
                Tezhongweixian tezhongweixian = (Tezhongweixian) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("tezhongweixian", tezhongweixian);
                intent.putExtras(bundle);
                intent.putExtra("type", type);
                intent.setClass(ListActivity.this, TezhongweixianActivity.class);
                startActivity(intent);
                break;
            case 49:
                Chengpin chengpin = (Chengpin) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("chengpin", chengpin);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ChengpinActivity.class);
                startActivity(intent);
                break;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                ZfzzInfoVillage zfzzInfoVillage = (ZfzzInfoVillage) list.get(position);
                intent.putExtra("isAdd", false);
                intent.putExtra("type", type);
                bundle.putParcelable("zfzzInfoVillage", zfzzInfoVillage);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ZfzzInfoVillageActivity.class);
                startActivity(intent);
                break;
            case 56:
                Zdry zdry = (Zdry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("zdry", zdry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ZdryActivity.class);
                startActivity(intent);
                break;
            case 57:
                Zszhjsbhz zszhjsbhz = (Zszhjsbhz) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("zszhjsbhz", zszhjsbhz);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ZszhjsbhzActivity.class);
                startActivity(intent);
                break;
            case 58:
                Zdqsn zdqsn = (Zdqsn) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("zdqsn", zdqsn);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, ZdqsnActivity.class);
                startActivity(intent);
                break;
            case 59:
                Sqjdry sqjdry = (Sqjdry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("sqjdry", sqjdry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, SqjdryActivity.class);
                startActivity(intent);
                break;
            case 60:
                Xjry xjry = (Xjry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("xjry", xjry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, XjryActivity.class);
                startActivity(intent);
                break;
            case 61:
                Sqfxry sqfxry = (Sqfxry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("sqfxry", sqfxry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, SqfxryActivity.class);
                startActivity(intent);
                break;
            case 62:
                Azbjry azbjry = (Azbjry) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("azbjry", azbjry);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, AzbjryActivity.class);
                startActivity(intent);
                break;
            case 63:
                Yjdw yjdw = (Yjdw) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("yjdw", yjdw);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, YjdwActivity.class);
                startActivity(intent);
                break;
            case 64:
                Hospital hospital = (Hospital) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putParcelable("hospital", hospital);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, HospitalActivity.class);
                startActivity(intent);
                break;
            case 65:
                Work work = (Work) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putSerializable("work", work);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect01Activity.class);
                startActivity(intent);
                break;
            case 66:
                Shbx shbx = (Shbx) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putSerializable("shbx", shbx);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect02Activity.class);
                startActivity(intent);
                break;
            case 67:
                Baozhang baozhang = (Baozhang) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putSerializable("baozhang", baozhang);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect03Activity.class);
                startActivity(intent);
                break;
            case 68:
                Zhongcai zhongcai = (Zhongcai) list.get(position);
                intent.putExtra("isAdd", false);
                bundle.putSerializable("zhongcai", zhongcai);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect04Activity.class);
                startActivity(intent);
                break;
            case 71:
                ToastUtil.show(ListActivity.this, "71");
                break;
            case 72:
                ToastUtil.show(ListActivity.this, "72");
                break;
            case 73:
                ShbxJbr shbxJbr = (ShbxJbr) list.get(position);
                intent.putExtra("isAdd", false);
                intent.putExtra("pid", pid);
                bundle.putSerializable("shbxJbr", shbxJbr);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect02JbrActivity.class);
                startActivity(intent);
                break;
            case 74:
                BaozhangJbr baozhangJbr = (BaozhangJbr) list.get(position);
                intent.putExtra("isAdd", false);
                intent.putExtra("pid", pid);
                bundle.putSerializable("baozhangJbr", baozhangJbr);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect03JbrActivity.class);
                startActivity(intent);
                break;
            case 75:
                ZhongcaiJbr zhongcaiJbr = (ZhongcaiJbr) list.get(position);
                intent.putExtra("isAdd", false);
                intent.putExtra("pid", pid);
                bundle.putSerializable("zhongcaiJbr", zhongcaiJbr);
                intent.putExtras(bundle);
                intent.setClass(ListActivity.this, LsCollect04JbrActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onDelClick(View widget, final int position) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog = new Dialog(ListActivity.this);
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
                switch (type) {
                    case -1:
                        Tshd tshd = (Tshd) list.get(position);
                        url.append("/tshd/delTshd");
                        delData(url.toString(), String.valueOf(tshd.getId()));
                        break;
                    case 0:
                        PartyMember pm = (PartyMember) list.get(position);
                        url.append("/party/delPartyMember");
                        delData(url.toString(), pm.getId());
                        break;
                    case 1:
                    case 2:
                        Dbry dbry = (Dbry) list.get(position);
                        url.append("/dbry/delDbry");
                        delData(url.toString(), dbry.getId());
                        break;
                    case 3:
                        Ylfn ylfn = (Ylfn) list.get(position);
                        url.append("/ylfn/delYlfn");
                        delData(url.toString(), ylfn.getId());
                        break;
                    case 4:
                    case 40:
                    case 41:
                    case 42:
                    case 100:
                        Company company = (Company) list.get(position);
                        url.append("/company/delCompany");
                        delData(url.toString(), String.valueOf(company.getId()));
                        break;
                    case 5:
                        Management management = (Management) list.get(position);
                        url.append("/management/delManagement");
                        delData(url.toString(), management.getId());
                        break;
                    case 43:
                        Yanhua yanhua = (Yanhua) list.get(position);
                        url.append("/yh/delData");
                        delData(url.toString(), String.valueOf(yanhua.getId()));
                        break;
                    case 44:
                    case 45:
                        Roupidouzhi roupidouzhi = (Roupidouzhi) list.get(position);
                        url.append("/rpdz/delData");
                        delData(url.toString(), String.valueOf(roupidouzhi.getId()));
                        break;
                    case 6:
                    case 8://持证残疾人
                    case 9://享受重残生活补贴人员
                    case 10://享受其他类生活补贴人员
                    case 11://享受一级护理补贴人员
                    case 12://享受二级护理补贴人员
                        Cjry cjry = (Cjry) list.get(position);
                        url.append("/cjry/delCjry");
                        delData(url.toString(), cjry.getId());
                        break;
                    case 7:
                        Plot plot = (Plot) list.get(position);
                        url.append("/plot/delPlot");
                        delData(url.toString(), plot.getId());
                        break;
                    case 13:
                        Lsrt lsrt = (Lsrt) list.get(position);
                        url.append("/lset/delLset");
                        delData(url.toString(), String.valueOf(lsrt.getId()));
                        break;
                    case 14:
                        Yfdx yfdx = (Yfdx) list.get(position);
                        url.append("/yfdx/delYfdx");
                        delData(url.toString(), String.valueOf(yfdx.getId()));
                        break;
                    case 15:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                        Tkgy tkgy = (Tkgy) list.get(position);
                        url.append("/tkgy/delTkgy");
                        delData(url.toString(), String.valueOf(tkgy.getId()));
                        break;
                    case 16:
                    case 37:
                    case 38:
                    case 39:
                        Zlj zlj = (Zlj) list.get(position);
                        url.append("/zlj/delZlj");
                        delData(url.toString(), String.valueOf(zlj.getId()));
                        break;
                    case 17://道路
                    case 18://河塘
                    case 19://垃圾清运员
                    case 20://流动保洁员
                    case 76:
                    case 77:
                        CityInfo cityInfo = (CityInfo) list.get(position);
                        url.append("/cityinfo/delCityinfo");
                        delData(url.toString(), String.valueOf(cityInfo.getId()));
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                        ZfzzInfo zfzzInfo = (ZfzzInfo) list.get(position);
                        url.append("/zfzzinfo/delZfzzInfo");
                        delData(url.toString(), String.valueOf(zfzzInfo.getId()));
                        break;
                    case 26:
                        Dszn dszn = (Dszn) list.get(position);
                        url.append("/dszn/delDszn");
                        delData(url.toString(), String.valueOf(dszn.getId()));
                        break;
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 55:
                        Jlfz jlfz = (Jlfz) list.get(position);
                        url.append("/jlfz/delJlfz");
                        delData(url.toString(), String.valueOf(jlfz.getId()));
                        break;
                    case 31:
                        Ldrk ldrk = (Ldrk) list.get(position);
                        url.append("/ldrk/delLdrk");
                        delData(url.toString(), String.valueOf(ldrk.getId()));
                        break;
                    case 32:
                        Jskn jskn = (Jskn) list.get(position);
                        url.append("/jskn/delJskn");
                        delData(url.toString(), String.valueOf(jskn.getId()));
                        break;
                    case 46:
                        Jzgd jzgd = (Jzgd) list.get(position);
                        url.append("/jzgd/delJzgd");
                        delData(url.toString(), String.valueOf(jzgd.getId()));
                        break;
                    case 47:
                    case 48:
                        Tezhongweixian tezhongweixian = (Tezhongweixian) list.get(position);
                        url.append("/tzwx/delData");
                        delData(url.toString(), String.valueOf(tezhongweixian.getId()));
                        break;
                    case 49:
                        Chengpin chengpin = (Chengpin) list.get(position);
                        url.append("/cpy/delData");
                        delData(url.toString(), String.valueOf(chengpin.getId()));
                        break;
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                        ZfzzInfoVillage zfzzInfoVillage = (ZfzzInfoVillage) list.get(position);
                        url.append("/zfzzinfovillage/delZfzzInfoVillage");
                        delData(url.toString(), String.valueOf(zfzzInfoVillage.getId()));
                        break;
                    case 56:
                        Zdry zdry = (Zdry) list.get(position);
                        url.append("/zdry/delZdry");
                        delData(url.toString(), String.valueOf(zdry.getId()));
                        break;
                    case 57:
                        Zszhjsbhz zszhjsbhz = (Zszhjsbhz) list.get(position);
                        url.append("/zszhjsbhz/delZszhjsbhz");
                        delData(url.toString(), String.valueOf(zszhjsbhz.getId()));
                        break;
                    case 58:
                        Zdqsn zdqsn = (Zdqsn) list.get(position);
                        url.append("/zdqsn/delZdqsn");
                        delData(url.toString(), String.valueOf(zdqsn.getId()));
                        break;
                    case 59:
                        Sqjdry sqjdry = (Sqjdry) list.get(position);
                        url.append("/sqjdry/delSqjdry");
                        delData(url.toString(), String.valueOf(sqjdry.getId()));
                        break;
                    case 60:
                        Xjry xjry = (Xjry) list.get(position);
                        url.append("/xjry/delXjry");
                        delData(url.toString(), String.valueOf(xjry.getId()));
                        break;
                    case 61:
                        Sqfxry sqfxry = (Sqfxry) list.get(position);
                        url.append("/sqfxry/delSqfxry");
                        delData(url.toString(), String.valueOf(sqfxry.getId()));
                        break;
                    case 62:
                        Azbjry azbjry = (Azbjry) list.get(position);
                        url.append("/azbjry/delAzbjry");
                        delData(url.toString(), String.valueOf(azbjry.getId()));
                        break;
                    case 63:
                        Yjdw yjdw = (Yjdw) list.get(position);
                        url.append("/yjdw/deleteYjdw");
                        delData(url.toString(), String.valueOf(yjdw.getId()));
                        break;
                    case 64:
                        Hospital hospital = (Hospital) list.get(position);
                        url.append("/hospital/deleteHospital");
                        delData(url.toString(), String.valueOf(hospital.getId()));
                        break;
                    case 65:
                        Work work = (Work) list.get(position);
                        url.append("/work1/delete");
                        delData(url.toString(), String.valueOf(work.getId()));
                        break;
                    case 66:
                        Shbx shbx = (Shbx) list.get(position);
                        url.append("/shbx/delete");
                        delData(url.toString(), String.valueOf(shbx.getId()));
                        break;
                    case 67:
                        Baozhang bz = (Baozhang) list.get(position);
                        url.append("/bzfw/delete");
                        delData(url.toString(), String.valueOf(bz.getId()));
                        break;
                    case 68:
                        Zhongcai zc = (Zhongcai) list.get(position);
                        url.append("/zyzc/delete");
                        delData(url.toString(), String.valueOf(zc.getId()));
                        break;
                    case 71:
                        ToastUtil.show(ListActivity.this, "71");
                        break;
                    case 72:
                        ToastUtil.show(ListActivity.this, "72");
                        break;
                    case 73:
                        ShbxJbr shbxJbr = (ShbxJbr) list.get(position);
                        url.append("/shbx/delete");
                        delData(url.toString(), String.valueOf(shbxJbr.getId()));
                        break;
                    case 74:
                        BaozhangJbr baozhangJbr = (BaozhangJbr) list.get(position);
                        url.append("/bzfw/delete");
                        delData(url.toString(), String.valueOf(baozhangJbr.getId()));
                        break;
                    case 75:
                        ZhongcaiJbr zhongcaiJbr = (ZhongcaiJbr) list.get(position);
                        url.append("/zyzc/delete");
                        delData(url.toString(), String.valueOf(zhongcaiJbr.getId()));
                        break;
                }
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
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据删除中···", SuccinctProgress
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
                        ToastUtil.show(ListActivity.this, R.string.del_success);
                        onRefresh();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, R.string.del_fail);
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
        Plot plot = (Plot) list.get(position);
        postDos(plot);
    }

    @Override
    public void onTwoClick(View widget, int position) {

    }

    private void postDos(final Plot plot) {
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
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
                        intent.setClass(ListActivity.this, PlotLtActivity.class);
                        startActivity(intent);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        //申请加入小区论坛
                        new AlertDialog.Builder(ListActivity.this).setTitle("确认加入" + plot.getName() + "的论坛吗？").setPositiveButton("取消", new DialogInterface
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
                        ToastUtil.show(ListActivity.this, "您的请求还在审核中");
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
        SuccinctProgress.showSuccinctProgress(ListActivity.this, "数据请求中···", SuccinctProgress
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
                        DialogUtil.showTipsDialog(ListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, "请求失败");
                    } else if ("4".equals(resultCode)) {
                        LogUtil.i("此处不应该有这个返回值");
                    } else if ("5".equals(resultCode)) {
                        ToastUtil.show(ListActivity.this, "申请发送成功，请等待审核...");
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

package com.xzz.hxjdglpt.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.AlreadythingActivity;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.CxglActivity;
import com.xzz.hxjdglpt.activity.DataCollectionActivity;
import com.xzz.hxjdglpt.activity.DjZzjgActivity;
import com.xzz.hxjdglpt.activity.FlfwActivity;
import com.xzz.hxjdglpt.activity.FwmdmActivity;
import com.xzz.hxjdglpt.activity.GgActivity;
import com.xzz.hxjdglpt.activity.GzdtActivity;
import com.xzz.hxjdglpt.activity.HrbActivity;
import com.xzz.hxjdglpt.activity.HxfcActivity;
import com.xzz.hxjdglpt.activity.LsIndexActivity;
import com.xzz.hxjdglpt.activity.MqztcActivity;
import com.xzz.hxjdglpt.activity.NewsActivity;
import com.xzz.hxjdglpt.activity.NoticeActivity;
import com.xzz.hxjdglpt.activity.NoticeDetailActivity;
import com.xzz.hxjdglpt.activity.PartyBuildingActivity;
import com.xzz.hxjdglpt.activity.QfhxActivity;
import com.xzz.hxjdglpt.activity.QlyListActicity;
import com.xzz.hxjdglpt.activity.QlyLoginActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.activity.RdzjActivity;
import com.xzz.hxjdglpt.activity.ScheduleActivity;
import com.xzz.hxjdglpt.activity.SignActivity;
import com.xzz.hxjdglpt.activity.SxgzListActivity;
import com.xzz.hxjdglpt.activity.VillageMapActivity;
import com.xzz.hxjdglpt.activity.WmcjActivity;
import com.xzz.hxjdglpt.activity.WorkBackListActivity;
import com.xzz.hxjdglpt.activity.WorkSendListActivity;
import com.xzz.hxjdglpt.activity.YxdyActivity;
import com.xzz.hxjdglpt.activity.ZhfwListActivity;
import com.xzz.hxjdglpt.activity.ui.activity.MainMIActivity;
import com.xzz.hxjdglpt.customview.AutoVerticalScrollTextView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Menu;
import com.xzz.hxjdglpt.model.Notice;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.qly.ResultInfo;
import com.xzz.hxjdglpt.model.qly.UserInfo;
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

import android.content.Intent;
import android.widget.LinearLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dbz on 2017/5/10.
 */

@ContentView(R.layout.fragment_index)
public class IndexFragment extends Fragment {

    @ViewInject(R.id.index_title)
    private TextView tvTitle;

    @ViewInject(R.id.ad_fragment)
    private ADFragment adFragment;

    @ViewInject(R.id.index_search)
    private ImageButton ibSearch;

    //    @ViewInject(R.id.information_tip)
//    private TextView tvTip;
    @ViewInject(R.id.information_tip)
    private AutoVerticalScrollTextView tvTip;

    @ViewInject(R.id.index_dbsx_count)
    private TextView tvCount;

    @ViewInject(R.id.index_dbsx)
    private LinearLayout ivDbsx;//待办事项
    @ViewInject(R.id.index_ybsx)
    private LinearLayout ivybsx;//已办事项
    @ViewInject(R.id.index_rwfp)
    private LinearLayout ivRwfp;//任务分派
    @ViewInject(R.id.index_gzfk)
    private LinearLayout ivGzfk;//工作反馈
    @ViewInject(R.id.index_zxtz)
    private LinearLayout ivZxtz;//最新通知
    @ViewInject(R.id.index_sqxw)
    private LinearLayout ivSqxw;//社区新闻
    @ViewInject(R.id.index_ckdt)
    private LinearLayout ivCkdt;//查看地图
    @ViewInject(R.id.index_xxcj)
    private LinearLayout ivXxcj;//信息采集
    @ViewInject(R.id.index_tjfx)
    private LinearLayout ivTjfx;//统计分析
    @ViewInject(R.id.index_djxx)
    private LinearLayout ivDjxx;//党建学习
    @ViewInject(R.id.index_fwmdm)
    private LinearLayout ivFwmdm;//服务面对面
    @ViewInject(R.id.index_mqztc)
    private LinearLayout ivMqztc;//民情直通车
    @ViewInject(R.id.index_mi)
    private LinearLayout ivMI;//即时聊天
    @ViewInject(R.id.index_qly)
    private LinearLayout ivQly;//千里眼
    @ViewInject(R.id.index_qd)
    private LinearLayout ivQd;//签到

    @ViewInject(R.id.index_aqsc)
    private LinearLayout ivAqsc;//应急服务
    @ViewInject(R.id.index_dj)
    private LinearLayout ivDj;//党建
    @ViewInject(R.id.index_fwzw)
    private LinearLayout ivFwzw;//防伪治违
    @ViewInject(R.id.index_xshs)
    private LinearLayout ivXshs;//协税护税
    @ViewInject(R.id.index_qygz)
    private LinearLayout ivQygz;//企业工作
    @ViewInject(R.id.index_mz)
    private LinearLayout ivMz;//民政
    @ViewInject(R.id.index_jhsy)
    private LinearLayout ivJhsy;//卫生健康
    @ViewInject(R.id.index_sfxz)
    private LinearLayout ivSfxz;//司法行政
    @ViewInject(R.id.index_cxgl)
    private LinearLayout ivCxgl;//长效管理
    @ViewInject(R.id.index_xfwd)
    private LinearLayout ivXfwd;//和谐社区
    @ViewInject(R.id.index_xqxx)
    private LinearLayout ivXqxx;//小区信息
    @ViewInject(R.id.index_qtgz)
    private LinearLayout ivQtgz;//其他工作


    private User user;
    private List<Notice> list = new ArrayList<Notice>();
    private BaseApplication application;

    private int number = 0;
    //    private boolean isRunning = true;
    private Handler mHandler = new Handler();
    private List<Role> roles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        tvTitle.setText(getString(R.string.cname));
        application = (BaseApplication) getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        initData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(task, 5000);
        loadCount();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(task);
    }

    public void initData() {
        loadData();
        initPower();
    }

    private List<String> ms = new ArrayList<>();

    private void initPower() {
        List<Menu> menus = application.getMenus();
        for (Menu m : menus) {
            ms.add(m.getMenuId());
        }
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            tvTip.next();
            number++;
            if (list.size() != 0) {
                tvTip.setText(list.get(number % list.size()).getTitle());
            }
            mHandler.postDelayed(task, 3000);
        }
    };


    @Event(value = {R.id.index_qd, R.id.index_qly, R.id.index_mi, R.id.index_aqsc, R.id.index_dj,
            R.id.index_fwzw, R.id.index_xshs, R.id.index_qygz, R.id.index_mz, R.id.index_jhsy, R
            .id.index_sfxz, R.id.index_cxgl, R.id.index_xfwd, R.id.information_tip, R.id
            .index_search, R.id.index_dbsx, R.id.index_ybsx, R.id.index_rwfp, R.id.index_gzfk, R
            .id.index_zxtz, R.id.index_sqxw, R.id.index_ckdt, R.id.index_xxcj, R.id.index_tjfx, R
            .id.index_djxx, R.id.index_fwmdm, R.id.index_mqztc, R.id.index_xqxx, R.id.index_rdzj,
            R.id.index_qtgz, R.id.index_gggs, R.id.index_dzz, R.id.index_dy, R.id.index_dfjn, R.id.index_yxdy, R.id.index_lrdy, R.id.index_hrb, R.id.index_ygcw, R.id.index_qfhx}, type = View.OnClickListener.class)
    private void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.index_hrb:
                intent.setClass(getActivity(), HrbActivity.class);
                startActivity(intent);
                break;
            case R.id.index_ygcw:
                //法律服务
                intent.setClass(getActivity(), FlfwActivity.class);
                startActivity(intent);
                //ToastUtil.show(getActivity(), "正在对接中...");
//                Intent intent = new Intent();
//                intent.setData(Uri.parse("http://222.184.125.10:7001/eyangguang"));
//               // intent.putExtra("", "");//这里Intent当然也可传递参数,但是一般情况下都会放到上面的URL中进行传递
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                String package_name = "com.sunsoft.sunvillage";
//                PackageManager packageManager = getActivity().getPackageManager();
//                Intent it = packageManager.getLaunchIntentForPackage(package_name);
//                if (it != null) {
//                    startActivity(it);
//                } else {
//                    //没有默认的入口 Activity
//                    ToastUtil.show(getActivity(), "未发现阳光村务应用，请下载");
//                }
                break;
            case R.id.index_gggs:
                intent.setClass(getActivity(), GgActivity.class);
                intent.putExtra("type","公告公示");
                startActivity(intent);
                break;
            case R.id.index_dzz:
//                intent.setClass(getActivity(), DjZzjgActivity.class);
//                startActivity(intent);
                intent.setClass(getActivity(), SxgzListActivity.class);

                intent.putExtra("type", 8);
                startActivity(intent);
                break;
            case R.id.index_dy:
                intent.setClass(getActivity(), ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
//                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.index_dfjn:
//                ToastUtil.show(getActivity(), "正在对接中...");
//                String package_name1 = "cn.jsb.china";
//                PackageManager packageManager1 = getActivity().getPackageManager();
//                Intent it1 = packageManager1.getLaunchIntentForPackage(package_name1);
//                if (it1 != null) {
//                    startActivity(it1);
//                } else {
//                    //没有默认的入口 Activity
//                    ToastUtil.show(getActivity(), "未发现江苏银行应用，请下载");
//                }
                break;
            case R.id.index_yxdy:
                intent.setClass(getActivity(), WmcjActivity.class);
                startActivity(intent);
                break;
            case R.id.index_qfhx:
                //清风河下
                intent.setClass(getActivity(), QfhxActivity.class);
                startActivity(intent);
                break;
            case R.id.index_lrdy:
                intent.setClass(getActivity(), ZhfwListActivity.class);
                intent.putExtra("isFrom", "index");
                intent.putExtra("type", 0);
                intent.putExtra("dyldType", "2");
                startActivity(intent);
                break;
            case R.id.index_qtgz:
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), GzdtActivity.class);
                intent.putExtra("type", 11);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_qd:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(getActivity(), SignActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), "您不需要签到！");
                }
                break;
            case R.id.index_qly:
                UserInfo userInfo = getQlyUser();
                if (getAutoLogin() && userInfo != null) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", userInfo.getLoginName());
                    params.put("domain", userInfo.getLoginDomain());
                    params.put("pass", userInfo.getMyPassW());
                    qlyLogin(params);
                } else {
                    intent.setClass(getActivity(), QlyLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.index_mi:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(getActivity(), MainMIActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), "暂无此权限");
                }
                break;
            case R.id.index_search:
                //intent.setClassName(getActivity(),);
                break;
            case R.id.index_dbsx:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(getActivity(), ScheduleActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), "暂无此权限");
                }
                break;
            case R.id.index_ybsx:
                if (roles != null && roles.size() > 0) {
                    intent.setClass(getActivity(), AlreadythingActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), "暂无此权限");
                }
                break;
            case R.id.index_rwfp:
                if (ms.contains("100114") && roles != null && roles.size() > 0) {
                    intent.putExtra("isFrom", 0);//判断标识'0'来自首页，'1'来自查看地图
                    intent.setClass(getActivity(), WorkSendListActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), R.string.no_power);
                }
                break;
            case R.id.index_gzfk:
                if (ms.contains("100153") && roles != null && roles.size() > 0) {
                    intent.setClass(getActivity(), WorkBackListActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), R.string.no_power);
                }
                break;
            case R.id.index_zxtz:
                intent.setClass(getActivity(), NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.index_sqxw:
                intent.setClass(getActivity(), NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.index_ckdt:
//                if (ms.contains("100105")) {
                intent.setClass(getActivity(), VillageMapActivity.class);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_xxcj:
                if (ms.contains("100107") && roles != null && roles.size() > 0) {
                    intent.setClass(getActivity(), DataCollectionActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), R.string.no_power);
                }
                break;
            case R.id.index_tjfx:
                intent.setClass(getActivity(), HxfcActivity.class);
//                intent.setClass(getActivity(), BrowserWapActivity.class);
//                intent.putExtra("StartWidgetUUID", ConstantUtil.BASE_URL + "/hxfc_app/hxfc.html");
                startActivity(intent);
                break;
            case R.id.index_djxx:
//                if (ms.contains("100127")) {
                intent.setClass(getActivity(), PartyBuildingActivity.class);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_fwmdm:
                intent.setClass(getActivity(), FwmdmActivity.class);
                startActivity(intent);
                break;
            case R.id.index_mqztc:
                intent.setClass(getActivity(), MqztcActivity.class);
                startActivity(intent);
                break;
            case R.id.information_tip:
                intent.setClass(getActivity(), NoticeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("notice", list.get(number % list.size()));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.index_aqsc:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), AqscActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_dj:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), DjActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 8);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_fwzw:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), FwzfActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 10);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_xshs:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), XshsActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
//                intent.setClass(getActivity(), SxgzListActivity.class);
//                intent.putExtra("type", 7);
//                startActivity(intent);
//                } else {
//                ToastUtil.show(getActivity(), "劳动保障建设中...");
//                }
                intent.setClass(getActivity(), LsIndexActivity.class);
                startActivity(intent);
                break;
            case R.id.index_qygz:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), QygzActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 4);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_mz:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), MzActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 5);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_jhsy:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), JhsyActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_sfxz:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), SfxzActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 6);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_cxgl:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), CscxglActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), CxglActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_xfwd:
//                if (ms.contains("100107")) {
//                    intent.setClass(getActivity(), XfwdActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
                intent.setClass(getActivity(), SxgzListActivity.class);
                intent.putExtra("type", 9);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_xqxx:
//                if (ms.contains("100107")) {
//                    intent.setClass(this, PlotActivity.class);
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
//                if (roles != null && roles.size() > 0) {
//                intent.setClass(getActivity(), SxgzListActivity.class);
//                intent.putExtra("type", 12);
//                startActivity(intent);
//                } else {
//                    ToastUtil.show(getActivity(), R.string.no_power);
//                }
                break;
            case R.id.index_rdzj:
                intent.setClass(getActivity(), RdzjActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 新闻获取
     */
    private void loadData() {
        String url = ConstantUtil.BASE_URL + "/m_notice/queryNoticeList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        //取前3条最新通知
        params.put("pageIndex", "0");
        params.put("pageSize", "3");
        VolleyRequest.RequestPost(getActivity(), url, "index_notice", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            LogUtil.i(resultCode);
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Notice>>() {
                                }.getType());

                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(getActivity());
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(getActivity(), R.string.load_fail);
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

    private void loadCount() {
        String url = ConstantUtil.BASE_URL + "/m_task/getDbsxCount";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("status", "1");
        VolleyRequest.RequestPost(getActivity(), url, "task_queryList", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                int count = result.getInt("count");
                                tvCount.setText(String.valueOf(count));
                                if (count == 0) {
                                    tvCount.setVisibility(View.GONE);
                                } else {
                                    tvCount.setVisibility(View.VISIBLE);
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(getActivity());
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(getActivity(), R.string.load_fail);
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
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("index_notice");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 登录请求
     */
    private void qlyLogin(final HashMap<String, String> params) {
        SuccinctProgress.showSuccinctProgress(getActivity(), "登录中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/qly/qly_login";
        VolleyRequest.RequestPost(getActivity(), url, "qly_login", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
                        try {
                            Gson gson = new Gson();
                            ResultInfo resultInfo = (ResultInfo) gson.fromJson(result.getJSONObject
                                    ("resultInfo").toString(), ResultInfo.class);
                            if ("OK".equals(resultInfo.getValue())) {
                                UserInfo user = (UserInfo) gson.fromJson(result.getJSONObject("data")
                                        .toString(), UserInfo.class);
                                user.setMyPassW(params.get("pass"));
                                application.setUserInfo(user);
                                saveUser(user);
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), QlyListActicity.class);
                                startActivity(intent);
                            } else {
                                ToastUtil.show(getActivity(), R.string.account_pwd_inc);
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), QlyLoginActivity.class);
                                startActivity(intent);
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
                        ToastUtil.show(getActivity(), R.string.login_fail);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), QlyLoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void saveUser(UserInfo user) {
        SharedPreferences mySharedP = getActivity().getSharedPreferences("hxjd_base64_qly",
                Activity.MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            SharedPreferences.Editor editor = mySharedP.edit();
            String userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("qly_userInfo", userBase64);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserInfo getQlyUser() {
        UserInfo user = null;
        try {
            SharedPreferences mySharedP = getActivity().getSharedPreferences("hxjd_base64_qly",
                    Activity.MODE_PRIVATE);
            String base64User = mySharedP.getString("qly_userInfo", "");
            byte[] userBytes = Base64.decode(base64User.getBytes(), Base64.DEFAULT);
            if (userBytes.length > 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(userBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                user = (UserInfo) ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    private boolean getAutoLogin() {
        SharedPreferences mySharedP = getActivity().getSharedPreferences("hxjd_base64_qly",
                Activity.MODE_PRIVATE);
        return mySharedP.getBoolean("autoLogin", false);
    }
}

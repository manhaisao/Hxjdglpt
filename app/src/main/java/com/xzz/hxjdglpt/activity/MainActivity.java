package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.activity.ui.activity.MainMIActivity;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.db.Groups;
import com.xzz.hxjdglpt.fragment.ConnectFragment;
import com.xzz.hxjdglpt.fragment.IndexFragment;
import com.xzz.hxjdglpt.fragment.MyInfoFragment;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.TokenResult;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.server.utils.NLog;
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
import org.xutils.x;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

@ContentView(R.layout.aty_main)
public class MainActivity extends BaseActivity implements OnItemSelectedListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private IndexFragment indexFragment;
    private MyInfoFragment myInfoFragment;

    private ConnectFragment connectFragment;

    private List<Role> roles;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        roles = application.getRolesList();
        if (user == null) {
            user = application.getUser();
        }
        setDisplayParams();
//        setTag();
//        setStyleCustom();
//        setStyleBasic();
        loadUsersData();
        if (roles != null && roles.size() > 0) {
            if (!TextUtils.isEmpty(user.getMiToken())) {
                gotoIndex(user.getMiToken());
            } else {
                getToken();
            }
        }
    }

    private void getToken() {
        String url = ConstantUtil.BASE_URL + "/m_mi/getMIToken";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("name", user.getRealName());
        params.put("portraitUri", ConstantUtil.FILE_DOWNLOAD_URL + user.getPicture());
//        SuccinctProgress.showSuccinctProgress(MainActivity.this, "聊天服务器连接中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "getMIToken", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("result=" + result.toString());
//                SuccinctProgress.dismiss();
                try {
                    Gson gson = new Gson();
                    TokenResult tokenResult = (TokenResult) gson.fromJson(result.toString(),
                            TokenResult.class);
                    Integer resultCode = tokenResult.getCode();
                    if (resultCode == 1001) {
                        ToastUtil.show(MainActivity.this, "token保存失败");
                    } else if (resultCode == 200) {
                        gotoIndex(tokenResult.getToken());
                        ToastUtil.show(MainActivity.this, "聊天服务器连接成功");
                    } else {
                        ToastUtil.show(MainActivity.this, "获取token失败");
                    }
                } catch (com.alibaba.fastjson.JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
//                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    private void gotoIndex(String token) {
        NLog.i("dbz", "token=" + token);
        SealUserInfoManager.getInstance().openDB();
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                NLog.e("connect", "onTokenIncorrect");
                getToken();
            }

            @Override
            public void onSuccess(String s) {
                NLog.e("connect", "onSuccess userid:" + s);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId(), user
                        .getRealName(), Uri.parse(ConstantUtil.FILE_DOWNLOAD_URL + user
                        .getPicture())));
//                SealUserInfoManager.getInstance().getAllUserInfo();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                NLog.e("connect", "onError errorcode:" + errorCode.getValue());
            }
        });
    }

    /**
     * 设置极光推送tag
     */
//    private void setTag() {
//        Set<String> sets = new HashSet<>();
//        sets.add(application.getUser().getUserId());
//        JPushInterface.setTags(this, sets, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                LogUtil.i("set tag result is" + i);
//            }
//        });
//        JPushInterface.setAliasAndTags(getApplicationContext(), application.getUser().getUserId()
//                , null, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                LogUtil.i("set tag result is" + i);
//            }
//        });
//    }

    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
//    private void setStyleCustom() {
//        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(MainActivity
//                .this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
//        builder.layoutIconDrawable = R.mipmap.my_bj;
//        builder.developerArg0 = "developerArg2";
//        JPushInterface.setPushNotificationBuilder(1, builder);
//    }

    /**
     * 设置通知提示方式 - 基础属性
     */
//    private void setStyleBasic() {
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity
// .this);
//        builder.statusBarDrawable = R.mipmap.jpush_notification_icon;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
//        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification
//        // .DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
//        JPushInterface.setPushNotificationBuilder(1, builder);
//    }
    public void setDisplayParams() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        application.setScreenWidth(width);
        application.setScreenHeight(height);
        float density = metric.density;
        int densityDpi = metric.densityDpi;
        LogUtil.i("width=" + width + "  height=" + height + "  density=" + density + "  " +
                "densityDpi=" + densityDpi);
    }

    @Override
    public void onItemSelected(int tabId) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        switch (tabId) {
            case R.id.index:
                hideFragment();
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.main_content, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }
                transaction.commit();
                break;
            case R.id.connect_address:
                if (roles != null && roles.size() > 0) {
                    hideFragment();
                    if (connectFragment == null) {
                        connectFragment = new ConnectFragment();
                        transaction.add(R.id.main_content, connectFragment);
                    } else {
                        transaction.show(connectFragment);
                    }
                    transaction.commit();
                } else {
                    ToastUtil.show(this, "暂无此权限");
                }
                break;
            case R.id.my:
                hideFragment();
                if (myInfoFragment == null) {
                    myInfoFragment = new MyInfoFragment();
                    transaction.add(R.id.main_content, myInfoFragment);
                } else {
                    transaction.show(myInfoFragment);
                }
                transaction.commit();
                break;
        }
    }

    private void hideFragment() {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (connectFragment != null) {
            transaction.hide(connectFragment);
        }
        if (myInfoFragment != null) {
            transaction.hide(myInfoFragment);
        }
    }


    private long lastTime;
    private long currentTime;

    /**
     * 捕捉返回事件，双击退出app
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime <= 1000) {
                application.setUser(null);
                //finish();
                System.exit(0);
            } else {
                lastTime = currentTime;
                ToastUtil.show(this, R.string.double_exit);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
//    private void init() {
//        JPushInterface.init(getApplicationContext());
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        JPushInterface.stopCrashHandler(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
    }

    private void loadUsersData() {
        SuccinctProgress.showSuccinctProgress(this, "请求数据中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/user/queryUserByIsMI";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryUserByIsMI", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Friend> users = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Friend>>() {
                        }.getType());
                        if(users!=null&&users.size()>0)
                        {
                            SealUserInfoManager.getInstance().openDB();
                            SealUserInfoManager.getInstance().addFriends(users);
                            SealUserInfoManager.getInstance().closeDB();
                        }

                        loadGroupsData();
                    } else if ("2".equals(resultCode)) {
                        LogUtil.i("resultCode=" + resultCode);
                    } else if ("3".equals(resultCode)) {
                        LogUtil.i("resultCode=" + resultCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
//                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });

    }

    private void loadGroupsData() {
        String url = ConstantUtil.BASE_URL + "/m_group/queryGroupList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryGroupList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

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
                        List<Groups> users = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Groups>>() {
                        }.getType());
                        if(users!=null&&users.size()>0)
                        {
                            SealUserInfoManager.getInstance().openDB();
                            SealUserInfoManager.getInstance().addMyGroups(users);
                            SealUserInfoManager.getInstance().closeDB();
                        }
                    } else if ("2".equals(resultCode)) {
                        LogUtil.i("resultCode=" + resultCode);
                    } else if ("3".equals(resultCode)) {
                        LogUtil.i("resultCode=" + resultCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
//                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryUserByIsMI");
        BaseApplication.getRequestQueue().cancelAll("queryGroupList");
        BaseApplication.getRequestQueue().cancelAll("getMIToken");
    }
}

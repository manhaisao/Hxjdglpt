package com.xzz.hxjdglpt.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.xzz.hxjdglpt.SealAppContext;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.baidumap.LocationService;
import com.xzz.hxjdglpt.message.TestMessage;
import com.xzz.hxjdglpt.message.provider.ContactNotificationMessageProvider;
import com.xzz.hxjdglpt.message.provider.TestMessageProvider;
import com.xzz.hxjdglpt.model.Menu;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.qly.UserInfo;
import com.xzz.hxjdglpt.superfileview.AdvanceLoadX5Service;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.SharedPreferencesContext;
import com.xzz.hxjdglpt.volley.BitmapCache;

import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.push.RongPushClient;

/**
 * 自定义Application
 *
 * @author dbz
 */
public class BaseApplication extends MultiDexApplication {
    public static RequestQueue queues;
    private ImageLoader imageLoader;

    public LocationService locationService;
    public Vibrator mVibrator;
    private static io.rong.imageloader.core.DisplayImageOptions options;


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, R.color.tab_tv_color);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //Volley
        queues = Volley.newRequestQueue(getApplicationContext());
        initImageLoader();
        //百度地图,初始化定位sdk，建议在Application中创建
        locationService = new LocationService(getApplicationContext());


        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

//            LeakCanary.install(this);//内存泄露检测
//            RongPushClient.registerHWPush(this);
            RongPushClient.registerMiPush(this, "2882303761517473625", "5451747338625");
//            try {
//                RongPushClient.registerGCM(this);
//            } catch (RongException e) {
//                e.printStackTrace();
//            }

            /**
             * 注意：
             *
             * IMKit SDK调用第一步 初始化
             *
             * context上下文
             *
             * 只有两个进程需要初始化，主进程和 push 进程
             */
            RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
            RongIM.init(this);
//            NLog.setDebug(true);//Seal Module Log 开关
            SealAppContext.init(this);
            SharedPreferencesContext.init(this);
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

            try {
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                RongIM.registerMessageType(TestMessage.class);
                RongIM.registerMessageTemplate(new TestMessageProvider());


            } catch (Exception e) {
                e.printStackTrace();
            }

            openSealDBIfHasCachedToken();

            options = new io.rong.imageloader.core.DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.de_default_portrait).showImageOnFail(R.mipmap
                            .de_default_portrait).showImageOnLoading(R.mipmap
                            .de_default_portrait).displayer(new FadeInBitmapDisplayer(300))
                    .cacheInMemory(true).cacheOnDisk(true).build();

        }

        //腾讯x5解析文档
        preinitX5WebCore();
        //预加载x5内核
        Intent intent = new Intent(this, AdvanceLoadX5Service.class);
        startService(intent);

    }

    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(getApplicationContext(), null);// 设置X5初始化完成的回调接口
        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.default_icon).showImageOnFail(R.mipmap
                        .default_icon).cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder
                (getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs().build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    public static RequestQueue getRequestQueue() {
        return queues;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(BaseApplication.getRequestQueue(), BitmapCache
                    .getInstance());
        }
        return imageLoader;
    }

    /**
     * 屏幕的宽
     */
    private int screenWidth;

    /**
     * 屏幕的高
     */
    private int screenHeight;

    /**
     * 登录的用户信息
     */
    private User user;

    public User getUser() {
        if (user == null) {
            user = getUser(this);
        }
        return user;
    }

    private User getUser(Context context) {
        User user = null;
        try {
            SharedPreferences mySharedP = context.getSharedPreferences("hxjd_base64_user",
                    Activity.MODE_PRIVATE);
            String base64User = mySharedP.getString("userInfo", "");
            byte[] userBytes = Base64.decode(base64User.getBytes(), Base64.DEFAULT);
            if (userBytes.length > 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(userBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                user = (User) ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<Role> rolesList;

    public List<Role> getRolesList() {
        if (rolesList == null) {
            rolesList = new ArrayList<>();
        }
        return rolesList;
    }

    public void setRolesList(List<Role> rolesList) {
        this.rolesList = rolesList;
    }

    //用户具有的网格权限
    private List<String> gridIds;

    public List<String> getGridIds() {
        return gridIds;
    }

    public void setGridIds(List<String> gridIds) {
        this.gridIds = gridIds;
    }

    //菜单权限
    private List<Menu> menus;

    public List<Menu> getMenus() {
        if(menus==null){
            menus = new ArrayList<>();
        }
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public static io.rong.imageloader.core.DisplayImageOptions getOptions() {
        return options;
    }

    private void openSealDBIfHasCachedToken() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cachedToken = sp.getString("loginToken", "");
        if (!TextUtils.isEmpty(cachedToken)) {
            String current = getCurProcessName(this);
            String mainProcessName = getPackageName();
            if (mainProcessName.equals(current)) {
                SealUserInfoManager.getInstance().openDB();
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}

package com.xzz.hxjdglpt.volley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.utils.NetUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.json.JSONObject;

import java.util.Map;


public class VolleyRequest {
    public static Request<JSONObject> request;

    public static Context context;

    /*
     * 获取GET请求内容 参数： context：当前上下文； url：请求的url地址； tag：当前请求的标签；
     * volleyListenerInterface：VolleyListenerInterface接口；
     */
    public static void RequestGet(Context context, String url, String tag,
                                  VolleyListenerInterface volleyListenerInterface) {
        // 检测网络是否可用
        if (!NetUtil.checkNet(context)) {
            ToastUtil.show(context,
                    context.getResources().getString(R.string.net_unuser));
            return;
        }
        // 清除请求队列中的tag标记请求
        BaseApplication.getRequestQueue().cancelAll(tag);
        // 创建当前的请求，获取字符串内容
        request = new MyRequest(Request.Method.GET, url,
                volleyListenerInterface.responseListener(),
                volleyListenerInterface.errorListener());
        // 为当前请求添加标记
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        BaseApplication.getRequestQueue().add(request);
        // 重启当前请求队列
//        BaseApplication.getRequestQueue().start();
    }

    /*
     * 获取POST请求内容（请求的代码为Map） 参数： context：当前上下文； url：请求的url地址； tag：当前请求的标签；
     * params：POST请求内容； volleyListenerInterface：VolleyListenerInterface接口；
     */
    public static void RequestPost(Context context, String url, String tag,
                                   Map<String, String> params,
                                   VolleyListenerInterface volleyListenerInterface) {
        // 检测网络是否可用
        if (!NetUtil.checkNet(context)) {
            ToastUtil.show(context,
                    context.getResources().getString(R.string.net_unuser));
            return;
        }

        // 清除请求队列中的tag标记请求
        BaseApplication.getRequestQueue().cancelAll(tag);
        // 创建当前的POST请求，并将请求内容写入Map中
        request = new MyRequest(Request.Method.POST, url,
                volleyListenerInterface.responseListener(),
                volleyListenerInterface.errorListener(), params);
        // 为当前请求添加标记
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        BaseApplication.getRequestQueue().add(request);
        // 重启当前请求队列
//        BaseApplication.getRequestQueue().start();
    }

    /*
   * 获取DELETE请求内容 参数： context：当前上下文； url：请求的url地址； tag：当前请求的标签；
   * volleyListenerInterface：VolleyListenerInterface接口；
   */
    public static void RequestDelete(Context context, String url, String tag,
                                  VolleyListenerInterface volleyListenerInterface) {
        // 检测网络是否可用
        if (!NetUtil.checkNet(context)) {
            ToastUtil.show(context,
                    context.getResources().getString(R.string.net_unuser));
            return;
        }
        // 清除请求队列中的tag标记请求
        BaseApplication.getRequestQueue().cancelAll(tag);
        // 创建当前的请求，获取字符串内容
        request = new MyRequest(Request.Method.DELETE, url,
                volleyListenerInterface.responseListener(),
                volleyListenerInterface.errorListener());
        // 为当前请求添加标记
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        BaseApplication.getRequestQueue().add(request);
        // 重启当前请求队列
//        BaseApplication.getRequestQueue().start();
    }


    /*
   * 获取POST请求内容（请求的代码为Map） 参数： context：当前上下文； url：请求的url地址； tag：当前请求的标签；
   * params：POST请求内容； volleyListenerInterface：VolleyListenerInterface接口；
   */
    public static void RequestPut(Context context, String url, String tag,
                                   Map<String, String> params,
                                   VolleyListenerInterface volleyListenerInterface) {
        // 检测网络是否可用
        if (!NetUtil.checkNet(context)) {
            ToastUtil.show(context,
                    context.getResources().getString(R.string.net_unuser));
            return;
        }

        // 清除请求队列中的tag标记请求
        BaseApplication.getRequestQueue().cancelAll(tag);
        // 创建当前的POST请求，并将请求内容写入Map中
        request = new MyRequest(Request.Method.PUT, url,
                volleyListenerInterface.responseListener(),
                volleyListenerInterface.errorListener(), params);
        // 为当前请求添加标记
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        BaseApplication.getRequestQueue().add(request);
        // 重启当前请求队列
        BaseApplication.getRequestQueue().start();
    }

}

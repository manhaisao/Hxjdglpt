package com.xzz.hxjdglpt.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xzz.hxjdglpt.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MyRequest extends Request<JSONObject> {

    private Map<String, String> mMap;
    private Listener<JSONObject> mListener;

    public MyRequest(int method, String url, Listener<JSONObject> listener,
                     ErrorListener errorListener, Map<String, String> map) {
        super(method, url, errorListener);
        LogUtil.i("url=" + url);
        mListener = listener;
        mMap = map;
    }

    public MyRequest(int method, String url, Listener<JSONObject> listener,
                     ErrorListener errorListener) {
        super(method, url, errorListener);
        LogUtil.i("url=" + url);
        mListener = listener;
    }

    // mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        LogUtil.i("MyRequest getParams = " + Thread.currentThread().getName());
        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        LogUtil.i("MyRequest deliverResponse = "
                + Thread.currentThread().getName());
        mListener.onResponse(response);
    }

    /**
     * 在没有网的情况下使用磁盘缓存的数据 ;没有网时打开app，展示最近一次展示的内容
     */
    @Override
    public void deliverError(VolleyError error) {
        if (error instanceof NoConnectionError) {
            Cache.Entry entry = this.getCacheEntry();
            if (entry != null) {
                Response<JSONObject> response = parseNetworkResponse(new NetworkResponse(
                        entry.data, entry.responseHeaders));
                deliverResponse(response.result);
                return;
            }
        }
        super.deliverError(error);
    }

}

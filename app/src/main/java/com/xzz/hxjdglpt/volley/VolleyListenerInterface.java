package com.xzz.hxjdglpt.volley;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class VolleyListenerInterface {
	public Context mContext;
	public static Response.Listener<JSONObject> mListener;
	public static Response.ErrorListener mErrorListener;

	public VolleyListenerInterface(Context context,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		this.mContext = context;
		this.mErrorListener = errorListener;
		this.mListener = listener;
	}

	public VolleyListenerInterface(Context context) {
		this.mContext = context;
	}

	/**
	 * 请求成功时的回调函数
	 * 
	 * @param result
	 */
	public abstract void onMySuccess(JSONObject result);

	/**
	 * 请求失败时的回调函数
	 * 
	 * @param error
	 */
	public abstract void onMyError(VolleyError error);

	/**
	 * 创建请求的事件监听
	 * 
	 * @return
	 */
	public Response.Listener<JSONObject> responseListener() {
		mListener = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject json) {
				onMySuccess(json);
			}
		};
		return mListener;
	}

	/**
	 * 创建请求失败的事件监听
	 * 
	 * @return
	 */
	public Response.ErrorListener errorListener() {
		mErrorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				onMyError(volleyError);
			}
		};
		return mErrorListener;
	}
}

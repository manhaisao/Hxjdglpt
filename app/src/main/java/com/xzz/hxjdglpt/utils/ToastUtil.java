package com.xzz.hxjdglpt.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * toast提醒
 * 
 * @author dbz
 */
public class ToastUtil {

	public static void show(Context context, int resId) {
		show(context, resId, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String str) {
		show(context, str, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration) {
		Toast.makeText(context, resId, duration).show();
	}

	public static void show(Context context, String str, int duration) {
		Toast.makeText(context, str, duration).show();
	}
}

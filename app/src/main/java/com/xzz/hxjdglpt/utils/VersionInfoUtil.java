package com.xzz.hxjdglpt.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 有关App版本的方法
 * Created by dbz on 2017/6/15.
 */

public class VersionInfoUtil {

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("versionInfo", "Exception", e);
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int vCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            vCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("versionInfo", "Exception", e);
        }
        return vCode;
    }

}

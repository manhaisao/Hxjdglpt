package com.xzz.hxjdglpt.utils;

import android.util.Log;

/**
 * log日志
 *
 * @author dbz
 */
public class LogUtil {

    private static final boolean DEBUG = false;

    public static void i(String info) {
        if (DEBUG) {
            Log.i("dbz", info);
        }
    }
}

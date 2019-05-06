package com.xzz.hxjdglpt.utils;

import android.app.Activity;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by dbz on 2017/5/16.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void finishActivity(Activity activity) {
        activity.finish();
    }

}

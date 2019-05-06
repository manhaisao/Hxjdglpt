package com.xzz.hxjdglpt.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.activity.R;

public class VolleyErrorHelper {
    /**
     * volley返回信息
     *
     * @param error
     * @param context
     * @return String
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.net_timeout);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkError(error)) {
            return context.getResources().getString(R.string.net_unstability);
        } else if (isNoConnectionError(error)) {
            return context.getResources().getString(R.string.link_net);
        }
        return context.getResources().getString(R.string.net_unstability);
    }

    private static boolean isNetworkError(Object error) {
        return (error instanceof NetworkError);
    }

    private static boolean isNoConnectionError(Object error) {
        return (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError)
                || (error instanceof AuthFailureError);
    }

    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    return context.getResources().getString(R.string.net_fail);

                default:
                    return context.getResources().getString(R.string.net_timeout);
            }
        }
        return context.getResources().getString(R.string.net_unstability);
    }
}

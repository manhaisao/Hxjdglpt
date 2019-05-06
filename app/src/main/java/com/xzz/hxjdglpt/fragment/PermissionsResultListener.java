package com.xzz.hxjdglpt.fragment;

/**
 * 权限申请接口
 * Created by dbz on 2017/7/14.
 */

public interface PermissionsResultListener {
    void onPermissionGranted();

    void onPermissionDenied();
}

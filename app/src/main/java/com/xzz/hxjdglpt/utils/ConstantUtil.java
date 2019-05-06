package com.xzz.hxjdglpt.utils;

import android.os.Environment;

import java.io.File;

/**
 * 常量
 */
public class ConstantUtil {

//    public static String FILE_DOWNLOAD_URL = "http://192.168.0.108:8080/";
//    public static String BASE_URL = "http://192.168.0.108:8080/hx";   http://ludd.picp.net  116.62.147.36:80

    public static String FILE_DOWNLOAD_URL = "http://116.62.147.36:80/";
    public static String BASE_URL = "http://116.62.147.36:80/hx";
//    public static String FILE_DOWNLOAD_URL = "http://3s.dkys.org:29391/";   w1q5323158.iask.in
//    public static String BASE_URL = "http://3s.dkys.org:29391/hx";


    public static final String LOG_TAG = "RTSP";


    //下载后的APK的命名
    public static final String APK_NAME = "hx_Android";

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "download" + File.separator;

    //分页大小
    public static final int PAGE_SIZE = 20;

    public static final int ALL_IMAGE_COUNT = 5;

    //选择图片
    public static final int RESULT_LOAD_IMAGE = 1;

    //拍照
    public static final int TAKE_PHOTO_WITH_DATA = 2;

    /**
     * 头像裁剪大小
     */
    public static final int PIC_SIZE = 100;

    // SharedPreferences文件名
    public static final String FILE_NAME_SHARE = "sharedPreferencesGuid";
}

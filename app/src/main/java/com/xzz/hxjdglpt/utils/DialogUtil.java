package com.xzz.hxjdglpt.utils;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.LoginActivity;
import com.xzz.hxjdglpt.activity.R;

/**
 * Created by dbz on 2017/5/15.
 */

public class DialogUtil {

    /**
     * 账号在其他设备登录时弹出框
     *
     * @param activity
     */
    public static void showTipsDialog(final Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.device_dialog, null);
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Button butOk = (Button) view.findViewById(R.id.device_comf);
        butOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("isLogin", "false");
                intent.setClass(activity, LoginActivity.class);
                activity.startActivity(intent);
                //activity.finish();
                ActivityCollector.finishAll();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public static Dialog createDialog(final Activity activity, LayoutInflater inflater, final
    File file) {
        final Dialog dialog = new Dialog(activity, R.style.Custom_Dialog);
        View view = inflater.inflate(R.layout.dialog_add_image, null);
        TextView take = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView select = (TextView) view.findViewById(R.id.tv_select_photo);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancle_add);
        take.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ImageUtil.startCamera(activity, file);
                dialog.cancel();
            }
        });

        select.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ImageUtil.startGallery(activity);
                dialog.cancel();
            }
        });

        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;
        int width = metric.widthPixels;
        android.view.WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.y = (height - lp.height) / 2;
        lp.width = width - 100;
        dialog.show();
        return dialog;
    }

    public static Dialog createDialog(final Activity activity, LayoutInflater inflater) {
        final Dialog dialog = new Dialog(activity, R.style.Custom_Dialog);
        View view = inflater.inflate(R.layout.dialog_add_image, null);
        TextView take = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView select = (TextView) view.findViewById(R.id.tv_select_photo);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancle_add);
        take.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CameraUtil.openCamera(activity);
                dialog.cancel();
            }
        });

        select.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ImageUtil.startGallery(activity);
                dialog.cancel();
            }
        });

        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;
        int width = metric.widthPixels;
        android.view.WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.y = (height - lp.height) / 2;
        lp.width = width - 100;
        dialog.show();
        return dialog;
    }

    public static Dialog createUploadDialog(final Activity activity, LayoutInflater inflater) {
        final Dialog dialog = new Dialog(activity, R.style.Custom_Dialog);
        View view = inflater.inflate(R.layout.dialog_add_image, null);
        TextView take = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView select = (TextView) view.findViewById(R.id.tv_select_photo);
        select.setText(R.string.loacl_choose);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancle_add);
        take.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CameraUtil.openCamera(activity);
                dialog.cancel();
            }
        });

        select.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                activity.startActivityForResult(intent, 1);
                //                Intent intent = new Intent();
//                intent.setType("video/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                try {
//                    startActivityForResult(Intent.createChooser(intent, "选择一个视频上传(建议在50M之间)"),
//                            USE_PICTURE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                dialog.cancel();
            }
        });

        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;
        int width = metric.widthPixels;
        android.view.WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.y = (height - lp.height) / 2;
        lp.width = width - 100;
        dialog.show();
        return dialog;
    }

    public static Dialog createOnlyPhotoDialog(final Activity activity, LayoutInflater inflater) {
        final Dialog dialog = new Dialog(activity, R.style.Custom_Dialog);
        View view = inflater.inflate(R.layout.dialog_add_image, null);
        TextView take = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView select = (TextView) view.findViewById(R.id.tv_select_photo);
        select.setVisibility(View.GONE);
        TextView cancle = (TextView) view.findViewById(R.id.tv_cancle_add);
        take.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CameraUtil.openCamera(activity);
                dialog.cancel();
            }
        });

        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;
        int width = metric.widthPixels;
        android.view.WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.y = (height - lp.height) / 2;
        lp.width = width - 100;
        dialog.show();
        return dialog;
    }

}

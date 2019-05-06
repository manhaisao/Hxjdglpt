package com.xzz.hxjdglpt.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.maning.updatelibrary.InstallUtils;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.activity.AboutOurInfoActivity;
import com.xzz.hxjdglpt.activity.AccountSafeActivity;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.LoginActivity;
import com.xzz.hxjdglpt.activity.MainActivity;
import com.xzz.hxjdglpt.activity.ModifyMyPasswordActivity;
import com.xzz.hxjdglpt.activity.MyAnswerActivity;
import com.xzz.hxjdglpt.activity.MyConsultActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Version;
import com.xzz.hxjdglpt.utils.ActivityCollector;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.ImageUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.utils.VersionInfoUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;


/**
 * 我的
 * Created by dbz on 2017/5/10.
 */

public class MyInfoFragment extends BaseFragment implements OnClickListener {

    private TextView tvTitle;

    private Dialog dialogUpdate;


    private Button btExit;

    private CircleNetImageView cHead;

    private TextView tvName;

    private BaseApplication application;

    private User user;

    private LinearLayout lIcon;

    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private RelativeLayout rl5;
    private RelativeLayout rl6;

    private ImageLoader mImageLoader;

    private Version version;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.hx_title_);
        tvTitle.setText(getText(R.string.my_info));
        btExit = (Button) view.findViewById(R.id.myinfo_exit);
        cHead = (CircleNetImageView) view.findViewById(R.id.myinfo_head_img);
        tvName = (TextView) view.findViewById(R.id.myinfo_head_name);
        lIcon = (LinearLayout) view.findViewById(R.id.myinfo_ll);
        rl1 = (RelativeLayout) view.findViewById(R.id.myinfo_r1);
        rl2 = (RelativeLayout) view.findViewById(R.id.myinfo_r2);
        rl3 = (RelativeLayout) view.findViewById(R.id.myinfo_r3);
        rl4 = (RelativeLayout) view.findViewById(R.id.myinfo_r4);
        rl5 = (RelativeLayout) view.findViewById(R.id.myinfo_r5);
        rl6 = (RelativeLayout) view.findViewById(R.id.myinfo_r6);
        application = (BaseApplication) getActivity().getApplication();
        btExit.setOnClickListener(this);
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);

        int[] size = getResources().getIntArray(R.array.ad_default_size);
        BaseApplication application = (BaseApplication) getActivity().getApplicationContext();
        double scale = application.getScreenWidth() * 1.0 / size[0];
        int desireHeight = (int) (scale * size[1]);
        int desireWidth = application.getScreenWidth();
        lIcon.getLayoutParams().width = desireWidth;
        lIcon.getLayoutParams().height = desireHeight;
        //下载进度弹出框
        createDialog();
    }

    public void initData() {
        user = application.getUser();
        mImageLoader = application.getImageLoader();

    }

    @Override
    public void onResume() {
        super.onResume();
        cHead.setDefaultImageResId(R.mipmap.user_icon);
        cHead.setErrorImageResId(R.mipmap.user_icon);
        cHead.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + user.getPicture(), mImageLoader);
        tvName.setText(application.getUser().getRealName());
//        setHeadPic();
    }

    private void setHeadPic() {
        if (!TextUtils.isEmpty(user.getPicture())) {
            File file = new File(ImageUtil.picturesPath(getActivity()).getAbsoluteFile() + user
                    .getPicture());
            if (file.exists()) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    cHead.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                downloadPicture(user.getPicture());
            }
        }
    }

    /**
     * 下载头像至指定目录
     *
     * @param fileName
     */
    private void downloadPicture(String fileName) {
        final String filePath = ImageUtil.picturesPath(getActivity()).getAbsoluteFile() + fileName;
        final File file = new File(ImageUtil.picturesPath(getActivity()).getAbsoluteFile() +
                "/uploadFile/temp/common/");
        //文件下载地址
        String url = ConstantUtil.FILE_DOWNLOAD_URL + fileName;
        //如果不存在
        if (!file.exists()) {
            //创建
            file.mkdirs();
        }
        RequestParams entity = new RequestParams(url);
        entity.setSaveFilePath(filePath);
        x.http().get(entity, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
//                filesPath.add(result.getAbsolutePath());
                LogUtil.i("onSuccess：" + result.getAbsolutePath());
                Bitmap bitmap = BitmapFactory.decodeFile(result.getPath());
                if (bitmap != null) {
                    cHead.setImageBitmap(bitmap);
                } else {
                    File file = new File(filePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.i("onError");
//                ToastUtil.show(AccountSafeActivity.this,R.string.download_failed);
                cHead.setImageBitmap(BitmapUtil.readBitMap(getActivity(), R.mipmap.user_icon));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.i("onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.i("onFinished ");
//                ToastUtil.show(AccountSafeActivity.this,R.string.download_success);
            }
        });
    }


    /**
     * 版本更新
     */
    private void checkVersion() {
        String url = ConstantUtil.BASE_URL + "/m_version/queryNewVersion";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(getActivity(), url, "queryNewVersion", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        version = (Version) gson.fromJson(result.getJSONObject("data").toString()
                                , Version.class);
                        if (version != null) {
                            int code = VersionInfoUtil.getVersionCode(getActivity());
                            if (code != version.getCode()) {
//                                String path = ConstantUtil.BASE_PATH + version.getName();
                                String url = ConstantUtil.FILE_DOWNLOAD_URL + version.getPath();
                                LogUtil.i("url=" + url);
                                downloadVersion(url);
                            } else {
                                ToastUtil.show(getActivity(), R.string.version_new);
                            }
                        }
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(getActivity(), R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                        ToastUtil.show(getActivity(), R.string.version_new);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryNewVersion");
    }

    /**
     * 下载apk谈出框
     */
    private void downloadVersion(final String url) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.version_update_dialog, null);
        dialogUpdate = new Dialog(getActivity());
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(view);
        Button butOk = (Button) view.findViewById(R.id.setting_update_version);
        Button butCancle = (Button) view.findViewById(R.id.setting_not_update);
        butCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogUpdate.dismiss();

            }
        });
        butOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdate.dismiss();
                downloadAPK(url);
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialogUpdate.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialogUpdate.getWindow().setAttributes(lp);
        dialogUpdate.show();
    }

    private ProgressDialog dialog = null;

    private void createDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("下载进度提示");
        dialog.setMax(100);
    }

    private void downloadAPK(String apkUrl) {
        new InstallUtils(mContext, apkUrl, ConstantUtil.APK_NAME, new InstallUtils
                .DownloadCallBack() {
            @Override
            public void onStart() {
                dialog.show();
                dialog.setProgress(0);
            }

            @Override
            public void onComplete(String path) {

                /**
                 * 安装APK工具类
                 * @param context       上下文
                 * @param filePath      文件路径
                 * @param authorities   ---------Manifest中配置provider的authorities字段---------
                 * @param callBack      安装界面成功调起的回调
                 */
                InstallUtils.installAPK(mContext, path, getActivity().getPackageName() + "" + ""
                        + ".FileProvider", new InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.show(mContext, "正在安装程序");
                    }

                    @Override
                    public void onFail(Exception e) {
                        ToastUtil.show(mContext, "安装失败:" + e.toString());
                    }
                });
                dialog.dismiss();
                dialog.setProgress(100);
            }

            @Override
            public void onLoading(long total, long current) {
                LogUtil.i("InstallUtils----onLoading:-----total:" + total + ",current:" + current);
                dialog.setProgress((int) (current * 100 / total));
            }

            @Override
            public void onFail(Exception e) {
                LogUtil.i("InstallUtils---onFail:" + e.getMessage());
                dialog.dismiss();
            }

        }).downloadAPK();
    }


    /**
     * 注销、退出登录
     */
    private void logout() {
        SealUserInfoManager.getInstance().closeDB();
        RongIM.getInstance().logout();
        Intent intent = new Intent();
        ((MainActivity) getActivity()).application.setUser(null);
        clearUser();
//        JPushInterface.stopPush(getActivity());
        intent.setClass((MainActivity) getActivity(), LoginActivity.class);
        startActivity(intent);
        ActivityCollector.finishAll();
    }

    /**
     * 清除SharedPreferences里面USER
     */
    private void clearUser() {
        SharedPreferences mySharedP = getActivity().getSharedPreferences("hxjd_base64_user",
                Activity.MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(null);
            Editor editor = mySharedP.edit();
            String userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("userInfo", userBase64);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.myinfo_exit:
                new AlertDialog.Builder(getActivity()).setTitle("确认退出吗？").setIcon(android.R
                        .drawable.ic_dialog_info).setPositiveButton("取消", new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).show();

                break;
            case R.id.myinfo_r1:
                intent.setClass(getActivity(), AccountSafeActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.myinfo_r2:
                intent.setClass(getActivity(), ModifyMyPasswordActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.myinfo_r3:
                intent.setClass(getActivity(), MyAnswerActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.myinfo_r4:
                intent.setClass(getActivity(), MyConsultActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.myinfo_r5:
                checkVersion();
                break;
            case R.id.myinfo_r6:
                intent.setClass(getActivity(), AboutOurInfoActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

}

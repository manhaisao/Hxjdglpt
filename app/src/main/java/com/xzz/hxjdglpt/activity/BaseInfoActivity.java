package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.fragment.BaseFragment;
import com.xzz.hxjdglpt.fragment.GridFragment;
import com.xzz.hxjdglpt.fragment.VillageFragment;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;


/**
 * 基础信息
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_baseinfo)
public class BaseInfoActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private String tabNames[] = new String[2];


    @ViewInject(R.id.c_baseinfo_rg)
    private RadioGroup rgChannel;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private VillageFragment villageFragment;
    private GridFragment gridFragment;

    private User user;
    private BaseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tabNames = getResources().getStringArray(R.array.baseinfo);
        tvTitle.setText(getText(R.string.base_info_));
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        initData();
        initView();
    }


    public void initView() {
        /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        rgChannel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onItemSelected(checkedId);
            }
        });
        initTab();//动态产生RadioButton
        rgChannel.check(0);
    }

    private void initTab() {
        for (int i = 0; i < tabNames.length; i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(this).
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(tabNames[i]);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams
                    .WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            rb.setLayoutParams(params);
            rgChannel.addView(rb, i);
        }
    }


    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     *
     * @param idx
     */
    private void setTab(int idx) {
        RadioButton rb = (RadioButton) rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
//        hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
    }

    public void onItemSelected(int tabId) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        switch (tabId) {
            case 0:
                hideFragment();
                if (villageFragment == null) {
                    villageFragment = new VillageFragment();
                    transaction.add(R.id.main_content, villageFragment);
                } else {
                    transaction.show(villageFragment);
                }
                transaction.commit();
                break;
            case 1:
                hideFragment();
                if (gridFragment == null) {
                    gridFragment = new GridFragment();
                    transaction.add(R.id.main_content, gridFragment);
                } else {
                    transaction.show(gridFragment);
                }
                transaction.commit();
                break;
        }
    }

    private void hideFragment() {
        if (villageFragment != null) {
            transaction.hide(villageFragment);
        }
        if (gridFragment != null) {
            transaction.hide(gridFragment);
        }
    }


    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }

    //获取图片路径 响应startActivityForResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("requestCode=" + requestCode);
        switch (requestCode) {
            case ConstantUtil.TAKE_PHOTO_WITH_DATA:
                if (villageFragment != null && !villageFragment.isHidden()) {
                    villageFragment.pathImage = CameraUtil.tempPath;
                    File f = new File(villageFragment.pathImage);
                    if (f.exists()) {
                        uploadFile(villageFragment.pathImage);
                    }
                } else if (gridFragment != null && !gridFragment.isHidden()) {
                    gridFragment.pathImage = CameraUtil.tempPath;
                    File f = new File(gridFragment.pathImage);
                    if (f.exists()) {
                        uploadFile(gridFragment.pathImage);
                    }
                }
                break;
            case ConstantUtil.RESULT_LOAD_IMAGE:
                switch (resultCode) {
                    case -1:
                        if (null != data) {
                            Uri uri = data.getData();
                            if (!TextUtils.isEmpty(uri.getAuthority())) {
                                //查询选择图片
                                Cursor cursor = getContentResolver().query(uri, new
                                        String[]{MediaStore.Images.Media.DATA}, null, null, null);
                                //返回 没找到选择图片
                                if (null == cursor) {
                                    return;
                                }
                                //光标移动至开头 获取图片路径
                                cursor.moveToFirst();
                                if (villageFragment != null && !villageFragment.isHidden()) {
                                    villageFragment.pathImage = cursor.getString(cursor
                                            .getColumnIndex(MediaStore.Images.Media.DATA));
                                    File f1 = new File(villageFragment.pathImage);
                                    if (f1.exists()) {
                                        uploadFile(villageFragment.pathImage);
                                    }
                                } else if (gridFragment != null && !gridFragment.isHidden()) {
                                    gridFragment.pathImage = cursor.getString(cursor
                                            .getColumnIndex(MediaStore.Images.Media.DATA));
                                    File f1 = new File(gridFragment.pathImage);
                                    if (f1.exists()) {
                                        uploadFile(gridFragment.pathImage);
                                    }
                                }
                            }
                        }
                        break;
                }
                break;
        }
    }


    private Callback.Cancelable post;

    private void uploadFile(final String path) {
        SuccinctProgress.showSuccinctProgress(BaseInfoActivity.this, "图片上传中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/upload/uploadFile";
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(path));
        params.addParameter("userId", user.getUserId());
        params.addParameter("token", user.getToken());
        post = x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:上传成功 ；2：token不一致；3：上传失败
                    if ("1".equals(resultCode)) {
                        String picPath = result.getString("attachFilePath");
                        if (villageFragment != null && !villageFragment.isHidden()) {
                            villageFragment.mPath.add(picPath);
                            villageFragment.picPath = villageFragment.getPathStr();
                            new Thread(villageFragment.runnable1).start();
                        } else if (gridFragment != null && !gridFragment.isHidden()) {
                            gridFragment.mPath.add(picPath);
                            gridFragment.picPath = gridFragment.getPathStr();
                            new Thread(gridFragment.runnable1).start();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(BaseInfoActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(BaseInfoActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(BaseInfoActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onDestroy() {
        if (post != null) {
            post.cancel();
        }
        super.onDestroy();
    }

}

package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.fragment.CjryFragment;
import com.xzz.hxjdglpt.fragment.DbryFragment;
import com.xzz.hxjdglpt.fragment.YlfnFragment;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.CameraUtil;
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


/**
 * 人口信息
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_peoper)
public class PeoperActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private String tabNames[] = new String[3];


    @ViewInject(R.id.peoper_rg)
    private RadioGroup rgChannel;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private CjryFragment cjryFragment;
    private DbryFragment dbryFragment;
    private YlfnFragment ylfnFragment;

    private User user;
    private BaseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.peoper_info_));
        tabNames = getResources().getStringArray(R.array.ryinfo);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        initData();
        initView();
    }

    public void initView() {
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

    public void onItemSelected(int tabId) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        switch (tabId) {
            case 0:
                hideFragment();
                if (dbryFragment == null) {
                    dbryFragment = new DbryFragment();
                    transaction.add(R.id.main_content, dbryFragment);
                } else {
                    transaction.show(dbryFragment);
                }
                transaction.commit();
                break;
            case 1:
                hideFragment();
                if (cjryFragment == null) {
                    cjryFragment = new CjryFragment();
                    transaction.add(R.id.main_content, cjryFragment);
                } else {
                    transaction.show(cjryFragment);
                }
                transaction.commit();
                break;
            case 2:
                hideFragment();
                if (ylfnFragment == null) {
                    ylfnFragment = new YlfnFragment();
                    transaction.add(R.id.main_content, ylfnFragment);
                } else {
                    transaction.show(ylfnFragment);
                }
                transaction.commit();
                break;
        }
    }

    private void hideFragment() {
        if (dbryFragment != null) {
            transaction.hide(dbryFragment);
        }
        if (cjryFragment != null) {
            transaction.hide(cjryFragment);
        }
        if (ylfnFragment != null) {
            transaction.hide(ylfnFragment);
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
                if (dbryFragment != null && !dbryFragment.isHidden()) {
                    dbryFragment.pathImage = CameraUtil.tempPath;
                    File f = new File(dbryFragment.pathImage);
                    if (f.exists()) {
                        uploadFile(dbryFragment.pathImage);
                    }
                } else if (cjryFragment != null && !cjryFragment.isHidden()) {
                    cjryFragment.pathImage = CameraUtil.tempPath;
                    File f = new File(cjryFragment.pathImage);
                    if (f.exists()) {
                        uploadFile(cjryFragment.pathImage);
                    }
                } else if (ylfnFragment != null && !ylfnFragment.isHidden()) {
                    ylfnFragment.pathImage = CameraUtil.tempPath;
                    File f = new File(ylfnFragment.pathImage);
                    if (f.exists()) {
                        uploadFile(ylfnFragment.pathImage);
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
                                if (dbryFragment != null && !dbryFragment.isHidden()) {
                                    dbryFragment.pathImage = cursor.getString(cursor
                                            .getColumnIndex(MediaStore.Images.Media.DATA));
                                    File f1 = new File(dbryFragment.pathImage);
                                    if (f1.exists()) {
                                        uploadFile(dbryFragment.pathImage);
                                    }
                                } else if (cjryFragment != null && !cjryFragment.isHidden()) {
                                    cjryFragment.pathImage = cursor.getString(cursor
                                            .getColumnIndex(MediaStore.Images.Media.DATA));
                                    File f1 = new File(cjryFragment.pathImage);
                                    if (f1.exists()) {
                                        uploadFile(cjryFragment.pathImage);
                                    }
                                } else if (ylfnFragment != null && !ylfnFragment.isHidden()) {
                                    ylfnFragment.pathImage = cursor.getString(cursor
                                            .getColumnIndex(MediaStore.Images.Media.DATA));
                                    File f1 = new File(ylfnFragment.pathImage);
                                    if (f1.exists()) {
                                        uploadFile(ylfnFragment.pathImage);
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
        SuccinctProgress.showSuccinctProgress(PeoperActivity.this, "图片上传中···", SuccinctProgress
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
                        if (dbryFragment != null && !dbryFragment.isHidden()) {
                            dbryFragment.mPath.add(picPath);
                            new Thread(dbryFragment.runnable).start();
                        } else if (cjryFragment != null && !cjryFragment.isHidden()) {
                            cjryFragment.mPath.add(picPath);
                            new Thread(cjryFragment.runnable).start();
                        } else if (ylfnFragment != null && !ylfnFragment.isHidden()) {
                            ylfnFragment.mPath.add(picPath);
                            new Thread(ylfnFragment.runnable).start();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PeoperActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PeoperActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(PeoperActivity.this, R.string.upload_image_fail);
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

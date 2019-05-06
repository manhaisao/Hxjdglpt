package com.xzz.hxjdglpt.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.ImageUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * 账号安全
 * Created by dbz on 2017/5/26.
 */
@ContentView(R.layout.aty_account_safe)
public class AccountSafeActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private User user;
    private ImageLoader mImageLoader;
    @ViewInject(R.id.myinfo_head_pic)
    private CircleNetImageView cHead;
    @ViewInject(R.id.myinfo_as_rl1)
    private RelativeLayout rl1;
    @ViewInject(R.id.myinfo_as_rl2)
    private RelativeLayout rl2;
    @ViewInject(R.id.myinfo_as_rl3)
    private RelativeLayout rl3;
    @ViewInject(R.id.myinfo_as_rl4)
    private RelativeLayout rl4;
    @ViewInject(R.id.myinfo_as_rl5)
    private RelativeLayout rl5;
    @ViewInject(R.id.myinfo_as_rl6)
    private RelativeLayout rl6;
    @ViewInject(R.id.myinfo_as_name)
    private TextView tvName;
    @ViewInject(R.id.myinfo_as_account)
    private TextView tvAccount;
    @ViewInject(R.id.myinfo_as_qq)
    private TextView tvQQ;
    @ViewInject(R.id.myinfo_as_email)
    private TextView tvEmail;
    @ViewInject(R.id.myinfo_as_phone)
    private TextView tvPhone;
    private LayoutInflater mInflater;
//    private File tempFile;

    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int INFO_REQUEST_QQ = 4;
    private static final int INFO_REQUEST_NAME = 5;
    private static final int INFO_REQUEST_EMAIL = 6;
    private static final int INFO_REQUEST_PHONE = 7;
    private static final int INFO_RESULT_VALUE = 8;

//    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
//        tempFile = CompressImageUtil.createPicture(this);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(R.string.account_safe);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    public void initData() {
        user = application.getUser();
        mImageLoader = application.getImageLoader();
        cHead.setDefaultImageResId(R.mipmap.user_icon);
        cHead.setErrorImageResId(R.mipmap.user_icon);
        cHead.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + user.getPicture(), mImageLoader);
        tvName.setText(user.getRealName() != null ? user.getRealName() : "");
        tvAccount.setText(user.getUserName() != null ? user.getUserName() : "");
        tvQQ.setText(user.getQqcode() != null ? user.getQqcode() : "");
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        tvPhone.setText(user.getPhone() != null ? user.getPhone() : "");
//        setHeadPic();
    }

    private void setHeadPic() {
        if (!TextUtils.isEmpty(user.getPicture())) {
            File file = new File(ImageUtil.picturesPath(this).getAbsoluteFile() + user.getPicture
                    ());
            if (file.exists()) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    cHead.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Event(value = {R.id.iv_back, R.id.myinfo_as_rl1, R.id.myinfo_as_rl2, R.id.myinfo_as_rl3, R
            .id.myinfo_as_rl4, R.id.myinfo_as_rl5, R.id.myinfo_as_rl6}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.myinfo_as_rl1:
                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE}, 0x0001);
                break;
            case R.id.myinfo_as_rl2:
                intent.setClass(AccountSafeActivity.this, ModifyMyInfo.class);
                intent.putExtra("type", "name");
                startActivityForResult(intent, INFO_REQUEST_NAME);
                break;
            case R.id.myinfo_as_rl3:
                ToastUtil.show(this, R.string.account_not_update);
                break;
            case R.id.myinfo_as_rl4:
                intent.setClass(AccountSafeActivity.this, ModifyMyInfo.class);
                intent.putExtra("type", "qq");
                startActivityForResult(intent, INFO_REQUEST_QQ);
                break;
            case R.id.myinfo_as_rl5:
                intent.setClass(AccountSafeActivity.this, ModifyMyInfo.class);
                intent.putExtra("type", "phone");
                startActivityForResult(intent, INFO_REQUEST_PHONE);
                break;
            case R.id.myinfo_as_rl6:
                intent.setClass(AccountSafeActivity.this, ModifyMyInfo.class);
                intent.putExtra("type", "email");
                startActivityForResult(intent, INFO_REQUEST_EMAIL);
                break;

        }
    }

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0001:
//                DialogUtil.createDialog(this, mInflater, tempFile);
                DialogUtil.createDialog(this, mInflater);
                break;
        }
    }


    /**
     * 裁剪照片
     *
     * @param uri
     * @param size
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantUtil.TAKE_PHOTO_WITH_DATA:
                Bitmap bitmap = BitmapFactory.decodeFile(CameraUtil.tempPath);
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                        bitmap, null, null));
                startPhotoZoom(uri, ConstantUtil.PIC_SIZE);
                break;
            case ConstantUtil.RESULT_LOAD_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (null != data) {
                            startPhotoZoom(data.getData(), ConstantUtil.PIC_SIZE);
                        }
                        break;
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = bundle.getParcelable("data");
//                        Bitmap roundP = CompressImageUtil.toRoundBitmap(photo);
                        String uploadFile = CompressImageUtil.putHeadPicture(photo, this).getPath();
                        LogUtil.i(uploadFile);
                        uploadFile(uploadFile);
                    }
                }
                break;
            case INFO_REQUEST_QQ:
                switch (resultCode) {
                    case INFO_RESULT_VALUE:
                        tvQQ.setText(data.getStringExtra("value"));
                        break;
                    default:
                        break;
                }
                break;
            case INFO_REQUEST_NAME:
                switch (resultCode) {
                    case INFO_RESULT_VALUE:
                        tvName.setText(data.getStringExtra("value"));
                        break;
                    default:
                        break;
                }
                break;
            case INFO_REQUEST_EMAIL:
                switch (resultCode) {
                    case INFO_RESULT_VALUE:
                        tvEmail.setText(data.getStringExtra("value"));
                        break;
                    default:
                        break;
                }
                break;
            case INFO_REQUEST_PHONE:
                switch (resultCode) {
                    case INFO_RESULT_VALUE:
                        tvPhone.setText(data.getStringExtra("value"));
                        break;
                    default:
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private Callback.Cancelable post;

    private void uploadFile(final String path) {
        String url = ConstantUtil.BASE_URL + "/upload/uploadFile";
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(path));
        params.addParameter("userId", user.getUserId());
        params.addParameter("token", user.getToken());
        post = x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:上传成功 ；2：token不一致；3：上传失败
                    if ("1".equals(resultCode)) {
                        String picPath = result.getString("attachFilePath");
                        modifyInfo(picPath, path);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(AccountSafeActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(AccountSafeActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(AccountSafeActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void modifyInfo(final String picPath, final String path) {
        String url = ConstantUtil.BASE_URL + "/user/updateUser";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", "picture");
        params.put("value", picPath);
        VolleyRequest.RequestPost(this, url, "update_user", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:修改成功 ；2；token失效 3：修改失败
                    if ("1".equals(resultCode)) {
                        ImageUtil.loadLocalImage(path, cHead);
                        user.setPicture(picPath);
                        application.setUser(user);
                        saveUser(user);
                        ToastUtil.show(AccountSafeActivity.this, R.string.upload_image_success);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(AccountSafeActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(AccountSafeActivity.this, R.string.modify_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    /**
     * 将修改后的信息保存到SharedPreferences里面
     *
     * @param user
     */
    public void saveUser(User user) {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_user", Activity
                .MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            SharedPreferences.Editor editor = mySharedP.edit();
            String userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("userInfo", userBase64);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("update_user");
        if (post != null) {
            post.cancel();
        }
    }


}

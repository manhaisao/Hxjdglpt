package com.xzz.hxjdglpt.activity.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.SealConst;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.activity.BaseActivity;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.db.Groups;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.server.broadcast.BroadcastManager;
import com.xzz.hxjdglpt.server.utils.NToast;
import com.xzz.hxjdglpt.server.widget.ClearWriteEditText;
import com.xzz.hxjdglpt.server.widget.LoadDialog;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {

    public static final String REFRESH_GROUP_UI = "REFRESH_GROUP_UI";
    private AsyncImageView asyncImageView;
    //    private PhotoUtils photoUtils;
//    private BottomMenuDialog dialog;
    private String mGroupName, mGroupId;
    private ClearWriteEditText mGroupNameEdit;
    private List<String> groupIds = new ArrayList<>();
    //    private Uri selectUri;
    private String imageUrl = "";
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    private LayoutInflater mInflater;
    private User user;

    private StringBuffer userIds = new StringBuffer();

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle(R.string.rc_item_create_group);
        List<Friend> memberList = (List<Friend>) getIntent().getSerializableExtra("GroupMember");
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (user == null) {
            user = application.getUser();
        }
        initView();
//        setPortraitChangeListener();
        if (memberList != null && memberList.size() > 0) {
            groupIds.add(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst
                    .SEALTALK_LOGIN_ID, ""));
            groupIds.add(user.getUserId());
            userIds.append(user.getUserId()).append(",");
            for (Friend f : memberList) {
                groupIds.add(f.getUserId());
                userIds.append(f.getUserId()).append(",");
            }
        }
    }

//    private void setPortraitChangeListener() {
//        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
//            @Override
//            public void onPhotoResult(Uri uri) {
//                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
//                    selectUri = uri;
//                    LoadDialog.show(CreateGroupActivity.this);
//                    LogUtil.i("上传图片");
//                }
//            }
//
//            @Override
//            public void onPhotoCancel() {
//
//            }
//        });
//    }

    public void initView() {
        asyncImageView = (AsyncImageView) findViewById(R.id.img_Group_portrait);
        asyncImageView.setOnClickListener(this);
        Button mButton = (Button) findViewById(R.id.create_ok);
        mButton.setOnClickListener(this);
        mGroupNameEdit = (ClearWriteEditText) findViewById(R.id.create_groupname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_Group_portrait:
//                showPhotoDialog();
                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE}, 0x0001);
                break;
            case R.id.create_ok:
                mGroupName = mGroupNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mGroupName)) {
                    NToast.shortToast(CreateGroupActivity.this, getString(R.string
                            .group_name_not_is_null));
                    break;
                }
                if (mGroupName.length() == 1) {
                    NToast.shortToast(CreateGroupActivity.this, getString(R.string
                            .group_name_size_is_one));
                    return;
                }
                if (AndroidEmoji.isEmoji(mGroupName)) {
                    if (mGroupName.length() <= 2) {
                        NToast.shortToast(CreateGroupActivity.this, getString(R.string
                                .group_name_size_is_one));
                        return;
                    }
                }
                if (TextUtils.isEmpty(imageUrl)) {
                    NToast.shortToast(CreateGroupActivity.this, "请上传组群头像");
                    return;
                }
                if (groupIds.size() > 1) {
                    LoadDialog.show(CreateGroupActivity.this);
                    addGroup();
                }
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }


    private void addGroup() {
//        SuccinctProgress.showSuccinctProgress(this, "请求数据中···", SuccinctProgress.THEME_ULTIMATE,
//                false, true);
        String url = ConstantUtil.BASE_URL + "/m_mi/createGroup";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userIds.toString());
        params.put("token", user.getToken());
        params.put("groupImageUrl", imageUrl);
        params.put("groupName", mGroupName);
        params.put("creator", user.getUserId());
        VolleyRequest.RequestPost(CreateGroupActivity.this, url, "createGroup", params, new
                VolleyListenerInterface(CreateGroupActivity.this, VolleyListenerInterface
                        .mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
                LoadDialog.dismiss(mContext);
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("200".equals(resultCode)) {
                        Gson gson = new Gson();
                        Groups group = (Groups) gson.fromJson(result.getJSONObject("data")
                                .toString(), Groups.class);
                        mGroupId = String.valueOf(group.getId());
                        mGroupName = group.getName();
                        imageUrl = group.getPortraitUri();
//                        if (TextUtils.isEmpty(imageUrl)) {
                        SealUserInfoManager.getInstance().addGroup(new Groups(mGroupId,
                                mGroupName, group.getCreator(), imageUrl));
                        BroadcastManager.getInstance(CreateGroupActivity.this).sendBroadcast
                                (REFRESH_GROUP_UI);
                        LoadDialog.dismiss(CreateGroupActivity.this);
                        NToast.shortToast(CreateGroupActivity.this, getString(R.string
                                .create_group_success));
                        RongIM.getInstance().startConversation(CreateGroupActivity.this,
                                Conversation.ConversationType.GROUP, mGroupId, mGroupName);
                        finish();
//                        } else if (!TextUtils.isEmpty(mGroupId)) {
//                            SealUserInfoManager.getInstance().addGroup(new Groups(mGroupId,
//                                    mGroupName, imageUrl, String.valueOf(0)));
//                            BroadcastManager.getInstance(CreateGroupActivity.this).sendBroadcast
//                                    (REFRESH_GROUP_UI);
//                            LoadDialog.dismiss(CreateGroupActivity.this);
//                            NToast.shortToast(CreateGroupActivity.this, getString(R.string
//                                    .create_group_success));
//                            RongIM.getInstance().startConversation(CreateGroupActivity.this,
//                                    Conversation.ConversationType.GROUP, mGroupId, mGroupName);
//                            finish();
//                        }
                    } else {
                        ToastUtil.show(CreateGroupActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
//                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                LoadDialog.dismiss(CreateGroupActivity.this);
                NToast.shortToast(CreateGroupActivity.this, getString(R.string
                        .group_create_api_fail));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
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
                        imageUrl = picPath;
                        Log.e("uploadImage", imageUrl);
                        if (!TextUtils.isEmpty(imageUrl)) {
                            ImageLoader.getInstance().displayImage(ConstantUtil.FILE_DOWNLOAD_URL
                                    + imageUrl, asyncImageView);
                            LoadDialog.dismiss(CreateGroupActivity.this);
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(CreateGroupActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CreateGroupActivity.this, R.string.upload_portrait_failed);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                ToastUtil.show(CreateGroupActivity.this, R.string.upload_image_fail);
                LoadDialog.dismiss(CreateGroupActivity.this);
                NToast.shortToast(CreateGroupActivity.this, getString(R.string
                        .upload_portrait_failed));
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
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("createGroup");
        if (post != null) {
            post.cancel();
        }
    }
}

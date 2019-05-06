package com.xzz.hxjdglpt.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.message.PlotEvent;
import com.xzz.hxjdglpt.model.Jyh;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.FileManageUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019\4\14 0014.
 */

@ContentView(R.layout.aty_jyh_fb)
public class JyhAddActivity extends BaseActivity {

    @ViewInject(R.id.jyh_spmc)
    private EditText jyh_spmc;
    @ViewInject(R.id.jyh_name)
    private EditText jyh_name;
    @ViewInject(R.id.jyh_phone)
    private EditText jyh_phone;
    @ViewInject(R.id.jyh_area)
    private EditText jyh_area;
    @ViewInject(R.id.gzdt_fb_des)
    private EditText gzdt_fb_des;


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.jyh_fb_commit)
    private Button btnCommit;
    private User user;
    private String plotId;
    @ViewInject(R.id.gzdt_fj)
    private LinearLayout tvFile;
    private LayoutInflater mInflater;

    private String pathStr = "";
    private String filename = "";

    private boolean isAdd;

    private Jyh jyh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        jyh = (Jyh) getIntent().getSerializableExtra("jyh");
        plotId = getIntent().getStringExtra("pid");
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        tvTitle.setText("经营户信息");
        initView();
        initData();
        createDialog();
    }

    public void initView() {
        if (!isAdd) {
            jyh_spmc.setText(jyh.getName());
            jyh_name.setText(jyh.getOwnerName());
            jyh_phone.setText(jyh.getPhone());
            jyh_area.setText(jyh.getArea());
            gzdt_fb_des.setText(jyh.getComment());
            filename = jyh.getFileName();
            pathStr = jyh.getFilePath();

            tvFile.removeAllViews();
            final String[] pss = pathStr.split(",");
            final String[] psn = filename.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < pss.length; i++) {
                if (pss[i] != null && !TextUtils.isEmpty(pss[i])) {
                    if (psn[i].endsWith(".jpg") || psn[i].endsWith(".png") ||
                            psn[i].endsWith(".JPG") || psn[i].endsWith("" + "" +
                            ".PNG") || psn[i].endsWith(".jpeg") || psn[i]
                            .endsWith(".JPEG") || psn[i].endsWith("" + ".BMP") ||
                            psn[i].endsWith(".bmp") || psn[i].endsWith("" + "" +
                            ".gif") || psn[i].endsWith("" + ".GIF")) {
                        list.add(pss[i]);
                    }
                }
            }
            final String[] values = (String[]) list.toArray(new String[list.size
                    ()]);
            if (pss.length == psn.length) {
                for (int i = 0; i < pss.length; i++) {
                    final String val = pss[i];
                    if (val != null && !TextUtils.isEmpty(val)) {
                        TextView tv = new TextView(JyhAddActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout
                                .LayoutParams(LinearLayout.LayoutParams
                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                .WRAP_CONTENT);
                        tv.setLayoutParams(lp);
                        int pxValue = DensityUtil.dip2px(JyhAddActivity.this, 5);
                        tv.setPadding(pxValue, pxValue, pxValue, pxValue);
                        tv.setTextColor(getResources().getColor(R.color.title_bg));
                        tv.setText(psn[i]);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (val.endsWith(".jpg") || val.endsWith(".png")
                                        || val.endsWith(".JPG") || val.endsWith
                                        ("" + ".PNG") || val.endsWith(".jpeg") ||
                                        val.endsWith(".JPEG") || val.endsWith(""
                                        + "" + ".BMP") || val.endsWith(".bmp") ||
                                        val.endsWith(".gif") || val.endsWith("" +
                                        ".GIF")) {
                                    Intent intent = new Intent();
                                    intent.putExtra("imagesName", values);
                                    intent.putExtra("index", 0);
                                    intent.setClass(JyhAddActivity.this,
                                            ShowImageActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.show(JyhAddActivity.this, R.string
                                            .file_tip);
                                }
                            }
                        });
                        tvFile.addView(tv);
                    }

                }
            }
        }
    }

    @Event(value = {R.id.iv_back, R.id.jyh_fb_commit, R.id.jyh_fj_add}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.jyh_fb_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }

                break;
            case R.id.jyh_fj_add:
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
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
                DialogUtil.createUploadDialog(this, mInflater);
                break;
        }
    }


    private void commit() {
        SuccinctProgress.showSuccinctProgress(JyhAddActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/" + plotId + "/jyhs";
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(jyh_area.getText().toString())) {
            params.put("area", jyh_area.getText().toString());
        } else {
            params.put("area", "");
        }
        if (!TextUtils.isEmpty(jyh_spmc.getText().toString())) {
            params.put("name", jyh_spmc.getText().toString());
        } else {
            params.put("name", "");
        }

        if (!TextUtils.isEmpty(jyh_name.getText().toString())) {
            params.put("ownerName", jyh_name.getText().toString());
        } else {
            params.put("ownerName", "");
        }
        if (!TextUtils.isEmpty(jyh_phone.getText().toString())) {
            params.put("phone", jyh_phone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(gzdt_fb_des.getText().toString())) {
            params.put("comment", gzdt_fb_des.getText().toString());
        } else {
            params.put("comment", "");
        }

        params.put("plotId ", plotId);
        params.put("fileName", filename);
        params.put("filePath", pathStr);
        VolleyRequest.RequestPost(this, url, "commitGzdt", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("0".equals(resultCode)) {
                        EventBus.getDefault().post(new PlotEvent(true));
                        ToastUtil.show(JyhAddActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyhAddActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyhAddActivity.this, R.string.commit_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMyError");
                // 返回失败的原因
                Log.e("insert", error.getMessage());
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }


    private void update() {
        SuccinctProgress.showSuccinctProgress(JyhAddActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/plot/jyhs/" + jyh.getId();
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(jyh_area.getText().toString())) {
            params.put("area", jyh_area.getText().toString());
        } else {
            params.put("area", "");
        }
        if (!TextUtils.isEmpty(jyh_spmc.getText().toString())) {
            params.put("name", jyh_spmc.getText().toString());
        } else {
            params.put("name", "");
        }

        if (!TextUtils.isEmpty(jyh_name.getText().toString())) {
            params.put("ownerName", jyh_name.getText().toString());
        } else {
            params.put("ownerName", "");
        }
        if (!TextUtils.isEmpty(jyh_phone.getText().toString())) {
            params.put("phone", jyh_phone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(gzdt_fb_des.getText().toString())) {
            params.put("comment", gzdt_fb_des.getText().toString());
        } else {
            params.put("comment", "");
        }
        params.put("id ", jyh.getId() + "");
        params.put("plotId ", plotId);
        params.put("fileName", filename);
        params.put("filePath", pathStr);
        VolleyRequest.RequestPost(this, url, "commitGzdt", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("0".equals(resultCode)) {
                        ToastUtil.show(JyhAddActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyhAddActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyhAddActivity.this, R.string.commit_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMyError");
                // 返回失败的原因
                Log.e("insert", error.getMessage());
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    private String path;//选择的文件本地路径

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantUtil.TAKE_PHOTO_WITH_DATA:
                path = CameraUtil.tempPath;
                Bitmap bm = CompressImageUtil.getSmallBitmap(path);
                Bitmap realBm = BitmapUtil.addTimeFlag(bm);
                File f = CompressImageUtil.putImage(realBm, this);
                if (f.exists()) {
                    path = f.getAbsolutePath();
                    uploadFile(f.getAbsolutePath());
                }
                break;
            case 1:
                if (data == null) {
                    ToastUtil.show(this, "没有文件，请重新选择");
                    return;
                }
                Uri uri = data.getData();
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    path = uri.getPath();
                    uploadFile(path);
                    return;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    path = FileManageUtil.getPath(this, uri);
                    uploadFile(path);
                } else {//4.4以下下系统调用方法
//                    path = FileManageUtil.getRealPathFromURI(uri);
                    path = FileManageUtil.getPath(this, uri);
                    uploadFile(path);
                }
                break;
        }
    }

    private ProgressDialog dialog = null;

    /**
     * 上传进度框
     */
    private void createDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("上传进度提示");
        dialog.setMax(100);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OkHttpManager.DOWNLOAD_SUCCESS:
                    ToastUtil.show(JyhAddActivity.this, R.string.upload_image_success);
                    times = 0;
                    dialog.dismiss();
                    LogUtil.i("msg.obj" + msg.obj);
                    if (msg.obj != null) {
                        String obj = String.valueOf(msg.obj);
                        Gson gson = new Gson();
                        Map<String, String> map = gson.fromJson(obj, new TypeToken<Map<String,
                                String>>() {
                        }.getType());
                        String resultCode = map.get("resultCode");
                        // resultCode 1:上传成功 ；2：token不一致；3：上传失败
                        if ("1".equals(resultCode)) {
                            String picPath = map.get("attachFilePath");
                            String[] paths = path.split("/");
                            filename = filename + "," + paths[paths.length - 1];
                            pathStr = pathStr + "," + picPath;
                            tvFile.removeAllViews();
                            final String[] pss = pathStr.split(",");
                            final String[] psn = filename.split(",");
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < pss.length; i++) {
                                if (pss[i] != null && !TextUtils.isEmpty(pss[i])) {
                                    if (psn[i].endsWith(".jpg") || psn[i].endsWith(".png") ||
                                            psn[i].endsWith(".JPG") || psn[i].endsWith("" + "" +
                                            ".PNG") || psn[i].endsWith(".jpeg") || psn[i]
                                            .endsWith(".JPEG") || psn[i].endsWith("" + ".BMP") ||
                                            psn[i].endsWith(".bmp") || psn[i].endsWith("" + "" +
                                            ".gif") || psn[i].endsWith("" + ".GIF")) {
                                        list.add(pss[i]);
                                    }
                                }
                            }
                            final String[] values = (String[]) list.toArray(new String[list.size
                                    ()]);
                            if (pss.length == psn.length) {
                                for (int i = 0; i < pss.length; i++) {
                                    final String val = pss[i];
                                    if (val != null && !TextUtils.isEmpty(val)) {
                                        TextView tv = new TextView(JyhAddActivity.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout
                                                .LayoutParams(LinearLayout.LayoutParams
                                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                                .WRAP_CONTENT);
                                        tv.setLayoutParams(lp);
                                        int pxValue = DensityUtil.dip2px(JyhAddActivity.this, 5);
                                        tv.setPadding(pxValue, pxValue, pxValue, pxValue);
                                        tv.setTextColor(getResources().getColor(R.color.title_bg));
                                        tv.setText(psn[i]);
                                        tv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (val.endsWith(".jpg") || val.endsWith(".png")
                                                        || val.endsWith(".JPG") || val.endsWith
                                                        ("" + ".PNG") || val.endsWith(".jpeg") ||
                                                        val.endsWith(".JPEG") || val.endsWith(""
                                                        + "" + ".BMP") || val.endsWith(".bmp") ||
                                                        val.endsWith(".gif") || val.endsWith("" +
                                                        ".GIF")) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("imagesName", values);
                                                    intent.putExtra("index", 0);
                                                    intent.setClass(JyhAddActivity.this,
                                                            ShowImageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    ToastUtil.show(JyhAddActivity.this, R.string
                                                            .file_tip);
                                                }
                                            }
                                        });
                                        tvFile.addView(tv);
                                    }
                                }
                            }
                            ToastUtil.show(JyhAddActivity.this, R.string.upload_image_success);
                        } else if ("2".equals(resultCode)) {
                            DialogUtil.showTipsDialog(JyhAddActivity.this);
                        } else if ("3".equals(resultCode)) {
                            ToastUtil.show(JyhAddActivity.this, R.string.upload_image_fail);
                        }
                    }
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(JyhAddActivity.this, R.string.upload_image_fail);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(JyhAddActivity.this, R.string.net_unuser);
                    times = 0;
                    dialog.dismiss();
                    break;
                default:
                    dialog.setProgress(msg.what);
                    break;
            }
        }
    };

    private int times = 0;

    private void uploadFile(String path) {
        dialog.show();
        File file = new File(path);
        String url = ConstantUtil.BASE_URL + "/upload/uploadFile";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        OkHttpManager.upload(url, new File[]{file}, new String[]{"file"}, params, new
                OkHttpManager.ProgressListener() {  //在这里监听上传进度
                    @Override
                    public void onProgress(long totalSize, long currSize, boolean done, int id) {
                        int p = (int) (((float) currSize / (float) totalSize) * 100);
                        if (times >= 64 || p == 100) {
                            times = 0;
                            Message msg = Message.obtain();
                            msg.what = p;
                            handler.sendMessage(msg);
                        }
                        times++;
                    }
                }, new OkHttpManager.ResultCallback() {    //在这里显示回调信息
            @Override
            public void onCallBack(OkHttpManager.State state, String result) {
                if (state == OkHttpManager.State.SUCCESS) {
                    Message msg = Message.obtain();
                    msg.what = OkHttpManager.DOWNLOAD_SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else if (state == OkHttpManager.State.FAILURE) {
                    handler.sendEmptyMessage(OkHttpManager.DOWNLOAD_FAIL);
                } else if (state == OkHttpManager.State.NETWORK_FAILURE) {
                    handler.sendEmptyMessage(OkHttpManager.NETWORK_FAILURE);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitGzdt");
    }
}

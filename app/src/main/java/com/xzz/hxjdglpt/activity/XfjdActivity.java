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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.baidumap.LocationService;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Mqztc;
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
 * 信访监督
 * Created by dbz on 2017/5/24.
 */
@ContentView(R.layout.aty_mqztc_qustion)
public class XfjdActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.mqztc_e_title)
    private EditText edtTitle;
    @ViewInject(R.id.mqztc_e_des)
    private EditText edtDes;
    @ViewInject(R.id.mqztc_commit)
    private Button btnCommit;
    private User user;

    private LocationService locationService;
    @ViewInject(R.id.mqztc_e_adress)
    private TextView tvAddress;
    @ViewInject(R.id.mqztc_e_btn)
    private ImageView startLocation;

    @ViewInject(R.id.mqztc_lay_fj)
    private LinearLayout tvFile;

    private Boolean isGet = false;

    private LayoutInflater mInflater;

    private String pathStr = "";
    private String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText("信访监督");
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        initView();
        initData();
    }

    public void initView() {
        createDialog();
    }

    @Event(value = {R.id.iv_back, R.id.mqztc_commit, R.id.mqztc_e_add}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.mqztc_commit:
                if (TextUtils.isEmpty(edtTitle.getText().toString())) {
                    ToastUtil.show(XfjdActivity.this, R.string.fwmdm_title_null);
                } else if (TextUtils.isEmpty(edtDes.getText().toString())) {
                    ToastUtil.show(XfjdActivity.this, R.string.fwmdm_des_null);
                } else {
                    commit();
                }
                break;
            case R.id.mqztc_e_add:
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
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
                // -----------location config ------------
                locationService = ((BaseApplication) getApplication()).locationService;
                //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity
                // ，都是通过此种方式获取locationservice实例的
                locationService.registerListener(mListener);
                //注册监听
//                locationService.setLocationOption(locationService
// .getDefaultLocationClientOption());
                locationService.setLocationOption(locationService.getOption());
                locationService.start();
                startLocation.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isGet = true;
                        SuccinctProgress.showSuccinctProgress(XfjdActivity.this,
                                "定位中···", SuccinctProgress.THEME_ULTIMATE, false, true);
                    }
                });
                break;
            case 0x0002:
                DialogUtil.createUploadDialog(this, mInflater);
                break;
        }
    }

    private void commit() {
        SuccinctProgress.showSuccinctProgress(XfjdActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
//        String url = ConstantUtil.BASE_URL + "/qfhx/addXfjds?title="+ edtTitle.getText().toString()+"&description="+edtDes.getText().toString()+"&address="+tvAddress.getText().toString()
//                +"&fileName="+filename+"&filePath="+pathStr+"&userid="+user.getUserId()+"&username="+user.getUserName();
        String url= ConstantUtil.BASE_URL + "/qfhx/addXfjds";
        HashMap<String, String> params = new HashMap<>();
        params.put("title", edtTitle.getText().toString());
        params.put("description", edtDes.getText().toString());
        params.put("address", tvAddress.getText().toString());
        params.put("fileName", filename);
        params.put("filePath", pathStr);
        params.put("userid", user.getUserId()+"");
        params.put("username", user.getUserName());
        VolleyRequest.RequestPost(this, url, "mqztc_add", params,new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                Log.e("insert",result.toString());
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("200".equals(resultCode)) {
                        ToastUtil.show(XfjdActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XfjdActivity.this);
                    } else if ("300".equals(resultCode)) {
                        ToastUtil.show(XfjdActivity.this, R.string.commit_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("mqztc_add");
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
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
                    uploadFile(f.getPath());
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
                    ToastUtil.show(XfjdActivity.this, R.string.upload_image_success);
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
                                            psn[i].endsWith(".JPG") || psn[i].endsWith(".PNG") ||
                                            psn[i].endsWith(".jpeg") || psn[i].endsWith(".JPEG")
                                            || psn[i].endsWith(".BMP") || psn[i].endsWith(".bmp")
                                            || psn[i].endsWith(".gif") || psn[i].endsWith(".GIF")) {
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
                                        TextView tv = new TextView(XfjdActivity.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout
                                                .LayoutParams(LinearLayout.LayoutParams
                                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                                .WRAP_CONTENT);
                                        tv.setLayoutParams(lp);
                                        int pxValue = DensityUtil.dip2px(XfjdActivity
                                                .this, 5);
                                        tv.setPadding(pxValue, pxValue, pxValue, pxValue);
                                        tv.setTextColor(getResources().getColor(R.color.title_bg));
                                        tv.setText(psn[i]);
                                        tv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (val.endsWith(".jpg") || val.endsWith("" + ""
                                                        + ".png") || val.endsWith(".JPG") || val
                                                        .endsWith("" + "" + ".PNG") || val
                                                        .endsWith(".jpeg") || val.endsWith("" +
                                                        "" + ".JPEG") || val.endsWith("" + "" +
                                                        "" + ".BMP") || val.endsWith(".bmp") ||
                                                        val.endsWith("" + "" + ".gif") || val
                                                        .endsWith("" + ".GIF")) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("imagesName", values);
                                                    intent.putExtra("index", 0);
                                                    intent.setClass(XfjdActivity.this,
                                                            ShowImageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    ToastUtil.show(XfjdActivity.this, R
                                                            .string.file_tip);
                                                }
                                            }
                                        });
                                        tvFile.addView(tv);
                                    }
                                }
                            }
                            ToastUtil.show(XfjdActivity.this, R.string
                                    .upload_image_success);
                        } else if ("2".equals(resultCode)) {
                            DialogUtil.showTipsDialog(XfjdActivity.this);
                        } else if ("3".equals(resultCode)) {
                            ToastUtil.show(XfjdActivity.this, R.string.upload_image_fail);
                        }
                    }
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(XfjdActivity.this, R.string.upload_image_fail);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(XfjdActivity.this, R.string.net_unuser);
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
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        requestPermission(new String[]{android.Manifest.permission.READ_PHONE_STATE, android
                .Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission
                .ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0001);
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                LogUtil.i(location.getAddrStr());
                if (isGet) {
                    Message msg = new Message();
                    msg.obj = location.getAddrStr();
                    handlerAddress.sendMessage(msg);
                    isGet = false;
                }
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    private Handler handlerAddress = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("insert",msg.obj.toString());
            tvAddress.setText(msg.obj.toString());
            SuccinctProgress.dismiss();
        }
    };
}

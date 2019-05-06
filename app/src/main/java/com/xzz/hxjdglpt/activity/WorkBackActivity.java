package com.xzz.hxjdglpt.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.xzz.hxjdglpt.customview.CustomSearchDialog;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Sxgz;
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

import org.json.JSONArray;
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
 * 任务反馈
 * Created by dbz on 2017/5/26.
 */
@ContentView(R.layout.aty_workback)
public class WorkBackActivity extends BaseActivity implements AdapterView.OnItemSelectedListener,
        View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.wordback_type)
    private AppCompatSpinner spType;
    @ViewInject(R.id.wordback_fj)
    private LinearLayout tvFile;
    @ViewInject(R.id.wordback_commit)
    private Button btnCommit;
    @ViewInject(R.id.workback_des)
    private EditText edtDes;
    @ViewInject(R.id.wordback_title)
    private EditText edtTitle;
    @ViewInject(R.id.wordback_isfinish)
    private AppCompatSpinner isFinish;
    @ViewInject(R.id.workback_content)
    private EditText edtContent;

    private User user;

    private List<Sxgz> sxgzs;

    private ArrayAdapter<String> arr_adapter;

    private String sxgzId;


    private String pathStr = "";
    private String filename = "";

    private LocationService locationService;
    @ViewInject(R.id.wordback_adress)
    private TextView tvAddress;
    @ViewInject(R.id.wordback_btn)
    private ImageView startLocation;

    @ViewInject(R.id.workback_receivor)
    private TextView spreceivor;

    private Boolean isGet = false;

    private LayoutInflater mInflater;

    private ArrayList<User> receivors = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private CustomSearchDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        userDialog = new CustomSearchDialog(this, userList);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(R.string.gzfk);
        spType.setOnItemSelectedListener(this);
        String[] types = getResources().getStringArray(R.array.isfinish);
        //适配器
        ArrayAdapter typeAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, types);
        //设置样式
        typeAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        isFinish.setAdapter(typeAd);
        createDialog();
        edtDes.setOnTouchListener(this);
        edtContent.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.workback_content && canVerticalScroll(edtContent))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.workback_des && canVerticalScroll(edtDes))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向能否够滚动
     *
     * @param editText 须要推断的EditText
     * @return true：能够滚动   false：不能够滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText
                .getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


    public void initData() {
        request();
        loadUser();
    }

    @Event(value = {R.id.iv_back, R.id.workback_fj_add, R.id.wordback_commit, R.id
            .workback_receivor}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.wordback_commit:
                commit();
                break;
            case R.id.workback_fj_add:
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
                break;
            case R.id.workback_receivor:
                receivors.clear();
                showMutilAlertDialog("请选择反馈对象");
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
                locationService.setLocationOption(locationService.getOption());
                locationService.start();
                startLocation.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isGet = true;
                        SuccinctProgress.showSuccinctProgress(WorkBackActivity.this, "定位中···",
                                SuccinctProgress.THEME_ULTIMATE, false, true);
                    }
                });
                break;
            case 0x0002:
                DialogUtil.createUploadDialog(this, mInflater);
                break;
        }
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(WorkBackActivity.this, "数据加载中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/sxgz/querySxgzList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "sxgz_queryList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                sxgzs = gson.fromJson(jsonArray.toString(), new TypeToken<List<Sxgz>>() {
                                }.getType());
                                ArrayList<String> names = new ArrayList<String>();
                                for (Sxgz s : sxgzs) {
                                    names.add(s.getName());
                                }
                                if (sxgzs.size() > 0) {
                                    sxgzId = sxgzs.get(0).getId();
                                }
                                //适配器
                                arr_adapter = new ArrayAdapter<String>(WorkBackActivity.this, android.R
                                        .layout.simple_spinner_item, names);
                                //设置样式
                                arr_adapter.setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                                //加载适配器
                                spType.setAdapter(arr_adapter);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(WorkBackActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(WorkBackActivity.this, R.string.load_fail);
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

    private void loadUser() {
        String url = ConstantUtil.BASE_URL + "/user/queryUsers";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryUserJunior", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<User> list = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<User>>() {
                                        }.getType());
                                userList.addAll(list);
                                if (userDialog.isShowing()) {
                                    userDialog.notifyDataSetChanged();
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(WorkBackActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(WorkBackActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    private void commit() {
        if (userId == null || TextUtils.isEmpty(userId.toString())) {
            ToastUtil.show(this, R.string.choose_back_user);
            return;
        }
        SuccinctProgress.showSuccinctProgress(WorkBackActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/worklog/commitWorklog";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("username", user.getUserName());
        params.put("type", sxgzId == null ? "" : sxgzId);
        params.put("title", edtTitle.getText().toString());
        params.put("question", edtDes.getText().toString());
        params.put("isFinish", isFinish.getSelectedItem().toString());
        params.put("content", edtContent.getText().toString());
        params.put("address", tvAddress.getText().toString());
        params.put("filename", filename);
        params.put("filepath", pathStr);
        params.put("receivors", userId.toString());
        VolleyRequest.RequestPost(this, url, "commitTask", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(WorkBackActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(WorkBackActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(WorkBackActivity.this, R.string.commit_fail);
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
                    ToastUtil.show(WorkBackActivity.this, R.string.upload_image_success);
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
                                        TextView tv = new TextView(WorkBackActivity.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout
                                                .LayoutParams(LinearLayout.LayoutParams
                                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                                .WRAP_CONTENT);
                                        tv.setLayoutParams(lp);
                                        int pxValue = DensityUtil.dip2px(WorkBackActivity.this, 5);
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
                                                    intent.setClass(WorkBackActivity.this,
                                                            ShowImageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    ToastUtil.show(WorkBackActivity.this, R
                                                            .string.file_tip);
                                                }
                                            }
                                        });
                                        tvFile.addView(tv);
                                    }
                                }
                            }
                            ToastUtil.show(WorkBackActivity.this, R.string.upload_image_success);
                        } else if ("2".equals(resultCode)) {
                            DialogUtil.showTipsDialog(WorkBackActivity.this);
                        } else if ("3".equals(resultCode)) {
                            ToastUtil.show(WorkBackActivity.this, R.string.upload_image_fail);
                        }
                    }
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
//                    ToastUtil.show(WorkBackActivity.this, R.string.upload_image_fail);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(WorkBackActivity.this, R.string.net_unuser);
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
        BaseApplication.getRequestQueue().cancelAll("sxgz_queryList");
        BaseApplication.getRequestQueue().cancelAll("commitTask");
        BaseApplication.getRequestQueue().cancelAll("queryUserJunior");
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.wordback_type:
                sxgzId = sxgzs.get(position).getId();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            tvAddress.setText(msg.obj.toString());
            SuccinctProgress.dismiss();
        }
    };


    public void showMutilAlertDialog(String title) {
        userDialog.showView();
        userDialog.setTitle(title);
        userDialog.setClicklistener(new CustomSearchDialog.ClickListenerInterface() {

            @Override
            public void doConfirm() {
                userDialog.dismiss();
                setName();
            }

            @Override
            public void doCancel() {
                userDialog.dismiss();
            }
        });
        userDialog.setItemClicklistener(new CustomSearchDialog.ItemClickListenerInterface() {
            @Override
            public void contactClick(AdapterView<?> parent, View view, int position, long id,
                                     boolean isChecked) {
                LogUtil.i("isChecked=" + isChecked);
                User us = userList.get(position);
                if (isChecked) {
                    // 选中
                    receivors.add(us);
                } else {
                    // 取消选中
                    receivors.remove(us);
                }
            }

            @Override
            public void searchClick(AdapterView<?> parent, View view, int position, long id,
                                    boolean isChecked) {
                User us = userDialog.mSearchList.get(position);
                if (isChecked) {
                    // 选中
                    receivors.add(us);
                } else {
                    // 取消选中
                    receivors.remove(us);
                }
            }
        });
    }

    private StringBuffer names = new StringBuffer();//展示姓名
    private StringBuffer userId = new StringBuffer();//USERID拼接，作为参数传到后台

    private void setName() {
        names.delete(0, names.length());
        userId.delete(0, userId.length());
        for (User u : receivors) {
            names.append(u.getRealName()).append(",");
            userId.append(u.getUserId()).append(",");
        }
        if (receivors.size() > 0) {
            spreceivor.setText(names.toString());
        } else {
            spreceivor.setText(getText(R.string.choose_back_user));
        }
    }

}

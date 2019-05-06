package com.xzz.hxjdglpt.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.CustomSearchGridZrrDialog;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.dateview.JudgeDate;
import com.xzz.hxjdglpt.customview.dateview.ScreenInfo;
import com.xzz.hxjdglpt.customview.dateview.WheelMain;
import com.xzz.hxjdglpt.model.GridZrr;
import com.xzz.hxjdglpt.model.Sxgz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DateUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务分派
 * Created by dbz on 2017/5/26.
 */
@ContentView(R.layout.aty_worksend)
public class WorkSendActivity extends BaseActivity implements AdapterView.OnItemSelectedListener,
        View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.worksend_receivor)
    private TextView spreceivor;

    @ViewInject(R.id.wordsend_type)
    private AppCompatSpinner spType;
    @ViewInject(R.id.worksend_fj)
    private LinearLayout tvFile;
    @ViewInject(R.id.worksend_commit)
    private Button btnCommit;
    @ViewInject(R.id.worksend_endtime)
    private TextView edtTime;
    @ViewInject(R.id.worksend_des)
    private EditText edtDes;
    @ViewInject(R.id.wordsend_title)
    private EditText edtTitle;
    private User user;
    private List<Sxgz> sxgzs;

    private ArrayAdapter<String> arr_adapter;

    private String sxgzId;


    private String pathStr = "";
    private String filename = "";

    private List<GridZrr> userList = new ArrayList<>();

    //    private UserListAdapter adapter;
    private ArrayList<GridZrr> receivors = new ArrayList<>();

    private String endtime = "";

    private LayoutInflater mInflater;

    private String gridId = "";

    private CustomSearchGridZrrDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        gridId = getIntent().getStringExtra("gridId");
        edtDes.setOnTouchListener(this);
        initView();
        initData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.worksend_des && canVerticalScroll(edtDes))) {
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

    public void initView() {
        tvTitle.setText(R.string.rwfp);
        spType.setOnItemSelectedListener(this);
//        adapter = new UserListAdapter(this, userList);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        createDialog();
    }

    public void initData() {
        request();
        if (gridId != null && !TextUtils.isEmpty(gridId)) {
            loadGridUser();
        } else {
            loadUser();
        }
        userDialog = new CustomSearchGridZrrDialog(this, userList);
    }

    private StringBuffer names = new StringBuffer();//展示姓名
    private StringBuffer userId = new StringBuffer();//USERID拼接，作为参数传到后台

    private void setName() {
        names.delete(0, names.length());
        userId.delete(0, userId.length());
        for (GridZrr u : receivors) {
            names.append(u.getRealName()).append(",");
            userId.append(u.getUserId()).append(",");
        }
        if (receivors.size() > 0) {
            spreceivor.setText(names.toString());
        } else {
            spreceivor.setText(getText(R.string.choose_user));
        }
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(WorkSendActivity.this, "数据加载中中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
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
                                arr_adapter = new ArrayAdapter<String>(WorkSendActivity.this, android.R
                                        .layout.simple_spinner_item, names);
                                //设置样式
                                arr_adapter.setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                                //加载适配器
                                spType.setAdapter(arr_adapter);
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(WorkSendActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(WorkSendActivity.this, R.string.load_fail);
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
        String url = ConstantUtil.BASE_URL + "/user/queryUserJuniorFormat";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryUserJunior", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        LogUtil.i(result.toString());
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<GridZrr> list = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<GridZrr>>() {
                                        }.getType());
                                userList.addAll(list);
                                if (userDialog.isShowing()) {
                                    userDialog.notifyDataSetChanged();
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(WorkSendActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(WorkSendActivity.this, R.string.load_fail);
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

    private void loadGridUser() {
        String url = ConstantUtil.BASE_URL + "/user/queryGridZrrByGridId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryUserJunior", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        LogUtil.i(result.toString());
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<GridZrr> list = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<GridZrr>>() {
                                        }.getType());
                                userList.addAll(list);
                                if (userDialog.isShowing()) {
                                    userDialog.notifyDataSetChanged();
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(WorkSendActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(WorkSendActivity.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back, R.id.worksend_commit, R.id.worksend_endtime, R.id
            .worksend_receivor, R.id.worksend_fj_add}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.worksend_commit:
                commit();
                break;
            case R.id.worksend_endtime:
                showBottoPopupWindow();
                break;
            case R.id.worksend_receivor:
                receivors.clear();
                showMutilAlertDialog("请选择接收人");
                break;
            case R.id.worksend_fj_add:
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
        if (userId == null || TextUtils.isEmpty(userId.toString())) {
            ToastUtil.show(this, R.string.choose_user);
            return;
        }
        SuccinctProgress.showSuccinctProgress(WorkSendActivity.this, "数据加载中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/m_task/commitTask";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("username", user.getUserName());
        params.put("type", sxgzId == null ? "" : sxgzId);
        params.put("des", edtDes.getText().toString());
        params.put("title", edtTitle.getText().toString());
        params.put("filename", filename);
        params.put("filepath", pathStr);
        params.put("receiveuser", userId.toString());
        params.put("endtime", endtime);
        VolleyRequest.RequestPost(this, url, "commitTask", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result);
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(WorkSendActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(WorkSendActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(WorkSendActivity.this, R.string.commit_fail);
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
                    ToastUtil.show(WorkSendActivity.this, R.string.upload_image_success);
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
                                        TextView tv = new TextView(WorkSendActivity.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout
                                                .LayoutParams(LinearLayout.LayoutParams
                                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                                .WRAP_CONTENT);
                                        tv.setLayoutParams(lp);
                                        int pxValue = DensityUtil.dip2px(WorkSendActivity.this, 5);
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
                                                    intent.setClass(WorkSendActivity.this,
                                                            ShowImageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    ToastUtil.show(WorkSendActivity.this, R
                                                            .string.file_tip);
                                                }
                                            }
                                        });
                                        tvFile.addView(tv);
                                    }
                                }
                            }
                            ToastUtil.show(WorkSendActivity.this, R.string.upload_image_success);
                        } else if ("2".equals(resultCode)) {
                            DialogUtil.showTipsDialog(WorkSendActivity.this);
                        } else if ("3".equals(resultCode)) {
                            ToastUtil.show(WorkSendActivity.this, R.string.upload_image_fail);
                        }
                    }
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(WorkSendActivity.this, R.string.upload_image_fail);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(WorkSendActivity.this, R.string.net_unuser);
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
        BaseApplication.getRequestQueue().cancelAll("queryUserJunior");
        BaseApplication.getRequestQueue().cancelAll("sxgz_queryList");
        BaseApplication.getRequestQueue().cancelAll("commitTask");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.wordsend_type:
                sxgzId = sxgzs.get(position).getId();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private WheelMain wheelMainDate;
    private String beginTime;

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void showBottoPopupWindow() {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_popup_window, null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int) (width * 0.8), ActionBar
                .LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtil.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day, hours, minute);
        final String currentTime = wheelMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(edtTime, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择截止时间");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                try {
                    Date begin = dateFormat.parse(currentTime);
//                    Date end = dateFormat.parse(beginTime);
                    edtTime.setText(DateUtil.formateStringH(beginTime, DateUtil.yyyyMMddHHmm));
                    endtime = DateUtil.formateStringH(beginTime, DateUtil.yyyyMMddHHmm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 将list转为string数组
     */
    public String[] getRealNameList() {
        String[] names = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            GridZrr u = userList.get(i);
            if (u.getRealName() == null) {
                StringBuffer sb = new StringBuffer();
                sb.append(u.getUserName()).append("（").append(u.getvName()).append("，").append(u
                        .getGridId()).append("）");
                names[i] = sb.toString();
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(u.getRealName()).append("（").append(u.getvName()).append("，").append(u
                        .getGridId()).append("）");
                names[i] = sb.toString();
            }
        }
        return names;
    }

    public void showMutilAlertDialog(String title) {
        userDialog.showView();
        userDialog.setTitle(title);
        userDialog.setClicklistener(new CustomSearchGridZrrDialog.ClickListenerInterface() {

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
        userDialog.setItemClicklistener(new CustomSearchGridZrrDialog.ItemClickListenerInterface() {
            @Override
            public void contactClick(AdapterView<?> parent, View view, int position, long id,
                                     boolean isChecked) {
                GridZrr us = userList.get(position);
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
                GridZrr us = userDialog.mSearchList.get(position);
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


}

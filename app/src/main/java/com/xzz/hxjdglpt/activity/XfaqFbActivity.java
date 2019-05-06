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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.baidumap.LocationService;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.dateview.JudgeDate;
import com.xzz.hxjdglpt.customview.dateview.ScreenInfo;
import com.xzz.hxjdglpt.customview.dateview.WheelMain;
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
 * Created by dbz on 2017/5/24.
 */
@ContentView(R.layout.aty_xfaq_fb)
public class XfaqFbActivity extends BaseActivity implements View.OnTouchListener {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.xfaq_lb)
    private AppCompatSpinner splx;//类别

    @ViewInject(R.id.xfaq_ddld)
    private EditText edtDdld;//带队领导
    @ViewInject(R.id.xfaq_ddldphone)
    private EditText edtDdldPhone;//带队领导电话
    @ViewInject(R.id.xfaq_fb_commit)
    private Button btnCommit;
    @ViewInject(R.id.xfaq_cy)
    private EditText edtCy;//检查组成员
    @ViewInject(R.id.xfaq_ptjcr)
    private EditText edtPtjcy;//陪同检查人
    @ViewInject(R.id.xfaq_ptjcrphone)
    private EditText edtPtPhone;//陪同检查人电话
    @ViewInject(R.id.xfaq_rb1)
    private RadioButton rb1;
    @ViewInject(R.id.xfaq_rb2)
    private RadioButton rb2;
    @ViewInject(R.id.xfaq_rb3)
    private RadioButton rb3;
    @ViewInject(R.id.xfaq_rb4)
    private RadioButton rb4;
    @ViewInject(R.id.xfaq_des)
    private EditText edtContent;//检查内容
    @ViewInject(R.id.xfaq_question_des)
    private EditText edtQuestion;//存在问题
    @ViewInject(R.id.xfaq_result)
    private AppCompatSpinner spResult;//处理措施
    @ViewInject(R.id.xfaq_time)
    private TextView mTime;

    private User user;
    private int type;

    private String pId;//企业ID

    private String gridId;//网格ID

    @ViewInject(R.id.xfaq_fj)
    private LinearLayout tvFile;
    private LayoutInflater mInflater;

    private String pathStr = "";
    private String filename = "";

    private String[] cljlTypes;//类型

    private String[] jclbTypes;//检查类别

    private ArrayAdapter<String> cljl_adapter;
    private ArrayAdapter<String> jclb_adapter;

    private String cType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        pId = getIntent().getStringExtra("pId");
        LogUtil.i("====================" + pId);
        gridId = getIntent().getStringExtra("gridId");
        type = getIntent().getIntExtra("type", 0);
        cType = getIntent().getStringExtra("cType");
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        edtQuestion.setOnTouchListener(this);
        edtCy.setOnTouchListener(this);
        edtContent.setOnTouchListener(this);
        initView();
        initData();
        createDialog();
    }

    public void initView() {
        if (type == 1) {
            tvTitle.setText("消防安全检查记录");
        } else if (type == 2) {
            tvTitle.setText("安全生产检查记录");
        }
        cljlTypes = getResources().getStringArray(R.array.cljl_list);
        jclbTypes = getResources().getStringArray(R.array.jclb_list);
        cljl_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                cljlTypes);
        //设置样式
        cljl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spResult.setAdapter(cljl_adapter);

        jclb_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                jclbTypes);
        jclb_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        splx.setAdapter(jclb_adapter);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.xfaq_cy && canVerticalScroll(edtCy))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.xfaq_des && canVerticalScroll(edtContent))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.xfaq_question_des && canVerticalScroll(edtQuestion))) {
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

    @Event(value = {R.id.iv_back, R.id.xfaq_fb_commit, R.id.xfaq_fj_add, R.id.xfaq_time}, type =
            View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.xfaq_fb_commit:
//                if (TextUtils.isEmpty(edtTitle.getText().toString())) {
//                    ToastUtil.show(XfaqFbActivity.this, R.string.fwmdm_title_null);
//                } else if (TextUtils.isEmpty(edtDes.getText().toString())) {
//                    ToastUtil.show(XfaqFbActivity.this, "内容为空");
//                } else {
                commit();
//                }
                break;
            case R.id.xfaq_fj_add:
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                break;
            case R.id.xfaq_time:
                showBottoPopupWindow(true);
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
        int c = 0;//处理措施
        int j = 0;//检查类别
        for (int i = 0; i < cljlTypes.length; i++) {
            if (cljlTypes[i].equals(spResult.getSelectedItem().toString())) {
                c = i;
                break;
            }
        }
        for (int i = 0; i < jclbTypes.length; i++) {
            if (jclbTypes[i].equals(splx.getSelectedItem().toString())) {
                j = i;
                break;
            }
        }
        SuccinctProgress.showSuccinctProgress(XfaqFbActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xfaqjc/commitXfaqjc";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("type", String.valueOf(type));
        if (pId != null) params.put("pId", pId);
        if (gridId != null) params.put("gridId", gridId);
        params.put("cljl", String.valueOf(c + 1));
        params.put("content", edtContent.getText().toString());
        params.put("filename", filename);
        params.put("filepath", pathStr);
        params.put("question", edtQuestion.getText().toString());
        if (rb3.isChecked()) params.put("isSj", "1");
        if (rb4.isChecked()) params.put("isSj", "0");
        if (rb1.isChecked()) params.put("isZj", "1");
        if (rb2.isChecked()) params.put("isZj", "0");
        params.put("jccy", edtCy.getText().toString());
        params.put("jcetime", eTime);
        params.put("jcstime", sTime);
        params.put("ddld", edtDdld.getText().toString());
        params.put("ldphone", edtDdldPhone.getText().toString());
        params.put("jclb", String.valueOf(j + 1));
        params.put("ptjcr", edtPtjcy.getText().toString());
        params.put("ptjcrPhone", edtPtPhone.getText().toString());
        params.put("bz", "");
        if (cType != null) params.put("ctype", cType);
        VolleyRequest.RequestPost(this, url, "commitXfaqjc", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(XfaqFbActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XfaqFbActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XfaqFbActivity.this, R.string.commit_fail);
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
                    ToastUtil.show(XfaqFbActivity.this, R.string.upload_image_success);
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
                                        TextView tv = new TextView(XfaqFbActivity.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout
                                                .LayoutParams(LinearLayout.LayoutParams
                                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                                .WRAP_CONTENT);
                                        tv.setLayoutParams(lp);
                                        int pxValue = DensityUtil.dip2px(XfaqFbActivity.this, 5);
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
                                                    intent.setClass(XfaqFbActivity.this,
                                                            ShowImageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    ToastUtil.show(XfaqFbActivity.this, R.string
                                                            .file_tip);
                                                }
                                            }
                                        });
                                        tvFile.addView(tv);
                                    }
                                }
                            }
                            ToastUtil.show(XfaqFbActivity.this, R.string.upload_image_success);
                        } else if ("2".equals(resultCode)) {
                            DialogUtil.showTipsDialog(XfaqFbActivity.this);
                        } else if ("3".equals(resultCode)) {
                            ToastUtil.show(XfaqFbActivity.this, R.string.upload_image_fail);
                        }
                    }
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(XfaqFbActivity.this, R.string.upload_image_fail);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(XfaqFbActivity.this, R.string.net_unuser);
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
        BaseApplication.getRequestQueue().cancelAll("commitXfaqjc");
    }


    private WheelMain wheelMainDate;
    private StringBuffer sbTime;


    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    private String sTime = "";
    private String eTime = "";

    public void showBottoPopupWindow(final boolean isF) {
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
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(mTime, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        if (isF) {
            tv_pop_title.setText("选择开始检查时间");
        } else {
            tv_pop_title.setText("选择结束检查时间");
        }
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
                if (isF) {
                    sbTime = new StringBuffer();
                    String bTime = wheelMainDate.getTime().toString();
                    sTime = DateUtil.formateStringH(bTime, DateUtil.yyyyMMddHHmmss);
                    sbTime.append(DateUtil.formateStringH(bTime, DateUtil.yyyyMMddHHmmss));
                    showBottoPopupWindow(false);
                } else {
                    String bTime = wheelMainDate.getTime().toString();
                    eTime = DateUtil.formateStringH(bTime, DateUtil.yyyyMMddHHmmss);
                    sbTime.append("至").append(DateUtil.formateStringH(bTime, DateUtil
                            .yyyyMMddHHmmss));
                    mTime.setText(sbTime.toString());
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
}

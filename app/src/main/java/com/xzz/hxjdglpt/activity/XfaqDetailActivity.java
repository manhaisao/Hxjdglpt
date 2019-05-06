package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Xfaqjc;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.superfileview.FileDisplayActivity;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
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


/**
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_xfaq_detail)
public class XfaqDetailActivity extends BaseActivity implements View.OnTouchListener {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.xfaq_lb)
    private TextView tvLb;
    @ViewInject(R.id.xfaq_rq)
    private TextView tvRq;
    @ViewInject(R.id.xfaq_ddld)
    private TextView tvDdld;
    @ViewInject(R.id.xfaq_ddldphone)
    private TextView tvDdldPhone;
    @ViewInject(R.id.xfaq_jczcy)
    private TextView tvJczcy;
    @ViewInject(R.id.xfaq_ptjcr)
    private TextView tvPtjcr;
    @ViewInject(R.id.xfaq_ptjcrphone)
    private TextView tvPtjcrPhone;

    @ViewInject(R.id.xfaq_rb1)
    private RadioButton rb1;
    @ViewInject(R.id.xfaq_rb2)
    private RadioButton rb2;
    @ViewInject(R.id.xfaq_rb3)
    private RadioButton rb3;
    @ViewInject(R.id.xfaq_rb4)
    private RadioButton rb4;

    @ViewInject(R.id.xfaq_content)
    private TextView tvContent;
    @ViewInject(R.id.xfaq_question_des)
    private TextView tvQuestion;
    @ViewInject(R.id.xfaq_cs_des)
    private TextView tvCs;

    private Xfaqjc xfaqjc;
    private int type = 0;
    private User user;
    private List<Role> roles;

    @ViewInject(R.id.xfaq_fj)
    private LinearLayout lay_fj;

    private String[] cljlTypes;//类型

    private String[] jclbTypes;//检查类别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        type = getIntent().getIntExtra("type", 0);
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        tvContent.setOnTouchListener(this);
        tvQuestion.setOnTouchListener(this);
        tvJczcy.setOnTouchListener(this);
        initView();
        initData();
        createDialog();
    }

    public void initView() {
        if (type == 1 && isXfaq()) {
            tvRight.setText("删除");
            tvRight.setVisibility(View.VISIBLE);
        } else if (type == 2 && isScaq()) {
            tvRight.setText("删除");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        if (type == 1) {
            tvTitle.setText("消防安全检查");
        } else {
            tvTitle.setText("安全生产检查");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //通知父控件不要干扰
            v.getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //通知父控件不要干扰
            v.getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return false;
    }

    public void initData() {
        cljlTypes = getResources().getStringArray(R.array.cljl_list);
        jclbTypes = getResources().getStringArray(R.array.jclb_list);
        xfaqjc = getIntent().getParcelableExtra("xfaqjc");
        tvLb.setText(jclbTypes[xfaqjc.getJclb() - 1]);
        tvRq.setText(xfaqjc.getJcstime() + "-" + xfaqjc.getJcetime());
        tvContent.setText(xfaqjc.getContent());
        tvQuestion.setText(xfaqjc.getQuestion());
        tvDdld.setText(xfaqjc.getDdld());
        tvDdldPhone.setText(xfaqjc.getLdphone());
        tvCs.setText(cljlTypes[xfaqjc.getCljl() - 1]);
        tvJczcy.setText(xfaqjc.getJccy());
        tvPtjcr.setText(xfaqjc.getPtjcr());
        tvPtjcrPhone.setText(xfaqjc.getPtjcrPhone());
        if (xfaqjc.getIsZj() == 1) {
            rb1.setChecked(true);
            rb2.setChecked(false);
        } else if (xfaqjc.getIsZj() == 0) {
            rb1.setChecked(false);
            rb2.setChecked(true);
        }

        if (xfaqjc.getIsSj() == 1) {
            rb3.setChecked(true);
            rb4.setChecked(false);
        } else if (xfaqjc.getIsSj() == 0) {
            rb3.setChecked(false);
            rb4.setChecked(true);
        }

        initGridFj();
    }


    private boolean isXfaq() {
        for (Role r : roles) {
            if ("4253".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isScaq() {
        for (Role r : roles) {
            if ("4252".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }


    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final Dialog dialog = new Dialog(XfaqDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                TextView tvContent = (TextView) view.findViewById(R.id.dialog_content);
                Button butOk = (Button) view.findViewById(R.id.dialog_ok);
                Button butCancle = (Button) view.findViewById(R.id.dialog_cancel);
                butOk.setText("确认");
                butCancle.setText("取消");
                butCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
                tvContent.setText("确认删除?");
                butOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        delData();
                    }
                });
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = dm.widthPixels - 50;
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                break;
        }
    }

    private void delData() {
        SuccinctProgress.showSuccinctProgress(XfaqDetailActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = "";
        HashMap<String, String> params = new HashMap<>();
        url = ConstantUtil.BASE_URL + "/xfaqjc/delXfaqjc";
        params.put("id", String.valueOf(xfaqjc.getId()));
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "delXfaqjc", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(XfaqDetailActivity.this, R.string.del_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(XfaqDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(XfaqDetailActivity.this, R.string.del_fail);
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


    /**
     * 动态添加附件
     */
    private void initGridFj() {
        lay_fj.removeAllViews();
        if (!TextUtils.isEmpty(xfaqjc.getFilepath())) {
            final String[] path = xfaqjc.getFilepath().split(",");
            String[] fileName = xfaqjc.getFilename().split(",");
            if (path.length == fileName.length) {
                //去除非图片文件路径
                List<String> list = new ArrayList<>();
                for (int i = 0; i < path.length; i++) {
                    if (path[i] != null && !TextUtils.isEmpty(path[i])) {
                        if (path[i].endsWith(".jpg") || path[i].endsWith(".png") || path[i]
                                .endsWith(".JPG") || path[i].endsWith("" + "" + ".PNG") ||
                                path[i].endsWith(".jpeg") || path[i].endsWith(".JPEG") || path[i]
                                .endsWith("" + ".BMP") || path[i].endsWith(".bmp") || path[i]
                                .endsWith("" + "" + ".gif") || path[i].endsWith("" + ".GIF")) {
                            list.add(path[i]);
                        }
                    }
                }
                final String[] values = (String[]) list.toArray(new String[list.size()]);

                for (int i = 0; i < path.length; i++) {
                    if (!TextUtils.isEmpty(fileName[i]) || !TextUtils.isEmpty(path[i])) {
                        final String fName = fileName[i];
                        final String fPath = path[i];
                        TextView tv = new TextView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tv.setLayoutParams(lp);
                        tv.setText(fileName[i]);
                        int pxValue = DensityUtil.dip2px(XfaqDetailActivity.this, 5);
                        tv.setPadding(pxValue, pxValue, pxValue, pxValue);
                        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                        tv.setTextColor(getResources().getColor(R.color.title_bg));
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fName.endsWith(".jpg") || fName.endsWith(".png") || fName
                                        .endsWith(".JPG") || fName.endsWith(".PNG") || fName
                                        .endsWith(".jpeg") || fName.endsWith(".JPEG") || fName
                                        .endsWith(".BMP") || fName.endsWith(".bmp") || fName
                                        .endsWith(".gif") || fName.endsWith(".GIF")) {
                                    Intent intent = new Intent();
                                    intent.putExtra("imagesName", values);
                                    intent.putExtra("index", 0);
                                    intent.setClass(XfaqDetailActivity.this, ShowImageActivity
                                            .class);
                                    startActivity(intent);
                                } else {
                                    File file = new File(ConstantUtil.BASE_PATH + fName);
                                    if (file.exists()) {
                                        Intent intent = new Intent(XfaqDetailActivity.this,
                                                FileDisplayActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("path", ConstantUtil.BASE_PATH +
                                                fName);
                                        bundle.putSerializable("fileName", fName);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        final Dialog dialog = new Dialog(XfaqDetailActivity
                                                .this);
                                        View view = getLayoutInflater().inflate(R.layout
                                                .tip_dialog, null);

                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(view);
                                        //TextView tvTip = (TextView) view.findViewById(R.id
                                        // .tip_title);
                                        TextView tvCon = (TextView) view.findViewById(R.id
                                                .tip_content);
                                        //tvTip.setText(NoticeDetailActivity.this.getText(R.string
                                        // .zxtz_detail_tip));
                                        tvCon.setText(XfaqDetailActivity.this.getText(R.string
                                                .zxtz_detail_content));
                                        Button butOk = (Button) view.findViewById(R.id.tip_ok);
                                        Button butCancle = (Button) view.findViewById(R.id
                                                .tip_cancel);
                                        butCancle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
                                                dialog.dismiss();
                                            }
                                        });
                                        butOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                String url = ConstantUtil.FILE_DOWNLOAD_URL + fPath;
                                                downloadFile(url, fName);
                                            }
                                        });
                                        DisplayMetrics dm = new DisplayMetrics();
                                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        WindowManager.LayoutParams lp = dialog.getWindow()
                                                .getAttributes();
                                        lp.width = dm.widthPixels - 50;
                                        dialog.getWindow().setAttributes(lp);
                                        dialog.show();
                                    }
                                }
                            }
                        });
                        lay_fj.addView(tv);
                    }
                }
            }
        } else {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText(getText(R.string.no_fj));
            int pxValue = DensityUtil.dip2px(XfaqDetailActivity.this, 5);
            tv.setPadding(pxValue, pxValue, pxValue, pxValue);
            tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv.setTextColor(getResources().getColor(R.color.title_bg));
            lay_fj.addView(tv);
        }
    }


    private ProgressDialog dialog = null;

    private void createDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("下载进度提示");
        dialog.setMax(100);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OkHttpManager.DOWNLOAD_SUCCESS:
                    ToastUtil.show(XfaqDetailActivity.this, R.string.download_success);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(XfaqDetailActivity.this, R.string.download_failed);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(XfaqDetailActivity.this, R.string.net_unuser);
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

    private void downloadFile(final String url, String fileName) {
        dialog.show();
        OkHttpManager.download(url, ConstantUtil.BASE_PATH, fileName, new OkHttpManager
                .ProgressListener() {
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
        }, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(OkHttpManager.State state, String result) {
                if (state == OkHttpManager.State.SUCCESS) {
                    handler.sendEmptyMessage(OkHttpManager.DOWNLOAD_SUCCESS);
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
        BaseApplication.getRequestQueue().cancelAll("delXfaqjc");
    }

}

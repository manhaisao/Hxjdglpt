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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Yzyh;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.superfileview.FileDisplayActivity;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.FljktDetail;
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
import java.util.List;

/**
 * 最新通知详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_gzdt_detail)
public class FlfwjktDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;
    @ViewInject(R.id.gzdt_detail_time)
    private TextView tvTime;
    @ViewInject(R.id.gzdt_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.party_detail_content)
    private TextView tvContent;
    private FljktDetail fljktDetail;
    private User user;
    private List<Role> roles;

    @ViewInject(R.id.gzdt_detail_fj)
    private LinearLayout lay_fj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        fljktDetail =  getIntent().getParcelableExtra("pfwsp");
        initView();
        initData();
        createDialog();
    }

    public void initView() {
        if (isContain()) {
            tvRight.setText("删除");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        tvTitle.setText(fljktDetail.getTitle());
    }


    public void initData() {
        String label = "创建人：" + fljktDetail.getFbrName() + " \t创建时间：" + fljktDetail.getFbsj();
        tvTime.setText(label);
        tvNoTitle.setText(fljktDetail.getTitle());
        tvContent.setText(fljktDetail.getContent());
        initFj();
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4261".equals(r.getRoleId()) || fljktDetail.getFbr().equals(user.getUserId())) {
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
                final Dialog dialog = new Dialog(FlfwjktDetailActivity.this);
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
        SuccinctProgress.showSuccinctProgress(FlfwjktDetailActivity.this, "数据删除中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = "";
        url = ConstantUtil.BASE_URL + "/lawDetails/"+fljktDetail.getId();
        VolleyRequest.RequestDelete(this, url, "deleteGzdt", new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("code");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("0".equals(resultCode)) {
                        ToastUtil.show(FlfwjktDetailActivity.this, R.string.del_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(FlfwjktDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(FlfwjktDetailActivity.this, R.string.del_fail);
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
    private void initFj() {
        lay_fj.removeAllViews();
        if (!TextUtils.isEmpty(fljktDetail.getFilepath())) {
            final String[] path = fljktDetail.getFilepath().split(",");
            String[] fileName = fljktDetail.getFilename().split(",");
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
                        int pxValue = DensityUtil.dip2px(FlfwjktDetailActivity.this, 5);
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
                                    intent.setClass(FlfwjktDetailActivity.this, ShowImageActivity
                                            .class);
                                    startActivity(intent);
                                } else {
                                    File file = new File(ConstantUtil.BASE_PATH + fName);
                                    if (file.exists()) {
                                        Intent intent = new Intent(FlfwjktDetailActivity.this,
                                                FileDisplayActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("path", ConstantUtil.BASE_PATH +
                                                fName);
                                        bundle.putSerializable("fileName", fName);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        final Dialog dialog = new Dialog(FlfwjktDetailActivity.this);
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
                                        tvCon.setText(FlfwjktDetailActivity.this.getText(R.string
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
            int pxValue = DensityUtil.dip2px(FlfwjktDetailActivity.this, 5);
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
                    ToastUtil.show(FlfwjktDetailActivity.this, R.string.download_success);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(FlfwjktDetailActivity.this, R.string.download_failed);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(FlfwjktDetailActivity.this, R.string.net_unuser);
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
        BaseApplication.getRequestQueue().cancelAll("deleteGzdt");
    }

}

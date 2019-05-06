package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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

import com.xzz.hxjdglpt.model.Task;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * 最新通知详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_task_detail)
public class TaskDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;

    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;
    @ViewInject(R.id.task_detail_label)
    private TextView tvLabel;
    @ViewInject(R.id.task_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.task_detail_content)
    private TextView tvContent;
    private Task task;
    private String status;
    @ViewInject(R.id.task_detail_fj)
    private LinearLayout lay_fj;
    @ViewInject(R.id.task_detail_fc)
    private TextView tvfj;
    private ProgressDialog progressDialog;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        task = getIntent().getParcelableExtra("task");
        status = getIntent().getStringExtra("status");
        initView();
        initData();
        initFj();
    }

    public void initView() {
        if ("1".equals(status)) {
            tvTitle.setText(getText(R.string.dbtask));
        } else {
            tvTitle.setText(getText(R.string.ybtask));
            tvRight.setText(getText(R.string.feedback));
        }
        tvNoTitle.setText(task.getName());
        StringBuffer sb = new StringBuffer();
        sb.append("发布人：").append(task.getCreateuser()).append("\t\t开始时间：").append(task
                .getCreatetime());
        tvLabel.setText(sb.toString());
        tvContent.setText(task.getDescription());
    }


    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.setClass(TaskDetailActivity.this, ScheduleBackActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 动态添加附件
     */
    private void initFj() {
        if (!TextUtils.isEmpty(task.getFilepath())) {
            String[] path = task.getFilepath().split(",");
            String[] fileName = task.getFilename().split(",");
            if (path.length == fileName.length) {
                for (int i = 0; i < path.length; i++) {
                    if (!TextUtils.isEmpty(fileName[i]) || !TextUtils.isEmpty(path[i])) {
                        final String fName = fileName[i];
                        TextView tv = new TextView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tv.setLayoutParams(lp);
                        tv.setText(fileName[i]);
                        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                        tv.setTextColor(getResources().getColor(R.color.blue));
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog = new Dialog(TaskDetailActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.tip_dialog, null);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(view);
                                //TextView tvTip = (TextView) view.findViewById(R.id.tip_title);
                                TextView tvCon = (TextView) view.findViewById(R.id.tip_content);
                                //tvTip.setText(NoticeDetailActivity.this.getText(R.string
                                // .zxtz_detail_tip));
                                tvCon.setText(TaskDetailActivity.this.getText(R.string
                                        .zxtz_detail_content));
                                Button butOk = (Button) view.findViewById(R.id.tip_ok);
                                Button butCancle = (Button) view.findViewById(R.id.tip_cancel);
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
                                        String url = ConstantUtil.FILE_DOWNLOAD_URL + fName;
                                        String path = ConstantUtil.BASE_PATH + fName;
                                        downloadFile(url, path);
                                    }
                                });
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                                lp.width = dm.widthPixels - 50;
                                dialog.getWindow().setAttributes(lp);
                                dialog.show();

                            }
                        });
                        lay_fj.addView(tv);
                    }
                }
            }
        } else {
            tvfj.setVisibility(View.GONE);
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param path
     */
    private void downloadFile(final String url, String path) {
        progressDialog = new ProgressDialog(this);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                LogUtil.i("onWaiting");
            }

            @Override
            public void onStarted() {
                LogUtil.i("onStarted");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.i("onLoading");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage(getString(R.string.downloading));
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                LogUtil.i("onSuccess");
                ToastUtil.show(TaskDetailActivity.this, R.string.download_success);
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.i("onError");
                ex.printStackTrace();
                ToastUtil.show(TaskDetailActivity.this, R.string.version_download_fail);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.i("onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.i("onFinished");
            }
        });
    }
}

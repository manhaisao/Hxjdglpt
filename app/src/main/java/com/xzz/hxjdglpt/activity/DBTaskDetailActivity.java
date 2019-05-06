package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.model.Sxgz;
import com.xzz.hxjdglpt.model.Task;
import com.xzz.hxjdglpt.model.TaskBack;
import com.xzz.hxjdglpt.model.User;
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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;


/**
 * 点击通知栏任务派发详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_db_task_detail)
public class DBTaskDetailActivity extends BaseActivity implements View.OnTouchListener {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;

    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.worksend_db_detail_type)
    private TextView tvType;
    @ViewInject(R.id.worksend_db_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.worksend_db_detail_des)
    private TextView tvDes;
    @ViewInject(R.id.worksend_db_detail_receivor)
    private TextView tvRe;
    @ViewInject(R.id.worksend_db_detail_fj)
    private LinearLayout tvFj;

    @ViewInject(R.id.worksend_db_detail_fkfj)
    private LinearLayout tvFkFj;
    @ViewInject(R.id.worksend_db_detail_fkcontent)
    private TextView tvContent;

    @ViewInject(R.id.worksend_db_detail_address)
    private TextView tvAddress;

    private Task t;
    private User user;

    private String taskId = "";

    private TaskBack tb;

    private ProgressDialog progressDialog;
//    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        Intent intent = getIntent();
        if (null != intent) {
//            Bundle bundle = getIntent().getExtras();
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//            if (content.contains("编号:")) {
//                String s1 = content.replaceAll(".*[^\\d](?=(\\d+))", "");
//            }
        }
        initView();
        initData();
        tvContent.setOnTouchListener(this);
        tvDes.setOnTouchListener(this);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFk();
    }

    public void initData() {
        queryTask();
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

    private void queryTask() {
        String url = ConstantUtil.BASE_URL + "/m_task/queryTaskById";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", taskId);
        VolleyRequest.RequestPost(this, url, "queryTaskById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        LogUtil.i(jsonObject.toString());
                        Gson gson = new Gson();
                        t = (Task) gson.fromJson(jsonObject.toString(), Task.class);
                        tvNoTitle.setText(t.getName());
                        tvDes.setText(t.getDescription());
                        loadData(t);
                        loadUser(t);
                        initFj(t);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DBTaskDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DBTaskDetailActivity.this, R.string.load_fail);
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

    public void initView() {
        tvRight.setVisibility(View.GONE);
        tvTitle.setText(getText(R.string.dbtask));
        tvRight.setText(getText(R.string.feedback));
    }

    private void loadData(final Task task) {
        String url = ConstantUtil.BASE_URL + "/sxgz/queryById";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", task.getType());
        VolleyRequest.RequestPost(this, url, "queryById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        LogUtil.i(jsonObject.toString());
                        Gson gson = new Gson();
                        Sxgz sxgz = (Sxgz) gson.fromJson(jsonObject.toString(), Sxgz.class);
                        tvNoTitle.setText(task.getName());
                        tvType.setText(sxgz.getName());
                        tvDes.setText(task.getDescription());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DBTaskDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DBTaskDetailActivity.this, R.string.load_fail);
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

    private void loadUser(Task task) {
        String url = ConstantUtil.BASE_URL + "/user/queryUserById";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", task.getReceiveuser());
        VolleyRequest.RequestPost(this, url, "queryUserById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        LogUtil.i(jsonObject.toString());
                        Gson gson = new Gson();
                        User u = (User) gson.fromJson(jsonObject.toString(), User.class);
                        tvRe.setText(u.getUserName());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DBTaskDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DBTaskDetailActivity.this, R.string.load_fail);
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

    private void loadFk() {
        String url = ConstantUtil.BASE_URL + "/m_taskback/queryTaskBackByTaskId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("taskId", taskId);
        VolleyRequest.RequestPost(this, url, "queryTaskBackByTaskId", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    LogUtil.i(resultCode);
                    if ("1".equals(resultCode)) {
                        try {
                            JSONObject jsonObject = result.getJSONObject("data");
                            LogUtil.i(jsonObject.toString());
                            Gson gson = new Gson();
                            tb = (TaskBack) gson.fromJson(jsonObject.toString(), TaskBack.class);
                            tvRight.setVisibility(View.GONE);
                            tvContent.setText(tb.getFknr());
                            initFkFj();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DBTaskDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DBTaskDetailActivity.this, R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                        tvRight.setVisibility(View.VISIBLE);
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

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryById");
        BaseApplication.getRequestQueue().cancelAll("updateTask");
        BaseApplication.getRequestQueue().cancelAll("queryUserById");
        BaseApplication.getRequestQueue().cancelAll("queryTaskBackByTaskId");
        BaseApplication.getRequestQueue().cancelAll("queryTaskById");
    }

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.setClass(DBTaskDetailActivity.this, ScheduleBackActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("task", t);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * 动态添加附件
     */
    private void initFj(Task task) {
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
                                final Dialog dialog = new Dialog(DBTaskDetailActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.tip_dialog, null);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(view);
                                //TextView tvTip = (TextView) view.findViewById(R.id.tip_title);
                                TextView tvCon = (TextView) view.findViewById(R.id.tip_content);
                                //tvTip.setText(NoticeDetailActivity.this.getText(R.string
                                // .zxtz_detail_tip));
                                tvCon.setText(DBTaskDetailActivity.this.getText(R.string
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
                        tvFj.addView(tv);
                    }
                }
            }
        }
    }

    /**
     * 动态添加附件
     */
    private void initFkFj() {
        if (!TextUtils.isEmpty(tb.getFilePath())) {
            String[] path = tb.getFilePath().split(",");
            String[] fileName = tb.getFileName().split(",");
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
                                final Dialog dialog = new Dialog(DBTaskDetailActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.tip_dialog, null);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(view);
                                TextView tvCon = (TextView) view.findViewById(R.id.tip_content);
                                tvCon.setText(DBTaskDetailActivity.this.getText(R.string
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
                        tvFkFj.addView(tv);
                    }
                }
            }
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
                ToastUtil.show(DBTaskDetailActivity.this, R.string.download_success);
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.i("onError");
                ex.printStackTrace();
                ToastUtil.show(DBTaskDetailActivity.this, R.string.version_download_fail);
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

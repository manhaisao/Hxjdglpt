package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Sxgz;
import com.xzz.hxjdglpt.model.Task;
import com.xzz.hxjdglpt.model.TaskBack;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zf;
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

/**
 * 任务派发详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_task_detail_new)
public class TaskDetailNewActivity extends BaseActivity implements View.OnTouchListener {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;

    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;

    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.worksend_detail_type)
    private TextView tvType;
    @ViewInject(R.id.worksend_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.worksend_detail_des)
    private TextView tvDes;
    @ViewInject(R.id.worksend_detail_receivor)
    private TextView tvRe;
    @ViewInject(R.id.worksend_detail_fj)
    private LinearLayout tvFj;
    @ViewInject(R.id.worksend_detail_commit)
    private Button btnCommit;
    @ViewInject(R.id.worksend_detail_fbr)
    private TextView tvFbr;
    @ViewInject(R.id.worksend_detail_ht)
    private Button btnHt;

    @ViewInject(R.id.worksend_detail_endtime)
    private TextView tvTime;

    @ViewInject(R.id.worksend_detail_back_thReason)
    private LinearLayout mReason;
    @ViewInject(R.id.worksend_detail_thReason)
    private EditText tvReason;

    @ViewInject(R.id.worksend_detail_back_zf)
    private LinearLayout mZfLay;

    @ViewInject(R.id.worksend_detail_back_fk)
    private LinearLayout mFkLay;


    @ViewInject(R.id.worksend_detail_zf)
    private Button btnZf;

    private Task task;
    private String status;
    private User user;

    private List<TaskBack> tb = new ArrayList<>();

    private List<Zf> zfs = new ArrayList<>();

    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        task = getIntent().getParcelableExtra("task");
        status = getIntent().getStringExtra("status");
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        tvDes.setOnTouchListener(this);
        initView();
        initData();
        initFj();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFk();
//        loadZf();
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

    public void initView() {
        tvRight.setVisibility(View.GONE);
        if ("1".equals(status)) {
            tvTitle.setText(getText(R.string.ybtask));
        } else if ("2".equals(status)) {
            tvTitle.setText(getText(R.string.dbtask));
            tvRight.setText(getText(R.string.feedback));
        } else if ("3".equals(status)) {
            tvTitle.setText(getText(R.string.rwpfxq));
            if ("0".equals(String.valueOf(task.getStatus()))) {
                btnCommit.setVisibility(View.GONE);
                btnHt.setVisibility(View.GONE);
                mReason.setVisibility(View.GONE);
            } else {
                btnCommit.setVisibility(View.VISIBLE);
                btnHt.setVisibility(View.VISIBLE);
                mReason.setVisibility(View.VISIBLE);
            }
            tvRight.setText(getText(R.string.cx));
        }

        tvNoTitle.setText(task.getName());
        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvDes.setText(task.getDescription());
        tvTime.setText(task.getEndTime());
        //设置退回原因有时，显示
        if (!TextUtils.isEmpty(task.getThReason())) {
            tvReason.setText(task.getThReason());
            tvReason.setEnabled(false);
            mReason.setVisibility(View.VISIBLE);
            btnHt.setVisibility(View.GONE);
        }
        loadData();
        loadUser();
        loadUName();
        createDialog();
    }

    private void loadData() {
        SuccinctProgress.showSuccinctProgress(TaskDetailNewActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
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
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.load_fail);
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

    private void loadUser() {
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
                    SuccinctProgress.dismiss();
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        LogUtil.i(jsonObject.toString());
                        Gson gson = new Gson();
                        User u = (User) gson.fromJson(jsonObject.toString(), User.class);
                        tvRe.setText(u.getRealName());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.load_fail);
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

    private void loadUName() {
        String url = ConstantUtil.BASE_URL + "/user/queryUserByAccount";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("account", task.getCreateuser());
        VolleyRequest.RequestPost(this, url, "queryUserByAccount", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    SuccinctProgress.dismiss();
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        LogUtil.i(jsonObject.toString());
                        Gson gson = new Gson();
                        User u = (User) gson.fromJson(jsonObject.toString(), User.class);
                        tvFbr.setText(u.getRealName());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.load_fail);
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

    private void loadFk() {
        String url = ConstantUtil.BASE_URL + "/m_taskback/queryTaskBackByTaskId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("taskId", task.getId());
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
                            JSONArray jsonArray = result.getJSONArray("data");
                            Gson gson = new Gson();
                            tb = gson.fromJson(jsonArray.toString(), new
                                    TypeToken<List<TaskBack>>() {
                            }.getType());
                            initFk();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                    }
                    if (tb.size() > 0) {
                        hasFk = true;
                    }
                    loadZf();
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


    private void loadZf() {
        zfs.clear();
        String url = ConstantUtil.BASE_URL + "/m_zf/queryZfByTaskId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("taskId", task.getId());
        VolleyRequest.RequestPost(this, url, "queryZfByTaskId", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i(result.toString());
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    LogUtil.i(resultCode);
                    if ("1".equals(resultCode)) {
                        try {
                            JSONArray jsonArray = result.getJSONArray("data");
                            LogUtil.i(jsonArray.toString());
                            Gson gson = new Gson();
                            zfs = gson.fromJson(jsonArray.toString(), new TypeToken<List<Zf>>() {
                            }.getType());
                            isShowZf();
                            initZf();
                            isShowFk();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                    }
                    showButton();
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

    /**
     * 判断当前用户是否在转发人当中
     */
    private void isShowZf() {
        String uId = user.getUserId();
        for (Zf zf : zfs) {
            String zId = String.valueOf(zf.getZfr());
            if (uId.equals(zId)) {
                hasZf = true;
                break;
            }
        }
    }

    private boolean isFkr = false;

    /**
     * 判断当前用户是不是反馈人
     */
    private void isShowFk() {
        String uId = user.getUserId();
        for (TaskBack zf : tb) {
            String zId = String.valueOf(zf.getFkr());
            if (uId.equals(zId)) {
                isFkr = true;
                break;
            }
        }
    }


    private boolean hasFk = false;//是否存在反馈
    private boolean hasZf = false;//是否存在自己的转发

    /**
     * 控制转发按钮和反馈按钮展示
     */
    private void showButton() {
        if ("2".equals(status)) {
            if (hasFk) {//有反馈，就不让转发和反馈
//                if (isFkr && task.getThReason() != null && !TextUtils.isEmpty(task.getThReason
// ())) {
                tvRight.setVisibility(View.VISIBLE);
                btnZf.setVisibility(View.GONE);
//                } else {
//                    tvRight.setVisibility(View.GONE);
//                    btnZf.setVisibility(View.GONE);
//                }
            } else if (!hasFk && hasZf) {//没反馈但转发过，也不让反馈转发
                tvRight.setVisibility(View.GONE);
                btnZf.setVisibility(View.GONE);
            } else if (!hasFk && !hasZf) {//没反馈没转发，则允许转发或反馈
                tvRight.setVisibility(View.VISIBLE);
                btnZf.setVisibility(View.VISIBLE);
            }
        } else if ("3".equals(status) && !hasFk && !hasZf) {
            tvRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 转发信息初始化
     */
    private void initZf() {
        mZfLay.removeAllViews();
        if (zfs.size() > 0) {
            View view = mInflater.inflate(R.layout.view_task_detail_title, null);
            mZfLay.addView(view);
        }
        for (int i = 0; i < zfs.size(); i++) {
            Zf zf = zfs.get(i);
            View view = mInflater.inflate(R.layout.view_task_detail_zf, null);
            TextView tvName = (TextView) view.findViewById(R.id.task_zf_zfr);
            tvName.setText(zf.getZfrName());
            TextView tvContent = (TextView) view.findViewById(R.id.task_zf_des);
            tvContent.setText(zf.getContent());
            TextView tvTime = (TextView) view.findViewById(R.id.task_zf_time);
            tvTime.setText(zf.getTime());
            TextView tvJsr = (TextView) view.findViewById(R.id.task_zf_receivor);
            tvJsr.setText(zf.getJsrName());
            mZfLay.addView(view);
        }
    }

    /**
     * 反馈信息初始化
     */
    private void initFk() {
        mFkLay.removeAllViews();
        if (tb.size() > 0) {
            View view = mInflater.inflate(R.layout.view_task_back_title, null);
            mFkLay.addView(view);
        }
        for (int i = 0; i < tb.size(); i++) {
            TaskBack zf = tb.get(i);
            View view = mInflater.inflate(R.layout.view_task_detail_fk, null);
            LinearLayout layFj = (LinearLayout) view.findViewById(R.id.worksend_detail_fkfj);
            initFkFj(layFj, zf);
            TextView tvContent = (TextView) view.findViewById(R.id.worksend_detail_fkcontent);
            tvContent.setText(zf.getFknr());
            TextView tvAddress = (TextView) view.findViewById(R.id.worksend_detail_fkaddress);
            tvAddress.setText(zf.getAddress());
            TextView tvSj = (TextView) view.findViewById(R.id.worksend_detail_fksj);
            tvSj.setText(zf.getFksj());
            mFkLay.addView(view);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryById");
        BaseApplication.getRequestQueue().cancelAll("updateTask");
        BaseApplication.getRequestQueue().cancelAll("queryUserById");
        BaseApplication.getRequestQueue().cancelAll("queryTaskBackByTaskId");
        BaseApplication.getRequestQueue().cancelAll("queryUserByAccount");
        BaseApplication.getRequestQueue().cancelAll("deleteTask");
        BaseApplication.getRequestQueue().cancelAll("queryZfByTaskId");
    }

    @Event(value = {R.id.iv_back_tv, R.id.worksend_detail_commit, R.id.hx_title_right, R.id
            .worksend_detail_zf, R.id.worksend_detail_ht}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.worksend_detail_commit:
                commit();
                break;
            case R.id.hx_title_right:
                if ("2".equals(status)) {
                    Intent intent = new Intent();
                    intent.setClass(TaskDetailNewActivity.this, ScheduleBackActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("task", task);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if ("3".equals(status)) {
                    new AlertDialog.Builder(this).setTitle("确认撤销吗？").setIcon(android.R.drawable
                            .ic_dialog_info).setPositiveButton("取消", new DialogInterface
                            .OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteTask();
                        }
                    }).show();
                }
                break;
            case R.id.worksend_detail_zf:
                Intent intent = new Intent();
                intent.setClass(TaskDetailNewActivity.this, ScheduleZfActivity.class);
                intent.putExtra("taskId", task.getId());
                startActivity(intent);
                break;
            case R.id.worksend_detail_ht:
                th();
                break;
        }
    }

    private void deleteTask() {
        String url = ConstantUtil.BASE_URL + "/m_task/deleteTask";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", task.getId());
        VolleyRequest.RequestPost(this, url, "deleteTask", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    SuccinctProgress.dismiss();
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.cx_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                ToastUtil.show(TaskDetailNewActivity.this, R.string.cx_fail);
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    /**
     * 动态添加附件
     */
    private void initFj() {
        tvFj.removeAllViews();
        if (!TextUtils.isEmpty(task.getFilepath())) {
            final String[] path = task.getFilepath().split(",");
            String[] fileName = task.getFilename().split(",");
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
                        final String p = path[i];
                        TextView tv = new TextView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tv.setLayoutParams(lp);
                        tv.setText(fileName[i]);
                        int pxValue = DensityUtil.dip2px(TaskDetailNewActivity.this, 5);
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
                                    intent.setClass(TaskDetailNewActivity.this, ShowImageActivity
                                            .class);
                                    startActivity(intent);
                                } else {
                                    File file = new File(ConstantUtil.BASE_PATH + fName);
                                    if (file.exists()) {
                                        Intent intent = new Intent(TaskDetailNewActivity.this,
                                                FileDisplayActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("path", ConstantUtil.BASE_PATH +
                                                fName);
                                        bundle.putSerializable("fileName", fName);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        final Dialog dialog = new Dialog(TaskDetailNewActivity
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
                                        tvCon.setText(TaskDetailNewActivity.this.getText(R.string
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
                                                String url = ConstantUtil.FILE_DOWNLOAD_URL + p;
                                                LogUtil.i(url);
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
                        tvFj.addView(tv);
                    }
                }
            }
        } else {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            int pxValue = DensityUtil.dip2px(TaskDetailNewActivity.this, 5);
            tv.setPadding(pxValue, pxValue, pxValue, pxValue);
            tv.setText(getText(R.string.no_fj));
            tvFj.addView(tv);
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

    /**
     * 动态添加附件
     */
    private void initFkFj(LinearLayout tvFkFj, TaskBack tba) {
        tvFkFj.removeAllViews();
        if (!TextUtils.isEmpty(tba.getFilePath())) {
            final String[] path = tba.getFilePath().split(",");
            final String[] fileName = tba.getFileName().split(",");
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
                        final String p = path[i];
                        TextView tv = new TextView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tv.setLayoutParams(lp);
                        tv.setText(fileName[i]);
                        int pxValue = DensityUtil.dip2px(TaskDetailNewActivity.this, 5);
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
                                    intent.setClass(TaskDetailNewActivity.this, ShowImageActivity
                                            .class);
                                    startActivity(intent);
                                } else {
                                    final Dialog dialog = new Dialog(TaskDetailNewActivity.this);
                                    View view = getLayoutInflater().inflate(R.layout.tip_dialog,
                                            null);

                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(view);
                                    TextView tvCon = (TextView) view.findViewById(R.id.tip_content);
                                    tvCon.setText(TaskDetailNewActivity.this.getText(R.string
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
                                            String url = ConstantUtil.FILE_DOWNLOAD_URL + p;
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
                        });
                        tvFkFj.addView(tv);
                    }
                }
            }
        } else {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            int pxValue = DensityUtil.dip2px(TaskDetailNewActivity.this, 5);
            tv.setPadding(pxValue, pxValue, pxValue, pxValue);
            tv.setText(getText(R.string.no_fj));
            tvFkFj.addView(tv);
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OkHttpManager.DOWNLOAD_SUCCESS:
                    ToastUtil.show(TaskDetailNewActivity.this, R.string.download_success);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(TaskDetailNewActivity.this, R.string.download_failed);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(TaskDetailNewActivity.this, R.string.net_unuser);
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


    private void commit() {
        SuccinctProgress.showSuccinctProgress(TaskDetailNewActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/m_task/updateTask";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("status", "0");
        params.put("id", task.getId());
        params.put("jgts", "1");
        VolleyRequest.RequestPost(this, url, "updateTask", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.comfirm_finish);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.comfirm_fail);
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

    private void th() {
        String reason = tvReason.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            ToastUtil.show(this, R.string.th_reason_tip);
            return;
        }
        SuccinctProgress.showSuccinctProgress(TaskDetailNewActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/m_task/updateTask";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", task.getId());
        params.put("reason", reason);
        params.put("jgts", "0");
        VolleyRequest.RequestPost(this, url, "updateTask", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.ht_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(TaskDetailNewActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(TaskDetailNewActivity.this, R.string.ht_fail);
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

}

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.MqztcglAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Mqztc;
import com.xzz.hxjdglpt.model.Mqztcgl;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.superfileview.FileDisplayActivity;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.StringUtil;
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
 * 民情直通车详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_mqztc_detail)
public class MqztcDetailActivity extends BaseActivity implements AbsListView.OnScrollListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;

    @ViewInject(R.id.mqztc_detail_list)
    private ListView mList;


    private View moreView;
    private View headView;

    private LayoutInflater layoutInflater;
    private TextView tv_load_more;
    private ProgressBar pb_load_progress;


    private TextView tvTi;
    private TextView tvLabel;
    private TextView tvTime;
    private TextView tvContent;
    private LinearLayout lay_fj;
    private TextView tcAddress;
    private TextView tvHfr;
    private TextView tvHfSj;

    private TextView tvLabel1;
    private TextView tvLabel2;
    private RelativeLayout tvLabel3;
    private TextView tvLabel4;

    private int pageIndex = 0;

    private int lastItem;

    private List<Mqztcgl> list = null;

    private MqztcglAdapter mqztcglAdapter = null;

    private Mqztc mqztc;
    private User user;

    @ViewInject(R.id.mqztc_send)
    private Button butHf;
    private String hf;
    @ViewInject(R.id.mqztc_input)
    private EditText edtHf;

    private List<Role> roles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mqztc = getIntent().getParcelableExtra("mqztc");
        roles = application.getRolesList();
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(getText(R.string.mqztc_detail));

        headView = layoutInflater.inflate(R.layout.mqztc_head, null);
        tvTi = (TextView) headView.findViewById(R.id.mqztc_detail_title);
        tvLabel = (TextView) headView.findViewById(R.id.mqztc_detail_label);
        tvTime = (TextView) headView.findViewById(R.id.mqztc_detail_time);
        tvContent = (TextView) headView.findViewById(R.id.mqztc_detail_content);
        lay_fj = (LinearLayout) headView.findViewById(R.id.mqztc_detail_fj);
        tcAddress = (TextView) headView.findViewById(R.id.mqztc_detail_backaddress);
        tvHfr = (TextView) headView.findViewById(R.id.mqztc_detail_hfr);
        tvHfSj = (TextView) headView.findViewById(R.id.mqztc_detail_hfsj);
        tvLabel1 = (TextView) headView.findViewById(R.id.mqztc_head_lab1);
        tvLabel2 = (TextView) headView.findViewById(R.id.mqztc_head_lab2);
        tvLabel3 = (RelativeLayout) headView.findViewById(R.id.mqztc_head_lab3);
        tvLabel4 = (TextView) headView.findViewById(R.id.mqztc_head_lab4);


        moreView = layoutInflater.inflate(R.layout.footer_more, null);
        tv_load_more = (TextView) moreView.findViewById(R.id.tv_load_more);
        pb_load_progress = (ProgressBar) moreView.findViewById(R.id.pb_load_progress);
        mList.addFooterView(moreView);
        mList.addHeaderView(headView);
        mList.setOnScrollListener(this);

        list = new ArrayList<>();
        mqztcglAdapter = new MqztcglAdapter(this, list, mqztc.getQuestionManXm());
        mList.setAdapter(mqztcglAdapter);
        tv_load_more.setText("");
        pb_load_progress.setVisibility(View.GONE);

        createDialog();
    }


    public void initData() {
        if (mqztc.getTitle() != null) {
            tvTi.setText(mqztc.getTitle());
        }
        if (mqztc.getDescription() != null) {
            tvLabel.setText("问题描述：" + mqztc.getDescription());
        }
        if (mqztc.getQuestionTime() != null) {
            tvTime.setText("提问时间：" + mqztc.getQuestionTime());
        }
        if (!StringUtil.isEmpty(mqztc.getAnswer())) {
            tvContent.setText(mqztc.getAnswer());
            tvLabel1.setVisibility(View.VISIBLE);
            tvLabel2.setVisibility(View.VISIBLE);
            tvLabel3.setVisibility(View.VISIBLE);
            tvLabel4.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
        }
        tcAddress.setText(mqztc.getAddress());
        tvHfSj.setText(mqztc.getAnswerTime());
        tvHfr.setText(mqztc.getAnswerManXm());
        initFj();
        loadData();
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4245".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    @Event(value = {R.id.iv_back, R.id.mqztc_send}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.mqztc_send:
                sendHf();
                break;
        }
    }

    /**
     * 动态添加附件
     */
    private void initFj() {
        lay_fj.removeAllViews();
        if (!TextUtils.isEmpty(mqztc.getFilePath())) {
            final String[] path = mqztc.getFilePath().split(",");
            String[] fileName = mqztc.getFileName().split(",");
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
                        int pxValue = DensityUtil.dip2px(MqztcDetailActivity.this, 5);
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
                                    intent.setClass(MqztcDetailActivity.this, ShowImageActivity
                                            .class);
                                    startActivity(intent);
                                } else {
                                    File file = new File(ConstantUtil.BASE_PATH + fName);
                                    if (file.exists()) {
                                        Intent intent = new Intent(MqztcDetailActivity
                                                .this, FileDisplayActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("path", ConstantUtil.BASE_PATH +
                                                fName);
                                        bundle.putSerializable("fileName", fName);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        final Dialog dialog = new Dialog(MqztcDetailActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.tip_dialog,
                                                null);

                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(view);
                                        //TextView tvTip = (TextView) view.findViewById(R.id.tip_title);
                                        TextView tvCon = (TextView) view.findViewById(R.id.tip_content);
                                        //tvTip.setText(NoticeDetailActivity.this.getText(R.string
                                        // .zxtz_detail_tip));
                                        tvCon.setText(MqztcDetailActivity.this.getText(R.string
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
            int pxValue = DensityUtil.dip2px(MqztcDetailActivity.this, 5);
            tv.setPadding(pxValue, pxValue, pxValue, pxValue);
            tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv.setTextColor(getResources().getColor(R.color.title_bg));
            lay_fj.addView(tv);
        }
    }

    private void loadData() {
        SuccinctProgress.showSuccinctProgress(this, "数据加载中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/m_mqztc/queryMqztcglList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pid", mqztc.getId());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "mqztc_queryList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<Mqztcgl> date = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<Mqztcgl>>() {
                                        }.getType());
                                list.addAll(date);
                                if (list.size() >= ConstantUtil.PAGE_SIZE) {
                                    tv_load_more.setText(R.string.load_more_data);
                                    pb_load_progress.setVisibility(View.GONE);
                                } else {
                                    tv_load_more.setText(R.string.no_more_data);
                                    pb_load_progress.setVisibility(View.GONE);
                                }
                                mqztcglAdapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(MqztcDetailActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(MqztcDetailActivity.this, R.string.load_fail);
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
                    ToastUtil.show(MqztcDetailActivity.this, R.string.download_success);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(MqztcDetailActivity.this, R.string.download_failed);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(MqztcDetailActivity.this, R.string.net_unuser);
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
    public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        LogUtil.i("onScrollStateChanged");
        if ((lastItem - 1) == mqztcglAdapter.getCount() && scrollState == AbsListView
                .OnScrollListener.SCROLL_STATE_IDLE && pageIndex - list.size() < ConstantUtil
                .PAGE_SIZE) {
            pageIndex += ConstantUtil.PAGE_SIZE;
            loadData();
        }
    }


    public void sendHf() {
        hf = edtHf.getText().toString();
        if (TextUtils.isEmpty(hf)) {
            ToastUtil.show(this, "内容不能为空");
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("wt", hf);
            params.put("wtrname", user.getRealName());
            edtHf.setText("");
            params.put("userId", String.valueOf(user.getUserId()));
            params.put("token", user.getToken());
            params.put("wtaddreess", "");
            params.put("pid", mqztc.getId());
            if (isContain()) {
                params.put("type", "2");
            } else {
                params.put("type", "1");
            }
            commitHf(params);
        }
    }


    private void commitHf(HashMap<String, String> params) {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/m_mqztc/commitMqztcgl";
        VolleyRequest.RequestPost(this, url, "commitMqztcgl", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("5".equals(resultCode)) {
                                Gson gson = new Gson();
                                Mqztc mdata = (Mqztc) gson.fromJson(result.getJSONObject("data").toString(),
                                        Mqztc.class);
                                if (mdata.getAnswer() != null) {
                                    tvContent.setText(mdata.getAnswer());
                                }
                                tvHfSj.setText(mdata.getAnswerTime());
                                tvHfr.setText(mdata.getAnswerManXm());
                                tvLabel1.setVisibility(View.VISIBLE);
                                tvLabel2.setVisibility(View.VISIBLE);
                                tvLabel3.setVisibility(View.VISIBLE);
                                tvLabel4.setVisibility(View.VISIBLE);
                                tvContent.setVisibility(View.VISIBLE);
                            } else if ("4".equals(resultCode)) {
                                Gson gson = new Gson();
                                Mqztcgl mqztcgl = (Mqztcgl) gson.fromJson
                                        (result.getJSONObject("data").toString(), Mqztcgl
                                                .class);
                                list.add(mqztcgl);
                                mqztcglAdapter.notifyDataSetChanged();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(MqztcDetailActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(MqztcDetailActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("commitMqztcgl");
        BaseApplication.getRequestQueue().cancelAll("mqztc_queryList");
    }

}

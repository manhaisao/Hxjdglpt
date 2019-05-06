package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jyfw;
import com.xzz.hxjdglpt.model.Pfwsp;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyImageLoader;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by dbz on 2017/8/23.
 */
@ContentView(R.layout.aty_pfwsp_detail)
public class PfwspDetailActivity extends BaseActivity {

    private User user;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.jyfw_detail_title)
    private TextView tvBTitle;
    @ViewInject(R.id.jyfw_detail_time)
    private TextView tvTime;
    @ViewInject(R.id.wmcj_video)
    private JCVideoPlayer wmcj_video;

    private Pfwsp pfwsp;

    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        pfwsp = (Pfwsp) getIntent().getParcelableExtra("pfwsp");

//        if (isGly()) {
//            tvRight.setText("删除");
//            tvRight.setVisibility(View.VISIBLE);
//        } else {
        tvRight.setVisibility(View.GONE);
//        }
        if(!TextUtils.isEmpty( pfwsp.getVideoUrl()))
        {
            path = pfwsp.getVideoUrl().replace(",", "");
            wmcj_video.setUp(ConstantUtil.FILE_DOWNLOAD_URL + path.replace(",", ""), null, "");
        }
        initView();
        initData();
    }


    private boolean isGly() {
        for (Role role : application.getRolesList()) {
            if ("4237".equals(role.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        tvTitle.setText("普法微视频");

    }

    public void initData() {
        tvBTitle.setText(pfwsp.getName());
        tvTime.setText("发布时间：" + pfwsp.getCreateTime());
    }


    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final Dialog dialog = new Dialog(PfwspDetailActivity.this);
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
                        delJyfw();
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

    private void delJyfw() {
        String url = ConstantUtil.BASE_URL + "/jyfw/deleteJyfwList";
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(pfwsp.getId()));
        SuccinctProgress.showSuccinctProgress(PfwspDetailActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "deleteJyfwList", params, new
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
                                finish();
                                ToastUtil.show(PfwspDetailActivity.this, "成功删除");
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(PfwspDetailActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(PfwspDetailActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("deleteJyfwList");
    }


}

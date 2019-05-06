package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.QlyListAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.qly.Camera;
import com.xzz.hxjdglpt.model.qly.Media;
import com.xzz.hxjdglpt.model.qly.ResultInfo;
import com.xzz.hxjdglpt.model.qly.SubjectInfo;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.vlc.FrameLayoutScale;
import com.xzz.hxjdglpt.vlc.MediaControl;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.vlc.VlcVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */
public class QlyListActicity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivBack;
    private User user;
    private BaseApplication application;

    private List<SubjectInfo> subjectInfos;

    private List<Camera> cameras;
    private LinearLayout linearLayout;
    private ListView listView;

    private List<Object> mData = new ArrayList<>();

    private QlyListAdapter qlyListAdapter;

    private Context mContext;

    //播放器
    VlcVideoView vlcVideoView;


    private LinearLayout mLoading;

    private ImageButton ibTop;
    private ImageButton ibLeft;
    private ImageButton ibRight;
    private ImageButton ibBottom;
    private ImageButton ibLeftTop;
    private ImageButton ibLeftBottom;
    private ImageButton ibRightTop;
    private ImageButton ibRightBottom;
    private RelativeLayout mPztType;//控制摄像头方向

    private TextView tvLTitle;
    private ImageView ivLBack;
    private Camera camera;

    private RelativeLayout mTitleBar;
    private HorizontalScrollView mScoll;
    private ImageView mLine;
    private RelativeLayout mRay;
    private ImageView mChange;

    private FrameLayoutScale mVlcView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_qly_list);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mContext = this;
        initView();
        initData();
    }

    public void initData() {
        qlyListAdapter = new QlyListAdapter(this, mData);
        listView.setAdapter(qlyListAdapter);
        loadData();
    }

    public void initView() {
        mTitleBar = (RelativeLayout) findViewById(R.id.hx_detail_title);
        mScoll = (HorizontalScrollView) findViewById(R.id.qly_list_scoll);
        mLine = (ImageView) findViewById(R.id.qly_list_line);
        mRay = (RelativeLayout) findViewById(R.id.qly_player_contain);
        linearLayout = (LinearLayout) findViewById(R.id.qly_list_tab);
        listView = (ListView) findViewById(R.id.qly_listview);
        tvTitle = (TextView) findViewById(R.id.hx_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mVlcView = (FrameLayoutScale) findViewById(R.id.vlc_video_contain);
//        vlcVideoView = (VlcVideoView) findViewById(R.id.player);
        mChange = (ImageView) findViewById(R.id.change);
        mLoading = (LinearLayout) findViewById(R.id.video_loading);
        ibTop = (ImageButton) findViewById(R.id.qly_control_top);
        ibBottom = (ImageButton) findViewById(R.id.qly_control_bottom);
        ibRight = (ImageButton) findViewById(R.id.qly_control_right);
        ibLeft = (ImageButton) findViewById(R.id.qly_control_left);
        ibLeftTop = (ImageButton) findViewById(R.id.qly_control_lefttop);
        ibLeftBottom = (ImageButton) findViewById(R.id.qly_control_leftbottom);
        ibRightBottom = (ImageButton) findViewById(R.id.qly_control_rightbottom);
        ibRightTop = (ImageButton) findViewById(R.id.qly_control_righttop);
        mPztType = (RelativeLayout) findViewById(R.id.qly_control);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvLTitle = (TextView) findViewById(R.id.vlc_title);
        ivLBack = (ImageView) findViewById(R.id.vlc_back);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData.get(position) instanceof SubjectInfo) {
                    SubjectInfo sb = (SubjectInfo) mData.get(position);
                    TextView tv = new TextView(mContext);
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                            .getDimensionPixelSize(R.dimen.title_size_tv));
                    tv.setPadding(0, DensityUtil.dip2px(mContext, 6), 0, DensityUtil.dip2px
                            (mContext, 10));
                    tv.setText(">" + sb.getSubjectName());
                    tv.setTag(sb);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SubjectInfo sb = (SubjectInfo) v.getTag();
                            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                                if (i > linearLayout.indexOfChild(v)) {
                                    linearLayout.removeViewAt(i);
                                }
                            }
                            setListData(sb);
                        }
                    });
                    linearLayout.addView(tv);
                    setListData(sb);
                } else if (mData.get(position) instanceof Camera) {
                    camera = (Camera) mData.get(position);
                    if ("1".equals(camera.getRunStatus())) {
                        loadMediaData();
                    } else {
                        ToastUtil.show(QlyListActicity.this, "该设备无法播放");
                    }
                }
            }
        });

        mPztType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.GONE);
                }
            }
        });
//        vlcVideoView.setMediaListenerEvent(new MediaControl(mContext, vlcVideoView, mLoading));
        tvTitle.setText("千里眼设备列表");
        ivBack.setOnClickListener(this);
        ibTop.setOnClickListener(this);
        ibBottom.setOnClickListener(this);
        ibRight.setOnClickListener(this);
        ibLeft.setOnClickListener(this);
        ibLeftTop.setOnClickListener(this);
        ibLeftBottom.setOnClickListener(this);
        ibRightBottom.setOnClickListener(this);
        ibRightTop.setOnClickListener(this);
        ivLBack.setOnClickListener(this);
        mChange.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadData() {
        String url = ConstantUtil.BASE_URL + "/qly/queryDevices";
        HashMap<String, String> params = new HashMap<>();
        String[] u = application.getUserInfo().getUserName().split("@");
        params.put("name", u[0]);
        params.put("domain", application.getUserInfo().getLoginDomain());
        params.put("pass", application.getUserInfo().getPassword());
        params.put("scType", "2");
        params.put("scId", application.getUserInfo().getUserID());

        VolleyRequest.RequestPost(this, url, "queryDevices", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    Gson gson = new Gson();
                    ResultInfo resultInfo = (ResultInfo) gson.fromJson(result.getJSONObject
                            ("resultInfo").toString(), ResultInfo.class);
                    if ("OK".equals(resultInfo.getValue())) {
                        JSONArray jsonArray = result.getJSONArray("subjectInfo");
                        subjectInfos = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<SubjectInfo>>() {
                        }.getType());
                        JSONArray jsonArray1 = result.getJSONArray("camera");
                        cameras = gson.fromJson(jsonArray1.toString(), new
                                TypeToken<List<Camera>>() {
                        }.getType());
                        setAllData();
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

    //    @Event(value = {R.id.iv_back, R.id.vlc_back, R.id.change, R.id.qly_control_top, R.id
//            .qly_control_left, R.id.qly_control_bottom, R.id.qly_control_right, R.id
//            .qly_control_lefttop, R.id.qly_control_leftbottom, R.id.qly_control_rightbottom, R.id
//            .qly_control_righttop}, type = View.OnClickListener.class)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.vlc_back:
                finish();
                break;
            case R.id.change:
                if (isFullscreen) {
                    mTitleBar.setVisibility(View.VISIBLE);
                    mScoll.setVisibility(View.VISIBLE);
                    mLine.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    mPztType.setVisibility(View.GONE);//隐藏控制方向的view
                    ViewGroup.LayoutParams params = mRay.getLayoutParams();
                    params.height = DensityUtil.dip2px(QlyListActicity.this, 200);
                    mRay.setLayoutParams(params);
                    mChange.setImageResource(R.mipmap.ic_fullscreen_white_24dp);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //设置视频播放高度
                    mTitleBar.setVisibility(View.GONE);
                    mScoll.setVisibility(View.GONE);
                    mLine.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = mRay.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mRay.setLayoutParams(params);
                    mChange.setImageResource(R.mipmap.ic_fullscreen_exit_white_36dp);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                isFullscreen = !isFullscreen;
                break;
            case R.id.qly_control_top:
                controlQly("PTZ_UP", "1", "1");
                break;
            case R.id.qly_control_left:
                controlQly("PTZ_LEFT", "1", "1");
                break;
            case R.id.qly_control_bottom:
                controlQly("PTZ_DOWN", "1", "1");
                break;
            case R.id.qly_control_right:
                controlQly("PTZ_RIGHT", "1", "1");
                break;
            case R.id.qly_control_lefttop:
                controlQly("PTZ_UP_LEFT", "1", "1");
                break;
            case R.id.qly_control_leftbottom:
                controlQly("PTZ_DOWN_LEFT", "1", "1");
                break;
            case R.id.qly_control_rightbottom:
                controlQly("PTZ_DOWN_RIGHT", "1", "1");
                break;
            case R.id.qly_control_righttop:
                controlQly("PTZ_UP_RIGHT", "1", "1");
                break;
        }
    }

    private void setAllData() {
        mData.clear();
        linearLayout.removeAllViews();
        TextView tv = new TextView(mContext);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen
                .title_size_tv));
        tv.setPadding(0, DensityUtil.dip2px(mContext, 6), 0, DensityUtil.dip2px(mContext, 10));
        tv.setText("全部");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllData();
            }
        });
        linearLayout.addView(tv);
        for (SubjectInfo sub : subjectInfos) {
            if (TextUtils.isEmpty(sub.getParentId())) {
                mData.add(sub);
            }
        }
        qlyListAdapter.notifyDataSetChanged();
    }

    private void setListData(SubjectInfo subjectInfo) {
        mData.clear();
        for (SubjectInfo sub : subjectInfos) {
            if (subjectInfo.getSubjectId().equals(sub.getParentId())) {
                mData.add(sub);
            }
        }
        if (mData.size() <= 0) {
            for (Camera camera : cameras) {
                if (camera.getSubjectId().equals(subjectInfo.getSubjectId())) {
                    mData.add(camera);
                }
            }
        }
        qlyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryDevices");
        BaseApplication.getRequestQueue().cancelAll("qly_get_realtime_rtsp");
        if (vlcVideoView != null) vlcVideoView.onStop();
    }

    private void loadMediaData() {
        String url = ConstantUtil.BASE_URL + "/qly/qly_get_realtime_rtsp";
        HashMap<String, String> params = new HashMap<>();
        String[] u = application.getUserInfo().getUserName().split("@");
        params.put("name", u[0]);
        params.put("domain", application.getUserInfo().getLoginDomain());
        params.put("pass", application.getUserInfo().getPassword());
        params.put("camId", camera.getId());
        params.put("streamType", "2");
        params.put("urlType", "1");
        params.put("scId", application.getUserInfo().getUserID());
        SuccinctProgress.showSuccinctProgress(this, "数据请求中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "qly_get_realtime_rtsp", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    Gson gson = new Gson();
                    ResultInfo resultInfo = (ResultInfo) gson.fromJson(result.getJSONObject
                            ("resultInfo").toString(), ResultInfo.class);
                    if ("OK".equals(resultInfo.getValue())) {
                        Media media = (Media) gson.fromJson(result.getJSONObject("data").toString
                                (), Media.class);
//                        Intent intent = new Intent();
//                        intent.setClass(QlyListActicity.this, VlcVideoActivity.class);
//                        intent.putExtra("url", media.getPath());
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("camera", camera);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                        if (vlcVideoView != null && vlcVideoView.isPlaying()) {
                            vlcVideoView.onStop();
                            vlcVideoView.onDestory();
                            vlcVideoView = null;
                        }
                        mVlcView.removeAllViews();
                        vlcVideoView = new VlcVideoView(mContext);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        vlcVideoView.setLayoutParams(lp);
                        vlcVideoView.setMediaListenerEvent(new MediaControl(mContext,
                                vlcVideoView, mLoading));
                        vlcVideoView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isFullscreen && mPztType.getVisibility() == View.GONE && "2"
                                        .equals(camera.getPtzType())) {
                                    mPztType.setVisibility(View.VISIBLE);
                                } else if (mPztType.getVisibility() == View.VISIBLE) {
                                    mPztType.setVisibility(View.GONE);
                                }
                            }
                        });
                        mVlcView.addView(vlcVideoView);
                        tvLTitle.setText(camera.getName());
                        final String pUrl = media.getPath();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                vlcVideoView.startPlay(pUrl);
                            }
                        }, 500);
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


    private boolean isFullscreen = false;

    private void controlQly(String opcode, String p1, String p2) {
        if (camera == null) {
            ToastUtil.show(mContext, "请选择设备");
            return;
        }
        ibLeft.setEnabled(false);
        ibRight.setEnabled(false);
        ibTop.setEnabled(false);
        ibBottom.setEnabled(false);
        ibLeftTop.setEnabled(false);
        ibRightTop.setEnabled(false);
        ibLeftBottom.setEnabled(false);
        ibRightBottom.setEnabled(false);
        String url = ConstantUtil.BASE_URL + "/qly/qly_ptz_run";
        HashMap<String, String> params = new HashMap<>();
        String[] u = application.getUserInfo().getUserName().split("@");
        params.put("name", u[0]);
        params.put("domain", application.getUserInfo().getLoginDomain());
        params.put("pass", application.getUserInfo().getPassword());
        params.put("camId", camera.getId());
        params.put("opcode", opcode);
        params.put("p1", p1);
        params.put("p2", p2);
        params.put("scId", application.getUserInfo().getUserID());

        VolleyRequest.RequestPost(this, url, "qly_ptz_run", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                ibLeft.setEnabled(true);
                ibRight.setEnabled(true);
                ibTop.setEnabled(true);
                ibBottom.setEnabled(true);
                ibLeftTop.setEnabled(true);
                ibRightTop.setEnabled(true);
                ibLeftBottom.setEnabled(true);
                ibRightBottom.setEnabled(true);
                LogUtil.i("onMySuccess");
                try {
                    Gson gson = new Gson();
                    ResultInfo resultInfo = (ResultInfo) gson.fromJson(result.getJSONObject
                            ("resultInfo").toString(), ResultInfo.class);
                    if ("OK".equals(resultInfo.getValue())) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                ibLeft.setEnabled(true);
                ibRight.setEnabled(true);
                ibTop.setEnabled(true);
                ibBottom.setEnabled(true);
                ibLeftTop.setEnabled(true);
                ibRightTop.setEnabled(true);
                ibRightTop.setEnabled(true);
                ibRightBottom.setEnabled(true);
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (isFullscreen) {
                isFullscreen = false;
                mTitleBar.setVisibility(View.VISIBLE);
                mScoll.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                mPztType.setVisibility(View.GONE);//隐藏控制方向的view
                ViewGroup.LayoutParams params = mRay.getLayoutParams();
                params.height = DensityUtil.dip2px(QlyListActicity.this, 200);
                mRay.setLayoutParams(params);
                mChange.setImageResource(R.mipmap.ic_fullscreen_white_24dp);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

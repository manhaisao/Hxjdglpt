package com.xzz.hxjdglpt.activity;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.qly.Camera;
import com.xzz.hxjdglpt.model.qly.ResultInfo;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.vlc.MediaControl;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.vlc.VlcVideoView;

import java.util.HashMap;


/**
 * d
 */
public class VlcVideoActivity extends AppCompatActivity implements View.OnClickListener {
    VlcVideoView vlcVideoView;

    String tag = "VlcVideoFragment";

    private String url;

    private LinearLayout mLoading;

    private User user;
    private BaseApplication application;

    private ImageButton ibTop;
    private ImageButton ibLeft;
    private ImageButton ibRight;
    private ImageButton ibBottom;
    private ImageButton ibLeftTop;
    private ImageButton ibLeftBottom;
    private ImageButton ibRightTop;
    private ImageButton ibRightBottom;
    private RelativeLayout mPztType;//控制摄像头方向
    private Camera camera;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView mChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_rtsp);
        vlcVideoView = (VlcVideoView) findViewById(R.id.player);
        mChange = (ImageView) findViewById(R.id.change);
        mLoading = (LinearLayout) findViewById(R.id.video_loading);
        url = getIntent().getStringExtra("url");
        camera = getIntent().getParcelableExtra("camera");
//        Media media = new Media(VLCInstance.get(getContext()), Uri.parse(path));
//        media.setHWDecoderEnabled(false, false);
//        vlcVideoView.setMedia(media);
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
        tvTitle = (TextView) findViewById(R.id.vlc_title);
        tvTitle.setText(camera.getName());
        mPztType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.GONE);
                }
            }
        });
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vlcVideoView.setMediaListenerEvent(new MediaControl(this, vlcVideoView, mLoading));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        Log.i(tag, "---------   start   ----------------");
        vlcVideoView.startPlay(url);
//            }
//        }, 500);
        vlcVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPztType.getVisibility() == View.GONE && "2".equals(camera.getPtzType())) {
                    mPztType.setVisibility(View.VISIBLE);
                } else if (mPztType.getVisibility() == View.VISIBLE) {
                    mPztType.setVisibility(View.GONE);
                }
            }
        });
        ibTop.setOnClickListener(this);
        ibBottom.setOnClickListener(this);
        ibRight.setOnClickListener(this);
        ibLeft.setOnClickListener(this);
        ibLeftTop.setOnClickListener(this);
        ibLeftBottom.setOnClickListener(this);
        ibRightBottom.setOnClickListener(this);
        ibRightTop.setOnClickListener(this);
        mChange.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        vlcVideoView.onStop();
    }

    public boolean isFullscreen;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                isFullscreen = !isFullscreen;
                if (isFullscreen) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    mChange.setImageResource(R.mipmap.ic_fullscreen_exit_white_36dp);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mChange.setImageResource(R.mipmap.ic_fullscreen_white_24dp);
                }
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

    private void controlQly(String opcode, String p1, String p2) {
        ibLeft.setEnabled(false);
        ibRight.setEnabled(false);
        ibTop.setEnabled(false);
        ibBottom.setEnabled(false);
        ibLeftTop.setEnabled(false);
        ibRightTop.setEnabled(false);
        ibRightTop.setEnabled(false);
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
                ibRightTop.setEnabled(true);
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
                isFullscreen=false;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                mChange.setImageResource(R.mipmap.ic_fullscreen_exit_white_36dp);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

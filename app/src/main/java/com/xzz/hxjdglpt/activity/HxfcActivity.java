package com.xzz.hxjdglpt.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.signView.ReWebChomeClient;
import com.xzz.hxjdglpt.signView.ReWebViewClient;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.ImageUtil;
import com.xzz.hxjdglpt.utils.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by dbz on 2017/7/6.
 */
@ContentView(R.layout.aty_hxfc)
public class HxfcActivity extends BaseActivity implements ReWebChomeClient.OpenFileChooserCallBack {
    @ViewInject(R.id.hxfc_webview)
    WebView webView;
    private static final String TAG = "HxfcActivity";
    private ValueCallback<Uri[]> mUploadMsg5Plus;
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;

    private ValueCallback<Uri> mUploadMsg;
    private Intent mSourceIntent;

    private ProgressDialog progressDialog;

    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        roles = application.getRolesList();
        initData();
        initView();
        loadWeb();
    }

    private int isContain() {
        for (Role r : roles) {
            if ("4249".equals(r.getRoleId())) {
                return 1;
            }
        }
        return 0;
    }

    public void loadWeb() {
        String url = ConstantUtil.BASE_URL + "/wc/hxfc?userId=" + application.getUser().getUserId
                () + "&addQx=" + isContain();
//        // 获取WebView的设置
//        WebSettings webSettings = webView.getSettings();
//        // 将JavaScript设置为可用，这一句话是必须的，不然所做一切都是徒劳的
//        webSettings.setJavaScriptEnabled(true);
//        //给webview添加JavaScript接口
//        webView.addJavascriptInterface(new JsInterface(), "control");
//        //此方法可以在webview中打开链接而不会跳转到外部浏览器
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl(url);

//        setContentView(R.layout.activity_main);
//        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.forum_context);
        webView.setWebChromeClient(new ReWebChomeClient(this, this) {
            @Override
            public void onProgressChanged(WebView view, int progress) {
//                if (progress == 100) {
//                    removeProgress();
                    // 隐藏进度条
//                    swipeLayout.setRefreshing(false);
//                }
//                if (progress < 100) {
//                    showProgress("正在加载中...");
//                }

                super.onProgressChanged(view, progress);
            }
        });
        webView.setWebViewClient(new ReWebViewClient() {
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
        // 设置可以访问文件
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true); //自适应屏幕

    }

    @Override
    public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
        mUploadMsg5Plus = filePathCallback;
        showOptions();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            showOptions();
//            Toast.makeText(getApplicationContext(), "请选择", 1).show();
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        try {
                            if (mUploadMsg == null && mUploadMsg5Plus == null) {
                                return;
                            }
                            Uri uri1 = Uri.fromFile(new File(ImageUtil.tempPath));
                            if (mUploadMsg != null) {
                                mUploadMsg.onReceiveValue(uri1);
                                mUploadMsg = null;
                            } else {
                                mUploadMsg5Plus.onReceiveValue(new Uri[]{uri1});
                                mUploadMsg5Plus = null;
                            }
                        } catch (Exception e) {
                            mUploadMsg5Plus = null;
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (mUploadMsg == null && mUploadMsg5Plus == null) {
                        return;
                    }
                    String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                    if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                        Log.w(TAG, "sourcePath empty or not exists.");
                        break;
                    }
                    Uri uri = Uri.fromFile(new File(sourcePath));
                    if (mUploadMsg != null) {
                        mUploadMsg.onReceiveValue(uri);
                        mUploadMsg = null;
                    } else {
                        mUploadMsg5Plus.onReceiveValue(new Uri[]{uri});
                        mUploadMsg5Plus = null;
                    }

                } catch (Exception e) {
                    mUploadMsg5Plus = null;
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("操作");
        alertDialog.setItems(R.array.options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        mSourceIntent = ImageUtil.choosePicture();
                        startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        mSourceIntent = ImageUtil.takeBigPicture(HxfcActivity.this);
                        requestPermission(new String[]{android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                    } catch (Exception e) {
                    }

                }
            }
        });
        alertDialog.show();
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
                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                break;
        }
    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsg5Plus != null) {
                mUploadMsg5Plus.onReceiveValue(null);
                mUploadMsg5Plus = null;
            }
        }
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable
                    .drawable_progress_style));
        }
        if (progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void removeProgress() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onStop() {
        removeProgress();
        super.onStop();
    }
}

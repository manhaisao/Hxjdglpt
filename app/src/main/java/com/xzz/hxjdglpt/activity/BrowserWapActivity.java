package com.xzz.hxjdglpt.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import com.whty.wicity.core.Device;
////import com.whty.wireless.yc.utils.ToolHelper;
//import com.whty.wireless.yc.utils.LoadingDialog;
//import com.whty.wireless.yc.utils.LogUtils;
//import com.whty.wireless.yc.utils.ToolHelper;
//import com.whty.wireless.yc.view.NewToast;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;


public class BrowserWapActivity extends BaseActivity implements OnClickListener, DownloadListener {

    public static final String WEB_CHAT_TAG = "/webchat/advisory?";
    /**
     * 在线客服IP
     */
    public static final String IP_WEB_CHAT = "http://218.206.24.72:7091";
    private WebView appWebView;
    private String widgetUUid, title;

//	private TextView tv_title_name;
//	private LinearLayout back_btn;
//	private RelativeLayout mHeader;

//	private ProgressBar mHorizontalProgress;
//	private LoadingDialog dialog;

    private boolean isShowHeader = true;// 控制物理返回键是否直接退出，而非wap后退 true 直接退出wap

    private Uri mImageCaptureUri;


    private boolean newWebViewShow = false;
    WebView newWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_wap);

//		tv_title_name = (TextView) findViewById(R.id.tv_title_name);
//		back_btn = (LinearLayout) findViewById(R.id.lly_back_btn);
//		mHeader = (RelativeLayout) findViewById(R.id.rly_header);
//		back_btn.setOnClickListener(this);
        appWebView = (WebView) findViewById(R.id.wap);
//		mHorizontalProgress = (ProgressBar) findViewById(R.id.progressBar1);
        appWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        updateWebViewSettings();
        appWebView.setWebChromeClient(webChromeClient);
        appWebView.setDownloadListener(this);
        appWebView.setWebViewClient(webViewClient);
        appWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        appWebView.removeJavascriptInterface("accessibility");
        appWebView.removeJavascriptInterface("accessibilityTraversal");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            appWebView.getSettings().setDisplayZoomControls(false);// 隐藏放大缩小按钮
        }
        appWebView.getSettings().setBlockNetworkImage(false);
        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            widgetUUid = bundle.getString("StartWidgetUUID");
            LogUtil.i("widgetUUid" + widgetUUid);
            title = bundle.getString("TITLE");
            isShowHeader = bundle.getBoolean("isShowHeader", true);

//			tv_title_name.setText(title);
            appWebView.loadUrl(widgetUUid);
//			if(!isShowHeader)
//			{
//				mHeader.setVisibility(View.GONE);
//			}
        }
    }

    private static int minimumFontSize = 8;
    private static int minimumLogicalFontSize = 8;
    private static int defaultFontSize = 16;
    private static int defaultFixedFontSize = 13;
    private String userAgentsString = "android";

    public void updateWebViewSettings() {

        WebSettings webSettings = appWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true); // 设置webview加载的页面的模式
        // setBuildInZoomControlsVisibility(webSettings, false);
        webSettings.setBuiltInZoomControls(true);// 设置是否使用WebView内置的放大机制。
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 缓存模式(默认)
        webSettings.setDefaultFontSize(defaultFontSize);// 设置默认字体大小
        webSettings.setDefaultFixedFontSize(defaultFixedFontSize);// 字体大小
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// javascript可自定打开窗口
        webSettings.setJavaScriptEnabled(true);// 启用javascript支持
        /**
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLightTouchEnabled(true);
        webSettings.setMinimumFontSize(minimumFontSize);
        webSettings.setMinimumLogicalFontSize(minimumLogicalFontSize);
        // webSettings.setNavDump(false);
        webSettings.setUseWideViewPort(true);// 设置webview推荐使用的窗口
//		webSettings.setUserAgentString(userAgentsString);// 设置webview的浏览器标识，一般用来方便做统计用
        webSettings.setAllowFileAccess(true);// WebView中启用或禁用文件访问。
        webSettings.setNeedInitialFocus(true);// 阻止webView里面控件获得焦点,这样阻止焦点画那个长方形
        webSettings.setSupportMultipleWindows(true);// 支持多窗口
        webSettings.setDomStorageEnabled(true); // 解决百度地图加载出来一片空白问题
        setPluginState(webSettings, "ON_DEMAND");

    }

    private Method setPluginStateMethod;
    private Object pluginState_ON_DEMAND;

    private void setPluginState(WebSettings s, String state) {
        if (state == null) {
            return;
        }

        if (setPluginStateMethod == null) {
            try {
                Class cls = WebSettings.class;
                Class pluginStateClass = Class.forName("android.webkit.WebSettings$PluginState");
                Field field;
                field = pluginStateClass.getField("ON_DEMAND");
                field.setAccessible(true);
                pluginState_ON_DEMAND = field.get(null);

                setPluginStateMethod = cls.getDeclaredMethod("setPluginState", pluginStateClass);
                setPluginStateMethod.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (pluginState_ON_DEMAND != null) {
                setPluginStateMethod.invoke(s, pluginState_ON_DEMAND);
            }
        } catch (Exception e) {
        }
    }

    WebChromeClientBase webChromeClient = new WebChromeClientBase();
    ValueCallback<Uri> mCallBack;

    public class WebChromeClientBase extends WebChromeClient {


        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            // TODO Auto-generated method stub
//			return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            newWebView = new WebView(BrowserWapActivity.this);
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    newWebViewShow = true;
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // TODO Auto-generated method stub
                    showDialog();
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    dismissdialog();
                    super.onPageFinished(view, url);
                }


            });
//		 Toast.makeText(getApplicationContext(), "OnCreateWindow", Toast.LENGTH_LONG).show();
            return true;
        }

        public void onProgressChanged(WebView view, int newProgress) {
//			mHorizontalProgress.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        // For Android 3.0-4.0
        public void openFileChooser(ValueCallback<Uri> callBack, String acceptType) {
            if (mCallBack != null) return;
            mCallBack = callBack;
            // selectImage();// 弹出系统图片选择器
            // 2套方式 任选其一
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> callBack) {
            openFileChooser(callBack, "");
        }

        // For Android > 4.0
        public void openFileChooser(ValueCallback<Uri> callBack, String acceptType, String
				capture) {
            openFileChooser(callBack, acceptType);
        }

        @SuppressLint("NewApi")
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (isWebchat() && consoleMessage.message().contains("Uncaught ReferenceError")) {
                appWebView.loadUrl(widgetUUid);
            }
            return super.onConsoleMessage(consoleMessage);
        }
    }

    ;

    WebViewClientBase webViewClient = new WebViewClientBase();

    public class WebViewClientBase extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			LogUtils.d("whty", "shouldOverrideUrlLoading");
//			view.loadUrl(url);

//			return super.shouldOverrideUrlLoading(view, url);
            view.loadUrl(url);
//			if(url.startsWith("newtab:")){
//				String realUrl=url.substring(7,url.length());
//				Intent it = new Intent(Intent.ACTION_VIEW);
//				it.setData(Uri.parse(realUrl));
//				BrowserWapActivity.this.startActivity(it);
////				view.loadUrl(realUrl);
//			}else{
//				view.loadUrl(url);
//			}
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            showDialog();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//			LogUtils.d("whty", "onPageFinished");
            dismissdialog();
//			view.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if
// (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link
// .getAttribute('target'); if (target && target == '_blank') {link.setAttribute('target',
// '_self');link.href = 'newtab:'+link.href;}}}");
//			super.onPageFinished(view, url);
        }

        /**
         * 加载一个网页时，先加载他的HTML文档，再根据HTML文档中的标签，加载链接的资源， 加载这些资源时，会调用这个函数
         */
        @Override
        public void onLoadResource(WebView view, String url) {
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            cancelMsg.sendToTarget();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
				failingUrl) {
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.webkit.WebViewClient#onReceivedSslError(android.webkit.WebView
         * , android.webkit.SslErrorHandler, android.net.http.SslError)
         */
        @SuppressLint("NewApi")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			ToolHelper.dissDialog();
            dismissdialog();
//			ToolHelper.toast(getActivity(), "数据加载失败!");
//			NewToast.makeToast(getActivity(),"数据加载失败!", NewToast.SHOWTIME).show();
            ToastUtil.show(BrowserWapActivity.this, "数据加载失败!");
            super.onReceivedSslError(view, handler, error);
//			handler.proceed();
        }
    }

    ;

    /**
     * 是否在在线客服页面
     *
     * @return
     */
    public boolean isWebchat() {
        if (widgetUUid != null && widgetUUid.contains(WEB_CHAT_TAG)) {
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//		case R.id.lly_back_btn:
//			if (widgetUUid.startsWith("http://wap.js")) {
//				finish();
//				break;
//			}
//			if(newWebViewShow)
//        	{
//        		if(newWebView.canGoBack())
//                {
//        			newWebView.goBack();//返回上一页面
////        			wv.removeView(newWebView);
//                }
//                else
//                {
//                	newWebViewShow = false;
//                	appWebView.removeView(newWebView);
//                	newWebView = null;
//                }
//        	}
//			else
//			{
//				if(appWebView.canGoBack())
//	            {
//	            	appWebView.goBack();//返回上一页面
//	            }
//	            else
//	            {
//	                BrowserWapActivity.this.finish();//退出程序
//	            }
//			}
//			break;
            default:
                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (widgetUUid.startsWith("http://wap.js")) {
                finish();
                return true;
            }
            if (newWebViewShow) {
                if (newWebView.canGoBack()) {
                    newWebView.goBack();//返回上一页面
//	        			wv.removeView(newWebView);
                    return true;
                } else {
                    newWebViewShow = false;
                    appWebView.removeView(newWebView);
                    newWebView = null;
                    return true;
                }
            } else {
                if (appWebView.canGoBack()) {
                    appWebView.goBack();//返回上一页面
                    return true;
                } else {
                    BrowserWapActivity.this.finish();//退出程序
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    protected void showDialog(String msg) {
        if (this.isFinishing()) {
            return;
        }
//			if (dialog == null)
//				dialog = new LoadingDialog(this, R.style.Loading);
//			dialog.setMessage(msg);
//			if (!dialog.isShowing())
//				dialog.show();
    }

    protected void showDialog() {
        showDialog("数据加载中...");
    }

    protected void dismissdialog() {
//			if (dialog != null)
//				dialog.dismiss();
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (appWebView != null) {
            appWebView.clearCache(true);
            appWebView.destroy();
        }
    }

}

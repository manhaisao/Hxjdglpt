package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 组织架构
 * Created by dbz on 2017/7/25.
 */
@ContentView(R.layout.aty_cjjj)
public class FwmdmWebViewActivity extends BaseActivity {
    @ViewInject(R.id.cjjj_webview)
    WebView webView;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        type = Integer.valueOf(getIntent().getStringExtra("type"));
        initData();
        initView();
        loadWeb();
    }


    public void loadWeb() {
        String url = "";
        switch (type) {
            case 1:
                url = ConstantUtil.BASE_URL + "/hxfc_app/hxfc-bzcy.html";
                break;
            case 2:
                url = ConstantUtil.BASE_URL + "/hxfc_app/hxfc-bmzz.html";
                break;
        }

        // 获取WebView的设置
        WebSettings webSettings = webView.getSettings();
        // 将JavaScript设置为可用，这一句话是必须的，不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        //给webview添加JavaScript接口
        webView.addJavascriptInterface(new JsInterface(), "control");
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setUseWideViewPort(true); //自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);
    }

    public class JsInterface {
        @JavascriptInterface
        public void showToast(String toast) {
            ToastUtil.show(FwmdmWebViewActivity.this, toast);
        }

        public void log(final String msg) {
            webView.post(new Runnable() {

                @Override
                public void run() {
                    webView.loadUrl("javascript log(" + "'" + msg + "'" + ")");

                }
            });
        }
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
}

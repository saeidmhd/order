package com.mahak.order;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahak.order.common.Notification;
import com.mahak.order.storage.RadaraDb;

public class WebViewActivity extends BaseActivity {

    private WebView webview;
    private RadaraDb radaraDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //config actionbar___________________________________________
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        TextView tvPageTitle = (TextView) view.findViewById(R.id.actionbar_title);
        tvPageTitle.setText("");
        getSupportActionBar().setCustomView(view);
        //_______________________________________________________________
        init();

        Bundle extras = getIntent().getExtras();
        String url = extras.getString("url");
        long notificationId = extras.getLong("Id");
        ///////////////////////////////////////////////////
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new CustomWebViewClient());
        webview.loadUrl(url);
        //////////////////////////////////////////////////
        //Update read////////////////////////////////////
        radaraDb.open();
        Notification notification = radaraDb.GetNotification(notificationId);
        if (notification != null) {
            notification.setRead(true);
            radaraDb.UpdateNotification(notification);
        }
        radaraDb.close();
    }

    private void init() {
        webview = (WebView) findViewById(R.id.webView);
        radaraDb = new RadaraDb(mContext);
        WebSettings websetting = webview.getSettings();
        websetting.setBuiltInZoomControls(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);

        webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webview.getSettings().setAppCachePath(appCachePath);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
    }
}

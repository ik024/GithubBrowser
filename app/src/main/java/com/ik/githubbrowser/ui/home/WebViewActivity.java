package com.ik.githubbrowser.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ik.githubbrowser.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    private final static String LOAD_URL = "load_url";

    @BindView(R.id.web_view)
    WebView mWebView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    public static void startActivity(Context context, String loadUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(LOAD_URL, loadUrl);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null || !bundle.containsKey(LOAD_URL) || TextUtils.isEmpty(bundle.getString(LOAD_URL)))
            finish();
        else {
            String loadUrl   = bundle.getString(LOAD_URL);
            ButterKnife.bind(this);

            mWebView.setWebViewClient(new MyBrowser());

            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            mWebView.loadUrl(loadUrl);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }
}

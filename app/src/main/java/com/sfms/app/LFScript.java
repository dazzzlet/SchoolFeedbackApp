package com.sfms.app;

import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


/**
 * Created by truongnln on 04/03/2018.
 */

public class LFScript {
    public static String JS_NAME = "LF";
    private final WebView webview;
    private final FragmentActivity context;

    public LFScript(WebView webView, FragmentActivity activity) {
        this.webview = webView;
        this.context = activity;
        this.webview.addJavascriptInterface(this, JS_NAME);
    }

    @JavascriptInterface
    public void doFeedback(final String id) {
        this.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl(MainWebClient.DETAIL_URL + "?id=" + id);
            }
        });
    }
}

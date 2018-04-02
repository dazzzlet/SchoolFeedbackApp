package com.sfms.app;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends FragmentActivity {

    private WebView wvDisplay;
    private MainWebClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.wvDisplay = (WebView) findViewById(R.id.wvDisplay);
        this.client = new MainWebClient(getApplicationContext());

        this.wvDisplay.setWebViewClient(client);
        WebSettings webSettings = this.wvDisplay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.wvDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        this.wvDisplay.loadUrl("http://sfms.com/list_feedback.html");
    }
}

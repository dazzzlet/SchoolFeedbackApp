package com.sfms.app;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends FragmentActivity {

    private WebView wvDisplay;
    private MainWebClient client;
    private FeedbackApi feedbackApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.wvDisplay = (WebView) findViewById(R.id.wvDisplay);
        this.feedbackApi = ApiFactory.createApi(FeedbackApi.class);
        this.client = new MainWebClient(this, this.wvDisplay, feedbackApi);

        WebSettings webSettings = this.wvDisplay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.wvDisplay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        this.wvDisplay.loadUrl(MainWebClient.LOGIN_URL);
    }

    @Override
    public void onBackPressed() {
        this.client.back();
    }
}

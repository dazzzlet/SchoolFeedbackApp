package com.sfms.app;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by truongnln on 03/21/2018.
 */

public class MainWebClient extends WebViewClient {
    private static final String LOGIN_VIEW_URL = "/login.html";
    private static final String LOGIN_CSS_URL = "/login_css.css";
    private static final String FEED_DETAIL_VIEW_URL = "/feedback.html";
    private static final String FEED_DETAIL_CSS_URL = "/feedback_css.css";
    private static final String LIST_FEED_VIEW_URL = "/list_feedback.html";
    private static final String LIST_FEED_CSS_URL = "/list_feedback_css.css";
    private static final String LIST_FEED_API_URL = "/api/pending-feedback";
    private static final String DETAIL_FEED_API_URL = "/api/detail-feedback";
    private static final String MAIN_CSS_URL = "/main_css.css";
    private static final String MAIN_JS_URL = "/main_js.js";
    public static final String LIST_URL = "http://sfms.com/list_feedback.html";
    public static final String DETAIL_URL = "http://sfms.com/feedback.html";
    public static final String LOGIN_URL = "http://sfms.com/login.html";
    private final FeedbackApi feedbackApi;
    private final WebView wvDisplay;
    private final LFScript lf;
    private final FDScript fd;
    private final LGScript lg;
    private Gson gson;
    private final Context mContext;
    private String detailId = "";
    private String currentUrl = "";

    public MainWebClient(FragmentActivity c, WebView wvDisplay, FeedbackApi api) {
        this.mContext = c;
        this.feedbackApi = api;
        this.gson = new Gson();
        this.wvDisplay = wvDisplay;
        wvDisplay.setWebViewClient(this);
        this.lf = new LFScript(wvDisplay, c);
        this.lg = new LGScript(wvDisplay, c, api);
        this.fd = new FDScript(wvDisplay, c, api, lg);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse response = null;
        switch (request.getUrl().getPath()) {
            case LOGIN_VIEW_URL:
                response = getLoginHtmlResponse();
                this.currentUrl = LOGIN_VIEW_URL;
                break;
            case LOGIN_CSS_URL:
                response = getLoginCssResponse();
                break;
            case LIST_FEED_VIEW_URL:
                response = getListFeedHtmlResponse();
                this.currentUrl = LIST_FEED_VIEW_URL;
                break;
            case LIST_FEED_CSS_URL:
                response = getListFeedCssResponse();
                break;
            case FEED_DETAIL_VIEW_URL:
                this.detailId = request.getUrl().getQueryParameter("id");
                response = getFeedDetailHtmlResponse();
                this.currentUrl = FEED_DETAIL_VIEW_URL;
                break;
            case FEED_DETAIL_CSS_URL:
                response = getFeedDetailCssResponse();
                break;
            case MAIN_CSS_URL:
                response = getMainCssResponse();
                break;
            case MAIN_JS_URL:
                response = getMainJsResponse();
                break;
            case LIST_FEED_API_URL:
                response = getListFeedApiResponse();
                break;
            case DETAIL_FEED_API_URL:
                response = getDetailFeedApiResponse();
                break;
        }
        return response;
    }

    private WebResourceResponse getDetailFeedApiResponse() {
        Call<JsonObject> callback = this.feedbackApi.getFeedback(this.lg.getUsername(), this.detailId);
        String rs;
        try {
            Response<JsonObject> response = callback.execute();
            rs = gson.toJson(response.body());
        } catch (IOException e) {
            rs = "[]";
            e.printStackTrace();
        }
        return getJsonResponse(rs);
    }

    private WebResourceResponse getListFeedApiResponse() {
        Call<JsonArray> callback = this.feedbackApi.getFeedbacks(this.lg.getUsername());
        String rs;
        try {
            Response<JsonArray> response = callback.execute();
            rs = gson.toJson(response.body());
        } catch (IOException e) {
            rs = "[]";
            e.printStackTrace();
        }
        return getJsonResponse(rs);
    }

    private WebResourceResponse getLoginHtmlResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.login);
        return new WebResourceResponse("text/html", "UTF-8", input);
    }

    private WebResourceResponse getLoginCssResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.login_css);
        return new WebResourceResponse("text/css", "UTF-8", input);
    }

    private WebResourceResponse getListFeedHtmlResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.list_feedback);
        return new WebResourceResponse("text/html", "UTF-8", input);
    }

    private WebResourceResponse getListFeedCssResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.list_feedback_css);
        return new WebResourceResponse("text/css", "UTF-8", input);
    }

    private WebResourceResponse getFeedDetailHtmlResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.feedback);
        return new WebResourceResponse("text/html", "UTF-8", input);
    }

    private WebResourceResponse getFeedDetailCssResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.feedback_css);
        return new WebResourceResponse("text/css", "UTF-8", input);
    }

    private WebResourceResponse getMainCssResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.main_css);
        return new WebResourceResponse("text/css", "UTF-8", input);
    }

    private WebResourceResponse getMainJsResponse() {
        InputStream input = mContext.getResources().openRawResource(R.raw.main_js);
        return new WebResourceResponse("application/javascript", "UTF-8", input);
    }

    public static WebResourceResponse getJsonResponse(String jsonData) {
        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(jsonData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            input = new ByteArrayInputStream("[]".getBytes());
        }
        WebResourceResponse response = new WebResourceResponse("application/json", "UTF-8", input);
        return response;
    }

    public void back() {
        if (this.currentUrl.equals(FEED_DETAIL_VIEW_URL)) {
            this.wvDisplay.loadUrl(MainWebClient.LIST_URL);
        }
    }
}

package com.sfms.app;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by truongnln on 03/21/2018.
 */

public class MainWebClient extends WebViewClient {
    private static final String FEED_DETAIL_VIEW_URL = "/feedback.html";
    private static final String FEED_DETAIL_CSS_URL = "/feedback_css.css";
    private static final String LIST_FEED_VIEW_URL = "/list_feedback.html";
    private static final String LIST_FEED_CSS_URL = "/list_feedback_css.css";
    private static final String LIST_FEED_API_URL = "/api/pending-feedback";
    private static final String MAIN_CSS_URL = "/main_css.css";
    private static final String MAIN_JS_URL = "/main_js.js";
    private Gson gson;
    private final Context mContext;

    public MainWebClient(Context c) {
        this.mContext = c;
        this.gson = new Gson();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse response = null;
        switch (request.getUrl().getPath()) {
            case LIST_FEED_VIEW_URL:
                response = getListFeedHtmlResponse();
                break;
            case LIST_FEED_CSS_URL:
                response = getListFeedCssResponse();
                break;
            case FEED_DETAIL_VIEW_URL:
                response = getFeedDetailHtmlResponse();
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
        }
        return response;
    }

    private WebResourceResponse getListFeedApiResponse() {
        return getJsonResponse("[{\"id\":1,\"feedbackName\":\"Feedback 1\",\"startDate\":\"2018/01/02\"},{\"id\":2,\"feedbackName\":\"Feedback 2\",\"startDate\":\"2018/01/02\"}]");
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

}

package com.sfms.app;

import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnln on 04/03/2018.
 */

public class FDScript {
    public static String JS_NAME = "FD";
    private final WebView webview;
    private final FragmentActivity context;
    private final FeedbackApi api;
    private final Gson gson;

    public FDScript(WebView webView, FragmentActivity activity, FeedbackApi api) {
        this.webview = webView;
        this.context = activity;
        this.webview.addJavascriptInterface(this, JS_NAME);
        this.api = api;
        this.gson = new Gson();
    }

    @JavascriptInterface
    public void save(String feedbackJson) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), feedbackJson);
        Call<JsonObject> callback = this.api.saveFeedback(body);
        callback.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadUrl(MainWebClient.LIST_URL);
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context,"Fail to submit your feedback! please try again!", Toast.LENGTH_LONG);
                        toast.show();
                        //webview.loadUrl(MainWebClient.LIST_URL);
                    }
                });
            }
        });
    }

    @JavascriptInterface
    public void back() {
        this.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl(MainWebClient.LIST_URL);
            }
        });
    }
}

package com.sfms.app;

import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnln on 04/03/2018.
 */

public class LGScript implements ILogin {
    private static String JS_NAME = "LG";
    private final WebView webview;
    private final FragmentActivity context;
    private final FeedbackApi api;
    private final Gson gson;
    private String username;

    public String getUsername() {
        return username;
    }

    public LGScript(WebView webView, FragmentActivity activity, FeedbackApi api) {
        this.webview = webView;
        this.context = activity;
        this.webview.addJavascriptInterface(this, JS_NAME);
        this.api = api;
        this.gson = new Gson();
    }

    @JavascriptInterface
    public void login(final String loginJson) {
        final JsonObject loginObject = this.gson.fromJson(loginJson, JsonObject.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), loginJson);
        Call<JsonObject> callback = this.api.login(body);
        callback.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        username = loginObject.get("username").getAsString();
                        webview.loadUrl(MainWebClient.LIST_URL);
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, "Login fail! please try again!", Toast.LENGTH_LONG);
                        toast.show();
                        webview.loadUrl("javascript:showLoginMessage('Username or password is invalid!')");
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

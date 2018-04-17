package com.sfms.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by truongnln on 04/03/2018.
 */

public interface FeedbackApi {
    @GET("feedbacks/conduct-mobile")
    Call<JsonArray> getFeedbacks();
    @GET("feedbacks/conduct-mobile/{id}")
    Call<JsonObject> getFeedback(@Path("id") String id);
    @POST("conduct-feedback/save-mobile")
    Call<JsonObject> saveFeedback(@Body RequestBody feedback);
}

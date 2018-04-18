package com.sfms.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by truongnln on 04/03/2018.
 */

public interface FeedbackApi {
    @GET("api/feedbacks/conduct-mobile")
    Call<JsonArray> getFeedbacks(@Header("username") String username);

    @GET("api/feedbacks/conduct-mobile/{id}")
    Call<JsonObject> getFeedback(@Header("username") String username, @Path("id") String id);

    @POST("api/conduct-feedback/save-mobile")
    Call<JsonObject> saveFeedback(@Header("username") String username, @Body RequestBody feedback);

    @POST("api/mobile/login")
    Call<JsonObject> login(@Body RequestBody login);
}

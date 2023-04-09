package com.example.dartero.database;

import com.example.dartero.BuildConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ScoreboardAPI {
    @Headers({"Accept: application/json", "apikey:" + BuildConfig.API_KEY})
    @GET("scoreboard")
    Call<List<Score>> getAllScores(@Query("select") String select);

    @Headers({"Accept: application/json", "apikey:" + BuildConfig.API_KEY})
    @GET("scoreboard")
    Call<Score> getScoresByUsername(@Query("username") String username, @Query("select") String select);

    @POST("scoreboard")
    Call<Score> createScore(@Body Score score);
}

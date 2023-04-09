package com.example.dartero.database;

import com.example.dartero.BuildConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ScoreboardAPI is an interface class for REST API calls to Scoreboard in database
 */
public interface ScoreboardAPI {
    @Headers({"Accept: application/json", "apikey:" + BuildConfig.API_KEY})
    @GET("scoreboard")
    Call<List<Scoreboard>> getAllScores(@Query("select") String select);

    @Headers({"Accept: application/json", "apikey:" + BuildConfig.API_KEY})
    @GET("scoreboard")
    Call<Scoreboard> getScoresByUsername(@Query("username") String username, @Query("select") String select);

    @Headers({"apikey:" + BuildConfig.API_KEY})
    @POST("scoreboard")
    Call<Scoreboard> createScore(@Body Scoreboard score);
}

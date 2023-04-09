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
 * UserAPI is an interface class for REST API calls to Users in database
 */
public interface UserAPI {
    @Headers({"Accept: application/json", "apikey:" + BuildConfig.API_KEY})
    @GET("users")
    Call<List<User>> getAllUsers();

    @Headers({"Accept: application/json", "apikey:" + BuildConfig.API_KEY})
    @GET("users")
    Call<User> getUserByUsername(@Query("username") String username, @Query("select") String select);

    @Headers({"apikey:" + BuildConfig.API_KEY})
    @POST("users")
    Call<User> createUser(@Body User user);
}

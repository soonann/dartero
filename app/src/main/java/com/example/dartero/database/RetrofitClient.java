package com.example.dartero.database;

import com.example.dartero.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create a Singleton class for API calls to backend
 */
public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String API_URL = BuildConfig.API_URL;

    public RetrofitClient() {}

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

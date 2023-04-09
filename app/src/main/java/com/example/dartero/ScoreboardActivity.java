package com.example.dartero;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartero.database.Score;
import com.example.dartero.database.ScoreboardAPI;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoreboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScoreAdapter scoreAdapter;
    private final String API_URL = BuildConfig.API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        recyclerView = findViewById(R.id.recyclerViewScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ScoreboardAPI scoreboardAPI = retrofit.create(ScoreboardAPI.class);

        Call<List<Score>> call = scoreboardAPI.getAllScores("username,score");
        call.enqueue(new Callback<List<Score>>() {
            @Override
            public void onResponse(Call<List<Score>> call, Response<List<Score>> response) {
                if (!response.isSuccessful()) {
                    // Handle errors
                    Log.d("Response", response.toString());
                    return;
                }
                List<Score> scores = response.body();
                Collections.sort(scores);
                Log.d("Response", response.body().toString());
                scoreAdapter = new ScoreAdapter(scores);
                recyclerView.setAdapter(scoreAdapter);
            }

            @Override
            public void onFailure(Call<List<Score>> call, Throwable t) {
                Log.d("Response", t.toString());
            }
        });
    }
}


package com.example.dartero;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartero.database.RetrofitClient;
import com.example.dartero.database.Score;
import com.example.dartero.database.ScoreboardAPI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ScoreboardActivity class for displaying high scores of each player.
 */
public class ScoreboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScoreAdapter scoreAdapter;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        recyclerView = findViewById(R.id.recyclerViewScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrofit = RetrofitClient.getRetrofitInstance();

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

                Log.d("Response", response.body().toString());

                scoreAdapter = new ScoreAdapter(getHighScores(scores));
                recyclerView.setAdapter(scoreAdapter);
            }

            @Override
            public void onFailure(Call<List<Score>> call, Throwable t) {
                Log.d("Response", t.toString());
            }
        });
    }

    /**
     * Function to get high scores for each player
     * @param scores A List of Score class object
     * @return A List of high scores for each player
     */
    private List<Score> getHighScores(List<Score> scores) {
        Map<String, Optional<Score>> highScoresMap = scores.stream()
                .collect(Collectors.groupingBy(Score::getUserName,
                        Collectors.maxBy(Comparator.comparing(Score::getScore))));

        List<Score> highScores = highScoresMap.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Collections.sort(highScores);

        return highScores;
    }
}


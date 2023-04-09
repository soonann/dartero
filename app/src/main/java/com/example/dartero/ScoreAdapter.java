package com.example.dartero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartero.database.Scoreboard;

import java.util.List;

/**
 * ScoreAdapter class to bind Score object to RecyclerView
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private List<Scoreboard> scores;

    public ScoreAdapter(List<Scoreboard> scores) {
        this.scores = scores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Scoreboard score = scores.get(position);
        holder.textViewPlayerName.setText(score.getUserName());
        holder.textViewPlayerScore.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPlayerName;
        TextView textViewPlayerScore;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlayerName = itemView.findViewById(R.id.playerName);
            textViewPlayerScore = itemView.findViewById(R.id.playerScore);
        }
    }
}


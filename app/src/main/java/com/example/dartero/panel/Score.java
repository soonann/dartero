package com.example.dartero.panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.dartero.R;

public class Score {
    private volatile int score;

    private Paint textColor;

    public Score(Context context) {
        score = 0;
        int color = ContextCompat.getColor(context, R.color.Score);
        textColor = new Paint();
        textColor.setColor(color);
        textColor.setTextSize(80);
    }

    public synchronized void addPoint(int point) {
        score = score + point;
    }

    public synchronized void deductPoint(int point) {
        score = score - point;
    }

   public void draw(Canvas canvas) {
        String scoreText = String.format("Score: %d", score);
        canvas.drawText(scoreText,100,200,textColor);
   }

   public int getScore() {
        return score;
   }
}

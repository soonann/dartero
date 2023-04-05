package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;

/**
 * GameOver panel draws Game Over to the screen.
 */
public class GameOver {

    private Context context;
    private Paint wordPaint, bgPaint;

    public GameOver(Context context) {
        this.context = context;

        this.wordPaint = new Paint();
        int wordColor = ContextCompat.getColor(context, R.color.gameOver);
        wordPaint.setColor(wordColor);
        wordPaint.setTextSize(150);

        this.bgPaint = new Paint();
        int bgColor = ContextCompat.getColor(context, R.color.white);
        bgPaint.setColor(bgColor);
        bgPaint.setAlpha(128); // opacity of the background
    }

    public void draw(Canvas canvas) {
        // GameOver background
        float bgLeft = 0;
        float bgTop = 0;
        float bgRight = canvas.getWidth();
        float bgBottom = canvas.getHeight();
        canvas.drawRect(bgLeft, bgTop, bgRight, bgBottom, bgPaint);

        // GameOver words
        String text = "Game Over";
        float x = 350;
        float y = 1500;
        canvas.drawText(text, x, y, wordPaint);
    }
}

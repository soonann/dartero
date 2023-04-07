package com.example.dartero.panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;

import com.example.dartero.R;
import com.example.dartero.objects.Button;

/**
 * GameOver panel draws Game Over to the screen.
 */
public class GameOver {

    private Paint wordPaint, bgPaint;
    private Button button;

    public GameOver(Context context) {
        this.wordPaint = new Paint();
        int wordColor = ContextCompat.getColor(context, R.color.gameOver);
        wordPaint.setColor(wordColor);
        wordPaint.setTextSize(150);

        this.bgPaint = new Paint();
        int bgColor = ContextCompat.getColor(context, R.color.white);
        bgPaint.setColor(bgColor);
        bgPaint.setAlpha(128); // opacity of the background

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int buttonWidth = 400;
        int buttonHeight = 150;

        this.button = new Button(
                context,
                (screenWidth - buttonWidth) / 2,
                screenHeight / 2,
                (screenWidth + buttonWidth) / 2,
                screenHeight / 2 + buttonHeight,
                "RESTART"
        );
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
        float y = canvas.getHeight()/4;
        canvas.drawText(text, x, y, wordPaint);

        button.draw(canvas);
    }

    public boolean handleTouchEvent(float x, float y) {
        if (button.isPressed(x, y)) {
            // Handle the button press
            return true;
        }
        return false;
    }
}

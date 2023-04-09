package com.example.dartero.panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;

import com.example.dartero.R;
import com.example.dartero.objects.Button;

public class Pause {
    private Paint wordPaint, bgPaint;
    private Button resumeButton, quitButton, restartButton;

    public Pause(Context context) {
        this.wordPaint = new Paint();
        int wordColor = ContextCompat.getColor(context, R.color.white);
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

// Set up buttons
        int buttonSpacing = 50;
        int buttonBlockHeight = buttonHeight * 3 + buttonSpacing * 2; // calculate the height of the button block
        int buttonTop = (screenHeight - buttonBlockHeight) / 2; // calculate the top of the button block
        int buttonBottom = buttonTop + buttonHeight; // calculate the bottom of the first button
        this.resumeButton = new Button(
                context,
                (screenWidth - buttonWidth) / 2,
                buttonTop,
                (screenWidth + buttonWidth) / 2,
                buttonBottom,
                "RESUME"
        );

        buttonTop = buttonBottom + buttonSpacing; // calculate the top of the second button
        buttonBottom = buttonTop + buttonHeight; // calculate the bottom of the second button
        this.quitButton = new Button(
                context,
                (screenWidth - buttonWidth) / 2,
                buttonTop,
                (screenWidth + buttonWidth) / 2,
                buttonBottom,
                "QUIT"
        );

        buttonTop = buttonBottom + buttonSpacing; // calculate the top of the third button
        buttonBottom = buttonTop + buttonHeight; // calculate the bottom of the third button
        this.restartButton = new Button(
                context,
                (screenWidth - buttonWidth) / 2,
                buttonTop,
                (screenWidth + buttonWidth) / 2,
                buttonBottom,
                "RESTART"
        );
    }

    public void draw(Canvas canvas) {
        // Pause background
        float bgLeft = 0;
        float bgTop = 0;
        float bgRight = canvas.getWidth();
        float bgBottom = canvas.getHeight();
        canvas.drawRect(bgLeft, bgTop, bgRight, bgBottom, bgPaint);

        // Pause words
        String text = "Paused";
        float textWidth = wordPaint.measureText(text);
        float x = (canvas.getWidth() - textWidth) / 2;
        float y = canvas.getHeight() / 4;
        canvas.drawText(text, x, y, wordPaint);

        // Draw buttons
        resumeButton.draw(canvas);
        quitButton.draw(canvas);
        restartButton.draw(canvas);
    }

    public boolean handleTouchEvent(float x, float y) {
        if (resumeButton.isPressed(x, y)) {
            // Handle the resume button press
            return true;
        } else if (quitButton.isPressed(x, y)) {
            // Handle the quit button press
            return true;
        } else if (restartButton.isPressed(x, y)) {
            // Handle the restart button press
            return true;
        }
        return false;
    }
}


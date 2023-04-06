package com.example.dartero.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.dartero.R;

public class Button {
    private Rect rect;
    private Paint wordPaint, btnPaint;
    private String text;

    public Button(Context context, int left, int top, int right, int bottom, String text) {
        this.rect = new Rect(left, top, right, bottom);

        this.wordPaint = new Paint();
        int wordColor = ContextCompat.getColor(context, R.color.black);
        wordPaint.setColor(wordColor);
        wordPaint.setTextSize(70);

        this.btnPaint = new Paint();
        int btnColor = ContextCompat.getColor(context, R.color.btn);
        btnPaint.setColor(btnColor);

        this.text = text;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, btnPaint);
        float textWidth = wordPaint.measureText(text);
        float x = rect.left + (rect.width() - textWidth) / 2;
        float y = rect.top + (rect.height() + wordPaint.getTextSize()) / 2;
        canvas.drawText(text, x, y, wordPaint);
    }

    public boolean isPressed(float x, float y) {
        return rect.contains((int) x, (int) y);
    }
}

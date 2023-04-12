package com.example.dartero.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.dartero.R;

/**
 * Potion object
 */
public class Potion extends GameObject{


    private Paint potionPaint, textPaint;

    public static final double radius = 60;
    private Player player;

    private boolean consumed;
    private long startTime;

    private Context context;

    public Potion(Context context, Player player) {
        super(Math.random() * 1000, Math.random() * 1000,radius);
        potionPaint = new Paint();
        potionPaint.setColor(ContextCompat.getColor(context, R.color.potion));
        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(context, R.color.black));
        textPaint.setTextSize(100);
        this.context = context;
        this.consumed = false;
        this.player = player;
    }

    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Check if potion is consumed by the player
     */
    private void tryConsume() {
        if (GameObject.isColliding(this,player)) {

            this.consumed = true;
            player.setHealthPoints(player.getHealthPoints()+1);
        }
    }

    /**
     * Make sure the potion appear for 10 seconds,
     * or see whether potion is consumed by the player
     */
    public void run() {
        startTime = System.currentTimeMillis();

        while (!isConsumed() && System.currentTimeMillis() - startTime < 10000) {
            //wait
            tryConsume();
        }

        this.consumed = true;
    }

    /**
     * Draw potion
     * @param canvas The Canvas Object
     */
    @Override
    public void draw(Canvas canvas) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        // allow flashing when 3 seconds left
        if (10000 - elapsedTime <= 3000) {
            if ((elapsedTime / 250) % 2 == 0) {
                potionPaint.setColor(ContextCompat.getColor(context, R.color.flash));
            } else {
                potionPaint.setColor(ContextCompat.getColor(context, R.color.potion));
            }
        }

        canvas.drawCircle((float) positionX,(float) positionY, 60, potionPaint);
        String text = "+";
        Rect result = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), result);
        float textX = (float) positionX - result.width() / 2.0f;
        float textY = (float) positionY + result.height() / 2.0f;
        canvas.drawText(text, textX, textY, textPaint);
    }

    @Override
    public void update() {

    }

}

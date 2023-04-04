package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

/**
 * HealthBar display on screen
 */
public class HealthBar {
    private Player player;
    private int width, height, margin;
    private Paint borderPaint, healthPaint;

    public HealthBar(Context context, Player player) {
        this.player = player;
        this.width = 150;
        this.height = 40;
        this.margin = 2;

        // color the border
        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.bar);
        borderPaint.setColor(borderColor);

        // color the health
        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.health);
        healthPaint.setColor(healthColor);
    }

    public void draw(Canvas canvas) {
        // move along with the player, get position
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();

        // place healthbar above the player
        float distanceToPlayer = 80;

        // total health of the player
        float healthPointsPercentage = (float) player.getHealthPoints()/player.MAX_HEALTH_POINTS;

        // Draw border
        float borderLeft, borderTop, borderRight, borderBtm;
        borderLeft= x - width/2;
        borderRight = x + width/2;
        borderBtm = y - distanceToPlayer;
        borderTop = borderBtm - height;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBtm, borderPaint);

        // Draw health
        float healthLeft, healthTop, healthRight, healthBtm, healthWidth, healthHeight;
        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointsPercentage;
        healthBtm = borderBtm - margin;
        healthTop = healthBtm - healthHeight;
        canvas.drawRect(healthLeft, healthTop, healthRight, healthBtm, healthPaint);
    }

}

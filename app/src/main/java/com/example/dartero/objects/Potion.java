package com.example.dartero.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.dartero.R;

public class Potion extends GameObject{


    private Paint paint;

    public static final double radius = 60;
    private Player player;

    private boolean consumed;

    public Potion(Context context, Player player) {
        super(Math.random() * 1000, Math.random() * 1000,radius);
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.potion));
        this.consumed = false;
        this.player = player;
    }

    public Potion(Context context, double positionX, double positionY, Player player) {
        super(positionX, positionY,radius);
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.potion));
        this.consumed = false;
        this.player = player;
    }


    public boolean isConsumed() {
        return consumed;
    }

    private void tryConsume() {
        if (GameObject.isColliding(this,player)) {
            Log.d("Potion", "consumed ");

            this.consumed = true;
            player.setHealthPoints(player.getHealthPoints()+1);
        }
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        while (!isConsumed() && System.currentTimeMillis() - startTime < 10000) {
            //wait
            Log.d("Potion", "try consume");
            tryConsume();
        }

        this.consumed = true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX,(float) positionY, 60, paint);
    }

    @Override
    public void update() {

    }

}

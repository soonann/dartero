package com.example.dartero.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.dartero.R;

public class Potion extends GameObject implements Runnable {

    private Context context;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private int screenWidth;
    private int screenHeight;
    private Paint paint;

    private Canvas canvas;

    private Player player;

    private boolean consumed;
    private Thread thread;
    private boolean running = false;

    public Potion(Context context, Canvas canvas,double positionX, double positionY, Player player) {
        super(positionX,positionY,60);
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.canvas = canvas;
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.potion));
        this.consumed = false;
        this.player = player;
    }

    private boolean isConsumed() {
        return consumed;
    }

    private void tryConsume() {
        if (GameObject.isColliding(this,player)) {
            Log.d("Potion", "consumed ");

            this.consumed = true;
            player.setHealthPoints(player.getHealthPoints()+1);
        }
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (!isConsumed() && System.currentTimeMillis() - startTime < 10000) {
            //wait
            tryConsume();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX,(float) positionY, 60, paint);
    }

    @Override
    public void update() {

    }

}

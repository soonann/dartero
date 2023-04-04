package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;

    private List<Mob> mobs = new ArrayList<>();
    private List<Dart> darts = new ArrayList<>();

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        joystick = new Joystick(getContext(), getResources().getDisplayMetrics().widthPixels/2, getResources().getDisplayMetrics().heightPixels/6 * 5, 70, 40);
        GameObject.maxX = getResources().getDisplayMetrics().widthPixels;
        GameObject.maxY = getResources().getDisplayMetrics().heightPixels;
        player = new Player(getContext(), getResources().getDisplayMetrics().widthPixels/2,  getResources().getDisplayMetrics().heightPixels/6 * 4, joystick);
        mobs.add(new Mob(getContext(), getResources().getDisplayMetrics().widthPixels/2,  getResources().getDisplayMetrics().heightPixels/6 * 4,  player));
        mobs.add(new Mob(getContext(), getResources().getDisplayMetrics().widthPixels/4,  getResources().getDisplayMetrics().heightPixels/6 * 2, player));
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for (Mob mob: mobs) {
            mob.draw(canvas);
        }
        for (Dart dart: darts) {
            dart.draw(canvas);
        }
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setTextSize(50);
        paint.setColor(color);
        canvas.drawText("UPS: " + averageUPS, 100,100,paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setTextSize(50);
        paint.setColor(color);
        canvas.drawText("FPS: " + averageFPS, 100,200,paint);

    }

    public void update() {
        //Update game state
        joystick.update();
        player.update();

        if (Mob.readyToSpawn()) {
            mobs.add(new Mob(getContext(),player));
        }

        // Update the state of each mob
        for (Mob mob: mobs) {
            mob.update();
        }

        Dart.player=player;
        Dart.mobs=mobs;

        if (Dart.readyToShoot()) {
            darts.add(new Dart(getContext()));
        }

        for (Dart dart: darts) {
            dart.update();
        }


        //Iterate through mobs and check for collision
        Iterator<Mob> iteratorMob = mobs.iterator();
        while (iteratorMob.hasNext()) {
            Mob mob = iteratorMob.next();
            if (GameObject.isColliding(mob, player)) {
                // if there is collision remove current enemy
                iteratorMob.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
            }
        }

        Iterator<Dart> iteratorDart = darts.iterator();
        while (iteratorDart.hasNext()) {
            Dart dart = iteratorDart.next();
            if(GameObject.isColliding(dart,dart.getNearestMob())) {
                iteratorDart.remove();
                mobs.remove(dart.getNearestMob());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
                    // joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }

        return super.onTouchEvent(event);

    }
}
package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.dartero.objects.Dart;
import com.example.dartero.objects.GameObject;
import com.example.dartero.objects.Mob;
import com.example.dartero.objects.Player;
import com.example.dartero.objects.PlayerState;
import com.example.dartero.panel.GameOver;
import com.example.dartero.panel.Joystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game class represents the main game logic and rendering for the Dartero game.
 * It handles player input, game objects, updates game state, and renders the game on the screen.
 * This class extends SurfaceView and implements SurfaceHolder.Callback for drawing and updating game objects.
 *
 * The Game class contains the following game elements:
 * - Player: The main character controlled by the user
 * - Joystick: The on-screen control for moving the player
 * - Mobs: A list of enemy mobs
 * - Darts: A list of darts shot by the player
 * - GameOver: The game over screen displayed when the player loses
 * - GameLoop: The game loop responsible for updating and rendering the game
 *
 * The Game class also handles touch events for controlling the player and restarting the game.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private MediaPlayer shootSound;

    private Joystick joystick;
    private Player player;
    private GameLoop gameLoop;

    private List<Mob> mobs = new ArrayList<>();
    private List<Dart> darts = new ArrayList<>();
    private GameOver gameOver;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameOver = new GameOver(getContext());
        gameLoop = new GameLoop(this, surfaceHolder);

        initGame();

        // Initialize shoot sound
        shootSound = MediaPlayer.create(context, R.raw.pew);
        shootSound.setLooping(false);

        setFocusable(true);
    }

    /**
     * Initializes the game display and settings
     */
    private void initGame() {
        GameObject.maxX = getResources().getDisplayMetrics().widthPixels;
        GameObject.maxY = getResources().getDisplayMetrics().heightPixels;
        joystick = new Joystick(getContext(), getResources().getDisplayMetrics().widthPixels/2, getResources().getDisplayMetrics().heightPixels/6 * 5, 70, 40);
        player = new Player(getContext(), getResources().getDisplayMetrics().widthPixels/2,  getResources().getDisplayMetrics().heightPixels/6 * 4, joystick);
//        mobs.add(new Mob(getContext(), getResources().getDisplayMetrics().widthPixels/2,  getResources().getDisplayMetrics().heightPixels/6 * 4,  player));
        mobs.add(new Mob(getContext(), getResources().getDisplayMetrics().widthPixels/4,  getResources().getDisplayMetrics().heightPixels/6 * 2, player));
    }

    /**
     * Allow reset of game when gameover
     */
    public void resetGame() {
        initGame();
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
        drawFPS(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for (Mob mob: mobs) {
            mob.draw(canvas);
        }
        for (Dart dart: darts) {
            dart.draw(canvas);
        }

        // game over if player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setTextSize(50);
        paint.setColor(color);
        canvas.drawText("FPS: " + averageFPS, 100,100,paint);

    }

    public void update() {
        // Stop updating game when player is dead
        if (player.getHealthPoints() <= 0) {
            return ;
        }

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
            shootSound.start();
            if(GameObject.isColliding(dart,dart.getNearestMob())) {
                iteratorDart.remove();
                mobs.remove(dart.getNearestMob());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Restart button for restarting the game
        if (player.getHealthPoints() <= 0 && event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (gameOver.handleTouchEvent(event.getX(), event.getY())) {
                resetGame();
            }
            return true;
        }

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

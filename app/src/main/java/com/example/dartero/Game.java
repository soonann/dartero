package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.content.Intent;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.dartero.database.Scoreboard;
import com.example.dartero.database.ScoreboardAPI;
import com.example.dartero.objects.Dart;
import com.example.dartero.objects.GameObject;
import com.example.dartero.objects.Mob;
import com.example.dartero.objects.Player;
import com.example.dartero.objects.Potion;
import com.example.dartero.panel.GameOver;
import com.example.dartero.panel.Pause;
import com.example.dartero.panel.Joystick;
import com.example.dartero.panel.Score;
import com.example.dartero.utils.PotionUpdaterPool;
import com.example.dartero.utils.RetrofitClient;
import com.example.dartero.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
 * - pause: An instance of Pause class representing the pause screen.
 * - isPaused: A boolean indicating whether the game is currently paused.
 *
 * The Game class also handles touch events for controlling the player and restarting the game.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private MediaPlayer shootSound;
    private Vibrator vibrator;
    private final VibrationEffect VIBRATION_EFFECT = VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE);
    private final PotionUpdaterPool potionUpdaterPool = new PotionUpdaterPool();

    private Joystick joystick;
    private Player player;
    private GameLoop gameLoop;

    private List<Mob> mobs;
    private List<Dart> darts;
    private List<Potion> potions;
    private GameOver gameOver;

    private Pause pause;

    private boolean isPaused;


    private Score score;

    private final String username;
    private boolean scoreRecorded;  // to check if the score have been recorded
    private static int count = 0;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        username = SharedPreferencesUtils.getName(context);

        gameOver = new GameOver(getContext());
        pause = new Pause(getContext());
        gameLoop = new GameLoop(this, surfaceHolder);
        isPaused = false;
        initGame();

        // Initialize shoot sound
        shootSound = MediaPlayer.create(context, R.raw.pew);
        shootSound.setLooping(false);

        // Initialize Vibrator
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        setFocusable(true);
    }

    /**
     * Initializes the game display and settings
     */
    private void initGame() {
        scoreRecorded = false;
        GameObject.maxX = getResources().getDisplayMetrics().widthPixels;
        GameObject.maxY = getResources().getDisplayMetrics().heightPixels;
        joystick = new Joystick(getContext(), getResources().getDisplayMetrics().widthPixels/2, getResources().getDisplayMetrics().heightPixels/6 * 5, 70, 40);
        player = new Player(getContext(), getResources().getDisplayMetrics().widthPixels/2,  getResources().getDisplayMetrics().heightPixels/6 * 4, joystick);
        isPaused = false;
        score = new Score(getContext());
        darts = new ArrayList<>();
        mobs = new ArrayList<>();
        potions = new ArrayList<>();
    }

    public void quitGame() {

        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }
    public void resumeGame(){
        gameLoop.resumeGame();
    }


    /**
     * Allow reset of game when gameover
     */
    public void resetGame() {
        gameLoop.resumeGame();
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

    /**
     * Render the game objects
     * @param canvas The Canvas object
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isPaused) {
            pause.draw(canvas);
        } else {
            player.draw(canvas);
            joystick.draw(canvas);
            score.draw(canvas);
            drawButton(canvas);
            for (Mob mob : mobs) {
                mob.draw(canvas);
            }
            for (Dart dart : darts) {
                dart.draw(canvas);
            }
            for (Potion potion: potions) {
                potion.draw(canvas);
            }
        }

        // game over if player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas);
        }
    }

    /**
     * Display FPS of the game
     * @param canvas The Canvas object
     * @param averageFPS Average FPS for rendering the game objects
     */
    public void drawFPS(Canvas canvas, double averageFPS) {
        String fpsText = String.format("FPS: %.2f", averageFPS);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setTextSize(50);
        paint.setColor(color);
        canvas.drawText(fpsText, 100,100,paint);
    }

    public void drawButton(Canvas canvas) {
        // Define the button dimensions and position
        int buttonWidth = 200;
        int buttonHeight = 100;
        int buttonPadding = 20;
        int buttonX = canvas.getWidth() - buttonWidth - buttonPadding;
        int buttonY = buttonPadding;

        // Draw the button background
        Paint buttonPaint = new Paint();
        int buttoncolor = ContextCompat.getColor(getContext(), R.color.purple_200);
        buttonPaint.setColor(buttoncolor);
        canvas.drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, buttonPaint);

        // Draw the button text
        Paint textPaint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        textPaint.setColor(color);
        textPaint.setTextSize(40);
        String buttonText = "Pause";
        canvas.drawText(buttonText, buttonX + (buttonWidth/2) - (textPaint.measureText(buttonText)/2), buttonY + (buttonHeight/2), textPaint);
    }


    /**
     * Updates the game logic
     */

    public void update() {
        // Stop updating game when player is dead
        if (player.getHealthPoints() <= 0) {
            if (!scoreRecorded) {
                createScoreRecord();
            }
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

        if (Dart.readyToShoot() && darts.size() < 1) {
            Log.d("dart shot!", Integer.toString(count++));
            shootSound.start();
            darts.add(new Dart(getContext()));
        }

        for (Dart dart: darts) {
            dart.update();
        }

        //Iterator through potions and check for consumed
        Iterator<Potion> iteratorPotion = potions.iterator();
        while (iteratorPotion.hasNext()) {
            Potion potion = iteratorPotion.next();
            if(potion.isConsumed()) {
                iteratorPotion.remove();
            }
        }


        //Iterate through mobs and check for collision
        Iterator<Mob> iteratorMob = mobs.iterator();
        while (iteratorMob.hasNext()) {
            Mob mob = iteratorMob.next();
            if (GameObject.isColliding(mob, player)) {
                // if there is collision remove current enemy
                if (vibrator != null && vibrator.hasVibrator()) {
                    // Vibrate for 200 milliseconds
                    vibrator.vibrate(VIBRATION_EFFECT);
                    Log.d("Vibration", "Vibrate for 200 ms");
                } else {
                    Log.d("Vibration", "Vibrator is not available or device does not support vibration");
                }
                iteratorMob.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
                score.deductPoint(5);
            }
        }

        /**
         * Here when a mob is killed 10 points will
         * be added and a potion will be spawned using a thread
         * from the threadpool
         */
        Iterator<Dart> iteratorDart = darts.iterator();
        while (iteratorDart.hasNext()) {
            Dart dart = iteratorDart.next();
            if(GameObject.isColliding(dart,dart.getNearestMob())) {
                iteratorDart.remove();
                Mob mob = dart.getNearestMob();
                mob.setHealthPoints(mob.getHealthPoints() - 1);
                if(mob.getHealthPoints() < 1) {
                    score.addPoint(10);
                    mobs.remove(mob);
                    createPotionAndStartThread();
                }
            }
        }
    }

    /**
     * Use ExecutorService to assign a thread for running the Potion threads
     */
    public void createPotionAndStartThread() {
        Potion potion = new Potion(getContext(), player);
        potions.add(potion);
        potionUpdaterPool.submit(potion::run);
    }

    /**
     * Send a POST request and create a score record in database
     */
    private void createScoreRecord() {
        scoreRecorded = true;
        int gameScore = score.getScore();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();

        ScoreboardAPI scoreboardAPI = retrofit.create(ScoreboardAPI.class);
        Call<Scoreboard> call = scoreboardAPI.createScore(new Scoreboard(username, gameScore));
        call.enqueue(new Callback<Scoreboard>() {
            @Override
            public void onResponse(Call<Scoreboard> call, Response<Scoreboard> response) {
                if (!response.isSuccessful()) {
                    Log.d("Create scoreboard response", response.toString());
                }
                post(() -> Toast.makeText(getContext(), "Score recorded", Toast.LENGTH_SHORT).show());
                return ;
            }

            @Override
            public void onFailure(Call<Scoreboard> call, Throwable t) {
                Log.d("Create user failed response", t.toString());
                return ;
            }
        });
    }

    /**
     * Handles the touch events of the game in the UI Thread
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pause button for pausing the game
        int buttonWidth = 200;
        int buttonHeight = 100;
        int buttonPadding = 20;
        int buttonX = getWidth() - buttonWidth - buttonPadding;
        int buttonY = buttonPadding;
        if (event.getX() >= buttonX && event.getX() <= buttonX + buttonWidth &&
                event.getY() >= buttonY && event.getY() <= buttonY + buttonHeight) {
            // Set isPaused to true
            isPaused = true;
            gameLoop.pauseGame();
            return true;
        }
        if(isPaused && pause.restartButton.isPressed(event.getX(), event.getY())){
            isPaused = false;
            gameLoop.resumeGame();
            resetGame();
        }

        if(isPaused && pause.resumeButton.isPressed(event.getX(), event.getY())){
            isPaused = false;
            gameLoop.resumeGame();
        }

        if(isPaused && pause.quitButton.isPressed(event.getX(), event.getY())){
            isPaused = false;
            quitGame();
        }


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

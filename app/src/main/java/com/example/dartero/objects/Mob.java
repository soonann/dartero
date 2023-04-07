package com.example.dartero.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.dartero.GameLoop;
import com.example.dartero.R;
import com.example.dartero.panel.HealthBar;

public class Mob extends ObjectsWithHealth {
    public static int MAX_HEALTH_POINTS = 2;
    private static double SPEED_MULTIPLIER = 0.6;
    private static final double SPEED_PIXEL_PER_SECOND = Player.SPEED_PIXEL_PER_SECOND * SPEED_MULTIPLIER;
    private static final double MAX_SPEED = SPEED_PIXEL_PER_SECOND / 100;
    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60.0;
    private static final double UPDATES_PER_SPAWN = 100/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;

    private HealthBar healthBar;



    public static final double radius = 50;
    private Paint paint;

    private Player player;

    // TODO: first mob spawn bug; spawn further
    public Mob(Context context, double positionX, double positionY, Player player) {
        super(positionX,positionY,radius,MAX_HEALTH_POINTS);

        this.player = player;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.mob));
        this.healthBar = new HealthBar(context,this);
        this.healthPoints = MAX_HEALTH_POINTS;
    }

    public Mob(Context context, Player player) {
        super(Math.random() * 1000, Math.random() * 1000, radius, MAX_HEALTH_POINTS);
        this.player = player;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.mob));
        this.healthBar = new HealthBar(context, this);
    }

    /**
     * ready to spawn checks if a mob should spawn, according to spawns per minute
     * @return
     */
    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn--;
            return false;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
        healthBar.draw(canvas);
    }

    @Override
    public void update() {
        // Chase the player logic
        // Player's location
        double distToPlayerX = player.getPositionX() - positionX;
        double distToPlayerY = player.getPositionY() - positionY;

        //Calculate distance between player and mob
        double distToPlayer = getDistBetweenGameObjects(this,player);

        //Direction from mob to player
        double directionX = distToPlayerX/distToPlayer;
        double directionY = distToPlayerY/distToPlayer;

        if (distToPlayer > 0) {
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }

        //Update position to be within frame
        positionX = positionX + velocityX;
        if(positionX > maxX) {
            positionX = maxX;
        } else if (positionX < 0) {
            positionX = 0;
        }
        positionY = positionY + velocityY;
        if(positionY > maxY) {
            positionY = maxY;
        } else if (positionY < 0) {
            positionY = 0;
        }


    }



}

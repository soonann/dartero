package com.example.dartero.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.dartero.GameLoop;
import com.example.dartero.R;

import java.util.List;


public class Dart extends GameObject {
    private static double SPEED_MULTIPLIER = 2.0;
    private static final double SPEED_PIXEL_PER_SECOND = Player.SPEED_PIXEL_PER_SECOND * SPEED_MULTIPLIER;
    private static final double MAX_SPEED = SPEED_PIXEL_PER_SECOND / 100;
    private static final double SHOTS_PER_MINUTE = 40;
    private static final double SHOTS_PER_SECOND = SHOTS_PER_MINUTE/60.0;
    private static final double UPDATES_PER_SHOT = 100/SHOTS_PER_SECOND;
    private static double updatesUntilNextShot = UPDATES_PER_SHOT;

    private Mob nearestMob;
    public static List<Mob> mobs;

    public static final double radius = 20;
    private Paint paint;
    public static Player player;

    public Dart(Context context) {
        super(player.positionX,player.positionY,radius);
        paint = new Paint();
        this.mobs = mobs;
        this.player = player;
        // Find the nearestMob on initialization of the dart
        Mob nearestMob = mobs.get(0);
        //Minimum distance between mob and dart
        Double minDistBetMobAndDart = GameObject.getDistBetweenGameObjects(this, nearestMob);
        for (Mob mob: mobs) {
            Double distBetMobAndDart = GameObject.getDistBetweenGameObjects(this, nearestMob);
            if (distBetMobAndDart < minDistBetMobAndDart) {
                nearestMob = mob;
                minDistBetMobAndDart = distBetMobAndDart;
            }
        }
        this.nearestMob = nearestMob;
        paint.setColor(ContextCompat.getColor(context, R.color.dart));
    }

    /**
     * Checks if dart should fire when there are mobs and the player is standing still
     * @return Boolean
     */
    public static boolean readyToShoot() {
        if (updatesUntilNextShot <= 0 && mobs.size() > 0 && player.getPlayerState().getState() == PlayerState.State.NOT_MOVING) {
            updatesUntilNextShot += UPDATES_PER_SHOT;
            return true;
        } else {
            updatesUntilNextShot--;
            return false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        float left = (float) positionX;
        float top = (float) positionY;
        float right = (float) positionX + 20;
        float bottom = (float) positionY + 30;

        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public void update() {

        //distToMob
        double distToMob = getDistBetweenGameObjects(this,nearestMob);

        //Direction from bullet to mob
        double distToMobX = nearestMob.positionX - positionX;
        double distToMobY = nearestMob.positionY - positionY;
        double directionX = distToMobX/distToMob;
        double directionY = distToMobY/distToMob;

        if (distToMob > 0) {
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

    public Mob getNearestMob() {
        return nearestMob;
    }


}

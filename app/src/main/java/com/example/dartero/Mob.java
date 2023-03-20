package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Mob extends GameObject{
    private static double SPEED_MULTIPLIER = 0.2;
    private static final double SPEED_PIXEL_PER_SECOND = Player.SPEED_PIXEL_PER_SECOND * SPEED_MULTIPLIER;
    private static final double MAX_SPEED = SPEED_PIXEL_PER_SECOND / GameLoop.MAX_UPS;



    private double radius;
    private Paint paint;

    private Player player;

    public Mob(Context context, double positionX, double positionY, double radius, double maxX, double maxY, Player player) {
        super(positionX,positionY,maxX,maxY);
        this.radius = radius;
        this.player = player;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.mob));
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
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

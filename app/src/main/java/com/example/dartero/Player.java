package com.example.dartero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Player extends GameObject{


    public static final double SPEED_PIXEL_PER_SECOND = 1000;
    private static final double MAX_SPEED = SPEED_PIXEL_PER_SECOND / GameLoop.MAX_UPS;



    private double radius;
    private Paint paint;

    private final Joystick joystick;


    public Player(Context context, double positionX, double positionY, double radius, double maxX, double maxY, Joystick joystick) {
        super(positionX,positionY,maxX,maxY);
        this.radius = radius;
        this.joystick = joystick;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.player));
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

    public void update() {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

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

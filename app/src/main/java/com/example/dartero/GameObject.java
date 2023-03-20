package com.example.dartero;

import android.graphics.Canvas;

public abstract class GameObject {
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    protected double maxX;
    protected double maxY;

    public GameObject(double positionX, double positionY, double maxX, double maxY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    public double getDistBetweenGameObjects(GameObject go1, GameObject go2) {
        return Math.sqrt(Math.pow(go1.positionX-go2.positionX,2) + Math.pow(go1.positionY-go2.positionY, 2));
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}

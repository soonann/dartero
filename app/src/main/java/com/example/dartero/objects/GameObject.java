package com.example.dartero.objects;

import android.graphics.Canvas;

public abstract class GameObject {
    public double positionX;
    public double positionY;
    public double velocityX;
    public double velocityY;
    public static double maxX;
    public static double maxY;

    protected double radius;

    public GameObject(double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
    }

    public static boolean isColliding(GameObject go1, GameObject go2) {
        if (getDistBetweenGameObjects(go1, go2) <= go1.getRadius() + go2.getRadius()) {
            return true;
        }
        return false;
    }

    private double getRadius() {
        return radius;
    }

    public static double getDistBetweenGameObjects(GameObject go1, GameObject go2) {
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

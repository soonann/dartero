package com.example.dartero.objects;


public abstract class ObjectsWithHealth extends GameObject{
    public static int MAX_HEALTH_POINTS;

    public abstract int getHealthPoints();

    public abstract void setHealthPoints(int healthPoints);



    public ObjectsWithHealth(double positionX, double positionY, double radius) {
        super(positionX, positionY, radius);
    }


}

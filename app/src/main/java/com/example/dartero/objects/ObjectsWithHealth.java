package com.example.dartero.objects;


public abstract class ObjectsWithHealth extends GameObject{


    public int maxHealthPoints;
    public volatile int healthPoints;

    public int getHealthPoints() {
        return healthPoints;
    }

    public synchronized void setHealthPoints(int healthPoints) {
        if (healthPoints >= 0) {
            this.healthPoints = healthPoints;
        }
        if (healthPoints > maxHealthPoints) {
            this.healthPoints = maxHealthPoints;
        }

    }
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }



    public ObjectsWithHealth(double positionX, double positionY, double radius, int healthPoints) {
        super(positionX, positionY, radius);
        this.healthPoints = healthPoints;
        setMaxHealthPoints(healthPoints);
    }


}

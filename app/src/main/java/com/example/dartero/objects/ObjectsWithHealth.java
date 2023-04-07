package com.example.dartero.objects;


public abstract class ObjectsWithHealth extends GameObject{


    public int maxHealthPoints;
    public int healthPoints;

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if (healthPoints >= 0) {
            this.healthPoints = healthPoints;
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

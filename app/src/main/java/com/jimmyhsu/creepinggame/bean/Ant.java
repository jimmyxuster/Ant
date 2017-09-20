package com.jimmyhsu.creepinggame.bean;

/**
 * Created by xuyanzhe on 19/9/17.
 */
public class Ant {

    public static final int DIR_LEFT = -1;
    public static final int DIR_RIGHT = 1;
    public static final int DEFAULT_VELOCITY = 5;

    private int id;
    private int velocity;
    private int direction;
    private int position;
    private int lastPosition;
    private float displayPosition;
    private boolean isAlive;

    public Ant() {
        this.isAlive = true;
    }

    public Ant(int id, int velocity, int direction, int position) {
        this.id = id;
        this.velocity = velocity;
        this.direction = direction;
        this.position = position;
        this.isAlive = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void toggleDirection() {
        this.direction *= -1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.lastPosition = this.position;
        this.position = position;
    }

    public float getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(float displayPosition) {
        if (direction > 0) {
            displayPosition = Math.min(displayPosition, position + velocity);
        } else if (direction < 0) {
            displayPosition = Math.max(displayPosition, position - velocity);
        }
        this.displayPosition = displayPosition;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void move() {
        if (isAlive()) this.position += direction * velocity;
    }
}

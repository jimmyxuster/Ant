package com.jimmyhsu.creepinggame.bean;

/**
 * Created by xuyanzhe on 19/9/17.
 */
public class Stick {
    private int min;
    private int max;

    public Stick(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean isFallen(Ant ant) {
        return ant.getPosition() <= min || ant.getPosition() >= max;
    }
}

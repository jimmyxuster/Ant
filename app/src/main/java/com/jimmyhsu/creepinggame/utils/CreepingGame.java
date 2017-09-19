package com.jimmyhsu.creepinggame.utils;



import com.jimmyhsu.creepinggame.bean.Ant;
import com.jimmyhsu.creepinggame.bean.Stick;

import java.util.List;

/**
 * Created by xuyanzhe on 19/9/17.
 */
public class CreepingGame {

    private List<Ant> mAnts;
    private Stick mStick;

    public CreepingGame(List<Ant> ants) {
        this.mAnts = ants;
        this.mStick = new Stick(0, 300);
    }

    private boolean collides(Ant ant, Ant another) {
        return ant.isAlive() && another.isAlive() && ant.getPosition() == another.getPosition();
    }

    public void iterate() {
        for (Ant ant: mAnts) {
            ant.move();
            ant.setAlive(!mStick.isFallen(ant));
        }
        for (int i = 0; i < mAnts.size() - 1; i++) {
            Ant antLeft = mAnts.get(i);
            Ant antRight = mAnts.get(i + 1);
            if (antLeft.isAlive() && antRight.isAlive() && collides(antLeft, antRight)) {
                antLeft.toggleDirection();
                antRight.toggleDirection();
            }
        }

    }
}

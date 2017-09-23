package com.jimmyhsu.creepinggame.bean;

/**
 * Created by xuyanzhe on 21/9/17.
 */

public class Result {
    private int moveCount;
    private int initialDirection;

    public Result(int moveCount, int initialDirection) {
        this.moveCount = moveCount;
        this.initialDirection = initialDirection;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getInitialDirection() {
        return initialDirection;
    }

    public void setInitialDirection(int initialDirection) {
        this.initialDirection = initialDirection;
    }
}

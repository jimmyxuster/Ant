package com.jimmyhsu.creepinggame.utils;


import android.util.Log;

import com.jimmyhsu.creepinggame.bean.Ant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyanzhe on 19/9/17.
 */
public class PlayRoom {

    private static final int ANT_COUNT = 5;
    private static final long DEFAULT_INC_TIME = 500;
    private static final int[] INIT_POSITIONS = new int[]{30, 80, 110, 160, 250};

    private int[] directions = new int[ANT_COUNT];
    private long incTime = DEFAULT_INC_TIME;


    public interface Callback {
        void onPositionChanged(List<Ant> ants);
    }

    private boolean isGameOver(List<Ant> ants) {
        for (Ant ant: ants) {
            if (ant.isAlive()) return false;
        }
        return true;
    }

    public void startGame() {
        int minTime = Integer.MAX_VALUE;
        int maxTime = 0;
        List<Ant> ants = new ArrayList<>();
        CreepingGame game = new CreepingGame(ants);
        for (int i = 0; i < ANT_COUNT; i++) ants.add(new Ant(i, Ant.DEFAULT_VELOCITY, Ant.DIR_LEFT, 0));
        for (int i = 0; i < Math.pow(2, ANT_COUNT); i++) {
            int time = 0;
            Log.e("PlayRoom", "initializing...");
            for (int a = 0; a < ANT_COUNT; a++) {
                Ant ant = ants.get(a);
                ant.setDirection((i & (0x1 << a)) == 0x1 << a ? Ant.DIR_RIGHT : Ant.DIR_LEFT);
                Log.e("PlayRoom", "ant " + a + ": direction " + ant.getDirection());
                ant.setPosition(INIT_POSITIONS[a]);
                ant.setAlive(true);
            }
            while (!isGameOver(ants)) {
                game.iterate();
                time++;
            }
            Log.e("PlayRoom", "Game over: " + time);
            if (time < minTime) minTime = time;
            if (time > maxTime) maxTime = time;
        }
        Log.e("PlayRoom", "----------------------");
        Log.e("PlayRoom", "report: minTime = " + minTime + ", maxTime = " + maxTime);
    }
}

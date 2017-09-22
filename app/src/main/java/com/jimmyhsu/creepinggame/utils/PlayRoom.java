package com.jimmyhsu.creepinggame.utils;


import android.util.Log;

import com.jimmyhsu.creepinggame.bean.Ant;
import com.jimmyhsu.creepinggame.bean.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by xuyanzhe on 19/9/17.
 */
public class PlayRoom {

    public static final int ANT_COUNT = 5;
    public static final long DEFAULT_INC_TIME = 100;
    private static final int[] INIT_POSITIONS = new int[]{30, 80, 110, 160, 250};
    private Semaphore mSemaphore = new Semaphore(1);

    private int[] directions = new int[ANT_COUNT];
    private long incTime = DEFAULT_INC_TIME;

    private Thread mComputeThread;


    public interface Callback {
        void onPositionChanged(List<Ant> ants);
        void onStart(List<Ant> ants);
        void onConditionChanged(Result result);
        void onEnd(int minTime, int maxTime);
    }

    private boolean isGameOver(List<Ant> ants) {
        for (Ant ant: ants) {
            if (ant.isAlive()) return false;
        }
        return true;
    }

    public void releaseSignal() {
        mSemaphore.release();
    }

    public void startGame(final Callback callback) {
        mComputeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int minTime = Integer.MAX_VALUE;
                int maxTime = 0;
                List<Ant> ants = new ArrayList<>();
                CreepingGame game = new CreepingGame(ants);
                for (int i = 0; i < ANT_COUNT; i++) {
                    Ant ant = new Ant(i, Ant.DEFAULT_VELOCITY, Ant.DIR_LEFT, 0);
                    ant.setPosition(INIT_POSITIONS[i]);
                    ant.setDisplayPosition(INIT_POSITIONS[i]);
                    ants.add(ant);
                }
                if (callback != null) {
                    callback.onStart(ants);
                }
                for (int i = 0; i < Math.pow(2, ANT_COUNT); i++) {
                    if (Thread.currentThread().isInterrupted()) return;
                    int time = 0;
                    Log.e("PlayRoom", "initializing...");
                    for (int a = 0; a < ANT_COUNT; a++) {
                        Ant ant = ants.get(a);
                        ant.setDirection((i & (0x1 << a)) == 0x1 << a ? Ant.DIR_RIGHT : Ant.DIR_LEFT);
                        Log.e("PlayRoom", "ant " + a + ": direction " + ant.getDirection());
                        ant.setPosition(INIT_POSITIONS[a]);
                        ant.setDisplayPosition(INIT_POSITIONS[a]);
                        ant.setAlive(true);
                    }
                    while (!isGameOver(ants)) {
                        try {
                            mSemaphore.acquire();
                            game.iterate();
                            if (callback != null) {
                                callback.onPositionChanged(ants);
                            }
                            time++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("PlayRoom", "Game over: " + time);
                    if (time < minTime) minTime = time;
                    if (time > maxTime) maxTime = time;
                    if (callback != null) {
                        callback.onConditionChanged(new Result(time, i));
                    }
                }
                Log.e("PlayRoom", "----------------------");
                Log.e("PlayRoom", "report: minTime = " + minTime + ", maxTime = " + maxTime);
                if (callback != null) {
                    callback.onEnd(minTime, maxTime);
                }
            }
        });
        mComputeThread.start();
    }

    public void terminateGame() {
        if (mComputeThread != null && mComputeThread.isAlive()) {
            mComputeThread.interrupt();
        }
    }
}

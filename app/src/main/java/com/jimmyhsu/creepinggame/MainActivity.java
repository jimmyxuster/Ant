package com.jimmyhsu.creepinggame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jimmyhsu.creepinggame.adapter.ResultAdapter;
import com.jimmyhsu.creepinggame.bean.Ant;
import com.jimmyhsu.creepinggame.bean.Result;
import com.jimmyhsu.creepinggame.utils.PlayRoom;
import com.jimmyhsu.creepinggame.view.GameView;
import com.jimmyhsu.creepinggame.view.ResultItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GameView mGameView;
    private FloatingActionButton mToggleFab;
    private RecyclerView mResultView;

    private PlayRoom mPlayroom;

    private List<Result> mResults = new ArrayList<>();
    private ResultAdapter mResultAdapter;

    private PlayRoom.Callback mCallback = new PlayRoom.Callback() {
        @Override
        public void onPositionChanged(final List<Ant> ants) {
            for (Ant ant: ants) {
                ant.setDisplayPosition(ant.getPosition());
            }
            mGameView.addTime();
        }

        @Override
        public void onStart(List<Ant> ants) {
            mResultView.post(new Runnable() {
                @Override
                public void run() {
                    mResultView.setVisibility(View.VISIBLE);
                    int oldSize = mResults.size();
                    mResults.clear();
                    mResultAdapter.notifyItemRangeRemoved(0, oldSize);
                    mResultAdapter.toggleResult(false, -1, -1);
                }
            });
            mGameView.setAnts(ants);
            mGameView.resetResult();
        }

        @Override
        public void onConditionChanged(Result result) {
            mResults.add(result);
            mResultView.post(new Runnable() {
                @Override
                public void run() {
                    mResultAdapter.notifyItemInserted(mResults.size() - 1);
                    mGameView.resetTime();
                }
            });
        }

        @Override
        public void onEnd(final int minTime, final int maxTime) {
            showButton();
            mGameView.setResult(minTime, maxTime);
            mResultView.post(new Runnable() {
                @Override
                public void run() {
                    mResultAdapter.toggleResult(true, maxTime, minTime);
                    for (int i = 0; i < mResults.size(); i++) {
                        if (mResults.get(i).getMoveCount() == minTime
                                || mResults.get(i).getMoveCount() == maxTime) {
                            mResultAdapter.notifyItemChanged(i);
                        }
                    }
                }
            });
        }
    };

    private GameView.GameCallback mGameCallback = new GameView.GameCallback() {
        @Override
        public void onRequestIterate() {
            mPlayroom.releaseSignal();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mGameView.setCallback(mGameCallback);
        mToggleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButton();
                mPlayroom = new PlayRoom();
                mPlayroom.startGame(mCallback);
            }
        });
        initResultView();
    }

    private void initResultView() {
        mResultView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        mResultView.addItemDecoration(new ResultItemDecoration(this, 3));
        mResultView.setAdapter(mResultAdapter = new ResultAdapter(this, mResults));
    }

    private void initViews() {
        mToggleFab = (FloatingActionButton) findViewById(R.id.id_start);
        mGameView = (GameView) findViewById(R.id.id_game_view);
        mResultView = (RecyclerView) findViewById(R.id.id_result_view);
    }

    private void hideButton() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mToggleFab.post(new Runnable() {
                @Override
                public void run() {
                    showAnimator(R.animator.hide_btn_set);
                }
            });
        } else {
            showAnimator(R.animator.hide_btn_set);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayroom.terminateGame();
    }

    private void showButton() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mToggleFab.post(new Runnable() {
                @Override
                public void run() {
                    showAnimator(R.animator.show_btn_set);
                }
            });
        } else {
            showAnimator(R.animator.show_btn_set);
        }
    }

    private void showAnimator(int resId) {
        Animator hideAnimatorSet = AnimatorInflater.loadAnimator(MainActivity.this, resId);
        hideAnimatorSet.setTarget(mToggleFab);
        hideAnimatorSet.start();
    }
}

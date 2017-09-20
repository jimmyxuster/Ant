package com.jimmyhsu.creepinggame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jimmyhsu.creepinggame.bean.Ant;
import com.jimmyhsu.creepinggame.utils.PlayRoom;
import com.jimmyhsu.creepinggame.view.GameView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GameView mGameView;
    private PlayRoom mPlayroom;

    private PlayRoom.Callback mCallback = new PlayRoom.Callback() {
        @Override
        public void onPositionChanged(final List<Ant> ants) {
            for (Ant ant: ants) {
                ant.setDisplayPosition(ant.getPosition());
            }
        }

        @Override
        public void onStart(List<Ant> ants) {
            mGameView.setAnts(ants);
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
        Button btnStart = (Button) findViewById(R.id.id_start);
        mGameView = (GameView) findViewById(R.id.id_game_view);
        mGameView.setCallback(mGameCallback);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayroom = new PlayRoom();
                mPlayroom.startGame(mCallback);
            }
        });
    }
}

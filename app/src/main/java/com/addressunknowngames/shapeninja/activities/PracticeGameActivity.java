package com.addressunknowngames.shapeninja.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.game.GameCallback;
import com.addressunknowngames.shapeninja.game.GameWindow;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class PracticeGameActivity extends Activity {

    private GameWindow gameWindow;

    private GameCallback gameCallback = new GameCallback() {

        @Override
        public void onGameEnd(final long timeMs, final int score) {
            Log.v("MNF", "onGameEnd score: " + score);
//            Intent gameOverIntent = new Intent(GameActivity.this, GameOverActivity.class);
//            gameOverIntent.putExtra(Constants.GAME_SCORE_KEY, score);
//            gameOverIntent.putExtra(Constants.GAME_TIME_KEY, timeMs);
//            startActivity(gameOverIntent);
            finish();
        }

        @Override
        public void onGameWon(long timeMs, int score) {
            Log.v("MNF", "onGameWon score: " + score);
//            SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
//            int level = prefs.getInt("level", 1);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putInt("level", level + 1);
//            editor.commit();
//
//            onGameEnd(timeMs, score);
//            gameEndedText.setText(GameOverActivity.this.getString(R.string.level_passed, level));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_game);

        gameWindow = findViewById(R.id.game);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
        gameWindow.setGameCallback(gameCallback);
        gameWindow.startGame(prefs.getInt("level", 1));
    }

    @Override
    protected void onStop() {
        super.onStop();

        gameWindow.setGameCallback(null);
    }

}

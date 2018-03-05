package com.addressunknowngames.shapeninja.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.addressunknowngames.shapeninja.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.addressunknowngames.shapeninja.game.GameWindow;
import com.addressunknowngames.shapeninja.game.IGame;
import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.utils.Constants;

/**
 * Created by matthewferguson on 2/26/18.
 */

public class GameActivity extends Activity {

    private GameWindow gameWindow;

    private IGame.GameCallback gameCallback = new IGame.GameCallback() {

        @Override
        public void onGameEnd(final long timeMs, final int score) {
            Log.v("MNF", "onGameEnd score: " + score);
            saveScore(new Long(score));
            Intent gameOverIntent = new Intent(GameActivity.this, GameOverActivity.class);
            gameOverIntent.putExtra(Constants.GAME_SCORE_KEY, score);
            gameOverIntent.putExtra(Constants.GAME_TIME_KEY, timeMs);
            startActivity(gameOverIntent);
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
        setContentView(R.layout.activity_game);

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

    public void saveScore(Long score) {
        if (score > FiguraRushActivity.highScore) {
            FiguraRushActivity.highScore = score;
            Utils.writeScoreToFile(GameActivity.this, "highScore.txt", "" + FiguraRushActivity.highScore);
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users/" + user.getUid() + "/highScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long previousHighScore = (Long)dataSnapshot.getValue();
                    if (score > previousHighScore) {
                        FiguraRushActivity.highScore = score;
                        database.child("high-scores").child(user.getUid()).setValue(score);
                        database.child("users").child(user.getUid()).child("highScore").setValue(score);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}

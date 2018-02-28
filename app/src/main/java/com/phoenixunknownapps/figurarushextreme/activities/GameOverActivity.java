package com.phoenixunknownapps.figurarushextreme.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.phoenixunknownapps.figurarushextreme.FiguraRushApplication;
import com.phoenixunknownapps.figurarushextreme.R;
import com.phoenixunknownapps.figurarushextreme.utils.Constants;
import com.phoenixunknownapps.figurarushextreme.utils.TimestampUtils;

/**
 * Created by matthewferguson on 2/26/18.
 */

public class GameOverActivity extends Activity {

    private Typeface FONT_REGULAR;
    private Typeface FONT_BOLD;

    private Button playButton;
    private Button homeButton;
    private Button shareButton;
    private TextView gameEndedText;
    private TextView highScoreTextLabel;
    private TextView highScoreText;
    private TextView gameTimeTextLabel;
    private TextView gameTimeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        FONT_REGULAR = ((FiguraRushApplication) getApplicationContext()).getFontRegular();
        FONT_BOLD = ((FiguraRushApplication) getApplicationContext()).getFontBold();

        playButton = findViewById(R.id.startButton);
        playButton.setTypeface(FONT_BOLD);
        homeButton = findViewById(R.id.homeButton);
        homeButton.setTypeface(FONT_BOLD);
        shareButton = findViewById(R.id.shareButton);
        shareButton.setTypeface(FONT_BOLD);
        gameEndedText = findViewById(R.id.gameEndedTitle);
        gameEndedText.setTypeface(FONT_REGULAR);
        highScoreTextLabel = findViewById(R.id.highScoreTextLabel);
        highScoreTextLabel.setTypeface(FONT_REGULAR);
        highScoreText = findViewById(R.id.highScoreText);
        highScoreText.setTypeface(FONT_REGULAR);
        gameTimeTextLabel = findViewById(R.id.gameTimeTextLabel);
        gameTimeTextLabel.setTypeface(FONT_REGULAR);
        gameTimeText = findViewById(R.id.gameTimeText);
        gameTimeText.setTypeface(FONT_REGULAR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int score = bundle.getInt(Constants.GAME_SCORE_KEY);
            long timeMs = bundle.getLong(Constants.GAME_TIME_KEY);
            String text = TimestampUtils.timeToString(timeMs);
            highScoreText.setText("" + score);
            gameTimeText.setText(text);
        }
    }
}

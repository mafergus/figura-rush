package com.addressunknowngames.shapeninja;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by matthewferguson on 10/22/15.
 */
public class HighScoreItem extends RelativeLayout {

    final protected View rootView;
    final protected TextView label;
    final protected TextView score;

    public HighScoreItem(Context context) {
        this(context, null);
    }

    public HighScoreItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighScoreItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.high_score_item, this);
        rootView = findViewById(R.id.rootView);
        label = findViewById(R.id.label);
        score = findViewById(R.id.score);
    }

    public void setLabel(String txt) {
        label.setText(txt);
    }

    public void setScore(int score) {
        this.score.setText("" + score);
    }
}

package com.phoenixunknownapps.figurarushextreme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by matthewferguson on 10/22/15.
 */
public class Scores extends RelativeLayout {

    private TextView title;
    private HighScoreItem item1;
    private HighScoreItem item2;
    private HighScoreItem item3;
    private HighScoreItem item4;

    public Scores(Context context) {
        this(context, null);
    }

    public Scores(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Scores(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.scores, this);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(((FiguraRushApplication) context.getApplicationContext()).getFontBold());
        item1 = (HighScoreItem) findViewById(R.id.score1);
        item2 = (HighScoreItem) findViewById(R.id.score2);
        item3 = (HighScoreItem) findViewById(R.id.score3);
        item4 = (HighScoreItem) findViewById(R.id.score4);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public HighScoreItem getItem1() {
        return item1;
    }

    public HighScoreItem getItem2() {
        return item2;
    }

    public HighScoreItem getItem3() {
        return item3;
    }

    public HighScoreItem getItem4() {
        return item4;
    }
}

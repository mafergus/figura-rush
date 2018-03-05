package com.addressunknowngames.shapeninja.game;

import android.content.Context;
import android.util.AttributeSet;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 10/26/15.
 */
public class NormalGame extends GameWindow {
    protected int targetScore = Integer.MAX_VALUE;

    public NormalGame(Context context) {
        this(context, null);
    }

    public NormalGame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalGame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.asdgame;
    }

    @Override
    protected void doGameStart(int level) {
        super.doGameStart(level);

        targetScore = level * 3;
        targetScoreTv.setText("" + targetScore);
    }

    @Override
    protected boolean didWinGame() {
        return score >= targetScore;
    }
}

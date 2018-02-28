package com.phoenixunknownapps.figurarushextreme;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by matthewferguson on 10/26/15.
 */
public class TargetGame extends GameWindow {

    private long targetTime = 0l;

    public TargetGame(Context context) {
        this(context, null);
    }

    public TargetGame(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TargetGame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.target_game;
    }

    @Override
    protected void doGameStart(int level) {
        super.doGameStart(level);

        targetTime = START_TIME_MS + (level / 2) * 1000;
        DecimalFormat df = new DecimalFormat("#.00");
        String targetStr = df.format(targetTime / 1000);
        targetScoreTv.setText("" + targetStr + "s");
        figuraRushProgressBar.setProgress(START_TIME_MS / targetTime);
    }

    @Override
    protected void onTimerTick(long timeElapsed) {
        figuraRushProgressBar.setProgress(timerCounterTextView.getTimeLeftMs() / (targetTime / 100));
    }

    @Override
    protected boolean didWinGame() {
        Log.v("MNF", "targetTime " + targetTime + " timeLeft " + timerCounterTextView.getTimeLeftMs());
        return timerCounterTextView.getTimeLeftMs() >= targetTime;
    }

    @Override
    protected float getTimeModifier() {
        return super.getTimeModifier() * 1.5f;
    }

    @Override
    protected void incorrectShape() {
        super.incorrectShape();
    }

    @Override
    protected void correctShape() {
        super.correctShape();
    }
}

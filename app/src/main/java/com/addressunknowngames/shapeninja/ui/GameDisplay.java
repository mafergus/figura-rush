package com.addressunknowngames.shapeninja.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.model.shapes.Shape;
import com.addressunknowngames.shapeninja.utils.Constants;

/**
 * Created by matthewferguson on 3/5/18.
 */

public class GameDisplay extends RelativeLayout {
    protected static final int ANIMATION_DURATION = 300;
    protected static final int ANIM_FADE_OUT_MS_200 = 200;
    protected static final int ANIM_FADE_OUT_MS_600 = 600;

    protected View rootView;
    protected ImageView background;
    protected TimerCounterTextView timerCounterTextView;
    protected RelativeLayout scoreContainer;
    protected TextView drawShapeText;
    protected TextView scoreLabelTv;
    protected TextView scoreTv;
    protected TextView timeBonusText;
    protected Typeface fontBold;
    protected Typeface fontRegular;

    public GameDisplay(Context context) { this(context, null); }

    public GameDisplay(Context context, AttributeSet attrs) { this(context, attrs, -1); }

    public GameDisplay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.game_display, this);

        fontBold = Typeface.createFromAsset(context.getAssets(), "josefinsans_bold.ttf");
        fontRegular = Typeface.createFromAsset(context.getAssets(), "josefinsans_regular.ttf");

        rootView = findViewById(R.id.rootView);
        scoreLabelTv = findViewById(R.id.scoreLabel);
        scoreLabelTv.setTypeface(fontRegular);
        scoreTv = findViewById(R.id.score);
        scoreTv.setTypeface(fontRegular);
        drawShapeText = findViewById(R.id.shapeText);
        drawShapeText.setTypeface(fontRegular);
        background = findViewById(R.id.background);

        timerCounterTextView = findViewById(R.id.timerCounter);
        timerCounterTextView.setTypeface(fontBold);
        timerCounterTextView.setTextSize(48f);
        timeBonusText = findViewById(R.id.timeBonusText);
        timeBonusText.setTypeface(fontBold);
        scoreContainer = findViewById(R.id.scoreContainer);
        rootView.setBackgroundColor(getResources().getColor(R.color.BlanchedAlmond));
    }

    public void setBackgroundColor(int colorResId) { rootView.setBackgroundColor(colorResId); }

    public void reset(int colorResId) {
        timerCounterTextView.reset();
        timeBonusText.setVisibility(View.INVISIBLE);
        scoreTv.setText("0");
        drawShapeText.setText("");
        setBackgroundColor(colorResId);
    }

    public void startTimer(TimerCounterTextView.Callback callback) {
        timerCounterTextView.init(Constants.TIME_INTERVAL, Constants.START_TIME_MS);
        timerCounterTextView.start(callback);
    }

    public void start(TimerCounterTextView.Callback callback) {
        Animation anim1 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim1.setDuration(ANIMATION_DURATION);
        Animation anim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim2.setDuration(ANIMATION_DURATION);
        anim2.setStartOffset(50);
        Animation anim3 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim3.setDuration(ANIMATION_DURATION);
        anim3.setStartOffset(100);
        anim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                startTimer(callback);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        scoreContainer.startAnimation(anim1);
        drawShapeText.startAnimation(anim2);
        timerCounterTextView.startAnimation(anim3);
    }

    public void stop() { timerCounterTextView.stop(); }

    public void setShape(Shape shape) {
        drawShapeText.setText(getContext().getString(shape.getStringResId()));
    }

    public void setScore(int score) {
        scoreTv.animate().alpha(0f).setDuration(100).withEndAction(() -> {
            scoreTv.setText("" + score);
            scoreTv.animate().alpha(1f).setDuration(100);
        });
    }

    public void showShapeAnimation(int color) {
        background.setVisibility(View.VISIBLE);
        background.setBackgroundColor(color);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
        fadeOut.setDuration(ANIM_FADE_OUT_MS_200);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {}

            @Override
            public void onAnimationRepeat(Animation arg0) {}

            @Override
            public void onAnimationEnd(Animation arg0) {
                background.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        });
        background.setAnimation(fadeOut);
    }

    public void addTime(long time) { timerCounterTextView.addTime(time); }

    public long getRunTime() { return timerCounterTextView.getTotalRunTimeMs(); }

    public void showCorrectAnimation(long timeBonus) {
        String modifierText = "";
        if (timeBonus >= 2000) {
            modifierText = getContext().getString(R.string.streak_2);
        } else if (timeBonus > 1000) {
            modifierText = getContext().getString(R.string.streak);
        }
        timeBonusText.setText(modifierText + " " + "+" + ((float) timeBonus / 1000f));
        timeBonusText.setVisibility(View.VISIBLE);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
        fadeOut.setDuration(ANIM_FADE_OUT_MS_600);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {}

            @Override
            public void onAnimationRepeat(Animation arg0) {}

            @Override
            public void onAnimationEnd(Animation arg0) {
                timeBonusText.setVisibility(View.INVISIBLE);
            }
        });
        timeBonusText.setAnimation(fadeOut);
    }
}

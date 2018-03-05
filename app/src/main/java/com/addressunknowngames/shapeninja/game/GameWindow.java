package com.addressunknowngames.shapeninja.game;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.game.levels.Level;
import com.addressunknowngames.shapeninja.model.shapes.Circle;
import com.addressunknowngames.shapeninja.model.shapes.Clover;
import com.addressunknowngames.shapeninja.model.shapes.Heart;
import com.addressunknowngames.shapeninja.model.shapes.Moon;
import com.addressunknowngames.shapeninja.model.shapes.Shape;
import com.addressunknowngames.shapeninja.model.shapes.Square;
import com.addressunknowngames.shapeninja.model.shapes.Star;
import com.addressunknowngames.shapeninja.model.shapes.Triangle;
import com.addressunknowngames.shapeninja.ui.FiguraRushProgressBar;
import com.addressunknowngames.shapeninja.ui.TimerCounterTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameWindow extends RelativeLayout implements IGame, OnGesturePerformedListener {
    protected static final int ANIMATION_DURATION = 300;
    protected static final int START_TIME_MS = 10000;
    protected static final int ANIM_FADE_OUT_MS_200 = 200;
    protected static final int ANIM_FADE_OUT_MS_600 = 600;
    protected static final int LEVEL_1 = 5;
    protected static final int LEVEL_2 = 15;
    protected static final int LEVEL_3 = 35;
    protected static final int LEVEL_4 = 60;
    protected static final int LEVEL_5 = 100;
    protected static final int LEVEL_6 = 9999;

    static final public List<Shape> ALL_SHAPES = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart(), new Star(), new Clover());
    final protected List<Shape> level1Shapes = Arrays.asList(new Circle(), new Triangle());
    final protected List<Shape> level2Shapes = Arrays.asList(new Circle(), new Triangle(), new Square());
    final protected List<Shape> level3Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon());
    final protected List<Shape> level4Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart());
    final protected List<Shape> level5Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart(), new Star());
    final protected List<Shape> level6Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart(), new Star(), new Clover());

    protected List<Level> levels = new ArrayList<>();
    protected Level currentLevel;

    protected View rootView;
    protected ImageView background;
    protected GestureLibrary gestureLib;
    protected GestureOverlayView gestureOverlayView;
    protected TimerCounterTextView timerCounterTextView;
    protected RelativeLayout scoreContainer;
    protected TextView drawShapeText;
    protected TextView scoreLabelTv;
    protected TextView scoreTv;
    protected TextView targetScoreTv;
    protected TextView timeBonusText;
    protected Typeface fontBold;
    protected Typeface fontRegular;
    protected FiguraRushProgressBar figuraRushProgressBar;

    protected boolean isStarted = false;
    protected int score = 0;
    protected int streak = 0;
    protected Shape currentShape = null;

    protected IGame.GameCallback gameCallback;

    public GameWindow(Context context) {
        this(context, null);
    }

    public GameWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(getLayoutResId(), this);

        fontBold = Typeface.createFromAsset(context.getAssets(), "josefinsans_bold.ttf");
        fontRegular = Typeface.createFromAsset(context.getAssets(), "josefinsans_regular.ttf");

        rootView = findViewById(R.id.rootView);
        gestureOverlayView = findViewById(R.id.gestureOverlay);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureLib = GestureLibraries.fromRawResource(context, R.raw.gestures);
        if (!gestureLib.load()) {
            //			finish();
        }

        scoreLabelTv = findViewById(R.id.trainMagicScoreLabel);
        scoreLabelTv.setTypeface(fontRegular);
        scoreTv = findViewById(R.id.trainMagicScore);
        scoreTv.setTypeface(fontRegular);
        targetScoreTv = findViewById(R.id.targetScore);
        targetScoreTv.setTypeface(fontRegular);
        TextView slash = findViewById(R.id.slash);
        slash.setTypeface(fontRegular);
        drawShapeText = findViewById(R.id.trainMagicShapeText);
        drawShapeText.setTypeface(fontRegular);
        background = findViewById(R.id.trainMagicBackground);

        timerCounterTextView = findViewById(R.id.timerCounter);
        timerCounterTextView.init(50, START_TIME_MS, new TimerCounterTextView.Callback() {

            @Override
            public void onEnded(long totalRunTimeMs) {
                endGame(totalRunTimeMs);
            }

            @Override
            public void onTick(long timeElapsed) {
                onTimerTick(timeElapsed);
            }
        });
        timerCounterTextView.setTypeface(fontBold);
        timerCounterTextView.setTextSize(48f);
        timeBonusText = findViewById(R.id.timeBonusText);
        timeBonusText.setTypeface(fontBold);
        scoreContainer = findViewById(R.id.trainMagicScoreContainer);
        figuraRushProgressBar = findViewById(R.id.figuraRushProgressBar);
        figuraRushProgressBar.setProgress(100);

        loadLevels();
        currentLevel = levels.get(0);
        rootView.setBackgroundColor(getResources().getColor(R.color.BlanchedAlmond));
    }

    protected int getLayoutResId() {
        return R.layout.basic_game;
    }

    protected void loadLevels() {
        levels.add(new Level(getContext().getResources().getColor(R.color.BlanchedAlmond), LEVEL_1, level1Shapes));
        levels.add(new Level(getContext().getResources().getColor(R.color.Lavender), LEVEL_2, level2Shapes));
        levels.add(new Level(getContext().getResources().getColor(R.color.Olive), LEVEL_3, level3Shapes));
        levels.add(new Level(getContext().getResources().getColor(R.color.Ivory), LEVEL_4, level4Shapes));
        levels.add(new Level(getContext().getResources().getColor(R.color.Bisque), LEVEL_5, level5Shapes));
        levels.add(new Level(getContext().getResources().getColor(R.color.Gold), LEVEL_6, level6Shapes));
    }

    protected void updateLevel() {
        Log.v("MNF", "updateLevel score: " + score);
        if (score > currentLevel.getPoints()) {
            int i = levels.indexOf(currentLevel);
            Level nextLevel = levels.get(i + 1);
            if (nextLevel != null) {
                currentLevel = nextLevel;
            }
            rootView.setBackgroundColor(currentLevel.getColor());
        }
    }

    public void startGame(final int level) {
        Log.v("MNF", "startGame level: " + level);
        gameStartAnim(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                doGameStart(level);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    protected void doGameStart(int level) {
        streak = 0;
        score = 0;
        scoreTv.setText("" + 0);
        currentLevel = levels.get(0);
        rootView.setBackgroundColor(currentLevel.getColor());
        timerCounterTextView.start();

        isStarted = true;
        nextShape();
    }

    protected void onTimerTick(long timeElapsed) {
        figuraRushProgressBar.setProgress(timerCounterTextView.getTimeLeftMs() / 100.0);
    }

    private void gameStartAnim(AnimationListener listener) {

        figuraRushProgressBar.animate().alpha(0f);
        figuraRushProgressBar.animate().alpha(1f).setDuration(ANIMATION_DURATION);

        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation anim1 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim1.setDuration(ANIMATION_DURATION);
        Animation anim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim2.setDuration(ANIMATION_DURATION);
        anim2.setStartOffset(50);
        Animation anim3 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim3.setDuration(ANIMATION_DURATION);
        anim3.setStartOffset(100);
        anim3.setAnimationListener(listener);

        figuraRushProgressBar.startAnimation(fadeIn);
        scoreContainer.startAnimation(anim1);
        drawShapeText.startAnimation(anim2);
        timerCounterTextView.startAnimation(anim3);
    }

    protected boolean didWinGame() {
        return false;
    }

    protected void gameWon(long totalRunTimeMs) {
        Log.v("MNF", "gameWon runTime: " + totalRunTimeMs);
        timerCounterTextView.stop();
        reset();
        if (gameCallback != null) {
            gameCallback.onGameWon(totalRunTimeMs, score);
        }
    }

    protected void endGame(long totalRunTimeMs) {
        Log.v("MNF", "endGame runTime: " + totalRunTimeMs);
        reset();
        if (gameCallback != null) {
            gameCallback.onGameEnd(totalRunTimeMs, score);
        }
    }

    public void reset() {
        Log.v("MNF", "reset");
        timerCounterTextView.reset();
        figuraRushProgressBar.setProgress(100.0);
        isStarted = false;
        timeBonusText.setVisibility(View.INVISIBLE);
        scoreTv.setText("0");
        drawShapeText.setText("");
    }

    protected float getTimeModifier() {
        float levelModifier = 0;
        if (score >= 500) {
            levelModifier = 1f;
        } else if (score < 500 && score > 300) {
            levelModifier = 0.5f;
        }
        if (streak >= 3 && streak <= 6) {
            return (1.5f - levelModifier);
        } else if (streak > 6) {
            return (2f - levelModifier);
        } else {
            return (1f - levelModifier);
        }
    }

    private void gotShape(Shape shape) {
        Log.v("MNF", "gotShape " + shape.getName());
        int color;
        if (shape.getName().equals(currentShape.getName())) {
            color = getResources().getColor(R.color.green);
            correctShape();
        } else {
            color = getResources().getColor(R.color.red);
            incorrectShape();
        }
        showShapeAnimation(color);
    }

    private void showShapeAnimation(int color) {
        background.setVisibility(View.VISIBLE);
        background.setBackgroundColor(color);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
        fadeOut.setDuration(ANIM_FADE_OUT_MS_200);
        fadeOut.setAnimationListener(new AnimationListener() {
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

    protected void incorrectShape() {
        streak = 0;
    }

    protected void correctShape() {
        Log.v("MNF", "correctShape()");
        incrementScore();

        if (score > currentLevel.getPoints()) {
            updateLevel();
        }
        streak++;
        long timeBonus = (long) (getTimeModifier() * 1000);
        showCorrectAnimation(timeBonus);
        timerCounterTextView.addTime(timeBonus);

        if (didWinGame()) {
            gameWon(timerCounterTextView.getTotalRunTimeMs());
        } else {
            nextShape();
        }
    }

    private void showCorrectAnimation(long timeBonus) {
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
        fadeOut.setAnimationListener(new AnimationListener() {
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

    protected void nextShape() {
        Log.v("MNF", "nextShape");
        currentShape = currentLevel.getShape();
        drawShapeText.setText(getContext().getString(currentShape.getStringResId()));
    }

    protected void incrementScore() {
        Log.v("MNF", "incrementScore");
        score++;
        scoreTv.animate().alpha(0f).setDuration(100).withEndAction(() -> {
            scoreTv.setText("" + score);
            scoreTv.animate().alpha(1f).setDuration(100);
        });
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        Log.v("MNF", "onGesturePerformed isStarted: " + isStarted);
        if (!isStarted) {
            return;
        }

        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        Prediction bestPrediction = predictions.get(0);
        Shape shape;
        if (bestPrediction.score > 1) {
            if (bestPrediction.name.startsWith("triangle")) {
                shape = new Triangle();
            } else if (bestPrediction.name.startsWith("circle")) {
                shape = new Circle();
            } else if (bestPrediction.name.startsWith("square")) {
                shape = new Square();
            } else if (bestPrediction.name.startsWith("clover")) {
                shape = new Clover();
            } else if (bestPrediction.name.startsWith("heart")) {
                shape = new Heart();
            } else if (bestPrediction.name.startsWith("moon")) {
                shape = new Moon();
            } else if (bestPrediction.name.startsWith("star")) {
                shape = new Star();
            } else {
                shape = null;
            }
            gotShape(shape);
        }
    }

    public void setGameCallback(final IGame.GameCallback callback) {
        this.gameCallback = callback;
    }

}

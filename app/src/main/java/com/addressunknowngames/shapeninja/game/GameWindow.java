package com.addressunknowngames.shapeninja.game;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

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
import com.addressunknowngames.shapeninja.ui.GameDisplay;
import com.addressunknowngames.shapeninja.ui.TimerCounterTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameWindow extends RelativeLayout implements OnGesturePerformedListener {
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

    protected GameDisplay gameDisplay;
    protected GestureLibrary gestureLib;
    protected GestureOverlayView gestureOverlayView;

    protected boolean isStarted = false;
    protected int score = 0;
    protected int streak = 0;
    protected Shape currentShape = null;

    protected GameCallback gameCallback;

    public GameWindow(Context context) {
        this(context, null);
    }

    public GameWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(getLayoutResId(), this);

        gameDisplay = findViewById(R.id.gameDisplay);
        gestureOverlayView = gameDisplay.findViewById(R.id.gestureOverlay);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureLib = GestureLibraries.fromRawResource(context, R.raw.gestures);
        if (!gestureLib.load()) {
            //			finish();
        }

        loadLevels();
        currentLevel = levels.get(0);
    }

    protected int getLayoutResId() {
        return R.layout.game_window;
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
            gameDisplay.setBackgroundColor(currentLevel.getColor());
        }
    }

    public void startGame(final int level) {
        Log.v("MNF", "startGame level: " + level);

        streak = 0;
        score = 0;
        currentLevel = levels.get(0);
        gameDisplay.start(new TimerCounterTextView.Callback() {

            @Override
            public void onEnded(long totalRunTimeMs) {
                endGame(totalRunTimeMs);
            }

            @Override
            public void onTick(long timeElapsed) {}
        });
        isStarted = true;
        nextShape();
    }

    protected boolean didWinGame() {
        return false;
    }

    protected void gameWon(long totalRunTimeMs) {
        Log.v("MNF", "gameWon runTime: " + totalRunTimeMs);
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
        isStarted = false;
        gameDisplay.reset(currentLevel.getColor());
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
        if (shape.getName().equals(currentShape.getName())) {
            gameDisplay.showShapeAnimation(getResources().getColor(R.color.green));
            correctShape();
        } else {
            gameDisplay.showShapeAnimation(getResources().getColor(R.color.red));
            incorrectShape();
        }

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
        gameDisplay.showCorrectAnimation(timeBonus);
        gameDisplay.addTime(timeBonus);

        if (didWinGame()) {
            gameWon(gameDisplay.getRunTime());
        } else {
            nextShape();
        }
    }

    protected void nextShape() {
        Log.v("MNF", "nextShape");
        currentShape = currentLevel.getShape();
        gameDisplay.setShape(currentShape);
    }

    protected void incrementScore() {
        Log.v("MNF", "incrementScore");
        gameDisplay.setScore(++score);
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

    public void setGameCallback(final GameCallback callback) {
        this.gameCallback = callback;
    }

}

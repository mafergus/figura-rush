//package com.addressunknowngames.shapeninja.game;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.addressunknowngames.shapeninja.FiguraRushApplication;
//import com.addressunknowngames.shapeninja.R;
//import com.addressunknowngames.shapeninja.game.levels.PracticeLevel;
//import com.addressunknowngames.shapeninja.ui.SelectShapePopupWindow;
//
//public class PracticeGame extends GameWindow {
//
//	private ImageView shapeImageView;
//	private Button pickShapeButton;
//
//	public PracticeGame(Context context) {
//		this(context, null);
//	}
//
//	public PracticeGame(Context context, AttributeSet attrs) {
//		this(context, attrs, 0);
//	}
//
//	public PracticeGame(final Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//
//		shapeImageView = findViewById(R.id.shapeImage);
//		shapeImageView.setVisibility(VISIBLE);
//		pickShapeButton = findViewById(R.id.pickShapeButton);
//		pickShapeButton.setTypeface(((FiguraRushApplication)context.getApplicationContext()).getFontRegular());
//		pickShapeButton.setOnClickListener(v -> {
//
//			SelectShapePopupWindow popup = new SelectShapePopupWindow(getContext(), shape -> {
//				if (shape == null) {
//					pickShapeButton.setText(context.getString(R.string.random));
//					((PracticeLevel)currentLevel).setRandom(true);
//				} else {
//					pickShapeButton.setText(context.getString(shape.getStringResId()));
//					((PracticeLevel)currentLevel).setRandom(false);
//					((PracticeLevel)currentLevel).setShape(shape);
//					shapeImageView.setImageResource(shape.getShapeImageResId());
//				}
//				nextShape();
//			});
//			popup.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//		});
//		pickShapeButton.setVisibility(View.VISIBLE);
//
//		timeBonusText.setVisibility(View.GONE);
//		timerCounterTextView.setVisibility(View.INVISIBLE);
//		scoreTv.setVisibility(View.INVISIBLE);
//
//		loadLevels();
//	}
//
//	@Override
//	public void startGame(int level) {
//		isStarted = true;
//		nextShape();
//	}
//
//	@Override
//	protected void correctShape() {
//		nextShape();
//	}
//
//	@Override
//	protected void nextShape() {
//		currentShape = currentLevel.getShape();
//		shapeImageView.setImageResource(currentShape.getShapeImageResId());
//		drawShapeText.setText(getContext().getString(currentShape.getStringResId()));
//	}
//
//	@Override
//	protected void loadLevels() {
//		levels.add(new PracticeLevel(getContext().getResources().getColor(R.color.BlanchedAlmond), LEVEL_6, level6Shapes));
//	}
//
//}
//

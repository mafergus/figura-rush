package com.phoenixunknownapps.figurarushextreme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PracticeGame extends GameWindow {

	private ImageView shapeImageView;
	private Button pickShapeButton;

	public PracticeGame(Context context) {
		this(context, null);
	}

	public PracticeGame(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PracticeGame(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		shapeImageView = findViewById(R.id.shapeImage);
		shapeImageView.setVisibility(VISIBLE);
		pickShapeButton = findViewById(R.id.pickShapeButton);
		pickShapeButton.setTypeface(fontRegular);
		pickShapeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SelectShapePopupWindow popup = new SelectShapePopupWindow(getContext(), new SelectShapePopupWindow.ItemSelectedCallback() {

					@Override
					public void onItemSelected(GestureType shape) {
						if (shape == GestureType.INVALID) {
							pickShapeButton.setText(context.getString(R.string.random));
							((PracticeLevel)currentLevel).setRandom(true);
						} else {
							pickShapeButton.setText(context.getString(shape.getStringRes()));
							((PracticeLevel)currentLevel).setRandom(false);
							((PracticeLevel)currentLevel).setShape(shape);
							shapeImageView.setImageResource(getShapeImage(shape));
						}
						nextShape();
					}
				});
				popup.showAtLocation(rootView, Gravity.CENTER, 0, 0);
			}
		});
		pickShapeButton.setVisibility(View.VISIBLE);

		timeBonusText.setVisibility(View.GONE);
		timerCounterTextView.setVisibility(View.INVISIBLE);
		scoreTv.setVisibility(View.INVISIBLE);

		loadLevels();
	}

	@Override
	public void startGame(int level) {
		isStarted = true;
		nextShape();
	}

	@Override
	protected void correctShape() {
		nextShape();
	}

	@Override
	protected void nextShape() {
		currentShape = currentLevel.getShape();
		shapeImageView.setImageResource(getShapeImage(currentShape));
		drawShapeText.setText(getContext().getString(currentShape.getStringRes()));
	}

	@Override
	protected void loadLevels() {
		levels.add(new PracticeLevel(getContext().getResources().getColor(R.color.BlanchedAlmond), LEVEL_6, level6Shapes));
	}

	static public int getShapeImage(GestureType shape) {
		switch (shape.ordinal()) {
		case 1:
			return R.drawable.triangle;
		case 2:
			return R.drawable.square;
		case 3:
			return R.drawable.circle;
		case 4:
			return R.drawable.clover;
		case 5:
			return R.drawable.heart;
		case 6:
			return R.drawable.moon;
		case 7:
			return R.drawable.star;
		default:
			return R.drawable.triangle;
		}
	}

}


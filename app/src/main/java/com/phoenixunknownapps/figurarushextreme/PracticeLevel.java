package com.phoenixunknownapps.figurarushextreme;

import com.phoenixunknownapps.figurarushextreme.GameBase.GestureType;

import java.util.List;

public class PracticeLevel extends Level {
	private boolean isRandom = true;
	private GestureType shape = GestureType.CIRCLE;

	public PracticeLevel(int color, int points, List<GestureType> shapes) {
		super(color, points, shapes);
	}

	@Override
	public GestureType getShape() {
		if (isRandom) { 
			return super.getShape();
		} else {
			return shape;
		}
	}

	public void setRandom(boolean isRandom) { this.isRandom = isRandom; }
	public void setShape(final GestureType shape) { this.shape = shape; }
}

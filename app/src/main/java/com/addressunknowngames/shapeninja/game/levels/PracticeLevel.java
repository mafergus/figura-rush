package com.addressunknowngames.shapeninja.game.levels;

import com.addressunknowngames.shapeninja.model.shapes.Circle;
import com.addressunknowngames.shapeninja.model.shapes.Shape;

import java.util.List;

public class PracticeLevel extends Level {
	private boolean isRandom = true;
	private Shape shape = new Circle();

	public PracticeLevel(int color, int points, List<Shape> shapes) {
		super(color, points, shapes);
	}

	@Override
	public Shape getShape() {
		if (isRandom) { 
			return super.getShape();
		} else {
			return shape;
		}
	}

	public void setRandom(boolean isRandom) { this.isRandom = isRandom; }
	public void setShape(final Shape shape) { this.shape = shape; }
}

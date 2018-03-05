package com.addressunknowngames.shapeninja.game.levels;

import com.addressunknowngames.shapeninja.model.shapes.Shape;

import java.util.List;
import java.util.Random;

public class Level {
	final int color;
	final int points;
	final List<Shape> shapes;
	
	private Random randomGenerator;
	
	public Level(int color, int points, final List<Shape> shapes) {
		this.color = color;
		this.points = points;
		this.shapes = shapes;
		randomGenerator = new Random();
	}
	
	private Shape getRandomShape(final List<Shape> shapes) {
		int index = randomGenerator.nextInt(shapes.size());
		return shapes.get(index);
	}
	
	public int getColor() { return color; }
	public Shape getShape() { return getRandomShape(shapes); }
	public int getPoints() { return points; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + points;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Level other = (Level) obj;
		if (points != other.points)
			return false;
		return true;
	}
}

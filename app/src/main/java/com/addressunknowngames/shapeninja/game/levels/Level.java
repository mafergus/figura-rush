package com.addressunknowngames.shapeninja.game.levels;

import android.content.Context;

import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.model.shapes.Circle;
import com.addressunknowngames.shapeninja.model.shapes.Clover;
import com.addressunknowngames.shapeninja.model.shapes.Heart;
import com.addressunknowngames.shapeninja.model.shapes.Moon;
import com.addressunknowngames.shapeninja.model.shapes.Shape;
import com.addressunknowngames.shapeninja.model.shapes.Square;
import com.addressunknowngames.shapeninja.model.shapes.Star;
import com.addressunknowngames.shapeninja.model.shapes.Triangle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Level {
	protected static final int LEVEL_1 = 5;
	protected static final int LEVEL_2 = 15;
	protected static final int LEVEL_3 = 35;
	protected static final int LEVEL_4 = 60;
	protected static final int LEVEL_5 = 100;
	protected static final int LEVEL_6 = 9999;

	final int color;
	final int points;
	final List<Shape> shapes;
	
	private Random randomGenerator;

	public static List<Level> allLevels(Context context) {
		return Arrays.asList(
				Level.levelOne(context),
				Level.levelTwo(context),
				Level.levelThree(context),
				Level.levelFour(context),
				Level.levelFive(context),
				Level.levelSix(context)
		);
	}

	public static Level levelOne(Context context) {
		List<Shape> level1Shapes = Arrays.asList(new Circle(), new Triangle());
		return new Level(context.getResources().getColor(R.color.BlanchedAlmond), LEVEL_1, level1Shapes);
	}

	public static Level levelTwo(Context context) {
		List<Shape> level2Shapes = Arrays.asList(new Circle(), new Triangle(), new Square());
		return new Level(context.getResources().getColor(R.color.Lavender), LEVEL_2, level2Shapes);
	}

	public static Level levelThree(Context context) {
		List<Shape> level3Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon());
		return new Level(context.getResources().getColor(R.color.Olive), LEVEL_3, level3Shapes);
	}

	public static Level levelFour(Context context) {
		List<Shape> level4Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart());
		return new Level(context.getResources().getColor(R.color.Ivory), LEVEL_4, level4Shapes);
	}

	public static Level levelFive(Context context) {
		List<Shape> level5Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart(), new Star());
		return new Level(context.getResources().getColor(R.color.Bisque), LEVEL_5, level5Shapes);
	}

	public static Level levelSix(Context context) {
		List<Shape> level6Shapes = Arrays.asList(new Circle(), new Triangle(), new Square(), new Moon(), new Heart(), new Star(), new Clover());
		return new Level(context.getResources().getColor(R.color.Gold), LEVEL_6, level6Shapes);
	}
	
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

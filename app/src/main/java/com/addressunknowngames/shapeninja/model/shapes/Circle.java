package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Circle implements Shape {
    @Override
    public int getStringResId() {
        return R.string.circle;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.circle;
    }

    @Override
    public String getName() { return "Circle"; }
}

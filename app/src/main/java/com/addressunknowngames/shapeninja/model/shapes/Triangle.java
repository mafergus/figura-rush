package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Triangle implements Shape {
    @Override
    public int getStringResId() {
        return R.string.triangle;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.triangle;
    }

    @Override
    public String getName() { return "Triangle"; }
}

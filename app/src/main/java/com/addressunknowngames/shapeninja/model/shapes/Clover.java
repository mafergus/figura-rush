package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Clover implements Shape {
    @Override
    public int getStringResId() {
        return R.string.clover;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.clover;
    }

    @Override
    public String getName() { return "Clover"; }
}

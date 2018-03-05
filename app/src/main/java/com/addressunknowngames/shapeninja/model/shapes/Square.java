package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Square implements Shape {
    @Override
    public int getStringResId() {
        return R.string.square;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.square;
    }

    @Override
    public String getName() { return "Square"; }
}

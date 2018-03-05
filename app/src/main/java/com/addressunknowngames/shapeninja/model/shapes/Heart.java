package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Heart implements Shape {
    @Override
    public int getStringResId() {
        return R.string.heart;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.heart;
    }

    @Override
    public String getName() { return "Heart"; }
}

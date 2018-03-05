package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Moon implements Shape {
    @Override
    public int getStringResId() {
        return R.string.moon;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.moon;
    }

    @Override
    public String getName() { return "Moon"; }
}

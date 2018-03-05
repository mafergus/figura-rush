package com.addressunknowngames.shapeninja.model.shapes;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 3/4/18.
 */

public class Star implements Shape {

    @Override
    public int getStringResId() {
        return R.string.star;
    }

    @Override
    public int getShapeImageResId() {
        return R.drawable.star;
    }

    @Override
    public String getName() { return "Star"; }
}

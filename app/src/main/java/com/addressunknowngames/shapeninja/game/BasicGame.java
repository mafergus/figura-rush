package com.addressunknowngames.shapeninja.game;

import android.content.Context;
import android.util.AttributeSet;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 10/21/15.
 */
public class BasicGame extends GameWindow {

    public BasicGame(Context context) {
        super(context);
    }
    public BasicGame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BasicGame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.basic_game;
    }


}

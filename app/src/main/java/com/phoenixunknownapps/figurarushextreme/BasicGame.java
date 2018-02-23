package com.phoenixunknownapps.figurarushextreme;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by matthewferguson on 10/21/15.
 */
public class BasicGame extends GameBase {

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

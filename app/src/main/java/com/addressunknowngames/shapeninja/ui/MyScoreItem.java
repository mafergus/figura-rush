package com.addressunknowngames.shapeninja.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.R;

/**
 * Created by matthewferguson on 10/22/15.
 */
public class MyScoreItem extends HighScoreItem {

    public MyScoreItem(Context context) {
        this(context, null);
    }

    public MyScoreItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScoreItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        label.setTextColor(getResources().getColorStateList(R.color.text_press_selector));
    }

    public void setLabelOnTouchListener(OnTouchListener touchListener) {
        label.setOnTouchListener(touchListener);
    }

    public TextView getLabel() {
        return label;
    }
}

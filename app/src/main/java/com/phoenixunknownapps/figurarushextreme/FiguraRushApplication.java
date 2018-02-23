package com.phoenixunknownapps.figurarushextreme;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by matthewferguson on 10/21/15.
 */
public class FiguraRushApplication extends Application {

    private static final String TAG = FiguraRushApplication.class.getCanonicalName();

    public Typeface FONT_BOLD;
    public Typeface FONT_REGULAR;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());

        FONT_BOLD = Typeface.createFromAsset(getAssets(), "JosefinSans-Bold.ttf");
        FONT_REGULAR = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
    }

    public Typeface getFontRegular() {
        return FONT_REGULAR;
    }

    public Typeface getFontBold() {
        return FONT_BOLD;
    }
}

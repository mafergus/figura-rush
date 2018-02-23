package com.phoenixunknownapps.figurarushextreme;

import android.app.Application;
import android.graphics.Typeface;

import com.parse.Parse;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by matthewferguson on 10/21/15.
 */
public class FiguraRushApplication extends Application {

    public Typeface FONT_BOLD;
    public Typeface FONT_REGULAR;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        FONT_BOLD = Typeface.createFromAsset(getAssets(), "JosefinSans-Bold.ttf");
        FONT_REGULAR = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "EjhEVFyKD2hreH9ZE02W6W8hYFj8yUHb1O4YNgU6", "7zFqDS9CmeeVsULqtSell7dbdT8cXXMaXlkr6f0n");
    }

    public Typeface getFontRegular() {
        return FONT_REGULAR;
    }

    public Typeface getFontBold() {
        return FONT_BOLD;
    }
}

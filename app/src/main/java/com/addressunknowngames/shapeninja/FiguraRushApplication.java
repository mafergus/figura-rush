package com.addressunknowngames.shapeninja;

import android.app.Application;
import android.graphics.Typeface;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

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

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FONT_BOLD = Typeface.createFromAsset(getAssets(), "josefinsans_bold.ttf");
        FONT_REGULAR = Typeface.createFromAsset(getAssets(), "josefinsans_regular.ttf");
    }

    public Typeface getFontRegular() {
        return FONT_REGULAR;
    }

    public Typeface getFontBold() {
        return FONT_BOLD;
    }
}

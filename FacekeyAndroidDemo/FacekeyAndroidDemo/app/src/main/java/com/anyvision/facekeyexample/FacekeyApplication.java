package com.anyvision.facekeyexample;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by igal on 15/11/2017.
 */

public class FacekeyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Heebo-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getAppContext() {
        return context;
    }
}

package com.anyvision.facekeyexample.models;

import android.text.TextUtils;

import com.anyvision.facekeyexample.utils.SerializationUtils;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

/**
 * Created by igal on 15/11/2017.
 */

public class AppData {

    private static final String SETTINGS_KEY = "settings_key";
    private static final String FIRST_NAME_KEY = "fist name_key";
    private static final String LAST_NAME_KEY = "last name_key";

    private static AppData instance;

    private File video;

    private String firstName;
    private String lastName;
    private File idImg;
    private File refImg;

    private Settings settings;

    private synchronized static AppData getInstance(){
        if(instance == null){
            instance = new AppData();
        }
        return instance;
    }

    private AppData(){}

    public static File getVideo() {
        return getInstance().video;
    }

    public static void setVideo(File video) {
        getInstance().video = video;
    }

    public static Settings getSettings() {
        if (getInstance().settings == null){
            String settingsJson = Prefs.getString(SETTINGS_KEY, null);
            if (TextUtils.isEmpty(settingsJson)){
                getInstance().settings = new Settings();
            }
            else {
                getInstance().settings = SerializationUtils.fromJson(settingsJson, Settings.class);
            }
        }

        return getInstance().settings;
    }

    public static void saveSettings(){
        Prefs.putString(SETTINGS_KEY, SerializationUtils.toJson(getInstance().settings));
    }

    public static String getFirstName() {
        if (TextUtils.isEmpty(getInstance().firstName)){
            getInstance().firstName = Prefs.getString(FIRST_NAME_KEY, "");
        }
        return getInstance().firstName;
    }

    public static void setFirstName(String firstName) {
        getInstance().firstName = firstName;
        Prefs.putString(FIRST_NAME_KEY, firstName);
    }

    public static String getLastName() {
        if (TextUtils.isEmpty(getInstance().lastName)){
            getInstance().lastName = Prefs.getString(LAST_NAME_KEY, "");
        }
        return getInstance().lastName;
    }

    public static void setLastName(String lastName) {
        getInstance().lastName = lastName;
        Prefs.putString(LAST_NAME_KEY, lastName);
    }

    public static File getRefImg() {
        return getInstance().refImg;
    }

    public static void setRefImg(File img) {
        getInstance().refImg = img;
    }

    public static File getIdImg() {
        return getInstance().idImg;
    }

    public static void setIdImg(File idImg) {
        getInstance().idImg = idImg;
    }
}

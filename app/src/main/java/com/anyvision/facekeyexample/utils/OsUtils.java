package com.anyvision.facekeyexample.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by igal on 18/03/2018.
 */

public class OsUtils {
    private static float SCREEN_HEIGHT = 0;

    public static File saveImage(Context context, String filename, byte[] img){
        File file = new File(context.getExternalFilesDir(null), filename);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(img);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }

    public static File saveImage(Context context, String filename, Bitmap img){
        File file = new File(context.getExternalFilesDir(null), filename);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.PNG, 100, output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }

    public static boolean isFaceSmallEnough(Activity context, Rect face){
        if (SCREEN_HEIGHT == 0){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            SCREEN_HEIGHT = displayMetrics.heightPixels;
        }

        return ((face.height() * 100) / SCREEN_HEIGHT) < 12;
    }

}

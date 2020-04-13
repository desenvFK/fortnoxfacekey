package com.anyvision.facekeyexample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import retrofit2.Callback;

/**
 * Created by igal on 21/11/2017.
 */

public class UiUtils {
    public static Animation getScannerAnimation() {
        Animation animation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, -0.05f,
                TranslateAnimation.ABSOLUTE, 1.05f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.05f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.05f);
        animation.setDuration(1000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new LinearInterpolator());

        return animation;
    }

    public static void setCameraDisplayOrientation(Activity activity, Camera cam) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(1, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        cam.setDisplayOrientation(result);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static void showToast(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}

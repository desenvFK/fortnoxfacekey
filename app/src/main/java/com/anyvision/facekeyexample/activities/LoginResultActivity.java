package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anyvision.facekey.Facekey;
import com.anyvision.facekey.listeners.IAuthenticateListener;
import com.anyvision.facekey.listeners.IRegisterListener;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.logged.MainActivity;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginResultActivity extends BaseActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");

    private View progressBar;
    private TextView resultText;
    private ImageView resultImage;
    private View tryAgainContainer;
    private View horizontalSeparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        progressBar = findViewById(R.id.progress_bar);
        resultText = findViewById(R.id.result_text);
        resultImage = findViewById(R.id.result_img);
        tryAgainContainer = findViewById(R.id.try_again_container);
        horizontalSeparator = findViewById(R.id.horizontal_separator);
        authenticate();
    }

    private void authenticate() {
        resultText.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        tryAgainContainer.setVisibility(View.GONE);
        horizontalSeparator.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String userId = InfoMobile.getMacAddress() + "/" + GetVariables.getInstance().getEtUsername();
        Log.d("debug", userId);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Facekey.authenticateUser(AppData.getVideo(),
                userId,
                new IAuthenticateListener() {
                    @Override
                    public void onSuccess(float v, float v1) {
                        MainActivity.startActivity(LoginResultActivity.this);
                    }

                    @Override
                    public void onFailure(Throwable reason, float v, float v1) {
                        onResult(false, reason.getMessage());
                    }
                });

    }

    private void onResult(boolean isSuccess, String msg) {
        resultText.setVisibility(View.VISIBLE);
        resultImage.setVisibility(View.VISIBLE);
        tryAgainContainer.setVisibility(View.VISIBLE);
        horizontalSeparator.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        resultText.setText(msg);
        if (isSuccess){
            resultImage.setImageDrawable(getDrawable(R.drawable.success));
        }
        else {
            resultImage.setImageDrawable(getDrawable(R.drawable.failure));
        }
        File videoAndImageDir = AppData.getVideo().getParentFile();
        AppData.getVideo().delete();
        //AppData.getIdImg().delete();
        videoAndImageDir.delete();
    }

    public static void startActivity(Context from){
        Intent intent = new Intent(from, LoginResultActivity.class);
        from.startActivity(intent);
    }
}

package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekey.Facekey;
import com.anyvision.facekey.listeners.IRegisterListener;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Response;

public class ResultActivity extends BaseActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    private static final String TAG = "ResultActivity";

    private View progressBar;
    private TextView resultText;
    private ImageView resultImage;
    private View tryAgainContainer;
    private View horizontalSeparator;
    private TextView tryAgain;
    private TextView backMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        progressBar = findViewById(R.id.progress_bar);
        resultText = findViewById(R.id.result_text);
        resultImage = findViewById(R.id.result_img);
        tryAgainContainer = findViewById(R.id.try_again_container);
        horizontalSeparator = findViewById(R.id.horizontal_separator);
        tryAgain = findViewById(R.id.try_again);
        backMenu = findViewById(R.id.menu);

        View tryAgain = findViewById(R.id.try_again);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoActivity.startActivity(view.getContext());
                finish();
            }
        });
        matchPhotos();

        backMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startActivity(ResultActivity.this);
            }
        });

    }

    private void matchPhotos() {
        resultText.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        tryAgainContainer.setVisibility(View.GONE);
        horizontalSeparator.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String userId = InfoMobile.getMacAddress() + "/" + GetVariables.getInstance().getEtRegisterUsername();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Facekey.registerUser(AppData.getIdImg(),
                AppData.getVideo(),
                userId,
                null,
                true,
                new IRegisterListener() {
                    @Override
                    public void onSuccess(float score, float livenessScore) {
                        onResult(true, getString(R.string.result_success));
                    }

                    @Override
                    public void onFailure(Throwable reason, float score, float livenessScore) {
                        onResult(false, reason.getMessage());
                    }
                });

    }

    private void onResult(boolean isSuccess, String msg) {
        resultText.setVisibility(View.VISIBLE);
        resultImage.setVisibility(View.VISIBLE);
        horizontalSeparator.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        resultText.setText(msg);
        if (isSuccess){
            resultImage.setImageDrawable(getDrawable(R.drawable.success));
            tryAgainContainer.setVisibility(View.VISIBLE);
            backMenu.setVisibility(View.VISIBLE);
            tryAgain.setVisibility(View.GONE);
        }
        else {
            resultImage.setImageDrawable(getDrawable(R.drawable.failure));
            tryAgainContainer.setVisibility(View.VISIBLE);
            tryAgain.setVisibility(View.VISIBLE);
            backMenu.setVisibility(View.GONE);
        }
        File videoAndImageDir = AppData.getVideo().getParentFile();
        //AppData.getVideo().delete();
        //AppData.getIdImg().delete();
        videoAndImageDir.delete();
    }
    public static void startActivity(Context from){
        Intent intent = new Intent(from, ResultActivity.class);
        from.startActivity(intent);
    }
}

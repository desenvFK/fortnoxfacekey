package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekey.Facekey;
import com.anyvision.facekey.listeners.IRegisterListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultActivity extends BaseActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");

    private View progressBar;
    private TextView resultText;
    private ImageView resultImage;
    private View tryAgainContainer;
    private View horizontalSeparator;
    private View surveyContainer;
//    private TextView real;
//    private TextView fake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        progressBar = findViewById(R.id.progress_bar);
        resultText = findViewById(R.id.result_text);
        resultImage = findViewById(R.id.result_img);
        tryAgainContainer = findViewById(R.id.try_again_container);
        horizontalSeparator = findViewById(R.id.horizontal_separator);
        View tryAgain = findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoActivity.startActivity(view.getContext());
                finish();
            }
        });
        surveyContainer = findViewById(R.id.survey_container);
//        fake = findViewById(R.id.fake);
//        real = findViewById(R.id.real);
//        fake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                submitSurvey(false);
//            }
//        });
//        real.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                submitSurvey(true);
//            }
//        });

        matchPhotos();
    }

    private void matchPhotos() {
        resultText.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        tryAgainContainer.setVisibility(View.GONE);
        horizontalSeparator.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Facekey.registerUser(AppData.getIdImg(),
                AppData.getVideo(),
                String.format(AppData.getFirstName() + "_" + AppData.getLastName() + "_" + DATE_FORMAT.format(Calendar.getInstance().getTime())),
                null,
                false,
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
        AppData.getIdImg().delete();
        videoAndImageDir.delete();
    }
    public static void startActivity(Context from){
        Intent intent = new Intent(from, ResultActivity.class);
        from.startActivity(intent);
    }
}

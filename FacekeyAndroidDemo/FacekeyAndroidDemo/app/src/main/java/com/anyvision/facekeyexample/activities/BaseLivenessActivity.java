package com.anyvision.facekeyexample.activities;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.anyvision.facekey.listeners.ILivenessListener;
import com.anyvision.facekey.listeners.eEvent;
import com.anyvision.facekey.liveness.FaceDetectorNotOperationalException;
import com.anyvision.facekey.liveness.LivenessView;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseLivenessActivity extends BaseActivity implements ILivenessListener {

    private static final int COUNT_DOWN_TIME = 5000;
    private static final int RECORD_TIME = 3000;

    private enum eStage {
        init, countdown, recording, smiling, blinking, startSmiling, startBlinking
    }

    private LivenessView livenessView;
    private TextView message;
    private TextView counter;
    private CountDownTimer countDownTimer;
    private String videoPath;
    private boolean isLivenessOn;
    private boolean isChallengeEnabled = false;
    private eStage stage = eStage.init;
    private Timer recordingTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        message = findViewById(R.id.message);
        counter = findViewById(R.id.counter);
        livenessView = findViewById(R.id.livenessView);
        livenessView.initialize(this);
        livenessView.setChallangeEnabled(isChallengeEnabled);
        File video = new File(getVideoPath());
        AppData.setVideo(video);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (livenessView != null) {
            livenessView.release();
        }
    }

    @Override
    public void onFrameCallback(eEvent event) {
        String error = null;
        switch (event) {
            case noFace:
                error = getString(R.string.no_face_in_video);
                break;
            case tooManyFaces:
                error = getString(R.string.too_many_in_video);
                break;
            case faceTooClose:
                error = getString(R.string.face_too_large);
                break;
            case faceTooFar:
                error = getString(R.string.face_too_small);
                break;
            case faceSmiling:
                startRecordingAndLivenessPattern();
                break;
            case startSmiling:
//                setTextViewText(message, getString(R.string.smiling_text));
                break;
            case faceBlinking:
                if (!isLivenessOn) {
                    startRecordingAndLivenessPattern();
                }
                break;
            case startBlinking:
//                setTextViewText(message, getString(R.string.start_blinking));
                break;
            case faceRightSize:
                break;
        }

        if (!TextUtils.isEmpty(error)) {
            setTextViewText(message, error);
            if (stage == eStage.countdown) {
                if(!isChallengeEnabled)
                    restartCountdown();
            }

        } else if(stage == eStage.init && countDownTimer == null) {
            if(!isChallengeEnabled) {
                runCountDown();
            }
        }
    }

    private void restartCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                counter.setVisibility(View.GONE);
            }
        });

        setTextViewText(counter, getTimeInSeconds(COUNT_DOWN_TIME));
        stage = eStage.init;
    }

    protected void runCountDown() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(COUNT_DOWN_TIME, 900) {
                    int secondsLeft = 0;

                    @Override
                    public void onTick(final long millisUntilFinished) {
                        if (Math.round((float) millisUntilFinished / 1000.0f) != secondsLeft) {
                            secondsLeft = Math.round((float) millisUntilFinished / 1000.0f);
                            setTextViewText(counter, String.valueOf(secondsLeft));
                        }
                    }

                    @Override
                    public void onFinish() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                counter.setVisibility(View.GONE);
                            }
                        });

                        startRecordingAndLivenessPattern();
                    }
                }.start();
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTextViewText(message, getString(R.string.get_ready));
                counter.setVisibility(View.VISIBLE);
            }
        });

        stage = eStage.countdown;
    }

    private void startRecordingAndLivenessPattern() {
        isLivenessOn = true;
        startRecordingVideo();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTextViewText(message, getString(R.string.processing));

                livenessView.startPattern(BaseLivenessActivity.this);


                recordingTimer = new Timer();
                recordingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stopRecordingVideo();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                livenessView.stopPattern();
                            }
                        });

                        ResultActivity.startActivity(BaseLivenessActivity.this);
                        isLivenessOn = false;
                        finish();
                    }
                }, RECORD_TIME);

                stage = eStage.recording;
            }
        });

    }

    protected void onFrame(Bitmap frame) {
        try {
            livenessView.onFrame(frame, 0);
        } catch (FaceDetectorNotOperationalException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    protected void onFrame(ByteBuffer frame, int width, int height) {
        try {
            livenessView.onFrame(frame, width, height);
        } catch (FaceDetectorNotOperationalException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected String getVideoPath() {
        if (TextUtils.isEmpty(videoPath)) {
            videoPath = getDir() + "/video.mp4";
        }
        return videoPath;
    }

    private String getDir() {
        File dirFile = getExternalFilesDir(null);
        String dir = (dirFile == null ? "" : (dirFile.getAbsolutePath() + "/"))
                + Calendar.getInstance().getTimeInMillis();

        File d = new File(dir);
        if (!d.exists()) {
            d.mkdirs();
        }

        return dir;
    }

    private void setTextViewText(final TextView textView, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    private String getTimeInSeconds(long millis) {
        return "" + (millis / 1000);
    }

    protected abstract int getLayoutId();

    protected abstract void startRecordingVideo();

    protected abstract void stopRecordingVideo();
}

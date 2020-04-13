package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.anyvision.facekey.listeners.ILivenessListener;
import com.anyvision.facekey.listeners.eEvent;
import com.anyvision.facekey.liveness.CameraLivenessView;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.size.AspectRatio;
import com.otaliastudios.cameraview.size.SizeSelector;
import com.otaliastudios.cameraview.size.SizeSelectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
public class CameraActivity extends BaseActivity implements ILivenessListener {
    private static final int COUNT_DOWN_TIME = 3000;
    private static final int RECORD_TIME = 3000;
    public static final int COUNT_DOWN_INTERVAL = 900;
    public static final float MILLISECOND = 1000.0f;
    public static final String VIDEO_MP_4 = "/video.mp4";
    public static final String VIDEO1_MP_4 = "/video1.mp4";

    private enum eStage {
        init, countdown, recording, smiling, blinking, startSmiling, startBlinking
    }

    private CameraLivenessView cameraLivenessView;
    private TextView message;
    private TextView counter;
    private CountDownTimer countDownTimer;
    private String videoPath;
    private boolean isChallengeEnabled = false;
    private eStage stage = eStage.init;
    private Timer recordingTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        message = findViewById(R.id.message);
        counter = findViewById(R.id.counter);
        cameraLivenessView = findViewById(R.id.cameraLiveness);
        cameraLivenessView.setChallangeEnabled(false);
        cameraLivenessView.initialize(this);
        //setCameraMaxSize(cameraLivenessView.o);
        File video = new File(getVideoPath());
        AppData.setVideo(video);
    }

    private void setCameraMaxSize(CameraView cameraLivenessView) {
        SizeSelector width = SizeSelectors.maxWidth(1280);
        SizeSelector height = SizeSelectors.maxHeight(720);
        SizeSelector dimensions = SizeSelectors.and(width, height); // Matches sizes bigger than 1000x2000.
        //SizeSelector ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0); // Matches 1:1 sizes.
        SizeSelector result = SizeSelectors.or(
                dimensions,
                SizeSelectors.smallest() // If none is found, take the biggest
        );
        cameraLivenessView.setPictureSize(result);
        cameraLivenessView.setVideoSize(result);
        cameraLivenessView.setPreviewStreamSize(result);

    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraLivenessView.openCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraLivenessView.closeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraLivenessView.destroyCamera();
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
            case faceNotAligned:
                error = getString(R.string.align_face);
                break;
            case faceSmiling:
                break;
            case startSmiling:
                break;
            case faceBlinking:
                break;
            case startBlinking:
                break;
            case faceRightSize:
                break;
        }
        if (!TextUtils.isEmpty(error)) {
            setTextViewText(message, error);
            if (stage == eStage.countdown) {
                if (!isChallengeEnabled)
                    restartCountdown();
            }

        } else if (stage == eStage.init && countDownTimer == null) {
            if (!isChallengeEnabled) {
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

    private void runCountDown() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(COUNT_DOWN_TIME, COUNT_DOWN_INTERVAL) {
                    int secondsLeft = 0;

                    @Override
                    public void onTick(final long millisUntilFinished) {
                        if (Math.round((float) millisUntilFinished / MILLISECOND) != secondsLeft) {
                            secondsLeft = Math.round((float) millisUntilFinished / MILLISECOND);
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
        stage = eStage.countdown;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTextViewText(message, getString(R.string.get_ready));
                counter.setVisibility(View.VISIBLE);
            }
        });

    }

    private void startRecordingAndLivenessPattern() {
        cameraLivenessView.startRecordingVideo(AppData.getVideo().getPath());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTextViewText(message, getString(R.string.processing));
                cameraLivenessView.startPattern(CameraActivity.this);
                recordingTimer = new Timer();
                recordingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        cameraLivenessView.stopRecordingVideo();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cameraLivenessView.stopPattern();
                            }
                        });

                        ResultActivity.startActivity(CameraActivity.this);
                        finish();
                    }
                }, RECORD_TIME);
                stage = eStage.recording;
            }
        });
    }

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    protected String getVideoPath() {
        if (TextUtils.isEmpty(videoPath)) {
            videoPath = getDir() + VIDEO_MP_4;
        }
        return videoPath;
    }

    protected String getVideo1Path() {
        if (TextUtils.isEmpty(videoPath)) {
            videoPath = getDir() + VIDEO1_MP_4;
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
        return "" + (millis / (int)MILLISECOND);
    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, CameraActivity.class);
        from.startActivity(intent);
    }
}
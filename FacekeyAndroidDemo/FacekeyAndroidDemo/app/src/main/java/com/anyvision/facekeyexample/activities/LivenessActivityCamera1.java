package com.anyvision.facekeyexample.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.anyvision.facekeyexample.R;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;


import java.io.ByteArrayOutputStream;
import java.io.File;

public class LivenessActivityCamera1 extends BaseLivenessActivity {

    private CameraView camera;
    private FrameProcessor frameProcessor = new FrameProcessor() {

        @SuppressLint("WrongThread")
        @Override
        public void process(@NonNull Frame frame) {
            Bitmap bitmap = createBitmapFromFrame(frame);
            onFrame(bitmap);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camera = findViewById(R.id.camera);
        camera.addFrameProcessor(frameProcessor);

    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        camera.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_liveness_camera1;
    }

    @Override
    protected void startRecordingVideo() {
        camera.takeVideo(new File(getVideoPath()));
    }

    @Override
    protected void stopRecordingVideo() {
        camera.stopVideo();
    }

    private Bitmap createBitmapFromFrame(Frame frame){

        Size size = frame.getSize();
        YuvImage yuvimage = new YuvImage(frame.getData(), ImageFormat.NV21, size.getWidth(), size.getHeight(), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, size.getWidth(), size.getHeight()), 50, baos); // Where 100 is the quality of the generated jpeg
        byte[] jpegArray = baos.toByteArray();

        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f); // flip horizontally
        matrix.postRotate(-90);
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.length);

        return  Bitmap.createBitmap(bitmap, 0, 0, yuvimage.getWidth(), yuvimage.getHeight(), matrix, true);
    }

    public static void startActivity(Context from){
        Intent intent = new Intent(from, LivenessActivityCamera1.class);
        from.startActivity(intent);
    }
}

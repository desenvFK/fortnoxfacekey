package com.anyvision.facekeyexample.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import com.anyvision.facekey.Facekey;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;

public class LoginActivity extends BaseActivity {

    private static final int TIMEOUT = 120000;
    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private static final float THRESHOLD = 0.30f;

    private View signUpBtn;
    private View progressBar;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpBtn = findViewById(R.id.signup);

        progressBar = findViewById(R.id.progress_bar);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);
                Facekey.initialize(AppData.getSettings().getBaseUrl(),TIMEOUT, THRESHOLD);
                InstructionsActivity.startActivity(LoginActivity.this);
            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.startActivity(view.getContext());
            }
        });

        getPermissions();
        if (!Settings.System.canWrite(this)) {
            showBrightnessPermissionDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        removeProgress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
                }
            }
        }
    }

    private void getPermissions() {
        boolean hasPermissions = true;
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermissions = false;
                break;
            }
        }

        if (!hasPermissions){
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
        }
    }
    private void removeProgress(){
        progressBar.setVisibility(View.GONE);
        signUpBtn.setEnabled(true);
    }
    private void showBrightnessPermissionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.writing_permission_alert_title));
        builder.setMessage(getString(R.string.writing_permission_alert_message))
                .setPositiveButton(getString(R.string.cont), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}

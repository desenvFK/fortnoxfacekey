package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.EditText;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.Settings;
import com.xw.repo.BubbleSeekBar;

public class SettingsActivity extends BaseActivity {

    private BubbleSeekBar thresholdSeekBar;
    private BubbleSeekBar vidTimeSeekBar;
    private BubbleSeekBar imageCompressionSeekBar;
    private SwitchCompat doDeleteSwitch;

    private EditText server_url;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = AppData.getSettings();

        thresholdSeekBar = findViewById(R.id.threshold_seekbar);
        vidTimeSeekBar = findViewById(R.id.vid_time_seekbar);
        imageCompressionSeekBar = findViewById(R.id.compression_seekbar);
        doDeleteSwitch = findViewById(R.id.do_delete_switch);
        server_url = findViewById(R.id.server_url);

        thresholdSeekBar.setProgress(Math.round(settings.getThreshold() * 100));
        vidTimeSeekBar.setProgress(settings.getVideoTime());
        imageCompressionSeekBar.setProgress(settings.getImageCompressionRate());
        doDeleteSwitch.setChecked(settings.isDoDelete());
        server_url.setText(AppData.getSettings().getBaseUrl());
    }

    @Override
    protected void onStop() {
        super.onStop();

        settings.setThreshold(thresholdSeekBar.getProgress() / 100f);
        settings.setVideoTime(vidTimeSeekBar.getProgress());
        settings.setImageCompressionRate(imageCompressionSeekBar.getProgress());
        settings.setDoDelete(doDeleteSwitch.isChecked());
        settings.setBaseUrl(server_url.getText().toString());
        AppData.saveSettings();
    }


    public static void startActivity(Context from){
        Intent intent = new Intent(from, SettingsActivity.class);
        from.startActivity(intent);
    }
}

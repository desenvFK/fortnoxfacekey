package com.anyvision.facekeyexample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.models.AppData;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.Settings;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends BaseActivity {

    private BubbleSeekBar thresholdSeekBar;
    private BubbleSeekBar vidTimeSeekBar;
    private BubbleSeekBar imageCompressionSeekBar;
    private Button btnSend;

    private EditText server_url;
    private EditText anyvision_url;
    private Settings settings;
    private Spinner spType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSend = (Button) findViewById(R.id.btnSend);
        spType = (Spinner) findViewById(R.id.spType);
        listenerType();

        settings = AppData.getSettings();

        thresholdSeekBar = findViewById(R.id.threshold_seekbar);
        vidTimeSeekBar = findViewById(R.id.vid_time_seekbar);
        imageCompressionSeekBar = findViewById(R.id.compression_seekbar);

        server_url = findViewById(R.id.server_url);
        anyvision_url = findViewById(R.id.sesame_url);

        thresholdSeekBar.setProgress(Math.round(settings.getThreshold() * 100));
        vidTimeSeekBar.setProgress(settings.getVideoTime());
        imageCompressionSeekBar.setProgress(settings.getImageCompressionRate());
//        server_url.setText(AppData.getSettings().getBaseUrl());

        server_url.setText(GetVariables.getInstance().getServerUrl());
        anyvision_url.setText(GetVariables.getInstance().getEtAnyvisionUrl());


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetVariables.getInstance().setServerUrl(server_url.getText().toString());
                GetVariables.getInstance().setEtAnyvisionUrl(anyvision_url.getText().toString());
                GetVariables.getInstance().getEtLocalServerUrl().setText(server_url.getText().toString());
                GetVariables.getInstance().getTextviewAnyvision().setText(anyvision_url.getText().toString());

                GetVariables.getInstance().setSpTypeAccount(spType.getSelectedItem().toString());

                Toast.makeText(SettingsActivity.this, "IP Local Servidor: " + GetVariables.getInstance().getServerUrl() +
                        "\nIP Anyvision: " + GetVariables.getInstance().getEtAnyvisionUrl() +
                        "\nTipo de Conta: " + GetVariables.getInstance().getSpTypeAccount(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        settings.setThreshold(thresholdSeekBar.getProgress() / 100f);
        settings.setVideoTime(vidTimeSeekBar.getProgress());
        settings.setImageCompressionRate(imageCompressionSeekBar.getProgress());
        settings.setBaseUrl(server_url.getText().toString());
        AppData.saveSettings();
    }

    public void listenerType(){
        addItemsType();
    }

    public void addItemsType(){
        List<String> list = new ArrayList<String>();
        list.add("AGENCIA");
        list.add("REGIONAL");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        spType.setAdapter(dataAdapter);

    }
    public static void startActivity(Context from){
        Intent intent = new Intent(from, SettingsActivity.class);
        from.startActivity(intent);
    }
}

package com.anyvision.facekeyexample.activities.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anyvision.facekeyexample.R;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
    }


    public static void startActivity(Context from) {
        Intent intent = new Intent(from, TimeActivity.class);
        from.startActivity(intent);
    }
}

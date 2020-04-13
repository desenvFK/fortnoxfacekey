package com.anyvision.facekeyexample.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.anyvision.facekey.Facekey;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.login.TokenAuth;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.firebase.Notification;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.models.LocalServer;
import com.anyvision.facekeyexample.prysm.AuthToken;
import com.anyvision.facekeyexample.prysm.Authentication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.Thread.sleep;


public class LoginActivity extends BaseActivity {
    private Settings settings = new Settings();



    private static final int TIMEOUT = 120000;
    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private static final float THRESHOLD = 0.30f;

    private Button signUpBtn;
    private Button logInBtn;
    private Button btnPanico;
    private View progressBar;
    private InfoMobile infoMobile;
    private LocalServer localServer;
    private TextView serverLocalUrl;
    private TextView anyvisionUrl;
    private View SettingsComponent;
    private EditText etUsername;

    private Authentication auth;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        infoMobile = new InfoMobile();

        SettingsComponent = (View) findViewById(R.id.settings);

        logInBtn = (Button) findViewById(R.id.login);
        signUpBtn = (Button) findViewById(R.id.signup);
        btnPanico = (Button) findViewById(R.id.loginPanico);
        serverLocalUrl = (TextView) findViewById(R.id.serverLocalUrl);
        anyvisionUrl = (TextView) findViewById(R.id.anyvisionUrl);
        etUsername =  (EditText) findViewById(R.id.username);

        GetVariables.getInstance().setEtLocalServerUrl(serverLocalUrl);
        GetVariables.getInstance().setTextviewAnyvision(anyvisionUrl);

        if(GetVariables.getInstance().getSpTypeAccount() == null ){
            GetVariables.getInstance().setSpTypeAccount("AGENCIA");
        }

        if(GetVariables.getInstance().getServerUrl() == null ){
            GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());
        }

        if(GetVariables.getInstance().getEtAnyvisionUrl() == null ){
            GetVariables.getInstance().setEtAnyvisionUrl(anyvisionUrl.getText().toString());
        }


        GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());
        GetVariables.getInstance().setEtAnyvisionUrl(anyvisionUrl.getText().toString());

        progressBar = findViewById(R.id.progress_bar);

        auth = new Authentication(GetVariables.getInstance().getServerUrl());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        Log.d("auth", GetVariables.getInstance().getServerUrl());
        SettingsComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.startActivity(LoginActivity.this);
            }
        });

        btnPanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Notification.sendNotification();
                    auth.requestToken("App.AGENCIA.Panico", "true");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                //Facekey.initialize("http://sesame-liveness-poc-long.anyvision.co:3000",120000, 0.35f);
                //Facekey.initialize("http://192.168.0.89:3000",120000, 0.35f);
                Facekey.initialize(anyvisionUrl.getText().toString(),120000, 0.35f);
                RegisterActivity.startActivity(LoginActivity.this);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetVariables.getInstance().setServerUrl(serverLocalUrl.getText().toString());
                auth.verifyServerStatus();

                //Notification.requestTokenFirebaseNotification();

                Log.d("auth", GetVariables.getInstance().getServerUrl());
                Log.d("auth", String.valueOf(auth.getStatusServer()));
                Log.d("username", etUsername.getText().toString());


                try {
                    if (auth.getStatusServer()) {
                        if (etUsername.getText().toString().matches("")) {
                            Toast.makeText(LoginActivity.this, "Por favor, digite o nome de usuário definido no registro!", Toast.LENGTH_LONG).show();

                        } else {
                            GetVariables.getInstance().setEtUsername(etUsername.getText().toString());
                            progressBar.setVisibility(View.VISIBLE);
                            //Facekey.initialize("http://192.168.0.89:3000",120000, 0.35f);
                            Facekey.initialize(anyvisionUrl.getText().toString(),120000, 0.35f);
                            //Facekey.initialize("http://sesame-liveness-poc-long.anyvision.co:3000", 120000, 0.35f);
                            LoginCameraActivity.startActivity(LoginActivity.this);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Por favor, verifique o status do servidor local!", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.startActivity(view.getContext());
            }
        });

        getPermissions();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (!Settings.System.canWrite(this)) {
                showBrightnessPermissionDialog();
            }
        }

        Log.d("debug", InfoMobile.getMacAddress());


    }


    private void getParse(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl.getText().toString())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<ResponseBody> call = tokenAuth.getToken();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String token;
                String hashpassword;

                try {
                    Log.d("auth", response.toString());
                    token = cleanOUTPUT(response.body().string());
                    Log.d("auth", "SessionID: " + token);
                    hashpassword = md5(token + md5("REGIONALREGIONAL"));
                    Log.d("auth", "hashpassword: " +  hashpassword);

                    if(response.isSuccessful()) {
                        getAuthentication(token, hashpassword);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("token", t.getMessage());
            }
        });
    }

    private void getAuthentication(final String SessionId, String pass){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl.getText().toString())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Log.d("auth", pass);
        Call<Void> call = tokenAuth.signIn(SessionId,"REGIONAL", pass);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("auth", "Deu BOM");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                    setVariable(SessionId, "App.Regional.Reprogramacao", "true");
                } else {
                    Log.d("auth", "Deu ruim");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    private void setVariable(final String SessionId, String name, String newValue){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl.getText().toString())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<Void> call = tokenAuth.setVariable(SessionId, name, newValue);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("auth", "Deu BOM");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                    closeSession(SessionId);
                } else {
                    Log.d("auth", "Deu ruim");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    private void closeSession(String SessionId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl.getText().toString())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<Void> call = tokenAuth.closeSession(SessionId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("auth", "Deu BOM");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                } else {
                    Log.d("auth", "Deu ruim");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    public String cleanOUTPUT(String OUTPUT) {
        OUTPUT = OUTPUT.replaceAll("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">","");
        OUTPUT = OUTPUT.replaceAll("</string>", "");
        return OUTPUT;
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

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, LoginActivity.class);
        from.startActivity(intent);
    }


    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}

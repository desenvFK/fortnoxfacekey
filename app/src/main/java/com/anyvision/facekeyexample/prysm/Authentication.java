package com.anyvision.facekeyexample.prysm;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.request.StringRequest;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.activities.logged.MainActivity;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.utils.UiUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Authentication extends Application {
    private String serverLocalUrl;
    private boolean statusServer;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public Authentication(String serverLocalUrl){
        this.serverLocalUrl = serverLocalUrl;
        this.statusServer = false;
        try {
            verifyServerStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServerLocalUrl() {
        return serverLocalUrl;
    }

    public void setServerLocalUrl(String serverLocalUrl) {
        this.serverLocalUrl = serverLocalUrl;
    }

    public void requestToken(final String name, final String newValue){
        final String AccountType = GetVariables.getInstance().getSpTypeAccount();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<ResponseBody> call = tokenAuth.getToken();

        call.enqueue(new Callback<ResponseBody>() {
            private String token;
            private String hashpassword;

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.d("auth", response.toString());
                    token = cleanOUTPUT(response.body().string());
                    Log.d("auth", "SessionID: " + token);
                    hashpassword = md5(token + md5(AccountType + AccountType));
                    Log.d("auth", "hashpassword: " +  hashpassword);

                    if(response.isSuccessful()) {
                        getAuthentication(token, hashpassword, name, newValue);
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

    private void getAuthentication(final String SessionId, String pass, final String name, final String newValue){
        final String AccountType = GetVariables.getInstance().getSpTypeAccount();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Log.d("auth", pass);
        Call<Void> call = tokenAuth.signIn(SessionId, AccountType, pass);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("auth", "Deu BOM");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                    setVariable(SessionId, name, newValue);
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
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<Void> call = tokenAuth.setVariable(SessionId, name, newValue);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showToast(response.code());
                if(response.isSuccessful()){

                    closeSession(SessionId);
                } else {
                    Log.d("auth", "Deu ruim");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                    closeSession(SessionId);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    private void showToast(int code){
        switch(code) {
            case 200:
                Toast.makeText(getContext(), "Mensagem enviada com sucesso!", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "Mensagem não foi enviada. Por favor, verifique o status do servidor!", Toast.LENGTH_LONG).show();
                break;

        }

    }

    private void setVariableWithTempo(final String SessionId, String name, int tempo, String valStart, String valEnd){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<Void> call = tokenAuth.setVariableWithPulse(SessionId, name, tempo, valStart, valEnd);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    closeSession(SessionId);
                } else {
                    Log.d("auth", "Deu ruim");
                    Log.d("auth", String.valueOf(response.code()));
                    Log.d("auth", response.toString());
                    closeSession(SessionId);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("auth", t.getMessage());
            }
        });
    }

    public void verifyServerStatus(){
        setServerLocalUrl(GetVariables.getInstance().getServerUrl());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AuthToken tokenAuth = retrofit.create(AuthToken.class);

        Call<ResponseBody> call = tokenAuth.getServerStatus();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setStatusServer(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setStatusServer(false);
            }
        });

    }

    public void setStatusServer(boolean status){
        this.statusServer = status;
    }

    public boolean getStatusServer(){
        return this.statusServer;
    }
    private void closeSession(final String SessionId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverLocalUrl)
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

    public static Context getContext() {
        return mContext;
    }

    public String cleanOUTPUT(String OUTPUT) {
        OUTPUT = OUTPUT.replaceAll("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">","");
        OUTPUT = OUTPUT.replaceAll("</string>", "");
        return OUTPUT;
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

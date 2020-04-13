package com.anyvision.facekeyexample.firebase;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.activities.RegisterActivity;
import com.anyvision.facekeyexample.activities.logged.MainActivity;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class Firebase {
    private static DatabaseReference mDatabase;
    private static Firebase firebaseInstance;
    private String token;
    private String mac;



    public Firebase(){
        this.mac = InfoMobile.getMacAddress();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static Firebase getInstance(){

        if(firebaseInstance == null){
            firebaseInstance = new Firebase();
        }

        return firebaseInstance;

    }

    public void createUser(String username,
                           String name,
                           String cargo,
                           String agencia){


        String mac = InfoMobile.getMacAddress();
        String key = mac + "/" + username;

        mDatabase.child("Users").child(key)
                .child("username").setValue(username);

        mDatabase.child("Users").child(key)
                .child("name").setValue(name);

        mDatabase.child("Users").child(key)
                .child("macAddress").setValue(mac);

        mDatabase.child("Users").child(key)
                .child("cargo").setValue(cargo);

        mDatabase.child("Users").child(key)
                .child("agencia").setValue(agencia);

        if(cargo.equals("Gerente do Polo")){
            mDatabase.child("Users").child(key)
                    .child("tipo").setValue("AGENCIA");
        } else if(cargo.equals("Gerente Regional")) {
            mDatabase.child("Users").child(key)
                    .child("tipo").setValue("REGIONAL");
        }

        mDatabase.child("Macs").child(mac)
                .child(username).setValue("1");

    }

    public void sendNotification(boolean status, String username){
        String mac = InfoMobile.getMacAddress();
        String key = mac + "/" + username;

        mDatabase.child("Notification").child(key)
                .child("username").setValue(username);

        mDatabase.child("Notification").child(key)
                .child("SolicitacaoExtensao").setValue(status);

    }




    public void sendTokenFirebaseToServer(String token) {
        Log.d("token", token);


        mDatabase.child("Users").child(this.mac)
                .child("tokenNotification").setValue(token);


    }



}

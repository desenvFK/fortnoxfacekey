package com.anyvision.facekeyexample.activities.logged;

import androidx.appcompat.app.AppCompatActivity;
import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.LoginActivity;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.firebase.Notification;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.prysm.Authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Authentication auth;

    private Button btnTimeExtend;
    private Button btnDesarmarAlarmes;
    private Button btnLigarLuzes;
    private Button btnDesligarLuzes;
    private Button btnPanico;

    private String typeAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = new Authentication(GetVariables.getInstance().getServerUrl());

        btnTimeExtend = (Button) findViewById(R.id.btnExtend);
        btnDesarmarAlarmes = (Button) findViewById(R.id.btnDesarmar);
        btnLigarLuzes = (Button) findViewById(R.id.btnLigar);
        btnDesligarLuzes = (Button) findViewById(R.id.btnDesligar);
        btnPanico = (Button) findViewById(R.id.btnPanico);

        typeAccount = GetVariables.getInstance().getSpTypeAccount();


        if(typeAccount.equals("AGENCIA")){
            btnTimeExtend.setVisibility(View.VISIBLE);
            btnDesarmarAlarmes.setVisibility(View.VISIBLE);
            btnLigarLuzes.setVisibility(View.VISIBLE);
            btnDesligarLuzes.setVisibility(View.VISIBLE);
            btnPanico.setVisibility(View.VISIBLE);

            btnTimeExtend.setText("PEDIDO DE EXTENSÃO DE HORÁRIO");
            btnDesarmarAlarmes.setText("DESARMAR ALARMES");


            btnTimeExtend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Extensao", "true");
                    Firebase.getInstance().sendNotification(true, GetVariables.getInstance().getEtUsername());
                    Notification.sendNotification();
                }
            });

            btnDesarmarAlarmes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Desarmar", "true");
                }
            });

            btnLigarLuzes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Luz_agencia", "true");
                }
            });

            btnDesligarLuzes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Luz_agencia", "false");
                }
            });

            btnPanico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Panico", "true");
                }
            });

        } else if(typeAccount.equals("REGIONAL")) {
            btnTimeExtend.setVisibility(View.VISIBLE);
            btnDesarmarAlarmes.setVisibility(View.VISIBLE);

            btnTimeExtend.setText("APROVAR");
            btnDesarmarAlarmes.setText("NÃO APROVAR");

            btnLigarLuzes.setVisibility(View.GONE);
            btnDesligarLuzes.setVisibility(View.GONE);
            btnPanico.setVisibility(View.GONE);

            btnTimeExtend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Reprogramacao", "1");
                }
            });

            btnDesarmarAlarmes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.requestToken("App." + typeAccount +".Reprogramacao", "2");
                }
            });
        }


    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, MainActivity.class);
        from.startActivity(intent);
    }


}

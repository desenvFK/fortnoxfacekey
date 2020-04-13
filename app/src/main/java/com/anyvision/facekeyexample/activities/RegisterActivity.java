package com.anyvision.facekeyexample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anyvision.facekeyexample.R;
import com.anyvision.facekeyexample.activities.logged.MainActivity;
import com.anyvision.facekeyexample.firebase.Firebase;
import com.anyvision.facekeyexample.models.GetVariables;
import com.anyvision.facekeyexample.models.InfoMobile;
import com.anyvision.facekeyexample.models.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etNome;
    private Spinner spCargo;
    private Spinner spAgencia;
    private Button btnNext;
    private Button btnPrevious;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(getApplicationContext());

        etUsername = (EditText) findViewById(R.id.etUsername);
        etNome = (EditText) findViewById(R.id.etName);

        spCargo = (Spinner) findViewById(R.id.spCargo);
        listenerCargo();
        spAgencia = (Spinner) findViewById(R.id.spAgencia);
        listenerAgencia();

        btnNext = (Button) findViewById(R.id.btNext);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = etUsername.getText().toString();
                    String nome = etNome.getText().toString();
                    GetVariables.getInstance().setEtRegisterUsername(username);

                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(nome)){
                        Toast.makeText(RegisterActivity.this, "Por favor, preencha os campos vazios!", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Firebase.getInstance().createUser(etUsername.getText().toString(),
                                etNome.getText().toString(),
                                spCargo.getSelectedItem().toString(),
                                spAgencia.getSelectedItem().toString());


                        InstructionsActivity.startActivity(RegisterActivity.this);
                    };


                }
                catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startActivity(RegisterActivity.this);
            }
        });
    }

    public void listenerCargo(){
        addItemsCargos();
    }

    public void addItemsCargos(){
        List<String> list = new ArrayList<String>();
        list.add("Gerente Regional");
        list.add("Gerente do Polo");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        spCargo.setAdapter(dataAdapter);

    }

    public void listenerAgencia(){
        addItemsAgencias();
    }

    public void addItemsAgencias(){
        List<String> list = new ArrayList<String>();
        list.add("Sicredi POC");
        list.add("Sicredi Porto Alegre");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        spAgencia.setAdapter(dataAdapter);

    }

    public static void startActivity(Context from) {
        Intent intent = new Intent(from, RegisterActivity.class);
        from.startActivity(intent);
    }

}

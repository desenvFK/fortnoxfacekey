package com.anyvision.facekeyexample.models;

import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GetVariables {

    private static String localServerUrl;
    private static GetVariables getVariablesInstance;

    private static TextView etLocalServerUrl;
    private static TextView textviewAnyvision;
    private static String etAnyvisionUrl;
    private static String etUsername;
    private static String etRegisterUsername;
    private static String spTypeAccount;



    public GetVariables(){
        localServerUrl = "";
    }

    public static GetVariables getInstance(){
        if(getVariablesInstance == null){
            getVariablesInstance = new GetVariables();
        }

        return getVariablesInstance;
    }

    public void setServerUrl(String url){
        localServerUrl = url;
    }

    public String getServerUrl(){
        return localServerUrl;
    }

    public TextView getEtLocalServerUrl() {
        return etLocalServerUrl;
    }

    public void setEtLocalServerUrl(TextView etLocalServerUrl) {
        GetVariables.etLocalServerUrl = etLocalServerUrl;
    }

    public String getEtUsername() {
        return etUsername;
    }

    public void setEtUsername(String etUsername) {
        GetVariables.etUsername = etUsername;
    }

    public String getEtRegisterUsername() {
        return etRegisterUsername;
    }

    public void setEtRegisterUsername(String etRegisterUsername) {
        GetVariables.etRegisterUsername = etRegisterUsername;
    }

    public String getSpTypeAccount() {
        return spTypeAccount;
    }

    public void setSpTypeAccount(String spTypeAccount) {
        GetVariables.spTypeAccount = spTypeAccount;
    }

    public String getEtAnyvisionUrl() {
        return etAnyvisionUrl;
    }

    public void setEtAnyvisionUrl(String etAnyvisionUrl) {
        GetVariables.etAnyvisionUrl = etAnyvisionUrl;
    }

    public TextView getTextviewAnyvision() {
        return textviewAnyvision;
    }

    public void setTextviewAnyvision(TextView textviewAnyvision) {
        GetVariables.textviewAnyvision = textviewAnyvision;
    }
}
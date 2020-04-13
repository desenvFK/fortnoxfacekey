package com.anyvision.facekeyexample.models;

public class LocalServer {

    private String localServerUrl;

    public LocalServer(String localServerUrl){

        this.localServerUrl = localServerUrl;

    }

    public void setLocalUrl(String localServerUrl){
        this.localServerUrl = localServerUrl;
    }

    public String getLocalUrl(){
        return this.localServerUrl;
    }



}

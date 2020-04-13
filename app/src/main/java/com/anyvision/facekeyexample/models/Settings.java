package com.anyvision.facekeyexample.models;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by igal on 30/11/2017.
 */

public class Settings {

    private float threshold;
    private float environmentThreshold;
    private float artifactThreshold;
    private int videoTime;
    private int imageCompressionRate;
    private boolean doDelete;
    private int shakeThreshold;
    private String baseUrl;
    public String localUrl;

    public Settings() {
        threshold = 0.3f;
        videoTime = 3;
        shakeThreshold = 10;
        imageCompressionRate = 60;
        doDelete = true;
        environmentThreshold = 1f;
        artifactThreshold = 1f;
        baseUrl = "http://sesame-liveness-poc-long.anyvision.co:3000";
//        baseUrl = "http://livenessdev.anyvision.co:3000";
       // localUrl = "http://192.168.0.224";
        localUrl = "https://192.168.5.233";
    }

    public void setLocalUrl(String localUrl){
        this.localUrl = localUrl;
    }

    public String getLocalUrl(){
        return this.localUrl;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public int getImageCompressionRate() {
        return imageCompressionRate;
    }

    public void setImageCompressionRate(int imageCompressionRate) {
        this.imageCompressionRate = imageCompressionRate;
    }

    public boolean isDoDelete() {
        return doDelete;
    }

    public void setDoDelete(boolean doDelete) {
        this.doDelete = doDelete;
    }

    public float getEnvironmentThreshold() {
        return environmentThreshold;
    }

    public void setEnvironmentThreshold(float environmentThreshold) {
        this.environmentThreshold = environmentThreshold;
    }

    public float getArtifactThreshold() {
        return artifactThreshold;
    }

    public void setArtifactThreshold(float artifactThreshold) {
        this.artifactThreshold = artifactThreshold;
    }

    public int getShakeThreshold() {
        return shakeThreshold;
    }

    public void setShakeThreshold(int shakeThreshold) {
        this.shakeThreshold = shakeThreshold;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }



}

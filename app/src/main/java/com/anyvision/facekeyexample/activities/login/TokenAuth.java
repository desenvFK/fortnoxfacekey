package com.anyvision.facekeyexample.activities.login;

import com.google.gson.annotations.SerializedName;



public class TokenAuth {
    @SerializedName("string")
    private String token;

    public String getToken() {
        return token;
    }
}

package com.anyvision.facekeyexample.prysm;

import com.anyvision.facekeyexample.activities.login.TokenAuth;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AuthToken {
    @GET("AppVisionService.svc/Open?clientProductDescription=AppMobile1&clientProductName=AppMobile&clientProductCompany=Prysm&clientProcessName=AppMobile&clientProcessVersion=1.0&clientHostName=local")
    Call<ResponseBody> getToken();

    @Headers({ "Accept: application/xml"})
    @GET("AppVisionService.svc/Login")
    Call<Void> signIn(@Header("SessionID") String SessionId, @Query("username") String username, @Query("hashpassword") String hashpassword);

    @Headers({ "Accept: application/xml"})
    @GET("AppVisionService.svc/SetVariable?changeOnly=false&severity=-1&quality=-1")
    Call<Void> setVariable(@Header("SessionID") String SessionId, @Query("name") String name, @Query("newValue") String newValue);

    @Headers({ "Accept: application/xml"})
    @GET("AppVisionService.svc/SetVariable?changeOnly=false&severity=-1&quality=-1")
    Call<Void> setVariableWithPulse(@Header("SessionID") String SessionId, @Query("name") String name, @Query("tempo") int tempo, @Query("valStart") String valStart, @Query("valEnd") String valEnd);

    @Headers({ "Accept: application/xml"})
    @GET("AppVisionService.svc/Close")
    Call<Void> closeSession(@Header("SessionID") String SessionId);

    @GET("AppVisionService.svc/GetServerState")
    Call<ResponseBody> getServerStatus();
}


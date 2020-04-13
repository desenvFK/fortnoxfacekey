package com.anyvision.facekeyexample.firebase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PushNotificationInterface {

    @GET("AppVisionService.svc/Open?clientProductDescription=AppMobile1&clientProductName=AppMobile&clientProductCompany=Prysm&clientProcessName=AppMobile&clientProcessVersion=1.0&clientHostName=local")
    Call<ResponseBody> getToken();
}

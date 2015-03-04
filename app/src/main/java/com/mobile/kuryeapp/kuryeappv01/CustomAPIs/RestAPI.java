package com.mobile.kuryeapp.kuryeappv01.CustomAPIs;

import com.mobile.kuryeapp.kuryeappv01.Classes.UserGot;
import com.mobile.kuryeapp.kuryeappv01.Classes.UserSent;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface RestAPI {
    @POST("/authorize")
    void getUserModel(@Body UserSent userSent,
                                    Callback<UserGot> response);
}

// login olduktan sonra @HEADER Key: Authorization Value: Bearer access_token

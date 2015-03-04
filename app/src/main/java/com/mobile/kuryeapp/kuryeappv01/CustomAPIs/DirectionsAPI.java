package com.mobile.kuryeapp.kuryeappv01.CustomAPIs;

import org.json.JSONObject;

import retrofit.http.GET;
import retrofit.http.Path;

public interface DirectionsAPI {
    @GET("/maps/api/directions/json?origin={myLat},{myLng}&destination={x},{y}")
    //TODO change object type to make it work
    JSONObject getResponse (@Path("myLat") Double myLat,
                         @Path("myLng") Double myLng,
                         @Path("x") Double x,
                         @Path("y") Double y);
}

package com.mobile.kuryeapp.kuryeappv01.api;

import com.mobile.kuryeapp.kuryeappv01.Classes.UserGot;
import com.mobile.kuryeapp.kuryeappv01.Classes.UserSent;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public class ApiClient {

    public static final String ENDPOINT = "https://cryptic-ridge-2951.herokuapp.com";
    public static final String ENDPOINT_DIRECTIONS = "https://maps.googleapis.com";

    private static LoginInterface sLoginService;
    private static DirectionsInterface sDirectionsService;


    public interface LoginInterface {
        // login olduktan sonra @HEADER Key: Authorization Value: Bearer access_token
        @POST("/authorize")
        void getUserModel(@Body UserSent userSent,
                          Callback<UserGot> response);
    }

    public static LoginInterface getLoginApiClient(){
        if (sLoginService == null){
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .build();
            sLoginService = adapter.create(LoginInterface.class);
        }
        return sLoginService;
    }


    public interface DirectionsInterface {
        @GET("/maps/api/directions/json")
            //TODO change object type to make it work
        public void getDirections(@Query("origin") String origin,
                           @Query("destination") String destination,
                           @Query("sensor") boolean _isFromSensor,
                           //@Query("key") String key,
                           Callback<Directions> response);
    }
    public static DirectionsInterface getDirectionsApiClient(){
        if (sDirectionsService == null){
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT_DIRECTIONS)
                    .build();
            sDirectionsService = adapter.create(DirectionsInterface.class);
        }
        return sDirectionsService;
    }
}

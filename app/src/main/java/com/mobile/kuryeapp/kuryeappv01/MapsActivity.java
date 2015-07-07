package com.mobile.kuryeapp.kuryeappv01;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.collect.Iterables;
import com.google.maps.android.PolyUtil;
import com.mobile.kuryeapp.kuryeappv01.api.ApiClient;
import com.mobile.kuryeapp.kuryeappv01.api.Directions;
import com.mobile.kuryeapp.kuryeappv01.api.Leg;
import com.mobile.kuryeapp.kuryeappv01.api.MyPolyline;
import com.mobile.kuryeapp.kuryeappv01.api.Steps;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double x, y;
    private String addr, name;
    private double myLat, myLng;
    private LatLng destCoord, myLatLng = new LatLng(0,0);
    private LocationManager locMan;
    private Socket mSocket;
    private List<Leg> mLegs;
    private Iterable<LatLng> decodedPoly;
    private Directions mDir;

    {
        try {
            mSocket = IO.socket(ApiClient.ENDPOINT);
            Log.d("ENDPOINT: ", ApiClient.ENDPOINT);
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        x = this.getIntent().getDoubleExtra("x",0);
        y = this.getIntent().getDoubleExtra("y",0);
        addr = this.getIntent().getStringExtra("description");
        name = this.getIntent().getStringExtra("name");
        destCoord = new LatLng(x,y);

        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        CameraPosition cameraPosition1 = new CameraPosition.Builder().target(destCoord).zoom(14).build();
        mMap.addMarker(new MarkerOptions().position(destCoord).title(name).snippet(addr)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));
        mMap.setMyLocationEnabled(true);

        ApiClient.getDirectionsApiClient().getDirections("Bursa",
                "Istanbul",true, new Callback<Directions>(){

            @Override
            public void success(Directions directions, Response response) {
                mDir = directions;
                mLegs= mDir.getRoutes().get(0).getLegs();
                List<Steps> steps = mLegs.get(0).getSteps();
                decodedPoly = PolyUtil.decode(steps.get(0).getMyPolyline().getPoints());
                for(int j=1,y=steps.size();j<y;j++){
                        MyPolyline myPolyline = steps.get(j).getMyPolyline();
                        Iterable<LatLng> tempDecodedPoly = PolyUtil.decode(myPolyline.getPoints());
                        decodedPoly = Iterables.concat(decodedPoly,tempDecodedPoly);
                    }
                mMap.addPolyline(new PolylineOptions().addAll(decodedPoly).width(5).color(Color.RED));
                //Log.d("DURATION:",directions.getRoutes().get(0).getLegs().get(0).getDuration().getValue()+"");
                //Log.d("DURATION:",directions.getRoutes().get(0).getLegs().get(0).getDuration().getText());

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("failure directions","");
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
        myLatLng = new LatLng(myLat,myLng);
        CameraPosition cameraPosition2 = new CameraPosition.Builder().target(myLatLng).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2), 4000, null);

        //TODO remove old polyline and add new one

        JSONObject coordJson = new JSONObject();
        try {
            coordJson.put("lat",myLat);
            coordJson.put("lng",myLng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketIOmethods.sendCoords(mSocket,coordJson);
        Log.d("onLOCATIONCHANGED","asfdfads");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

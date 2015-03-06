package com.mobile.kuryeapp.kuryeappv01;


import android.content.Context;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double x, y;
    private String addr;
    private double myLat, myLng;
    private LatLng destCoord, myLatLng = new LatLng(0,0);
    private LocationManager locMan;
    private Marker userMarker;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(LoginActivity.ENDPOINT);
            Log.d("ENDPOINT: ", LoginActivity.ENDPOINT);
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        x = this.getIntent().getDoubleExtra("x",0);
        y = this.getIntent().getDoubleExtra("y",0);
        addr = this.getIntent().getStringExtra("address");
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
        mMap.addMarker(new MarkerOptions().position(destCoord).title("Kahve Diyari").snippet(addr)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));
        mMap.setMyLocationEnabled(true);

//        userMarker = mMap.addMarker(new MarkerOptions()
//                .position(myLatLng)
//                .title("MY LOCATION")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redusericon)));
    }

    @Override
    public void onLocationChanged(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
        myLatLng = new LatLng(myLat,myLng);
        //userMarker.setPosition(myLatLng);
        CameraPosition cameraPosition2 = new CameraPosition.Builder().target(myLatLng).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2), 4000, null);
        JSONObject coordJson = new JSONObject();
        try {
            coordJson.put("lat",myLat);
            coordJson.put("lng",myLng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketIOmethods.sendCoords(mSocket,coordJson);
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

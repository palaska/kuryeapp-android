package com.mobile.kuryeapp.kuryeappv01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import kuryeapp.tabsswipe.adapter.TabsPagerAdapter;


public class AddressPaymentActivity extends ActionBarActivity implements LocationListener {

    public static FragmentManager fragmentManager;
    public String access_token,username;
    SharedPreferences mSharedPrefs;

    private double myLat, myLng;
    private LocationManager locMan;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(LoginActivity.ENDPOINT);
            Log.d("ENDPOINT: ",LoginActivity.ENDPOINT);
        } catch (URISyntaxException e) {}
    }

    Activity that = this;

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            that.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String world;
                    try {
                        world = data.getString("hello");
                    } catch (JSONException e) {
                        return;
                    }
                    Toast.makeText(getApplicationContext(), world, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresspayment);
        getSupportActionBar().hide();

        mSocket.on("new message", onNewMessage);
        mSocket.connect();

        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
//        Location lastLoc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        myLat = lastLoc.getLatitude();
//        myLng = lastLoc.getLongitude();
        //startLocation();

        fragmentManager = getSupportFragmentManager();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TabsPagerAdapter(fragmentManager));
        pager.setCurrentItem(1);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        access_token = this.getIntent().getStringExtra("access_token");
        username = this.getIntent().getStringExtra("username");

        mSharedPrefs = getSharedPreferences("xmlFile", MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("saved_access_token",access_token);
        mPrefsEditor.putString("saved_username",username);
        Log.d("MY SAVED USERNAME", username);
        mPrefsEditor.apply();
        //commit()

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //SmartLocation.with(this).location().stop();
        mSocket.disconnect();

    }

    @Override
    protected void onStart() {
        super.onStart();
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), myLat+" // "+myLng, Toast.LENGTH_LONG).show();
        //startLocation();
        //SocketIOmethods.sendMessage(mSocket,"Lat: "+myLat+" / Lng: "+myLng);
    }

    @Override
    public void onLocationChanged(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
        Toast.makeText(getApplicationContext(), myLat+" // "+myLng, Toast.LENGTH_LONG).show();
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


    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
                Intent i = new Intent(getApplicationContext(),AddressPaymentActivity.class);
                startActivity(i);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");
                isPhoneCalling = true;
                Intent i = new Intent(getApplicationContext(),AddressPaymentActivity.class);
                startActivity(i);
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                      //Log.i(LOG_TAG, "restart app");
//                    // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }
}
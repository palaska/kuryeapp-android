package com.mobile.kuryeapp.kuryeappv01;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mobile.kuryeapp.kuryeappv01.Classes.CountdownChronometer;
import com.mobile.kuryeapp.kuryeappv01.api.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import kuryeapp.tabsswipe.adapter.TabsPagerAdapter;


public class AddressPaymentActivity extends ActionBarActivity implements LocationListener {

    TextView jobtype, neighborhood;
    BootstrapEditText addressedittext, addressdesc;
    public static JSONObject data;
    public static FragmentManager fragmentManager;
    public String access_token,username,jobstr;
    CountdownChronometer countdown;
    SharedPreferences mSharedPrefs;

    private double myLat, myLng;
    private LocationManager locMan;


    public Socket mSocket;
    {
        try {
            mSocket = IO.socket(ApiClient.ENDPOINT);
        } catch (URISyntaxException e) {}
    }

    Activity that = this;

    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            that.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    data = (JSONObject) args[0];
                    try {
                        Log.d("TYPE: ",data.getString("type"));
                        jobstr = data.getString("type");
                        if(jobstr != null) {
                            SuperToast superToast = new SuperToast(getApplicationContext(),
                            Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN));
                            superToast.setDuration(SuperToast.Duration.EXTRA_LONG);
                            superToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
                            Log.d("dataToString",data.toString());
                            if(jobstr.equals("pickup")){
                                superToast.setText("Yeni 'Paket Alma' görevi!");
                                ((TextView)findViewById(R.id.jobtype)).setText("Paket Alma");
                            } else {
                                ((TextView)findViewById(R.id.jobtype)).setText("Sipariş Teslimi");
                                superToast.setText("Yeni 'Sipariş Teslimi' görevi!");
                            }
                            superToast.show();
                            JSONObject dest = data.getJSONObject("destination");
                            ((BootstrapEditText)findViewById(R.id.addressedittext)).setText(dest.getString("description"));
                            ((BootstrapEditText)findViewById(R.id.addressdesc)).setText(dest.getString("depiction"));
                            ((TextView)findViewById(R.id.neighborhood)).setText(dest.getString("neighborhood"));
                            ((CountdownChronometer)findViewById(R.id.chronometer)).setBase(System.currentTimeMillis() + data.getInt("due"));
                            ((CountdownChronometer)findViewById(R.id.chronometer)).start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public static JSONObject getData() {
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresspayment);
        getSupportActionBar().hide();

        jobtype = (TextView) findViewById(R.id.jobtype);
        addressedittext = (BootstrapEditText) findViewById(R.id.addressedittext);
        addressdesc = (BootstrapEditText) findViewById(R.id.addressdesc);
        neighborhood = (TextView) findViewById(R.id.neighborhood);

        countdown = (CountdownChronometer) findViewById(R.id.chronometer);

        mSocket.on("new message", onNewMessage);
        mSocket.connect();

        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

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
        mPrefsEditor.apply();
        //mPrefsEditor.commit();

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSocket.disconnect();

    }

    @Override
    protected void onStart() {
        super.onStart();
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), myLat+" // "+myLng, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
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
                //Intent i = new Intent(getApplicationContext(),AddressPaymentActivity.class);
                //startActivity(i);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");
                isPhoneCalling = true;
                //Intent i = new Intent(getApplicationContext(),AddressPaymentActivity.class);
                //startActivity(i);
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
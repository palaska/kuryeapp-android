package com.mobile.kuryeapp.kuryeappv01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

import kuryeapp.tabsswipe.adapter.TabsPagerAdapter;


public class AddressPaymentActivity extends ActionBarActivity  {

    public static FragmentManager fragmentManager;
    public String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresspayment);
        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TabsPagerAdapter(fragmentManager));
        pager.setCurrentItem(1);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        access_token = this.getIntent().getStringExtra("access_token");
        SharedPreferences mSharedPrefs = getSharedPreferences("xmlFile", MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("saved_access_token",access_token);
        mPrefsEditor.commit();

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
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
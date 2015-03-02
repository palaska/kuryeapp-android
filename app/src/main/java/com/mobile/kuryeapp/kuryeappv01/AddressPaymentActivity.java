package com.mobile.kuryeapp.kuryeappv01;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.astuetz.PagerSlidingTabStrip;

import kuryeapp.tabsswipe.adapter.TabsPagerAdapter;


public class AddressPaymentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresspayment);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

}
package com.melons.melon.guitarguide;

import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class GuitarPartsActivity extends AppCompatActivity {

    //necessities
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar_parts);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onPause() {
        super.onPause();
        connectivityReceiver.i = Integer.valueOf(0);
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
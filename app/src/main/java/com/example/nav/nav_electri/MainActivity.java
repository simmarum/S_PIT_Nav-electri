package com.example.nav.nav_electri;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1Ijoic2ltbWFydW0iLCJhIjoiY2pvazQ2aTc3MGI1bzNwbzFtbDZvOGJqZyJ9.o5xHndqBvRXIVWQ_9yHiAQ");
        Mapbox.getTelemetry().setUserTelemetryRequestState(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        View tab_navigation = getLayoutInflater().inflate(R.layout.my_main_tab, null);
        tab_navigation.findViewById(R.id.icon).setBackgroundResource(R.drawable.navigation_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tab_navigation));

        View tab_stations = getLayoutInflater().inflate(R.layout.my_main_tab, null);
        tab_stations.findViewById(R.id.icon).setBackgroundResource(R.drawable.stations_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tab_stations));

        View tab_attractions = getLayoutInflater().inflate(R.layout.my_main_tab, null);
        tab_attractions.findViewById(R.id.icon).setBackgroundResource(R.drawable.attractions_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tab_attractions));

        View tab_settings = getLayoutInflater().inflate(R.layout.my_main_tab, null);
        tab_settings.findViewById(R.id.icon).setBackgroundResource(R.drawable.settings_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tab_settings));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
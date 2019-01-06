package com.example.nav.nav_electri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabSettings extends Fragment {
    private View fragmentLayout;
    private Button my_connect_button;
    private TextView my_connect_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentLayout = inflater.inflate(R.layout.settings, container, false);
        my_connect_button = fragmentLayout.findViewById(R.id.my_connect_button);
        my_connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectButtonClick(v);
            }
        });

        my_connect_text = fragmentLayout.findViewById(R.id.my_connect_button_text);

        return fragmentLayout;
    }


    public void onConnectButtonClick(View v){
        if (Car.isConnected) {
            my_connect_button.setText(R.string.disconnect);
            my_connect_text.setText(R.string.connect_text);
        } else {
            my_connect_button.setText(R.string.connect);
            my_connect_text.setText(R.string.disconnect_text);
        }
        Car.isConnected = !Car.isConnected;

    }
}

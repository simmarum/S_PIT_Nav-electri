package com.example.nav.nav_electri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TabSettings extends Fragment {
    private View fragmentLayout;
    private Button my_connect_button;
    private TextView my_connect_text;
    private EditText my_edit_battery_value;

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

        my_edit_battery_value = fragmentLayout.findViewById(R.id.my_setting_battery_input);
        my_edit_battery_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    Integer x = Integer.parseInt(s.toString());
                    if (x > 100){
                        my_edit_battery_value.setText("100");
                        Car.min_battery_percent = 100;
                    } else {
                        Car.min_battery_percent = x;
                    }
                } else {
                    my_edit_battery_value.setText("0");
                    Car.min_battery_percent = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return fragmentLayout;
    }


    public void onConnectButtonClick(View v){
        if (Car.isConnected) {
            my_connect_button.setText(R.string.connect);
            my_connect_text.setText(R.string.disconnect_text);
            Car.isConnected = false;

        } else {
            my_connect_button.setText(R.string.disconnect);
            my_connect_text.setText(R.string.connect_text);
            Car.isConnected = true;
        }

    }
}

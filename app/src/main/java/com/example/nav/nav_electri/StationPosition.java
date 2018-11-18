package com.example.nav.nav_electri;

import android.content.Context;

public class StationPosition extends Position {
    public Integer empty_place;
    public String telephone;

    public StationPosition(Context context, double lat, double lon, String title, String snip, Integer icon, Integer empty_place, String telephone) {
        super(context, lat, lon, title, snip, icon);
        setEmpty_place(empty_place);
        setTelephone(telephone);

    }

    public String getStationInfo() {
        String empty = this.empty_place.toString();
        if (this.empty_place == -1) {
            empty = getContext().getResources().getString(R.string.no_empty_place);
        }
        return getContext().getResources().getString(R.string.time_to_charge_holder) + " " + this.snip + "\n" +
                getContext().getResources().getString(R.string.empty_place_holder) + " " + empty + "\n" +
                getContext().getResources().getString(R.string.phone_number_holder) + " " + this.telephone;
    }

    public Integer getEmpty_place() {
        return empty_place;
    }

    public void setEmpty_place(Integer empty_place) {
        if (empty_place == null) {
            this.empty_place = -1;
        } else {
            this.empty_place = empty_place;
        }
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null) {
            this.telephone = getContext().getResources().getString(R.string.no_number_phone);
        } else {
            this.telephone = telephone;
        }
    }
}

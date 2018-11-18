package com.example.nav.nav_electri;

import android.content.Context;

public class Position {
    public double lat;
    public double lon;
    public String title;
    public String snip;
    public Integer icon;
    public Integer empty_place;
    public String telephone;
    public Context context;


    public Position(Context context, double lat, double lon, String title, String snip, Integer icon, Integer empty_place, String telephone) {
        setContext(context);
        setLat(lat);
        setLon(lon);
        setTitle(title);
        setSnip(snip);
        setIcon(icon);
        setEmpty_place(empty_place);
        setTelephone(telephone);

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        if (icon == null) {
            this.icon = R.drawable.pin_orange;
        } else {
            this.icon = icon;
        }
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getSnip() {
        return snip;
    }

    public void setSnip(String snip) {
        if (snip == null) {
            this.snip = "";
        } else {
            this.snip = snip;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            this.title = "";
        } else {
            this.title = title;
        }
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Lat: " + lat + " Lon: " + lon + " Title: " + title + " Snippest: " + snip + " Icon: " + icon;
    }
}

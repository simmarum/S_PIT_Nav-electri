package com.example.nav.nav_electri;

import android.content.Context;

public class AttractionPosition extends Position {
    public String today_open_hours;
    public String telephone;
    public double distance;

    public AttractionPosition(Context context, double lat, double lon, String title, String snip, Integer icon,Boolean sponsor, String today_open_hours, String telephone,double distance) {
        super(context, lat, lon, title, snip, icon,sponsor);
        setToday_open_hours(today_open_hours);
        setTelephone(telephone);
        setDistance(distance);

    }


    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getStationInfo() {
        return this.snip + "\n" +
                getContext().getResources().getString(R.string.no_open_hours_holder) + " " + this.today_open_hours + "\n" +
                getContext().getResources().getString(R.string.phone_number_holder) + " " + this.telephone;
    }

    public String getToday_open_hours() {
        return today_open_hours;
    }

    public void setToday_open_hours(String today_open_hours) {
        if (today_open_hours == null) {
            this.today_open_hours = getContext().getResources().getString(R.string.no_open_hours);
        } else {
            this.today_open_hours = today_open_hours;
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

    @Override
    public String toString() {
        return super.toString() + " Open hours: "+today_open_hours+" phone: "+telephone;
    }
}

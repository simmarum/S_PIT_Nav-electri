package com.example.nav.nav_electri;

import android.graphics.drawable.Icon;

import com.mapbox.mapboxsdk.annotations.IconFactory;

public class Position {
    public double lat;
    public double lon;
    public String title;
    public String snip;
    public Integer icon;


    public Position(double lat,double lon, String title,String snip,Integer icon) {
        setLat(lat);
        setLon(lon);
        setTitle(title);
        setSnip(snip);
        setIcon(icon);
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        if(icon == null){
            this.icon = R.drawable.pin_orange;
        } else {
            this.icon = icon;
        }
    }

    public double getLon() {
        return lon;
    }

    public String getSnip() {
        return snip;
    }

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setSnip(String snip) {
        if(snip == null){
            this.snip = "";
        } else {
            this.snip = snip;
        }
    }

    public void setTitle(String title) {
        if (title == null){
            this.title = "";
        } else {
            this.title = title;
        }
    }

    @Override
    public String toString() {
        return "Lat: "+lat+" Lon: "+lon+" Title: "+title+" Snippest: "+snip+" Icon: "+icon;
    }
}

package com.example.nav.nav_electri;

import android.content.Context;

public class Position {
    public double lat;
    public double lon;
    public String title;
    public String snip;
    public Integer icon;
    public Boolean sponsor;
    public Boolean code;

    public Context context;


    public Position(Context context, double lat, double lon, String title, String snip, Integer icon,Boolean sponsor,Boolean code) {
        setContext(context);
        setLat(lat);
        setLon(lon);
        setTitle(title);
        setSnip(snip);
        setIcon(icon);
        setSponsor(sponsor);
        setCode(code);
    }


    public Boolean getCode() {
        return code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Boolean getSponsor() {
        return sponsor;
    }

    public void setSponsor(Boolean sponsor) {
        this.sponsor = sponsor;
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

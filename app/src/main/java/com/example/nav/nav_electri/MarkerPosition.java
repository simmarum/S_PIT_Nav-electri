package com.example.nav.nav_electri;

import android.content.Context;

import java.util.ArrayList;

public class MarkerPosition {
    private Context context;
    private ArrayList<Position> stationList;
    private ArrayList<Position> attractionList = new ArrayList<Position>() {{
    }};

    public MarkerPosition(Context context) {
        this.context = context;
        this.setStationList();
    }

    public void setStationList() {
        this.stationList = new ArrayList<Position>();
        this.stationList.add(new Position(this.context, 52.406374, 16.9251681, "Stacja 001", "1 godzina 20 minut (70%)", R.drawable.pin_green, 3, "123123123"));
        this.stationList.add(new Position(this.context, 52.391207, 16.955803, "Stacja 002", "1 godzina (70%)", R.drawable.pin_green, null, "123123124"));
        this.stationList.add(new Position(this.context, 52.405053, 16.897547, "Stacja 003", "1 godzina 50 minut (80%)", R.drawable.pin_green, 5, null));
        this.stationList.add(new Position(this.context, 52.399188, 16.905309, "Stacja 004", "40 minut (65%)", R.drawable.pin_red, 0, "123123125"));
        this.stationList.add(new Position(this.context, 52.383093, 16.990479, "Stacja 005", "1 godzina (90%)", R.drawable.pin_red, 3, "123123126"));
        this.stationList.add(new Position(this.context, 52.410433, 16.989368, "Stacja 006", "2 godzina (50%)", R.drawable.pin_green, 2, "123123127"));
        this.stationList.add(new Position(this.context, 52.422791, 16.925932, "Stacja 007", "1 godzina (90%)", R.drawable.pin_red, 9, null));
        this.stationList.add(new Position(this.context, 52.364353, 16.927768, "Stacja 008", "1 godzina 10 minut (90%)", R.drawable.pin_red, 1, "123123128"));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Position> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList(ArrayList<Position> attractionList) {
        this.attractionList = attractionList;
    }

    public ArrayList<Position> getStationList() {
        return stationList;
    }

}

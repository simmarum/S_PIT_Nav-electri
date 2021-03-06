package com.example.nav.nav_electri;

import android.content.Context;

import java.util.ArrayList;

public class MarkerPosition {
    private Context context;
    private ArrayList<StationPosition> stationList;
    private ArrayList<AttractionPosition> attractionList;

    public MarkerPosition(Context context) {
        this.context = context;
        this.setStationList();
        this.setAttractionList();
    }

    public void setStationList() {
        this.stationList = new ArrayList<StationPosition>();
        this.stationList.add(new StationPosition(this.context, 52.406374, 16.9251681, "Stacja 001", "1 godzina 20 minut (70%)", R.drawable.pin_green,false,false, 3, "123123123"));
        this.stationList.add(new StationPosition(this.context, 52.391207, 16.955803, "Stacja 002", "1 godzina (70%)", R.drawable.pin_green,false,false, null, "123123124"));
        this.stationList.add(new StationPosition(this.context, 52.405053, 16.897547, "Stacja 003", "1 godzina 50 minut (80%)", R.drawable.pin_green,false,false, 5, null));
        this.stationList.add(new StationPosition(this.context, 52.399188, 16.905309, "Stacja 004", "40 minut (65%)", R.drawable.pin_red,false,false, 0, "123123125"));
        this.stationList.add(new StationPosition(this.context, 52.383093, 16.990479, "Stacja 005", "1 godzina (90%)", R.drawable.pin_red,false,false, 3, "123123126"));
        this.stationList.add(new StationPosition(this.context, 52.410433, 16.989368, "Stacja 006", "2 godzina (50%)", R.drawable.pin_green,false,false, 2, "123123127"));
        this.stationList.add(new StationPosition(this.context, 52.422791, 16.925932, "Stacja 007", "1 godzina (90%)", R.drawable.pin_red,false,false, 9, null));
        this.stationList.add(new StationPosition(this.context, 52.364353, 16.927768, "Stacja 008", "1 godzina 10 minut (90%)", R.drawable.pin_red,false,false, 1, "123123128"));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<AttractionPosition> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList() {
        this.attractionList = new ArrayList<AttractionPosition>();
        this.attractionList.add(new AttractionPosition(this.context, 52.401744, 16.958003, "Galeria Malta", "Multikino, Sphinx...", R.drawable.pin_orange,false,false, "10.00 - 22.00", "999999999",-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.397705, 16.954559, "Galeria Posnania", "Łyżwy, Helios, Leroy Merlin...", R.drawable.pin_orange,false,false, "10.00 - 22.00", "999999998",-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.408483, 16.906295, "Stare Zoo", "Małpy, Róże, Plac zabaw...", R.drawable.pin_violet,false,false, "9.00 - 20.00", "999999997",-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.408070, 16.933467, "Ratusz w Poznaniu", "Stary Rynek, Koziołki...", R.drawable.pin_violet,true,false, "24/7", null,-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.419546, 16.829309, "Lotnisko Ławica", "Samoloty...", R.drawable.pin_violet,false,false, "24/7", "999999996",-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.418070, 16.923467, "Kuchnia Ruska", "Pierogis", R.drawable.pin_orange,true,true, "24/7", null,-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.400070, 16.953467, "Mac Donald", "Big Burges", R.drawable.pin_violet,true,false, "24/7", null,-1));
        this.attractionList.add(new AttractionPosition(this.context, 52.398070, 16.903467, "Mała chatka", "Śniadania jak u mamy", R.drawable.pin_violet,true,true, "24/7", null,-1));

    }

    public ArrayList<StationPosition> getStationList() {
        return stationList;
    }

}

package com.example.nav.nav_electri;

import java.util.ArrayList;

public class MarkerPosition {

    private static final ArrayList<Position> stationList = new ArrayList<Position>() {{
        add(new Position(52.406374, 16.9251681, "Poznań", "Wielkopolska", null));
        add(new Position(52.391207, 16.955803, "Stacja Rataje", "Poznań", R.drawable.pin_red));
    }};
    private static final ArrayList<Position> attractionList = new ArrayList<Position>() {{
    }};

    public static ArrayList<Position> getAttractionList() {
        return attractionList;
    }

    public static ArrayList<Position> getStationList() {
        return stationList;
    }

}

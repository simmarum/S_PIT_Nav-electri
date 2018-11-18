package com.example.nav.nav_electri;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;

public class TabStations extends Fragment {
    View fragmentLayout;
    MarkerPosition markerPosition;
    private MapView miniMap;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.stations, container, false);
        markerPosition = new MarkerPosition(getContext());

        miniMap = (MapView) fragmentLayout.findViewById(R.id.mapView);
        miniMap.onCreate(savedInstanceState);

        miniMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                TabStations.this.mapboxMap = mapboxMap;

                enableLocationComponent(TabStations.this.mapboxMap);
//                // One way to add a marker view
                for (Position station : markerPosition.getStationList()) {
                    TabStations.this.mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(station.lat, station.lon))
                            .title(station.title)
                            .snippet(station.getStationInfo())
                            .icon(IconFactory.getInstance(getContext()).fromResource(station.icon))
                    );
                }
            }

        });
        return fragmentLayout;
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(MapboxMap mapboxMap) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(getContext());
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            originLocation = locationComponent.getLastKnownLocation();

        } else {
            permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                    Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    if (granted) {
                        enableLocationComponent(mapboxMap);
                    } else {
                        Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
            });
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onStart() {
        super.onStart();
        miniMap.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        miniMap.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        miniMap.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        miniMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        miniMap.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        miniMap.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        miniMap.onSaveInstanceState(outState);
    }
}

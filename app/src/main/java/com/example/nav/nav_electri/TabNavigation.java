package com.example.nav.nav_electri;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TabNavigation extends Fragment {
    private static final String TAG = "DirectionsActivity";
    private View fragmentLayout;
    private MapView miniMap;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private Button button;
    private MarkerPosition markerPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.navigation, container, false);
        markerPosition = new MarkerPosition(getContext());

        miniMap = (MapView) fragmentLayout.findViewById(R.id.mapView);
        miniMap.onCreate(savedInstanceState);

        miniMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                TabNavigation.this.mapboxMap = mapboxMap;
                enableLocationComponent(TabNavigation.this.mapboxMap);


//                // One way to add a marker view
                for (Position station : markerPosition.getStationList()) {
                    TabNavigation.this.mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(station.lat, station.lon))
                            .title(station.title)
                            .snippet(station.snip)
                            .icon(IconFactory.getInstance(getContext()).fromResource(station.icon))
                    );
                }
                for (AttractionPosition attraction : markerPosition.getAttractionList()) {
                    TabNavigation.this.mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(attraction.lat, attraction.lon))
                            .title(attraction.title)
                            .snippet(attraction.getStationInfo())
                            .icon(IconFactory.getInstance(getContext()).fromResource(attraction.icon))
                    );
                }

                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
                TabNavigation.this.mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        if (destinationMarker != null) {
                            TabNavigation.this.mapboxMap.removeMarker(destinationMarker);
                        }
                        destinationCoord = point;
                        destinationMarker = TabNavigation.this.mapboxMap.addMarker(new MarkerOptions()
                                .position(destinationCoord)
                                .icon(IconFactory.getInstance(getContext()).fromResource(R.drawable.pin_blue))
                        );
                        destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                        originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                        getRoute(originPosition, destinationPosition);
                    }
                });
            }

        });

        button = fragmentLayout.findViewById(R.id.startButton);
        button.setOnClickListener(v -> {
            boolean simulateRoute = true;
            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build();
            // Call this method with Context from within an Activity
            NavigationLauncher.startNavigation(getActivity(), options);
        });
        return fragmentLayout;
    }

    private Point getNearestStation(Point origin, Point destination) {
        int station_index = 0;
        double min_dist = Double.POSITIVE_INFINITY;
        LatLng origin_pos = new LatLng(origin.longitude(), origin.latitude());
        LatLng dest_pos = new LatLng(destination.longitude(), destination.latitude());
        ArrayList<StationPosition> all_stations = markerPosition.getStationList();
        for (int i = 0; i < all_stations.size(); i++) {
            LatLng tmp_station = new LatLng(all_stations.get(i).getLon(), all_stations.get(i).getLat());
            double tmp_dist = origin_pos.distanceTo(tmp_station) + tmp_station.distanceTo(dest_pos);

            if (min_dist > tmp_dist) {
                min_dist = tmp_dist;
                station_index = i;
            }
        }
        return Point.fromLngLat(all_stations.get(station_index).getLon(), all_stations.get(station_index).getLat());
    }

    private void getRoute(Point origin, Point destination) {
        Point nearest_station = getNearestStation(origin, destination);
        NavigationRoute.builder(getContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .addWaypoint(nearest_station)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, miniMap, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                        button.setEnabled(true);
                        button.setBackgroundResource(R.color.mapboxBlue);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
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
            if (originLocation == null){
                originLocation = new Location("");
                originLocation.setLatitude(52.403624);
                originLocation.setLongitude(16.950047);
            }

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

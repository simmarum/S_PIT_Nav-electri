package com.example.nav.nav_electri;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
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
    private TextView battery_actual;
    private TextView battery_after;
    private TextView route_distance;
    private Button find_button;
    private EditText from_input;
    private Boolean from_input_correct;
    private EditText to_input;
    private Boolean to_input_correct;
    private MapboxGeocoding mapboxGeocoding;
    private Point firstResultPoint_A = null;
    private Point firstResultPoint_B = null;

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
                        setActualBatteryText();
                        setAfterBatteryText();

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

        battery_actual = fragmentLayout.findViewById(R.id.textViewBatteryPercentageActual);
        setActualBatteryText();

        battery_after = fragmentLayout.findViewById(R.id.textViewBatteryPercentageAfter);
        setAfterBatteryText();

        route_distance = fragmentLayout.findViewById(R.id.textViewDistance);
        setActualRouteDistance(0);

        find_button = fragmentLayout.findViewById(R.id.findRoute);
        find_button.setEnabled(false);
        find_button.setBackgroundResource(R.color.mapboxGrayLight);

        from_input_correct = false;
        from_input = fragmentLayout.findViewById(R.id.point_A);
        from_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    find_button.setEnabled(false);
                    find_button.setBackgroundResource(R.color.mapboxGrayLight);
                    from_input_correct = false;
                } else {
                    from_input_correct = true;
                    if (to_input_correct){
                        find_button.setEnabled(true);
                        find_button.setBackgroundResource(R.color.mapboxBlue);
                    }
                }
            }
        });

        to_input_correct = false;
        to_input = fragmentLayout.findViewById(R.id.point_B);
        to_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    find_button.setEnabled(false);
                    find_button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.mapboxGrayLight));
                    to_input_correct = false;
                } else {
                    to_input_correct = true;
                    if (from_input_correct) {
                        find_button.setEnabled(true);
                        find_button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.mapboxBlue));
                    }
                }
            }
        });

        find_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken(Mapbox.getAccessToken())
                        .query(from_input.getText().toString())
                        .build();
                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                        List<CarmenFeature> results = response.body().features();

                        if (results.size() > 0) {
                            // Log the first results Point.
                            firstResultPoint_A = results.get(0).center();

                        } else {
                            firstResultPoint_A = null;
                            // No result for your request were found.
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

                mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken(Mapbox.getAccessToken())
                        .query(to_input.getText().toString())
                        .build();
                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                        List<CarmenFeature> results = response.body().features();

                        if (results.size() > 0) {
                            // Log the first results Point.
                            firstResultPoint_B = results.get(0).center();

                        } else {
                            firstResultPoint_B = null;
                            // No result for your request were found.
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
                if (firstResultPoint_A != null){
                    Log.i("MMMAA",firstResultPoint_A.toString());
                }
                if (firstResultPoint_B != null){
                    Log.i("MMMBB",firstResultPoint_B.toString());
                }
                if ((firstResultPoint_A != null) && (firstResultPoint_B != null) ){
                    getRoute(firstResultPoint_A, firstResultPoint_B);
                    setActualBatteryText();
                    setAfterBatteryText();
                    mapboxMap.setCameraPosition(new CameraPosition.Builder()
                            .target(new LatLng(firstResultPoint_A.latitude(), firstResultPoint_A.longitude()))
                            .zoom(13.0)
                            .build());
                }
            }
        });
        return fragmentLayout;
    }


    private void setActualRouteDistance(double distance){
        route_distance.setText(getResources().getString(R.string.text_navigation_distance, distance));
    }

    private void setActualBatteryText(){
        if (Car.isConnected){
            battery_actual.setText(getResources().getString(R.string.text_navigation_battery_actual, Car.battery_percent_actual));
        } else {
            battery_actual.setText(getResources().getString(R.string.text_navigation_battery_actual, -1));
        }
    }

    private void setAfterBatteryText(){
        if (Car.isConnected){
            battery_after.setText(getResources().getString(R.string.text_navigation_battery_after, Car.battery_percent_after));
        } else {
            battery_after.setText(getResources().getString(R.string.text_navigation_battery_after, -1));
        }
    }
    private Point getNearestStation(Point origin, Point destination) throws Exception {
        int station_index_0 = -1;
        double min_dist_0 = Double.POSITIVE_INFINITY;
        int battery_after_0 = -1;

        int station_index_min = -1;
        double min_dist_min = Double.POSITIVE_INFINITY;
        int battery_after_min = -1;

        double ratio = 0.003;

        LatLng origin_pos = new LatLng(origin.longitude(), origin.latitude());
        LatLng dest_pos = new LatLng(destination.longitude(), destination.latitude());

        double simple_distance = origin_pos.distanceTo(dest_pos);
        int after_simple_distance_battery = Car.battery_percent_actual - (int)(simple_distance*ratio);

        if (Car.isConnected) {
            if (after_simple_distance_battery > Car.min_battery_percent) {
                Car.battery_percent_after = after_simple_distance_battery;
                setActualRouteDistance(simple_distance / 1000.0);
                return null;
            } else {
                ArrayList<StationPosition> all_stations = markerPosition.getStationList();
                for (int i = 0; i < all_stations.size(); i++) {
                    LatLng tmp_station = new LatLng(all_stations.get(i).getLon(), all_stations.get(i).getLat());
                    double dist_part_1 = origin_pos.distanceTo(tmp_station);
                    int perc_part_1 = Car.battery_percent_actual - (int) (dist_part_1 * ratio);
                    double dist_part_2 = tmp_station.distanceTo(dest_pos);
                    int perc_part_2 = 100 - (int) (dist_part_2 * ratio);

                    if (perc_part_1 > 0) {
                        double tmp_dist = dist_part_1 + dist_part_2;
                        if (perc_part_2 > Car.min_battery_percent) {
                            if (min_dist_min > tmp_dist) {
                                min_dist_min = tmp_dist;
                                station_index_min = i;
                                battery_after_min = perc_part_2;
                            }
                        } else if (perc_part_2 > 0) {
                            if (min_dist_0 > tmp_dist) {
                                min_dist_0 = tmp_dist;
                                station_index_0 = i;
                                battery_after_0 = perc_part_2;
                            }
                        }
                    }
                }
                if (station_index_min >= 0) {
                    Car.battery_percent_after = battery_after_min;
                    setActualRouteDistance(min_dist_min / 1000.0);
                    return Point.fromLngLat(all_stations.get(station_index_min).getLon(), all_stations.get(station_index_min).getLat());
                } else if (station_index_0 >= 0) {
                    Car.battery_percent_after = battery_after_0;
                    setActualRouteDistance(min_dist_0 / 1000.0);
                    return Point.fromLngLat(all_stations.get(station_index_0).getLon(), all_stations.get(station_index_0).getLat());
                } else {
                    setActualRouteDistance(0.0);
                    throw new Exception();
                }
            }
        } else {
            setActualRouteDistance(simple_distance / 1000.0);
            return null;
        }
    }

    private void getRoute(Point origin, Point destination) {
        Point nearest_station = null;
        try {
            nearest_station = getNearestStation(origin, destination);
            if (nearest_station != null) {
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
            } else {
                NavigationRoute.builder(getContext())
                        .accessToken(Mapbox.getAccessToken())
                        .origin(origin)
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
        } catch (Exception e) {
            Log.w(TAG,"Route too long (battery drop below zero!)");
            navigationMapRoute.removeRoute();
        }

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
        mapboxGeocoding.cancelCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        miniMap.onResume();
        setActualBatteryText();
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

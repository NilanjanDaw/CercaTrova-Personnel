package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import debapriya.thunderstruck.com.cercatrovapersonnel.support.EmergencyPersonnel;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Activity showing the User and shortest possible path
 * @author nilanjan and debapriya
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener {
    // Class variable definition section
    public static final int REQUEST_ACCESS_LOCATION = 0;
    public static final String TAG = "MapsActivity";
    FloatingActionButton navigate;
    private GoogleMap mMap;
    private User user;
    private EmergencyPersonnel emergencyPersonnel;
    private List<Polyline> polylines;
    private GoogleApiClient mGoogleApiClient;
    private LatLng start, end;

    /**
     * Perform initialization of all fragments and loaders.
     * @param savedInstanceState is a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        navigate = (FloatingActionButton) findViewById(R.id.fab_navigate);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Getting user and personnel data from intent bundle
        user = (User) getIntent().getSerializableExtra("user_profile_data");
        emergencyPersonnel = (EmergencyPersonnel) getIntent().getSerializableExtra("profile_data");
        // setting up user and personnel location objects
        start = new LatLng(emergencyPersonnel.getLocation().getCoordinates().get(0),
                emergencyPersonnel.getLocation().getCoordinates().get(1));
        end = new LatLng(user.getLocation().getCoordinates().get(0), user.getLocation().getCoordinates().get(1));

        /*
        Asynchronously finding shortest path between the user and emergency personnel
        and updating UI accordingly
         */
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .build();
        routing.execute();
        /*
        Setting a click listener on navigate fab to start turn by turn navigation
         */
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + end.latitude + "," + end.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    /**
     * Lifecycle method to start listening for location updates
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            buildGoogleApiClient();
        }
    }

    /**
     * Lifecycle method to stop listening for location updates
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Setting up Google Map and updating markers and camera as required
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker to user's location and move the camera
        LatLng userLocation = new LatLng(user.getLocation().getCoordinates().get(0),
                user.getLocation().getCoordinates().get(1));
        mMap.addMarker(new MarkerOptions().position(userLocation).title(user.getFirstName() + " " + user.getLastName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
        getLocationUpdate(); // start listening for GPS updates

    }

    /**
     * Check if location permissions are granted
     * If not handle dynamic permission grant
     * else start connecting to Google Location API client
     */
    private void getLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mayRequestLocation();
            return;
        }
        mMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
    }

    /**
     * Synchronously connect to Google Location API client
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Dynamically handle Location permission from user
     * @return status of the permission after the exercise
     */
    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            /*
            If permission was denied previously show rationale and get permission
             */
            Snackbar.make(((Activity) getBaseContext()).findViewById(android.R.id.content),
                    R.string.permission_rationale_location, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
                        }
                    });
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdate(); // start connecting to Google Location API
            }
        }
    }

    /**
     * Method invoked on receiving new GPS location
     * @param location object containing new GPS location details
     */
    @Override
    public void onLocationChanged(Location location) {
        //TODO update server about user location update
        debapriya.thunderstruck.com.cercatrovapersonnel.support.Location userLocation;
        ArrayList<Double> coordinates = new ArrayList<>();
        coordinates.add(location.getLatitude());
        coordinates.add(location.getLongitude());
        userLocation = new debapriya.thunderstruck.com.cercatrovapersonnel.support.Location("POINT", coordinates);
        emergencyPersonnel.setLocation(userLocation); // update emergency personnel's location according to new details
        Log.d(TAG, "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
    }

    /**
     * On successful connection to Google Client, start listening to
     * GPS updates
     * @param bundle containing connection details
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // setup parameters
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // start location callback service
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Method to handle errors in finding a suitable route
     * @param e
     */
    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    /**
     * Callback method invoked on finding the shortest routing path
     * @param route Object containing routing information
     * @param shortestRouteIndex index values
     */
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 16f));

        // clear polyline object of previous routing information
        if(polylines != null && polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        /*
        Setup new polyline according to routing information received
        and update UI
         */
        polylines = new ArrayList<>();
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(R.color.colorPrimary));
        polyOptions.width(10);
        polyOptions.addAll(route.get(0).getPoints());
        Polyline polyline = mMap.addPolyline(polyOptions);
        polylines.add(polyline);
    }

    @Override
    public void onRoutingCancelled() {

    }
}

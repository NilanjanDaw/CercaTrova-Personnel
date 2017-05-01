package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.Constants;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.EmergencyPersonnel;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.Endpoint;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.UpdatePacket;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Nilanjan and Debapriya on 09-Apr-17.
 * Project CercaTrova
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int REQUEST_ACCESS_LOCATION = 0;
    public static final String TAG = "MainActivity";
    private static final int REQUEST_CHECK_SETTINGS = 10;
    @BindView(R.id.button_yes)
    Button yes;
    @BindView(R.id.unit_status)
    TextView statusText;
    @BindView(R.id.unit_status_question)
    TextView statusQuestion;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private EmergencyPersonnel emergencyPersonnel;
    private Location location;
    private ProgressDialog progressDialog;
    private int unitStatus = 0;
    private Endpoint apiService;
    private int counter = 0;

    /**
     * All registered receivers for an event are notified by the Android runtime once this event happens.
     * The user details and the details of the personnel are sent as serialized objects
     *  to the next Activity.
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Broadcast" + intent.getSerializableExtra("user_profile_data").toString());
            User user = (User) intent.getSerializableExtra("user_profile_data");
            Intent intentDetails = new Intent(context, AlertActivity.class);
            intentDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentDetails.putExtra("user_data", user);
            intentDetails.putExtra("profile_data", emergencyPersonnel);
            startActivity(intentDetails);
        }
    };

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    /**
     * Perform initialization of all fragments and loaders.
     * @param savedInstanceState is a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        emergencyPersonnel = (EmergencyPersonnel) getIntent().getSerializableExtra("profile_data");
        progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
         /*
          Retrofit is a type-safe REST client for Android, used for interacting with the APIs and sending network requests
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Create an implementation of the API endpoints defined by the service interface.
        apiService = retrofit.create(Endpoint.class);
    }

    /**
     * When the activity enters the Resumed state, it comes to the foreground, and then the system invokes the onResume() callback
     * In this state, the personnel interacts with the app.
     * If the personnel chooses to be active, it's status is sent as a broadcast.
     */
    @Override
    protected void onResume() {
        super.onResume();
        statusText.setText(getString(R.string.unit_status_inactive));
        statusQuestion.setText(getString(R.string.unit_active_question));
        getLocation();
        getBaseContext().registerReceiver(broadcastReceiver, new IntentFilter(getString(R.string.broadcast_intent_filter)));
    }

    /**
     * When an interruptive event occurs, the activity enters the Paused state, and the system invokes the onPause() callback.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
        getBaseContext().unregisterReceiver(broadcastReceiver);
    }

    /**
     * Called when the activity is no longer visible to the user, because another activity has been resumed and is covering this one.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null)
            googleApiClient.disconnect();
    }

    /**
     *If the personnel chooses to be active, then its status changes to 1, otherwise it remains as being 0
     */
    @OnClick({R.id.button_yes})
    void notify(View view) {
        if (view.getId() == R.id.button_yes) {
            unitStatus = (unitStatus == 1) ? 0 : 1;
            new ProfileUpdateNotificationTask().execute();
        }
    }

    /**
     * checks if the user has turned on the location updates
     * If not, enable the location settings.
     */
    public int getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mayRequestPermission();
            return -1;
        }
        if (googleApiClient == null) {
            buildGoogleClientApi();
            createLocationRequest();
            buildLocationSettingsRequest();
            googleApiClient.connect();
        }

        startLocationUpdates();
        return 0;
    }

    /**
     * The Google API Client provides a common entry point to all the Google Play services
     * and manages the network connection between the user's device and each Google service.
     */
    protected synchronized void buildGoogleClientApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * LocationRequest objects are used to request a quality of service for location updates from the FusedLocationProviderApi.
     * The application requires high priority for accessing the location
     */
    public void createLocationRequest() {
        locationRequest = new LocationRequest();
        //They will only be assigned power blame for the interval set by 10sec,
        //but can still receive locations triggered by other applications at a rate up to 5 sec.
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        LocationServices.SettingsApi.checkLocationSettings(
                googleApiClient,
                locationSettingsRequest
        ).setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                           mayRequestPermission();
                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest, MainActivity.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Log.e(TAG, errorMessage);
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        counter++;
        if (counter == 4 && unitStatus == 1) {
            updateLocationOnServer(location);
            counter = 0;
        }
        Log.d(TAG, "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: Google client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * method invoked on receiving GPS update
     * @param location object containing new GPS coordinates
     */
    private void updateLocationOnServer(Location location) {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preference_file), MODE_PRIVATE);
        String locationString = "POINT(" + location.getLatitude() + " " + location.getLongitude() + ")";
        String deviceID = sharedPreferences.getString("device_id", "");
        //A packet formed containing the personnel id, location, device id and the status of the unit
        UpdatePacket packet = new UpdatePacket(emergencyPersonnel.getPersonnelId(), locationString, deviceID, unitStatus);
        //An invocation of a Retrofit method that sends a request to a web server and returns a response.
        Call<EmergencyPersonnel> updateProfile = apiService.updateProfile(packet);
        //Calls have been handled asynchronously with enqueue method
        updateProfile.enqueue(new Callback<EmergencyPersonnel>() {
            /**
             * onResponse method is invoked for a received HTTP response.
             * @param call creates a new, identical call to this one which can be enqueued
             *             or executed even if this call has already been.
             * @param response synchronously sends the request and returns its response.
             */
            @Override
            public void onResponse(Call<EmergencyPersonnel> call, Response<EmergencyPersonnel> response) {
                Log.d(TAG, "onResponse: " + response.body().getLocation().getCoordinates().get(0));
            }

            /**
             * onFailure method is invoked when a network exception occurred talking to the server
             * or when an unexpected exception occurred creating the request or processing the response.
             */
            @Override
            public void onFailure(Call<EmergencyPersonnel> call, Throwable t) {
                //TODO update logic to handle server failure
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * dynamically requesting permission for accessing GPS location
     * @return status of the permission
     */
    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, getString(R.string.permission_rationale_location), Toast.LENGTH_LONG).show();
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
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
                getLocation();
            } else {
                Toast.makeText(this, getString(R.string.permission_rationale_location), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     *The personnel is requested for changing the location settings
     * If the personnel makes changes, an appropriate message is displayed on the Log
     * else a toast is displayed mentioning they are needed for GPS triangulation.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, getString(R.string.permission_rationale_location), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                break;
        }
    }


    private class ProfileUpdateNotificationTask extends AsyncTask<Void, Void, EmergencyPersonnel> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Updating Profile! Please wait");
            progressDialog.show();
        }

        @Override
        protected EmergencyPersonnel doInBackground(Void... voids) {
            waitForLocationUpdate();

            debapriya.thunderstruck.com.cercatrovapersonnel.support.Location userLocation;
            ArrayList<Double> coordinates = new ArrayList<>();
            coordinates.add(location.getLatitude());
            coordinates.add(location.getLongitude());
            //updating personnel location to new GPS coordinates
            userLocation = new debapriya.thunderstruck.com.cercatrovapersonnel.support.Location("Point", coordinates);
            emergencyPersonnel.setLocation(userLocation);
            String locationString = "POINT(" + location.getLatitude() + " " + location.getLongitude() + ")";
            updateUserProfile(emergencyPersonnel.getPersonnelId(), locationString);
            return emergencyPersonnel;
        }

        /**
         * Update UI according to the emergency personnel status
         */
        @Override
        protected void onPostExecute(EmergencyPersonnel emergencyPersonnel) {
            super.onPostExecute(emergencyPersonnel);
            Log.d(TAG, "onPostExecute: called" );
            statusText.setText((unitStatus == 0)? getString(R.string.unit_status_inactive):
                    getString(R.string.unit_status_active));
            statusQuestion.setText((unitStatus == 0)? getString(R.string.unit_active_question):
                    getString(R.string.unit_inactive_question));
            progressDialog.dismiss();
        }

        private void updateUserProfile(String personnelID, String location) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preference_file), MODE_PRIVATE);
            String deviceID = "";
            while (deviceID.equals("")) {
                //the device id is saved as shared preferences
                deviceID = sharedPreferences.getString("device_id", "");
                if (!deviceID.equals(""))
                    break;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "updateUserProfile: " + deviceID);
            UpdatePacket packet = new UpdatePacket(personnelID, location, deviceID, unitStatus);
            //An invocation of a Retrofit method that sends a request to a web server and returns a response.
            Call<EmergencyPersonnel> updateProfile = apiService.updateProfile(packet);
            //Calls being handled asynchronously
            updateProfile.enqueue(new Callback<EmergencyPersonnel>() {
                @Override
                public void onResponse(Call<EmergencyPersonnel> call, Response<EmergencyPersonnel> response) {
                    Log.d(TAG, "onResponse: " + response.body().getLocation().getCoordinates().get(0));
                }

                @Override
                public void onFailure(Call<EmergencyPersonnel> call, Throwable t) {
                    //TODO update logic to handle server failure
                }
            });
        }

        /**
         * Suspending the profile update notification task thread until a location update is received.
         */
        private void waitForLocationUpdate() {
            while (location == null) {
                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "doInBackground: null");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

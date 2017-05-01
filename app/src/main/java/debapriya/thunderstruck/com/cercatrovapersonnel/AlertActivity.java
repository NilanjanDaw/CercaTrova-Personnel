package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import debapriya.thunderstruck.com.cercatrovapersonnel.support.Constants;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.EmergencyAcceptPacket;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.EmergencyPersonnel;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.Endpoint;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Alert Notification Activity
 * @author Debapriya and Nilanjan
 */
public class AlertActivity extends AppCompatActivity {

    Button investigate;
    private Endpoint apiService;
    private MediaPlayer mediaPlayer;

    /**
     * Perform initialization of all fragments and loaders.
     * Instantiate and play alert tone, and render Alert UI
     * @param savedInstanceState is a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        final User user = (User) getIntent().getSerializableExtra("user_data");
        final EmergencyPersonnel personnel = (EmergencyPersonnel) getIntent().getSerializableExtra("profile_data");
        // Instantiating a MediaPlayer object to play alert tone
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.oxygen);
        this.mediaPlayer = mediaPlayer;
        mediaPlayer.setLooping(true); // Alert tone player started in loop
        mediaPlayer.start();
        /*
         * Instantiating Retrofit object to perform server operations
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(Endpoint.class);
        investigate = (Button) findViewById(R.id.investigate);
        investigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Will start rendering SOS details and notify Server of Personnel's willingness
                to investigate
                 */
                mediaPlayer.stop();
                Intent intent = new Intent(getBaseContext(), SOSDetailsActivity.class);
                intent.putExtra("user_data", user);
                intent.putExtra("profile_data", personnel);
                startActivity(intent);
                EmergencyAcceptPacket acceptPacket = new EmergencyAcceptPacket(user.getAdhaarNumber(), personnel.getPersonnelId());
                // starting network activity in background
                notifyAcceptance(acceptPacket);
                finish();
            }
        });
    }

    /**
     * Overriding back button to prevent Personnel's from accidentally going back
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Cannot go back. Please Answer the Call of Duty!", Toast.LENGTH_LONG).show();
    }

    /**
     * Enabling Immersive mode to hide soft Navigation bar
     * @param hasFocus checks if View has focus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Setting Window flags as required
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * Overriding lifecycle method to stop playing Alert tone on losing UI focus
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }

    /**
     * Method to notify web server about personnel positively confirming investigation
     * of emergency
     * @param accept Payload object containing data to be sent to the web server as part of
     *               the confirmation packet
     */
    private void notifyAcceptance(EmergencyAcceptPacket accept) {

        /*
        Performing an asynchronous network POST request to inform server of personnel interaction
         */
        Call<Void> acceptEmergency = apiService.acceptLogin(accept);
        acceptEmergency.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(AlertActivity.this, "Emergency Accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}

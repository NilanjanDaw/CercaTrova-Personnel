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

public class AlertActivity extends AppCompatActivity {

    Button investigate;
    Endpoint apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        final User user = (User) getIntent().getSerializableExtra("user_data");
        final EmergencyPersonnel personnel = (EmergencyPersonnel) getIntent().getSerializableExtra("profile_data");
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.oxygen);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(Endpoint.class);
        investigate = (Button) findViewById(R.id.investigate);
        investigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                Intent intent = new Intent(getBaseContext(), SOSDetailsActivity.class);
                intent.putExtra("user_data", user);
                intent.putExtra("profile_data", personnel);
                startActivity(intent);
                EmergencyAcceptPacket acceptPacket = new EmergencyAcceptPacket(user.getAdhaarNumber(), personnel.getPersonnelId());
                notifyAcceptance(acceptPacket);
                finish();
            }
        });

    }

    private void notifyAcceptance(EmergencyAcceptPacket accept) {

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

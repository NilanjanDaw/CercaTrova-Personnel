package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.EmergencyPersonnel;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;

public class  SOSDetailsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "SOSDetailsActivity";
    protected Location mLastLocation;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.age) TextView age;
    @BindView(R.id.gender) TextView gender;
    @BindView(R.id.adhaar_number) TextView adhaarNumber;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.blood_group) TextView bloodGroup;
    @BindView(R.id.contact) Button contact;
    @BindView(R.id.navigate) Button navigate;
    private User user;
    private EmergencyPersonnel emergencyPersonnel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosdetails);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra("user_data");
        emergencyPersonnel = (EmergencyPersonnel) getIntent().getSerializableExtra("profile_data");
        setupUI(user);
    }

    public void setupUI(final User user) {
        name.setText(user.getFirstName() + " " + user.getLastName());
        Log.d(TAG, "setupUI: " + user.getAge() + user.getAddress());

        age.setText(String.format(Locale.ENGLISH, "%d", user.getAge()));
        gender.setText(user.getGender());
        adhaarNumber.setText(user.getAdhaarNumber());
        address.setText(user.getAddress());
        bloodGroup.setText(user.getBloodGroup());
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + user.getContactNumber()));
                startActivity(intent);
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("user_profile_data", user);
                intent.putExtra("profile_data", emergencyPersonnel);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

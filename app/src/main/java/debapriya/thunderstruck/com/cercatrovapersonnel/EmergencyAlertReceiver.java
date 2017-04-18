package debapriya.thunderstruck.com.cercatrovapersonnel;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import debapriya.thunderstruck.com.cercatrovapersonnel.support.Location;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;

/**
 * Created by nilanjan on 16-Apr-17.
 * Project client_personnel
 */

public class EmergencyAlertReceiver extends FirebaseMessagingService {

    public static final String TAG = "EmergencyBroadcast";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Map<String, String> data = remoteMessage.getData();
        User user = digestData(data);
        handleNotification(user);
    }

    private User digestData(Map<String, String> data) {
        User user = new User();
        user.setAdhaarNumber(data.get("adhaar_number"));
        user.setFirstName(data.get("first_name"));
        user.setLastName(data.get("last_name"));
        user.setAddress(data.get("address"));
        user.setContactNumber(data.get("contact_number"));
        user.setAge(Integer.parseInt(data.get("age")));
        user.setBloodGroup(data.get("blood_group"));
        user.setEmailId(data.get("email_id"));
        user.setGender(data.get("gender"));
        String locationData = data.get("location");
        Gson gson = new GsonBuilder().create();
        Location location = gson.fromJson(locationData, Location.class);
        user.setLocation(location);
        return user;
    }

    private void handleNotification(User user) {

        Intent intent = new Intent(getString(R.string.broadcast_intent_filter));
        intent.putExtra("user_profile_data", user);
        getBaseContext().sendBroadcast(intent);
    }
}

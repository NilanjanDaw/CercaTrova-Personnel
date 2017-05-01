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
 * Created by nilanjan and debapriya on 16-Apr-17.
 * Project client_personnel
 */

public class EmergencyBroadcastListener extends FirebaseMessagingService {

    public static final String TAG = "EmergencyBroadcast";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
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
        /*
         Retrieving Data map and digesting it for further use by  foreground processes
         */
        Map<String, String> data = remoteMessage.getData();
        User user = digestData(data);
        handleNotification(user);
    }

    /**
     * Method to digest the data received as part of the Firebase message payload
     * @param data String Map containing message payload
     * @return Deserialized User object instantiated according to the Message payload
     */
    private User digestData(Map<String, String> data) {
        User user = new User();
        /*
        Extracting and updating User object according to payload data
         */
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

    /**
     * Broadcast notifier to notify foreground activity of Incoming Emergency
     * @param user Object containing details of the Emergency User
     */
    private void handleNotification(User user) {
        //sending broadcast to activity with registered broadcast filter
        Intent intent = new Intent(getString(R.string.broadcast_intent_filter));
        intent.putExtra("user_profile_data", user);
        getBaseContext().sendBroadcast(intent);
    }
}

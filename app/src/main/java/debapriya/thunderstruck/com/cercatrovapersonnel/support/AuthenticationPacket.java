package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */

public class AuthenticationPacket {

    @SerializedName("user_id") @Expose
    private String userID;
    @SerializedName("password") @Expose
    private String password;

    public AuthenticationPacket(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

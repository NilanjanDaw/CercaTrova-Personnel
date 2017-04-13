package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */

public class AuthenticationPacket {

    @SerializedName("personnel_id") @Expose
    private String personnelId;

    @SerializedName("password") @Expose
    private String password;

    public AuthenticationPacket(String personnelId, String password) {
        this.personnelId = personnelId;
        this.password = password;
    }

    public String getUserID() {
        return personnelId;
    }

    public void setUserID(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

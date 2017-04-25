package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nilanjan on 20-Apr-17.
 * Project client_personnel
 */

public class EmergencyAcceptPacket {

    @SerializedName("user_adhaar_number") @Expose
    private String adhaarNumber;
    @SerializedName("personnel_id") @Expose
    private String personnelID;

    public EmergencyAcceptPacket(String adhaarNumber, String personnelID) {
        this.adhaarNumber = adhaarNumber;
        this.personnelID = personnelID;
    }
}

package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nilanjan on 17-Apr-17.
 * Project CercaTrova
 */

public class UpdatePacket {

    @SerializedName("personnel_id")
    @Expose
    private String personnelID;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("device_id")
    @Expose
    private String deviceID;
    @SerializedName("status") @Expose
    private int status;

    public UpdatePacket(String personnelID, String location, String deviceID, int status) {
        this.personnelID = personnelID;
        this.location = location;
        this.deviceID = deviceID;
        this.status = status;
    }
}

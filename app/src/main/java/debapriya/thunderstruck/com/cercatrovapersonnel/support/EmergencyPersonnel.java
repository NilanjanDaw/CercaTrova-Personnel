package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */


public class EmergencyPersonnel implements Serializable {

    @SerializedName("personnel_id")
    @Expose
    private String personnelId;
    @SerializedName("adhaar_number")
    @Expose
    private String adhaarNumber;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("contact_number")
    @Expose
    private int contactNumber;
    @SerializedName("car_number")
    @Expose
    private String carNumber;
    @SerializedName("responder_type")
    @Expose
    private int responderType;
    @SerializedName("base_station")
    @Expose
    private String baseStation;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("location")
    @Expose
    private Location location;


    public EmergencyPersonnel(String personnelId, String adhaarNumber, String firstName,
                              String lastName, int contactNumber, String carNumber,
                              int responderType, String baseStation, String password, Location location) {
        this.personnelId = personnelId;
        this.adhaarNumber = adhaarNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.carNumber = carNumber;
        this.responderType = responderType;
        this.baseStation = baseStation;
        this.password = password;
        this.location = location;
    }


    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(String adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getResponderType() {
        return responderType;
    }

    public void setResponderType(int responderType) {
        this.responderType = responderType;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public void setBaseStation(String baseStation) {
        this.baseStation = baseStation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */


public class User implements Serializable{

    @SerializedName("adhaar_number") @Expose
    private String adhaarNumber;
    @SerializedName("first_name") @Expose
    private String firstName;
    @SerializedName("last_name") @Expose
    private String lastName;
    @SerializedName("email_id") @Expose
    private String emailId;
    @SerializedName("contact_number") @Expose
    private String contactNumber;
    @SerializedName("device_id")
    @Expose
    private String deviceID;
    @SerializedName("address") @Expose
    private String address;
    @SerializedName("age") @Expose
    private int age;
    @SerializedName("gender") @Expose
    private String gender;
    @SerializedName("blood_group") @Expose
    private String bloodGroup;
    @SerializedName("password") @Expose
    private String password;
    @SerializedName("location") @Expose
    private Location location;


    /**
     *
     * @param lastName
     * @param contactNumber
     * @param emailId
     * @param bloodGroup
     * @param location
     * @param address
     * @param age
     * @param gender
     * @param firstName
     * @param password
     * @param adhaarNumber
     * @param deviceID
     */
    public User(String adhaarNumber, String firstName, String lastName,
                String emailId, String contactNumber, String address,
                int age, String gender, String bloodGroup,
                String password, Location location, String deviceID) {
        super();
        this.adhaarNumber = adhaarNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.password = password;
        this.location = location;
        this.deviceID = deviceID;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getDeviceID() {
        return this.deviceID;
    }

}

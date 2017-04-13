package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */

public class Location implements Serializable {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;

    /**
     *
     * @param type
     * @param coordinates
     */
    public Location(String type, List<Double> coordinates) {
        super();
        this.type = type;
        this.coordinates = coordinates;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}

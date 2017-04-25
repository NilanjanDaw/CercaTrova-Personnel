package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */

public interface Endpoint {

    @POST("personnel_login_server/user/")
    Call<EmergencyPersonnel> createUser(@Body EmergencyPersonnel user);

    @POST("personnel_login_server/update/")
    Call<EmergencyPersonnel> updateProfile(@Body UpdatePacket packet);

    @POST("personnel_login_server/account_authentication/")
    Call<EmergencyPersonnel> validateLogin(@Body AuthenticationPacket authenticationPacket);

    @POST("emergency/accept/")
    Call<Void> acceptLogin(@Body EmergencyAcceptPacket user);


}

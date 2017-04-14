package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by nilanjan on 09-Apr-17.
 * Project CercaTrova
 */

public interface Endpoint {

    @GET("personnel_login_server/user/")
    Call<EmergencyPersonnel[]> getUserList();

    @POST("personnel_login_server/user/")
    Call<EmergencyPersonnel> createUser(@Body EmergencyPersonnel user);

    @POST("personnel_login_server/account_authentication/")
    Call<EmergencyPersonnel> validateLogin(@Body AuthenticationPacket authenticationPacket);


}

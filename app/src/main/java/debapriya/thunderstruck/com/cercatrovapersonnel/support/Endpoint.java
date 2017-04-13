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

    @GET("login_server/user/")
    Call<User[]> getUserList();

    @POST("login_server/user/")
    Call<User> createUser(@Body User user);

    @POST("login_server/account_authentication/")
    Call<User> validateLogin(@Body AuthenticationPacket authenticationPacket);

    @POST("emergency/notify/")
    Call<Emergency> notifyEmergency(@Body Emergency emergency);

}

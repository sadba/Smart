package sadba.lab.com.smart.Remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import sadba.lab.com.smart.Model.PostUser;
import sadba.lab.com.smart.Model.PostVerifUser;
import sadba.lab.com.smart.Model.User;
import sadba.lab.com.smart.Model.VerifUser;

public interface IMyAPI {

    @Headers("Content-type: application/json")
    @POST("mob-connexion")
    Call<User> loginUser(@Body PostUser postUser);

    @Headers("Content-type: application/json")
    @POST("mob-connexion")
    Call<VerifUser> verifUser(@Body PostVerifUser postVerifUser);
}

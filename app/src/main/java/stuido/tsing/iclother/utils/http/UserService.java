package stuido.tsing.iclother.utils.http;

import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import stuido.tsing.iclother.data.user.User;

/**
 * Created by Endless on 2017/7/19.
 */

public interface UserService {
    @POST("clientUser/signIn")
    Observable<HttpResponse<User>> signIn(@Body User user);

    @POST("clientUser/signUp")
    Observable<HttpResponse<User>> signUp(@Body User user);

}

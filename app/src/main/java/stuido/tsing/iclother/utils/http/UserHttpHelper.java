package stuido.tsing.iclother.utils.http;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import stuido.tsing.iclother.data.user.User;
import stuido.tsing.iclother.utils.ApiException;

public class UserHttpHelper extends HttpHelper {
    public Observable<User> signIn(String name, String pwd) {
        User user = new User();
        user.setName(name).setPwd(pwd);
        return retrofit.create(UserService.class)
                .signIn(user)
                .map(new HttpResponseFunc<>());
    }

    public Observable<User> signUp(String name, String pwd) {
        User user = new User();
        user.setName(name).setPwd(pwd);
        return retrofit.create(UserService.class)
                .signUp(user)
                .map(new HttpResponseFunc<>());
    }

    private class HttpResponseFunc<T> implements Function<HttpResponse<T>, T> {
        @Override
        public T apply(@NonNull HttpResponse<T> httpResponse) throws Exception {
            if (httpResponse.getStatus() != 200) {
                throw new ApiException(httpResponse.getMsg());
            }
            return httpResponse.getData();
        }
    }
}

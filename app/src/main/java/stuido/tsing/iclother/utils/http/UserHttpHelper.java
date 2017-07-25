package stuido.tsing.iclother.utils.http;

import rx.Observable;
import stuido.tsing.iclother.data.user.User;

public class UserHttpHelper extends HttpHelper {
    public Observable<User> signIn(String name, String pwd) {
        User user = new User();
        user.setName(name).setPwd(pwd);
        Observable<HttpResponse<User>> httpResponseObservable = retrofit.create(UserService.class)
                .signIn(user);
        return httpResponseObservable.map(new HttpResponseFunc<>());
    }

    public Observable<User> signUp(String name, String pwd) {
        User user = new User();
        user.setName(name).setPwd(pwd);
        return retrofit.create(UserService.class)
                .signUp(user)
                .map(new HttpResponseFunc<>());
    }
}
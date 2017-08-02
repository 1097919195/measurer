package stuido.tsing.iclother.utils.http.account;

import rx.Observable;
import stuido.tsing.iclother.data.user.User;
import stuido.tsing.iclother.utils.http.HttpHelper;

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
}
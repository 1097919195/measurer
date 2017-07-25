package stuido.tsing.iclother.data.user;

import rx.Observable;
import stuido.tsing.iclother.utils.http.UserHttpHelper;

/**
 * Created by Endless on 2017/7/20.
 */

public class UserRepository implements UserDataSource {
    @Override
    public Observable<User> signIn(String name, String pwd) {
        return new UserHttpHelper().signIn(name, pwd);
    }
}

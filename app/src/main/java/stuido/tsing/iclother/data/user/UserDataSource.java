package stuido.tsing.iclother.data.user;

import rx.Observable;

/**
 * Created by Endless on 2017/7/20.
 */

public interface UserDataSource {
    public Observable<User> signIn(String name, String pwd);

    public Observable<User> signUp(String name, String pwd);
}

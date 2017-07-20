package stuido.tsing.iclother.data.user;

/**
 * Created by Endless on 2017/7/20.
 */

public interface UserDataSource {
    public io.reactivex.Observable<User> signIn(String name, String pwd);
}

package stuido.tsing.iclother.user;


import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/8/24.
 */

public class UserPresenter implements UserContract.Presenter {
    private final UserContract.View userView;

    public UserPresenter(@NonNull UserContract.View userView) {
        this.userView = checkNotNull(userView);
        this.userView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void logout() {
        userView.logout();
    }
}

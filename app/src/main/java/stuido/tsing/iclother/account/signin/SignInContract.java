package stuido.tsing.iclother.account.signin;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;

/**
 * Created by Endless on 2017/7/20.
 */

public interface SignInContract {
    interface View extends BaseView<Presenter> {
        void showSignInSuccess();

        void showSignInError();
    }

    interface Presenter extends BasePresenter {
        void signIn();
    }
}

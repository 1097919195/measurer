package stuido.tsing.iclother.account.signup;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;

/**
 * Created by Endless on 2017/7/24.
 */

public interface SignUpContract {
    interface view extends BaseView<presenter> {
        void showSignUpSuccess();

        void showSignUpError();
    }

    interface presenter extends BasePresenter {
        void signUp(String name, String pwd);
    }
}

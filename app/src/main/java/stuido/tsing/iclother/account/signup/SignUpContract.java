package stuido.tsing.iclother.account.signup;

import rx.Subscriber;
import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;

/**
 * Created by Endless on 2017/7/24.
 */

public interface SignUpContract {
    interface View extends BaseView<Presenter> {
        void showSignUpSuccess();

        void showSignUpError();
    }

    interface Presenter extends BasePresenter {
        void signUp(Subscriber o);
    }
}

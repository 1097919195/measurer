package stuido.tsing.iclother.login;

import android.content.Context;
import android.widget.EditText;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;

/**
 * Created by Endless on 2017/7/20.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showLoginSuccess();

        void showLoginError();
    }

    interface Presenter extends BasePresenter {
        void login(EditText name, EditText pwd, Context context);
    }
}

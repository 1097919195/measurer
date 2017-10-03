package com.npclo.imeasurer.account.signin;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.user.User;

/**
 * Created by Endless on 2017/7/20.
 */

public interface SignInContract {
    interface View extends BaseView<Presenter> {
        void showSignInSuccess(User __);

        void showSignInError(Throwable e);

        void completeSignIn();

        void showLoading(boolean bool);
    }

    interface Presenter extends BasePresenter {
        void signIn(String name, String pwd);
    }
}

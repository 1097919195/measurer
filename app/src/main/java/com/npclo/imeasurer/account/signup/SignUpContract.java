package com.npclo.imeasurer.account.signup;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;

import rx.Subscriber;

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

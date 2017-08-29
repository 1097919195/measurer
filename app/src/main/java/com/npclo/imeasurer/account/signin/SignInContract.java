package com.npclo.imeasurer.account.signin;

import com.npclo.imeasurer.base.BasePresenter;

import rx.Subscriber;

import com.npclo.imeasurer.base.BaseView;

/**
 * Created by Endless on 2017/7/20.
 */

public interface SignInContract {
    interface View extends BaseView<Presenter> {
        void showSignInSuccess();

        void showSignInError();
    }

    interface Presenter extends BasePresenter {
        void signIn(Subscriber o);
    }
}

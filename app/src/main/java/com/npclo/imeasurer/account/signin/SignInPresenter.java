package com.npclo.imeasurer.account.signin;

import android.support.annotation.NonNull;

import com.npclo.imeasurer.data.user.UserRepository;
import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SignInPresenter implements SignInContract.Presenter {
    private SignInContract.View mView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public SignInPresenter(@NonNull SignInContract.View signinView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        signinView = checkNotNull(signinView);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        mView = signinView;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        // TODO: 2017/8/30 maybe have bug
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void signIn(String name, String pwd) {
        Subscription subscribe = new UserRepository().signIn(name, pwd)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .doOnSubscribe(() -> mView.showLoading(true))
                .subscribe(user -> mView.showSignInSuccess(user),
                        e -> mView.showSignInError(e),
                        () -> mView.completeSignIn());
        mSubscriptions.add(subscribe);
    }
}
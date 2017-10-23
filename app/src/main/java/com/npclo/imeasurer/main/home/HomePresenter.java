package com.npclo.imeasurer.main.home;

import android.support.annotation.NonNull;

import com.npclo.imeasurer.data.app.AppRepository;
import com.npclo.imeasurer.data.user.UserRepository;
import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/9/1.
 */

public class HomePresenter implements HomeContract.Presenter {
    @NonNull
    private HomeContract.View fragment;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public HomePresenter(@NonNull HomeContract.View view, @NonNull BaseSchedulerProvider schedulerProvider) {
        fragment = checkNotNull(view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        fragment.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void getLatestVersion() {
        new AppRepository()
                .getLatestVersion()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(app -> fragment.showGetVersionSuccess(app),
                        e -> fragment.showGetVersionError(e));
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getUserInfoWithCode(String result) {
        Subscription subscribe = new UserRepository()
                .getUserInfoWithCode(result)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(
                        user -> fragment.onGetWechatUserInfoSuccess(user),
                        e -> fragment.showGetInfoError(e),
                        () -> fragment.showCompleteGetInfo());
        mSubscriptions.add(subscribe);
    }

    @Override
    public void getUserInfoWithOpenID(String id) {
        Subscription subscribe = new UserRepository()
                .getUserInfoWithOpenID(id)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(
                        user -> fragment.onGetWechatUserInfoSuccess(user),
                        e -> fragment.showGetInfoError(e),
                        () -> fragment.showCompleteGetInfo());
        mSubscriptions.add(subscribe);
    }
}
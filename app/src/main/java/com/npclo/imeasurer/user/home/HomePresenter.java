package com.npclo.imeasurer.user.home;


import android.support.annotation.NonNull;

import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import rx.subscriptions.CompositeSubscription;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/8/24.
 */

public class HomePresenter implements HomeContract.Presenter {
    @NonNull
    private final HomeContract.View userView;
    @NonNull
    private SchedulerProvider mSchedulerProvider;
    private CompositeSubscription mSubscription;

    public HomePresenter(@NonNull HomeContract.View userView, SchedulerProvider schedulerProvider) {
        this.userView = checkNotNull(userView);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        this.userView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscription.clear();
    }

    @Override
    public void logout() {
        userView.logout();
    }
}

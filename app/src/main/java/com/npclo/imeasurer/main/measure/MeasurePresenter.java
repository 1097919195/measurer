package com.npclo.imeasurer.main.measure;

import android.support.annotation.NonNull;

import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;

import rx.subscriptions.CompositeSubscription;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/9/1.
 */

public class MeasurePresenter implements MeasureContract.Presenter {
    @NonNull
    private MeasureFragment fragment;
    @NonNull
    private BaseSchedulerProvider schedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public MeasurePresenter(@NonNull MeasureContract.View view, @NonNull BaseSchedulerProvider schedulerProvider) {
        fragment = ((MeasureFragment) checkNotNull(view));
        this.schedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        fragment.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}

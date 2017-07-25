package stuido.tsing.iclother.account.signup;

import android.support.annotation.NonNull;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.user.UserRepository;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/24.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpFragment mView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public SignUpPresenter(@NonNull SignUpContract.View signUpView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        signUpView = checkNotNull(signUpView);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        mView = (SignUpFragment) signUpView;
        mView.setPresenter(this);
    }

    @Override
    public void signUp(Subscriber o) {
        Subscription subscribe = new UserRepository()
                .signUp(mView.inputName.getText().toString(), mView.inputPassword.getText().toString())
                .map(__ -> __.get_id())
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(o);
        mSubscriptions.add(subscribe);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}

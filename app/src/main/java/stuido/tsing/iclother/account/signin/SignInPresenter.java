package stuido.tsing.iclother.account.signin;

import android.support.annotation.NonNull;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.user.UserRepository;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SignInPresenter implements SignInContract.Presenter {
    private SignInFragment mView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public SignInPresenter(@NonNull SignInContract.View loginView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        loginView = checkNotNull(loginView);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        mView = (SignInFragment) loginView;
        loginView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void signIn(Subscriber o) {
        Subscription subscribe = new UserRepository()
                .signIn(mView._nameText.getText().toString(), mView._passwordText.getText().toString())
                .map(__ -> __.get_id())
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(o);
        mSubscriptions.add(subscribe);
    }
}

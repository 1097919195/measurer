package stuido.tsing.iclother.account.signin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import stuido.tsing.iclother.data.user.UserRepository;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/20.
 */

public class SignInPresenter implements SignInContract.Presenter {
    private SignInFragment mView;
    @Nullable
    private String mName;
    @Nullable
    private String mPwd;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeDisposable mSubscriptions;

    public SignInPresenter(@NonNull SignInContract.View loginView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        loginView = checkNotNull(loginView);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeDisposable();
        mView = (SignInFragment) loginView;
        loginView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mName = mView._nameText.getText().toString();
        mPwd = mView._passwordText.getText().toString();

        mSubscriptions.add(new UserRepository()
                .signIn(mName, mPwd)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(__ -> mView.showSignInSuccess(), __ -> mView.showSignInError()
                ));
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void signIn() {
        mName = mView._nameText.getText().toString();
        mPwd = mView._passwordText.getText().toString();
        if (!mView.validate()) {
            return;
        }
        new UserRepository().signIn(mName, mPwd);
    }



}

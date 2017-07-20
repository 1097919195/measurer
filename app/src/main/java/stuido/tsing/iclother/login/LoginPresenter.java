package stuido.tsing.iclother.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.widget.EditText;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.data.user.UserRepository;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/20.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private EditText _nameText;
    private EditText _passwordText;

    public LoginPresenter(@NonNull LoginContract.View loginView,
                          @NonNull BaseSchedulerProvider schedulerProvider, EditText nameText, EditText passwordText) {
        loginView = checkNotNull(loginView);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        _nameText = nameText;
        _passwordText = passwordText;
        loginView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mSubscriptions.add((Subscription) new UserRepository()
                .signIn(_nameText.getText().toString(), _passwordText.getText().toString())
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(__ -> loginView.showLoginSuccess(), __ -> {

                }));
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void login(EditText _nameText, EditText _passwordText, Context context) {
        if (!validate(_nameText, _passwordText, context)) {
            loginView.showLoginSuccess();
            return;
        }
        new UserRepository().signIn(_nameText.getText().toString(), _passwordText.getText().toString());
    }

    private boolean validate(EditText _nameText, EditText _passwordText, Context context) {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || !Patterns.PHONE.matcher(name).matches()) {
            _nameText.setError(context.getString(R.string.name_enter_valid));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            _passwordText.setError(context.getString(R.string.pwd_enter_valid));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}

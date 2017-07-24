package stuido.tsing.iclother.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.signin.SignInFragment;
import stuido.tsing.iclother.account.signin.SignInPresenter;
import stuido.tsing.iclother.base.BaseFragment;
import stuido.tsing.iclother.utils.schedulers.SchedulerProvider;

/**
 * 管理用户登录和注册的界面，使用两个不同的fragment来分别承载
 */
public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";
    private static final int REQUEST_SIGNUP = 0;
    private SignInPresenter signInPresenter;
//    EditText _emailText = null;
//    EditText _passwordText = null;
//    Button _loginButton = null;
//    TextView _signupLink = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_act);

        SignInFragment signInFragment = (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.acc_content_frame);
        if (signInFragment == null) {
            signInFragment = SignInFragment.newInstance();
            BaseFragment.addFragmentToActivity(getSupportFragmentManager(), signInFragment, R.id.acc_content_frame);

        }
        // TODO: 2017/7/24 instantiate presenter
        signInPresenter = new SignInPresenter(signInFragment, SchedulerProvider.getInstance());
    }

//    public void login() {
//
//        new android.os.Handler().postDelayed(
//                () -> {
//                    // On complete call either onLoginSuccess or onLoginFailed
//                    onLoginSuccess();
//                    // onLoginFailed();
//                    progressDialog.dismiss();
//                }, 3000);
//    }



    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

//    public void onLoginSuccess() {
//        _loginButton.setEnabled(true);
//        finish();
//    }
//
//    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//
//        _loginButton.setEnabled(true);
//    }
}


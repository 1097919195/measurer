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

    private static final int REQUEST_SIGNUP = 0;

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
        new SignInPresenter(signInFragment, SchedulerProvider.getInstance());
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

}


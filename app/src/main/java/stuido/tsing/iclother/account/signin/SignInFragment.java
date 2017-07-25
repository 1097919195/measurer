package stuido.tsing.iclother.account.signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.signup.SignUpFragment;
import stuido.tsing.iclother.base.BaseFragment;
import stuido.tsing.iclother.home.HomeActivity;
import stuido.tsing.iclother.utils.suscriber.ProgressSubscriber;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SignInFragment extends BaseFragment implements SignInContract.View {
    private SignInContract.Presenter loginPresenter;
    //    private CoordinatorLayout coordinatorLayout;
    public EditText _nameText = null;
    public EditText _passwordText = null;
    private Button _loginButton = null;
    private TextView _signupLink = null;
    private static SignInFragment instance;
    private static final int DELAY_TIME = 5;
    private ProgressSubscriber progressSubscriber;
    private static final String TAG = "SignInFragment";


    public static SignInFragment newInstance() {
        if (instance == null) {
            instance = new SignInFragment();
        }
        return instance;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        progressSubscriber = new ProgressSubscriber(__ -> {
            showToast(getResources().getString(R.string.login_success_hint));
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.app_name), Context.MODE_APPEND);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("loginState", true);
            edit.apply();
            getActivity().finish();
        }, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        loginPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        loginPresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressSubscriber.dismissProgressDialog();
    }

//    @Override
//    public void showSignInSuccess() {
//        showToast(getResources().getString(R.string.login_success_hint));
//    }
//
//    @Override
//    public void showSignInError() {
//        //因为某些原因登录失败，再次尝试登陆
//        Snackbar.make(coordinatorLayout, getString(R.string.login_error_message), Snackbar.LENGTH_LONG)
//                .setAction(R.string.action_login_again, __ ->
//                        loginPresenter.signIn(progressSubscriber)
//                ).setActionTextColor(getResources().getColor(R.color.snackbar_color)).show();
//    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        loginPresenter = checkNotNull(presenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_frag;
    }

    @Override
    protected void initView() {
        _nameText = mRootView.findViewById(R.id.input_email);
        _passwordText = mRootView.findViewById(R.id.input_password);
        _loginButton = mRootView.findViewById(R.id.btn_login);
        _signupLink = mRootView.findViewById(R.id.link_signup);
//        coordinatorLayout = mRootView.findViewById(R.id.snackbar_common);
    }

    @Override
    protected void initEvent() {
        _loginButton.setOnClickListener(__ ->
                        loginPresenter.signIn(progressSubscriber)
                // FIXME: 2017/7/26 若用户名或密码输入错误，无法再次输入
        );
        _signupLink.setOnClickListener(__ -> {
            switchContent(instance, new SignUpFragment(), R.id.sign_up_frag);
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty()) {
            _nameText.setError(getActivity().getString(R.string.name_enter_valid));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            _passwordText.setError(getActivity().getString(R.string.pwd_enter_valid));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

package stuido.tsing.iclother.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.sign.SignActivity;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/20.
 */

public class LoginFragment extends Fragment implements LoginContract.View {
    private LoginContract.Presenter loginPresenter;
    private CoordinatorLayout coordinatorLayout;
    private EditText _emailText = null;
    private EditText _passwordText = null;
    private Button _loginButton = null;
    private TextView _signupLink = null;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
    public void showLoginSuccess() {

    }

    @Override
    public void showLoginError() {
        //因为某些原因登录失败，再次尝试登陆
        Snackbar.make(coordinatorLayout, getString(R.string.login_error_message), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_login_again, __ ->
                        loginPresenter.login(_emailText, _passwordText, getActivity())
                ).show();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        loginPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_frag, container, false);
        _emailText = root.findViewById(R.id.input_email);
        _passwordText = root.findViewById(R.id.input_password);
        _loginButton = root.findViewById(R.id.btn_login);
        _signupLink = root.findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(__ -> loginPresenter.login(_emailText, _passwordText, getActivity()));

        _signupLink.setOnClickListener(__ -> {
            // Start the Signup activity
            Intent intent = new Intent(getActivity(), SignActivity.class);
//            startActivityForResult(intent, REQUEST_SIGNUP);
            startActivity(intent);
            //注册成功后直接调用登录，不在这里做返回处理
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
        return root;
    }
}

package com.npclo.imeasurer.account.signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountFragment;
import com.npclo.imeasurer.home.HomeActivity;
import com.npclo.imeasurer.utils.suscriber.ProgressSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SignInFragment extends AccountFragment implements SignInContract.View {
    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    Unbinder unbinder;
    private SignInContract.Presenter loginPresenter;
    private static SignInFragment instance;
    private static final int DELAY_TIME = 5;
    private ProgressSubscriber progressSubscriber;
    private static final String TAG = SignInFragment.class.getSimpleName();

    public static SignInFragment newInstance() {
        instance = new SignInFragment();
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
            edit.putString("id", __.toString());
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

    @Override
    public void showSignInSuccess() {
        showToast(getResources().getString(R.string.login_success_hint));
    }

    @Override
    public void showSignInError() {
        //因为某些原因登录失败，再次尝试登陆
        Snackbar.make(getView(), getString(R.string.login_error_message), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_login_again, measure ->
                        loginPresenter.signIn(progressSubscriber)
                ).setActionTextColor(getResources().getColor(R.color.snackbar_color)).show();
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        loginPresenter = checkNotNull(presenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sign_in_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void initEvent() {
//        _loginButton.setOnClickListener(__ ->
//                {
//                    if (!validate()) {
//                        return;
//                    }
//                    loginPresenter.signIn(progressSubscriber);
//                    // FIXME: 2017/7/26 若用户名或密码输入错误，无法再次输入
//                }
//        );
//        _signupLink.setOnClickListener(__ -> {
//                    SignUpFragment signUpFragment = SignUpFragment.newInstance();
//                    new SignUpPresenter(signUpFragment, SchedulerProvider.getInstance());
//                    switchContent(instance, signUpFragment, R.id.content_frame);
//                }
//        );
    }

    private boolean validate() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

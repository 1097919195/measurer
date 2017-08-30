package com.npclo.imeasurer.account.signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountFragment;
import com.npclo.imeasurer.account.signup.SignUpFragment;
import com.npclo.imeasurer.home.HomeActivity;
import com.npclo.imeasurer.utils.suscriber.ProgressSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SignInFragment extends AccountFragment implements SignInContract.View {
    Unbinder unbinder;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_eye)
    ImageView inputEye;
    @BindView(R.id.action_remember_pwd)
    CheckBox actionRememberPwd;
    @BindView(R.id.action_sign_in)
    AppCompatButton actionSignIn;
    @BindView(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    @BindView(R.id.signup_tv)
    TextView signupTv;
    @BindView(R.id.action_wechat_login)
    ImageView actionWechatLogin;
    @BindView(R.id.sign_in_frag)
    ScrollView signInFrag;

    private SignInContract.Presenter signinPresenter;
    private ProgressSubscriber progressSubscriber;
    private static final String TAG = SignInFragment.class.getSimpleName();
    private boolean pwd_label = true;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        signinPresenter.subscribe();
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
    public void onPause() {
        super.onPause();
        signinPresenter.unsubscribe();
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
                        signinPresenter.signIn(progressSubscriber)
                ).setActionTextColor(getResources().getColor(R.color.snackbar_color)).show();
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        signinPresenter = checkNotNull(presenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sign_in_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
    }

    private boolean validate() {
        boolean valid = true;

//        String name = _nameText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        if (name.isEmpty()) {
//            _nameText.setError(getActivity().getString(R.string.name_enter_valid));
//            valid = false;
//        } else {
//            _nameText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
//            _passwordText.setError(getActivity().getString(R.string.pwd_enter_valid));
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }

        return valid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.logo, R.id.input_eye, R.id.action_remember_pwd, R.id.action_sign_in, R.id.forget_pwd_tv, R.id.signup_tv, R.id.action_wechat_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.logo:
                break;
            case R.id.input_eye:
                if (pwd_label) {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwd_label = false;
                } else {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwd_label = true;
                }
                break;
            case R.id.action_remember_pwd:
                break;
            case R.id.action_sign_in:
                actionSignIn.setOnClickListener(__ -> {
                    if (!validate()) {
                        return;
                    }
                    signinPresenter.signIn(progressSubscriber);
//                    // FIXME: 2017/7/26 若用户名或密码输入错误，无法再次输入
                });
                break;
            case R.id.forget_pwd_tv:
                break;
            case R.id.signup_tv:
                SignUpFragment signUpFragment = SignUpFragment.newInstance();
                showHideFragment(signUpFragment, this);
                break;
            case R.id.action_wechat_login:
                showToast("待开放微信登录");
                break;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }
}
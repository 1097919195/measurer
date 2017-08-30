package com.npclo.imeasurer.account.signup;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/24.
 */

public class SignUpFragment extends AccountFragment implements SignUpContract.View {
    @BindView(R.id.input_mobile)
    EditText inputName;
    @BindView(R.id.input_password)
    EditText inputPassword;
    //    @BindView(R.id.input_reEnterPassword)
//    EditText inputReEnterPassword;
//    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    //    @BindView(R.id.sign_up_nav)
    Toolbar toolbar;

    private SignUpContract.Presenter signUpPresenter;
    private Unbinder bind;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sign_up_frag;
    }

    @Override
    protected void initView(View mRootView) {
        bind = ButterKnife.bind(this, this.mRootView);
        //注册页面返回导航栏
//        toolbar.setNavigationIcon(R.drawable.back);
//        toolbar.setNavigationOnClickListener(__ ->
//                switchContent(instance, SignInFragment.newInstance(), R.id.content_frame)
//        );
    }


//        linkLogin.setOnClickListener(__ ->
//                switchContent(instance, new SignInFragment(), R.id.content_frame)
//        );
//        btnSignup.setOnClickListener(__ -> {
//            if (!checkInputValid()) {
//                return;
//            }
//            signUpPresenter.signUp(userProgressSubscriber);
//        });

//    private boolean checkInputValid() {
//        Boolean flag = true;
//        String name = inputName.getText().toString();
//        String pwd1 = inputPassword.getText().toString();
//        String pwd2 = inputReEnterPassword.getText().toString();
//        if (TextUtils.isEmpty(name)) {
//            inputName.setError(getActivity().getString(R.string.name_enter_valid));
//            flag = false;
//        } else {
//            inputName.setError(null);
//        }
//
//        checkPwdInputValid(inputPassword, flag);
//        checkPwdInputValid(inputReEnterPassword, flag);
//        if (!pwd1.equals(pwd2)) inputReEnterPassword.setError(getString(R.string.pwd_not_same));
//        return flag;
//    }

//    private void checkPwdInputValid(EditText text, Boolean valid) {
//        String s = text.getText().toString();
//        if (TextUtils.isEmpty(s) || s.length() < 6 || s.length() > 20) {
//            text.setError(getActivity().getString(R.string.pwd_enter_valid));
//            valid = false;
//        } else {
//            text.setError(null);
//        }
//    }

    @Override
    public void showSignUpSuccess() {

    }

    @Override
    public void showSignUpError() {

    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        signUpPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

//    @Override
//    protected void afterCreate(Bundle savedInstanceState) {
//        userProgressSubscriber = new ProgressSubscriber(__ -> {
//            showToast(getResources().getString(R.string.register_success_hint));
//            Intent intent = new Intent(getActivity(), HomeActivity.class);
//            startActivity(intent);
//            SharedPreferences sharedPreferences = getActivity()
//                    .getSharedPreferences(getString(R.string.app_name), Context.MODE_APPEND);
//            SharedPreferences.Editor edit = sharedPreferences.edit();
//            edit.putBoolean("loginState", true);
//            edit.putString("id", __.toString());
//            edit.apply();
//            getActivity().finish();
//        }, getActivity());
//    }
}

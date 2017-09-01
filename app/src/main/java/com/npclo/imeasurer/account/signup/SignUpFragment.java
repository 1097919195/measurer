package com.npclo.imeasurer.account.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.account.signin.SignInFragment;
import com.npclo.imeasurer.account.signin.SignInPresenter;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.user.User;
import com.npclo.imeasurer.data.user.ValidCode;
import com.npclo.imeasurer.main.MainActivity;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SignUpFragment extends BaseFragment implements SignUpContract.View {

    @BindView(R.id.input_mobile)
    EditText inputMobile;
    @BindView(R.id.input_valid_code)
    EditText inputValidCode;
    @BindView(R.id.action_valid_code)
    AppCompatButton actionValidCode;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_eye)
    ImageView inputEye;
    @BindView(R.id.action_sign_up)
    AppCompatButton actionSignUp;
    @BindView(R.id.to_sign_in)
    LinearLayout toSignIn;
    private SignUpContract.Presenter signUpPresenter;
    private Unbinder bind;
    private String name;
    private String pwd;
    private boolean pwd_label = true;
    private MaterialDialog dialog;
    private String code;

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
    }

    private boolean checkInputValid() {
        Boolean flag = true;
        name = inputMobile.getText().toString();
        pwd = inputPassword.getText().toString();
        code = inputValidCode.getText().toString();
        if (TextUtils.isEmpty(name)) {
            inputMobile.setError(getActivity().getString(R.string.name_enter_valid));
            flag = false;
        }

        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 20) {
            inputPassword.setError(getActivity().getString(R.string.pwd_enter_valid));
            flag = false;
        }
        if (TextUtils.isEmpty(code) || code.length() != 6) {
            inputValidCode.setError(getActivity().getString(R.string.plz_valid_code));
            flag = false;
        }
        return flag;
    }

    @Override
    public void showSignUpSuccess(User user) {
        showToast(getResources().getString(R.string.register_success_hint));
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.app_name), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("loginState", true);
        edit.putString("id", user.get_id());
        edit.putString("name", user.getName());
        edit.putString("curr_times", user.getCurrTimes() + "");
        edit.putString("total_times", user.getTotalTimes() + "");
        edit.apply();
    }

    @Override
    public void showSignUpError(Throwable e) {
        handleError(e, TAG);
    }

    @Override
    public void completeSignUp() {
        showLoading(false);
    }

    @Override
    public void showLoading(boolean bool) {
        if (bool) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .progress(true, 100)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void showValidCodeSendSuccess(ValidCode code) {
        showToast(getString(R.string.valid_code_send_success) + code.getCode());
    }

    @Override
    public void showValidCodeSendError(Throwable e) {
        handleError(e, TAG);
    }

    @Override
    protected void handleError(Throwable e, String TAG) {
        super.handleError(e, TAG);
        dialog.dismiss();
    }

    @Override
    public void showCompleteGetValidCode() {
        showLoading(false);
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

    @OnClick({R.id.action_valid_code, R.id.input_eye, R.id.action_sign_up, R.id.to_sign_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_valid_code:
                String mobile = inputMobile.getText().toString();
                if (mobile.length() == 0 || mobile.length() != 11) {
                    // TODO: 2017/8/30 check mobile number valid
                    showToast(getString(R.string.plz_valid_mobile));
                    return;
                }
                signUpPresenter.getValidCode(mobile, "singup");
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
            case R.id.action_sign_up:
                if (!checkInputValid()) return;
                signUpPresenter.signUp(name, pwd, code);
                break;
            case R.id.to_sign_in:
                SignInFragment fragment = SignInFragment.newInstance();
                start(fragment, SINGLETASK);
                ((AccountActivity) getActivity()).setSignInPresenter(new SignInPresenter(fragment,
                        SchedulerProvider.getInstance()));
                break;
        }
    }
}

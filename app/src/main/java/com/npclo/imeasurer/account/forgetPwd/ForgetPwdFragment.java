package com.npclo.imeasurer.account.forgetPwd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.account.signin.SignInFragment;
import com.npclo.imeasurer.account.signin.SignInPresenter;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.ValidCode;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class ForgetPwdFragment extends BaseFragment implements ForgetPwdContract.View {
    private static String TAG = ForgetPwdFragment.class.getSimpleName();
    @BindView(R.id.base_toolbar_title)
    TextView baseToolbarTitle;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.input_mobile)
    EditText inputMobile;
    @BindView(R.id.input_valid_code)
    EditText inputValidCode;
    @BindView(R.id.action_valid_code)
    AppCompatButton actionValidCode;
    @BindView(R.id.input_new_pwd1)
    EditText inputNewPwd1;
    @BindView(R.id.input_new_pwd2)
    EditText inputNewPwd2;
    @BindView(R.id.action_submit)
    AppCompatButton actionResetPwd;
    Unbinder unbinder;
    private ForgetPwdContract.Presenter mPresenter;
    private MaterialDialog dialog;
    private String mobile;
    private String code;
    private String pwd1;

    public static ForgetPwdFragment newInstance() {
        return new ForgetPwdFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_forget_pwd;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        RxTextView
                .textChanges(inputMobile)
                .subscribe(sequence -> {
                    if (sequence.length() == 11) { // TODO: 2017/8/31 校验手机号格式等
                        actionValidCode.setEnabled(true);
                        actionValidCode.setBackground(getResources().getDrawable(R.drawable.btn_radius_solid_primary));
                        actionValidCode.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        actionValidCode.setEnabled(false);
                        actionValidCode.setBackground(getResources().getDrawable(R.drawable.btn_radius_border_primary));
                        actionValidCode.setTextColor(getResources().getColor(R.color.primary));
                    }
                });
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbarTitle.setText(getString(R.string.forget_pwd));
        //返回登录界面
        baseToolbar.setNavigationOnClickListener(__ -> {
            SignInFragment signInFragment = SignInFragment.newInstance();
            start(signInFragment, SINGLETASK);
            signInFragment.setPresenter(new SignInPresenter(signInFragment,
                    SchedulerProvider.getInstance()));
        });
    }

    @Override
    public void setPresenter(ForgetPwdContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.action_valid_code, R.id.action_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_valid_code:
                String s = inputMobile.getText().toString();
                mPresenter.getValidCode(s);
                break;
            case R.id.action_submit:
                if (checkInput()) return;
                mPresenter.resetPwd(mobile, pwd1, code);
                break;
        }
    }

    private boolean checkInput() {
        boolean flag = false;
        mobile = inputMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            showToast(getString(R.string.plz_valid_mobile));
            flag = true;
        }
        code = inputValidCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.plz_valid_code));
            flag = true;
        }
        pwd1 = inputNewPwd1.getText().toString();
        if (TextUtils.isEmpty(pwd1)) {
            showToast(getString(R.string.plz_valid_new_pwd));
            flag = true;
        }
        String pwd2 = inputNewPwd2.getText().toString();
        if (TextUtils.isEmpty(pwd2)) {
            showToast(getString(R.string.plz_valid_new_pwd2));
            flag = true;
        }
        if (!pwd1.equals(pwd2)) {
            showToast(getString(R.string.plz_same_pwd));
            flag = true;
        }
        return flag;
    }

    @Override
    public void showLoading(boolean b) {
        if (b) {
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
    public void showResetPwdSuccess() {
        //att 重置密码成功，需要重新登录
        showToast(getString(R.string.reset_pwd_success));
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.app_name), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("loginState", false);
        edit.apply();
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void showResetPwdError(Throwable e) {
        handleError(e, TAG);
        dialog.dismiss();
    }

    @Override
    public void showResetPwdComplete() {
        showLoading(false);
    }
}
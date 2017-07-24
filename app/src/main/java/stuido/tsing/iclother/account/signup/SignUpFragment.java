package stuido.tsing.iclother.account.signup;

import stuido.tsing.iclother.base.BaseFragment;

/**
 * Created by Endless on 2017/7/24.
 */

public class SignUpFragment extends BaseFragment implements SignUpContract.view {
    private static SignUpFragment instance;
    private SignUpContract.presenter signUpPresenter;

    public static SignUpFragment newInstance() {
        if (instance == null) {
            instance = new SignUpFragment();
        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showSignUpSuccess() {

    }

    @Override
    public void showSignUpError() {

    }

    @Override
    public void setPresenter(SignUpContract.presenter presenter) {
        signUpPresenter = presenter;
    }
}

package com.npclo.imeasurer.user.manage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.user.home.HomeFragment;
import com.npclo.imeasurer.user.home.HomePresenter;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class ManageFragment extends BaseFragment implements ManageContract.View {
    private static final String TAG = ManageFragment.class.getSimpleName();
    @BindView(R.id.base_toolbar_title)
    TextView baseToolbarTitle;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.input_old_pwd)
    AppCompatEditText inputOldPwd;
    @BindView(R.id.input_new_pwd1)
    AppCompatEditText inputNewPwd1;
    @BindView(R.id.input_new_pwd2)
    AppCompatEditText inputNewPwd2;
    @BindView(R.id.action_submit)
    AppCompatButton actionResetPwd;
    Unbinder unbinder;
    @NonNull
    private ManageContract.Presenter presenter;
    private MaterialDialog dialog;

    @Override
    public void setPresenter(@NonNull ManageContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_manage;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbarTitle.setText(getString(R.string.edit_pwd));
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(__ -> {
            HomeFragment homeFragment = HomeFragment.newInstance();
            start(homeFragment, SINGLETASK);
            homeFragment.setPresenter(new HomePresenter(
                    RxBleClient.create(getActivity()),
                    homeFragment,
                    SchedulerProvider.getInstance()));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.action_submit)
    public void onViewClicked() {
        String old = inputOldPwd.getText().toString();
        if (checkInput(old)) {
            showSnackbar("旧密码格式不正确");
            return;
        }
        String newpwd1 = inputNewPwd1.getText().toString();
        if (checkInput(newpwd1)) {
            showSnackbar("新密码格式不正确");
            return;
        }
        String newpwd2 = inputNewPwd2.getText().toString();
        if (checkInput(newpwd2)) {
            showSnackbar("再次输入密码格式不正确");
            return;
        }
        if (!newpwd1.equals(newpwd2)) {
            showSnackbar("新输入密码两次不一致，请检查");
            return;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if (TextUtils.isEmpty(id)) {
            showToast("账号异常，请重新登录");
            startActivity(new Intent(getActivity(), AccountActivity.class));
            return;
        }
        presenter.resetPwd(id, old, newpwd1);
    }

    private boolean checkInput(String field) {
        if (TextUtils.isEmpty(field)) return true;
        if (field.length() < 6) return true;
        return false;
    }


    public static ManageFragment newInstance() {
        return new ManageFragment();
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
    public void showEditSuccess() {
        showLoading(false);
        showSnackbar("修改成功");
    }

    @Override
    public void showEditError(Throwable e) {
        showLoading(false);
        handleError(e, TAG);
    }

    @Override
    public void showEditCompleted() {
        showLoading(false);
    }
}
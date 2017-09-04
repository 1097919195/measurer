package com.npclo.imeasurer.user.manage;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.user.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ManageFragment extends BaseFragment implements ManageContract.View {
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

    @Override
    public void setPresenter(ManageContract.Presenter presenter) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.manage_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbarTitle.setText(getString(R.string.edit_pwd));
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(__ -> {
            HomeFragment homeFragment = HomeFragment.newInstance();
            start(homeFragment, SINGLETASK);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.action_submit)
    public void onViewClicked() {
    }

    public static ManageFragment newInstance() {
        return new ManageFragment();
    }
}
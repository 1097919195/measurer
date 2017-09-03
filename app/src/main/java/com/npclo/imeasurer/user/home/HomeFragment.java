package com.npclo.imeasurer.user.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_APPEND;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    HomeContract.Presenter mPresenter;
    @BindView(R.id.action_logout)
    AppCompatButton actionLogout;
    Unbinder unbinder;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    Unbinder unbinder1;

    public HomeFragment() {
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(__ -> startActivity(new Intent(getActivity(), MainActivity.class)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.action_logout)
    public void onViewClicked() {
        mPresenter.logout();
    }

    @Override
    public void logout() {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(getString(R.string.app_name), MODE_APPEND).edit();
        edit.putBoolean("loginState", false);
        edit.putString("id", null);
        edit.apply();
        Toast.makeText(getActivity(), "login out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
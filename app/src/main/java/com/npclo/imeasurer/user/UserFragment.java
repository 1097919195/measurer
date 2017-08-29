package com.npclo.imeasurer.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_APPEND;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class UserFragment extends Fragment implements UserContract.View {
    UserContract.Presenter mPresenter;
    @BindView(R.id.action_logout)
    AppCompatButton actionLogout;
    Unbinder unbinder;

    public UserFragment() {
    }

    public static UserFragment getInstance() {
        return new UserFragment();
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_center_frag, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
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
        Toast.makeText(getActivity(), "login out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }
}
package com.npclo.imeasurer.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.main.home.HomeFragment;
import com.npclo.imeasurer.main.home.HomePresenter;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Endless on 2017/9/1.
 */

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
        initView();
        init();
    }

    private void init() {
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .request();
        //加载登录后的欢迎界面
        HomeFragment homeFragment = findFragment(HomeFragment.class);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            loadRootFragment(R.id.content_frame, homeFragment);
            new HomePresenter(homeFragment, SchedulerProvider.getInstance());
        }
    }

    private void checkLogin() {
        SharedPreferences loginState = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean isLogin = loginState.getBoolean("loginState", false);
        String id = loginState.getString("id", null);

        if (!isLogin && TextUtils.isEmpty(id)) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
    }

    protected void initView() {
        setContentView(R.layout.act_main);
    }
}
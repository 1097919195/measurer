package com.npclo.imeasurer.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.main.home.HomeFragment;
import com.npclo.imeasurer.main.home.HomePresenter;
import com.npclo.imeasurer.utils.BleClientHelper;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleClient;

import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Endless on 2017/9/1.
 */

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RxBleClient bleClient;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        bleClient = BleClientHelper.getInstance(this);
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

    protected void initView() {
        setContentView(R.layout.main_act);
    }
}
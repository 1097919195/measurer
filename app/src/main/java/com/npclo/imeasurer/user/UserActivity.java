package com.npclo.imeasurer.user;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.user.home.HomeFragment;
import com.npclo.imeasurer.user.home.HomePresenter;
import com.npclo.imeasurer.utils.BleClientHelper;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleClient;

public class UserActivity extends BaseActivity {

    private RxBleClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        init();
    }

    private void init() {
        client = BleClientHelper.getInstance(this);
        //加载登录后的欢迎界面
        HomeFragment homeFragment = findFragment(HomeFragment.class);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            loadRootFragment(R.id.content_frame, homeFragment);
            new HomePresenter(client, homeFragment, SchedulerProvider.getInstance());
        }
    }

    protected void initView() {
        setContentView(R.layout.main_act);
    }
}
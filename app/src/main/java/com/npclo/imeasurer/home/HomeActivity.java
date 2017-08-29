package com.npclo.imeasurer.home;

import android.content.Intent;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.data.measure.remote.MeasurementRemoteDataSource;
import com.npclo.imeasurer.measure.MeasureActivity;
import com.npclo.imeasurer.utils.ActivityUtils;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;


public class HomeActivity extends BaseActivity {
    HomePresenter presenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.main_act_title);
    }

    @Override
    protected void initFragment() {
        HomeFragment homeFragment =
                (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.act_content_view);
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, R.id.act_content_view);
        }

        // Create the presenter
        presenter = new HomePresenter(MeasurementRemoteDataSource.getInstance(),
                homeFragment, SchedulerProvider.getInstance());
        // 恢复数据，如果有的话
    }

    @Override
    protected void actionMenuClickEvent() {
        Intent intent = new Intent(HomeActivity.this, MeasureActivity.class);
        startActivity(intent);
    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return getString(R.string.main_act_action_menu_title);
    }
}
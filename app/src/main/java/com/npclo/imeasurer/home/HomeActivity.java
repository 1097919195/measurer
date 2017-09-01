package com.npclo.imeasurer.home;

import android.content.Intent;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.measure.MeasureActivity;


public class HomeActivity extends BaseActivity {
    HomePresenter presenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.main_act_title);
    }

    @Override
    protected void initFragment() {
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
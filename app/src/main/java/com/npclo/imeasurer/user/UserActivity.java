package com.npclo.imeasurer.user;

//TODO 展示用户所有测量的数据

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.utils.ActivityUtils;

public class UserActivity extends BaseActivity {
    UserPresenter userPresenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.user_center);
    }

    @Override
    protected void init() {
        UserFragment userFragment = (UserFragment) getSupportFragmentManager().findFragmentById(R.id.content_view);
        if (userFragment == null) {
            userFragment = UserFragment.getInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), userFragment, R.id.content_view);
        }
        userPresenter = new UserPresenter(userFragment);
    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return null;
    }
}

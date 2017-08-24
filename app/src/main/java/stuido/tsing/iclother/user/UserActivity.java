package stuido.tsing.iclother.user;

//TODO 展示用户所有测量的数据

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.base.BaseActivity;
import stuido.tsing.iclother.utils.ActivityUtils;

public class UserActivity extends BaseActivity {
    UserPresenter userPresenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.user_center);
    }

    @Override
    protected void initFragment() {
        UserFragment userFragment = (UserFragment) getSupportFragmentManager().findFragmentById(R.id.act_content_view);
        if (userFragment == null) {
            userFragment = UserFragment.getInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), userFragment, R.id.act_content_view);
        }
        userPresenter = new UserPresenter(userFragment);
    }

    @Override
    protected void actionMenuClickEvent() {

    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return null;
    }
}

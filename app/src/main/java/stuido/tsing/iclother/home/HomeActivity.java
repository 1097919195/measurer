package stuido.tsing.iclother.home;

import android.content.Intent;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.base.BaseActivity;
import stuido.tsing.iclother.data.measure.remote.MeasurementRemoteDataSource;
import stuido.tsing.iclother.measure.MeasureActivity;
import stuido.tsing.iclother.utils.ActivityUtils;
import stuido.tsing.iclother.utils.schedulers.SchedulerProvider;

public class HomeActivity extends BaseActivity {
    HomePresenter presenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.main_act_title);
    }

    @Override
    protected void initEvent() {
        HomeFragment homeFragment =
                (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.act_content_view);
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, R.id.act_content_view);
        }

        // Create the presenter
        // 恢复数据，如果有的话
        presenter = new HomePresenter(MeasurementRemoteDataSource.getInstance(),
                homeFragment, SchedulerProvider.getInstance());
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
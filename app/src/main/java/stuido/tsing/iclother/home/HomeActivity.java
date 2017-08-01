package stuido.tsing.iclother.home;

import android.widget.Toast;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.base.BaseActivity;
import stuido.tsing.iclother.data.measure.MeasurementRepository;
import stuido.tsing.iclother.data.measure.local.MeasurementLocalDataSource;
import stuido.tsing.iclother.data.measure.remote.MeasurementRemoteDataSource;
import stuido.tsing.iclother.utils.ActivityUtils;
import stuido.tsing.iclother.utils.schedulers.SchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeActivity extends BaseActivity {
    HomePresenter presenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.main_act_title);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.main_act);
//        act_content_view.addView();
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
        presenter = new HomePresenter(
                MeasurementRepository.getInstance(MeasurementRemoteDataSource.getInstance(),
                        MeasurementLocalDataSource.getInstance(checkNotNull(this), SchedulerProvider.getInstance())),
                homeFragment,
                SchedulerProvider.getInstance());
        // 恢复数据，如果有的话
    }

    @Override
    protected void actionMenuClickEvent() {
        Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return getString(R.string.main_act_action_menu_title);
    }
}
package stuido.tsing.iclother.measure;

import com.polidea.rxandroidble.RxBleClient;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.base.BaseActivity;
import stuido.tsing.iclother.utils.ActivityUtils;
import stuido.tsing.iclother.utils.BLEclientHelper;
import stuido.tsing.iclother.utils.schedulers.SchedulerProvider;

/**
 * Created by Endless on 2017/7/19.
 */

public class MeasureActivity extends BaseActivity {
    public static final int REQUEST_ADD_MEASUREMENT = 1;
    private RxBleClient rxBleClient;
    private MeasurePresenter mPresenter;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.measure_act_title);
    }

    @Override
    protected void initEvent() {
        rxBleClient = BLEclientHelper.getInstance(MeasureActivity.this);
        MeasureFragment measure_fragment = (MeasureFragment) getSupportFragmentManager()
                .findFragmentById(R.id.act_content_view);
        if (measure_fragment == null) {
            measure_fragment = MeasureFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), measure_fragment, R.id.act_content_view);
        }
        mPresenter = new MeasurePresenter(rxBleClient, measure_fragment, SchedulerProvider.getInstance());
    }

    @Override
    protected void actionMenuClickEvent() {

    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return null;
    }
}
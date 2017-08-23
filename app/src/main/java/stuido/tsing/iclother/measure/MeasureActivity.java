package stuido.tsing.iclother.measure;

import android.Manifest;

import com.polidea.rxandroidble.RxBleClient;

import kr.co.namee.permissiongen.PermissionGen;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.base.BaseActivity;
import stuido.tsing.iclother.utils.ActivityUtils;
import stuido.tsing.iclother.utils.BleClientHelper;
import stuido.tsing.iclother.utils.schedulers.SchedulerProvider;

/**
 * Created by Endless on 2017/7/19.
 */

public class MeasureActivity extends BaseActivity {
    public static final int REQUEST_ADD_MEASUREMENT = 1;
    private MeasurePresenter mPresenter;
    private RxBleClient rxBleClient;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.measure_act_title);
    }

    @Override
    protected void initFragment() {
        rxBleClient = BleClientHelper.getInstance(MeasureActivity.this);
        MeasureFragment measure_fragment = (MeasureFragment) getSupportFragmentManager()
                .findFragmentById(R.id.act_content_view);

        if (measure_fragment == null) {
            measure_fragment = MeasureFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), measure_fragment, R.id.act_content_view);
        }

        mPresenter = new MeasurePresenter(rxBleClient, measure_fragment, SchedulerProvider.getInstance());
        PermissionGen.with(MeasureActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .request();
    }


    @Override
    protected void actionMenuClickEvent() {

    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return null;
    }
}
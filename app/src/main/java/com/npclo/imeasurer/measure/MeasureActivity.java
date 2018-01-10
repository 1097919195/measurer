package com.npclo.imeasurer.measure;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleDevice;

import java.util.UUID;

/**
 * @author Endless
 * @date 10/12/2017
 */

public class MeasureActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        MeasureFragment measureFragment = findFragment(MeasureFragment.class);
        if (measureFragment == null) {
            measureFragment = MeasureFragment.newInstance();
            loadRootFragment(R.id.content_frame, measureFragment);
            SharedPreferences preferences = getSharedPreferences(getString(R.string.app_config),
                    Context.MODE_PRIVATE);
            int offsetMeasure = preferences.getInt("measure_offset", 14);
            String address = preferences.getString("mac_address", null);
            String uuid = preferences.getString("uuid", null);
            if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(uuid)) {
                RxBleDevice device = BaseApplication.getRxBleClient(this).getBleDevice(address);
                new MeasurePresenter(measureFragment, SchedulerProvider.getInstance(), offsetMeasure,
                        address, device, UUID.fromString(uuid));
            } else {
                Toast.makeText(this, "未连接蓝牙设备", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

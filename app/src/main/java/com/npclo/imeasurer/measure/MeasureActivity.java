package com.npclo.imeasurer.measure;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.DecodeHintType;
import com.google.zxing.client.android.decode.CaptureActivity;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.utils.ActivityUtils;
import com.npclo.imeasurer.utils.BleClientHelper;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleClient;

import java.util.EnumMap;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Endless on 2017/7/19.
 */

public class MeasureActivity extends BaseActivity {
    public static final int REQUEST_ADD_MEASUREMENT = 1;
    private static final int REQUEST_CODE = 1001;
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
        Intent intent = new Intent(MeasureActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return getString(R.string.scan_qrcode);
    }

    public final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Toast.makeText(MeasureActivity.this, scanResult, Toast.LENGTH_LONG).show();
        }
    }
}
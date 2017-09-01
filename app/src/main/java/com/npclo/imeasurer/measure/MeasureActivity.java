package com.npclo.imeasurer.measure;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.utils.BleClientHelper;
import com.polidea.rxandroidble.RxBleClient;

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
    protected void init() {
        rxBleClient = BleClientHelper.getInstance(MeasureActivity.this);


    }


    @Override
    protected CharSequence getActionMenuTitle() {
        return getString(R.string.scan_qrcode);
    }


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
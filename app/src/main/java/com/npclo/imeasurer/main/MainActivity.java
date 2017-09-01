package com.npclo.imeasurer.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.client.android.decode.CaptureActivity;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.utils.BleClientHelper;
import com.polidea.rxandroidble.RxBleClient;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Endless on 2017/9/1.
 */

public class MainActivity extends BaseActivity {

    private RxBleClient bleClient;

    @Override
    protected CharSequence getToolbarTitle() {
        return null;
    }

    @Override
    protected void init() {
        bleClient = BleClientHelper.getInstance(this);
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .request();
        //加载登录后的欢迎界面
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivity(intent);
    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        String result = bundle.getString("result");
        switch (resultCode) {
            case RESULT_OK:
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(MainActivity.this, result + " enter", Toast.LENGTH_SHORT).show();
        }
    }
}

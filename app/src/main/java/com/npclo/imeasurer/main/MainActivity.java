package com.npclo.imeasurer.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.main.home.HomeFragment;
import com.npclo.imeasurer.main.home.HomePresenter;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Endless on 2017/9/1.
 */

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SCAN_HINT = 1001;
    private static final int CODE_HINT = 1002;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        init();
    }

    private void init() {
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission_group.LOCATION,
                        Manifest.permission_group.STORAGE,
                        Manifest.permission_group.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .request();
        //加载登录后的欢迎界面
        HomeFragment homeFragment = findFragment(HomeFragment.class);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            loadRootFragment(R.id.content_frame, homeFragment);
            HomePresenter mPresenter = new HomePresenter(homeFragment, SchedulerProvider.getInstance());
            homeFragment.setPresenter(mPresenter);
        }
    }

    protected void initView() {
        setContentView(R.layout.act_main);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        String result = null;
//        try {
//            Bundle bundle = data.getExtras();
//            result = bundle.getString("result");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        switch (resultCode) {
//            case SCAN_HINT:
//                if (result != null) {
//                    mPresenter.getUserInfoWithOpenID(result);
//                } else {
//                    Toast.makeText(MainActivity.this, getString(R.string.scan_qrcode_failed), Toast.LENGTH_LONG).show();
//                }
//                break;
//            case CODE_HINT:
//                if (result != null) {
//                    mPresenter.getUserInfoWithCode(result);
//                } else {
//                    Toast.makeText(MainActivity.this, getString(R.string.enter_qrcode_error), Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }
}
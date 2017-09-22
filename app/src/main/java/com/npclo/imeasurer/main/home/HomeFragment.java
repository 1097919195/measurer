package com.npclo.imeasurer.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.camera.CaptureActivity;
import com.npclo.imeasurer.data.app.App;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.main.measure.MeasureFragment;
import com.npclo.imeasurer.main.measure.MeasurePresenter;
import com.npclo.imeasurer.user.home.HomePresenter;
import com.npclo.imeasurer.utils.LogUtils;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    public static final int REQUEST_CODE = 1001;
    @BindView(R.id.scan_img)
    ImageView scanImg;
    @BindView(R.id.scan_hint)
    TextView scanHint;
    @BindView(R.id.base_toolbar)
    Toolbar toolbarBase;
    @BindView(R.id.ble_state)
    TextView bleState;
    Unbinder unbinder;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeContract.Presenter mPresenter;
    private MaterialDialog dialog;
    private static final int SCAN_HINT = 1001;
    private static final int CODE_HINT = 1002;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_home;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        initToolbar();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.scan_img, R.id.scan_hint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_img:
            case R.id.scan_hint:
                startScan();
                break;
        }
    }

    private void startScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String result = null;
//        try {
//            Bundle bundle = data.getExtras();
//            result = bundle.getString("result");
//        } catch (Exception e) {
//            Log.e(TAG, "===============fragment未接收到数据==================");
//            e.printStackTrace();
//        }
//        Log.e(TAG, "fragment传输过来的数据：" + result);
//        switch (resultCode) {
//            case SCAN_HINT:
//                mPresenter.getUserInfoWithOpenID(result);
//                break;
//            case CODE_HINT:
//                mPresenter.getUserInfoWithCode(result);
//                break;
//        }
//    }

    /**
     * 初始化toolbar的一些默认属性
     */
    protected void initToolbar() {
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.toolbar_text));//设置主标题颜色
        toolbarBase.inflateMenu(R.menu.base_toolbar_menu);
        toolbarBase.getMenu().getItem(0).setOnMenuItemClickListener(__ -> {
            com.npclo.imeasurer.user.home.HomeFragment fragment = com.npclo.imeasurer.user.home.HomeFragment.newInstance();
            start(fragment);
            fragment.setPresenter(new HomePresenter(RxBleClient.create(getActivity()), fragment, SchedulerProvider.getInstance()));
            return true;
        });
        bleState.setOnClickListener(__ -> {
            com.npclo.imeasurer.user.home.HomeFragment fragment = com.npclo.imeasurer.user.home.HomeFragment.newInstance();
            start(fragment);
            fragment.setPresenter(new HomePresenter(RxBleClient.create(getActivity()), fragment, SchedulerProvider.getInstance()));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        RxBleDevice rxBleDevice = BaseApplication.getRxBleDevice(getActivity());
        if (rxBleDevice != null && rxBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
            Log.e(TAG, "获取到蓝牙状态" + rxBleDevice.toString());
            bleState.setText(getString(R.string.connected));
            bleState.setTextColor(getResources().getColor(R.color.green));
            bleState.setEnabled(false);
        }
        if (BaseApplication.getFirstCheckHint(getActivity())) {
            mPresenter.getLatestVersion();
            BaseApplication.setIsFirstCheck(getActivity());
        }
        LogUtils.upload(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showLoading(boolean b) {
        if (b) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .progress(true, 100)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void showGetInfoSuccess(WechatUser user) {
        showLoading(false);
        MeasureFragment measureFragment = findFragment(MeasureFragment.class);
        if (measureFragment == null) {
            measureFragment = MeasureFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            measureFragment.setArguments(bundle);
            measureFragment.setPresenter(new MeasurePresenter(measureFragment, SchedulerProvider.getInstance()));
            start(measureFragment, SINGLETASK);
        }
    }

    @Override
    public void showGetInfoError(Throwable e) {
        showLoading(false);
        handleError(e, TAG);
    }

    @Override
    public void showCompleteGetInfo() {
        showLoading(false);
    }


    @Override
    public void showGetVersionSuccess(App app) {
        if (app.getCode() > getVersionCode()) updateApp(app);
    }

    @Override
    public void showGetVersionError(Throwable e) {
        handleError(e, TAG);
    }
}
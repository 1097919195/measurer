package com.npclo.imeasurer.user.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.app.App;
import com.npclo.imeasurer.data.ble.BleDevice;
import com.npclo.imeasurer.main.home.HomePresenter;
import com.npclo.imeasurer.user.contact.ContactFragment;
import com.npclo.imeasurer.user.feedback.FeedbackFragment;
import com.npclo.imeasurer.user.manage.ManageFragment;
import com.npclo.imeasurer.user.manage.ManagePresenter;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_APPEND;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author Endless
 */
public class HomeFragment extends BaseFragment implements HomeContract.View {
    HomeContract.Presenter mPresenter;
    @BindView(R.id.action_logout)
    AppCompatButton actionLogout;
    Unbinder unbinder;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.device_state)
    TextView deviceState;
    @BindView(R.id.action_connect)
    ImageView actionConnect;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.curr_times)
    TextView currTimes;
    @BindView(R.id.total_times)
    TextView totalTimes;
    @BindView(R.id.action_update)
    ImageView actionUpdate;
    @BindView(R.id.action_help)
    ImageView actionHelp;
    @BindView(R.id.action_feedback)
    ImageView actionFeedback;
    @BindView(R.id.action_contact)
    ImageView actionContact;
    private List<BleDevice> bleDeviceList = new ArrayList<>();
    private List<String> rxBleDeviceAddressList = new ArrayList<>();
    private ScanResultsAdapter scanResultsAdapter;
    private MaterialDialog.Builder scanResultDialog;
    private MaterialDialog connectingProgressBar;
    private MaterialDialog scanningProgressBar;
    private SpeechSynthesizer speechSynthesizer;
    private MaterialDialog resultDialog;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_user;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(c -> {
            com.npclo.imeasurer.main.home.HomeFragment fragment = com.npclo.imeasurer.main.home.HomeFragment.newInstance();
            start(fragment);
            fragment.setPresenter(new HomePresenter(fragment, SchedulerProvider.getInstance()));
        });
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_APPEND);
        currTimes.setText(preferences.getString("currTimes", "N/A"));
        totalTimes.setText(preferences.getString("totalTimes", "N/A"));
        String name = preferences.getString("name", "N/A");
        String nickname = preferences.getString("nickname", "");
        userName.setText(!TextUtils.isEmpty(nickname) ? nickname : name);

        configureResultList();
    }

    private void configureResultList() {
        scanResultsAdapter = new ScanResultsAdapter(this, bleDeviceList);
        scanResultDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_device_prompt)
                .backgroundColor(getResources().getColor(R.color.white))
                .titleColor(getResources().getColor(R.color.scan_result_list_title))
                .dividerColor(getResources().getColor(R.color.divider))
                .adapter(scanResultsAdapter, null);
        ;
        //选择目的蓝牙设备
        scanResultsAdapter.setOnAdapterItemClickListener(v -> {
                    // FIXME: 2017/8/23 点击所选择的蓝牙设备后，蓝牙设备对话框关闭
                    String s = ((TextView) v.findViewById(R.id.txt_mac)).getText().toString();
                    mPresenter.connectDevice(s);
                }
        );
    }

    private void initSpeech() {
        String APPKEY = "hhzjkm3l5akcz5oiflyzmmmitzrhmsfd73lyl3y2";
        String APPSECRET = "29aa998c451d64d9334269546a4021b8";
        if (speechSynthesizer == null) {
            speechSynthesizer = new SpeechSynthesizer(getActivity(), APPKEY, APPSECRET);
        }
        speechSynthesizer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_NET);
        speechSynthesizer.setTTSListener(new SpeechSynthesizerListener() {
            @Override
            public void onEvent(int i) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
        speechSynthesizer.init(null);// FIXME: 2017/8/24 语音播报需要联网
    }

    @Override
    public void onResume() {
        super.onResume();
        initSpeech();

        String macAddress = BaseApplication.getMacAddress(getActivity());
        if (macAddress != null) {
            RxBleDevice rxBleDevice = BaseApplication.getRxBleClient(getActivity()).getBleDevice(macAddress);
            if (rxBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
                updateDeviceState();
            }
        }
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
        //att 处理版本更新提示
        if (BaseApplication.canUpdate(getActivity())) {
            actionConnect.setEnabled(true);
            appVersion.setEnabled(true);
            appVersion.setText(getString(R.string.prompt_can_update));
            appVersion.setTextColor(getResources().getColor(R.color.primary));
        } else {
            actionConnect.setEnabled(false);
            appVersion.setEnabled(false);
            appVersion.setText(getString(R.string.prompt_latest_version));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.action_logout)
    public void onViewClicked() {
        new MaterialDialog.Builder(getActivity())
                .title("确定要退出登录？")
                .titleColor(getResources().getColor(R.color.ff5001))
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .backgroundColor(getResources().getColor(R.color.white))
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    mPresenter.logout();
                })
                .show();
    }

    @Override
    public void logout() {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(getString(R.string.app_name), MODE_APPEND).edit();
        edit.putBoolean("loginState", false);
        edit.putString("id", "");
        edit.apply();
        Toast.makeText(getActivity(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.action_user_manage, R.id.action_connect, R.id.action_update, R.id.action_help,
            R.id.action_feedback, R.id.action_contact, R.id.device_state, R.id.app_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_user_manage:
                ManageFragment manageFragment = ManageFragment.newInstance();
                start(manageFragment, SINGLETASK);
                manageFragment.setPresenter(new ManagePresenter(manageFragment, SchedulerProvider.getInstance()));
                break;
            case R.id.action_connect:
            case R.id.device_state:
                mPresenter.startScan();
                break;
            case R.id.action_update:
            case R.id.app_version:
                mPresenter.checkVersion();
                break;
            case R.id.action_help:
                showToast(getString(R.string.prompt_app_help), Toast.LENGTH_LONG);
                break;
            case R.id.action_feedback:
                FeedbackFragment feedbackFragment = FeedbackFragment.newInstance();
                start(feedbackFragment, SINGLETASK);
                break;
            case R.id.action_contact:
                ContactFragment contactFragment = ContactFragment.newInstance();
                start(contactFragment, SINGLETASK);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleBleScanException(BleScanException bleScanException) {
        switch (bleScanException.getReason()) {
            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                showToast(getString(R.string.bluetooth_not_avavilable));
                break;
            case BleScanException.BLUETOOTH_DISABLED:
                showToast(getString(R.string.bluetooth_disabled));
                break;
            case BleScanException.LOCATION_PERMISSION_MISSING:
                showToast("未授予获取位置权限");
                break;
            case BleScanException.LOCATION_SERVICES_DISABLED:
                showToast("6.0以上手机需要开启位置服务");
                break;
            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                showToast("Scan with the same filters is already started");
                break;
            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                showToast("Failed to register application for bluetooth scan");
                break;
            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                showToast("Scan with specified parameters is not supported");
                break;
            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                showToast("Scan failed due to internal error");
                break;
            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                showToast("Scan cannot start due to limited hardware resources");
                break;
            case BleScanException.UNKNOWN_ERROR_CODE:
            case BleScanException.BLUETOOTH_CANNOT_START:
            default:
                showToast("不能够扫描外接蓝牙设备");
                break;
        }
    }

    @Override
    public void handleScanResult(ScanResult result) {
        if (scanningProgressBar != null) {
            scanningProgressBar.dismiss();
            scanningProgressBar = null;
        }
        RxBleDevice device = result.getBleDevice();
        if (resultDialog == null) {
            resultDialog = scanResultDialog.show();
        }
        if (!resultDialog.isShowing()) {
            resultDialog.show();
        }
        if (!rxBleDeviceAddressList.contains(device.getMacAddress())) {
            rxBleDeviceAddressList.add(device.getMacAddress());
            bleDeviceList.add(new BleDevice(device.getName(), device.getMacAddress(), result.getRssi()));
            scanResultsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String s) {
        showToast(s);
    }

    @Override
    public void showError() {
        showToast(getString(R.string.unKnownError));
    }

    @Override
    public void showConnected(RxBleDevice bleDevice) {
        connectingProgressBar.dismiss();
        updateDeviceState();
        showToast(getString(R.string.device_connected));
        BaseApplication.setBleAddress(getActivity(), bleDevice.getMacAddress());
        speechSynthesizer.playText("蓝牙连接成功");
    }

    private void updateDeviceState() {
        deviceState.setText(getString(R.string.prompt_connected));
        deviceState.setTextColor(getResources().getColor(R.color.primary));
        deviceState.setEnabled(false);
        actionConnect.setEnabled(false);
    }

    @Override
    public void isConnecting() {
        connectingProgressBar = new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.connecting))
                .titleColor(getResources().getColor(R.color.ff5001))
                .backgroundColor(getResources().getColor(R.color.white))
                .progress(true, 100)
                .show();
    }

    @Override
    public void setLoadingIndicator(boolean bool) {

    }

    @Override
    public void showScanning() {
        scanningProgressBar = new MaterialDialog.Builder(getActivity())
                .backgroundColor(getResources().getColor(R.color.white))
                .progress(true, 100)
                .show();
    }

    @Override
    public void closeScanResultDialog() {
        try {
            if (resultDialog != null || resultDialog.isShowing()) {
                resultDialog.dismiss();
                resultDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showGetVersionSuccess(App app) {
        int code = getVersionCode();
        if (app.getCode() > code && code != 0) {
            updateApp(app);
        } else {
            showToast(getString(R.string.prompt_latest_version));
        }
    }

    @Override
    public void showGetVersionError(Throwable e) {
        handleError(e);
    }

    @Override
    public void setNotificationUUID(UUID characteristicUUID) {
        BaseApplication.setNotificationUUID(getActivity(), characteristicUUID);
    }

    @Override
    public void setBleAddress(String macAddress) {
        BaseApplication.setBleAddress(getActivity(), macAddress);
    }
}
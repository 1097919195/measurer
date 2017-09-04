package com.npclo.imeasurer.user.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.ble.BleDevice;
import com.npclo.imeasurer.main.MainActivity;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_APPEND;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    HomeContract.Presenter mPresenter;
    @BindView(R.id.action_logout)
    AppCompatButton actionLogout;
    Unbinder unbinder;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.device_state)
    TextView deviceState;
    @BindView(R.id.app_version)
    TextView appVersion;
    private List<BleDevice> bleDeviceList = new ArrayList<>();
    private List<String> rxBleDeviceAddressList = new ArrayList<>();
    private ScanResultsAdapter scanResultsAdapter;
    private boolean showDialogLabel = true;
    private MaterialDialog.Builder scanResultDialog;
    private MaterialDialog connectingProgressBar;
    private MaterialDialog scanningProgressBar;
    private SpeechSynthesizer speechSynthesizer;

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
        return R.layout.user_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(__ -> startActivity(new Intent(getActivity(), MainActivity.class)));
    }

    private void initSpeech() {
        String APPKEY = "hhzjkm3l5akcz5oiflyzmmmitzrhmsfd73lyl3y2";
        String APPSECRET = "29aa998c451d64d9334269546a4021b8";
        if (speechSynthesizer == null)
            speechSynthesizer = new SpeechSynthesizer(getActivity(), APPKEY, APPSECRET);
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
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.action_logout)
    public void onViewClicked() {
        mPresenter.logout();
    }

    @Override
    public void logout() {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(getString(R.string.app_name), MODE_APPEND).edit();
        edit.putBoolean("loginState", false);
        edit.putString("id", null);
        edit.apply();
        Toast.makeText(getActivity(), "login out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.action_user_manage, R.id.action_connect, R.id.action_update, R.id.action_help,
            R.id.action_feedback, R.id.action_contact, R.id.device_state, R.id.app_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_user_manage:
                break;
            case R.id.action_connect:
            case R.id.device_state:
                mPresenter.startScan();
                break;
            case R.id.action_update:
            case R.id.app_version:
                showToast(getString(R.string.prompt_latest_version));
                // TODO: 2017/9/4 mPresenter.checkVersionUpdate();
                break;
            case R.id.action_help:
                showToast(getString(R.string.prompt_app_help), Toast.LENGTH_LONG);
                break;
            case R.id.action_feedback:

                break;
            case R.id.action_contact:
                break;
        }
    }

    /********************************************************************
     *蓝牙连接相关
     ********************************************************************
     **/

    /**
     * @param bleScanException
     */
    public void handleBleScanException(BleScanException bleScanException) {

        switch (bleScanException.getReason()) {
            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                Toast.makeText(getActivity(), getString(R.string.bluetooth_not_avavilable), Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.BLUETOOTH_DISABLED:
                Toast.makeText(getActivity(), getString(R.string.bluetooth_disabled), Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.LOCATION_PERMISSION_MISSING:
                Toast.makeText(getActivity(),
                        "On Android 6.0 location permission is required. Implement Runtime Permissions", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.LOCATION_SERVICES_DISABLED:
                Toast.makeText(getActivity(), "Location services needs to be enabled on Android 6.0", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                Toast.makeText(getActivity(), "Scan with the same filters is already started", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                Toast.makeText(getActivity(), "Failed to register application for bluetooth scan", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                Toast.makeText(getActivity(), "Scan with specified parameters is not supported", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                Toast.makeText(getActivity(), "Scan failed due to internal error", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                Toast.makeText(getActivity(), "Scan cannot start due to limited hardware resources", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.UNKNOWN_ERROR_CODE:
            case BleScanException.BLUETOOTH_CANNOT_START:
            default:
                Toast.makeText(getActivity(), "Unable to start scanning", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void handleScanResult(ScanResult result) {
        scanningProgressBar.dismiss();
        RxBleDevice device = result.getBleDevice();
        scanResultsAdapter = new ScanResultsAdapter(this, bleDeviceList);

        if (showDialogLabel) {
            showScanDialog();
            showDialogLabel = false;
        }

        if (!rxBleDeviceAddressList.contains(device.getMacAddress())) {
            rxBleDeviceAddressList.add(device.getMacAddress());
            bleDeviceList.add(new BleDevice(device.getName(), device.getMacAddress(), result.getRssi()));
            scanResultsAdapter.notifyDataSetChanged();
        }
    }

    private void showScanDialog() {
        if (scanResultDialog == null) {
            scanResultDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.choose_device_prompt)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .titleColor(getResources().getColor(R.color.scan_result_list_title))
                    .dividerColor(getResources().getColor(R.color.divider));
        }
        scanResultDialog.adapter(scanResultsAdapter, null).show();
        //选择目的蓝牙设备
        scanResultsAdapter.setOnAdapterItemClickListener(v -> {
                    // FIXME: 2017/8/23 点击所选择的蓝牙设备后，蓝牙设备对话框关闭
                    String s = ((TextView) v.findViewById(R.id.txt_mac)).getText().toString();
                    mPresenter.connectDevice(s);
                    showDialogLabel = true;
                }
        );
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
    public void showConnected() {
        connectingProgressBar.dismiss();

        deviceState.setText(getString(R.string.prompt_connected));
        showToast(getString(R.string.device_connected));
    }

    @Override
    public void isConnecting() {
        scanResultDialog.build().dismiss();
        connectingProgressBar = new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.connecting))
                .progress(true, 100)
                .show();
    }

    @Override
    public void setLoadingIndicator(boolean bool) {

    }

    @Override
    public void showScanning() {
        scanningProgressBar = new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.scanning))
                .backgroundColor(getResources().getColor(R.color.white))
                .progress(true, 100)
                .show();
    }
}
package com.npclo.imeasurer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.camera.CaptureActivity;
import com.npclo.imeasurer.data.App;
import com.npclo.imeasurer.data.ThirdMember;
import com.npclo.imeasurer.data.WechatUser;
import com.npclo.imeasurer.data.ble.BleDevice;
import com.npclo.imeasurer.data.measure.Contract;
import com.npclo.imeasurer.data.measure.Item;
import com.npclo.imeasurer.measure.MeasureActivity;
import com.npclo.imeasurer.utils.Constant;
import com.npclo.imeasurer.utils.LogUtils;
import com.npclo.imeasurer.utils.PreferencesUtils;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;
import com.unisound.client.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author Endless
 */
public class HomeFragment extends BaseFragment implements HomeContract.View {
    public static final String REDIRECT_URI = "redirect_uri";
    @BindView(R.id.scan_img)
    ImageView scanImg;
    @BindView(R.id.scan_hint)
    TextView scanHint;
    Unbinder unbinder;
    private HomeContract.Presenter mPresenter;
    private List<BleDevice> bleDeviceList = new ArrayList<>();
    private List<String> rxBleDeviceAddressList = new ArrayList<>();
    private ScanResultsAdapter scanResultsAdapter;
    private MaterialDialog.Builder scanResultDialog;
    private MaterialDialog cirProgressBar;
    private MaterialDialog resultDialog;


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
        configureResultList();
        String userLogo = PreferencesUtils.getInstance(getActivity()).getUserLogo();
        if (!TextUtils.isEmpty(userLogo)) {
            Glide.with(this).load(Constant.getHttpScheme() + Constant.IMG_BASE_URL + userLogo)
                    .apply(new RequestOptions().error(R.drawable.load_fail_pic))
                    .into(scanImg);
        }
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
            default:
                break;
        }
    }

    /**
     * 扫码微信用户二维码
     */
    private void startScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQUEST_CODE_WECHATUSER_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
        boolean currentDayFirst = PreferencesUtils.getInstance(getActivity()).isCurrentDayFirst(getTodayStr());
        if (currentDayFirst) {
            mPresenter.autoGetLatestVersion();
        }
        LogUtils.upload(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }

    @Override
    protected void initComToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.basetoolbar);
        String userTitle = PreferencesUtils.getInstance(getActivity()).getUserTitle();
        if (!TextUtils.isEmpty(userTitle)) {
            toolbar.setTitle(userTitle);
        } else {
            toolbar.setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void showLoading(boolean b) {
        if (b) {
            cirProgressBar = new MaterialDialog.Builder(getActivity())
                    .progress(true, 100)
                    .content("处理中...")
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
        } else {
            cirProgressBar.dismiss();
        }
    }

    /**
     * 获取用户数据成功回调
     *
     * @param user 微信用户
     */
    @Override
    public void onGetWechatUserInfo(WechatUser user) {
        showLoading(false);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra("userBundle", bundle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showGetWechatUserInfoError(Throwable e) {
        showLoading(false);
        onHandleError(e);
    }

    @Override
    public void showCompleteGetInfo() {
        showLoading(false);
    }

    @Override
    public void onGetVersionInfo(App app, String type) {
        int code = getVersionCode();
        if (Constant.AUTO.equals(type)) {
            PreferencesUtils.getInstance(getActivity()).setCurrentDate(getTodayStr());
        }
        if (app.getCode() > code && code != 0) {
            updateApp(app);
        } else {
            if (Constant.MANUAL.equals(type)) {
                showToast("已经是最新版");
            }
        }
    }

    @NonNull
    private String getTodayStr() {
        Calendar instance = Calendar.getInstance();
        String year = String.valueOf(instance.get(Calendar.YEAR));
        String month = String.valueOf(instance.get(Calendar.MONTH) + 1);
        String day = String.valueOf(instance.get(Calendar.DAY_OF_MONTH));
        return year + month + day;
    }

    @Override
    public void onGetVersionError(Throwable e, String type) {
        if (type.equals(Constant.MANUAL)) {
            showLoading(false);
        }
        onHandleError(e);
    }

    @Override
    public void onLogout() {
        PreferencesUtils instance = PreferencesUtils.getInstance(getActivity());
        instance.setToken("");
        showToast(getString(R.string.logout_success));
        new android.os.Handler().postDelayed(() -> {
            MainActivity activity = (MainActivity) getActivity();
            Intent intent = new Intent(activity, AccountActivity.class);
            startActivity(intent);
            activity.finish();
        }, 500);
    }

    @Override
    public void onHandleScanResult(ScanResult result) {
        fillScanResult();

        if (cirProgressBar != null) {
            cirProgressBar.dismiss();
            cirProgressBar = null;
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

    private void fillScanResult() {
        if (scanResultDialog == null) {
            scanResultDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.choose_device_prompt)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .titleColor(getResources().getColor(R.color.scan_result_list_title))
                    .dividerColor(getResources().getColor(R.color.divider))
                    .adapter(scanResultsAdapter, null);
        }
    }

    @Override
    public void onShowError(String s) {
        showToast(s);
    }

    private void configureResultList() {
        if (scanResultsAdapter == null) {
            scanResultsAdapter = new ScanResultsAdapter(this, bleDeviceList);
            //选择目的蓝牙设备
            scanResultsAdapter.setOnAdapterItemClickListener(v -> {
                String s = ((TextView) v.findViewById(R.id.txt_mac)).getText().toString();
                mPresenter.connectDevice(s);
            });
        }
    }

    @Override
    public void onHandleBleScanException(BleScanException e) {
        switch (e.getReason()) {
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
    public void onSetNotificationUUID(UUID uuid) {
        PreferencesUtils.getInstance(getActivity()).setDeviceUuid(uuid.toString());
    }

    private void setBleAddress(String s) {
        PreferencesUtils.getInstance(getActivity()).setMacAddress(s);
    }

    private void setBleDeviceName(String name) {
        PreferencesUtils.getInstance(getActivity()).setDeviceName(name);
    }

    @Override
    public void onDeviceChoose(RxBleDevice bleDevice) {
        showToast(getString(R.string.device_connected));
        getSynthesizer().playText("蓝牙连接成功");
        setBleDeviceName(bleDevice.getName());
        setBleAddress(bleDevice.getMacAddress());
        //更新设备连接信息状态 //TODO: 23/01/2018  使用R'xBus
        ((MainActivity) getActivity()).updateBlueToothState(bleDevice.getName());
    }

    @Override
    public void onCloseScanResultDialog() {
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
    public void onDefaultMeasureParts(List<Item> partList) {
        // FIXME: 11/12/2017 更好的解决方法，存储到数据库中
        StringBuilder builder = new StringBuilder();
        for (int i = 0, l = partList.size(); i < l; i++) {
            builder.append(partList.get(i).getName());
            builder.append(",");
        }
        String s = builder.toString();

        PreferencesUtils instance = PreferencesUtils.getInstance(getActivity());
        instance.setMeasureItems(s.trim());
        instance.setContractName("");
        instance.setMeasureCid("10000");//默认量体cid
        instance.setMeasureNum(0);
        instance.setMeasureMeasured(0);

        showToast("设置默认量体项目成功");
        updateContractName("默认");
    }

    /**
     * 处理获取到的量体部位集合
     *
     * @param contract
     */
    @Override
    public void onHandleContractInfo(Contract contract) {
        // FIXME: 11/12/2017 更好的解决方法，存储到数据库中
        List<Item> list = contract.getData();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, l = list.size(); i < l; i++) {
            builder.append(list.get(i).getName());
            builder.append(",");
        }
        String s = builder.toString();

        PreferencesUtils instance = PreferencesUtils.getInstance(getActivity());
        instance.setMeasureItems(s.trim());
        instance.setContractName(contract.getName());
        instance.setMeasureCid(contract.getId());
        instance.setMeasureNum(contract.getNum());
        instance.setMeasureMeasured(contract.getMeasured());

        showToast(contract.getName() + "量体合同量体项目加载成功");
        updateContractName(contract.getName());
    }

    private void updateContractName(String name) {
        //TODO: 23/01/2018  使用R'xBus
        ((MainActivity) getActivity()).updateContractName(name);
    }

    @Override
    public void startScanContractNum() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQUEST_CODE_CONTRACT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_WECHATUSER_CODE:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString("result");
                        if (result.contains("https")) {
                            //解析出tid(ThirdMember中id)
                            int redirectUriIndex = result.indexOf(REDIRECT_URI) + REDIRECT_URI.length() + 1;
                            String s = result.substring(redirectUriIndex);
                            String tid = LogUtils.getParams(s, "tid");
                            String cid = PreferencesUtils.getInstance(getActivity()).getMeasureCid();
                            mPresenter.getThirdMemberInfo(tid, cid);
                        } else {
                            mPresenter.getUserInfoWithOpenID(result);
                        }
                    } else {
                        showToast(getString(R.string.scan_qrcode_failed));
                    }
                    break;
                case Constant.REQUEST_CODE_WECHATUSER_NUM:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString("result");
                        mPresenter.getUserInfoWithCode(result);
                    } else {
                        showToast(getString(R.string.enter_qrcode_error));
                    }
                    break;
                case Constant.REQUEST_CODE_CONTRACT_CODE:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString("result");
                        mPresenter.getContractInfoWithCode(result);
                    } else {
                        showToast(getString(R.string.scan_qrcode_failed));
                    }
                    break;
                case Constant.REQUEST_CODE_CONTRACT_NUM:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString("result");
                        mPresenter.getContractInfoWithNum(result);
                    } else {
                        showToast(getString(R.string.enter_qrcode_error));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private SpeechSynthesizer getSynthesizer() {
        return ((MainActivity) getActivity()).speechSynthesizer;
    }

    /**
     * 动态更新需要测量角度的部位
     *
     * @param list
     */
    @Override
    public void onGetAngleOfParts(List<Item> list) {
        BaseApplication.setAngleList(getActivity(), list);
    }

    @Override
    public void onGetAngleOfPartsError(Throwable e) {
        onHandleError(e);
    }

    @Override
    public void onUpdateUserInfoError(Throwable e) {
        onHandleError(e);
    }

    @Override
    public void onHandleUnknownError(Throwable e) {
        onHandleError(e);
    }

    @Override
    public void onHandleConnectError(Throwable e) {
        onHandleError(e);
    }

    @Override
    public void onGetThirdMemberInfo(ThirdMember member) {
        showLoading(false);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", member);
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra("userBundle", bundle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showGetThirdMemberInfoError(Throwable e) {
        showLoading(false);
        onHandleError(e);
    }

    @Override
    public void showCompleteGetThirdMemberInfo() {
        showLoading(false);
    }

    @Override
    public void showGetThirdOrgDefaultInfoError(Throwable e) {
        showLoading(false);
        onHandleError(e);
    }

    @Override
    public void showGetContractInfoError(Throwable e) {
        showLoading(false);
        onHandleError(e);
    }
}
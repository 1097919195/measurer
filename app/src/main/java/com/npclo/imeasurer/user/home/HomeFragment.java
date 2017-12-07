//package com.npclo.imeasurer.user.home;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.v7.widget.AppCompatButton;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.npclo.imeasurer.R;
//import com.npclo.imeasurer.account.AccountActivity;
//import com.npclo.imeasurer.base.BaseApplication;
//import com.npclo.imeasurer.base.BaseFragment;
//import com.npclo.imeasurer.data.app.App;
//import com.npclo.imeasurer.data.ble.BleDevice;
//import com.npclo.imeasurer.main.home.ScanResultsAdapter;
//import com.npclo.imeasurer.main.contact.ContactFragment;
//import com.npclo.imeasurer.main.feedback.FeedbackFragment;
//import com.npclo.imeasurer.main.manage.ManageFragment;
//import com.npclo.imeasurer.main.manage.ManagePresenter;
//import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
//import com.polidea.rxandroidble.RxBleConnection;
//import com.polidea.rxandroidble.RxBleDevice;
//import com.polidea.rxandroidble.exceptions.BleScanException;
//import com.polidea.rxandroidble.scan.ScanResult;
//import com.unisound.client.SpeechConstants;
//import com.unisound.client.SpeechSynthesizer;
//import com.unisound.client.SpeechSynthesizerListener;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
//import static android.content.Context.MODE_APPEND;
//import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
//
///**
// * @author Endless
// */
//public class HomeFragment extends BaseFragment implements HomeContract.View {
//    HomeContract.Presenter mPresenter;
//    @BindView(R.id.action_logout)
//    AppCompatButton actionLogout;
//    Unbinder unbinder;
//    @BindView(R.id.base_toolbar)
//    Toolbar baseToolbar;
//    @BindView(R.id.device_state)
//    TextView deviceState;
//    @BindView(R.id.action_connect)
//    ImageView actionConnect;
//    @BindView(R.id.app_version)
//    TextView appVersion;
//    @BindView(R.id.user_name)
//    TextView userName;
//    @BindView(R.id.curr_times)
//    TextView currTimes;
//    @BindView(R.id.total_times)
//    TextView totalTimes;
//    @BindView(R.id.action_update)
//    ImageView actionUpdate;
//    @BindView(R.id.action_help)
//    ImageView actionHelp;
//    @BindView(R.id.action_feedback)
//    ImageView actionFeedback;
//    @BindView(R.id.action_contact)
//    ImageView actionContact;
//    private List<BleDevice> bleDeviceList = new ArrayList<>();
//    private List<String> rxBleDeviceAddressList = new ArrayList<>();
//    private ScanResultsAdapter scanResultsAdapter;
//    private MaterialDialog.Builder scanResultDialog;
//    private MaterialDialog connectingProgressBar;
//    private MaterialDialog scanningProgressBar;
//    private SpeechSynthesizer speechSynthesizer;
//    private MaterialDialog resultDialog;
//
//    public HomeFragment() {
//    }
//
//    public static HomeFragment newInstance() {
//        return new HomeFragment();
//    }
//
//    @Override
//    public void setPresenter(HomeContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.frag_user;
//    }
//
//    @Override
//    protected void initView(View mRootView) {
//        unbinder = ButterKnife.bind(this, mRootView);
//        baseToolbar.setNavigationIcon(R.mipmap.left);
//        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_APPEND);
//        currTimes.setText(preferences.getString("currTimes", "N/A"));
//        totalTimes.setText(preferences.getString("totalTimes", "N/A"));
//        String name = preferences.getString("name", "N/A");
//        String nickname = preferences.getString("nickname", "");
//        userName.setText(!TextUtils.isEmpty(nickname) ? nickname : name);
//
//        configureResultList();
//    }
//
//    private void configureResultList() {
//        scanResultsAdapter = new ScanResultsAdapter(this, bleDeviceList);
//        scanResultDialog = new MaterialDialog.Builder(getActivity())
//                .title(R.string.choose_device_prompt)
//                .backgroundColor(getResources().getColor(R.color.white))
//                .titleColor(getResources().getColor(R.color.scan_result_list_title))
//                .dividerColor(getResources().getColor(R.color.divider))
//                .adapter(scanResultsAdapter, null);
//        ;
//        //选择目的蓝牙设备
//        scanResultsAdapter.setOnAdapterItemClickListener(v -> {
//                    String s = ((TextView) v.findViewById(R.id.txt_mac)).getText().toString();
//                    mPresenter.connectDevice(s);
//                }
//        );
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        initSpeech();
//
//        String macAddress = BaseApplication.getMacAddress(getActivity());
//        if (macAddress != null) {
//            RxBleDevice rxBleDevice = BaseApplication.getRxBleClient(getActivity()).getBleDevice(macAddress);
//            if (rxBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
//                updateDeviceState();
//            }
//        }
//        if (mPresenter != null) {
//            mPresenter.subscribe();
//        }
//        if (BaseApplication.canUpdate(getActivity())) {
//            actionConnect.setEnabled(true);
//            appVersion.setEnabled(true);
//            appVersion.setText(getString(R.string.prompt_can_update));
//            appVersion.setTextColor(getResources().getColor(R.color.primary));
//        } else {
//            actionConnect.setEnabled(false);
//            appVersion.setEnabled(false);
//            appVersion.setText(getString(R.string.prompt_latest_version));
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mPresenter != null) {
//            mPresenter.unsubscribe();
//        }
//    }
//
//
//    @OnClick({R.id.action_user_manage, R.id.action_connect, R.id.action_update, R.id.action_help,
//            R.id.action_feedback, R.id.action_contact, R.id.device_state, R.id.app_version})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.action_user_manage:
//                ManageFragment manageFragment = ManageFragment.newInstance();
//                start(manageFragment, SINGLETASK);
//                manageFragment.setPresenter(new ManagePresenter(manageFragment, SchedulerProvider.getInstance()));
//                break;
//            case R.id.action_connect:
//            case R.id.device_state:
//                mPresenter.startScan();
//                break;
//            case R.id.action_update:
//            case R.id.app_version:
//                mPresenter.checkVersion();
//                break;
//            case R.id.action_help:
//                showToast(getString(R.string.prompt_app_help), Toast.LENGTH_LONG);
//                break;
//            case R.id.action_feedback:
//                FeedbackFragment feedbackFragment = FeedbackFragment.newInstance();
//                start(feedbackFragment, SINGLETASK);
//                break;
//            case R.id.action_contact:
//                ContactFragment contactFragment = ContactFragment.newInstance();
//                start(contactFragment, SINGLETASK);
//                break;
//            default:
//                break;
//        }
//    }
//

//
//    @Override
//    public void showError(String s) {
//        showToast(s);
//    }
//
//    @Override
//    public void showError() {
//        showToast(getString(R.string.unKnownError));
//    }
//
//    @Override
//    public void showConnected(RxBleDevice bleDevice) {
//        connectingProgressBar.dismiss();
//        updateDeviceState();
//        showToast(getString(R.string.device_connected));
//        speechSynthesizer.playText("蓝牙连接成功");
//    }
//
//    private void updateDeviceState() {
//        deviceState.setText(getString(R.string.prompt_connected));
//        deviceState.setTextColor(getResources().getColor(R.color.primary));
//        deviceState.setEnabled(false);
//        actionConnect.setEnabled(false);
//    }
//
//    @Override
//    public void isConnecting() {
//        connectingProgressBar = new MaterialDialog.Builder(getActivity())
//                .title(getString(R.string.connecting))
//                .titleColor(getResources().getColor(R.color.ff5001))
//                .backgroundColor(getResources().getColor(R.color.white))
//                .progress(true, 100)
//                .show();
//    }
//
//    @Override
//    public void setLoadingIndicator(boolean bool) {
//
//    }
//
//    @Override
//    public void showScanning() {
//        scanningProgressBar = new MaterialDialog.Builder(getActivity())
//                .backgroundColor(getResources().getColor(R.color.white))
//                .progress(true, 100)
//                .show();
//    }
//
//    @Override
//    public void closeScanResultDialog() {
//        try {
//            if (resultDialog != null || resultDialog.isShowing()) {
//                resultDialog.dismiss();
//                resultDialog = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void showGetVersionSuccess(App app) {
//        int code = getVersionCode();
//        if (app.getCode() > code && code != 0) {
//            updateApp(app);
//        } else {
//            showToast(getString(R.string.prompt_latest_version));
//        }
//    }
//
//    @Override
//    public void showGetVersionError(Throwable e) {
//        onHandleError(e);
//    }
//}
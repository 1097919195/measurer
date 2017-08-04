package stuido.tsing.iclother.measure;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.GsonBuilder;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.data.ble.BleCharacter;
import stuido.tsing.iclother.data.ble.BleService;
import stuido.tsing.iclother.data.measure.Measurement;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/8/1.
 */

public class MeasureFragment extends Fragment implements MeasureContract.View {
    @BindView(R.id.radio_male)
    RadioButton radioMale;
    @BindView(R.id.radio_female)
    RadioButton radioFemale;
    @BindView(R.id.sex_rg)
    RadioGroup sexRadioGroup;
    @BindView(R.id.measure_height_input)
    EditText measureHeightInput;
    @BindView(R.id.measure_weight_input)
    EditText measureWeightInput;
    @BindView(R.id.scan_toggle_btn)
    AppCompatButton scanToggleBtn;
    @BindView(R.id.ruler_state)
    TextView rulerState;
    @BindView(R.id.ruler_battery)
    TextView rulerBattery;
    @BindView(R.id.measure_button)
    AppCompatButton measureButton;
    @BindView(R.id.xiongwei_et)
    EditText xiongweiEt;
    @BindView(R.id.yaowei_et)
    EditText yaoweiEt;
    @BindView(R.id.tunwei_et)
    EditText tunweiEt;
    @BindView(R.id.save_measure_result)
    AppCompatButton saveMeasureResult;
    Unbinder unbinder;
    private MeasureContract.Presenter mPresenter;
    private Map<String, String> mData = new LinkedHashMap<>();
    //    private RecyclerView recyclerView;
//    private ScanResultsAdapter resultsAdapter;
    private List<String> macAddress = new ArrayList<>();
    private List<BleService> bleServiceList = new ArrayList<>();
    private List<BleCharacter> bleCharacters = new ArrayList<>();
    private Map<String, BluetoothGattService> bleServiceMap = new LinkedHashMap<>();
    private MaterialDialog serviceListDialog;
    private MaterialDialog characteristicListDialog;

    public MeasureFragment() {
    }

    public static MeasureFragment newInstance() {
        return new MeasureFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        configureResultList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
//
//    @PermissionSuccess(requestCode = 100)
//    public void doSomething() {
//
//    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        Toast.makeText(getActivity(), "permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.measure_frag, container, false);
        unbinder = ButterKnife.bind(this, root);
//        measureButton.setVisibility(View.INVISIBLE);
        return root;
    }

    @Override
    public void setPresenter(MeasureContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showBleDisconnectHint() {
        Snackbar.make(getView(), getString(R.string.device_disconnected), Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.scan_toggle_btn, R.id.measure_button, R.id.save_measure_result})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_toggle_btn:
                mPresenter.scanToggle();
                break;
            case R.id.measure_button:
                mPresenter.startMeasure();
                break;
            case R.id.save_measure_result:
                saveMeasurement();
                break;
        }
    }


    private void saveMeasurement() {
        String height = measureHeightInput.getText().toString();
        checkInputValid(height, "身高");
        String weight = measureWeightInput.getText().toString();
        checkInputValid(weight, "体重");
        String xiongwei = xiongweiEt.getText().toString();
        checkInputValid(xiongwei, "胸围");
        String yaowei = yaoweiEt.getText().toString();
        checkInputValid(yaowei, "腰围");
        String tunwei = tunweiEt.getText().toString();
        checkInputValid(tunwei, "臀围");
        mData.put("xiongwei", xiongwei);
        mData.put("yaowei", yaowei);
        mData.put("tunwei", tunwei);
        int sex = 0;
        if (sexRadioGroup.getCheckedRadioButtonId() == radioFemale.getId()) {
            sex = 1;
        }

        String data = new GsonBuilder().enableComplexMapKeySerialization().create().toJson(mData);
        Measurement measurement = new Measurement("11", data, sex);
        mPresenter.saveMeasurement(measurement);
    }

    @Override
    public void showSuccessSave() {

    }

    @Override
    public void showSaveError() {

    }

    @Override
    public void setLoadingIndicator(boolean b) {

    }

    @Override
    public void showAlreadyConnectedException() {
        Snackbar.make(getView(), "重复连接", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showConnecting() {
        scanToggleBtn.setText(getString(R.string.connecting));
    }

    private void checkInputValid(String v, String field) {
        if (TextUtils.isEmpty(v)) showInputError(field);
    }

    private void showInputError(String field) {
        Toast.makeText(getActivity(), field + "不能为空", Toast.LENGTH_SHORT).show();
        return;
    }

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

    @Override
    public void showStartReceiveData() {
        Snackbar.make(getView(), "Notifications has been set up", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void addScanResult(ScanResult result) {
        setLoadingIndicator(false);
        RxBleDevice device = result.getBleDevice();
        String name = device.getName();
        String address = device.getMacAddress();
        macAddress.clear();
//        macAddress.add("名称: "+name+"\n地址: "+address);
        macAddress.add(address);
        showDeviceList();
    }

    @Override
    public void updateButtonUIState() {
//        resultsAdapter.clearScanResults();
        scanToggleBtn.setText(mPresenter.isScanning() ? getString(R.string.stop_scan) : getString(R.string.start_scan));
    }

    private void configureResultList() {
//        recyclerView = new RecyclerView(getActivity());
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(recyclerLayoutManager);
//        resultsAdapter = new ScanResultsAdapter();
//        recyclerView.setAdapter(resultsAdapter);
//        resultsAdapter.setOnAdapterItemClickListener(view -> {
//            final int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
//            final ScanResult itemAtPosition = resultsAdapter.getItemAtPosition(childAdapterPosition);
//            onAdapterItemClick(itemAtPosition);
//        });
    }


    private void onAdapterItemClick(ScanResult results) {
        String macAddress = results.getBleDevice().getMacAddress();
        mPresenter.connectDevice();
    }

    @Override
    public void showServiceChoiceView(BluetoothGattCharacteristic characteristic) {
        int properties = characteristic.getProperties();
//        mPresenter.discoveryServices(text.toString());
//        boolean connected = mPresenter.isConnected();
//        if (connected) {
//            scanToggleBtn.setEnabled(false);
//            rulerState.setText(getString(R.string.connected));
//            measureButton.setVisibility(View.VISIBLE);
//        } else {
//            rulerState.setText(getString(R.string.disconnected));
//            rulerState.setTextColor(getResources().getColor(R.color.error));
//        }
    }

    @Override
    public void showBleServicesDiscoveryView() {
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(getView(), getString(R.string.unKnownError), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showUnknownError(String s) {
        Snackbar.make(getView(), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateMeasureData(int length, float battery, float angle) {
        rulerBattery.setText(battery + "");
        // TODO: 017/8/3 更新当前焦点输入的框的结果
        yaoweiEt.setText(length + "");
    }

    public void showDeviceList() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_device_prompt)
                .items(macAddress)
                .backgroundColor(getResources().getColor(R.color.white))
                .contentColor(getResources().getColor(R.color.primary))
                .titleColor(getResources().getColor(R.color.battery_color))
                .itemsCallback((dialog, view, which, text) -> mPresenter.discoveryServices(text.toString()))
                .show();
    }

    /**
     * 显示设备的可用服务
     * 事件：点击条目选中某个服务下的具体特性
     *
     * @param deviceServices
     */
    @Override
    public void showServiceListView(RxBleDeviceServices deviceServices) {
        bleServiceList.clear();
        bleServiceMap.clear();
        for (BluetoothGattService service : deviceServices.getBluetoothGattServices()) {
            // Add service
            bleServiceList.add(new BleService(getServiceType(service), service.getUuid().toString()));
            bleServiceMap.put(service.getUuid().toString(), service);
        }
        ServiceListAdapter adapter = new ServiceListAdapter(this, bleServiceList);
        serviceListDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_service_prompt)
                // second parameter is an optional layout manager. Must be a LinearLayoutManager or GridLayoutManager.
                .adapter(adapter, null)
                .titleColor(getResources().getColor(R.color.battery_color))
                .backgroundColor(getResources().getColor(R.color.white))
                .show();
        adapter.setOnAdapterItemClickListener(this::showCharacteristicListView);
    }

    private void showCharacteristicListView(String uuid) {
        checkNotNull(bleServiceMap);
        checkNotNull(serviceListDialog);
        serviceListDialog.dismiss();
        BluetoothGattService service = bleServiceMap.get(uuid);
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            bleCharacters.add(new BleCharacter(describeProperties(characteristic), characteristic.getUuid().toString()));
        }
        CharacterListAdapter adapter = new CharacterListAdapter(this, bleCharacters);
        characteristicListDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_character_prompt)
                .adapter(adapter, null)
                .titleColor(getResources().getColor(R.color.battery_color))
                .backgroundColor(getResources().getColor(R.color.white))
                .titleColor(getResources().getColor(R.color.primary))
                .show();
        adapter.setOnAdapterItemClickListener(this::onCharacteristicItemClick);
    }

    private void onCharacteristicItemClick(String uuid) {
        checkNotNull(characteristicListDialog);
        characteristicListDialog.dismiss();
        mPresenter.chooseCharacteristic(uuid);
    }

    private String getServiceType(BluetoothGattService service) {
        return service.getType() == BluetoothGattService.SERVICE_TYPE_PRIMARY ? getString(R.string.service_primary)
                : getString(R.string.service_secondary);
    }

    private String describeProperties(BluetoothGattCharacteristic characteristic) {
        List<String> properties = new ArrayList<>();
        if (isCharacteristicReadable(characteristic)) properties.add("Read");
        if (isCharacteristicWritable(characteristic)) properties.add("Write");
        if (isCharacteristicNotifiable(characteristic)) properties.add("Notify");
        return TextUtils.join(" ", properties);
    }

    private boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    private boolean isCharacteristicReadable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    private boolean isCharacteristicWritable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE
                | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }
}

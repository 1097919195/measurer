package stuido.tsing.iclother.measure;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import stuido.tsing.iclother.data.ble.BleDevice;
import stuido.tsing.iclother.data.ble.BleService;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.UserSex;
import stuido.tsing.iclother.data.measure.item.MeasurementFemaleItem;
import stuido.tsing.iclother.data.measure.item.MeasurementItem;
import stuido.tsing.iclother.data.measure.item.MeasurementMaleItem;
import stuido.tsing.iclother.data.measure.item.parts.Part;
import stuido.tsing.iclother.data.wuser.WeiXinUser;
import stuido.tsing.iclother.utils.DensityUtil;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MeasureFragment extends Fragment implements MeasureContract.View {
    private List<String> angleList;
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
    @BindView(R.id.save_measure_result)
    AppCompatButton saveMeasureResult;
    @BindView(R.id.measure_table_layout)
    TableLayout measureTableLayout;
    Unbinder unbinder;
    private MeasureContract.Presenter mPresenter;
    private List<BleService> bleServiceList = new ArrayList<>();
    private List<BleDevice> bleDevices = new ArrayList<>();
    private List<BleCharacter> bleCharacters = new ArrayList<>();
    private Map<String, BluetoothGattService> bleServiceMap = new LinkedHashMap<>();
    private MaterialDialog serviceListDialog;
    private MaterialDialog characteristicListDialog;
    private MaterialDialog scanningDialog;
    private static final float TEXT_VIEW_HEIGHT = 30;
    private List<TableRow> maleRows = new ArrayList<>();
    private List<TableRow> femaleRows = new ArrayList<>();
    private String[] angleItems = new String[5];
    private SpeechSynthesizer speechSynthesizer;
    private String PART_PACKAGE = Part.class.getPackage().getName();
    private String ITEM_PACKAGE = MeasurementItem.class.getPackage().getName();
    private String APPKEY = "hhzjkm3l5akcz5oiflyzmmmitzrhmsfd73lyl3y2";
    private String APPSECRET = "29aa998c451d64d9334269546a4021b8";

    public MeasureFragment() {
    }

    public static MeasureFragment newInstance() {
        return new MeasureFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        Toast.makeText(getActivity(), "permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        angleItems = getResources().getStringArray(R.array.angle_items);
        angleList = Arrays.asList(angleItems);
        initSpeech();
        mPresenter.subscribe();
    }

    private void initSpeech() {
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
        speechSynthesizer.init(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        speechSynthesizer = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.measure_frag, container, false);
        unbinder = ButterKnife.bind(this, root);
        if (maleRows.size() == 0) {
            maleRows = initMeasureItemList(UserSex.MALE);
        }
        appendTableRows(maleRows);
        return root;
    }

    private List<TableRow> initMeasureItemList(int type) {
        List<TableRow> rows = new ArrayList<>();
        MeasurementItem item;
        try {
            if (type == UserSex.MALE) {
                item = (MeasurementMaleItem) Class.forName(ITEM_PACKAGE + ".MeasurementMaleItem").newInstance();
            } else {
                item = (MeasurementFemaleItem) Class.forName(ITEM_PACKAGE + ".MeasurementFemaleItem").newInstance();
            }
            for (Field field : item.getClass().getDeclaredFields()) {
                String name = field.getName();
                Class<?> itemSubclass = Class.forName(PART_PACKAGE + "." + name);
                Part part = (Part) itemSubclass.newInstance();
                TableRow tableRow = getTableRow(part.getCn(), part.getEn());
                rows.add(tableRow);
            }
            for (Field field : item.getClass().getSuperclass().getDeclaredFields()) {
                String name = field.getName();
                Class<?> itemSubclass = Class.forName(PART_PACKAGE + "." + name);
                Part part = (Part) itemSubclass.newInstance();
                TableRow tableRow = getTableRow(part.getCn(), part.getEn());
                rows.add(tableRow);
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rows;
    }

    @NonNull
    private TableRow getTableRow(String cn, String en) {
// TODO: 2017/8/5 布局对齐
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new TableRow
                .LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setPadding(0, 5, 0, 0);

        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setBackground(getResources().getDrawable(R.drawable.table_border_1dp));
        textView.setHeight(DensityUtil.dp2px(getActivity(), TEXT_VIEW_HEIGHT));
        textView.setPadding(0, 3, 0, 3);
        textView.setText(cn);

        EditText editText = new EditText(getActivity());
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setTextColor(getResources().getColor(R.color.ff5001));
        editText.setBackground(getResources().getDrawable(R.drawable.table_border_1dp));
        editText.setHeight(DensityUtil.dp2px(getActivity(), TEXT_VIEW_HEIGHT));
        editText.setPadding(0, 5, 0, 5);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTag(en);

        tableRow.addView(textView);
        tableRow.addView(editText);
        return tableRow;
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

    @OnClick({R.id.scan_toggle_btn, R.id.measure_button, R.id.save_measure_result, R.id.radio_male, R.id.radio_female})
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
            case R.id.radio_male:
                appendTableRows(maleRows);
                break;
            case R.id.radio_female:
                if (femaleRows.size() == 0) {
                    femaleRows = initMeasureItemList(UserSex.FEMALE);
                }
                appendTableRows(femaleRows);
                break;
        }
    }

    void appendTableRows(List<TableRow> list) {
        TableRow tableTitle = (TableRow) measureTableLayout.getChildAt(0);
        measureTableLayout.removeAllViews();
        measureTableLayout.addView(tableTitle);
        for (TableRow row : list) {
            measureTableLayout.addView(row);
        }
    }

    private void saveMeasurement() {
        Map<String, String> mData = new LinkedHashMap<>();
        String height = measureHeightInput.getText().toString();
        checkInputValid(height, "身高");
        String weight = measureWeightInput.getText().toString();
        checkInputValid(weight, "体重");
        int count = measureTableLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            TableRow row = (TableRow) measureTableLayout.getChildAt(i);
            EditText editText = (EditText) row.getChildAt(1);
            String tag = (String) editText.getTag();
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                String v = editText.getText().toString();
                mData.put(tag, v);
            }
        }
        MeasurementItem item;
        int sex = UserSex.MALE;
        Iterator<Map.Entry<String, String>> iterator = mData.entrySet().iterator();
        if (sexRadioGroup.getCheckedRadioButtonId() == radioFemale.getId()) {
            sex = UserSex.FEMALE;
        }
        item = getMeasurementItem(sex, iterator);
        WeiXinUser weiXinUser = new WeiXinUser();
        // TODO: 2017/8/15 微信接口
        weiXinUser.setHeight(height).setWeight(weight).setSex(sex).setWid("123123").setNickname("test");
        Measurement measurement = new Measurement(weiXinUser, item);
        mPresenter.saveMeasurement(measurement);
    }

    private MeasurementItem getMeasurementItem(int type, Iterator<Map.Entry<String, String>> iterator) {
        MeasurementItem item = null;
        Class<?> Class2 = null;
        try {
            if (type == UserSex.MALE) {
                Class2 = Class.forName(ITEM_PACKAGE + ".MeasurementMaleItem");
                item = (MeasurementMaleItem) Class2.newInstance();
            } else {
                Class2 = Class.forName(ITEM_PACKAGE + ".MeasurementFemaleItem");
                item = (MeasurementFemaleItem) Class2.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                Class<?> aClass = Class.forName(PART_PACKAGE + "." + key);
                Part part = (Part) aClass.newInstance();
                part.setValue(value);
                part.setEn(key);
                Method method = Class2.getMethod("set" + key, aClass);
                method.invoke(item, part);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    @Override
    public void showSuccessSave() {
        Snackbar.make(getView(), getString(R.string.save_measurement_success), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSaveError() {

    }

    @Override
    public void setLoadingIndicator(boolean b) {

    }

    @Override
    public void showAlreadyConnectedError() {
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

    @Override
    public void showScanning() {
        scanningDialog = new MaterialDialog.Builder(getActivity())
                .progress(true, 100)
                .backgroundColor(getResources().getColor(R.color.white))
                .show();
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
    public void showScanResult(ScanResult result) {
        scanningDialog.dismiss();
        RxBleDevice device = result.getBleDevice();
        bleDevices.clear();
        bleDevices.add(new BleDevice(device.getName(), device.getMacAddress(), result.getRssi()));
        ScanResultsAdapter adapter = new ScanResultsAdapter(this, bleDevices);
        new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_device_prompt)
                .adapter(adapter, null)
                .backgroundColor(getResources().getColor(R.color.white))
                .titleColor(getResources().getColor(R.color.scan_result_list_title))
                .dividerColor(getResources().getColor(R.color.divider))
                .show();
        adapter.setOnAdapterItemClickListener(v ->
                mPresenter.discoveryServices(((TextView) v.findViewById(R.id.txt_mac)).getText().toString())
        );
    }

    @Override
    public void finishScan() {
        scanToggleBtn.setText(getString(R.string.scan_finished));
        scanToggleBtn.setEnabled(false);
    }

    @Override
    public void updateButtonUIState() {
//        resultsAdapter.clearScanResults();
        scanToggleBtn.setText(mPresenter.isScanning() ? getString(R.string.stop_scan) : getString(R.string.start_scan));
    }

    @Override
    public void showServiceChoiceView(BluetoothGattCharacteristic characteristic) {
        int properties = characteristic.getProperties();
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
    public void updateMeasureData(float length, int angle, int battery) {
        rulerBattery.setText(battery + "%");
        rulerState.setTextColor(getResources().getColor(R.color.green));
        // TODO: 017/8/3 更新当前焦点输入的框的结果
        if (sexRadioGroup.getCheckedRadioButtonId() == radioMale.getId()) {
            for (TableRow row : maleRows) {
                if (assignValue(length, angle, row)) break;
            }
        } else {
            for (TableRow row : femaleRows) {
                if (assignValue(length, angle, row)) break;
            }
        }
    }

    /**
     * 结果赋值，有几个字段需要的结果为角度
     *
     * @param length 长度
     * @param row    行
     * @return boolean
     */
    private boolean assignValue(float length, float angle, TableRow row) {
        EditText editText = (EditText) row.getChildAt(1);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            String tag = (String) editText.getTag();
            String cn;
            try {
                Part part = (Part) Class.forName(PART_PACKAGE + "." + tag).newInstance();
                cn = part.getCn();
                String value;
                if (angleList.contains(tag)) {
                    editText.setText(angle + "");
                    value = angle + "";
                } else {
                    editText.setText(length + "");
                    value = length + "";
                }
                if (speechSynthesizer != null) {
                    speechSynthesizer.playText("测量部位" + cn + "，结果为" + value);
                    // TODO: 2017/8/22 接着播放下一个部位
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void bleDeviceMeasuring() {
        speechSynthesizer.playText("蓝牙连接成功，请按顺序测量");
        measureButton.setText(getString(R.string.measuring));
        measureButton.setTextColor(getResources().getColor(R.color.measuring));
    }

    @Override
    public void showConnected() {
        rulerState.setText(mPresenter.isConnected() ? getString(R.string.connected) : getString(R.string.disconnected));
        rulerState.setTextColor(getResources().getColor(R.color.ble_connected));
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
                .titleColor(getResources().getColor(R.color.scan_result_list_title))
                .backgroundColor(getResources().getColor(R.color.white))
                .dividerColor(getResources().getColor(R.color.divider))
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
                .dividerColor(getResources().getColor(R.color.divider))
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
        if (isCharacteristicReadable(characteristic)) properties.add("可读");
        if (isCharacteristicWritable(characteristic)) properties.add("可写");
        if (isCharacteristicNotifiable(characteristic)) properties.add("通知");
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
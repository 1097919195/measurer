//package com.npclo.imeasurer.measure;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.npclo.imeasurer.R;
//import com.npclo.imeasurer.data.ble.BleDevice;
//import com.npclo.imeasurer.data.measure.UserSex;
//import com.npclo.imeasurer.data.measure.item.MeasurementFemaleItem;
//import com.npclo.imeasurer.data.measure.item.MeasurementItem;
//import com.npclo.imeasurer.data.measure.item.MeasurementMaleItem;
//import com.npclo.imeasurer.data.measure.item.parts.Part;
//import com.npclo.imeasurer.utils.DensityUtil;
//import com.polidea.rxandroidble.RxBleDevice;
//import com.polidea.rxandroidble.exceptions.BleScanException;
//import com.polidea.rxandroidble.scan.ScanResult;
//import com.unisound.client.SpeechConstants;
//import com.unisound.client.SpeechSynthesizer;
//import com.unisound.client.SpeechSynthesizerListener;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import butterknife.Unbinder;
//import kr.co.namee.permissiongen.PermissionFail;
//import kr.co.namee.permissiongen.PermissionGen;
//
//public class MeasureFragment extends Fragment   {
//    private List<String> angleList;
//    Unbinder unbinder;
//    private List<BleDevice> bleDeviceList = new ArrayList<>();
//    private List<String> rxBleDeviceAddressList = new ArrayList<>();
//    private MaterialDialog scanningDialog;
//    private static final float TEXT_VIEW_HEIGHT = 30;
//    private List<TableRow> maleRows = new ArrayList<>();
//    private List<TableRow> femaleRows = new ArrayList<>();
//    private SpeechSynthesizer speechSynthesizer;
//    private String PART_PACKAGE = Part.class.getPackage().getName();
//    private String ITEM_PACKAGE = MeasurementItem.class.getPackage().getName();
//    private MaterialDialog.Builder scanResutlDialog;
//    private ScanResultsAdapter scanResultsAdapter;
//    private boolean showDialogLabel = true;
//    private String[] maleMeasureSequence;
//
//    public MeasureFragment() {
//    }
//
//    public static MeasureFragment newInstance() {
//        return new MeasureFragment();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }
//
//    @PermissionFail(requestCode = 100)
//    public void doFailSomething() {
//        Toast.makeText(getActivity(), "permission is not granted", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        String[] angleItems = getResources().getStringArray(R.array.angle_items);
//        angleList = Arrays.asList(angleItems);
//        initSpeech();
//    }
//
//    private void initSpeech() {
//        String APPKEY = "hhzjkm3l5akcz5oiflyzmmmitzrhmsfd73lyl3y2";
//        String APPSECRET = "29aa998c451d64d9334269546a4021b8";
//        speechSynthesizer = new SpeechSynthesizer(getActivity(), APPKEY, APPSECRET);
//        speechSynthesizer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_NET);
//        speechSynthesizer.setTTSListener(new SpeechSynthesizerListener() {
//            @Override
//            public void onEvent(int i) {
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//        speechSynthesizer.init(null);// FIXME: 2017/8/24 语音播报需要联网
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        speechSynthesizer = null;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        View root = inflater.inflate(R.layout.measure_frag, container, false);
////        unbinder = ButterKnife.bind(this, root);
//        if (maleRows.size() == 0) {
//            maleRows = initMeasureItemList(UserSex.MALE);
//        }
////        appendTableRows(maleRows);
//        return null;
//    }
//
//    private List<TableRow> initMeasureItemList(int type) {
//        List<TableRow> rows = new ArrayList<>();
//        MeasurementItem item;
//        try {
//            if (type == UserSex.MALE) {
//                item = (MeasurementMaleItem) Class.forName(ITEM_PACKAGE + ".MeasurementMaleItem").newInstance();
//            } else {
//                item = (MeasurementFemaleItem) Class.forName(ITEM_PACKAGE + ".MeasurementFemaleItem").newInstance();
//            }
//            Field[] declaredFields = item.getClass().getDeclaredFields();
//            List<String> nameList = new ArrayList<>();
//            for (Field field : declaredFields) {
//                String name = field.getName();
//                nameList.add(name);
//            }
//            Field[] declaredFields2 = item.getClass().getSuperclass().getDeclaredFields();
//            for (Field field : declaredFields2) {
//                String name = field.getName();
//                nameList.add(name);
//            }
//
//            String[] objects = new String[nameList.size()];
//            String[] strings = nameList.toArray(objects);
//            Arrays.sort(strings);
//            for (String name : strings) {
//                Class<?> itemSubclass = Class.forName(PART_PACKAGE + "." + name);
//                Part part = (Part) itemSubclass.newInstance();
//                TableRow tableRow = getTableRow(part.getCn(), part.getEn());
//                rows.add(tableRow);
//            }
//        } catch (java.lang.InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return rows;
//    }
//    @NonNull
//    private TableRow getTableRow(String cn, String en) {
//// TODO: 2017/8/5 布局对齐
//        TableRow tableRow = new TableRow(getActivity());
//        tableRow.setLayoutParams(new TableRow
//                .LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        tableRow.setPadding(0, 5, 0, 0);
//
//        TextView textView = new TextView(getActivity());
//        textView.setGravity(Gravity.CENTER_VERTICAL);
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        textView.setTextColor(getResources().getColor(R.color.black));
//        textView.setBackground(getResources().getDrawable(R.drawable.table_border_1dp));
//        textView.setHeight(DensityUtil.dp2px(getActivity(), TEXT_VIEW_HEIGHT));
//        textView.setPadding(0, 3, 0, 3);
//        textView.setText(cn);
//
//        EditText editText = new EditText(getActivity());
//        editText.setGravity(Gravity.CENTER_VERTICAL);
//        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        editText.setTextColor(getResources().getColor(R.color.ff5001));
//        editText.setBackground(getResources().getDrawable(R.drawable.table_border_1dp));
//        editText.setHeight(DensityUtil.dp2px(getActivity(), TEXT_VIEW_HEIGHT));
//        editText.setPadding(0, 5, 0, 5);
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        editText.setTag(en);
//
//        tableRow.addView(textView);
//        tableRow.addView(editText);
//        return tableRow;
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    public void showBleDisconnectHint() {
//        Snackbar.make(getView(), getString(R.string.device_disconnected), Snackbar.LENGTH_SHORT).show();
//    }
//
//    private void saveMeasurement() {
////        Map<String, String[]> mData = new LinkedHashMap<>();
////        String height = measureHeightInput.getText().toString();
////        checkInputValid(height, "身高");
////        String weight = measureWeightInput.getText().toString();
////        checkInputValid(weight, "体重");
////        int count = measureTableLayout.getChildCount();
////        for (int i = 1; i < count; i++) {
////            TableRow row = (TableRow) measureTableLayout.getChildAt(i);
////            EditText editText = (EditText) row.getChildAt(1);
////            String tag = (String) editText.getTag();
////            if (!TextUtils.isEmpty(editText.getText().toString())) {
////                String v = editText.getText().toString();
////                String[] strings = new String[2];
////                strings[0] = v;
////                try {
////                    Class<?> aClass = Class.forName(PART_PACKAGE + "." + tag);
////                    Part part = (Part) aClass.newInstance();
////                    strings[1] = part.getCn();
////                    mData.put(tag, strings);
////                } catch (ClassNotFoundException e) {
////                    e.printStackTrace();
////                } catch (java.lang.InstantiationException e) {
////                    e.printStackTrace();
////                } catch (IllegalAccessException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////        MeasurementItem item;
////        int sex = UserSex.MALE;
////        Iterator<Map.Entry<String, String[]>> iterator = mData.entrySet().iterator();
////        if (sexRadioGroup.getCheckedRadioButtonId() == radioFemale.getId()) {
////            sex = UserSex.FEMALE;
////        }
////        item = getMeasurementItem(sex, iterator);
////        WechatUser wechatUser = new WechatUser();
////        // TODO: 2017/8/15 微信接口
////        wechatUser.setHeight(height).setWeight(weight).setSex(sex).setOpenID("123123").setNickname("test");
////        Measurement measurement = new Measurement(wechatUser, item);
////        mPresenter.saveMeasurement(measurement);
//    }
//
//
//    public void showSuccessSave() {
//        Snackbar.make(getView(), getString(R.string.save_measurement_success), Snackbar.LENGTH_SHORT).show();
//    }
//
//    public void showSaveError() {
//        Log.e("tag", "测量结果保存出错");
//    }
//
//    public void setLoadingIndicator(boolean b) {
//        showDialogLabel = true;
//    }
//
//    private void checkInputValid(String v, String field) {
//        if (TextUtils.isEmpty(v)) showInputError(field);
//    }
//
//    private void showInputError(String field) {
//        Toast.makeText(getActivity(), field + "不能为空", Toast.LENGTH_SHORT).show();
//    }
//
//    public void showScanning() {
//        scanningDialog = new MaterialDialog.Builder(getActivity())
//                .progress(true, 100)
//                .backgroundColor(getResources().getColor(R.color.white))
//                .show();
//    }
//
//    public void handleBleScanException(BleScanException bleScanException) {
//
//        switch (bleScanException.getReason()) {
//            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
//                Toast.makeText(getActivity(), getString(R.string.bluetooth_not_avavilable), Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.BLUETOOTH_DISABLED:
//                Toast.makeText(getActivity(), getString(R.string.bluetooth_disabled), Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.LOCATION_PERMISSION_MISSING:
//                Toast.makeText(getActivity(),
//                        "On Android 6.0 location permission is required. Implement Runtime Permissions", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.LOCATION_SERVICES_DISABLED:
//                Toast.makeText(getActivity(), "Location services needs to be enabled on Android 6.0", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
//                Toast.makeText(getActivity(), "Scan with the same filters is already started", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
//                Toast.makeText(getActivity(), "Failed to register application for bluetooth scan", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
//                Toast.makeText(getActivity(), "Scan with specified parameters is not supported", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
//                Toast.makeText(getActivity(), "Scan failed due to internal error", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
//                Toast.makeText(getActivity(), "Scan cannot start due to limited hardware resources", Toast.LENGTH_SHORT).show();
//                break;
//            case BleScanException.UNKNOWN_ERROR_CODE:
//            case BleScanException.BLUETOOTH_CANNOT_START:
//            default:
//                Toast.makeText(getActivity(), "Unable to start scanning", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//    public void showStartReceiveData() {
////        Snackbar.make(getView(), "Notifications has been set up", Snackbar.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 显示蓝牙扫描到的所有结果
//     *
//     * @param result
//     */
//    public void handleScanResult(ScanResult result) {
//        scanningDialog.dismiss();
//        RxBleDevice device = result.getBleDevice();
//        scanResultsAdapter = new ScanResultsAdapter(this, bleDeviceList);
//
//        if (showDialogLabel) {
//            showScanDialog();
//            showDialogLabel = false;
//        }
//
//        if (!rxBleDeviceAddressList.contains(device.getMacAddress())) {
//            rxBleDeviceAddressList.add(device.getMacAddress());
//            bleDeviceList.add(new BleDevice(device.getName(), device.getMacAddress(), result.getRssi()));
//            scanResultsAdapter.notifyDataSetChanged();
//        }
//    }
//
//    private void showScanDialog() {
//        if (scanResutlDialog == null) {
//            scanResutlDialog = new MaterialDialog.Builder(getActivity())
//                    .title(R.string.choose_device_prompt)
//                    .backgroundColor(getResources().getColor(R.color.white))
//                    .titleColor(getResources().getColor(R.color.scan_result_list_title))
//                    .dividerColor(getResources().getColor(R.color.divider));
//        }
//        scanResutlDialog.adapter(scanResultsAdapter, null).show();
//        //选择目的蓝牙设备
//        scanResultsAdapter.setOnAdapterItemClickListener(v -> {
////                    scanResutlDialog =null; // FIXME: 2017/8/23 点击所选择的蓝牙设备后，蓝牙设备对话框关闭
//                }
//        );
//    }
//
//    /**
//     * 扫描结束，显示扫描结果
//     * TODO 扫描不停止
//     */
//    public void finishScan() {
////        scanToggleBtn.setText(getString(R.string.scan_finished));
////        scanToggleBtn.setEnabled(false);
//    }
//
//    public void updateButtonUIState() {
////        resultsAdapter.clearScanResults();
////        scanToggleBtn.setText(mPresenter.isScanning() ? getString(R.string.stop_scan) : getString(R.string.start_scan));
//    }
//
//    public void showUnknownError() {
//        Snackbar.make(getView(), getString(R.string.unKnownError), Snackbar.LENGTH_SHORT).show();
//    }
//
//    public void showUnknownError(String s) {
//        Snackbar.make(getView(), s, Snackbar.LENGTH_SHORT).show();
//    }
//
//    public void updateMeasureData(float length, int angle, int battery) {
////        rulerBattery.setText(battery + "%");
////        rulerState.setTextColor(getResources().getColor(R.color.green));
////        if (sexRadioGroup.getCheckedRadioButtonId() == radioMale.getId()) {
////            for (TableRow row : maleRows) {
////                if (assignValue(length, angle, row)) break;
////            }
////        } else {
////            for (TableRow row : femaleRows) {
////                if (assignValue(length, angle, row)) break;
////            }
////        }
//    }
//
//    /**
//     * 结果赋值，有几个字段需要的结果为角度
//     *
//     * @param length 长度
//     * @param row    行
//     * @return boolean
//     */
//    private boolean assignValue(float length, float angle, TableRow row) {
////        EditText editText = (EditText) row.getChildAt(1);
////        if (TextUtils.isEmpty(editText.getText().toString())) {// TODO: 2017/8/24 修改赋值
////            String tag = (String) editText.getTag();
////            String cn;
////            try {
////                Part part = (Part) Class.forName(PART_PACKAGE + "." + tag).newInstance();
////                cn = part.getCn();
////                String value;
////                if (angleList.contains(tag)) {
////                    editText.setText(angle + "");
////                    value = angle + "";
////                } else {
////                    editText.setText(length + "");
////                    value = length + "";
////                }
////                if (speechSynthesizer != null) {
////                    String result = cn + "，结果为" + value;
////                    String[] nextString;
////
////                    if (sexRadioGroup.getCheckedRadioButtonId() == radioMale.getId()) {
////                        maleMeasureSequence = getResources().getStringArray(R.array.male_items_sequence);
////                        nextString = getNextString(cn, maleMeasureSequence);
////                    } else {
////                        nextString = getNextString(cn, getResources().getStringArray(R.array.items_sequence));
////                    }
////                    if (!TextUtils.isEmpty(nextString[0]))
////                        speechSynthesizer.playText(result + "      下一个测量部位" + nextString[0]);
////                    if (!TextUtils.isEmpty(nextString[1]))
////                        speechSynthesizer.playText(result + nextString[1]);
////                }
////            } catch (ClassNotFoundException e) {
////                e.printStackTrace();
////            } catch (java.lang.InstantiationException e) {
////                e.printStackTrace();
////            } catch (IllegalAccessException e) {
////                e.printStackTrace();
////            }
////            return true;
////        }
//        return false;
//    }
//
//    /**
//     * 获取下一个测量字段
//     *
//     * @param cn     当前字段
//     * @param arrays 字段数组
//     * @return 包含结果的数组
//     */
//    private String[] getNextString(String cn, String[] arrays) {
//        String last = null;
//        String next = null;
//        String[] strings = new String[2];
//        for (int m = 0, l = arrays.length; m < l; m++) {
//            if (arrays[m].equals(cn)) {
//                if (m == l - 1) {
//                    last = "所有部位测量完成";
//                } else {
//                    next = arrays[m + 1];
//                }
//            }
//        }
//        strings[0] = next;
//        strings[1] = last;
//        return strings;
//    }
//
//    public void bleDeviceMeasuring() {
//        maleMeasureSequence = getResources().getStringArray(R.array.male_items_sequence);
//        speechSynthesizer.playText("请先选择待测人员性别，首先测量部位" + maleMeasureSequence[0]);
////        measureButton.setText(getString(R.string.measuring));
////        measureButton.setTextColor(getResources().getColor(R.color.measuring));
//    }
//
//    public void showConnected() {
//        speechSynthesizer.playText("蓝牙连接成功，点击开始测量按钮启动测量");
////        rulerState.setText(mPresenter.isConnected() ? getString(R.string.connected) : getString(R.string.disconnected));
////        rulerState.setTextColor(getResources().getColor(R.color.ble_connected));
//    }
//}
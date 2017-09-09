package com.npclo.imeasurer.main.measure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.measure.item.parts.Part;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.main.home.HomeFragment;
import com.npclo.imeasurer.utils.MeasureStateEnum;
import com.npclo.imeasurer.utils.views.MyTextView;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;

import static android.content.Context.MODE_PRIVATE;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MeasureFragment extends BaseFragment implements MeasureContract.View {
    private static final String TAG = MeasureFragment.class.getSimpleName();
    @BindView(R.id.base_toolbar_title)
    TextView baseToolbarTitle;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.wechat_icon)
    ImageView wechatIcon;
    @BindView(R.id.wechat_nickname)
    TextView wechatNickname;
    @BindView(R.id.wechat_name)
    TextView wechatName;
    @BindView(R.id.wechat_gender)
    TextView wechatGender;
    @BindView(R.id.camera_add)
    RelativeLayout cameraAdd;
    @BindView(R.id.save_measure_result)
    AppCompatButton saveMeasureResult;
    @BindView(R.id.measure_table_layout)
    GridView gridView;
    @BindView(R.id.wechat_gender_edit)
    LinearLayout gender_line;
    Unbinder unbinder;
    private MeasureContract.Presenter measurePresenter;
    private WechatUser user;
    private SpeechSynthesizer speechSynthesizer;
    private String PART_PACKAGE = Part.class.getPackage().getName();
    private String ITEM_PACKAGE = MeasurementItem.class.getPackage().getName();
    private String[] measureSequence;
    private List<String> angleList;
    private List<Part> partList = new ArrayList<>();
    private MaterialDialog saveProgressbar;

    public static MeasureFragment newInstance() {
        return new MeasureFragment();
    }

    @Override
    public void setPresenter(MeasureContract.Presenter presenter) {
        measurePresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FIXME is it right
        Bundle bundle = getArguments();
        String s = (String) bundle.get("user");
        try {
            user = new Gson().fromJson(s, WechatUser.class);
        } catch (JsonSyntaxException e) {
            showToast("二维码格式不正确");
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.measure_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        try {
            wechatNickname.setText(user.getNickname());
            wechatGender.setText(user.getSex() == 0 ? "男" : "女");
            wechatName.setText("微信号：" + user.getName());
            Glide.with(this).load(user.getAvatar()).into(wechatIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initToolbar();
        //渲染测量部位列表
        initMeasureItemList();
        ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.measure_item, (ArrayList<Part>) partList);
        gridView.setAdapter(adapter);// TODO: 2017/9/4 使用RecyclerView替代
        // FIXME: 2017/9/8 notifyItemChanged 部分绑定
        gridView.setOnItemClickListener((AdapterView<?> var1, View view, int position, long var4) -> {
            resetTextViewClickState();
            MyTextView textView = (MyTextView) ((LinearLayout) view).getChildAt(0);
            String cn = textView.getText().toString();
            //att 只有处于已测量的部位才能修改，未测量部位不能修改
            if (textView.getState() == MeasureStateEnum.MEASURED.ordinal()) {
                textView.setTextColor(getResources().getColor(R.color.modifying));
                textView.setState(MeasureStateEnum.MODIFYING.ordinal());
                speechSynthesizer.playText("重新测量部位" + cn);
            }
        });
    }

    // FIXME: 2017/9/8 遍历 更好的方式
    //att 针对误点击进行修改操作，重置所有的处于修改状态的textview为非修改状态
    private void resetTextViewClickState() {
        for (int i = 0, count = gridView.getCount(); i < count; i++) {
            MyTextView textView = (MyTextView) ((LinearLayout) gridView.getChildAt(i)).getChildAt(0);
            if (textView.getState() == MeasureStateEnum.MODIFYING.ordinal()) {
                textView.setState(MeasureStateEnum.MEASURED.ordinal());
                textView.setTextColor(getResources().getColor(R.color.measured));
            }
        }
    }

    private void initToolbar() {
        baseToolbarTitle.setText("量体");
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(__ -> {
            start(HomeFragment.newInstance(), SINGLETASK);
            pop();
        });
        baseToolbar.inflateMenu(R.menu.base_toolbar_menu);
        baseToolbar.getMenu().getItem(0).setIcon(R.mipmap.redact);
        baseToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_menu) {
                Toast.makeText(getActivity(), "启用编辑状态", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] angleItems = getResources().getStringArray(R.array.angle_items);
        angleList = Arrays.asList(angleItems);
        initSpeech();
        measurePresenter.subscribe();
        RxBleDevice bleDevice = BaseApplication.getRxBleDevice(getActivity());
        if (bleDevice != null && bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
            Log.e(TAG, "获取到蓝牙状态" + bleDevice.toString());
            //启动测量
            try {
                UUID characteristicUUID = BaseApplication.getUUID(getActivity());
                Observable<RxBleConnection> connectionObservable = BaseApplication.getConnection(getActivity());
                measurePresenter.startMeasure(characteristicUUID, connectionObservable);
            } catch (Exception e) {
                showToast("蓝牙连接异常，请重新连接！");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        measurePresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        speechSynthesizer = null;
    }

    @OnClick({R.id.save_measure_result, R.id.wechat_gender_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wechat_gender_edit:
                switchGender(view);
                break;
            case R.id.save_measure_result:
                handleSaveData();
                break;
        }
    }

    private void switchGender(View view) {
        TextView genderView = view.findViewById(R.id.wechat_gender);
        String gender = genderView.getText().toString();
        int index = 0;
        if (gender.equals("女")) {
            index = 1;
        }
        //颜色状态列表
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        }, new int[]{
                getResources().getColor(R.color.c252527), getResources().getColor(R.color.primary),
        });
        new MaterialDialog.Builder(getActivity())
                .title("修改性别")
                .choiceWidgetColor(sl)
                .titleColor(getResources().getColor(R.color.c252527))
                .items(R.array.genders)
                .contentColor(getResources().getColor(R.color.c252527))
                .itemsCallbackSingleChoice(index, (dialog, itemView, which, text) -> {
                    genderView.setText(text);
                    return true;
                })
                .backgroundColor(getResources().getColor(R.color.white))
                .positiveText(R.string.sure)
                .show();
    }

    private void handleSaveData() {
        try {
            Class Class2 = Class.forName(ITEM_PACKAGE + ".MeasurementItem");
            MeasurementItem item = (MeasurementItem) Class2.newInstance();
            for (int i = 0, count = gridView.getCount(); i < count; i++) {
                MyTextView textView = (MyTextView) ((LinearLayout) gridView.getChildAt(i)).getChildAt(0);
                if (textView.getState() == MeasureStateEnum.UNMEASUED.ordinal()) {
                    showToast("量体未完成");
                    break;
                }
                float value = textView.getValue();
                String cn = textView.getText().toString();
                String en = textView.getTag().toString();
                Class<?> aClass = Class.forName(PART_PACKAGE + "." + en);
                Part part = (Part) aClass.newInstance();
                part.setValue(value + "");
                part.setCn(cn);
                part.setEn(en);
                Method method = Class2.getMethod("set" + en, aClass);
                method.invoke(item, part);
            }
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
            String id = sharedPreferences.getString("id", null);
            if (TextUtils.isEmpty(id)) {
                showToast("账号异常，请重新登录");
                startActivity(new Intent(getActivity(), AccountActivity.class));
                return;
            }
            Measurement measurement = new Measurement(user, item, id);
            measurePresenter.saveMeasurement(measurement);
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

    private void initSpeech() {
        String APPKEY = "hhzjkm3l5akcz5oiflyzmmmitzrhmsfd73lyl3y2";
        String APPSECRET = "29aa998c451d64d9334269546a4021b8";
        if (speechSynthesizer == null)
            speechSynthesizer = new SpeechSynthesizer(getActivity(), APPKEY, APPSECRET);
        speechSynthesizer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_NET);
        speechSynthesizer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 70);
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

    private void initMeasureItemList() {
        try {
            MeasurementItem item = (MeasurementItem) Class.forName(ITEM_PACKAGE + ".MeasurementItem").newInstance();
            Field[] declaredFields = item.getClass().getDeclaredFields();
            List<String> nameList = new ArrayList<>();
            for (Field field : declaredFields) {
                String name = field.getName();
                nameList.add(name);
            }

            String[] objects = new String[nameList.size()];
            String[] strings = nameList.toArray(objects);
            Arrays.sort(strings);
            //att 循环添加单行
            for (String name : strings) {
                Class<?> itemSubclass = Class.forName(PART_PACKAGE + "." + name);
                Part part = (Part) itemSubclass.newInstance();
                partList.add(new Part(part.getCn(), part.getEn()));
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleError(Throwable e) {
        handleError(e, TAG);
    }

    @Override
    public void showStartReceiveData() {
        // TODO: 2017/9/7
    }

    @Override
    public void bleDeviceMeasuring() {
        measureSequence = getResources().getStringArray(R.array.items_sequence);
        speechSynthesizer.playText("请确定待测人员性别，首先测量部位" + measureSequence[0]);
    }

    @Override
    public void handleMeasureData(float length, float angle, int battery) {
//        if (battery < 20) speechSynthesizer.playText("电量低");
        // FIXME: 2017/9/8 赋值方式
        for (int i = 0, count = gridView.getChildCount(); i < count; i++) {
            MyTextView textView = (MyTextView) ((LinearLayout) gridView.getChildAt(i)).getChildAt(0);
            int state = textView.getState();
            if (state == MeasureStateEnum.UNMEASUED.ordinal()) {
                assignValue(length, angle, textView, 0);
                break;
            } else if (state == MeasureStateEnum.MODIFYING.ordinal()) {
                assignValue(length, angle, textView, 1);
                break;
            }
//            else {
//                speechSynthesizer.playText("当前测量结束");// TODO: 2017/9/8  可修改 or 直接提交提示
//                break;
//            }
        }
    }

    @Override
    public void showSuccessSave() {
        showToast("保存成功");
        // TODO: 2017/9/8 下一个人量体
    }

    @Override
    public void showSaveError(Throwable e) {
        handleError(e, TAG);
    }

    @Override
    public void showSaveCompleted() {
        showLoading(false);
    }

    @Override
    public void showLoading(boolean bool) {
        if (bool) {
            saveProgressbar = new MaterialDialog.Builder(getActivity())
                    .progress(true, 100)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
        } else {
            saveProgressbar.dismiss();
        }
    }

    /**
     * 结果赋值，有几个字段需要的结果为角度
     *
     * @param length
     * @param angle
     * @param textView
     * @param type
     */
    private void assignValue(float length, float angle, MyTextView textView, int type) {
        String tag = (String) textView.getTag();
        String cn;
        try {
            Part part = (Part) Class.forName(PART_PACKAGE + "." + tag).newInstance();
            cn = part.getCn();
            String value;//播报的测量结果
            if (angleList.contains(tag)) { //按要求赋值
                textView.setValue(angle);
                value = angle + "";
            } else {
                textView.setValue(length);
                value = length + "";
            }
            textView.setState(MeasureStateEnum.MEASURED.ordinal());//更新状态
            textView.setTextColor(getResources().getColor(R.color.measured));//修改颜色
            if (speechSynthesizer != null) {
                if (type == 1) { //修改原有结果
                    String result = cn + "，重新测量结果为" + value;// TODO: 2017/9/8 播报下一个测量部位
                    speechSynthesizer.playText(result);
                } else {
                    String result = cn + value;
                    String[] nextString;
                    nextString = getNextString(cn, measureSequence);
                    if (!TextUtils.isEmpty(nextString[0]))
                        speechSynthesizer.playText(result + "        请测" + nextString[0]);
                    if (!TextUtils.isEmpty(nextString[1]))
                        speechSynthesizer.playText(result + nextString[1]);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取下一个测量字段
     *
     * @param cn     当前字段
     * @param arrays 字段数组
     * @return 包含结果的数组
     */
    private String[] getNextString(String cn, String[] arrays) {
        String last = null;
        String next = null;
        String[] strings = new String[2];
        for (int m = 0, l = arrays.length; m < l; m++) {
            if (arrays[m].equals(cn)) {
                if (m == l - 1) {
                    last = "所有部位测量完成";
                } else {
                    next = arrays[m + 1];
                }
            }
        }
        strings[0] = next;
        strings[1] = last;
        return strings;
    }
}
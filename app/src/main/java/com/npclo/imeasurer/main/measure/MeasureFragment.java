package com.npclo.imeasurer.main.measure;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.measure.item.parts.Part;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.main.home.HomeFragment;
import com.npclo.imeasurer.utils.views.MyTextView;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;

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
        user = new Gson().fromJson(s, WechatUser.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.measure_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        wechatNickname.setText(user.getNickname());
        wechatGender.setText(user.getSex() == 0 ? "男" : "女");
        wechatName.setText("微信号：" + user.getName());
        Glide.with(this).load(user.getAvatar()).into(wechatIcon);
        initToolbar();
        //渲染测量部位列表
        initMeasureItemList();
        ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.measure_item, (ArrayList<Part>) partList);
        gridView.setAdapter(adapter);// TODO: 2017/9/4 使用RecyclerView替代
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
                break;
        }
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
    public void handleMeasureData(float length, int angle, int battery) {
        if (battery < 20) speechSynthesizer.playText("电量低");// TODO: 2017/9/7
        int count = gridView.getChildCount();
        for (int i = 0; i < count; i++) {
            if (assignValue(length, angle, (LinearLayout) gridView.getChildAt(i))) break;
        }
    }

    /**
     * 结果赋值，有几个字段需要的结果为角度
     *
     * @param length       长度
     * @param linearLayout 行
     * @return boolean
     */
    private boolean assignValue(float length, float angle, LinearLayout linearLayout) {
        MyTextView textView = (MyTextView) linearLayout.getChildAt(0);
        float tv_value = textView.getValue(); //att ? 有值36.0????
        if (TextUtils.isEmpty(textView.getValue() + "")) {
            String tag = (String) textView.getTag();
            String cn;
            try {
                Part part = (Part) Class.forName(PART_PACKAGE + "." + tag).newInstance();
                cn = part.getCn();
                String value;//播报的测量结果
                if (angleList.contains(tag)) {
                    textView.setValue(angle);
                    value = angle + "";
                } else {
                    textView.setValue(length);
                    value = length + "";
                }
                textView.setTextColor(getResources().getColor(R.color.measured));//修改颜色
                if (speechSynthesizer != null) {
                    String result = cn + "，结果为" + value;
                    String[] nextString;
                    nextString = getNextString(cn, measureSequence);
                    if (!TextUtils.isEmpty(nextString[0]))
                        speechSynthesizer.playText(result + "      下一个测量部位" + nextString[0]);
                    if (!TextUtils.isEmpty(nextString[1]))
                        speechSynthesizer.playText(result + nextString[1]);
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
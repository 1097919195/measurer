package com.npclo.imeasurer.main.measure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.camera.decode.CaptureActivity;
import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.measure.item.parts.Part;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.main.home.HomeFragment;
import com.npclo.imeasurer.utils.MeasureStateEnum;
import com.npclo.imeasurer.utils.views.MyGridView;
import com.npclo.imeasurer.utils.views.MyTextView;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    AppCompatButton btnSave;
    @BindView(R.id.next_person)
    AppCompatButton btnNext;
    @BindView(R.id.measure_table_layout)
    MyGridView gridView;
    @BindView(R.id.wechat_gender_edit)
    LinearLayout gender_line;
    @BindView(R.id.img_1)
    ImageView img_1;
    @BindView(R.id.img_2)
    ImageView img_2;
    @BindView(R.id.img_3)
    ImageView img_3;
    @BindView(R.id.del_1)
    ImageView del_1;
    @BindView(R.id.del_2)
    ImageView del_2;
    @BindView(R.id.del_3)
    ImageView del_3;
    @BindView(R.id.frame_1)
    FrameLayout frame_1;
    @BindView(R.id.frame_2)
    FrameLayout frame_2;
    @BindView(R.id.frame_3)
    FrameLayout frame_3;
    Unbinder unbinder;
    private MeasureContract.Presenter measurePresenter;
    private WechatUser user;
    private SpeechSynthesizer speechSynthesizer;
    private String PART_PACKAGE = Part.class.getPackage().getName();
    private String ITEM_PACKAGE = MeasurementItem.class.getPackage().getName();
    private List<String> angleList;
    private List<Part> partList = new ArrayList<>();
    private MaterialDialog saveProgressbar;
    private Uri imageUri; //图片路径
    public static final int TAKE_PHOTO = 1003;
    public static final int CROP_PHOTO = 1004;
    private List<FrameLayout> picList = new ArrayList<>();
    private List<MyTextView> unMeasuredList = new ArrayList<>();
    private MyTextView modifyingView;
    private boolean initUmMeasureListFlag;

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
        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_measure;
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
        ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.list_measure_item, (ArrayList<Part>) partList);
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
                modifyingView = textView;//att 正在修改测量值textview赋值
            }
        });
        picList.clear();
        picList.add(frame_1);
        picList.add(frame_1);
        picList.add(frame_1);
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
        initSpeech();
        measurePresenter.subscribe();
        String[] angleItems = getResources().getStringArray(R.array.angle_items);
        angleList = Arrays.asList(angleItems);

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
        initUmMeasureListFlag = true;
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

    @OnClick({R.id.save_measure_result, R.id.wechat_gender_edit, R.id.camera_add, R.id.next_person,
            R.id.del_1, R.id.del_2, R.id.del_3,
            R.id.img_1, R.id.img_2, R.id.img_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wechat_gender_edit:
                switchGender(view);
                break;
            case R.id.save_measure_result:
                handleSaveData();
                break;
            case R.id.next_person:
                measureNextPerson();
                break;
            case R.id.camera_add:
                capturePic();
                break;
            case R.id.del_1:
            case R.id.del_2:
            case R.id.del_3:
                delPic((ImageView) view);
                break;
            case R.id.img_1:
            case R.id.img_2:
            case R.id.img_3:
                preview(((ImageView) view));
                break;
        }
    }

    private void measureNextPerson() {
        int count = gridView.getChildCount();
        for (int i = 0; i < count; i++) {
            LinearLayout linearLayout = (LinearLayout) gridView.getChildAt(i);
            MyTextView textView = (MyTextView) linearLayout.getChildAt(0);
            textView.setState(MeasureStateEnum.UNMEASUED.ordinal());
            textView.setTextColor(getResources().getColor(R.color.unmeasured));
            textView.setValue(0.0f);
        }
        frame_1.setVisibility(View.INVISIBLE);
        frame_2.setVisibility(View.INVISIBLE);
        frame_3.setVisibility(View.INVISIBLE);
        img_1.setImageDrawable(null);
        img_2.setImageDrawable(null);
        img_3.setImageDrawable(null);// FIXME: 2017/9/11
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, 1001);
        btnNext.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
    }

    private void preview(ImageView view) {
        new MaterialDialog.Builder(getActivity())
                .customView(view, false)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    private void delPic(ImageView view) {
        FrameLayout parent = (FrameLayout) view.getParent();
        ImageView img = (ImageView) parent.getChildAt(0);
        img.setImageDrawable(null);// FIXME: 2017/9/11
        parent.setVisibility(View.INVISIBLE);
    }

    private void capturePic() {
        Date date = new Date(System.nanoTime());
        String pic_name;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(date.toString().getBytes());
            pic_name = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            pic_name = date.toString();
            e.printStackTrace();
        }
        //创建File对象用于存储拍照的图片 SD卡根目录
        //File outputImage = new File(Environment.getExternalStorageDirectory(),"test.jpg");
        //存储至DCIM文件夹
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path, pic_name + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将File对象转换为Uri并启动照相程序
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
        startActivityForResult(intent, TAKE_PHOTO); //启动照相
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
        if (unMeasuredList.size() != 0) { //att 校验是否量体完成
            showToast("量体未完成");
            return;
        }
        try {
            Class Class2 = Class.forName(ITEM_PACKAGE + ".MeasurementItem");
            MeasurementItem item = (MeasurementItem) Class2.newInstance();
            for (int i = 0, count = gridView.getCount(); i < count; i++) {
                MyTextView textView = (MyTextView) ((LinearLayout) gridView.getChildAt(i)).getChildAt(0);
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        String[] measureSequence = getResources().getStringArray(R.array.items_sequence);
        speechSynthesizer.playText("请确定待测人员性别，首先测量部位" + measureSequence[0]);
    }

    private void initUnMeasureList() {
        int count = gridView.getChildCount();
        unMeasuredList.clear();
        try {
            for (int i = 0; i < count; i++) {
                MyTextView textView = (MyTextView) ((LinearLayout) gridView.getChildAt(i)).getChildAt(0);
                if (textView.getState() == MeasureStateEnum.UNMEASUED.ordinal())
                    unMeasuredList.add(textView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        unMeasuredList.remove(0);//att 排除第一项
        initUmMeasureListFlag = false;
        Log.e(TAG, "未测量数目" + unMeasuredList.size());
    }

    @Override
    public void handleMeasureData(float length, float angle, int battery) {
//        if (battery < 20) speechSynthesizer.playText("battery_low");
        if (initUmMeasureListFlag) initUnMeasureList();
        // FIXME: 2017/9/8 赋值方式
        //att 先判断是否有正处于修改状态的textview，有的话，先给其赋值，再给下一个未测量的部位赋值
        if (modifyingView != null) {
            assignValue(length, angle, modifyingView, 1);
        } else {
            MyTextView textView = unMeasuredList.get(0);
            if (textView != null) {
                Log.e(TAG, "当前赋值部位：" + textView.getText().toString());
                assignValue(length, angle, textView, 0);
            }
        }
    }

    @Override
    public void showSuccessSave() {
        showToast("保存成功");
        btnSave.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
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
            // FIXME: 2017/9/11
            String s = null;//最终播放文字
            String result;//播报当前测量结果
            String[] strings = getNextString(type);
            if (type == 1) { //修改原有结果
                result = cn + "，重新测量结果为" + value;// TODO: 2017/9/8 播报下一个测量部位
                modifyingView = null;//att 重置待修改项
            } else { //按顺序测量
                result = cn + value;
                unMeasuredList.remove(0);//att 最前的一项测量完毕
            }
            if (!TextUtils.isEmpty(strings[0])) s = result + "        请测" + strings[0];
            if (!TextUtils.isEmpty(strings[1])) s = result + strings[1];
            speechSynthesizer.playText(s);
            Log.e(TAG, "播报" + s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getNextString(int type) {
        String last = null;
        String next = null;
        String[] strings = new String[2];
        if (type == 1) {
            Log.e(TAG, "按中途修改测量，当前未测量数目" + unMeasuredList.size());
            if (unMeasuredList.size() == 0) {
                last = "测量完毕";
            } else {
                next = unMeasuredList.get(0).getText().toString();
            }
        } else {
            Log.e(TAG, "按顺序测量，当前未测量数目" + unMeasuredList.size());
            if (unMeasuredList.size() == 1) {
                last = "测量完毕";
            } else {
                next = unMeasuredList.get(1).getText().toString();
            }
        }
        strings[0] = next;
        strings[1] = last;
        return strings;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("scale", true);
                //设置宽高比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                //设置裁剪图片宽高
                intent.putExtra("outputX", 340);
                intent.putExtra("outputY", 340);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //广播刷新相册
                Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intentBc.setData(imageUri);// FIXME: 2017/9/11
                getActivity().sendBroadcast(intentBc);
                startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至ImageView
                break;
            case CROP_PHOTO:
                try {
                    //图片解析成Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().
                            getContentResolver()
                            .openInputStream(imageUri));
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.2f, 0.2f);
                    Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);// FIXME: 2017/9/11 bitmap为空
                    bitmap.recycle();
                    for (FrameLayout frameLayout : picList) {
                        if (frameLayout.getVisibility() == View.INVISIBLE) {
                            ImageView img = (ImageView) frameLayout.getChildAt(0);
                            img.setImageBitmap(bm); //将剪裁后照片显示出来
                            frameLayout.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
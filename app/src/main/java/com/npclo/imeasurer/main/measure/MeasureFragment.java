package com.npclo.imeasurer.main.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.measure.item.parts.Part;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.utils.DensityUtil;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MeasureFragment extends BaseFragment implements MeasureContract.View {
    private static final float TEXT_VIEW_HEIGHT = 42;
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
        baseToolbarTitle.setText("量体");
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.inflateMenu(R.menu.base_toolbar_menu);
        baseToolbar.getMenu().getItem(0).setIcon(R.mipmap.redact);
        //渲染测量部位列表
        initMeasureItemList();
        ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.measure_item, (ArrayList<Part>) partList);
        gridView.setAdapter(adapter);// TODO: 2017/9/4 使用RecyclerView替代
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] angleItems = getResources().getStringArray(R.array.angle_items);
        angleList = Arrays.asList(angleItems);
        initSpeech();
        measurePresenter.subscribe();
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

    @OnClick(R.id.save_measure_result)
    public void onViewClicked() {
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

    private LinearLayout getMeasureItem(String cn, String en) {
        FragmentActivity context = getActivity();

        LinearLayout item = new LinearLayout(context);
        int width = DensityUtil.dp2px(getActivity(), 187);
        int height = DensityUtil.dp2px(getActivity(), TEXT_VIEW_HEIGHT);
        item.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(getResources().getColor(R.color.unmeasured));
//        textView.setHeight(DensityUtil.dp2px(context, DensityUtil.dp2px(context, TEXT_VIEW_HEIGHT)));
//        textView.setPadding(0, 3, 0, 3);
        textView.setText(cn);

        EditText editText = new EditText(context);
        editText.setTag(en);
        editText.setVisibility(View.GONE);

        item.addView(textView);
        item.addView(editText);

        return item;
    }
}
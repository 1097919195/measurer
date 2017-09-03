package com.npclo.imeasurer.main.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.wuser.WechatUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MeasureFragment extends BaseFragment implements MeasureContract.View {
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
    LinearLayout cameraAdd;
    @BindView(R.id.save_measure_result)
    AppCompatButton saveMeasureResult;
    Unbinder unbinder;
    private MeasureContract.Presenter measurePresenter;
    private WechatUser user;


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
        return R.layout.measure_new_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        wechatNickname.setText(user.getNickname());
        wechatGender.setText(user.getSex() == 0 ? "男" : "女");
        wechatName.setText("微信号：" + user.getName());
        Glide.with(this).load(user.getAvatar()).into(wechatIcon);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @OnClick(R.id.save_measure_result)
    public void onViewClicked() {
    }
}

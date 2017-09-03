package com.npclo.imeasurer.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.zxing.client.android.decode.CaptureActivity;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.main.measure.MeasureFragment;
import com.npclo.imeasurer.main.measure.MeasurePresenter;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    @BindView(R.id.scan_img)
    ImageView scanImg;
    @BindView(R.id.scan_hint)
    TextView scanHint;
    @BindView(R.id.base_toolbar)
    Toolbar toolbarBase;
    Unbinder unbinder;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeContract.Presenter mPresenter;
    private MaterialDialog dialog;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        initToolbar();
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
        }
    }

    private void startScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        String result = bundle.getString("result");
        Log.e(TAG, result);
        switch (resultCode) {
            case 1:
                toMeasure(result);
                break;
            case 2:
                mPresenter.getUserInfoWithCode(result);
                break;
        }
    }

    /**
     * 初始化toolbar的一些默认属性
     */
    protected void initToolbar() {
//        ((MainActivity) getActivity()).setSupportActionBar(toolbarBase);
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.toolbar_text));//设置主标题颜色
        toolbarBase.inflateMenu(R.menu.base_toolbar_menu);
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

    @Override
    public void showLoading(boolean b) {
        if (b) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .progress(true, 100)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void showGetInfoSuccess(WechatUser user) {
        showLoading(false);
        String s = new Gson().toJson(user);
        toMeasure(s);
    }

    private void toMeasure(String s) {
        MeasureFragment measureFragment = findFragment(MeasureFragment.class);
        if (measureFragment == null) {
            measureFragment = MeasureFragment.newInstance();
            Bundle bundle = new Bundle();
            //TODO for test
            s = "{\"openID\":\"oaym60wV0nUG2KUmD84enwm5CAzE\",\"gender\":0,\"height\":\"170\"," +
                    "\"weight\":\"60\",\"name\":\"MapleImage\",\"nickname\":\"MapleImage\"," +
                    "\"avatar\":\"http:\\/\\/wx.qlogo.cn\\/mmopen\\/vi_32\\/icBY913leXwE7HP1pqlQnt7sJJwLL" +
                    "VguLicMTvPYsDUHRtp0qNuKHrpPeUJJgNpsfxg62k5ENTl8NPhsiceWRKMSg\\/0\"}";

            bundle.putString("user", s);
            measureFragment.setArguments(bundle);
            new MeasurePresenter(measureFragment, SchedulerProvider.getInstance());
            start(measureFragment, SINGLETASK);
        }
    }

    @Override
    public void showGetInfoError(Throwable e) {
        super.handleError(e, TAG);
    }

    @Override
    public void showCompleteGetInfo() {
        showLoading(false);
    }
}